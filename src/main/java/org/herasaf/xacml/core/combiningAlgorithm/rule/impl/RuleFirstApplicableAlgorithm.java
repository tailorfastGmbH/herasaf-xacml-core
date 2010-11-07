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

import java.util.List;

import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleUnorderedCombiningAlgorithm;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.XACMLDefaultStatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.impl.RuleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * The implementation of the default XACML 2.0 <i>rule first applicable algorithm</i>.<br />
 * See: <a href= "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20"> OASIS eXtensible Access
 * Control Markup Langugage (XACML) 2.0, Errata 29. January 2008</a> pages 151-152, for further information.
 * 
 * @author Sacha Dolski
 * @author Stefan Oberholzer
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class RuleFirstApplicableAlgorithm extends RuleUnorderedCombiningAlgorithm {
	
	/** XACMLcombining algorithm ID. */
	public static final String ID = "urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:first-applicable";

    private static final long serialVersionUID = 1L;

    private final Logger logger = LoggerFactory.getLogger(RuleFirstApplicableAlgorithm.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCombiningAlgorithmId() {
		return ID;
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
			evaluationContext.updateStatusCode(XACMLDefaultStatusCode.SYNTAX_ERROR);
			return DecisionType.INDETERMINATE;
		}

		/*
		 * If the list of rules contains no values, the for-loop is skipped and
		 * a NOT_APPLICABLE is returned.
		 */
		for (int i = 0; i < rules.size(); i++) {
			RuleType rule = rules.get(i);
			/*
			 * keeps the actual state and missing attributes of this combining
			 * process.
			 */
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
			case INDETERMINATE:
			case PERMIT:
				return decision;
			}
		}
		return DecisionType.NOT_APPLICABLE;

	}
}