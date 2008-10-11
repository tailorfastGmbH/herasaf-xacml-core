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

package org.herasaf.xacml.core.policy.combiningAlgorithm.policy.impl.test;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.combiningAlgorithm.AbstractCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.OnlyOneApplicableAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.combiningAlgorithm.mock.PolicyCombiningAlgMock;
import org.herasaf.xacml.core.policy.combiningAlgorithm.mock.RuleCombiningAlgMock;
import org.herasaf.xacml.core.policy.combiningAlgorithm.mock.TargetMatcherMock;
import org.herasaf.xacml.core.policy.impl.PolicySetType;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.herasaf.xacml.core.policy.impl.TargetType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class TestOnlyOneApplicableAlgorithm {
	PolicyCombiningAlgorithm combAlg;
	TargetMatcherMock targetMatcher;

	@DataProvider(name = "testPolicyCombinations")
	public Object[][] testPolicyCombinations() throws Exception {
		return new Object[][] {
				new Object[] {
						"Policy: Permit, NotApplicable.",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null, false),
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null, false) },
						DecisionType.PERMIT, StatusCode.OK },
				new Object[] {
						"Policy: Permit, Deny.",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null, false),
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null, false) },
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR },
				new Object[] {
						"Policy: Permit, Indeterminate.",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null, false),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.OK, null, false) },
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR },
				new Object[] {
						"Policy: Deny, NotApplicable.",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null, false),
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null, false) },
						DecisionType.DENY, StatusCode.OK },
				new Object[] {
						"Policy: Deny, Deny.",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null, false),
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null, false) },
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR },
				new Object[] {
						"Policy: Deny, Indeterminate.",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null, false),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.SYNTAX_ERROR, null, false) },
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR },
				new Object[] {
						"Policy: Deny, Target Indeterminate.",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null, false),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.SYNTAX_ERROR, null, true) },
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR },
				new Object[] {
						"Policy: Indeterminate, Target Indeterminate.",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.PROCESSING_ERROR, null,
										false),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.SYNTAX_ERROR, null, true) },
						DecisionType.INDETERMINATE,
						StatusCode.PROCESSING_ERROR },
				new Object[] {
						"Policy: NotApplicable, Target NotApplicable.",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null,
										false),
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null, false) },
						DecisionType.NOT_APPLICABLE,
						StatusCode.OK }, };

	}

	@BeforeMethod
	public void beforeTest() {
		targetMatcher = new TargetMatcherMock();
		combAlg = new OnlyOneApplicableAlgorithm();
		((AbstractCombiningAlgorithm)combAlg).setTargetMatcher(targetMatcher);
	}

	@Test(enabled = true)
	public void testID() throws Exception {
		assertEquals(combAlg.toString(),
				"urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:only-one-applicable");
	}

	@Test(enabled = true, dataProvider = "testPolicyCombinations")
	public void testPolicySetCombining(
			String testID, // for debugging
			PolicyCombiningAlgorithm[] policyCombiningAlgs,
			DecisionType decision, StatusCode decisionStatusCode)
			throws Exception {

		List<Evaluatable> policies = new ArrayList<Evaluatable>();
		Map<String, Evaluatable> references = new HashMap<String, Evaluatable>();
		RequestInformation reqEvals = new RequestInformation(references, null);

		for (PolicyCombiningAlgorithm algs : policyCombiningAlgs) {
			policies.add(initializePolicy(algs));
		}
		assertEquals(combAlg.evaluateEvaluatableList(null, policies, reqEvals),
				decision);
		assertEquals(reqEvals.getStatusCode(), decisionStatusCode);
	}

	@Test(enabled = true)
	public void testWrongEvaluatable() throws Exception {
		Map<String, Evaluatable> references = new HashMap<String, Evaluatable>();
		RequestInformation reqEvals = new RequestInformation(references, null);

		PolicyType p = new PolicyType();
		p
				.setCombiningAlg(new RuleCombiningAlgMock(
						DecisionType.PERMIT));
		TargetType tt = new TargetType();
		p.setTarget(tt);

		targetMatcher
				.setDecision(new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.TRUE });
		assertEquals(combAlg.evaluate(null, p, reqEvals),
				DecisionType.INDETERMINATE);
	}

	@Test(enabled = true)
	public void testNullInEvaluatableList() throws Exception {
		List<Evaluatable> policies = new ArrayList<Evaluatable>();
		Map<String, Evaluatable> references = new HashMap<String, Evaluatable>();
		RequestInformation reqEvals = new RequestInformation(references, null);

		policies.add(initializePolicy(new PolicyCombiningAlgMock(
				DecisionType.PERMIT, StatusCode.OK, null)));
		policies.add(initializePolicy(new PolicyCombiningAlgMock(
				DecisionType.PERMIT, StatusCode.OK, null)));
		policies.add(null);
		policies.add(initializePolicy(new PolicyCombiningAlgMock(
				DecisionType.PERMIT, StatusCode.OK, null)));

		assertEquals(combAlg.evaluateEvaluatableList(null, policies, reqEvals),
				DecisionType.INDETERMINATE);
	}



	@Test(enabled = true)
	public void testPolicyAsEvaluatable() throws Exception {
		Map<String, Evaluatable> references = new HashMap<String, Evaluatable>();
		RequestInformation reqEvals = new RequestInformation(references, null);
		targetMatcher
				.setDecision(new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.TRUE });
		assertEquals(combAlg.evaluate(null, new PolicyType(), reqEvals),
				DecisionType.INDETERMINATE);
	}

	@Test(enabled = true)
	public void testNullCombiningAlg() throws Exception {
		List<Evaluatable> policies = new ArrayList<Evaluatable>();
		Map<String, Evaluatable> references = new HashMap<String, Evaluatable>();
		RequestInformation reqEvals = new RequestInformation(references, null);

		policies.add(new PolicySetType());
		assertEquals(combAlg.evaluateEvaluatableList(null, policies, reqEvals),
				DecisionType.INDETERMINATE);
	}

	private PolicySetType initializePolicy(PolicyCombiningAlgorithm algorithm) {
		PolicySetType policySet = new PolicySetType();
		policySet.setCombiningAlg(algorithm);
		TargetType tt = new TargetType();
		policySet.setTarget(tt);
		return policySet;
	}

}
