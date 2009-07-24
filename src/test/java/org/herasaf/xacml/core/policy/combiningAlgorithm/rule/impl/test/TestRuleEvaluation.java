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

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleCombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.MissingAttributeDetailType;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.herasaf.xacml.core.policy.ExpressionProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.combiningAlgorithm.mock.RuleCombiningAlgMock;
import org.herasaf.xacml.core.policy.combiningAlgorithm.mock.TargetMatcherMock;
import org.herasaf.xacml.core.policy.impl.ConditionType;
import org.herasaf.xacml.core.policy.impl.EffectType;
import org.herasaf.xacml.core.policy.impl.IdReferenceType;
import org.herasaf.xacml.core.policy.impl.RuleType;
import org.herasaf.xacml.core.policy.impl.TargetType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestRuleEvaluation {

	@DataProvider(name = "evaluationData")
	public Object[][] evaluationData() throws Exception {
		return new Object[][] {
				new Object[] {
						"DENY, true, missing attribute Exception, true",
						initializeRule(EffectType.DENY,
								new ConditionMock(true,
										new MissingAttributeException(
												"attribute ID",
												new StringDataTypeAttribute(),
												"issuer"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.TRUE),
						DecisionType.INDETERMINATE,
						StatusCode.MISSING_ATTRIBUTE,
						new MissingAttributeDetailType() },
				new Object[] {
						"Permit, true, null, true",
						initializeRule(EffectType.PERMIT, new ConditionMock(
								true, null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.TRUE),
						DecisionType.PERMIT, StatusCode.OK, null },
				new Object[] {
						"Permit, true, null, false",
						initializeRule(EffectType.PERMIT, new ConditionMock(
								true, null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.FALSE),
						DecisionType.NOT_APPLICABLE, StatusCode.OK, null },
				new Object[] {
						"Permit, true, null, processing exception",
						initializeRule(EffectType.PERMIT, new ConditionMock(
								true, null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.PROCESSINGEXCEPTION),
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR, null },
				new Object[] {
						"Permit, true, null, syntax exception",
						initializeRule(EffectType.PERMIT, new ConditionMock(
								true, null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.SYNTAXEXCEPTION),
						DecisionType.INDETERMINATE, StatusCode.SYNTAX_ERROR,
						null },
				new Object[] {
						"Permit, false, null, true",
						initializeRule(EffectType.PERMIT, new ConditionMock(
								false, null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.TRUE),
						DecisionType.NOT_APPLICABLE, StatusCode.OK, null },
				new Object[] {
						"Permit, false, null, false",
						initializeRule(EffectType.PERMIT, new ConditionMock(
								false, null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.FALSE),
						DecisionType.NOT_APPLICABLE, StatusCode.OK, null },
				new Object[] {
						"Permit, false, null, processing exception",
						initializeRule(EffectType.PERMIT, new ConditionMock(
								false, null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.PROCESSINGEXCEPTION),
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR, null },
				new Object[] {
						"Permit, false, null, syntax exception",
						initializeRule(EffectType.PERMIT, new ConditionMock(
								false, null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.SYNTAXEXCEPTION),
						DecisionType.INDETERMINATE, StatusCode.SYNTAX_ERROR,
						null },
				new Object[] {
						"Permit, true, processingException, true",
						initializeRule(EffectType.PERMIT,
								new ConditionMock(true,
										new ExpressionProcessingException(
												"test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.TRUE),
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR, null },
				new Object[] {
						"Permit, true, processingException, false",
						initializeRule(EffectType.PERMIT,
								new ConditionMock(true,
										new ExpressionProcessingException(
												"test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.FALSE),
						DecisionType.NOT_APPLICABLE, StatusCode.OK, null },
				new Object[] {
						"Permit, true, processingException, processing exception",
						initializeRule(EffectType.PERMIT,
								new ConditionMock(true,
										new ExpressionProcessingException(
												"test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.PROCESSINGEXCEPTION),
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR, null },
				new Object[] {
						"Permit, true, processingException, syntax exception",
						initializeRule(EffectType.PERMIT,
								new ConditionMock(true,
										new ExpressionProcessingException(
												"test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.SYNTAXEXCEPTION),
						DecisionType.INDETERMINATE, StatusCode.SYNTAX_ERROR,
						null },
				new Object[] {
						"Permit, true, syntaxException, true",
						initializeRule(EffectType.PERMIT, new ConditionMock(
								false, new SyntaxException("test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.TRUE),
						DecisionType.INDETERMINATE, StatusCode.SYNTAX_ERROR,
						null },
				new Object[] {
						"Permit, true, syntaxException, false",
						initializeRule(EffectType.PERMIT, new ConditionMock(
								false, new SyntaxException("test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.FALSE),
						DecisionType.NOT_APPLICABLE, StatusCode.OK, null },
				new Object[] {
						"Permit, true, syntaxException, processing exception",
						initializeRule(EffectType.PERMIT, new ConditionMock(
								false, new SyntaxException("test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.PROCESSINGEXCEPTION),
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR, null },
				new Object[] {
						"Permit, true, syntaxException, syntax exception",
						initializeRule(EffectType.PERMIT, new ConditionMock(
								false, new SyntaxException("test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.SYNTAXEXCEPTION),
						DecisionType.INDETERMINATE, StatusCode.SYNTAX_ERROR,
						null },
				new Object[] {
						"DENY, true, null, true",
						initializeRule(EffectType.DENY, new ConditionMock(true,
								null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.TRUE),
						DecisionType.DENY, StatusCode.OK, null },
				new Object[] {
						"DENY, true, null, false",
						initializeRule(EffectType.DENY, new ConditionMock(true,
								null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.FALSE),
						DecisionType.NOT_APPLICABLE, StatusCode.OK, null },
				new Object[] {
						"DENY, true, null, processing exception",
						initializeRule(EffectType.DENY, new ConditionMock(true,
								null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.PROCESSINGEXCEPTION),
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR, null },
				new Object[] {
						"DENY, true, null, syntax exception",
						initializeRule(EffectType.DENY, new ConditionMock(true,
								null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.SYNTAXEXCEPTION),
						DecisionType.INDETERMINATE, StatusCode.SYNTAX_ERROR,
						null },
				new Object[] {
						"DENY, false, null, true",
						initializeRule(EffectType.DENY, new ConditionMock(
								false, null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.TRUE),
						DecisionType.NOT_APPLICABLE, StatusCode.OK, null },
				new Object[] {
						"DENY, false, null, false",
						initializeRule(EffectType.DENY, new ConditionMock(
								false, null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.FALSE),
						DecisionType.NOT_APPLICABLE, StatusCode.OK, null },
				new Object[] {
						"DENY, false, null, processing exception",
						initializeRule(EffectType.DENY, new ConditionMock(
								false, null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.PROCESSINGEXCEPTION),
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR, null },
				new Object[] {
						"DENY, false, null, syntax exception",
						initializeRule(EffectType.DENY, new ConditionMock(
								false, null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.SYNTAXEXCEPTION),
						DecisionType.INDETERMINATE, StatusCode.SYNTAX_ERROR,
						null },
				new Object[] {
						"DENY, true, processingException, true",
						initializeRule(EffectType.DENY, new ConditionMock(true,
								new ExpressionProcessingException("test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.TRUE),
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR, null },
				new Object[] {
						"DENY, true, processingException, false",
						initializeRule(EffectType.DENY, new ConditionMock(true,
								new ExpressionProcessingException("test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.FALSE),
						DecisionType.NOT_APPLICABLE, StatusCode.OK, null },
				new Object[] {
						"DENY, true, processingException, processing exception",
						initializeRule(EffectType.DENY, new ConditionMock(true,
								new ExpressionProcessingException("test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.PROCESSINGEXCEPTION),
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR, null },
				new Object[] {
						"DENY, true, processingException, syntax exception",
						initializeRule(EffectType.DENY, new ConditionMock(true,
								new ExpressionProcessingException("test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.SYNTAXEXCEPTION),
						DecisionType.INDETERMINATE, StatusCode.SYNTAX_ERROR,
						null },
				new Object[] {
						"DENY, true, syntaxException, true",
						initializeRule(EffectType.DENY, new ConditionMock(
								false, new SyntaxException("test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.TRUE),
						DecisionType.INDETERMINATE, StatusCode.SYNTAX_ERROR,
						null },
				new Object[] {
						"DENY, true, syntaxException, false",
						initializeRule(EffectType.DENY, new ConditionMock(
								false, new SyntaxException("test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.FALSE),
						DecisionType.NOT_APPLICABLE, StatusCode.OK, null },
				new Object[] {
						"DENY, true, syntaxException, processing exception",
						initializeRule(EffectType.DENY, new ConditionMock(
								false, new SyntaxException("test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.PROCESSINGEXCEPTION),
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR, null },
				new Object[] {
						"DENY, true, syntaxException, syntax exception",
						initializeRule(EffectType.DENY, new ConditionMock(
								false, new SyntaxException("test"))),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.SYNTAXEXCEPTION),
						DecisionType.INDETERMINATE, StatusCode.SYNTAX_ERROR,
						null },
				new Object[] {
						"true, wrong type, null, true",
						initializeRule(EffectType.DENY, new ConditionMock(
								"Hallo", null)),
						initializeRuleCombiningAlg(TargetMatcherMock.Decisions.TRUE),
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR, null }, };
	}

	private RuleType initializeRule(EffectType effect, ConditionType condition) {
		RuleType rule = new RuleType();
		rule.setTarget(new TargetType());
		rule.setEffect(effect);
		rule.setCondition(condition);
		return rule;
	}

	private RuleCombiningAlgorithm initializeRuleCombiningAlg(
			TargetMatcherMock.Decisions decision) {
		TargetMatcherMock targetMatcher = new TargetMatcherMock(
				new TargetMatcherMock.Decisions[] { decision });
		return new RuleCombiningAlgMock(targetMatcher);
	}

	@Test(dataProvider = "evaluationData")
	public void testEvaluateRule(String testID, RuleType rule,
			RuleCombiningAlgMock combAlg, DecisionType expectedDecision,
			StatusCode expectedStatusCode,
			MissingAttributeDetailType expectedMissingAttribute)
			throws Exception {
		List<IdReferenceType> references = new ArrayList<IdReferenceType>();
		RequestInformation info = new RequestInformation(null);
		DecisionType madeDecision = combAlg.evaluateRule(null, rule, info);
		assertEquals(madeDecision, expectedDecision);
		assertEquals(info.getStatusCode(), expectedStatusCode);
		if (expectedMissingAttribute != null) {
			assertEquals(info.getMissingAttributes().size(), 1);
		} else {
			assertEquals(info.getMissingAttributes().size(), 0);
		}
	}

}
