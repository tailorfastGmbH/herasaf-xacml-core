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
import org.herasaf.xacml.core.context.impl.ActionType;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.RFC822NameDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.herasaf.xacml.core.policy.ExpressionProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.impl.ActionAttributeDesignatorType;
import org.herasaf.xacml.core.targetMatcher.impl.TargetMatcherImpl;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class tests the {@link ActionAttributeDesignatorType}.
 * 
 * @author Florian Huonder
 */
public class TestActionAttributeDesignator {
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
	 * Initializes the test successful cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "successfulActionAttrDesignator")
	public Object[][] successfulActionAttrDesignator() {
		return new Object[][] {
				new Object[] {
						initializeRequest(initializeAction("action-id",
								new StringDataTypeAttribute(), "hsr", "read",
								false)),
						initializeDesignator("action-id",
								new StringDataTypeAttribute(), null, false),
						initResult("read") },
				new Object[] {
						initializeRequest(initializeAction("action-id",
								new StringDataTypeAttribute(), "hsr", "read",
								false)),
						initializeDesignator("action-id",
								new StringDataTypeAttribute(), "hsr", false),
						initResult("read") },
				new Object[] {
						initializeRequest(initializeAction("action-id",
								new StringDataTypeAttribute(), "hsr", "write",
								false)),
						initializeDesignator("action-id",
								new StringDataTypeAttribute(), "hsr", false),
						initResult("write") },
				new Object[] {
						initializeRequest(initializeAction("action-id",
								new StringDataTypeAttribute(), "hsr", "write",
								false)),
						initializeDesignator("action-id",
								new StringDataTypeAttribute(), "hsr", true),
						initResult("write") },
				new Object[] {
						initializeRequest(initializeAction("action-id",
								new StringDataTypeAttribute(), "zhw", "read",
								false)),
						initializeDesignator("action-id",
								new StringDataTypeAttribute(), "zhw", true),
						initResult("read") },
				new Object[] {
						initializeRequest(initializeAction("action-id",
								new StringDataTypeAttribute(), "zhw", "read",
								false)),
						initializeDesignator("action-id",
								new StringDataTypeAttribute(), "hsr", false),
						initResult() }, };
	}

	/**
	 * Initializes the exception test cases.
	 * 
	 * @return The test cases.
	 */
	@DataProvider(name = "actionAttrDesignatorException")
	public Object[][] actionAttrDesignatorException() {
		return new Object[][] {
				new Object[] {
						initializeRequest(initializeAction("action-id",
								new StringDataTypeAttribute(), "hsr", "read",
								false)),
						initializeDesignator("action-id",
								new RFC822NameDataTypeAttribute(), "hsr", true) },
				new Object[] {
						initializeRequest(initializeAction("action-id",
								new StringDataTypeAttribute(), "zhw", "read",
								false)),
						initializeDesignator("action-id",
								new StringDataTypeAttribute(), "hsr", true) },
				new Object[] {
						initializeRequest(initializeAction("act",
								new StringDataTypeAttribute(), "hsr", "read",
								false)),
						initializeDesignator("action-id",
								new RFC822NameDataTypeAttribute(), "hsr", true) },
				new Object[] {
						initializeRequest(initializeAction("action-id",
								new StringDataTypeAttribute(), "hsr", "write",
								false)),
						initializeDesignator("action-id",
								new StringDataTypeAttribute(), "fhsg", true) },

		};
	}

	/**
	 * Test the successful cases.
	 * 
	 * @param req
	 *            The {@link EvaluationContext}.
	 * @param designator
	 *            The {@link ActionAttributeDesignatorType} (is under test)
	 * @param result
	 *            The expected result.
	 * @throws Exception
	 *             In case an error occurs.
	 */
	@SuppressWarnings("unchecked")
	@Test(dataProvider = "successfulActionAttrDesignator", enabled = true)
	public void testHandle(RequestType req,
			ActionAttributeDesignatorType designator, List<Object> result)
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
	 *            The {@link ActionAttributeDesignatorType} (is under test)
	 * @throws Throwable
	 *             In case an unexpected error occurs.
	 */
	@Test(enabled = true, dataProvider = "actionAttrDesignatorException", expectedExceptions = MissingAttributeException.class)
	public void testHandle(RequestType req,
			ActionAttributeDesignatorType designator) throws Throwable {
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
		RequestType req = initializeRequest(initializeActionWithIllegalType(
				"action-Id", new StringDataTypeAttribute(), "hsr", 1));
		ActionAttributeDesignatorType designator = initializeDesignator(
				"action-Id", new StringDataTypeAttribute(), null, false);
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
		RequestType req = initializeRequest(initializeAction("action-Id",
				new StringDataTypeAttribute(), "hsr", "Fredi", true));
		ActionAttributeDesignatorType designator = initializeDesignator(
				"action-Id", new StringDataTypeAttribute(), null, false);
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
	 * Initializes the {@link ActionAttributeDesignatorType} with ID, data type,
	 * issuer and must be present.
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
	 * @return The initialized {@link ActionAttributeDesignatorType}.
	 */
	private ActionAttributeDesignatorType initializeDesignator(String attrId,
			DataTypeAttribute<?> dataType, String issuer, Boolean mustBePresent) {
		ActionAttributeDesignatorType designator = new ActionAttributeDesignatorType();
		designator.setAttributeId(attrId);
		designator.setDataType(dataType);
		designator.setIssuer(issuer);
		designator.setMustBePresent(mustBePresent);

		return designator;
	}

	/**
	 * Initializes the {@link ActionType}.
	 * 
	 * @param attrId
	 *            The Attribute ID of the of the attribute contained in the
	 *            {@link ActionType}.
	 * @param dataType
	 *            The datatype of the attribute.
	 * @param issuer
	 *            The issuer of the attribute.
	 * @param value
	 *            The value of the attribute
	 * @param multiContent
	 *            True if the attribute contains multi content.
	 * @return The created {@link ActionType}.
	 */
	private ActionType initializeAction(String attrId,
			DataTypeAttribute<?> dataType, String issuer, String value,
			boolean multiContent) {

		ActionType act = new ActionType();

		AttributeType attr = new AttributeType();
		attr.setAttributeId(attrId);
		attr.setDataType(dataType);
		attr.setIssuer(issuer);

		AttributeValueType attrVal = new AttributeValueType();
		attrVal.getContent().add(value);

		attr.getAttributeValues().add(attrVal);
		if (multiContent) {
			attrVal.getContent().add(value);
		}

		act.getAttributes().add(attr);

		return act;
	}

	/**
	 * Creates an action with an illegal type.
	 * 
	 * @param attrId
	 *            The Attribute ID of the of the attribute contained in the
	 *            {@link ActionType}.
	 * @param dataType
	 *            The datatype of the attribute.
	 * @param issuer
	 *            The issuer of the attribute.
	 * @param value
	 *            The value of the attribute
	 * @return The created {@link ActionType}.
	 */
	private ActionType initializeActionWithIllegalType(String attrId,
			DataTypeAttribute<?> dataType, String issuer, Integer value) {

		ActionType act = new ActionType();

		AttributeType attr = new AttributeType();
		attr.setAttributeId(attrId);
		attr.setDataType(dataType);
		attr.setIssuer(issuer);

		AttributeValueType attrVal = new AttributeValueType();
		attrVal.getContent().add(value);

		attr.getAttributeValues().add(attrVal);

		act.getAttributes().add(attr);

		return act;
	}

	/**
	 * Initializes the request with the given {@link ActionType}.
	 * 
	 * @param a1
	 *            The action type to place into the {@link RequestType}.
	 * @return The initialized {@link RequestType}.
	 */
	private RequestType initializeRequest(ActionType a1) {
		RequestType req = new RequestType();
		req.setAction(a1);
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
