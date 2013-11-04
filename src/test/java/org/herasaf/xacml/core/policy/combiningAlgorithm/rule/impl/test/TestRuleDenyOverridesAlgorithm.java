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

package org.herasaf.xacml.core.policy.combiningAlgorithm.rule.impl.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.combiningAlgorithm.rule.AbstractRuleCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RuleDenyOverridesAlgorithm;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.StatusCodeComparator;
import org.herasaf.xacml.core.context.XACMLDefaultStatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.herasaf.xacml.core.function.FunctionProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.combiningAlgorithm.mock.TargetMatcherMock;
import org.herasaf.xacml.core.policy.impl.EffectType;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.herasaf.xacml.core.policy.impl.RuleType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests the {@link RuleDenyOverridesAlgorithm}.
 * 
 * @author Florian Huonder.
 */
public class TestRuleDenyOverridesAlgorithm {
	private AbstractRuleCombiningAlgorithm combAlg;
	private TargetMatcherMock[] targetMatcherMockWithTRUEDecisionsArray;
	private TargetMatcherMock targetMatcherMockWithTRUEFALSEDecisions;

	/**
	 * Initializes the {@link TargetMatcherMock}s.
	 */
	@BeforeTest
	public void init() {
		targetMatcherMockWithTRUEDecisionsArray = new TargetMatcherMock[11];
		for (int i = 0; i < targetMatcherMockWithTRUEDecisionsArray.length; i++) {
			targetMatcherMockWithTRUEDecisionsArray[i] = new TargetMatcherMock(
					new TargetMatcherMock.Decisions[] {
							TargetMatcherMock.Decisions.TRUE,
							TargetMatcherMock.Decisions.TRUE,
							TargetMatcherMock.Decisions.TRUE });
		}

		targetMatcherMockWithTRUEFALSEDecisions = new TargetMatcherMock(
				new TargetMatcherMock.Decisions[] {
						TargetMatcherMock.Decisions.TRUE,
						TargetMatcherMock.Decisions.FALSE,
						TargetMatcherMock.Decisions.FALSE });
	}

	/**
	 * Creates the test cases.
	 * 
	 * @return The test cases.
	 * @throws Exception
	 *             If an error occurs.
	 */
	@DataProvider(name = "testData")
	public Object[][] evaluationData() throws Exception {
		return new Object[][] {
				new Object[] {
						"permit true null, permit true null",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)),
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)), },
						targetMatcherMockWithTRUEDecisionsArray[0],
						DecisionType.PERMIT, XACMLDefaultStatusCode.OK, false, },
				new Object[] {
						"permit true null, permit false null",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(false, null)),
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)), },
						targetMatcherMockWithTRUEDecisionsArray[1],
						DecisionType.PERMIT, XACMLDefaultStatusCode.OK, false, },
				new Object[] {
						"permit true null, permit true prcocessingException",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)),
								initializeRule(
										EffectType.PERMIT,
										new ConditionMock(
												true,
												new FunctionProcessingException(
														"test"))), },
						targetMatcherMockWithTRUEDecisionsArray[2],
						DecisionType.PERMIT, XACMLDefaultStatusCode.OK, false, },
				new Object[] {
						"permit true null, permit true SyntaxException",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)),
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true,
												new SyntaxException("test"))), },
						targetMatcherMockWithTRUEDecisionsArray[3],
						DecisionType.PERMIT, XACMLDefaultStatusCode.OK, false, },
				new Object[] {
						"permit true null, permit true missingAttributeException",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)),
								initializeRule(
										EffectType.PERMIT,
										new ConditionMock(
												true,
												new MissingAttributeException(
														"ID",
														new StringDataTypeAttribute(),
														"Issuer"))), },
						targetMatcherMockWithTRUEDecisionsArray[4],
						DecisionType.PERMIT, XACMLDefaultStatusCode.OK, false, },
				new Object[] {
						"permit true SyntaxException, permit true FunctionProcessingException",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true,
												new SyntaxException("Test"))),
								initializeRule(
										EffectType.PERMIT,
										new ConditionMock(
												true,
												new FunctionProcessingException(
														"Test"))), },
						targetMatcherMockWithTRUEDecisionsArray[5],
						DecisionType.INDETERMINATE, XACMLDefaultStatusCode.SYNTAX_ERROR,
						false, },
				new Object[] {
						"permit true SyntaxException, permit true FunctionProcessingException",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true,
												new SyntaxException("Test"))),
								initializeRule(
										EffectType.PERMIT,
										new ConditionMock(
												true,
												new FunctionProcessingException(
														"Test"))), },
						targetMatcherMockWithTRUEFALSEDecisions,
						DecisionType.NOT_APPLICABLE, XACMLDefaultStatusCode.OK, false, },
				new Object[] {
						"permit true null, deny true null",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)),
								initializeRule(EffectType.DENY,
										new ConditionMock(true, null)), },
						targetMatcherMockWithTRUEDecisionsArray[6],
						DecisionType.DENY, XACMLDefaultStatusCode.OK, false, },
				new Object[] {
						"permit true null, permit false null",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(false, null)),
								initializeRule(EffectType.DENY,
										new ConditionMock(true, null)), },
						targetMatcherMockWithTRUEDecisionsArray[7],
						DecisionType.DENY, XACMLDefaultStatusCode.OK, false, },
				new Object[] {
						"permit true null, deny true processingException",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)),
								initializeRule(
										EffectType.DENY,
										new ConditionMock(
												true,
												new FunctionProcessingException(
														"test"))), },
						targetMatcherMockWithTRUEDecisionsArray[8],
						DecisionType.INDETERMINATE,
						XACMLDefaultStatusCode.PROCESSING_ERROR, false, },
				new Object[] {
						"permit true null, deny true syntaxException",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)),
								initializeRule(EffectType.DENY,
										new ConditionMock(true,
												new SyntaxException("test"))), },
						targetMatcherMockWithTRUEDecisionsArray[9],
						DecisionType.INDETERMINATE, XACMLDefaultStatusCode.SYNTAX_ERROR,
						false, },
				new Object[] {
						"permit true null, permit true missingAttribueException",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)),
								initializeRule(
										EffectType.DENY,
										new ConditionMock(
												true,
												new MissingAttributeException(
														"ID",
														new StringDataTypeAttribute(),
														"Issuer"))), },
						targetMatcherMockWithTRUEDecisionsArray[10],
						DecisionType.INDETERMINATE,
						XACMLDefaultStatusCode.MISSING_ATTRIBUTE, true, }, };
	}

	/**
	 * Creates a {@link RuleType}.
	 * 
	 * @param effect
	 *            The {@link EffectType} that the created rule shall return.
	 * @param condition
	 *            The {@link ConditionMock} that the {@link RuleType} shall
	 *            contain.
	 * @return The created {@link RuleType}.
	 */
	private RuleType initializeRule(EffectType effect, ConditionMock condition) {
		RuleType rule = new RuleType();
		rule.setCondition(condition);
		rule.setEffect(effect);
		return rule;
	}

	/**
	 * Initializes the {@link RuleDenyOverridesAlgorithm}.
	 */
	@BeforeMethod
	public void beforeTest() {
		combAlg = new RuleDenyOverridesAlgorithm();
	}

	/**
	 * Tests if the {@link RuleDenyOverridesAlgorithm} returns a proper ID:
	 * 
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test(enabled = true)
	public void testID() throws Exception {
		assertEquals(combAlg.toString(),
				"urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:deny-overrides");
	}

	/**
	 * Tests if the {@link RuleDenyOverridesAlgorithm} works properly.
	 * 
	 * @param testID
	 *            An ID for the test case.
	 * @param rulesArray
	 *            An array containing all {@link RuleType}s.
	 * @param tmm
	 *            The {@link TargetMatcherMock} to use.
	 * @param expectedDecision
	 *            The expected {@link DecisionType}.
	 * @param expectedStatusCode
	 *            The expected {@link StatusCode}.
	 * @param expectMissingAttribute
	 *            True if missing attributes are expected, false otherwise.
	 * @throws Exception
	 */
	@Test(dataProvider = "testData")
	public void testCombiningAlg(String testID, RuleType[] rulesArray,
			TargetMatcherMock tmm, DecisionType expectedDecision,
			StatusCode expectedStatusCode, boolean expectMissingAttribute)
			throws Exception {

		PolicyType policy = new PolicyTypeMock(rulesArray);
		EvaluationContext infos = new EvaluationContext(tmm, new StatusCodeComparator(), null);
		DecisionType decision = combAlg.evaluate(null, policy, infos);

		assertEquals(decision, expectedDecision);
		assertEquals(infos.getStatusCode(), expectedStatusCode);
		if (expectMissingAttribute) {
			assertFalse(infos.getMissingAttributes().isEmpty());
		} else {
			assertTrue(infos.getMissingAttributes().isEmpty());
		}
	}
}