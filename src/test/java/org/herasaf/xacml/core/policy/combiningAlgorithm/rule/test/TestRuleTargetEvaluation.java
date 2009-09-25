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

package org.herasaf.xacml.core.policy.combiningAlgorithm.rule.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.herasaf.xacml.core.combiningAlgorithm.rule.AbstractRuleCombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.combiningAlgorithm.rule.impl.test.PolicyTypeMock;
import org.herasaf.xacml.core.policy.impl.PolicySetType;
import org.herasaf.xacml.core.policy.impl.RuleType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests the target evaluation of the {@link RuleType}.
 * 
 * @author Florian Huonder
 */
public class TestRuleTargetEvaluation {
	
	/**
	 * Creates the tests cases.
	 * @return The test cases.
	 * @throws Exception If an error occurs.
	 */
	@DataProvider(name = "TargetEvaluationInput")
	public Object[][] testTargetMatchAndOneEvaluatable() throws Exception {
		return new Object[][] {
				new Object[] {
						new OrderedRuleMock(DecisionType.PERMIT,
								StatusCode.OK, null), new PolicyTypeMock(),
								DecisionType.PERMIT, StatusCode.OK, false },
				new Object[] {
						new OrderedRuleMock(DecisionType.DENY,
								StatusCode.OK, null), new PolicyTypeMock(),
								DecisionType.DENY, StatusCode.OK, false },
				new Object[] {
						new OrderedRuleMock(DecisionType.PERMIT,
								StatusCode.OK, null), new PolicySetType(),
								DecisionType.INDETERMINATE,
						StatusCode.SYNTAX_ERROR, false },
				new Object[] {
						new UnorderedRuleMock(DecisionType.PERMIT,
								StatusCode.OK, null), new PolicyTypeMock(),
								DecisionType.PERMIT, StatusCode.OK, false },
				new Object[] {
						new UnorderedRuleMock(DecisionType.DENY,
								StatusCode.OK, null), new PolicyTypeMock(),
								DecisionType.DENY, StatusCode.OK, false },
				new Object[] {
						new UnorderedRuleMock(DecisionType.PERMIT,
								StatusCode.OK, null), new PolicySetType(),
								DecisionType.INDETERMINATE,
						StatusCode.SYNTAX_ERROR, false },
		};
	}

	/**
	 * Tests the target evaluation of the {@link RuleType}.
	 * 
	 * @param alg The combining algorithm to use.
	 * @param eval The {@link Evaluatable} that contains the {@link RuleType}.
	 * @param expectedDecision The expected {@link DecisionType}.
	 * @param expectedStatusCode The expected {@link StatusCode}.
	 * @param missingAttributeExpected True if the {@link DecisionType} contains missing attributes.
	 */
	@Test(dataProvider = "TargetEvaluationInput")
	public void testOrderedRuleTargetEvaluation(AbstractRuleCombiningAlgorithm alg,
			Evaluatable eval, DecisionType expectedDecision,
			StatusCode expectedStatusCode, boolean missingAttributeExpected) {

		RequestInformation infos = new RequestInformation(null);
		DecisionType decision = alg.evaluate(null, eval, infos);
		assertEquals(decision, expectedDecision);
		assertEquals(infos.getStatusCode(), expectedStatusCode);
		if (missingAttributeExpected) {
			assertFalse(infos.getMissingAttributes().isEmpty());
		} else {
			assertTrue(infos.getMissingAttributes().isEmpty());
		}
	}
}