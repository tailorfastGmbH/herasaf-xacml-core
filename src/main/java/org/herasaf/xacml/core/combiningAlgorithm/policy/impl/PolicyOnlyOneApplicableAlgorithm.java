/*
 * Copyright 2008 - 2013 HERAS-AF (www.herasaf.org)
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

import org.herasaf.xacml.core.combiningAlgorithm.CombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyUnorderedCombiningAlgorithm;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.XACMLDefaultStatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.MissingAttributeDetailType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.EffectType;
import org.herasaf.xacml.core.policy.impl.IdReferenceType;
import org.herasaf.xacml.core.policy.impl.ObligationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * The implementation of the default XACML 2.0 <i>policy only one applicable
 * algorithm</i>.<br />
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29.
 * January 2008</a> pages 152 - 153, for further information.
 * 
 * @author Stefan Oberholzer
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class PolicyOnlyOneApplicableAlgorithm extends
		PolicyUnorderedCombiningAlgorithm {

	/** XACMLcombining algorithm ID. */
	public static final String ID = "urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:only-one-applicable";

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(PolicyOnlyOneApplicableAlgorithm.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DecisionType evaluateEvaluatableList(final RequestType request,
			final List<Evaluatable> possiblePolicies,
			final EvaluationContext evaluationContext) {

		if (possiblePolicies == null) {
			// It is an illegal state if the list containing the policies is
			// null.
			logger.error("The possiblePolicies list was null. This is an illegal state.");
			evaluationContext
					.updateStatusCode(XACMLDefaultStatusCode.SYNTAX_ERROR);
			return DecisionType.INDETERMINATE;
		}

		// Variable to keep the first made decision
		DecisionType firstApplicableDecision = null;
		// keeps the statuscode of the first decision
		StatusCode statusCode = null;
		// remembers the missing Attributes of the first decision
		List<MissingAttributeDetailType> missingAttributes = new ArrayList<MissingAttributeDetailType>();
		List<ObligationType> obligationsOfFirstApplicableEval = new ArrayList<ObligationType>();

		/*
		 * If the list of evaluatables contains no values, the for-loop is
		 * skipped and a NOT_APPLICABLE is returned.
		 */
		for (int i = 0; i < possiblePolicies.size(); i++) {
		        Evaluatable eval = possiblePolicies.get(i);
		        if (eval instanceof IdReferenceType) {
		                eval = evaluationContext.getPolicyRetrievalPoint().getEvaluatable(
		                                                                                  eval.getId());
		        }

			if (eval == null) {
				// It is an illegal state if the list contains any
				// null.
				logger.error("The list of possible policies must not contain any null values.");
				evaluationContext
						.updateStatusCode(XACMLDefaultStatusCode.SYNTAX_ERROR);
				return DecisionType.INDETERMINATE;
			}

			DecisionType decision;
			// Resets the status to go sure, that the returned statuscode is
			// the one of the evaluation.
			evaluationContext.resetStatus();

			if (logger.isDebugEnabled()) {
				MDC.put(MDC_EVALUATABLE_ID, eval.getId().toString());
				logger.debug("Starting evaluation of: {}", eval.getId()
						.toString());
			}

			CombiningAlgorithm combiningAlg = eval.getCombiningAlg();
			if (combiningAlg == null) {
				logger.error(
						"Unable to locate combining algorithm for policy {}",
						eval.getId());
				evaluationContext
						.updateStatusCode(XACMLDefaultStatusCode.SYNTAX_ERROR);
				decision = DecisionType.INDETERMINATE;
			} else {
				decision = combiningAlg.evaluate(request, eval,
						evaluationContext);
			}

			if (logger.isDebugEnabled()) {
				MDC.put(MDC_EVALUATABLE_ID, eval.getId().toString());
				logger.debug("Evaluation of {} was: {}", eval.getId()
						.toString(), decision.toString());
				MDC.remove(MDC_EVALUATABLE_ID);
			}

			if (decision == DecisionType.PERMIT
					|| decision == DecisionType.DENY) {
				obligationsOfFirstApplicableEval.addAll(eval
						.getContainedObligations(EffectType.fromValue(decision
								.toString())));
				obligationsOfFirstApplicableEval.addAll(evaluationContext
						.getObligations().getObligations());
			}
			evaluationContext.clearObligations();
			switch (decision) {
			case NOT_APPLICABLE:
				// If the target of the evaluated policy has matched, the
				// evaluatable
				// itself was Applicable and only its rules/inner policy haven't
				// been
				// applicable.
				if (evaluationContext.isTargetMatched()) {
					// If there was already another applicable Evaluatable,
					// Indeterminate has to be thrown.
					if (firstApplicableDecision != null) {
						/*
						 * When indeterminate is returned, it has to be sure
						 * that the returned error is a processing exception.
						 * because of this, the evaluation context have to be
						 * reset and set to Processing-Exception. See: OASIS
						 * eXtensible Access Control Markup Langugage (XACML)
						 * 2.0, Errata 29. January 2008</a> pages 143-144 and
						 * pages 152-153 for further information.
						 */
						evaluationContext.resetStatus();
						evaluationContext
								.updateStatusCode(XACMLDefaultStatusCode.PROCESSING_ERROR);
						return DecisionType.INDETERMINATE;
					}
					/*
					 * If no decision has been made yet, the decision has to be
					 * remembered including the status code and the missing
					 * attribute data.
					 */
					statusCode = evaluationContext.getStatusCode();
					missingAttributes.addAll(evaluationContext
							.getMissingAttributes());
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
					statusCode = evaluationContext.getStatusCode();
					missingAttributes.addAll(evaluationContext
							.getMissingAttributes());
				} else {
					/*
					 * When indeterminate is returned, it has to be sure that
					 * the returned error is a processing exception. because of
					 * this, the evaluation context have to be reset and set to
					 * Processing-exception. See: OASIS eXtensible Access
					 * Control Markup Langugage (XACML) 2.0, Errata 29. January
					 * 2008</a> page 91 and page 146 and the specification of
					 * the only-one-applicable algorithm for further
					 * information.
					 */
					evaluationContext.resetStatus();
					evaluationContext
							.updateStatusCode(XACMLDefaultStatusCode.PROCESSING_ERROR);
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
		evaluationContext.resetStatus();
		if (firstApplicableDecision != null) {
			evaluationContext.setMissingAttributes(missingAttributes);
			evaluationContext.updateStatusCode(statusCode);
			if (firstApplicableDecision == DecisionType.DENY) {
				evaluationContext.addObligations(
						obligationsOfFirstApplicableEval, EffectType.DENY);
			} else if (firstApplicableDecision == DecisionType.PERMIT) {
				evaluationContext.addObligations(
						obligationsOfFirstApplicableEval, EffectType.PERMIT);
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
		return ID;
	}
}