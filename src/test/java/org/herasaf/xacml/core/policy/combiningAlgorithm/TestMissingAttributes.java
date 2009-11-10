package org.herasaf.xacml.core.policy.combiningAlgorithm;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.herasaf.xacml.core.combiningAlgorithm.AbstractCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.CombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyDenyOverridesAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyFirstApplicableAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyOnlyOneApplicableAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyOrderedDenyOverridesAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyOrderedPermitOverridesAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyPermitOverridesAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RuleDenyOverridesAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RuleFirstApplicableAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RuleOrderedDenyOverridesAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RuleOrderedPermitOverridesAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RulePermitOverridesAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.herasaf.xacml.core.function.impl.equalityPredicates.StringEqualFunction;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.PolicySetType;
import org.herasaf.xacml.core.policy.impl.RuleType;
import org.herasaf.xacml.core.policy.impl.SubjectAttributeDesignatorType;
import org.herasaf.xacml.core.policy.impl.SubjectMatchType;
import org.herasaf.xacml.core.policy.impl.SubjectType;
import org.herasaf.xacml.core.policy.impl.SubjectsType;
import org.herasaf.xacml.core.policy.impl.TargetType;
import org.herasaf.xacml.core.targetMatcher.impl.TargetMatcherImpl;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestMissingAttributes {

	/**
	 * Tests if the missing attributes are correctly returned in case only one
	 * combining algorithm is given that has a missing attribtue in its target.
	 * 
	 * @param combiningAlgorithm
	 *            The {@link CombiningAlgorithm} under test.
	 * @param eval
	 *            the evaluatable that has an attribute that is not present in
	 *            the request.
	 * @param requestInfo
	 *            the {@link RequestInformation} needed for evaluation.
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test(dataProvider = "testDataForTargetMatch")
	public void testMissingAttributesInTargetMatch(
			CombiningAlgorithm combiningAlgorithm, Evaluatable eval,
			RequestInformation requestInfo) throws Exception {
		DecisionType decision = combiningAlgorithm.evaluate(new RequestType(),
				eval, requestInfo);

		assertEquals(decision, DecisionType.INDETERMINATE);
		assertEquals(requestInfo.getStatusCode(), StatusCode.MISSING_ATTRIBUTE);
		assertEquals(requestInfo.getMissingAttributes().size(), 1); // A missing
		// attribute
		// is
		// expected
		assertEquals(
				requestInfo.getMissingAttributes().get(0).getAttributeId(),
				"urn:org:herasaf:xacml:test:my-subject-id");
	}

	/**
	 * This test does not the the deny-override and ordered-deny-overrides
	 * algorithms because they cannot return an indeterminate because this is
	 * always turned into a deny.
	 * 
	 * @param rootCombiningAlgorithm
	 * @param evals
	 * @param requestInfo
	 * @throws Exception
	 */
	@Test(dataProvider = "testDataForHierarchyForPolicies")
	public void testMissingAttributesInHierarchyForPolicies(
			RuleCombiningAlgorithm rootCombiningAlgorithm,
			List<RuleType> rules, RequestInformation requestInfo)
			throws Exception {

		DecisionType decision = rootCombiningAlgorithm.evaluateRuleList(
				new RequestType(), rules, requestInfo);

		assertEquals(decision, DecisionType.INDETERMINATE);
		assertEquals(requestInfo.getStatusCode(), StatusCode.MISSING_ATTRIBUTE);
		assertEquals(requestInfo.getMissingAttributes().size(), 1); // A missing
		// attribute
		// is
		// expected
		assertEquals(
				requestInfo.getMissingAttributes().get(0).getAttributeId(),
				"urn:org:herasaf:xacml:test:my-subject-id");
	}

	/**
	 * This test does not the the deny-override and ordered-deny-overrides
	 * algorithms because they cannot return an indeterminate because this is
	 * always turned into a deny.
	 * 
	 * @param rootCombiningAlgorithm
	 * @param evals
	 * @param requestInfo
	 * @throws Exception
	 */
	@Test(dataProvider = "testDataForHierarchyForPolicySets")
	public void testMissingAttributesInHierarchyForPolicySets(
			PolicyCombiningAlgorithm rootCombiningAlgorithm,
			List<Evaluatable> evals, RequestInformation requestInfo)
			throws Exception {

		DecisionType decision = rootCombiningAlgorithm.evaluateEvaluatableList(
				new RequestType(), evals, requestInfo);

		assertEquals(decision, DecisionType.INDETERMINATE);
		assertEquals(requestInfo.getStatusCode(), StatusCode.MISSING_ATTRIBUTE);
		assertEquals(requestInfo.getMissingAttributes().size(), 1); // A missing
		// attribute
		// is
		// expected
		assertEquals(
				requestInfo.getMissingAttributes().get(0).getAttributeId(),
				"urn:org:herasaf:xacml:test:my-subject-id");
	}

	@DataProvider(name = "testDataForHierarchyForPolicies")
	public Object[][] createdTestDataForHierarchyForPolicies() throws Exception {
		return new Object[][] {
				{
						createCombiningAlgorihtm(RuleFirstApplicableAlgorithm.class),
						createRuleList(),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(RulePermitOverridesAlgorithm.class),
						createRuleList(),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(RuleOrderedPermitOverridesAlgorithm.class),
						createRuleList(),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(RuleOrderedDenyOverridesAlgorithm.class),
						createRuleList(),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(RuleDenyOverridesAlgorithm.class),
						createRuleList(),
						new RequestInformation(null) }, };
	}

	@DataProvider(name = "testDataForHierarchyForPolicySets")
	public Object[][] createdTestDataForHierarchyForPolicySets()
			throws Exception {
		return new Object[][] {
				{
						createCombiningAlgorihtm(PolicyOnlyOneApplicableAlgorithm.class),
						createEvaluatableList(PolicySetType.class,
								PolicyOnlyOneApplicableAlgorithm.class),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(PolicyFirstApplicableAlgorithm.class),
						createEvaluatableList(PolicySetType.class,
								PolicyOnlyOneApplicableAlgorithm.class),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(PolicyPermitOverridesAlgorithm.class),
						createEvaluatableList(PolicySetType.class,
								PolicyOnlyOneApplicableAlgorithm.class),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(PolicyOrderedPermitOverridesAlgorithm.class),
						createEvaluatableList(PolicySetType.class,
								PolicyOnlyOneApplicableAlgorithm.class),
						new RequestInformation(null) }, };
	}

	private List<Evaluatable> createEvaluatableList(
			Class<? extends Evaluatable> evaluatableType,
			Class<? extends PolicyCombiningAlgorithm> combiningAlgorithmType)
			throws Exception {
		List<Evaluatable> evals = new ArrayList<Evaluatable>();
		evals.add(createEvaluatable(evaluatableType, combiningAlgorithmType));
		return evals;
	}

	private List<RuleType> createRuleList() throws Exception {
		List<RuleType> rules = new ArrayList<RuleType>();

		RuleType rule = new RuleType();
		TargetType target = new TargetType();
		SubjectsType subjects = new SubjectsType();
		SubjectType subject = new SubjectType();
		SubjectMatchType subjectMatch = new SubjectMatchType();
		SubjectAttributeDesignatorType subjectAttributeDesignator = new SubjectAttributeDesignatorType();
		subjectAttributeDesignator
				.setAttributeId("urn:org:herasaf:xacml:test:my-subject-id");
		subjectAttributeDesignator.setDataType(new StringDataTypeAttribute());
		subjectAttributeDesignator.setMustBePresent(true);
		subjectMatch.setSubjectAttributeDesignator(subjectAttributeDesignator);
		subjectMatch.setMatchFunction(new StringEqualFunction());
		subject.getSubjectMatches().add(subjectMatch);
		subjects.getSubjects().add(subject);
		target.setSubjects(subjects);
		rule.setTarget(target);
		rule.setRuleId("TestEvaluatable");
		
		rules.add(rule);

		return rules;
	}

	/**
	 * Here it does not matter of what type the {@link Evaluatable} is because
	 * the evaluation is anyway skipped after the target match.
	 * 
	 * @return
	 * @throws Exception
	 */
	@DataProvider(name = "testDataForTargetMatch")
	public Object[][] createdTestDataForTargetMatch() throws Exception {
		return new Object[][] {
				{
						createCombiningAlgorihtm(PolicyPermitOverridesAlgorithm.class),
						createEvaluatable(PolicySetType.class,
								PolicyPermitOverridesAlgorithm.class),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(PolicyOrderedPermitOverridesAlgorithm.class),
						createEvaluatable(PolicySetType.class,
								PolicyPermitOverridesAlgorithm.class),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(PolicyDenyOverridesAlgorithm.class),
						createEvaluatable(PolicySetType.class,
								PolicyPermitOverridesAlgorithm.class),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(PolicyOrderedDenyOverridesAlgorithm.class),
						createEvaluatable(PolicySetType.class,
								PolicyPermitOverridesAlgorithm.class),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(PolicyFirstApplicableAlgorithm.class),
						createEvaluatable(PolicySetType.class,
								PolicyPermitOverridesAlgorithm.class),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(PolicyOnlyOneApplicableAlgorithm.class),
						createEvaluatable(PolicySetType.class,
								PolicyPermitOverridesAlgorithm.class),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(RulePermitOverridesAlgorithm.class),
						createEvaluatable(PolicySetType.class,
								PolicyPermitOverridesAlgorithm.class),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(RuleOrderedPermitOverridesAlgorithm.class),
						createEvaluatable(PolicySetType.class,
								PolicyPermitOverridesAlgorithm.class),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(RuleDenyOverridesAlgorithm.class),
						createEvaluatable(PolicySetType.class,
								PolicyPermitOverridesAlgorithm.class),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(RuleOrderedDenyOverridesAlgorithm.class),
						createEvaluatable(PolicySetType.class,
								PolicyPermitOverridesAlgorithm.class),
						new RequestInformation(null) },
				{
						createCombiningAlgorihtm(RuleFirstApplicableAlgorithm.class),
						createEvaluatable(PolicySetType.class,
								PolicyPermitOverridesAlgorithm.class),
						new RequestInformation(null) } };
	}

	private AbstractCombiningAlgorithm createCombiningAlgorihtm(
			Class<? extends AbstractCombiningAlgorithm> combiningAlgorithmClass)
			throws Exception {

		AbstractCombiningAlgorithm combiningAlgorithm = combiningAlgorithmClass
				.newInstance();
		combiningAlgorithm.setTargetMatcher(new TargetMatcherImpl());

		return combiningAlgorithm;
	}

	private Evaluatable createEvaluatable(
			Class<? extends Evaluatable> evaluatableType,
			Class<? extends PolicyCombiningAlgorithm> combiningAlgorithmType)
			throws Exception {
		PolicySetType policy = new PolicySetType();
		TargetType target = new TargetType();
		SubjectsType subjects = new SubjectsType();
		SubjectType subject = new SubjectType();
		SubjectMatchType subjectMatch = new SubjectMatchType();
		SubjectAttributeDesignatorType subjectAttributeDesignator = new SubjectAttributeDesignatorType();
		subjectAttributeDesignator
				.setAttributeId("urn:org:herasaf:xacml:test:my-subject-id");
		subjectAttributeDesignator.setDataType(new StringDataTypeAttribute());
		subjectAttributeDesignator.setMustBePresent(true);
		subjectMatch.setSubjectAttributeDesignator(subjectAttributeDesignator);
		subjectMatch.setMatchFunction(new StringEqualFunction());
		subject.getSubjectMatches().add(subjectMatch);
		subjects.getSubjects().add(subject);
		target.setSubjects(subjects);
		policy.setTarget(target);
		policy.setPolicySetId("TestEvaluatable");
		AbstractCombiningAlgorithm combAlg = (AbstractCombiningAlgorithm) combiningAlgorithmType
				.newInstance();
		combAlg.setTargetMatcher(new TargetMatcherImpl());
		policy.setCombiningAlg((PolicyCombiningAlgorithm) combAlg);

		return policy;
	}
}