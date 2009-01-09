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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.SyntaxException;
import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RuleOrderedDenyOverridesAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.herasaf.xacml.core.function.FunctionProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.combiningAlgorithm.mock.TargetMatcherMock;
import org.herasaf.xacml.core.policy.impl.EffectType;
import org.herasaf.xacml.core.policy.impl.IdReferenceType;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.herasaf.xacml.core.policy.impl.RuleType;
import org.herasaf.xacml.core.policy.requestinformationfactory.RequestInformationFactoryMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/ApplicationContext.xml" })
public class TestRuleOrderedDenyOverridesAlgorithm extends AbstractTestNGSpringContextTests{
	private RuleCombiningAlgorithm combAlg;
	private TargetMatcherMock targetMatcher;
	private RuleOrderedDenyOverridesAlgorithm[] ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray;
	private RuleOrderedDenyOverridesAlgorithm ruleOrderedDenyOverridesAlgorithmWithTRUEFALSEDecisions;
	@Autowired
	private RequestInformationFactoryMock requestInformationFactory;	
	public TestRuleOrderedDenyOverridesAlgorithm() {
		
		ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray = new RuleOrderedDenyOverridesAlgorithm[11];
		for (int i = 0; i < ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray.length; i++) {
			ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray[i] = new RuleOrderedDenyOverridesAlgorithm();
			ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray[i].setTargetMatcher(new TargetMatcherMock(
					new TargetMatcherMock.Decisions[] {
							TargetMatcherMock.Decisions.TRUE,
							TargetMatcherMock.Decisions.TRUE,
							TargetMatcherMock.Decisions.TRUE }));
		} 
		
		ruleOrderedDenyOverridesAlgorithmWithTRUEFALSEDecisions = new RuleOrderedDenyOverridesAlgorithm();
		ruleOrderedDenyOverridesAlgorithmWithTRUEFALSEDecisions.setTargetMatcher(new TargetMatcherMock(
				new TargetMatcherMock.Decisions[] {
						TargetMatcherMock.Decisions.TRUE,
						TargetMatcherMock.Decisions.FALSE,
						TargetMatcherMock.Decisions.FALSE }));
	}

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
										ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray[0],
						DecisionType.PERMIT, StatusCode.OK, false, },
				new Object[] {
						"permit true null, permit false null",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(false, null)),
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)), },
										ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray[1],
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
														ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray[2],
						DecisionType.PERMIT, StatusCode.OK, false, },
				new Object[] {
						"permit true null, permit true SyntaxException",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)),
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true,
												new SyntaxException("test"))), },
												ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray[3],
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
														ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray[4],
						DecisionType.PERMIT,
						StatusCode.OK, false, },
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
														ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray[5],
						DecisionType.INDETERMINATE,
						StatusCode.SYNTAX_ERROR, false, },
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
														ruleOrderedDenyOverridesAlgorithmWithTRUEFALSEDecisions,
						DecisionType.NOT_APPLICABLE, StatusCode.OK,
						false, },
				new Object[] {
						"permit true null, deny true null",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)),
								initializeRule(EffectType.DENY,
										new ConditionMock(true, null)), },
										ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray[6],
						DecisionType.DENY, StatusCode.OK, false, },
				new Object[] {
						"permit true null, permit false null",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(false, null)),
								initializeRule(EffectType.DENY,
										new ConditionMock(true, null)), },
										ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray[7],
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
														ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray[8],
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR, false, },
				new Object[] {
						"permit true null, deny true syntaxException",
						new RuleType[] {
								initializeRule(EffectType.PERMIT,
										new ConditionMock(true, null)),
								initializeRule(EffectType.DENY,
										new ConditionMock(true,
												new SyntaxException("test"))), },
												ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray[9],
						DecisionType.INDETERMINATE,
						StatusCode.SYNTAX_ERROR, false, },
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
														ruleOrderedDenyOverridesAlgorithmWithTRUEDecisionsArray[10],
						DecisionType.INDETERMINATE,
						StatusCode.MISSING_ATTRIBUTE, true, }, };
	}

	private RuleType initializeRule(EffectType effect, ConditionMock condition) {
		RuleType rule = new RuleType();
		rule.setCondition(condition);
		rule.setEffect(effect);
		return rule;
	}

	@BeforeMethod
	public void beforeTest() {
		targetMatcher = new TargetMatcherMock();
		combAlg = new RuleOrderedDenyOverridesAlgorithm();
		combAlg.setTargetMatcher(targetMatcher);
	}

	@Test(enabled = true)
	public void testID() throws Exception {
		assertEquals(combAlg.toString(),
				"urn:oasis:names:tc:xacml:1.1:rule-combining-algorithm:ordered-deny-overrides");
	}
	@Test(dataProvider = "testData")
	public void testCombiningAlg(String testID, RuleType[] rulesArray,
			RuleCombiningAlgorithm alg, DecisionType expectedDecision,
			StatusCode expectedStatusCode, boolean expectMissintAttribute)
			throws Exception {

		PolicyType policy = new PolicyTypeMock(rulesArray);
		List<IdReferenceType> references = new ArrayList<IdReferenceType>();
		RequestInformation infos = requestInformationFactory.createRequestInformation(references,null);
		DecisionType decision = alg.evaluate(null, policy, infos);

		assertEquals(decision, expectedDecision);
		assertEquals(infos.getStatusCode(), expectedStatusCode);
		if (expectMissintAttribute) {
			assertFalse(infos.getMissingAttributes().isEmpty());
		} else {
			assertTrue(infos.getMissingAttributes().isEmpty());
		}

	}
}
