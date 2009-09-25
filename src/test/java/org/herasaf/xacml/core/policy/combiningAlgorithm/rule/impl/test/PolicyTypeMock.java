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

package org.herasaf.xacml.core.policy.combiningAlgorithm.rule.impl.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.herasaf.xacml.core.policy.impl.RuleType;
import org.herasaf.xacml.core.policy.impl.Variable;

/**
 * A mock of the {@link PolicyType}.
 * 
 * @author Florian Huonder
 */
public class PolicyTypeMock extends PolicyType {
	private static final long serialVersionUID = 555197990866583034L;
	private List<RuleType> rules;

	/**
	 * Creates a mock with the given containing rules.
	 * 
	 * @param rulesArray The rules to add to the mock.
	 */
	public PolicyTypeMock(RuleType[] rulesArray ){
		List<RuleType> rules = new ArrayList<RuleType>();
		for (RuleType rule : rulesArray){
			rules.add(rule);
		}
		this.rules = rules;
	}

	/**
	 * Creates a new mock that contains an empty {@link RuleType}.
	 */
	public PolicyTypeMock(){
		rules = new ArrayList<RuleType>();
		rules.add(new RuleType());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RuleType> getOrderedRules() {
		return rules;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RuleType> getUnorderedRules() {
		return rules;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Returns always null because there are no {@link Variable}s.
	 */
	@Override
	public Map<String, Variable> getVariables() {
		return null;
	}
}