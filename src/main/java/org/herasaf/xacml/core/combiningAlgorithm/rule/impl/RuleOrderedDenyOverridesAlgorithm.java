/*
 * Copyright 2008-2010 HERAS-AF (www.herasaf.org)
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

package org.herasaf.xacml.core.combiningAlgorithm.rule.impl;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleOrderedCombiningAlgorithm;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.MissingAttributeDetailType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.impl.EffectType;
import org.herasaf.xacml.core.policy.impl.RuleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * The implementation of the default XACML 2.0 <i>rule ordered deny
 * overrides algorithm</i>.<br />
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 133-134, for further information.
 * 
 * @author Sacha Dolski
 * @author Stefan Oberholzer
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class RuleOrderedDenyOverridesAlgorithm extends RuleOrderedCombiningAlgorithm {
	// XACML Name of the Combining Algorithm
	private static final String COMBALGOID = "urn:oasis:names:tc:xacml:1.1:rule-combining-algorithm:ordered-deny-overrides";
	private final Logger logger = LoggerFactory.getLogger(RuleOrderedDenyOverridesAlgorithm.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCombiningAlgorithmId() {
		return COMBALGOID;
	}

	/**
	 * {@inheritDoc}
	 */
	public DecisionType evaluateRuleList(final RequestType request, final List<RuleType> rules,
			final EvaluationContext evaluationContext) {
		
		if (rules == null) {
			// It is an illegal state if the list containing the rules is
			// null.
			logger.error("the rules list was null. This is an illegal state.");
			evaluationContext.updateStatusCode(StatusCode.SYNTAX_ERROR);
			return DecisionType.INDETERMINATE;
		}
		
		/*
		 * keeps the actual state and missing attributes of this combining
		 * process.
		 */
		List<MissingAttributeDetailType> missingAttributes = new ArrayList<MissingAttributeDetailType>();
		List<StatusCode> statusCodes = new ArrayList<StatusCode>();

		boolean atLeastOnePermit = false;
		boolean potentialDeny = false;
		boolean atLeastOneError = false;

		/*
		 * If the list of rules contains no values, the for-loop is
		 * skipped and a NOT_APPLICABLE is returned.
		 */
		for (int i = 0; i < rules.size(); i++) {
			RuleType rule = rules.get(i);
			// Resets the status to go sure, that the returned statuscode is
			// the one of the evaluation.
			evaluationContext.resetStatus();

			if (logger.isDebugEnabled()) {
				MDC.put(MDC_RULE_ID, rule.getRuleId());
				logger.debug("Starting evaluation of: {}", rule.getRuleId());
			}

			DecisionType decision = this.evaluateRule(request, rule, evaluationContext);

			if (logger.isDebugEnabled()) {
				MDC.put(MDC_RULE_ID, rule.getRuleId());
				logger.debug("Evaluation of {} was: {}", rule.getRuleId(), decision.toString());
				MDC.remove(MDC_RULE_ID);
			}

			switch (decision) {
			// default case is not required here.
			case DENY:
				return DecisionType.DENY;
			case INDETERMINATE:
				/*
				 * Adds the missing attributes and of the rule evaluation.
				 */
				missingAttributes.addAll(evaluationContext.getMissingAttributes());
				statusCodes.add(evaluationContext.getStatusCode());
				atLeastOneError = true;
				/*
				 * If the effect is deny if the evaluation results in true, the
				 * result is potentialy deny if an error occures.
				 */
				if (rule.getEffect() == EffectType.DENY) {
					potentialDeny = true;
				}
				break;
			case PERMIT:
				atLeastOnePermit = true;
				break;
			}
		}
		if (potentialDeny) {
			evaluationContext.resetStatus();
			evaluationContext.setMissingAttributes(missingAttributes);
			evaluationContext.updateStatusCode(statusCodes);
			return DecisionType.INDETERMINATE;
		}
		if (atLeastOnePermit) {
			evaluationContext.resetStatus();
			return DecisionType.PERMIT;
		}
		if (atLeastOneError) {
			evaluationContext.resetStatus();
			evaluationContext.setMissingAttributes(missingAttributes);
			evaluationContext.updateStatusCode(statusCodes);
			return DecisionType.INDETERMINATE;
		}
		return DecisionType.NOT_APPLICABLE;

	}
}