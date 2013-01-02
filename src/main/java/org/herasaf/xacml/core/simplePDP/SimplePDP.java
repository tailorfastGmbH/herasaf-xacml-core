/*
 * Copyright 2008 - 2012 HERAS-AF (www.herasaf.org)
 * Holistic Enterprise-Ready Application Security Architecture Framework
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.herasaf.xacml.core.simplePDP;

import org.herasaf.xacml.core.InitializationException;
import org.herasaf.xacml.core.api.OrderedPolicyRepository;
import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.PIP;
import org.herasaf.xacml.core.api.PolicyRetrievalPoint;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyOrderedCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyUnorderedCombiningAlgorithm;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.ResponseCtx;
import org.herasaf.xacml.core.context.ResponseCtxFactory;
import org.herasaf.xacml.core.context.ResponseMarshaller;
import org.herasaf.xacml.core.context.StatusCodeComparator;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResponseType;
import org.herasaf.xacml.core.targetMatcher.TargetMatcher;
import org.herasaf.xacml.core.types.Date;
import org.herasaf.xacml.core.types.DateTime;
import org.herasaf.xacml.core.types.Time;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * This class is a <i>simple</i> (easy to use) implementation of a {@link PDP}.<br />
 * The PDP needs an initialized JAXB environment (Functions, DataTypes, CombiningAlgorithms etc.) and a
 * PolicyRepository. A PIP can optionally be plugged in.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class SimplePDP implements PDP {
	private final PolicyRetrievalPoint policyRepository;
	private final PIP pip;
	private final PolicyCombiningAlgorithm rootPolicyCombiningAlgorithm;
	private final TargetMatcher targetMatcher;
	private final boolean respectAbandonedEvaluatables;
	private transient final Logger logger = LoggerFactory.getLogger(SimplePDP.class);
	private static final String MDC_REQUEST_TIME = "org:herasaf:request:xacml:evaluation:requesttime";
	private final StatusCodeComparator statusCodeComparator;

	/**
	 * Initializes the PDP with the given {@link SimplePDPConfiguration}.
	 * 
	 * @param simplePDPConfiguration
	 *            The {@link SimplePDPConfiguration} to use.
	 * 
	 */
	public SimplePDP(SimplePDPConfiguration simplePDPConfiguration) {
		this.rootPolicyCombiningAlgorithm = simplePDPConfiguration.getRootCombiningAlgorithm();
		this.policyRepository = simplePDPConfiguration.getPolicyRetrievalPoint();

		/*
		 * Checks if the policy repository and the root combining algorithm are both of the same type. The type is
		 * either ordered or unordered (XOR).
		 */
		if (!((PolicyOrderedCombiningAlgorithm.class.isInstance(this.rootPolicyCombiningAlgorithm) && OrderedPolicyRepository.class
				.isInstance(this.policyRepository)) ^ PolicyUnorderedCombiningAlgorithm.class
				.isInstance(this.rootPolicyCombiningAlgorithm))) {
			InitializationException ie = new InitializationException(
					"Root combining algorithm and policy repository are not of the same type "
							+ "(type is either ordered or unordered).");
			logger.error(ie.getMessage());
			throw ie;
		}

		this.respectAbandonedEvaluatables = simplePDPConfiguration.isRespectAbandonedEvaluatables();
		this.pip = simplePDPConfiguration.getPip();
		this.targetMatcher = simplePDPConfiguration.getTargetMatcher();
		this.statusCodeComparator = simplePDPConfiguration.getStatusCodeComparator();

		if (pip == null) {
			logger.warn("No PIP is set. Attributes that are not present in the request cannot be resolved.");
		}

		// Set the timezone to use for all time related stuff within the PDP.
		DateTimeZone.setDefault(simplePDPConfiguration.getTimeZone());

		// Set whether Zulu representation ('Z') or +00:00 shall be used as UTC timezone representation.
		boolean useZuluUtcRepresentation = simplePDPConfiguration.isZuluUtcRepresentation();
		DateTime.useZuluUtcRepresentation(useZuluUtcRepresentation);
		Date.useZuluUtcRepresentation(useZuluUtcRepresentation);
		Time.useZuluUtcRepresentation(useZuluUtcRepresentation);
	}

	/**
	 * {@inheritDoc}
	 */
	public PolicyRetrievalPoint getPolicyRepository() {
		return policyRepository;
	}

	/**
	 * {@inheritDoc}
	 */
	public PIP getPIP() {
		return pip;
	}

	/**
	 * {@inheritDoc}
	 */
	public PolicyCombiningAlgorithm getRootCombiningAlgorithm() {
		return rootPolicyCombiningAlgorithm;
	}

	/**
	 * {@inheritDoc} <br />
	 * <br />
	 * <b>Logging:</b><br />
	 * This section is relevant for all users of the {@link SimplePDP} in a multi-threaded environment. All logging
	 * messages during the evaluation should be connected with a correlation ID to be able to distinguish the different
	 * requesters. Due to the fact that this connection lays with the requester here is a hint how this could be
	 * realized with the SLF4J Logging Framework (<a href="http://www.slf4j.org">http://www.slf4j.org</a>) used here if
	 * the underlying logging framework (such as logback) supports MDC (Mapped Diagnostic Context).<br />
	 * The MDC (Mapped Diagnostic Context) shall be used to distinguish the different requesters as described here: <a
	 * href="http://logback.qos.ch/manual/mdc.html" >http://logback.qos.ch/manual/mdc.html</a>.<br />
	 * See the description in the Getting Started Guide on the HERAS<sup>AF</sup> Wiki of how to configure MDC.
	 * 
	 * @deprecated Use {@link #evaluate(RequestType)} instead.
	 */
	@Deprecated
	public ResponseCtx evaluate(RequestCtx request) {
		return new ResponseCtx(evaluate(request.getRequest()));
	}

	/**
	 * {@inheritDoc} <br />
	 * <br />
	 * <b>Logging:</b><br />
	 * This section is relevant for all users of the {@link SimplePDP} in a multi-threaded environment. All logging
	 * messages during the evaluation should be connected with a correlation ID to be able to distinguish the different
	 * requesters. Due to the fact that this connection lays with the requester here is a hint how this could be
	 * realized with the SLF4J Logging Framework (<a href="http://www.slf4j.org">http://www.slf4j.org</a>) used here if
	 * the underlying logging framework (such as logback) supports MDC (Mapped Diagnostic Context).<br />
	 * The MDC (Mapped Diagnostic Context) shall be used to distinguish the different requesters as described here: <a
	 * href="http://logback.qos.ch/manual/mdc.html" >http://logback.qos.ch/manual/mdc.html</a>.<br />
	 * See the description in the Getting Started Guide on the HERAS<sup>AF</sup> Wiki of how to configure MDC.
	 */
	public ResponseType evaluate(RequestType request) {
		MDC.put(MDC_REQUEST_TIME, String.valueOf(System.currentTimeMillis()));
		logger.debug("Evaluating Request: {}", request);
		
		request.ensureThatCreationTimeIsSet();

		EvaluationContext evaluationContext = new EvaluationContext(targetMatcher, pip, respectAbandonedEvaluatables,
				statusCodeComparator);

		/*
		 * Checks whether the request is a valid XACML request concering the number of resources within the request.
		 * Standard XACML only allows a single resource per request. For multiple resource the context handler is in
		 * charge. See the XACML multiple resources profile for further information.
		 */
		if (!containsOnlyOneResource(request)) {
			logger.error("The request must not contain multiple resources.");
			return createResponse(request, DecisionType.INDETERMINATE, evaluationContext);
		}

		DecisionType decision = rootPolicyCombiningAlgorithm.evaluateEvaluatableList(request,
				policyRepository.getEvaluatables(request), evaluationContext);

		MDC.remove(MDC_REQUEST_TIME);
		return createResponse(request, decision, evaluationContext);
	}

	/**
	 * This method uses the default HERAS-AF {@link ResponseCtxFactory}. This method may be overriden in an extending
	 * subclass.
	 * 
	 * @param request
	 *            The requests corresponding to the response to create.
	 * @param decision
	 *            The decision of the response.
	 * @param evaluationContext
	 *            The evaluation context of this evaluation.
	 * @return The created {@link ResponseCtx}.
	 * 
	 * @deprecated Use {@link #createResponse(RequestType, DecisionType, EvaluationContext)} instead.
	 */
	@Deprecated
	protected ResponseCtx createResponse(RequestCtx request, DecisionType decision, EvaluationContext evaluationContext) {
		return ResponseCtxFactory.create(request.getRequest(), decision, evaluationContext);
	}

	/**
	 * This method uses the default HERAS-AF {@link ResponseCtxFactory}. This method may be overriden in an extending
	 * subclass.
	 * 
	 * @param request
	 *            The requests corresponding to the response to create.
	 * @param decision
	 *            The decision of the response.
	 * @param evaluationContext
	 *            The evaluation context of this evaluation.
	 * @return The created {@link ResponseType}.
	 */
	protected ResponseType createResponse(RequestType request, DecisionType decision,
			EvaluationContext evaluationContext) {
		return ResponseMarshaller.create(request, decision, evaluationContext);
	}

	/**
	 * Checks whether the request is a valid XACML request concering the number of resources within the request.
	 * Standard XACML only allows a single resource per request. For multiple resource the context handler is in charge.
	 * See the XACML multiple resources profile for further information.
	 * 
	 * @param request
	 *            The request to be checked.
	 * @return True if the request contains multiple resources, false otherwise.
	 */
	private boolean containsOnlyOneResource(RequestType request) {
		// All mentioned sections in this method are related to:
		// Multiple resource profile of XACML v2.0 (OASIS Standard, 1 February
		// 2005).
		if (request.getResources().size() > 1) {
			logger.error("The request must not contain more than one <Resource> elements.");
			// See Section 2.3.
			return false;
		} else if (request.getResources().size() == 1) {
			for (AttributeType attr : request.getResources().get(0).getAttributes()) {
				if ("urn:oasis:names:tc:xacml:2.0:resource:scope".startsWith(attr.getAttributeId())) {
					// Is true in case the resource contains either an
					// urn:oasis:names:tc:xacml:2.0:profile:multiple:scope:xml
					// or an
					// urn:oasis:names:tc:xacml:2.0:profile:multiple:scope:non-xml
					// attribute.
					if ("Immediate".equals(attr.getAttributeValues().get(0))) {
						// This attribute value is present if only one resource
						// is in the request.
						// See Section 4.1.
						return true;
					} else if ("Children".equals(attr.getAttributeValues().get(0))) {
						logger.error("The request must not request a decision for multiple resources.");
						// The set of resources consists of a single node
						// described by the â€œresource-idâ€� resource
						// attribute
						// and of all that node's immediate children in the
						// hierarchy.
						// See Section 4.1.
						return false;
					} else if ("Descendants".equals(attr.getAttributeValues().get(0))) {
						logger.error("The request must not request a decision for multiple resources.");
						// The set of resources consists of a single node
						// described by the â€œresource-idâ€� resource
						// attribute
						// and of all that node's descendants in the hierarchy.
						// See Section 4.1.
						return false;
					} else if ("XPath-expression".equals(attr.getAttributeValues().get(0))) {
						logger.error("The request must not request a decision for multiple resources.");
						// The set of resources consists of the nodes in a
						// nodeset described by the â€œresource-idâ€�
						// resource attribute.
						// See Section 2.2.
						// See Section 4.1.
						return false;
					} else if ("EntireHierarchy".equals(attr.getAttributeValues().get(0))) {
						logger.error("The request must not request a decision for multiple resources.");
						// The resource consists of a node described by the
						// â€œresource-idâ€� resource attribute
						// along with all that node's descendants.
						// See Section 3.1.
						// See Section 4.1.
						return false;
					}
				}
			}
			// Only one resource is in the request.
			return true;
		} else {
			// No resource element is present. This is equal to "any resource".
			return true;
		}
	}
	
}