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

package org.herasaf.xacml.core.combiningAlgorithm.rule;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.combiningAlgorithm.AbstractCombiningAlgorithm;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.XACMLDefaultStatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.MissingAttributeDetailType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.impl.ConditionType;
import org.herasaf.xacml.core.policy.impl.EffectType;
import org.herasaf.xacml.core.policy.impl.ExpressionType;
import org.herasaf.xacml.core.policy.impl.RuleType;
import org.herasaf.xacml.core.targetMatcher.TargetMatchingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles all commonality of the rule combining algorithms. This is the evaluation of a single rule.
 * 
 * @author Sacha Dolski
 */
public abstract class AbstractRuleCombiningAlgorithm extends AbstractCombiningAlgorithm implements
		RuleCombiningAlgorithm {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRuleCombiningAlgorithm.class);

	protected static final String MDC_RULE_ID = "org:herasaf:xacml:evaluation:ruleid";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DecisionType evaluateRule(final RequestType request, final RuleType rule,
			final EvaluationContext evaluationContext) {
		/*
		 * Matches the target of the rule
		 */
		final TargetMatchingResult targetDecision = matchTarget(request, rule.getTarget(), evaluationContext);

		if (targetDecision == TargetMatchingResult.NO_MATCH) {
			return DecisionType.NOT_APPLICABLE;
		} else if (targetDecision == TargetMatchingResult.INDETERMINATE) {
			return DecisionType.INDETERMINATE;
		}

		final ConditionType condition = rule.getCondition();
		Boolean decision = null;
		/*
		 * If the rule doesn't contain a condition, the result of the condition is the result of the rule.
		 */
		if (condition == null) {
			if (rule.getEffect() == EffectType.PERMIT) {
				return DecisionType.PERMIT;
			}
			return DecisionType.DENY;

		}
		try {
			decision = (Boolean) ((ExpressionType) condition.getExpression().getValue()).handle(request,
					evaluationContext);
		} catch (ProcessingException e) {
			LOGGER.warn("A processing error occurred.", e);

			evaluationContext.updateStatusCode(XACMLDefaultStatusCode.PROCESSING_ERROR);
			return DecisionType.INDETERMINATE;
		} catch (MissingAttributeException e) {
			MissingAttributeDetailType missingAttribute = e.getMissingAttribute();
			LOGGER.warn("The required attribute [" + missingAttribute + "] was missing in the request.", e);

			evaluationContext.updateStatusCode(XACMLDefaultStatusCode.MISSING_ATTRIBUTE);
			evaluationContext.addMissingAttributes(missingAttribute);
			return DecisionType.INDETERMINATE;
		} catch (SyntaxException e) {
			LOGGER.warn("A syntax error occurred during evaluation.", e);

			evaluationContext.updateStatusCode(XACMLDefaultStatusCode.SYNTAX_ERROR);
			return DecisionType.INDETERMINATE;
		} catch (Exception e) {
			/*
			 * If an exception occures. There is something wrong with the elements in the data and because of this a
			 * Syntax Error has happened. See: OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29.
			 * January 2008</a> page 96 for further information.
			 */
			LOGGER.error("An unexpected error occurred during evaluation.", e);

			evaluationContext.updateStatusCode(XACMLDefaultStatusCode.PROCESSING_ERROR);
			return DecisionType.INDETERMINATE;
		}
		if (decision) {
			/*
			 * If the evaluation of the Condition returns true, the Effect of the rule applies. See: OASIS eXtensible
			 * Access Control Markup Langugage (XACML) 2.0, Errata 29. January 2008</a> page 93, chapter
			 * "Rule evaluation" for further information.
			 */
			if (rule.getEffect() == EffectType.PERMIT) {
				return DecisionType.PERMIT;
			}
			return DecisionType.DENY;
		}
		/*
		 * If the Evaluation of the Condition returns false, the rule is not applicable. See: OASIS eXtensible Access
		 * Control Markup Langugage (XACML) 2.0, Errata 29. January 2008</a> page 93, chapter "Rule evaluation" for
		 * further information.
		 */
		return DecisionType.NOT_APPLICABLE;
	}
}