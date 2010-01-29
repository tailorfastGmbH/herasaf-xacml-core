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

package org.herasaf.xacml.core.policy.combiningAlgorithm.rule.impl.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.combiningAlgorithm.rule.AbstractRuleCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RulePermitOverridesAlgorithm;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.StatusCode;
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
 * Tests the {@link RulePermitOverridesAlgorithm}.
 *
 * @author Florian Huonder.
 */
public class TestRulePermitOverridesAlgorithm {
	private AbstractRuleCombiningAlgorithm combAlg;
	private TargetMatcherMock targetMatcher;
	private RulePermitOverridesAlgorithm[] rulePermitOverridesAlgorithmWithTRUEDecisionsArray;
	private RulePermitOverridesAlgorithm rulePermitOverridesAlgorithmWithTRUEFALSEDecisions;

	/**
	 * Initializes the {@link RulePermitOverridesAlgorithm}s.
	 */
	@BeforeTest
	public void init() {
		rulePermitOverridesAlgorithmWithTRUEDecisionsArray = new RulePermitOverridesAlgorithm[12];
		for (int i = 0; i < rulePermitOverridesAlgorithmWithTRUEDecisionsArray.length; i++) {
			rulePermitOverridesAlgorithmWithTRUEDecisionsArray[i] = new RulePermitOverridesAlgorithm();
			rulePermitOverridesAlgorithmWithTRUEDecisionsArray[i]
					.setTargetMatcher(new TargetMatcherMock(
							new TargetMatcherMock.Decisions[] {
									TargetMatcherMock.Decisions.TRUE,
									TargetMatcherMock.Decisions.TRUE,
									TargetMatcherMock.Decisions.TRUE }));
		}

		rulePermitOverridesAlgorithmWithTRUEFALSEDecisions = new RulePermitOverridesAlgorithm();
		rulePermitOverridesAlgorithmWithTRUEFALSEDecisions
				.setTargetMatcher(new TargetMatcherMock(
						new TargetMatcherMock.Decisions[] {
								TargetMatcherMock.Decisions.TRUE,
								TargetMatcherMock.Decisions.FALSE,
								TargetMatcherMock.Decisions.FALSE }));
	}

	/**
	 * Creates the test cases.
	 * 
	 * @return The test cases.
	 * @throws Exception If an error occurs.
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
						rulePermitOverridesAlgorithmWithTRUEDecisionsArray[0],
						DecisionType.PERMIT, StatusCode.OK, false, },
				new Object[] {
						"permit true null, permit false null",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(false, null)),
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)), },
						rulePermitOverridesAlgorithmWithTRUEDecisionsArray[1],
						DecisionType.PERMIT, StatusCode.OK, false, },
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
						rulePermitOverridesAlgorithmWithTRUEDecisionsArray[2],
						DecisionType.PERMIT, StatusCode.OK, false, },
				new Object[] {
						"permit true null, permit true SyntaxException",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)),
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true,
												new SyntaxException("test"))), },
						rulePermitOverridesAlgorithmWithTRUEDecisionsArray[3],
						DecisionType.PERMIT, StatusCode.OK, false, },
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
						rulePermitOverridesAlgorithmWithTRUEDecisionsArray[4],
						DecisionType.PERMIT, StatusCode.OK, false, },
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
						rulePermitOverridesAlgorithmWithTRUEDecisionsArray[5],
						DecisionType.INDETERMINATE, StatusCode.SYNTAX_ERROR,
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
						rulePermitOverridesAlgorithmWithTRUEFALSEDecisions,
						DecisionType.NOT_APPLICABLE, StatusCode.OK, false, },
				new Object[] {
						"permit true null, deny true null",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)),
								initializeRule(EffectType.DENY,
										new ConditionMock(true, null)), },
						rulePermitOverridesAlgorithmWithTRUEDecisionsArray[6],
						DecisionType.PERMIT, StatusCode.OK, false, },
				new Object[] {
						"permit true null, deny false null",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(false, null)),
								initializeRule(EffectType.DENY,
										new ConditionMock(true, null)), },
						rulePermitOverridesAlgorithmWithTRUEDecisionsArray[7],
						DecisionType.DENY, StatusCode.OK, false, },
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
						rulePermitOverridesAlgorithmWithTRUEDecisionsArray[8],
						DecisionType.PERMIT, StatusCode.OK, false, },
				new Object[] {
						"permit true null, deny true syntaxException",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)),
								initializeRule(EffectType.DENY,
										new ConditionMock(true,
												new SyntaxException("test"))), },
						rulePermitOverridesAlgorithmWithTRUEDecisionsArray[9],
						DecisionType.PERMIT, StatusCode.OK, false, },
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
						rulePermitOverridesAlgorithmWithTRUEDecisionsArray[10],
						DecisionType.PERMIT, StatusCode.OK, false, },
				new Object[] {
						"Not applicable true null, indeterminate true missing attribute Exception",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(false, null)),
								initializeRule(
										EffectType.DENY,
										new ConditionMock(
												false,
												new MissingAttributeException(
														"ID",
														new StringDataTypeAttribute(),
														"Issuer"))), },
						rulePermitOverridesAlgorithmWithTRUEDecisionsArray[11],
						DecisionType.INDETERMINATE,
						StatusCode.MISSING_ATTRIBUTE, true, }, };

	}
	
	/**
	 * Creates a {@link RuleType}.
	 * 
	 * @param effect The {@link EffectType} that the created rule shall return.
	 * @param condition The {@link ConditionMock} that the {@link RuleType} shall contain.
	 * @return The created {@link RuleType}.
	 */
	private RuleType initializeRule(EffectType effect, ConditionMock condition) {
		RuleType rule = new RuleType();
		rule.setCondition(condition);
		rule.setEffect(effect);
		return rule;
	}

	/**
	 * Initializes the {@link RulePermitOverridesAlgorithm} and sets {@link TargetMatcherMock} into it.
	 */
	@BeforeMethod
	public void beforeTest() {
		targetMatcher = new TargetMatcherMock();
		combAlg = new RulePermitOverridesAlgorithm();
		combAlg.setTargetMatcher(targetMatcher);
	}

	/**
	 * Tests if the {@link RulePermitOverridesAlgorithm} returns a proper ID:
	 * @throws Exception If an error occurs.
	 */
	@Test(enabled = true)
	public void testID() throws Exception {
		assertEquals(combAlg.toString(),
				"urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:permit-overrides");
	}

	/**
	 * Tests if the {@link RulePermitOverridesAlgorithm} works properly.
	 * 
	 * @param testID An ID for the test case.
	 * @param rulesArray An array containing all {@link RuleType}s.
	 * @param alg The combining algorithm to test.
	 * @param expectedDecision The expected {@link DecisionType}.
	 * @param expectedStatusCode The expected {@link StatusCode}.
	 * @param expectMissingAttribute True if missing attributes are expected, false otherwise.
	 * @throws Exception
	 */
	@Test(dataProvider = "testData")
	public void testCombiningAlg(String testID, RuleType[] rulesArray,
			AbstractRuleCombiningAlgorithm alg, DecisionType expectedDecision,
			StatusCode expectedStatusCode, boolean expectMissingAttribute)
			throws Exception {

		PolicyType policy = new PolicyTypeMock(rulesArray);
		EvaluationContext infos = new EvaluationContext(null);
		DecisionType decision = alg.evaluate(null, policy, infos);

		assertEquals(decision, expectedDecision);
		assertEquals(infos.getStatusCode(), expectedStatusCode);
		if (expectMissingAttribute) {
			assertFalse(infos.getMissingAttributes().isEmpty());
		} else {
			assertTrue(infos.getMissingAttributes().isEmpty());
		}
	}
}