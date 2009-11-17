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

package org.herasaf.xacml.core.combiningAlgorithm.policy.impl;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyUnorderedCombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.MissingAttributeDetailType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.EffectType;
import org.herasaf.xacml.core.policy.impl.ObligationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * TODO JAVADOC
 * 
 * <p>
 * The implementation of the policy combining algorithm with the
 * Only-One-Applicable strategy.
 * </p>
 * 
 * <p>
 * The Implementation of the Only-One-Applicable implementation oriented at the
 * sample implementation in the XACML 2.0 specification.
 * </p>
 * 
 * <p>
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 139, for further information.
 * </p>
 * 
 * @author Sacha Dolski
 * @author Stefan Oberholzer
 * @author René Eggenschwiler
 * @version 1.0
 */
public class PolicyOnlyOneApplicableAlgorithm extends PolicyUnorderedCombiningAlgorithm {
	// XACML Name of the Combining Algorithm
	private static final String COMBALGOID = "urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:only-one-applicable";
	private final Logger logger = LoggerFactory.getLogger(PolicyOnlyOneApplicableAlgorithm.class);

	/**
	 * TODO JAVADOC.
	 */
	public PolicyOnlyOneApplicableAlgorithm() {
		setIsOrderedCombiningAlgorithm(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DecisionType evaluateEvaluatableList(final RequestType request, final List<Evaluatable> possiblePolicies,
			final RequestInformation requestInfo) {
		// Variable to keep the first made decision
		DecisionType firstApplicableDecision = null;
		// keeps the statuscode of the first decision
		StatusCode statusCode = null;
		// remembers the missing Attributes of the first decision
		final List<MissingAttributeDetailType> missingAttributes = new ArrayList<MissingAttributeDetailType>();
		final List<ObligationType> obligationsOfFirstApplicableEval = new ArrayList<ObligationType>();

		for (int i = 0; i < possiblePolicies.size(); i++) {
			final Evaluatable eval = possiblePolicies.get(i);
			DecisionType decision;
			try {
				// Resets the status to go sure, that the returned statuscode is
				// the one of the evaluation.
				requestInfo.resetStatus();

				if (logger.isDebugEnabled()) {
					MDC.put(MDC_EVALUATABLE_ID, eval.getId().getId());
					logger.debug("Starting evaluation of: {}", eval.getId().getId());
				}

				decision = eval.getCombiningAlg().evaluate(request, eval, requestInfo);

				if (logger.isDebugEnabled()) {
					MDC.put(MDC_EVALUATABLE_ID, eval.getId().getId());
					logger.debug("Evaluation of {} was: {}", eval.getId().getId(), decision.toString());
					MDC.remove(MDC_EVALUATABLE_ID);
				}

				if (decision == DecisionType.PERMIT || decision == DecisionType.DENY) {
					obligationsOfFirstApplicableEval.addAll(eval.getContainedObligations(EffectType.fromValue(decision
							.toString())));
					obligationsOfFirstApplicableEval.addAll(requestInfo.getObligations().getObligations());
				}
			} catch (NullPointerException e) {
				/*
				 * If an error occures or a reference returnes null, the answer
				 * has to be treated as indeterminate. See: OASIS eXtensible
				 * Access Control Markup Langugage (XACML) 2.0, Errata 29 June
				 * 2006</a> page 86 and page 139 for further information.
				 */
				requestInfo.updateStatusCode(StatusCode.SYNTAX_ERROR);
				decision = DecisionType.INDETERMINATE;
			}
			requestInfo.clearObligations();
			switch (decision) {
			case NOT_APPLICABLE:
				// If the target of the evaluated policy has matched, the
				// evaluatable
				// itself was Applicable and only its rules/inner policy haven't
				// been
				// applicable.
				if (requestInfo.isTargetMatched()) {
					// If there was already another applicable Evaluatable,
					// Indeterminate has to be thrown.
					if (firstApplicableDecision != null) {
						/*
						 * When indeterminate is returned, it has to be sure
						 * that the returned error is a processing exception.
						 * because of this, the request information have to be
						 * reset and set to Processing-exception. See: OASIS
						 * eXtensible Access Control Markup Langugage (XACML)
						 * 2.0, Errata 29 June 2006</a> page 86 and page 139 and
						 * the specification of the only-one-applicable
						 * algorithm for further information.
						 */
						requestInfo.resetStatus();
						requestInfo.updateStatusCode(StatusCode.PROCESSING_ERROR);
						return DecisionType.INDETERMINATE;
					}
					/*
					 * If no decision has been made yet, the decision has to be
					 * remembered including the status code and the missing
					 * attribute data.
					 */
					statusCode = requestInfo.getStatusCode();
					missingAttributes.addAll(requestInfo.getMissingAttributes());
					firstApplicableDecision = decision;
				}
				break;
			default:
				/*
				 * If no decision has been made yet, the decision has to be
				 * remembered including the status code and the missing
				 * attribute data. If there was already another applicable
				 * Evaluatable, Indeterminate has to be returned.
				 */
				if (firstApplicableDecision == null) {
					firstApplicableDecision = decision;
					statusCode = requestInfo.getStatusCode();
					missingAttributes.addAll(requestInfo.getMissingAttributes());
				} else {
					/*
					 * When indeterminate is returned, it has to be sure that
					 * the returned error is a processing exception. because of
					 * this, the request information have to be reset and set to
					 * Processing-exception. See: OASIS eXtensible Access
					 * Control Markup Langugage (XACML) 2.0, Errata 29 June
					 * 2006</a> page 86 and page 139 and the specification of
					 * the only-one-applicable algorithm for further
					 * information.
					 */
					requestInfo.resetStatus();
					requestInfo.updateStatusCode(StatusCode.PROCESSING_ERROR);
					return DecisionType.INDETERMINATE;
				}
				break;
			}
		}

		/*
		 * When the decision was made, the statuscode and the missing attributes
		 * where saved. To return the result, they have to be set to the state
		 * of the decision. Or if the policy is not applicable it has to be sure
		 * that no chances of the state have been made.
		 */
		requestInfo.resetStatus();
		if (firstApplicableDecision != null) {
			requestInfo.setMissingAttributes(missingAttributes);
			requestInfo.updateStatusCode(statusCode);
			if (firstApplicableDecision == DecisionType.DENY) {
				requestInfo.addObligations(obligationsOfFirstApplicableEval, EffectType.DENY);
			} else if (firstApplicableDecision == DecisionType.PERMIT) {
				requestInfo.addObligations(obligationsOfFirstApplicableEval, EffectType.PERMIT);
			}
			return firstApplicableDecision;
		}
		return DecisionType.NOT_APPLICABLE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCombiningAlgorithmId() {
		return COMBALGOID;
	}
}