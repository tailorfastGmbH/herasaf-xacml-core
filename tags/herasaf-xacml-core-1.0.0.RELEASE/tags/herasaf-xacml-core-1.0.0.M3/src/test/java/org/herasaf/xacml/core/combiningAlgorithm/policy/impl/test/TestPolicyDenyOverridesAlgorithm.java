/*
 * Copyright 2008 - 2012 HERAS-AF (www.herasaf.org)
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
package org.herasaf.xacml.core.combiningAlgorithm.policy.impl.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyDenyOverridesAlgorithm;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.ObligationType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This test tests the {@link PolicyDenyOverridesAlgorithm}. It tests various
 * combinations of different {@link PolicyCombiningAlgorithm}s and
 * {@link Evaluatable}s.
 * 
 * @author Florian Huonder
 * @author Stefan Oberholzer
 */
public class TestPolicyDenyOverridesAlgorithm extends
		TestPolicyCombiningAlgorithm {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PolicyCombiningAlgorithm getCombiningAlgorithm() {
		PolicyDenyOverridesAlgorithm alg = new PolicyDenyOverridesAlgorithm();
		return alg;
	}

	/**
	 * This test calls the test method in the super class (
	 * {@link TestPolicyCombiningAlgorithm}). This is only done to avoid
	 * duplicate code. This test method is equal for all combining algorithms.
	 */
	// @Test(dataProvider = "evaluatableCombinations")
	// public void testDenyOverridesCase(
	// PolicyCombiningAlgorithm alg, EvaluatableMock eval1,
	// EvaluationContext evaluationContext1, EvaluatableMock eval2,
	// EvaluationContext evaluationContext2, DecisionType expectedDecision,
	// List<ObligationType> expectedObligations,
	// StatusCode expectedStatusCode, Boolean expectedHasTargetMatched)
	// throws Exception {
	// super.testPolicySetMatchAndOneEvaluatable(alg, eval1, evaluationContext1,
	// eval2,
	// evaluationContext2, expectedDecision, expectedObligations,
	// expectedStatusCode, expectedHasTargetMatched);
	// }

	/*
	 * FIXME: Surefire blocks when the test data is fed through JUnit's
	 * DataProvider. If the test iterates manually over the data it works. This
	 * issue has to be investigated more deeply.
	 */

	@Test
	public void testDenyOverridesCase() throws Exception {
		Iterator<Object[]> cases = createTargetMatchAndOneEvaluatableTestData();
		while (cases.hasNext()) {
			Object[] singleCase = cases.next();
			PolicyCombiningAlgorithm alg = (PolicyCombiningAlgorithm) singleCase[0];
			EvaluatableMock eval1 = (EvaluatableMock) singleCase[1];
			EvaluationContext evaluationContext1 = (EvaluationContext) singleCase[2];
			EvaluatableMock eval2 = (EvaluatableMock) singleCase[3];
			EvaluationContext evaluationContext2 = (EvaluationContext) singleCase[4];
			DecisionType expectedDecision = (DecisionType) singleCase[5];
			@SuppressWarnings("unchecked")
			List<ObligationType> expectedObligations = (List<ObligationType>) singleCase[6];
			StatusCode expectedStatusCode = (StatusCode) singleCase[7];
			Boolean expectedHasTargetMatched = (Boolean) singleCase[8];

			super.testPolicySetMatchAndOneEvaluatable(alg, eval1,
					evaluationContext1, eval2, evaluationContext2,
					expectedDecision, expectedObligations, expectedStatusCode,
					expectedHasTargetMatched);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DecisionType evaluateDecision(EvaluatableMock eval1,
			EvaluatableMock eval2) {
		if (eval1.getDecision() == DecisionType.DENY
				|| eval2.getDecision() == DecisionType.DENY) {
			return DecisionType.DENY;
		}
		if (eval1.getDecision() == DecisionType.INDETERMINATE
				|| eval2.getDecision() == DecisionType.INDETERMINATE) {
			return DecisionType.DENY;
		}
		if (eval1.getDecision() == DecisionType.PERMIT
				|| eval2.getDecision() == DecisionType.PERMIT) {
			return DecisionType.PERMIT;
		}
		return DecisionType.NOT_APPLICABLE;
	}
}