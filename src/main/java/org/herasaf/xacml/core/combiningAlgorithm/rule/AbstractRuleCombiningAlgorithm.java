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

package org.herasaf.xacml.core.combiningAlgorithm.rule;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.combiningAlgorithm.AbstractCombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.impl.ConditionType;
import org.herasaf.xacml.core.policy.impl.EffectType;
import org.herasaf.xacml.core.policy.impl.ExpressionType;
import org.herasaf.xacml.core.policy.impl.RuleType;

/**
 * This class handles all commonality of the rule combining algorithms. This is
 * the evaluation of a single rule.
 * 
 * @author Sacha Dolski
 */
public abstract class AbstractRuleCombiningAlgorithm extends AbstractCombiningAlgorithm implements
		RuleCombiningAlgorithm {
	protected static final String MDC_RULE_ID = "org:herasaf:xacml:evaluation:ruleid";

	/**
	 * {@inheritDoc}
	 */
	public DecisionType evaluateRule(final RequestType request, final RuleType rule,
			final RequestInformation requestInfo) {
		/*
		 * Matches the target of the rule
		 */
		final DecisionType targetDecision = matchTarget(request, rule.getTarget(), requestInfo);
		if (targetDecision != DecisionType.PERMIT) {
			return targetDecision;
		}

		final ConditionType condition = rule.getCondition();
		Boolean decision = null;
		/*
		 * If the rule doesn't contain a condition, the result of the condition
		 * is the result of the rule.
		 */
		if (condition == null) {
			if (rule.getEffect() == EffectType.PERMIT) {
				return DecisionType.PERMIT;
			}
			return DecisionType.DENY;

		}
		try {
			decision = (Boolean) ((ExpressionType) condition.getExpression().getValue()).handle(request, requestInfo);
		} catch (ProcessingException e) {
			requestInfo.updateStatusCode(StatusCode.PROCESSING_ERROR);
			return DecisionType.INDETERMINATE;
		} catch (MissingAttributeException e) {
			requestInfo.updateStatusCode(StatusCode.MISSING_ATTRIBUTE);
			requestInfo.addMissingAttributes(e.getMissingAttribute());
			return DecisionType.INDETERMINATE;
		} catch (SyntaxException e) {
			requestInfo.updateStatusCode(StatusCode.SYNTAX_ERROR);
			return DecisionType.INDETERMINATE;
		} catch (Exception e) {
			/*
			 * If an exception occures. There is something wrong with the
			 * Elements in the Data and because of this a Syntax Error has
			 * happened. See: OASIS eXtensible Access Control Markup Langugage
			 * (XACML) 2.0, Errata 29 June 2006</a> page 85, for further
			 * information.
			 */
			requestInfo.updateStatusCode(StatusCode.PROCESSING_ERROR);
			return DecisionType.INDETERMINATE;
		}
		if (decision) {
			/*
			 * If the Evaluation of the Condition returns true, the Effect of
			 * the rule applies. See: OASIS eXtensible Access Control Markup
			 * Langugage (XACML) 2.0, Errata 29 June 2006</a> page 82, chapter
			 * "Rule evaluation" for further information.
			 */
			if (rule.getEffect() == EffectType.PERMIT) {
				return DecisionType.PERMIT;
			}
			return DecisionType.DENY;
		}
		/*
		 * If the Evaluation of the Condition returns false, the rule is not
		 * applicable. See: OASIS eXtensible Access Control Markup Langugage
		 * (XACML) 2.0, Errata 29 June 2006</a> page 82, chapter "Rule
		 * evaluation" for further information.
		 */
		return DecisionType.NOT_APPLICABLE;

	}
}
