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

package org.herasaf.xacml.core.targetMatcher.impl.test;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.api.PIP;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.StatusCodeComparator;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.herasaf.xacml.core.function.FunctionProcessingException;
import org.herasaf.xacml.core.policy.ExpressionProcessingException;
import org.herasaf.xacml.core.policy.impl.ActionMatchType;
import org.herasaf.xacml.core.policy.impl.ActionType;
import org.herasaf.xacml.core.policy.impl.ActionsType;
import org.herasaf.xacml.core.policy.impl.AttributeValueType;
import org.herasaf.xacml.core.policy.impl.EnvironmentMatchType;
import org.herasaf.xacml.core.policy.impl.EnvironmentType;
import org.herasaf.xacml.core.policy.impl.EnvironmentsType;
import org.herasaf.xacml.core.policy.impl.ResourceMatchType;
import org.herasaf.xacml.core.policy.impl.ResourceType;
import org.herasaf.xacml.core.policy.impl.ResourcesType;
import org.herasaf.xacml.core.policy.impl.SubjectMatchType;
import org.herasaf.xacml.core.policy.impl.SubjectType;
import org.herasaf.xacml.core.policy.impl.SubjectsType;
import org.herasaf.xacml.core.policy.impl.TargetType;
import org.herasaf.xacml.core.targetMatcher.TargetMatcher;
import org.herasaf.xacml.core.targetMatcher.TargetMatchingResult;
import org.herasaf.xacml.core.targetMatcher.impl.TargetMatcherImpl;
import org.herasaf.xacml.core.targetMatcher.impl.test.mock.ActionAttributeDesignatorMock;
import org.herasaf.xacml.core.targetMatcher.impl.test.mock.EnvironmentAttributeDesignatorMock;
import org.herasaf.xacml.core.targetMatcher.impl.test.mock.FunctionMock;
import org.herasaf.xacml.core.targetMatcher.impl.test.mock.ResourceAttributeDesignatorMock;
import org.herasaf.xacml.core.targetMatcher.impl.test.mock.SubjectAttributeDesignatorMock;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the {@link TargetMatcher} behaves properly.
 * 
 * @author Florian Huonder
 */
public class TestTargetMatcher {
	EvaluationContext evaluationContext;

	/**
	 * Initializes the {@link EvaluationContext} containing an mock for the
	 * {@link PIP}.
	 */
	@BeforeTest
	public void init() {
		evaluationContext = new EvaluationContext(new TargetMatcherImpl(), new StatusCodeComparator(), null);
	}

	/**
	 * Creates various test cases.
	 * 
	 * @return The created test cases.
	 */
	@DataProvider(name = "positiveData")
	public Object[][] createPositiveData() {
		// 1 = length of the array says how many subjects the Target has
		// 2 = The number of subjectMatches that match an attribute Value in the
		// designator
		// 3 = The number of subjectMatches the do not match an attribute Value
		// in the designator
		// 4 = The number of matching attribute values
		// 5 = The number of not matching attribute values
		// 6 = If true than first subject - match values are MATCH, NOMATCH
		// otherwise (in the designator)
		// 96 = Causes that an FunctionProcessingException is thrown, if true
		// 97 = Causes that an ExpressionProcessingException is thrown, if true
		// 98 = Causes that an SyntaxException is thrown, if true
		// 99 = Expected result of the match

		return new Object[][] {
				// 1 2 3 4 5 6 96 97 98 99
				new Object[] { new int[][] { { 1, 0 } }, 0, 0, true, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 0 } }, 1, 0, true, false,
						false, false, TargetMatchingResult.MATCH },
				new Object[] { new int[][] { { 1, 0 } }, 0, 1, true, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 0 } }, 1, 1, true, false,
						false, false, TargetMatchingResult.MATCH },
				new Object[] { new int[][] { { 1, 0 } }, 1, 5, true, false,
						false, false, TargetMatchingResult.MATCH },
				new Object[] { new int[][] { { 1, 0 } }, 0, 5, true, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 0 } }, 5, 0, true, false,
						false, false, TargetMatchingResult.MATCH },
				new Object[] { new int[][] { { 0, 1 } }, 1, 0, true, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 0, 1 } }, 0, 1, true, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 0, 1 } }, 1, 1, true, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 0, 1 } }, 1, 5, true, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 0, 1 } }, 0, 5, true, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 0, 1 } }, 5, 0, true, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 } }, 1, 0, true, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 } }, 0, 1, true, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 } }, 1, 1, true, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 } }, 1, 5, true, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 } }, 0, 5, true, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 } }, 5, 0, true, false,
						false, false, TargetMatchingResult.NO_MATCH },
				// 1 2 3 4 5 6 96 97 98 99
				new Object[] { new int[][] { { 1, 0 } }, 1, 0, false, false,
						false, false, TargetMatchingResult.MATCH },
				new Object[] { new int[][] { { 1, 0 } }, 0, 1, false, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 0 } }, 1, 1, false, false,
						false, false, TargetMatchingResult.MATCH },
				new Object[] { new int[][] { { 1, 0 } }, 1, 5, false, false,
						false, false, TargetMatchingResult.MATCH },
				new Object[] { new int[][] { { 1, 0 } }, 0, 5, false, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 0 } }, 5, 0, false, false,
						false, false, TargetMatchingResult.MATCH },
				new Object[] { new int[][] { { 0, 1 } }, 1, 0, false, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 0, 1 } }, 0, 1, false, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 0, 1 } }, 1, 1, false, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 0, 1 } }, 1, 5, false, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 0, 1 } }, 0, 5, false, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 0, 1 } }, 5, 0, false, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 } }, 1, 0, false, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 } }, 0, 1, false, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 } }, 1, 1, false, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 } }, 1, 5, false, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 } }, 0, 5, false, false,
						false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 } }, 5, 0, false, false,
						false, false, TargetMatchingResult.NO_MATCH },
				// 1 2 3 4 5 6 7 8 9 10
				new Object[] { new int[][] { { 1, 1 }, { 1, 0 }, { 0, 1 } }, 1,
						0, true, false, false, false, TargetMatchingResult.MATCH },
				new Object[] { new int[][] { { 1, 1 }, { 1, 0 }, { 0, 1 } }, 0,
						1, true, false, false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 }, { 1, 0 }, { 0, 1 } }, 1,
						1, true, false, false, false, TargetMatchingResult.MATCH },
				new Object[] { new int[][] { { 1, 1 }, { 1, 0 }, { 0, 1 } }, 1,
						5, true, false, false, false, TargetMatchingResult.MATCH },
				new Object[] { new int[][] { { 1, 1 }, { 1, 0 }, { 0, 1 } }, 0,
						5, true, false, false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 }, { 1, 0 }, { 0, 1 } }, 5,
						0, true, false, false, false, TargetMatchingResult.MATCH },
				// 1 2 3 4 5 6 7 8 9 10
				new Object[] { new int[][] { { 1, 1 }, { 1, 0 }, { 0, 1 } }, 1,
						0, false, false, false, false, TargetMatchingResult.MATCH },
				new Object[] { new int[][] { { 1, 1 }, { 1, 0 }, { 0, 1 } }, 0,
						1, false, false, false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 }, { 1, 0 }, { 0, 1 } }, 1,
						1, false, false, false, false, TargetMatchingResult.MATCH },
				new Object[] { new int[][] { { 1, 1 }, { 1, 0 }, { 0, 1 } }, 1,
						5, false, false, false, false, TargetMatchingResult.MATCH },
				new Object[] { new int[][] { { 1, 1 }, { 1, 0 }, { 0, 1 } }, 0,
						5, false, false, false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { new int[][] { { 1, 1 }, { 1, 0 }, { 0, 1 } }, 5,
						0, false, false, false, false, TargetMatchingResult.MATCH }, };
	}

	/**
	 * Creates various test cases.
	 * 
	 * @return The created test cases.
	 */
	@DataProvider(name = "overAllData")
	public Object[][] createOverAllData() {
		return new Object[][] {
				// 1 = should subjects match
				// 2 = should resources match
				// 3 = should actions match
				// 4 = should environments match
				// 5 = expected result

				// 1 2 3 4 5
				new Object[] { false, false, false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { true, false, false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { false, true, false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { false, false, true, false, TargetMatchingResult.NO_MATCH },
				new Object[] { false, false, false, true, TargetMatchingResult.NO_MATCH },
				new Object[] { true, true, false, false, TargetMatchingResult.NO_MATCH },
				new Object[] { false, false, true, true, TargetMatchingResult.NO_MATCH },
				new Object[] { false, true, false, true, TargetMatchingResult.NO_MATCH },
				new Object[] { true, false, true, false, TargetMatchingResult.NO_MATCH },
				new Object[] { true, true, true, true, TargetMatchingResult.MATCH }, };
	}

	/**
	 * Tests the target matcher in various combinations.
	 * 
	 * @param subjects
	 *            The number of subjects that match.
	 * @param matchingAttributeValues
	 *            The number of {@link AttributeValueType}s that match.
	 * @param nonMatchingAttributeValues
	 *            The number of the {@link AttributeValueType}s that do not
	 *            match.
	 * @param firstIsMatch
	 *            Tells if the first matches.
	 * @param funcProcessException
	 *            True if a {@link FunctionProcessingException} should occur,
	 *            false othwerwise.
	 * @param expressProcessException
	 *            True if a {@link ExpressionProcessingException} should occur,
	 *            false otherwise.
	 * @param syntaxException
	 *            True if a {@link SyntaxException} should occur, flase
	 *            otherwise.
	 * @param result
	 *            The expected result.
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test(enabled = true, dataProvider = "positiveData", description = "The test only tests if the subject-match does work because resource,"
			+ "action and environment match are absolutely the same. Therefor they are"
			+ "omitted and this results in that the elements alway match, what means"
			+ "true for these elements.")
	public void testMatch(int[][] subjects, int matchingAttributeValues,
			int nonMatchingAttributeValues, boolean firstIsMatch,
			boolean funcProcessException, boolean expressProcessException,
			boolean syntaxException, TargetMatchingResult result) throws Exception {
		final String MATCH = "Match";
		final String NOMATCH = "NoMatch";

		TargetType target = new TargetType();
		SubjectsType subjectsType = new SubjectsType();
		target.setSubjects(subjectsType);

		SubjectAttributeDesignatorMock designatorMock = new SubjectAttributeDesignatorMock(
				expressProcessException);

		if (firstIsMatch) {
			designatorMock.extendValues(getMatchValues(MATCH,
					matchingAttributeValues));
			designatorMock.extendValues(getMatchValues(NOMATCH,
					nonMatchingAttributeValues));
		} else {
			designatorMock.extendValues(getMatchValues(NOMATCH,
					nonMatchingAttributeValues));
			designatorMock.extendValues(getMatchValues(MATCH,
					matchingAttributeValues));
		}

		for (int i = 0; i < subjects.length; i++) { // Number of subjects
			SubjectType subject = new SubjectType();
			for (int j = 0; j < subjects[i][0]; j++) { // Number of
				// subject-matches that
				// match
				SubjectMatchType match = createSubjectMatchType(
						funcProcessException, MATCH);
				match.setSubjectAttributeDesignator(designatorMock);
				subject.getSubjectMatches().add(match);
			}
			for (int j = 0; j < subjects[i][1]; j++) { // Number of
				// subject-matches that
				// do not match
				SubjectMatchType match = createSubjectMatchType(
						funcProcessException, "Something that does not match");
				match.setSubjectAttributeDesignator(designatorMock);
				subject.getSubjectMatches().add(match);
			}
			target.getSubjects().getSubjects().add(subject);
		}

		TargetMatcher matcher = new TargetMatcherImpl();
		assertEquals(matcher.match(null, target, evaluationContext), result); // Request
		// can be
		// null
		// because
		// the data
		// is
		// provided
		// through
		// the
		// Designator
		// above.
	}

	/**
	 * Returns the an {@link String} array containing a a value n time (n =
	 * size).
	 * 
	 * @param value
	 *            The value to add to the array
	 * @param size
	 *            The number of times the value shall be in the array.
	 * @return The array containing the value size-time.
	 */
	private String[] getMatchValues(String value, int size) {
		String[] values = new String[size];
		Arrays.fill(values, value);
		return values;
	}

	/**
	 * Creates a {@link SubjectMatchType}.
	 * 
	 * @param funcProcessException
	 *            True if the {@link SubjectMatchType} should throw a
	 *            {@link FunctionProcessingException}, false otherwise.
	 * @param content
	 *            the content of the {@link AttributeValueType}.
	 * @return The created {@link SubjectMatchType}.
	 */
	private SubjectMatchType createSubjectMatchType(
			boolean funcProcessException, String content) {
		SubjectMatchType match = new SubjectMatchType();

		AttributeValueType attrValue1 = new AttributeValueType();
		attrValue1.getContent().add(content);
		attrValue1.setDataType(new StringDataTypeAttribute());

		match.setAttributeValue(attrValue1);
		match.setMatchFunction(new FunctionMock(funcProcessException)); // This
		// function
		// matches
		return match;
	}

	/**
	 * Tests if the the match throws a {@link FunctionProcessingException} when
	 * expected.
	 * 
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test(expectedExceptions = { FunctionProcessingException.class })
	public void testFunctionProcessingException() throws Exception {
		testMatch(new int[][] { { 1, 1 }, { 1, 0 }, { 0, 1 } }, 5, 0, true,
				true, false, false, TargetMatchingResult.MATCH); // All parameters except of the 5th
		// have no impact
	}

	/**
	 * Tests if the the match throws a {@link ExpressionProcessingException}
	 * when expected.
	 * 
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test(expectedExceptions = { ExpressionProcessingException.class })
	public void testExceptionProcessingException() throws Exception {
		testMatch(new int[][] { { 1, 1 }, { 1, 0 }, { 0, 1 } }, 5, 0, true,
				false, true, false, TargetMatchingResult.MATCH); // All parameters except of the 6th
		// have no impact
	}

	/**
	 * Tests if all various combinations of the four elements (subject-,
	 * resource-, action- and environment-match) works as expected.
	 * 
	 * @param subjectMatches
	 *            True if the {@link SubjectMatchType} shall match, false
	 *            otherwise.
	 * @param resourceMatches
	 *            True if the {@link ResourceMatchType} shall match, false
	 *            otherwise.
	 * @param actionMatches
	 *            True if the {@link ActionMatchType} shall match, false
	 *            otherwise.
	 * @param environmentMatches
	 *            True if the {@link EnvironmentMatchType} shall match, false
	 *            otherwise.
	 * @param result
	 *            The expected result.
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test(dataProvider = "overAllData", description = "Tests the combinations of all four elements")
	public void testOverAll(boolean subjectMatches, boolean resourceMatches,
			boolean actionMatches, boolean environmentMatches, TargetMatchingResult result)
			throws Exception {
		final String MATCH = "Match";
		final String NOMATCH = "NoMatch";
		TargetType target = new TargetType();
		SubjectsType subjects = new SubjectsType();
		target.setSubjects(subjects);
		ResourcesType resources = new ResourcesType();
		target.setResources(resources);
		ActionsType actions = new ActionsType();
		target.setActions(actions);
		EnvironmentsType environments = new EnvironmentsType();
		target.setEnvironments(environments);

		SubjectType subject = new SubjectType();
		ResourceType resource = new ResourceType();
		ActionType action = new ActionType();
		EnvironmentType environment = new EnvironmentType();

		SubjectAttributeDesignatorMock subjectDesignatorMock = new SubjectAttributeDesignatorMock(
				false);
		ResourceAttributeDesignatorMock resourceDesignatorMock = new ResourceAttributeDesignatorMock(
				false);
		ActionAttributeDesignatorMock actionDesignatorMock = new ActionAttributeDesignatorMock(
				false);
		EnvironmentAttributeDesignatorMock environmentDesignatorMock = new EnvironmentAttributeDesignatorMock(
				false);

		String value = null;
		if (subjectMatches) {
			value = MATCH;
		} else {
			value = NOMATCH;
		}
		subjectDesignatorMock.extendValues(new String[] { value });

		if (resourceMatches) {
			value = MATCH;
		} else {
			value = NOMATCH;
		}
		resourceDesignatorMock.extendValues(new String[] { value });

		if (actionMatches) {
			value = MATCH;
		} else {
			value = NOMATCH;
		}
		actionDesignatorMock.extendValues(new String[] { value });

		if (environmentMatches) {
			value = MATCH;
		} else {
			value = NOMATCH;
		}
		environmentDesignatorMock.extendValues(new String[] { value });

		SubjectMatchType smt = new SubjectMatchType();
		subject.getSubjectMatches().add(smt);
		ResourceMatchType rmt = new ResourceMatchType();
		resource.getResourceMatches().add(rmt);
		ActionMatchType amt = new ActionMatchType();
		action.getActionMatches().add(amt);
		EnvironmentMatchType emt = new EnvironmentMatchType();
		environment.getEnvironmentMatches().add(emt);

		smt.setSubjectAttributeDesignator(subjectDesignatorMock);
		rmt.setResourceAttributeDesignator(resourceDesignatorMock);
		amt.setActionAttributeDesignator(actionDesignatorMock);
		emt.setEnvironmentAttributeDesignator(environmentDesignatorMock);

		subjects.getSubjects().add(subject);
		resources.getResources().add(resource);
		actions.getActions().add(action);
		environments.getEnvironments().add(environment);

		FunctionMock function = new FunctionMock(false);

		smt.setMatchFunction(function);
		rmt.setMatchFunction(function);
		amt.setMatchFunction(function);
		emt.setMatchFunction(function);

		AttributeValueType attrValue = new AttributeValueType();
		attrValue.getContent().add(MATCH);
		attrValue.setDataType(new StringDataTypeAttribute()); // String is fix
		// because all
		// tests need
		// string as
		// data type
		smt.setAttributeValue(attrValue);
		rmt.setAttributeValue(attrValue);
		amt.setAttributeValue(attrValue);
		emt.setAttributeValue(attrValue);

		TargetMatcher matcher = new TargetMatcherImpl();
		assertEquals(matcher.match(null, target, evaluationContext), result); // Request
		// can be
		// null
		// because
		// the data
		// is
		// provided
		// through
		// the
		// Designator
		// above.
	}

	/**
	 * Tests if the match works when the {@link RequestType} is null.
	 * 
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test
	public void testNullElements() throws Exception {
		TargetType target = new TargetType();

		TargetMatcher matcher = new TargetMatcherImpl();
		assertEquals(matcher.match(null, target, evaluationContext), TargetMatchingResult.MATCH); // Request
																			// can
		// be null
		// because
		// the data
		// is
		// provided
		// through
		// the
		// Designator
		// above.
	}
}