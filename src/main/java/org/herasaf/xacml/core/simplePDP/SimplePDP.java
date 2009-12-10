/*
 * Copyright 2008 HERAS-AF (www.herasaf.org)
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

import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.PIP;
import org.herasaf.xacml.core.api.PolicyRepository;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.ResponseCtx;
import org.herasaf.xacml.core.context.ResponseCtxFactory;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * TODO JAVADOC!!!!!!!!!! The implementation of the {@link PDP}. <br>
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class SimplePDP implements PDP {
	private PolicyRepository policyRepository;
	private PIP pip; // TODO introduce PIP
	private PolicyCombiningAlgorithm rootPolicyCombiningAlgorithm;
	private final Logger logger = LoggerFactory.getLogger(SimplePDP.class);

	private static final String MDC_REQUEST_TIME = "org:herasaf:request:xacml:evaluation:requesttime";

	/**
	 * TODO JAVADOC.
	 * 
	 * @param rootCombiningAlgorithm
	 * @param policyRepository
	 * @param pip
	 */
	public SimplePDP(PolicyCombiningAlgorithm rootCombiningAlgorithm, PolicyRepository policyRepository,
			PIP pip) {
		this.rootPolicyCombiningAlgorithm = rootCombiningAlgorithm;
		this.policyRepository = policyRepository;
		this.pip = pip;

		if (pip == null) {
			logger.warn("No PIP is set. Attributes that are not present in the request cannot be resolved.");
		}
	}

	/**
	 * TODO JAVADOC.
	 */
	public PolicyRepository getPolicyRepository() {
		return policyRepository;
	}

	/**
	 * TODO JAVADOC.
	 * 
	 * @return
	 */
	public PIP getPIP() {
		return pip;
	}

	/**
	 * TODO JAVADOC.
	 * 
	 * @return
	 */
	public PolicyCombiningAlgorithm getRootCombiningAlgorithm() {
		return rootPolicyCombiningAlgorithm;
	}

	/**
	 * TODO REVIEW René.
	 * 
	 * {@inheritDoc} The evaluation starts at the predefined root combining
	 * algorithm.
	 * 
	 * <br />
	 * <b>Logging:</b><br />
	 * This section is relevant for all users of the {@link SimplePDP} in a
	 * multi-threaded environment. All logging messages during the evaluation
	 * should be connected with a correlation ID to be able to distinguish the
	 * different requesters. Due to the fact that this connection lays with the
	 * requester here is a hint how this could be realized with the SLF4J
	 * Logging Framework (<a
	 * href="http://www.slf4j.org">http://www.slf4j.org</a>) used here if the
	 * underlying logging framework (such as logback) supports MDC (Mapped
	 * Diagnostic Context).<br />
	 * The MDC (Mapped Diagnostic Context) shall be used to distinguish the
	 * different requesters as described here: <a
	 * href="http://logback.qos.ch/manual/mdc.html"
	 * >http://logback.qos.ch/manual/mdc.html</a>. TODO extend log message after
	 * log issue is handled.
	 */
	public ResponseCtx evaluate(RequestCtx request) {
		MDC.put(MDC_REQUEST_TIME, String.valueOf(System.currentTimeMillis()));
		logger.debug("Evaluating Request: {}", request.toString());
		RequestInformation reqInfo = new RequestInformation(pip);

		DecisionType decision = rootPolicyCombiningAlgorithm.evaluateEvaluatableList(request.getRequest(),
				policyRepository.getEvaluatables(request), reqInfo);

		MDC.remove(MDC_REQUEST_TIME);
		return ResponseCtxFactory.create(request.getRequest(), decision, reqInfo);
	}
}