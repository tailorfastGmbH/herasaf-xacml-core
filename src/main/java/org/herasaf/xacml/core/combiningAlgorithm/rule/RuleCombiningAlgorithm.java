/*
 * Copyright 2009 HERAS-AF (www.herasaf.org)
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

import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.impl.RuleType;

/**
 * TODO JAVADOC!!
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 * 
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
	 *            The request to evaluates
	 * @param rule
	 *            The rule which should be used for the evaluation
	 * @param requestInfo
	 *            Information used for this request.
	 * @return The Decision made by this rule. The returned values for the
	 *         different situations can be found in the specification.
	 */
	public DecisionType evaluateRule(RequestType request, RuleType rule,
			RequestInformation requestInfo);

	/**
	 * Evaluates all rules given in the rule list and returnes the result.
	 * 
	 * @param request
	 *            the request which should be evaluated
	 * @param possibleRules
	 *            the rules that might match
	 * @param requestInfos
	 *            the requestInformation holding the additional information
	 *            needed to evaluate the request
	 * @return the decision
	 */
	public abstract DecisionType evaluateRuleList(RequestType request,
			List<RuleType> possibleRules, RequestInformation requestInfos);
}