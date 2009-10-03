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

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.api.PIP;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.EnvironmentType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.RFC822NameDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.herasaf.xacml.core.policy.ExpressionProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.impl.EnvironmentAttributeDesignatorType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class tests the {@link EnvironmentAttributeDesignatorType}.
 * 
 * @author Florian Huonder
 */
public class TestEnvironmentAttributeDesignator {
	RequestInformation reqInfo;

	/**
	 * Initializes the {@link RequestInformation} with an mock for the {@link PIP}.
	 */
	@BeforeTest
	public void init() {

		reqInfo = new RequestInformation(null); //null means no PIP
	}
	
	/**
	 * Initializes the test successful cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "successfulEnvironmentAttrDesignator")
	public Object[][] successfulEnvironmentAttrDesignator() {
		return new Object[][] {
				new Object[] {
						initializeRequest(initializeEnvironment(
								"environment-Id",
								new StringDataTypeAttribute(), "hsr", "Fredi",
								false)),
						initializeDesignator("environment-Id",
								new StringDataTypeAttribute(), null, false),
						initResult("Fredi") },
				new Object[] {
						initializeRequest(initializeEnvironment(
								"environment-Id",
								new StringDataTypeAttribute(), "hsr", "Fredi",
								false)),
						initializeDesignator("environment-Id",
								new StringDataTypeAttribute(), "hsr", false),
						initResult("Fredi") },
				new Object[] {
						initializeRequest(initializeEnvironment(
								"environment-Id",
								new RFC822NameDataTypeAttribute(), "hsr",
								"Hans@test.ch", false)),
						initializeDesignator("environment-Id",
								new RFC822NameDataTypeAttribute(), "hsr", false),
						initResult("Hans@test.ch") },
				new Object[] {
						initializeRequest(initializeEnvironment(
								"environment-Id",
								new StringDataTypeAttribute(), "hsr",
								"Hans@hotmail.com", false)),
						initializeDesignator("environment-Id",
								new StringDataTypeAttribute(), "hsr", true),
						initResult("Hans@hotmail.com") },
				new Object[] {
						initializeRequest(initializeEnvironment(
								"environment-Id",
								new StringDataTypeAttribute(), "zhw", "Werner",
								false)),
						initializeDesignator("environment-Id",
								new StringDataTypeAttribute(), "zhw", true),
						initResult("Werner") },
				new Object[] {
						initializeRequest(initializeEnvironment(
								"environment-Id",
								new StringDataTypeAttribute(), "zhw", "Werner",
								false)),
						initializeDesignator("environment-Id",
								new StringDataTypeAttribute(), "hsr", false),
						initResult() },

		};
	}

	/**
	 * Initializes the exception test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "environmentAttrDesignatorException")
	public Object[][] environmentAttrDesignatorException() {
		return new Object[][] {
				new Object[] {
						initializeRequest(initializeEnvironment(
								"environment-Id",
								new StringDataTypeAttribute(), "hsr", "Fredi",
								false)),
						initializeDesignator("environment-Id",
								new RFC822NameDataTypeAttribute(), "hsr", true) },
				new Object[] {
						initializeRequest(initializeEnvironment(
								"environment-Id",
								new StringDataTypeAttribute(), "zhw", "Hans",
								false)),
						initializeDesignator("environment-Id",
								new StringDataTypeAttribute(), "hsr", true) },
				new Object[] {
						initializeRequest(initializeEnvironment("env",
								new StringDataTypeAttribute(), "hsr", "Fritz",
								false)),
						initializeDesignator("environment-Id",
								new RFC822NameDataTypeAttribute(), "hsr", true) },
				new Object[] {
						initializeRequest(initializeEnvironment(
								"environment-Id",
								new StringDataTypeAttribute(), "hsr", "Werner",
								false)),
						initializeDesignator("environment-Id",
								new StringDataTypeAttribute(), "fhsg", true) },

		};
	}

	/**
	 * Test the successful cases.
	 * 
	 * @param req The {@link RequestInformation}.
	 * @param designator The {@link EnvironmentAttributeDesignatorType} (is under test)
	 * @param result The expected result.
	 * @throws Exception In case an error occurs.
	 */
	@SuppressWarnings("unchecked")
	@Test(enabled = true, dataProvider = "successfulEnvironmentAttrDesignator")
	public void testHandle(RequestType req,
			EnvironmentAttributeDesignatorType designator, List<Object> result)
			throws Exception{

		List<Object> returnValue = (List<Object>) designator.handle(req, reqInfo);
		assertEquals(returnValue.size(), result.size());
		for (Object obj : returnValue) {
			assertTrue(isContained(obj.toString(), result));
		}
	}

	/**
	 * Tests if all error-cases throw the proper exception.
	 * Expects a {@link MissingAttributeException}.
	 * 
	 * @param req The {@link RequestInformation}.
	 * @param designator The {@link EnvironmentAttributeDesignatorType} (is under test)
	 * @throws Throwable In case an unexpected error occurs.
	 */
	@Test(enabled = true, dataProvider = "environmentAttrDesignatorException", expectedExceptions = MissingAttributeException.class)
	public void testHandle(RequestType req,
			EnvironmentAttributeDesignatorType designator) throws Throwable {
		try {
			designator.handle(req, reqInfo);
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
		RequestType req = initializeRequest(initializeEnvironmentWithIllegalType(
				"environment-Id", new StringDataTypeAttribute(), "hsr", 1));
		EnvironmentAttributeDesignatorType designator = initializeDesignator(
				"environment-Id", new StringDataTypeAttribute(), null, false);
		designator.handle(req, reqInfo);
	}

	/**
	 * Tests if all error-cases throw the proper exception.
	 * Expects a {@link ExpressionProcessingException}.
	 * 
	 * @throws Throwable In case an unexpected error occurs.
	 */
	@Test(enabled = true, expectedExceptions = ExpressionProcessingException.class)
	public void testHandleExpressionProcessingException() throws Throwable {
		RequestType req = initializeRequest(initializeEnvironment(
				"environment-Id", new StringDataTypeAttribute(), "hsr",
				"Fredi", true));
		EnvironmentAttributeDesignatorType designator = initializeDesignator(
				"environment-Id", new StringDataTypeAttribute(), null, false);
		designator.handle(req, reqInfo);
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
	 * Initializes the {@link EnvironmentAttributeDesignatorType} with ID, data type, issuer and must be present.
	 * 
	 * @param attrId The attribute ID.
	 * @param dataType The data type of the designator.
	 * @param issuer The issuer of the designator.
	 * @param mustBePresent True if mustbepresent is on.
	 * 
	 * @return The initialized {@link EnvironmentAttributeDesignatorType}.
	 */
	private EnvironmentAttributeDesignatorType initializeDesignator(
			String attrId, DataTypeAttribute<?> dataType, String issuer,
			Boolean mustBePresent) {
		EnvironmentAttributeDesignatorType designator = new EnvironmentAttributeDesignatorType();
		designator.setAttributeId(attrId);
		designator.setDataType(dataType);
		designator.setIssuer(issuer);
		designator.setMustBePresent(mustBePresent);

		return designator;
	}

	/**
	 * Initializes the {@link EnvironmentType}.
	 * 
	 * @param attrId The Attribute ID of the of the attribute contained in the {@link EnvironmentType}.
	 * @param dataType The datatype of the attribute.
	 * @param issuer The issuer of the attribute.
	 * @param value The value of the attribute
	 * @param multiContent True if the attribute contains multi content.
	 * @return The created {@link EnvironmentType}.
	 */
	private EnvironmentType initializeEnvironment(String attrId,
			DataTypeAttribute<?> dataType, String issuer, String value,
			boolean multiContent) {

		EnvironmentType env = new EnvironmentType();

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

		env.getAttributes().add(attr);

		return env;
	}

	/**
	 * Creates an environment with an illegal type.
	 * 
	 * @param attrId The Attribute ID of the of the attribute contained in the {@link EnvironmentType}.
	 * @param dataType The datatype of the attribute.
	 * @param issuer The issuer of the attribute.
	 * @param value The value of the attribute
	 * @return The created {@link EnvironmentType}.
	 */
	private EnvironmentType initializeEnvironmentWithIllegalType(String attrId,
			DataTypeAttribute<?> dataType, String issuer, Integer value) {

		EnvironmentType env = new EnvironmentType();

		AttributeType attr = new AttributeType();
		attr.setAttributeId(attrId);
		attr.setDataType(dataType);
		attr.setIssuer(issuer);

		AttributeValueType attrVal = new AttributeValueType();

		// Illegal Type Integer in content
		attrVal.getContent().add(value);

		attr.getAttributeValues().add(attrVal);

		env.getAttributes().add(attr);

		return env;
	}

	/**
	 * Initializes the request with the given {@link EnvironmentType}.
	 * 
	 * @param e1 The environment type to place into the {@link RequestType}.
	 * @return The initialized {@link RequestType}.
	 */
	private RequestType initializeRequest(EnvironmentType e1) {
		RequestType req = new RequestType();
		req.setEnvironment(e1);
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
