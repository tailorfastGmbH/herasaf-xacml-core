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

import org.herasaf.xacml.core.combiningAlgorithm.CombiningAlgorithm;
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
 * Permit-Overrides strategy.
 * </p>
 * <p>
 * The Implementation of the Permit-override implementation oriented at the
 * sample implementation in the XACML 2.0 specification.
 * </p>
 * 
 * <p>
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 135-137, for further information.
 * </p>
 * 
 * @author Stefan Oberholzer
 * @author René Eggenschwiler
 * @version 1.0
 */
public class PolicyPermitOverridesAlgorithm extends PolicyUnorderedCombiningAlgorithm {
	// XACML Name of the Combining Algorithm
	private static final String COMBALGOID = "urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:permit-overrides";
	private final Logger logger = LoggerFactory.getLogger(PolicyPermitOverridesAlgorithm.class);

	/**
	 * TODO JAVADOC.
	 */
	public PolicyPermitOverridesAlgorithm() {
		setIsOrderedCombiningAlgorithm(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DecisionType evaluateEvaluatableList(final RequestType request, final List<Evaluatable> possiblePolicies,
			final RequestInformation requestInfo) {

		if (possiblePolicies == null) {
			requestInfo.resetStatus();
			logger.debug("Decision is NOT_APPLICABLE because the PolicySet does not contain any Evaluatables.");
			return DecisionType.NOT_APPLICABLE;
		}

		boolean atLeastOneError = false;
		boolean atLeastOneDeny = false;
		boolean atLeastOnePermit = false;
		/*
		 * keeps the actual state and missing attributes of this combining
		 * process.
		 */
		List<MissingAttributeDetailType> missingAttributes = new ArrayList<MissingAttributeDetailType>();
		List<StatusCode> statusCodes = new ArrayList<StatusCode>();
		List<ObligationType> obligationsOfApplicableEvals = new ArrayList<ObligationType>();

		for (int i = 0; i < possiblePolicies.size(); i++) {
			final Evaluatable eval = possiblePolicies.get(i);

			if (eval == null) {
				continue;
			}

			if (atLeastOnePermit && isRespectAbandonedEvaluatables() && !eval.hasObligations()) {
				/*
				 * If a decision is already made (decisionPermit == true) and
				 * the abandoned Obligations must be taken into account
				 * (respectAbandonedEvaluatables == true) and the evaluatable to
				 * evaluate (and its sub evaluatables) do not have Obligations,
				 * then this iteration can be skipped.
				 */
				break;
			}

			DecisionType decision;
			// Resets the status to go sure, that the returned statuscode is
			// the one of the evaluation.
			requestInfo.resetStatus();

			if (logger.isDebugEnabled()) {
				MDC.put(MDC_EVALUATABLE_ID, eval.getId().getId());
				logger.debug("Starting evaluation of: {}", eval.getId().getId());
			}

			CombiningAlgorithm combiningAlg = eval.getCombiningAlg();
			if (combiningAlg == null) {
				logger.error("Unable to locate combining algorithm for policy {}", eval.getId());
				requestInfo.updateStatusCode(StatusCode.SYNTAX_ERROR);
				decision = DecisionType.INDETERMINATE;
			} else {
				decision = combiningAlg.evaluate(request, eval, requestInfo);
			}

			if (logger.isDebugEnabled()) {
				MDC.put(MDC_EVALUATABLE_ID, eval.getId().getId());
				logger.debug("Evaluation of {} was: {}", eval.getId().getId(), decision.toString());
				MDC.remove(MDC_EVALUATABLE_ID);
			}

			if (decision == DecisionType.PERMIT || decision == DecisionType.DENY) {
				obligationsOfApplicableEvals.addAll(eval.getContainedObligations(EffectType.fromValue(decision
						.toString())));
				obligationsOfApplicableEvals.addAll(requestInfo.getObligations().getObligations());
			}
			
			switch (decision) {
			case PERMIT:
				if (!isRespectAbandonedEvaluatables()) {
					requestInfo.clearObligations();
					requestInfo.addObligations(obligationsOfApplicableEvals, EffectType.PERMIT);
					/*
					 * If the result is permit, the statuscode is always ok.
					 */
					requestInfo.resetStatus();
					return DecisionType.PERMIT;
				} else {
					atLeastOnePermit = true;
				}
				break;
			case DENY:
				/*
				 * If the decision of the evaluatable is deny, the status has to
				 * be saved.
				 */
				missingAttributes.addAll(requestInfo.getMissingAttributes());
				statusCodes.add(requestInfo.getStatusCode());
				atLeastOneDeny = true;
				break;
			case INDETERMINATE:
				/*
				 * If the decision of the evaluatable is Indeterminate, the
				 * status has to be saved.
				 */
				missingAttributes.addAll(requestInfo.getMissingAttributes());
				statusCodes.add(requestInfo.getStatusCode());
				atLeastOneError = true;
				break;
			case NOT_APPLICABLE:
				break;
			}
			requestInfo.clearObligations();
		}
		if (atLeastOnePermit) {
			/*
			 * If the result is permit, the statuscode is always ok.
			 */
			requestInfo.resetStatus();
			requestInfo.addObligations(obligationsOfApplicableEvals, EffectType.PERMIT);
			return DecisionType.PERMIT;
		} else if (atLeastOneDeny) {
			// The obligationsOfApplicableEvals may not be revised because it
			// can only contain DENY-Obligations.
			requestInfo.addObligations(obligationsOfApplicableEvals, EffectType.DENY);
			requestInfo.setMissingAttributes(missingAttributes);
			requestInfo.resetStatus();
			return DecisionType.DENY;
		} else if (atLeastOneError) {
			requestInfo.setMissingAttributes(missingAttributes);
			requestInfo.updateStatusCode(statusCodes);
			return DecisionType.INDETERMINATE;
		}
		requestInfo.clearObligations();
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