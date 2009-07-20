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
package org.herasaf.xacml.core.combiningAlgorithm.policy.impl.test;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.EffectType;
import org.herasaf.xacml.core.policy.impl.ObligationType;
import org.testng.annotations.DataProvider;

/**
 * @author Florian Huonder
 * @author Stefan Oberholzer
 */
public abstract class TestPolicyCombiningAlgorithm {

	protected abstract PolicyCombiningAlgorithm getCombiningAlgorithm();

	protected abstract DecisionType evaluateDecision(EvaluatableMock eval1,
			EvaluatableMock eval2);

	private static final Boolean[] trueFalse = { true, false };

	@DataProvider(name = "evaluatableCombinations")
	public Object[][] testTargetMatchAndOneEvaluatable() throws Exception {
		List<Object[]> data = new ArrayList<Object[]>();

		for (EvaluatableMock templEval1 : createEvalCombinations()) {
			for (RequestInformation reqInfo1 : createReqInfoCombinations(templEval1))
				for (EvaluatableMock templEval2 : createEvalCombinations()) {
					for (RequestInformation reqInfo2 : createReqInfoCombinations(templEval2)) {
						EvaluatableMock eval1 = templEval2.clone();
						eval1.setReqInfo(reqInfo1);
						EvaluatableMock eval2 = templEval2.clone();
						eval2.setReqInfo(reqInfo2);
						Object[] dataSet = new Object[9];
						dataSet[0] = getCombiningAlgorithm();
						dataSet[1] = eval1;
						dataSet[2] = reqInfo1;
						dataSet[3] = eval2;
						dataSet[4] = reqInfo2;
						dataSet[5] = evaluateDecision(eval1, eval2);
						dataSet[6] = determineObligations(
								(DecisionType) dataSet[5], reqInfo1, eval1,
								reqInfo2, eval2);
						dataSet[7] = determineStatusCode(
								(DecisionType) dataSet[5], reqInfo1, eval1,
								reqInfo2, eval2);
						dataSet[8] = true;
						data.add(dataSet);

						// for debugging
						// int point = 1280;
						// if (data.size() == point){
						// Object[][] retVal = new Object[data.size()][9];
						// for (int i = 0; i < data.size(); i++) {
						// retVal[i] = data.get(i);
						// }

						// Object[][] retVal = new Object[1][9];
						// retVal[0] = data.get(point - 1);
						// return retVal;
						// }
					}
				}
		}
		Object[][] retVal = new Object[data.size()][9];
		for (int i = 0; i < data.size(); i++) {
			retVal[i] = data.get(i);
		}
		return retVal;
	}

	protected static List<RequestInformation> createReqInfoCombinations(
			EvaluatableMock eval) {
		List<RequestInformation> reqInfos = new ArrayList<RequestInformation>();
		switch (eval.getDecision()) {
		case INDETERMINATE:
			reqInfos.add(buildRequestInformation(false, false,
					StatusCode.MISSING_ATTRIBUTE, true));
			reqInfos.add(buildRequestInformation(false, false,
					StatusCode.PROCESSING_ERROR, true));
			reqInfos.add(buildRequestInformation(false, false,
					StatusCode.SYNTAX_ERROR, true));
			reqInfos.add(buildRequestInformation(false, true,
					StatusCode.SYNTAX_ERROR, false));
			reqInfos.add(buildRequestInformation(false, false,
					StatusCode.MISSING_ATTRIBUTE, true));
			break;
		case NOT_APPLICABLE:
			reqInfos.add(buildRequestInformation(false, true, StatusCode.OK,
					false));
			reqInfos.add(buildRequestInformation(false, false, StatusCode.OK,
					false));
			break;
		case PERMIT: // Same as Deny
		case DENY:
			for (boolean reqInfReturnesPermitObligation : trueFalse) {
				for (boolean reqInfReturnesDenyObligation : trueFalse) {
					reqInfos.add(buildRequestInformation(
							reqInfReturnesPermitObligation,
							reqInfReturnesDenyObligation, StatusCode.OK, true));
				}
			}
		}

		return reqInfos;
	}

	protected static RequestInformation buildRequestInformation(
			boolean reqInfReturnesPermitObligation,
			boolean reqInfReturnesDenyObligation, StatusCode reqInfStatusCode,
			boolean reqInfTargetMatched) {
		RequestInformation reqInfo = new RequestInformation(null, null);
		reqInfo.setTargetMatched(reqInfTargetMatched);
		reqInfo.updateStatusCode(reqInfStatusCode);
		if (reqInfReturnesPermitObligation) {
			List<ObligationType> obligations = new ArrayList<ObligationType>();
			obligations.add(new ObligationType("permitObligation",
					EffectType.PERMIT));
			reqInfo.addObligations(obligations, EffectType.PERMIT);
		}
		if (reqInfReturnesDenyObligation) {
			List<ObligationType> obligations = new ArrayList<ObligationType>();
			obligations.add(new ObligationType("denyObligation",
					EffectType.DENY));
			reqInfo.addObligations(obligations, EffectType.DENY);
		}
		return reqInfo;
	}

	protected static List<EvaluatableMock> createEvalCombinations() {
		List<EvaluatableMock> evals = new ArrayList<EvaluatableMock>();
		for (DecisionType decision : DecisionType.values()) {
			if (decision == DecisionType.INDETERMINATE) {
				evals.add(new EvaluatableMock(false, false, decision));
				evals.add(new EvaluatableMock(false, true, decision)); // an
				// wrong
				// combination
			} else if (decision == DecisionType.NOT_APPLICABLE) {
				evals.add(new EvaluatableMock(false, false, decision));
			} else {
				for (boolean hasDenyObligation : trueFalse) {
					for (boolean hasPermitObligation : trueFalse) {
						evals.add(new EvaluatableMock(hasDenyObligation,
								hasPermitObligation, decision));
					}
				}
			}
		}

		return evals;

	}

	protected void addObligations(List<ObligationType> obligations,
			RequestInformation reqInf, DecisionType algDecision) {
		for (ObligationType obl : reqInf.getObligations().getObligations()) {
			if (obl.getFulfillOn().equals(EffectType.PERMIT)
					&& algDecision.equals(DecisionType.PERMIT)) {
				obligations.add(obl);
			}
			if (obl.getFulfillOn().equals(EffectType.DENY)
					&& algDecision.equals(DecisionType.DENY)) {
				obligations.add(obl);
			}
		}

	}

	private Object determineObligations(DecisionType algDecision,
			RequestInformation requestInf1, EvaluatableMock eval1,
			RequestInformation requestInf2, EvaluatableMock eval2) {
		List<ObligationType> obligations = new ArrayList<ObligationType>();

		// The check that both are not indeterminate is because the aldDecision
		// overrides with DENY in case of an INDETERMINATE.
		if ((algDecision == DecisionType.PERMIT || algDecision == DecisionType.DENY)
				&& (!eval1.getDecision().equals(DecisionType.INDETERMINATE) && !eval2
						.getDecision().equals(DecisionType.INDETERMINATE))) {
			addObligations(obligations, requestInf1, algDecision);
			addObligations(obligations, requestInf2, algDecision);
			if (algDecision == DecisionType.PERMIT) {
				if (eval1.getPermitObligation() != null) {
					obligations.add(eval1.getPermitObligation());
				}
				if (eval2.getPermitObligation() != null) {
					obligations.add(eval2.getPermitObligation());
				}
			} else {
				if (eval1.getDenyObligation() != null) {
					obligations.add(eval1.getDenyObligation());
				}
				if (eval2.getDenyObligation() != null) {
					obligations.add(eval2.getDenyObligation());
				}
			}
		}
		return obligations;
	}

	protected Object determineStatusCode(DecisionType algDecision,
			RequestInformation requestInf1, EvaluatableMock eval1,
			RequestInformation requestInf2, EvaluatableMock eval2) {
		if (algDecision == DecisionType.PERMIT) {
			return StatusCode.OK;
		}
		if (algDecision == DecisionType.DENY) {
			return StatusCode.OK;
		}
		if (algDecision == DecisionType.INDETERMINATE) {
			if (requestInf1.getStatusCode() == StatusCode.SYNTAX_ERROR
					|| requestInf2.getStatusCode() == StatusCode.SYNTAX_ERROR) {
				return StatusCode.SYNTAX_ERROR;
			}
			if (requestInf1.getStatusCode() == StatusCode.MISSING_ATTRIBUTE
					|| requestInf2.getStatusCode() == StatusCode.MISSING_ATTRIBUTE) {
				return StatusCode.MISSING_ATTRIBUTE;
			}

			return StatusCode.PROCESSING_ERROR; // In case of one request
			// information contains status
			// code
			// Processing_error

		}
		return StatusCode.OK;

	}

	// @Test(enabled = true, dataProvider = "evaluatableCombinations")
	protected void testPolicySetMatchAndOneEvaluatable(
			PolicyCombiningAlgorithm alg, EvaluatableMock eval1,
			RequestInformation reqInfo1, EvaluatableMock eval2,
			RequestInformation reqInfo2, DecisionType expectedDecision,
			List<ObligationType> expectedObligations,
			StatusCode expectedStatusCode, Boolean expectedHasTargetMatched)
			throws Exception {
		RequestInformation reqInfo = new RequestInformation(null, null);
		List<Evaluatable> evals = new ArrayList<Evaluatable>();
		evals.add(eval1);
		evals.add(eval2);
		DecisionType decision = alg.evaluateEvaluatableList(new RequestType(),
				evals, reqInfo);

		assertEquals(decision, expectedDecision);

		assertEquals(reqInfo.getStatusCode(), expectedStatusCode);
		assertEquals((Boolean) reqInfo.isTargetMatched(),
				expectedHasTargetMatched);
		if (reqInfo.getObligations() == null) {
			assertEquals(0, expectedObligations.size());
		} else {
			assertEquals(reqInfo.getObligations().getObligations().size(),
					expectedObligations.size());
		}
	}
}