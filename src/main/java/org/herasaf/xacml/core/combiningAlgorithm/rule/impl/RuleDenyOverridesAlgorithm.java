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

package org.herasaf.xacml.core.combiningAlgorithm.rule.impl;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleUnorderedCombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
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
 * TODO JAVADOC
 * 
 * <p>
 * The implementation of the policy combining algorithm with the Deny-Overrides
 * strategy.
 * </p>
 * <p>
 * The Implementation of the Deny-override implementation oriented at the sample
 * implementation in the XACML 2.0 specification.
 * </p>
 * 
 * <p>
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 133-134, for further information.
 * </p>
 * 
 * @author Sacha Dolski
 * @author Stefan Oberholzer
 * @author René Eggenschwiler
 * @version 1.0
 */
public class RuleDenyOverridesAlgorithm extends RuleUnorderedCombiningAlgorithm {
	// XACML Name of the Combining Algorithm
	private static final String COMBALGOID = "urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:deny-overrides";
	private final Logger logger = LoggerFactory.getLogger(RuleDenyOverridesAlgorithm.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCombiningAlgorithmId() {
		return COMBALGOID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.core.combiningAlgorithm.rule.RuleUnorderedCombiningAlgorithm
	 * #evaluateRuleList(org.herasaf.core.context.impl.RequestType,
	 * java.util.List, org.herasaf.core.dataTypes.RequestInformation,
	 * java.util.Map)
	 */
	@Override
	public DecisionType evaluateRuleList(final RequestType request, final List<RuleType> rules,
			final RequestInformation requestInfo) {
		/*
		 * keeps the actual state and missing attributes of this combining
		 * process.
		 */
		final List<MissingAttributeDetailType> missingAttributes = new ArrayList<MissingAttributeDetailType>();
		final List<StatusCode> statusCodes = new ArrayList<StatusCode>();

		boolean atLeastOnePermit = false;
		boolean potentialDeny = false;
		boolean atLeastOneError = false;

		for (int i = 0; i < rules.size(); i++) {
			final RuleType rule = rules.get(i);
			// Resets the status to go sure, that the returned statuscode is
			// the one of the evaluation.
			requestInfo.resetStatus();

			if (logger.isDebugEnabled()) {
				MDC.put(MDC_RULE_ID, rule.getRuleId());
				logger.debug("Starting evaluation of: {}", rule.getRuleId());
			}

			final DecisionType decision = this.evaluateRule(request, rule, requestInfo);

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
				missingAttributes.addAll(requestInfo.getMissingAttributes());
				statusCodes.add(requestInfo.getStatusCode());
				atLeastOneError = true;
				/*
				 * If the effect is deny if the evaluation results in true, the
				 * result is potentially deny if an error occurres.
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
			/*
			 * The status of the requestinfo has to be reset to go sure that
			 * only the wanted values are setted.
			 */
			requestInfo.resetStatus();
			requestInfo.setMissingAttributes(missingAttributes);
			requestInfo.updateStatusCode(statusCodes);
			return DecisionType.INDETERMINATE;
		}
		if (atLeastOnePermit) {
			requestInfo.resetStatus();
			return DecisionType.PERMIT;
		}
		if (atLeastOneError) {
			/*
			 * The status of the requestinfo has to be reset to go sure that
			 * only the wanted values are setted.
			 */
			requestInfo.resetStatus();
			requestInfo.setMissingAttributes(missingAttributes);
			requestInfo.updateStatusCode(statusCodes);
			return DecisionType.INDETERMINATE;
		}
		return DecisionType.NOT_APPLICABLE;

	}
}