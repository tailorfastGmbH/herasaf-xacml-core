/*
 * Copyright 2009-2010 HERAS-AF (www.herasaf.org)
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

import java.util.List;

import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.impl.RuleType;

/**
 * All rule combining algorithms must implement this interface. It provides the
 * entry point ({@link #evaluateRuleList(RequestType, List, EvaluationContext)}
 * ) for starting to evaluate all containing rules and a method to evaluate a
 * single rule ({@link #evaluateRule(RequestType, RuleType, EvaluationContext)}
 * ).
 * 
 * @author Sacha Dolski
 * @author Florian Huonder
 */
public interface RuleCombiningAlgorithm {

	/**
	 * Evaluates a rule.
	 * 
	 * The different return values of a rule evaluation are specified in the
	 * XACML 2.0 specification. See: <a href=
	 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20"
	 * > OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29
	 * June 2006</a> page 82, for further information.
	 * 
	 * @param request
	 *            The request to evaluate.
	 * @param rule
	 *            The rule that shall be evaluated.
	 * @param evaluationContext
	 *            The evaluation context.
	 * @return The decision of the evaluation.
	 */
	DecisionType evaluateRule(RequestType request, RuleType rule,
			EvaluationContext evaluationContext);

	/**
	 * Evaluates all rules given in the rule list and returns the combined
	 * result.
	 * 
	 * @param request
	 *            The request that should be evaluated.
	 * @param possibleRules
	 *            The rules that might match und shall be evaluated.
	 * @param evaluationContexts
	 *            t The evaluation context.
	 * @return The decision of the evaluation.
	 */
	DecisionType evaluateRuleList(RequestType request,
			List<RuleType> possibleRules, EvaluationContext evaluationContexts);
}