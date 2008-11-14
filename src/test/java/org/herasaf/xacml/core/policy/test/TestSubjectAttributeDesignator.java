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

package org.herasaf.xacml.core.policy.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.SyntaxException;
import org.herasaf.xacml.core.attributeFinder.impl.AttributeFinderMock;
import org.herasaf.xacml.core.context.RequestInformation;
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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestSubjectAttributeDesignator {
	RequestInformation reqInfo = new RequestInformation(null, new AttributeFinderMock());

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
						initializeDesignator(
								null,
								"subject-role", new StringDataTypeAttribute(),
								null, false), initResult("Fredi", "Hans") },
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
								null,
								"subject-role", new StringDataTypeAttribute(),
								"hsr", false), initResult("Fredi", "Hans") },
				new Object[] {
						initializeRequest(
								initializeSubject(
										null,
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Fredi", false),
								initializeSubject(
										null,
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Hans", false),
								initializeSubject(
										"urn:oasis:names:tc:xacml:1.0:subject-category:requesting-machine",
										"subject-role",
										new StringDataTypeAttribute(), "hsr",
										"Fritz", false)),
						initializeDesignator(
								null,
								"subject-role", new StringDataTypeAttribute(),
								"hsr", false), initResult("Fredi", "Hans") },
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

	@SuppressWarnings("unchecked")
	@Test(dataProvider = "successfulSubjectAttrDesignator")
	public void testHandle(RequestType req,
			SubjectAttributeDesignatorType designator, List<Object> result)
			throws Exception {

		List<Object> returnValue = (List<Object>) designator.handle(req, reqInfo);
		assertEquals(returnValue.size(), result.size());
		for (Object obj : returnValue) {
			assertTrue(isContained(obj.toString(), result));
		}
	}

	@Test(dataProvider = "subjectAttrDesignatorException", expectedExceptions = MissingAttributeException.class)
	public void testHandle(RequestType req,
			SubjectAttributeDesignatorType designator) throws Throwable {
		try {
			designator.handle(req, reqInfo);
		} catch (ExpressionProcessingException e) {
			throw e.getCause();
		}
	}

	@Test(enabled = true, expectedExceptions = SyntaxException.class)
	public void testHandleClassCastException() throws Throwable {
		RequestType req = initializeRequest(initializeSubjectWithIllegalType("test",
				"resource-name", new StringDataTypeAttribute(), "hsr", 1), initializeSubjectWithIllegalType(
						"test","subject-role", new StringDataTypeAttribute(), "hsr", 1),initializeSubjectWithIllegalType(
								"test","subject-role", new StringDataTypeAttribute(), "hsr", 1));
		SubjectAttributeDesignatorType designator = initializeDesignator(
				"test","subject-role", new StringDataTypeAttribute(), null, false);
		designator.handle(req, reqInfo);
	}

	@Test(enabled = true, expectedExceptions = ExpressionProcessingException.class)
	public void testHandleExpressionProcessingException() throws Throwable {
		RequestType req = initializeRequest(initializeSubject(
				"test","subject-role", new StringDataTypeAttribute(), "hsr", "Fredi", true),initializeSubject(
						"test","subject-role", new StringDataTypeAttribute(), "hsr", "Fredi", false),initializeSubject(
								"test","subject-role", new StringDataTypeAttribute(), "hsr", "Fredi", false));
		SubjectAttributeDesignatorType designator = initializeDesignator(
				"test","subject-role", new StringDataTypeAttribute(), null, false);
		designator.handle(req, reqInfo);
	}

	private boolean isContained(String elem, List<Object> list) {
		for (Object obj : list) {
			if (elem.equals(obj.toString())) {
				return true;
			}
		}
		return false;
	}

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

	private SubjectType initializeSubject(String subjCat, String attrId,
			DataTypeAttribute<?> dataType, String issuer, String value, boolean multiContent) {

		SubjectType sub = new SubjectType();
		sub.setSubjectCategory(subjCat);

		AttributeType attr = new AttributeType();
		attr.setAttributeId(attrId);
		attr.setDataType(dataType);
		attr.setIssuer(issuer);

		AttributeValueType attrVal = new AttributeValueType();
		attrVal.getContent().add(value);
		if(multiContent){
			attrVal.getContent().add(value);
		}

		attr.getAttributeValues().add(attrVal);

		sub.getAttributes().add(attr);

		return sub;
	}
	private SubjectType initializeSubjectWithIllegalType(String subjCat, String attrId,
			DataTypeAttribute<?> dataType, String issuer, Integer value) {

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

	private RequestType initializeRequest(SubjectType s1, SubjectType s2,
			SubjectType s3) {
		RequestType req = new RequestType();
		req.getSubjects().add(s1);
		req.getSubjects().add(s2);
		req.getSubjects().add(s3);
		return req;
	}

	private List<String> initResult(String... args) {
		List<String> returnValues = new ArrayList<String>();
		for (String str : args) {
			returnValues.add(str);
		}
		return returnValues;
	}
}
