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

package org.herasaf.xacml.core.policy.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.api.PIP;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.StatusCodeComparator;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.SubjectType;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.RFC822NameDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.herasaf.xacml.core.policy.ExpressionProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.impl.SubjectAttributeDesignatorType;
import org.herasaf.xacml.core.targetMatcher.impl.TargetMatcherImpl;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class tests the {@link SubjectAttributeDesignatorType}.
 * 
 * @author Florian Huonder
 */
public class TestSubjectAttributeDesignator {
	EvaluationContext evaluationContext;

	/**
	 * Initializes the {@link EvaluationContext} with an mock for the
	 * {@link PIP}.
	 */
	@BeforeTest
	public void init() {
		evaluationContext = new EvaluationContext(new TargetMatcherImpl(), new StatusCodeComparator(), null);
	}

	/**
	 * Initializes the {@link EvaluationContext} with an mock for the
	 * {@link PIP}.
	 */
	@DataProvider(name = "successfulSubjectAttrDesignator")
	public Object[][] successfulSubjectAttrDesignator() {
		return new Object[][] {
				new Object[] {
						initializeRequest(
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Fredi", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Hans", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Fritz", false)),
						initializeDesignator(null, "subject-role",
								new StringDataTypeAttribute(), null, false),
						initResult("Fredi", "Hans") },
				new Object[] {
						initializeRequest(
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Fredi", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Hans", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Fritz", false)),
						initializeDesignator(null, "subject-role",
								new StringDataTypeAttribute(), "hsr", false),
						initResult("Fredi", "Hans") },
				new Object[] {
						initializeRequest(
								initializeSubject(null, "subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Fredi", false),
								initializeSubject(null, "subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Hans", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Fritz", false)),
						initializeDesignator(null, "subject-role",
								new StringDataTypeAttribute(), "hsr", false),
						initResult("Fredi", "Hans") },
				new Object[] {
						initializeRequest(
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Fredi", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Hans", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Fritz", false)),
						initializeDesignator(
								"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject",
								"subject-role", new StringDataTypeAttribute(),
								"hsr", false), initResult("Fredi", "Hans") },
				new Object[] {
						initializeRequest(initializeSubject(null,
								"subject-role", new StringDataTypeAttribute(),
								"hsr", "Hans", false), initializeSubject(null,
								"subject-role",
								new RFC822NameDataTypeAttribute(), "hsr",
								"Fredi@gmx.ch", false), initializeSubject(null,
								"subject-role", new StringDataTypeAttribute(),
								"hsr", "Fritz", false)),
						initializeDesignator(
								"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject",
								"subject-role", new StringDataTypeAttribute(),
								"hsr", false), initResult("Hans", "Fritz") },
				new Object[] {
						initializeRequest(
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Fritz", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"subject-role",
										new RFC822NameDataTypeAttribute(),
										"hsr", "Hans@hotmail.com", false),
								initializeSubject(null, "subject-role",
										new StringDataTypeAttribute(), "zhw",
										"Fredi", false)),
						initializeDesignator(
								"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
								"subject-role",
								new RFC822NameDataTypeAttribute(), "hsr", true),
						initResult("Hans@hotmail.com") },
				new Object[] {
						initializeRequest(
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"role", new StringDataTypeAttribute(),
										"hsr", "Werner", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"subject-role",
										new RFC822NameDataTypeAttribute(),
										"zhw", "Werner@beinhart.de", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Fridolin", false)),
						initializeDesignator(
								"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
								"subject-role", new StringDataTypeAttribute(),
								"hsr", true), initResult("Fridolin") },

		};
	}

	/**
	 * Initializes the exception test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "subjectAttrDesignatorException")
	public Object[][] subjectAttrDesignatorException() {
		return new Object[][] {
				new Object[] {
						initializeRequest(
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Fredi", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Hans", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Fritz", false)),
						initializeDesignator(
								"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject",
								"subject-role",
								new RFC822NameDataTypeAttribute(), "hsr", true) },
				new Object[] {
						initializeRequest(initializeSubject(null,
								"subject-role", new StringDataTypeAttribute(),
								"hsr", "Hans", false), initializeSubject(null,
								"subject-role",
								new RFC822NameDataTypeAttribute(), "hsr",
								"Fredi@gmx.ch", false), initializeSubject(null,
								"subject-role", new StringDataTypeAttribute(),
								"hsr", "Fritz", false)),
						initializeDesignator(
								"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
								"subject-role", new StringDataTypeAttribute(),
								"hsr", true) },
				new Object[] {
						initializeRequest(
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"subject",
										new StringDataTypeAttribute(), "hsr",
										"Fritz", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"subject",
										new RFC822NameDataTypeAttribute(),
										"hsr", "Hans@hotmail.com", false),
								initializeSubject(null, "subject-role",
										new StringDataTypeAttribute(), "zhw",
										"Fredi", false)),
						initializeDesignator(
								"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
								"subject-role",
								new RFC822NameDataTypeAttribute(), "hsr", true) },
				new Object[] {
						initializeRequest(
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"role", new StringDataTypeAttribute(),
										"hsr", "Werner", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"subject-role",
										new RFC822NameDataTypeAttribute(),
										"zhw", "Werner@beinhart.de", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Fridolin", false)),
						initializeDesignator(
								"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
								"subject-role", new StringDataTypeAttribute(),
								"fhsg", true) },

		};
	}

	/**
	 * Test the successful cases.
	 * 
	 * @param req
	 *            The {@link EvaluationContext}.
	 * @param designator
	 *            The {@link SubjectAttributeDesignatorType} (is under test)
	 * @param result
	 *            The expected result.
	 * @throws Exception
	 *             In case an error occurs.
	 */
	@SuppressWarnings("unchecked")
	@Test(dataProvider = "successfulSubjectAttrDesignator")
	public void testHandle(RequestType req,
			SubjectAttributeDesignatorType designator, List<Object> result)
			throws Exception {

		List<Object> returnValue = (List<Object>) designator.handle(req,
				evaluationContext);
		assertEquals(returnValue.size(), result.size());
		for (Object obj : returnValue) {
			assertTrue(isContained(obj.toString(), result));
		}
	}

	/**
	 * Tests if all error-cases throw the proper exception. Expects a
	 * {@link MissingAttributeException}.
	 * 
	 * @param req
	 *            The {@link EvaluationContext}.
	 * @param designator
	 *            The {@link SubjectAttributeDesignatorType} (is under test)
	 * @throws Throwable
	 *             In case an unexpected error occurs.
	 */
	@Test(dataProvider = "subjectAttrDesignatorException", expectedExceptions = MissingAttributeException.class)
	public void testHandle(RequestType req,
			SubjectAttributeDesignatorType designator) throws Throwable {
		try {
			designator.handle(req, evaluationContext);
		} catch (ExpressionProcessingException e) {
			throw e.getCause();
		}
	}

	/**
	 * Tests if all error-cases throw the proper exception. Expects a
	 * {@link SyntaxException}.
	 * 
	 * @throws Throwable
	 *             In case an unexpected error occurs.
	 */
	@Test(enabled = true, expectedExceptions = SyntaxException.class)
	public void testHandleClassCastException() throws Throwable {
		RequestType req = initializeRequest(initializeSubjectWithIllegalType(
				"test", "resource-name", new StringDataTypeAttribute(), "hsr",
				1), initializeSubjectWithIllegalType("test", "subject-role",
				new StringDataTypeAttribute(), "hsr", 1),
				initializeSubjectWithIllegalType("test", "subject-role",
						new StringDataTypeAttribute(), "hsr", 1));
		SubjectAttributeDesignatorType designator = initializeDesignator(
				"test", "subject-role", new StringDataTypeAttribute(), null,
				false);
		designator.handle(req, evaluationContext);
	}

	/**
	 * Tests if all error-cases throw the proper exception. Expects a
	 * {@link ExpressionProcessingException}.
	 * 
	 * @throws Throwable
	 *             In case an unexpected error occurs.
	 */
	@Test(enabled = true, expectedExceptions = ExpressionProcessingException.class)
	public void testHandleExpressionProcessingException() throws Throwable {
		RequestType req = initializeRequest(initializeSubject("test",
				"subject-role", new StringDataTypeAttribute(), "hsr", "Fredi",
				true), initializeSubject("test", "subject-role",
				new StringDataTypeAttribute(), "hsr", "Fredi", false),
				initializeSubject("test", "subject-role",
						new StringDataTypeAttribute(), "hsr", "Fredi", false));
		SubjectAttributeDesignatorType designator = initializeDesignator(
				"test", "subject-role", new StringDataTypeAttribute(), null,
				false);
		designator.handle(req, evaluationContext);
	}

	/**
	 * Checks if a certain {@link String} is contained in a {@link List} of
	 * {@link Object}s.
	 * 
	 * @param elem
	 *            The {@link String} that is expected.
	 * @param list
	 *            The list the may contain the element.
	 * @return True if the element is contained in the {@link List}, false
	 *         otherwise.
	 */
	private boolean isContained(String elem, List<Object> list) {
		for (Object obj : list) {
			if (elem.equals(obj.toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Initializes the {@link SubjectAttributeDesignatorType} with ID, data
	 * type, issuer and must be present.
	 * 
	 * @param attrId
	 *            The attribute ID.
	 * @param dataType
	 *            The data type of the designator.
	 * @param issuer
	 *            The issuer of the designator.
	 * @param mustBePresent
	 *            True if mustbepresent is on.
	 * 
	 * @return The initialized {@link SubjectAttributeDesignatorType}.
	 */
	private SubjectAttributeDesignatorType initializeDesignator(String subjCat,
			String attrId, DataTypeAttribute<?> dataType, String issuer,
			Boolean mustBePresent) {
		SubjectAttributeDesignatorType designator = new SubjectAttributeDesignatorType();
		designator.setAttributeId(attrId);
		designator.setDataType(dataType);
		designator.setIssuer(issuer);
		designator.setMustBePresent(mustBePresent);
		designator.setSubjectCategory(subjCat);

		return designator;
	}

	/**
	 * Initializes the {@link SubjectType}.
	 * 
	 * @param attrId
	 *            The Attribute ID of the of the attribute contained in the
	 *            {@link SubjectType}.
	 * @param dataType
	 *            The datatype of the attribute.
	 * @param issuer
	 *            The issuer of the attribute.
	 * @param value
	 *            The value of the attribute
	 * @param multiContent
	 *            True if the attribute contains multi content.
	 * @return The created {@link SubjectType}.
	 */
	private SubjectType initializeSubject(String subjCat, String attrId,
			DataTypeAttribute<?> dataType, String issuer, String value,
			boolean multiContent) {

		SubjectType sub = new SubjectType();
		sub.setSubjectCategory(subjCat);

		AttributeType attr = new AttributeType();
		attr.setAttributeId(attrId);
		attr.setDataType(dataType);
		attr.setIssuer(issuer);

		AttributeValueType attrVal = new AttributeValueType();
		attrVal.getContent().add(value);
		if (multiContent) {
			attrVal.getContent().add(value);
		}

		attr.getAttributeValues().add(attrVal);

		sub.getAttributes().add(attr);

		return sub;
	}

	/**
	 * Creates an subject with an illegal type.
	 * 
	 * @param attrId
	 *            The Attribute ID of the of the attribute contained in the
	 *            {@link SubjectType}.
	 * @param dataType
	 *            The datatype of the attribute.
	 * @param issuer
	 *            The issuer of the attribute.
	 * @param value
	 *            The value of the attribute
	 * @return The created {@link SubjectType}.
	 */
	private SubjectType initializeSubjectWithIllegalType(String subjCat,
			String attrId, DataTypeAttribute<?> dataType, String issuer,
			Integer value) {

		SubjectType sub = new SubjectType();
		sub.setSubjectCategory(subjCat);

		AttributeType attr = new AttributeType();
		attr.setAttributeId(attrId);
		attr.setDataType(dataType);
		attr.setIssuer(issuer);

		AttributeValueType attrVal = new AttributeValueType();
		attrVal.getContent().add(value);

		attr.getAttributeValues().add(attrVal);

		sub.getAttributes().add(attr);

		return sub;
	}

	/**
	 * Initializes the request with the given {@link SubjectType}.
	 * 
	 * @param s1
	 *            The first subject type to place into the {@link RequestType}.
	 * @param s2
	 *            The second subject type to place into the {@link RequestType}.
	 * @param s3
	 *            The third subject type to place into the {@link RequestType}.
	 * @return The initialized {@link RequestType}.
	 */
	private RequestType initializeRequest(SubjectType s1, SubjectType s2,
			SubjectType s3) {
		RequestType req = new RequestType();
		req.getSubjects().add(s1);
		req.getSubjects().add(s2);
		req.getSubjects().add(s3);
		return req;
	}

	/**
	 * Initializes the expected results.
	 * 
	 * @param args
	 *            The resultes.
	 * @return The {@link List} containing the results.
	 */
	private List<String> initResult(String... args) {
		List<String> returnValues = new ArrayList<String>();
		for (String str : args) {
			returnValues.add(str);
		}
		return returnValues;
	}
}
