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

package org.herasaf.xacml.core.policy.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.api.PIP;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResourceType;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.RFC822NameDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.herasaf.xacml.core.policy.ExpressionProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.impl.ResourceAttributeDesignatorType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class tests the {@link ResourceAttributeDesignatorType}.
 * 
 * @author Florian Huonder
 */
public class TestResourceAttributeDesignator {
	EvaluationContext evaluationContext;

	@BeforeTest
	public void init() {

		evaluationContext = new EvaluationContext();
	}

	/**
	 * Initializes the {@link EvaluationContext} with an mock for the {@link PIP}.
	 */
	@DataProvider(name = "successfulResourceAttrDesignator")
	public Object[][] successfulResourceAttrDesignator() {
		return new Object[][] {
				new Object[] {
						initializeRequest(initializeResource("resource-name",
								new StringDataTypeAttribute(), "hsr", "Fredi",
								false), initializeResource("resource-name",
								new StringDataTypeAttribute(), "hsr", "Hans",
								false), initializeResource("resource-name",
								new StringDataTypeAttribute(), "hsr", "Fritz",
								false)),
						initializeDesignator("resource-name",
								new StringDataTypeAttribute(), null, false),
						initResult("Fredi", "Hans", "Fritz") },
				new Object[] {
						initializeRequest(initializeResource("resource-name",
								new StringDataTypeAttribute(), "hsr", "Fredi",
								false), initializeResource("resource-name",
								new StringDataTypeAttribute(), "hsr", "Hans",
								false), initializeResource("resource-name",
								new StringDataTypeAttribute(), "hsr", "Fritz",
								false)),
						initializeDesignator("resource-name",
								new StringDataTypeAttribute(), "hsr", false),
						initResult("Fredi", "Hans", "Fritz") },
				new Object[] {
						initializeRequest(initializeResource("resource-name",
								new StringDataTypeAttribute(), "hsr", "Hans",
								false), initializeResource("resource-name",
								new RFC822NameDataTypeAttribute(), "hsr",
								"Fredi@gmx.ch", false), initializeResource(
								"resource-name", new StringDataTypeAttribute(),
								"hsr", "Fritz", false)),
						initializeDesignator("resource-name",
								new StringDataTypeAttribute(), "hsr", false),
						initResult("Hans", "Fritz") },
				new Object[] {
						initializeRequest(initializeResource("resource-name",
								new StringDataTypeAttribute(), "hsr", "Fritz",
								false), initializeResource("resource-name",
								new RFC822NameDataTypeAttribute(), "hsr",
								"Hans@hotmail.com", false), initializeResource(
								"resource-name", new StringDataTypeAttribute(),
								"zhw", "Fredi", false)),
						initializeDesignator("resource-name",
								new RFC822NameDataTypeAttribute(), "hsr", true),
						initResult("Hans@hotmail.com") },
				new Object[] {
						initializeRequest(initializeResource("role",
								new StringDataTypeAttribute(), "hsr", "Werner",
								false), initializeResource("resource-name",
								new RFC822NameDataTypeAttribute(), "zhw",
								"Werner@beinhart.de", false),
								initializeResource("resource-name",
										new StringDataTypeAttribute(), "hsr",
										"Fridolin", false)),
						initializeDesignator("resource-name",
								new StringDataTypeAttribute(), "hsr", true),
						initResult("Fridolin") },

		};
	}

	/**
	 * Initializes the exception test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "resourceAttrDesignatorException")
	public Object[][] resourceAttrDesignatorException() {
		return new Object[][] {
				new Object[] {
						initializeRequest(initializeResource("resource-name",
								new StringDataTypeAttribute(), "hsr", "Fredi",
								false), initializeResource("resource-name",
								new StringDataTypeAttribute(), "hsr", "Hans",
								false), initializeResource("resource-name",
								new StringDataTypeAttribute(), "hsr", "Fritz",
								false)),
						initializeDesignator("resource-name",
								new RFC822NameDataTypeAttribute(), "hsr", true) },
				new Object[] {
						initializeRequest(initializeResource("resource-name",
								new StringDataTypeAttribute(), "zhw", "Hans",
								false), initializeResource("resource-name",
								new RFC822NameDataTypeAttribute(), "hsr",
								"Fredi@gmx.ch", false), initializeResource(
								"resource", new StringDataTypeAttribute(),
								"hsr", "Fritz", false)),
						initializeDesignator("resource-name",
								new StringDataTypeAttribute(), "hsr", true) },
				new Object[] {
						initializeRequest(initializeResource("res",
								new StringDataTypeAttribute(), "hsr", "Fritz",
								false), initializeResource("subject",
								new RFC822NameDataTypeAttribute(), "hsr",
								"Hans@hotmail.com", false), initializeResource(
								"resource-name", new StringDataTypeAttribute(),
								"zhw", "Fredi", false)),
						initializeDesignator("resource-name",
								new RFC822NameDataTypeAttribute(), "hsr", true) },
				new Object[] {
						initializeRequest(initializeResource("role",
								new StringDataTypeAttribute(), "hsr", "Werner",
								false), initializeResource("resource-name",
								new RFC822NameDataTypeAttribute(), "zhw",
								"Werner@beinhart.de", false),
								initializeResource("resource-name",
										new StringDataTypeAttribute(), "hsr",
										"Fridolin", false)),
						initializeDesignator("resource-name",
								new StringDataTypeAttribute(), "fhsg", true) },

		};
	}

	/**
	 * Test the successful cases.
	 * 
	 * @param req The {@link EvaluationContext}.
	 * @param designator The {@link ResourceAttributeDesignatorType} (is under test)
	 * @param result The expected result.
	 * @throws Exception In case an error occurs.
	 */
	@SuppressWarnings("unchecked")
	@Test(dataProvider = "successfulResourceAttrDesignator")
	public void testHandle(RequestType req,
			ResourceAttributeDesignatorType designator, List<Object> result)
			throws Exception {

		List<Object> returnValue = (List<Object>) designator.handle(req,
				evaluationContext);
		assertEquals(returnValue.size(), result.size());
		for (Object obj : returnValue) {
			assertTrue(isContained(obj.toString(), result));
		}
	}

	/**
	 * Tests if all error-cases throw the proper exception.
	 * Expects a {@link MissingAttributeException}.
	 * 
	 * @param req The {@link EvaluationContext}.
	 * @param designator The {@link ResourceAttributeDesignatorType} (is under test)
	 * @throws Throwable In case an unexpected error occurs.
	 */
	@Test(dataProvider = "resourceAttrDesignatorException", expectedExceptions = MissingAttributeException.class)
	public void testHandle(RequestType req,
			ResourceAttributeDesignatorType designator) throws Throwable {
		try {
			designator.handle(req, evaluationContext);
		} catch (ExpressionProcessingException e) {
			throw e.getCause();
		}
	}

	/**
	 * Tests if all error-cases throw the proper exception.
	 * Expects a {@link SyntaxException}.
	 * 
	 * @throws Throwable In case an unexpected error occurs.
	 */
	@Test(enabled = true, expectedExceptions = SyntaxException.class)
	public void testHandleClassCastException() throws Throwable {
		RequestType req = initializeRequest(initializeResourceWithIllegalType(
				"resource-name", new StringDataTypeAttribute(), "hsr", 1),
				initializeResourceWithIllegalType("resource-name",
						new StringDataTypeAttribute(), "hsr", 1),
				initializeResourceWithIllegalType("resource-name",
						new StringDataTypeAttribute(), "hsr", 1));
		ResourceAttributeDesignatorType designator = initializeDesignator(
				"resource-name", new StringDataTypeAttribute(), null, false);
		designator.handle(req, evaluationContext);
	}

	/**
	 * Tests if all error-cases throw the proper exception.
	 * Expects a {@link ExpressionProcessingException}.
	 * 
	 * @throws Throwable In case an unexpected error occurs.
	 */
	@Test(enabled = true, expectedExceptions = ExpressionProcessingException.class)
	public void testHandleExpressionProcessingException() throws Throwable {
		RequestType req = initializeRequest(initializeResource("resource-name",
				new StringDataTypeAttribute(), "hsr", "Fredi", true),
				initializeResource("resource-name",
						new StringDataTypeAttribute(), "hsr", "Fredi", false),
				initializeResource("resource-name",
						new StringDataTypeAttribute(), "hsr", "Fredi", false));
		ResourceAttributeDesignatorType designator = initializeDesignator(
				"resource-name", new StringDataTypeAttribute(), null, false);
		designator.handle(req, evaluationContext);
	}

	/**
	 * Checks if a certain {@link String} is contained in a {@link List} of {@link Object}s.
	 * @param elem The {@link String} that is expected.
	 * @param list The list the may contain the element.
	 * @return True if the element is contained in the {@link List}, false otherwise.
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
	 * Initializes the {@link ResourceAttributeDesignatorType} with ID, data type, issuer and must be present.
	 * 
	 * @param attrId The attribute ID.
	 * @param dataType The data type of the designator.
	 * @param issuer The issuer of the designator.
	 * @param mustBePresent True if mustbepresent is on.
	 * 
	 * @return The initialized {@link ResourceAttributeDesignatorType}.
	 */
	private ResourceAttributeDesignatorType initializeDesignator(String attrId,
			DataTypeAttribute<?> dataType, String issuer, Boolean mustBePresent) {
		ResourceAttributeDesignatorType designator = new ResourceAttributeDesignatorType();
		designator.setAttributeId(attrId);
		designator.setDataType(dataType);
		designator.setIssuer(issuer);
		designator.setMustBePresent(mustBePresent);

		return designator;
	}

	/**
	 * Initializes the {@link ResourceType}.
	 * 
	 * @param attrId The Attribute ID of the of the attribute contained in the {@link ResourceType}.
	 * @param dataType The datatype of the attribute.
	 * @param issuer The issuer of the attribute.
	 * @param value The value of the attribute
	 * @param multiContent True if the attribute contains multi content.
	 * @return The created {@link ResourceType}.
	 */
	private ResourceType initializeResource(String attrId,
			DataTypeAttribute<?> dataType, String issuer, String value,
			boolean multiContent) {

		ResourceType res = new ResourceType();

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

		res.getAttributes().add(attr);

		return res;
	}

	/**
	 * Creates an resource with an illegal type.
	 * 
	 * @param attrId The Attribute ID of the of the attribute contained in the {@link ResourceType}.
	 * @param dataType The datatype of the attribute.
	 * @param issuer The issuer of the attribute.
	 * @param value The value of the attribute
	 * @return The created {@link ResourceType}.
	 */
	private ResourceType initializeResourceWithIllegalType(String attrId,
			DataTypeAttribute<?> dataType, String issuer, Integer value) {

		ResourceType res = new ResourceType();

		AttributeType attr = new AttributeType();
		attr.setAttributeId(attrId);
		attr.setDataType(dataType);
		attr.setIssuer(issuer);

		AttributeValueType attrVal = new AttributeValueType();
		attrVal.getContent().add(value);

		attr.getAttributeValues().add(attrVal);

		res.getAttributes().add(attr);

		return res;
	}

	/**
	 * Initializes the request with the given {@link ResourceType}.
	 * 
	 * @param r1 The first resource type to place into the {@link RequestType}.
	 * @param r2 The second resource type to place into the {@link RequestType}.
	 * @param r3 The third resource type to place into the {@link RequestType}.
	 * @return The initialized {@link RequestType}.
	 */
	private RequestType initializeRequest(ResourceType r1, ResourceType r2,
			ResourceType r3) {
		RequestType req = new RequestType();
		req.getResources().add(r1);
		req.getResources().add(r2);
		req.getResources().add(r3);
		return req;
	}

	/**
	 * Initializes the expected results.
	 * 
	 * @param args The resultes.
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
