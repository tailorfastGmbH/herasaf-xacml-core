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

import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyOrderedCombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
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
 * First-Applicable strategy.
 * </p>
 * <p>
 * The Implementation of the First-Applicable implementation oriented at the
 * sample implementation in the XACML 2.0 specification.
 * </p>
 * 
 * <p>
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> pages 137-139, for further information.
 * </p>
 * 
 * @author Stefan Oberholzer
 * @author René Eggenschwiler
 * @version 1.0
 */
public class PolicyFirstApplicableAlgorithm extends PolicyOrderedCombiningAlgorithm {
	// XACML Name of the Combining Algorithm
	private static final String COMBALGOID = "urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:first-applicable";
	private final Logger logger = LoggerFactory.getLogger(PolicyFirstApplicableAlgorithm.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm#evaluate
	 * (org.herasaf.core.context.impl.RequestType, java.util.List)
	 */
	@Override
	public DecisionType evaluateEvaluatableList(RequestType request, List<Evaluatable> possiblePolicies,
			RequestInformation requestInfo) {
		List<ObligationType> obligations = new ArrayList<ObligationType>();
		for (int i = 0; i < possiblePolicies.size(); i++) {
			Evaluatable eval = possiblePolicies.get(i);
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
					obligations.addAll(eval.getContainedObligations(EffectType.fromValue(decision.toString())));
					obligations.addAll(requestInfo.getObligations().getObligations());
				}
			} catch (NullPointerException e) {
				/*
				 * If an error occures or a reference returnes null, the answer
				 * has to be treated as indeterminate. See: OASIS eXtensible
				 * Access Control Markup Langugage (XACML) 2.0, Errata 29 June
				 * 2006</a> page 86 and page 137 for further information.
				 */
				requestInfo.updateStatusCode(StatusCode.SYNTAX_ERROR);
				decision = DecisionType.INDETERMINATE;
			}
			requestInfo.clearObligations();
			switch (decision) {
			case PERMIT:
				/*
				 * If the result is permit, the statuscode is always ok.
				 */
				requestInfo.resetStatus();
				requestInfo.addObligations(obligations, EffectType.PERMIT);
				return DecisionType.PERMIT;
			case DENY:
				requestInfo.addObligations(obligations, EffectType.DENY);
				return decision;
			case INDETERMINATE:
				return decision;
			}
		}
		/*
		 * If no evaluation leaded to a result, the status has to be ok.
		 */
		requestInfo.resetStatus();
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