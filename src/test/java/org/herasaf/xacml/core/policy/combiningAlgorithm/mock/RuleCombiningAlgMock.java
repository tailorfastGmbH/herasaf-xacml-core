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

package org.herasaf.xacml.core.policy.combiningAlgorithm.mock;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.combiningAlgorithm.rule.AbstractRuleCombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.MissingAttributeDetailType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.RuleType;
import org.herasaf.xacml.core.targetMatcher.TargetMatcher;

/**
 * This is a mock object of a rule combining algorithm. This mock can be fed
 * with a {@link TargetMatcher} or it uses the {@link TargetMatcherMock}.
 * 
 * @author Florian Huonder
 */
public class RuleCombiningAlgMock extends AbstractRuleCombiningAlgorithm {
	public DecisionType decision;
	public StatusCode statusCode;
	public MissingAttributeDetailType missingAttr;

	/**
	 * Creates a new mock with a given TargetMatcher.
	 * 
	 * @param matcher The {@link TargetMatcher} to use.
	 */
	public RuleCombiningAlgMock(TargetMatcher matcher){
		super.setTargetMatcher(matcher);
	}
	
	/**
	 * Creates a new mock with a {@link TargetMatcherMock}.
	 */
	public RuleCombiningAlgMock(){
		super.setTargetMatcher(new TargetMatcherMock());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DecisionType evaluateRule(RequestType request,
			RuleType rule, RequestInformation requestInfo) {
		return super.evaluateRule(request, rule, requestInfo);
	}
	
	/**
	 * Creates a new mock with a given {@link DecisionType}.
	 * 
	 * @param decision The {@link DecisionType} to use.
	 */
	public RuleCombiningAlgMock(DecisionType decision){
		this();
		this.decision = decision;
	}

	/**
	 * Creates a new mock with a given {@link DecisionType}, {@link StatusCode} and {@link MissingAttributeDetailType}.
	 * 
	 * @param decision The {@link DecisionType} to use.
	 * @param statusCode The {@link StatusCode} to use.
	 * @param missingAttr The {@link MissingAttributeDetailType} to use.
	 */
	public RuleCombiningAlgMock(DecisionType decision,
			StatusCode statusCode, MissingAttributeDetailType missingAttr) {
		this(decision);
		this.statusCode = statusCode;
		this.missingAttr = missingAttr;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Returns a {@link DecisionType} containing the predefined
	 * {@link StatusCode}, {@link DecisionType} and
	 * {@link MissingAttributeDetailType}.
	 */
	public DecisionType evaluate(RequestType request,
			Evaluatable evals, RequestInformation requestInfo) {
		List<MissingAttributeDetailType> missingAttributes = new ArrayList<MissingAttributeDetailType>();
		if (missingAttr != null) {
			missingAttributes.add(missingAttr);
		}
		requestInfo.setMissingAttributes(missingAttributes);
		requestInfo.updateStatusCode(statusCode);
		return decision;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Returns a {@link DecisionType} containing the predefined
	 * {@link StatusCode}, {@link DecisionType} and
	 * {@link MissingAttributeDetailType}.
	 */
	public DecisionType evaluateRuleList(RequestType request,
			List<RuleType> possibleEvaluatables,
			RequestInformation requestInfo) {
		List<MissingAttributeDetailType> missingAttributes = new ArrayList<MissingAttributeDetailType>();
		if (missingAttr != null) {
			missingAttributes.add(missingAttr);
		}
		requestInfo.setMissingAttributes(missingAttributes);
		requestInfo.updateStatusCode(statusCode);
		return decision;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCombiningAlgorithmId() {
		return "mockCombiningAlgorithm";
	}
}