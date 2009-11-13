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

import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleOrderedCombiningAlgorithm;
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
 * 
 * TODO JAVADOC
 * <p>
 * The implementation of the policy combining algorithm with the
 * Ordered-Permit-Override strategy.
 * </p>
 * <p>
 * The Implementation of the Ordered-Permit-Override implementation oriented at
 * the sample implementation in the XACML 2.0 specification.
 * </p>
 * 
 * <p>
 * See <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 137, for further information.
 * </p>
 * 
 * @author Stefan Oberholzer
 * @author Florian Huonder
 * @author René Eggenschwiler
 * @version 1.0
 */
public class RuleOrderedPermitOverridesAlgorithm extends RuleOrderedCombiningAlgorithm {
	// XACML Name of the Combining Algorithm
	private static final String COMBALGOID = "urn:oasis:names:tc:xacml:1.1:rule-combining-algorithm:ordered-permit-overrides";
	private final Logger logger = LoggerFactory.getLogger(RuleOrderedPermitOverridesAlgorithm.class);

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
	 * org.herasaf.core.combiningAlgorithm.rule.RuleOrderedCombiningAlgorithm
	 * #evaluateRuleList(org.herasaf.core.context.impl.RequestType,
	 * java.util.List, org.herasaf.core.dataTypes.RequestInformation,
	 * java.util.Map)
	 */
	@Override
	public DecisionType evaluateRuleList(final RequestType request, final List<RuleType> rules, final RequestInformation requestInfo) {
		/*
		 * keeps the actual state and missing attributes of this combining
		 * process.
		 */
		final List<MissingAttributeDetailType> missingAttributes = new ArrayList<MissingAttributeDetailType>();
		final List<StatusCode> statusCodes = new ArrayList<StatusCode>();

		boolean atLeastOneError = false;
		boolean potentialPermit = false;
		boolean atLeastOneDeny = false;

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
			case DENY:
				atLeastOneDeny = true;
				break;
			case INDETERMINATE:
				missingAttributes.addAll(requestInfo.getMissingAttributes());
				statusCodes.add(requestInfo.getStatusCode());
				atLeastOneError = true;
				/*
				 * If the Effect is permit and the result is indeterminate, the
				 * policy might have returned permit if no error has occured.
				 */
				if (rule.getEffect() == EffectType.PERMIT) {
					potentialPermit = true;
				}
				break;
			case PERMIT:
				return decision;
			}
		}
		if (potentialPermit) {
			requestInfo.resetStatus();
			requestInfo.setMissingAttributes(missingAttributes);
			requestInfo.updateStatusCode(statusCodes);
			return DecisionType.INDETERMINATE;
		}
		if (atLeastOneDeny) {
			requestInfo.resetStatus();
			requestInfo.setMissingAttributes(missingAttributes);
			requestInfo.updateStatusCode(statusCodes);
			return DecisionType.DENY;
		}
		if (atLeastOneError) {
			requestInfo.resetStatus();
			requestInfo.setMissingAttributes(missingAttributes);
			requestInfo.updateStatusCode(statusCodes);
			return DecisionType.INDETERMINATE;
		}
		return DecisionType.NOT_APPLICABLE;

	}
}