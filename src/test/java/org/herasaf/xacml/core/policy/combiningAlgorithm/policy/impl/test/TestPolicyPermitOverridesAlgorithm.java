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
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.herasaf.xacml.core.combiningAlgorithm.AbstractCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyPermitOverridesAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.MissingAttributeDetailType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.combiningAlgorithm.mock.PolicyCombiningAlgMock;
import org.herasaf.xacml.core.policy.combiningAlgorithm.mock.RuleCombiningAlgMock;
import org.herasaf.xacml.core.policy.combiningAlgorithm.mock.TargetMatcherMock;
import org.herasaf.xacml.core.policy.impl.ObjectFactory;
import org.herasaf.xacml.core.policy.impl.PolicySetType;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.herasaf.xacml.core.policy.impl.TargetType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class TestPolicyPermitOverridesAlgorithm {
	PolicyCombiningAlgorithm combAlg;
//	RequestType req;
	TargetMatcherMock targetMatcher;
	ObjectFactory factory = new ObjectFactory();

	@DataProvider(name = "testTargetMatchAndOneEvaluatable")
	public Object[][] testTargetMatchAndOneEvaluatable() throws Exception {
		return new Object[][] {
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.PERMIT, StatusCode.OK,
								null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.TRUE },
						DecisionType.PERMIT },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.PERMIT, StatusCode.OK,
								null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.FALSE },
						DecisionType.NOT_APPLICABLE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.PERMIT, StatusCode.OK,
								null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.PROCESSINGEXCEPTION },
						DecisionType.INDETERMINATE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.PERMIT, StatusCode.OK,
								null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.SYNTAXEXCEPTION },
						DecisionType.INDETERMINATE },
				new Object[] {
						new PolicyCombiningAlgMock(DecisionType.DENY,
								StatusCode.OK, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.FALSE },
						DecisionType.NOT_APPLICABLE },
				new Object[] {
						new PolicyCombiningAlgMock(DecisionType.DENY,
								StatusCode.MISSING_ATTRIBUTE, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.TRUE },
						DecisionType.DENY },
				new Object[] {
						new PolicyCombiningAlgMock(DecisionType.DENY,
								StatusCode.MISSING_ATTRIBUTE, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.SYNTAXEXCEPTION },
						DecisionType.INDETERMINATE },
				new Object[] {
						new PolicyCombiningAlgMock(DecisionType.DENY,
								StatusCode.OK, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.PROCESSINGEXCEPTION },
						DecisionType.INDETERMINATE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.INDETERMINATE,
								StatusCode.MISSING_ATTRIBUTE,
								new MissingAttributeDetailType()),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.FALSE },
						DecisionType.NOT_APPLICABLE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.INDETERMINATE,
								StatusCode.MISSING_ATTRIBUTE,
								new MissingAttributeDetailType()),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.TRUE },
						DecisionType.INDETERMINATE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.INDETERMINATE,
								StatusCode.MISSING_ATTRIBUTE,
								new MissingAttributeDetailType()),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.PROCESSINGEXCEPTION },
						DecisionType.INDETERMINATE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.INDETERMINATE,
								StatusCode.MISSING_ATTRIBUTE,
								new MissingAttributeDetailType()),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.SYNTAXEXCEPTION },
						DecisionType.INDETERMINATE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.INDETERMINATE,
								StatusCode.PROCESSING_ERROR, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.FALSE },
						DecisionType.NOT_APPLICABLE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.INDETERMINATE,
								StatusCode.PROCESSING_ERROR, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.TRUE },
						DecisionType.INDETERMINATE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.INDETERMINATE,
								StatusCode.PROCESSING_ERROR, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.PROCESSINGEXCEPTION },
						DecisionType.INDETERMINATE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.INDETERMINATE,
								StatusCode.PROCESSING_ERROR, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.SYNTAXEXCEPTION },
						DecisionType.INDETERMINATE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.INDETERMINATE,
								StatusCode.SYNTAX_ERROR, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.FALSE },
						DecisionType.NOT_APPLICABLE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.INDETERMINATE,
								StatusCode.SYNTAX_ERROR, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.TRUE },
						DecisionType.INDETERMINATE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.INDETERMINATE,
								StatusCode.SYNTAX_ERROR, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.PROCESSINGEXCEPTION },
						DecisionType.INDETERMINATE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.INDETERMINATE,
								StatusCode.SYNTAX_ERROR, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.SYNTAXEXCEPTION },
						DecisionType.INDETERMINATE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.NOT_APPLICABLE,
								StatusCode.OK, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.FALSE },
						DecisionType.NOT_APPLICABLE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.NOT_APPLICABLE,
								StatusCode.OK, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.TRUE },
						DecisionType.NOT_APPLICABLE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.NOT_APPLICABLE,
								StatusCode.OK, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.SYNTAXEXCEPTION },
						DecisionType.INDETERMINATE },
				new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.NOT_APPLICABLE,
								StatusCode.OK, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.PROCESSINGEXCEPTION },
						DecisionType.INDETERMINATE },
						new Object[] {
						new PolicyCombiningAlgMock(
								DecisionType.PERMIT,
								StatusCode.OK, null),
						new TargetMatcherMock.Decisions[] { TargetMatcherMock.Decisions.NULLPOINTEREXCEPTION },
						DecisionType.INDETERMINATE }};
	}

	@DataProvider(name = "testPolicyCombinations")
	public Object[][] testPolicyCombinations() throws Exception {
		return new Object[][] {
				new Object[] {
						"Permit, Permit. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null) },
						DecisionType.PERMIT },
				new Object[] {
						"Permit, Deny. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null) },
						DecisionType.PERMIT },
				new Object[] {
						"Permit, Deny Missing Attr. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.MISSING_ATTRIBUTE,
										new MissingAttributeDetailType()) },
						DecisionType.PERMIT },
				new Object[] {
						"Permit, Indeterminate missing attr. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.MISSING_ATTRIBUTE,
										new MissingAttributeDetailType()) },
						DecisionType.PERMIT },
				new Object[] {
						"Permit, Indeterminate processing error. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.PROCESSING_ERROR, null) },
						DecisionType.PERMIT },
				new Object[] {
						"Permit, Indeterminate Syntax error. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.SYNTAX_ERROR, null) },
						DecisionType.PERMIT },
				new Object[] {
						"Permit, NotApplicable. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null) },
						DecisionType.PERMIT },
				new Object[] {
						"Deny, Permit. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null) },
						DecisionType.PERMIT },
				new Object[] {
						"Deny, Deny. Result: Deny",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null) },
						DecisionType.DENY },
				new Object[] {
						"Deny, Indeterminate. Result: Deny",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.OK, null) },
						DecisionType.DENY },
				new Object[] {
						"Deny, NotApplicable. Result: Deny",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null) },
						DecisionType.DENY },
				new Object[] {
						"Indeterminate missing Attribute, Permit. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.MISSING_ATTRIBUTE,
										new MissingAttributeDetailType()),
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null) },
						DecisionType.PERMIT },
				new Object[] {
						"Indeterminate Processing Error, Permit. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.PROCESSING_ERROR, null),
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null) },
						DecisionType.PERMIT },
				new Object[] {
						"Indeterminate syntax error, Permit. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.SYNTAX_ERROR, null),
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null) },
						DecisionType.PERMIT },
				new Object[] {
						"Indeterminate missingAttribute, Indeterminate Missing Attribute. Result: Indeterminate",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.MISSING_ATTRIBUTE,
										new MissingAttributeDetailType()),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.MISSING_ATTRIBUTE,
										new MissingAttributeDetailType()) },
						DecisionType.INDETERMINATE },
				new Object[] {
						"Indeterminate missing Attribute, Indeterminate Processing Error. Result: Indeterminate",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.MISSING_ATTRIBUTE,
										new MissingAttributeDetailType()),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.PROCESSING_ERROR, null) },
						DecisionType.INDETERMINATE },
				new Object[] {
						"Indeterminate missing Attribute, Indeterminate syntax error. Result: Indeterminate",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.MISSING_ATTRIBUTE,
										new MissingAttributeDetailType()),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.SYNTAX_ERROR, null) },
						DecisionType.INDETERMINATE },
				new Object[] {
						"Indeterminate processing error, Indeterminate processing error. Result: Indeterminate",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.PROCESSING_ERROR, null),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.PROCESSING_ERROR, null) },
						DecisionType.INDETERMINATE },
				new Object[] {
						"Indeterminate processing error, Indeterminate syntax error. Result: Indeterminate",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.PROCESSING_ERROR, null),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.SYNTAX_ERROR, null) },
						DecisionType.INDETERMINATE },
				new Object[] {
						"Indeterminate syntax error, Indeterminate syntax error. Result: Indeterminate",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.SYNTAX_ERROR, null),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.SYNTAX_ERROR, null) },
						DecisionType.INDETERMINATE },
				new Object[] {
						"Indeterminate missing Attribute, Not applicable. Result: Indeterminate",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.MISSING_ATTRIBUTE,
										new MissingAttributeDetailType()),
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null) },
						DecisionType.INDETERMINATE },
				new Object[] {
						"Indeterminate processing error, Not applicable. Result: Indeterminate",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.PROCESSING_ERROR, null),
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null) },
						DecisionType.INDETERMINATE },
				new Object[] {
						"Indeterminate syntax error, Not applicable. Result: Indeterminate",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.SYNTAX_ERROR, null),
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null) },
						DecisionType.INDETERMINATE },
				new Object[] {
						"Not Applicable, Permit. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null) },
						DecisionType.PERMIT },
				new Object[] {
						"Not Applicable, Deny. Result: Deny",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null) },
						DecisionType.DENY },
				new Object[] {
						"Not Applicable, Deny missing attribute. Result: Deny",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.MISSING_ATTRIBUTE,
										new MissingAttributeDetailType()) },
						DecisionType.DENY },
				new Object[] {
						"Not Applicable, Indeterminate missing attribute. Result: Indeterminate",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.MISSING_ATTRIBUTE,
										new MissingAttributeDetailType()) },
						DecisionType.INDETERMINATE },
				new Object[] {
						"Not Applicable, Indeterminate processing error. Result: Indeterminate",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.PROCESSING_ERROR, null) },
						DecisionType.INDETERMINATE },
				new Object[] {
						"Not Applicable, Indeterminate syntax error. Result: Indeterminate",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.SYNTAX_ERROR, null) },
						DecisionType.INDETERMINATE },
				new Object[] {
						"Not Applicable, Not Applicable. Result: Not Applicable",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null) },
						DecisionType.NOT_APPLICABLE },

				new Object[] {
						"Permit, Deny, NotApplicable. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null) },
						DecisionType.PERMIT },
				new Object[] {
						"Permit, Deny missing Attribute, NotApplicable. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.MISSING_ATTRIBUTE,
										new MissingAttributeDetailType()),
								new PolicyCombiningAlgMock(
										DecisionType.NOT_APPLICABLE,
										StatusCode.OK, null) },
						DecisionType.PERMIT },
				new Object[] {
						"Permit, Deny missing Attribute, Indeterminate Missing Attribute. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.MISSING_ATTRIBUTE,
										new MissingAttributeDetailType()) },
						DecisionType.PERMIT },
				new Object[] {
						"Permit, Deny , Indeterminate Processing Error. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.PROCESSING_ERROR, null) },
						DecisionType.PERMIT },
				new Object[] {
						"Permit, Deny missing Attribute, Indeterminate Syntax Error. Result: Permit",
						new PolicyCombiningAlgMock[] {
								new PolicyCombiningAlgMock(
										DecisionType.PERMIT,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.DENY,
										StatusCode.OK, null),
								new PolicyCombiningAlgMock(
										DecisionType.INDETERMINATE,
										StatusCode.SYNTAX_ERROR, null) },
						DecisionType.PERMIT }, };
	}

	@BeforeMethod
	public void beforeTest() {
		targetMatcher = new TargetMatcherMock();
		combAlg = new PolicyPermitOverridesAlgorithm();
		((AbstractCombiningAlgorithm)combAlg).setTargetMatcher(targetMatcher);
	}

	@Test(enabled = true)
	public void testID() throws Exception {
		assertEquals(combAlg.toString(),
				"urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:permit-overrides");
	}

	@Test(enabled = true, dataProvider = "testTargetMatchAndOneEvaluatable")
	public void testPolicySetMatchAndOneEvaluatable(
			PolicyCombiningAlgorithm addedPolicySet,
			TargetMatcherMock.Decisions[] targetDecision,
			DecisionType decision) throws Exception {

		Map<String, Evaluatable> references = new HashMap<String, Evaluatable>();
		RequestInformation reqEvals = new RequestInformation(references, null);
		PolicySetType p = initializePolicy(new PolicyCombiningAlgMock(
				DecisionType.PERMIT, StatusCode.OK, null));
		addPolicySet(p, addedPolicySet);
		targetMatcher.setDecision(targetDecision);
		assertEquals(combAlg.evaluate(null, p, reqEvals), decision);
	}

	@Test(enabled = true, dataProvider = "testPolicyCombinations")
	public void testPolicySetCombining(
			String testID, // for debugging
			PolicyCombiningAlgorithm[] policyCombiningAlgs,
			DecisionType decision) throws Exception {

		List<Evaluatable> policies = new ArrayList<Evaluatable>();
		Map<String, Evaluatable> references = new HashMap<String, Evaluatable>();
		RequestInformation reqEvals = new RequestInformation(references, null);

		for (PolicyCombiningAlgorithm algs : policyCombiningAlgs) {
			policies.add(initializePolicy(algs));
		}
		assertEquals(combAlg.evaluateEvaluatableList(null, policies, reqEvals), decision);

		List<MissingAttributeDetailType> details = new ArrayList<MissingAttributeDetailType>();

		RequestInformation info = new RequestInformation(null, null);
		// Used to ensure that the strongest statusCode will stay
		switch (decision) {
		case DENY:
			for (PolicyCombiningAlgorithm alg : policyCombiningAlgs) {
				PolicyCombiningAlgMock algMock = (PolicyCombiningAlgMock) alg;
				if (algMock.decision == decision) {
					info.updateStatusCode(algMock.statusCode);
					if (algMock.missingAttr != null) {
						details.add(algMock.missingAttr);
					}
					break;
				}
			}
			break;
		case INDETERMINATE:
			for (PolicyCombiningAlgorithm alg : policyCombiningAlgs) {
				PolicyCombiningAlgMock algMock = (PolicyCombiningAlgMock) alg;
				if (algMock.decision == DecisionType.INDETERMINATE) {
					info.updateStatusCode(algMock.statusCode);
					if (algMock.missingAttr != null) {
						details.add(algMock.missingAttr);
					}
				}
			}
			break;
		case NOT_APPLICABLE:
			break;
		case PERMIT:
			break;
		}
		assertEquals(reqEvals.getStatusCode(), info.getStatusCode());
		assertEquals(reqEvals.getMissingAttributes().size(), details.size());
		assertTrue(reqEvals.getMissingAttributes().containsAll(details));
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
				DecisionType.PERMIT);
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


	private void addPolicySet(PolicySetType policySet,
			PolicyCombiningAlgorithm algorithm) {
		List<JAXBElement<?>> additionalInformation = policySet
				.getAdditionalInformation();
		PolicySetType newPolicySet = new PolicySetType();
		newPolicySet.setCombiningAlg(algorithm);
		TargetType tt = new TargetType();
		newPolicySet.setTarget(tt);
		additionalInformation.add(factory.createPolicySet(newPolicySet));
	}

	private PolicySetType initializePolicy(PolicyCombiningAlgorithm algorithm) {
		PolicySetType policySet = new PolicySetType();
		policySet.setCombiningAlg(algorithm);
		TargetType tt = new TargetType();
		policySet.setTarget(tt);
		return policySet;
	}

}
