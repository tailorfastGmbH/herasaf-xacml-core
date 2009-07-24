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
import org.herasaf.xacml.core.attributeFinder.impl.AttributeFinderMock;
import org.herasaf.xacml.core.context.RequestInformation;
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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestActionAttributeDesignator {
	RequestInformation reqInfo;

	@BeforeTest
	public void init() {
		reqInfo = new RequestInformation(new AttributeFinderMock());
	}

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
						initResult() },

		};
	}

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

	@SuppressWarnings("unchecked")
	@Test(dataProvider = "successfulActionAttrDesignator", enabled = true)
	public void testHandle(RequestType req,
			ActionAttributeDesignatorType designator, List<Object> result)
			throws Exception {

		List<Object> returnValue = (List<Object>) designator.handle(req,
				reqInfo);
		assertEquals(returnValue.size(), result.size());
		for (Object obj : returnValue) {
			assertTrue(isContained(obj.toString(), result));
		}
	}

	@Test(enabled = true, dataProvider = "actionAttrDesignatorException", expectedExceptions = MissingAttributeException.class)
	public void testHandle(RequestType req,
			ActionAttributeDesignatorType designator) throws Throwable {
		try {
			designator.handle(req, reqInfo);
		} catch (ExpressionProcessingException e) {
			throw e.getCause();
		}
	}

	@Test(enabled = true, expectedExceptions = SyntaxException.class)
	public void testHandleClassCastException() throws Throwable {
		RequestType req = initializeRequest(initializeActionWithIllegalType(
				"action-Id", new StringDataTypeAttribute(), "hsr", 1));
		ActionAttributeDesignatorType designator = initializeDesignator(
				"action-Id", new StringDataTypeAttribute(), null, false);
		designator.handle(req, reqInfo);
	}

	@Test(enabled = true, expectedExceptions = ExpressionProcessingException.class)
	public void testHandleExpressionProcessingException() throws Throwable {
		RequestType req = initializeRequest(initializeAction("action-Id",
				new StringDataTypeAttribute(), "hsr", "Fredi", true));
		ActionAttributeDesignatorType designator = initializeDesignator(
				"action-Id", new StringDataTypeAttribute(), null, false);
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

	private ActionAttributeDesignatorType initializeDesignator(String attrId,
			DataTypeAttribute<?> dataType, String issuer, Boolean mustBePresent) {
		ActionAttributeDesignatorType designator = new ActionAttributeDesignatorType();
		designator.setAttributeId(attrId);
		designator.setDataType(dataType);
		designator.setIssuer(issuer);
		designator.setMustBePresent(mustBePresent);

		return designator;
	}

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

	private RequestType initializeRequest(ActionType a1) {
		RequestType req = new RequestType();
		req.setAction(a1);
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
