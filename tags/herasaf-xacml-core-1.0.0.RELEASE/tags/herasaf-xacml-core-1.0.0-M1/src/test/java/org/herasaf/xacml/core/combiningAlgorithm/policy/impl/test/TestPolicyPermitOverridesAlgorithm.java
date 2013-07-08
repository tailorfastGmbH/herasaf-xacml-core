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
package org.herasaf.xacml.core.combiningAlgorithm.policy.impl.test;

import java.util.List;

import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyPermitOverridesAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.ObligationType;
import org.testng.annotations.Test;

/**
 * This test tests the {@link PolicyPermitOverridesAlgorithm}. It tests various
 * combinations of different {@link PolicyCombiningAlgorithm}s and
 * {@link Evaluatable}s.
 * 
 * @author Florian Huonder
 * @author Stefan Oberholzer
 */
public class TestPolicyPermitOverridesAlgorithm extends
		TestPolicyCombiningAlgorithm {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PolicyCombiningAlgorithm getCombiningAlgorithm() {
		PolicyPermitOverridesAlgorithm alg = new PolicyPermitOverridesAlgorithm();
		alg.setRespectAbandondEvaluatables(true);
		return alg;
	}

	/**
	 * This test calls the test method in the super class ({@link TestPolicyCombiningAlgorithm}).
	 * This is only done to avoid duplicate code. This test method is equal for all combining algorithms.
	 */
	@Test(enabled = true, dataProvider = "evaluatableCombinations")
	protected void testPolicySetMatchAndOneEvaluatable(
			PolicyCombiningAlgorithm alg, EvaluatableMock eval1,
			RequestInformation reqInfo1, EvaluatableMock eval2,
			RequestInformation reqInfo2, DecisionType expectedDecision,
			List<ObligationType> expectedObligations,
			StatusCode expectedStatusCode, Boolean expectedHasTargetMatched)
			throws Exception {
		super.testPolicySetMatchAndOneEvaluatable(alg, eval1, reqInfo1, eval2,
				reqInfo2, expectedDecision, expectedObligations,
				expectedStatusCode, expectedHasTargetMatched);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DecisionType evaluateDecision(EvaluatableMock eval1,
			EvaluatableMock eval2) {
		if (eval1.getDecision() == DecisionType.PERMIT
				|| eval2.getDecision() == DecisionType.PERMIT) {
			return DecisionType.PERMIT;
		}
		if (eval1.getDecision() == DecisionType.INDETERMINATE
				&& eval2.getDecision() == DecisionType.INDETERMINATE) {
			return DecisionType.INDETERMINATE;
		}
		if (eval1.getDecision() == DecisionType.INDETERMINATE
				|| eval2.getDecision() != DecisionType.INDETERMINATE) {
			return eval2.getDecision();
		}
		if (eval1.getDecision() != DecisionType.INDETERMINATE
				|| eval2.getDecision() == DecisionType.INDETERMINATE) {
			return eval1.getDecision();
		}
		if (eval1.getDecision() == DecisionType.DENY
				|| eval2.getDecision() == DecisionType.DENY) {
			return DecisionType.DENY;
		}
		return DecisionType.NOT_APPLICABLE;
	}
}