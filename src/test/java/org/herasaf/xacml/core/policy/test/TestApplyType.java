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

import java.util.Arrays;

import javax.xml.bind.JAXBElement;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResourceType;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.impl.bagFunctions.StringBagFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.StringOneAndOnlyFunction;
import org.herasaf.xacml.core.function.impl.equalityPredicates.StringEqualFunction;
import org.herasaf.xacml.core.function.impl.higherOrderBagFunctions.AnyOfFunction;
import org.herasaf.xacml.core.policy.impl.ApplyType;
import org.herasaf.xacml.core.policy.impl.AttributeValueType;
import org.herasaf.xacml.core.policy.impl.ExpressionType;
import org.herasaf.xacml.core.policy.impl.FunctionType;
import org.herasaf.xacml.core.policy.impl.ObjectFactory;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.herasaf.xacml.core.policy.impl.ResourceAttributeDesignatorType;
import org.relaxng.datatype.Datatype;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests the {@link ApplyType}.
 * @author Florian Huonder
 */
public class TestApplyType {
	ObjectFactory factory;
	
	/**
	 * Creates successful test cases.
	 * @return The test cases.
	 */
	@DataProvider(name = "successfulApplyType")
	public Object[][] successfulApplyType() {
		return new Object[][] { new Object[] {
				initializeRequest(initializeResource("resource-name",
						new StringDataTypeAttribute(), "hsr", "test.txt")),
				initApply(new StringEqualFunction(), new JAXBElement<?>[] {
						initApply(new StringOneAndOnlyFunction(),
								new JAXBElement<?>[] { initResAttrDesignator(
										"resource-name",
										new StringDataTypeAttribute(), "hsr",
										true) }),
						initAttributeValue("test.txt",
								new StringDataTypeAttribute()) }), true }, };
	}

	/**
	 * Creates test cases that cause an exception.
	 * @return The test cases.
	 */
	@DataProvider(name = "applyTypeExceptions")
	public Object[][] applyTypeExceptions() {
		return new Object[][] {

				new Object[] {
						initializeRequest(initializeResource("resource-name",
								new StringDataTypeAttribute(), "hsr",
								"test.txt")),
						initApply(new StringEqualFunction(),
								new JAXBElement<?>[] { factory
										.createPolicy(new PolicyType()) }),
						true },
				new Object[] {
						initializeRequest(initializeResource("resource-name",
								new StringDataTypeAttribute(), "hsr",
								"test.txt")),
						initApply(new StringEqualFunction(),
								new JAXBElement<?>[] { null }), true } };
	}

	/**
	 * Creates test cases with the {@link FunctionType}.
	 * @return The test cases.
	 */
	@DataProvider(name = "applyWithFunctionType")
	public Object[][] applyWithFunctionType() {
		return new Object[][] {

		new Object[] { initApply(new AnyOfFunction(), new JAXBElement<?>[] {
				initFunction(new StringEqualFunction()),
				initAttributeValue("Paul", new StringDataTypeAttribute()),
				initApply(new StringBagFunction(), new JAXBElement<?>[] {
						initAttributeValue("John",
								new StringDataTypeAttribute()),
						initAttributeValue("Paul",
								new StringDataTypeAttribute()),
						initAttributeValue("George",
								new StringDataTypeAttribute()),
						initAttributeValue("Ringo",
								new StringDataTypeAttribute()) }) }), true } };
	}

	/**
	 * Initializes the {@link ObjectFactory}.
	 */
	@BeforeTest
	public void beforeTest() {
		factory = new ObjectFactory();
	}

	/**
	 * Tests if the {@link ApplyType} behaves as expected.
	 * @param request The {@link RequestType} needed to for the handle method.
	 * @param JaxbElem The jaxb element containing the {@link ApplyType}.
	 * @param result The expected result.
	 * @throws Exception In case an error occurs.
	 */
	@Test(enabled = true, dataProvider = "successfulApplyType")
	public void testHandle(RequestType request,
			JAXBElement<ApplyType> JaxbElem, Object result) throws Exception {
		ApplyType apply = JaxbElem.getValue();
		assertEquals(apply.handle(request, new RequestInformation(null)),
				result);
	}

	/**
	 * Tests if the error cases throw the proper exception.
	 * The expected exception is {@link SyntaxException}.
	 * 
	 * @param request The {@link RequestType} needed to for the handle method.
	 * @param JaxbElem The jaxb element containing the {@link ApplyType}.
	 * @param result The expected result.
	 * @throws Exception In case an error occurs.
	 */
	@Test(enabled = true, dataProvider = "applyTypeExceptions", expectedExceptions = SyntaxException.class)
	public void testHandleExceptions(RequestType request,
			JAXBElement<ApplyType> JaxbElem, Object result) throws Exception {
		ApplyType apply = JaxbElem.getValue();
		assertEquals(apply.handle(request, new RequestInformation(null)),
				result);
	}

	/**
	 * Tests the {@link ApplyType} with the {@link FunctionType}.
	 * 
	 * @param JaxbElem The jaxb element containing the {@link ApplyType}.
	 * @param result The expected result.
	 * @throws Exception In case an error occurs.
	 */
	@Test(enabled = true, dataProvider = "applyWithFunctionType")
	public void testApplyWithFunctionType(JAXBElement<ApplyType> JaxbElem,
			Object result) throws Exception {
		ApplyType apply = JaxbElem.getValue();
		assertEquals(apply.handle(new RequestType(), new RequestInformation(null)),
				result);

	}

	/**
	 * Initializes the ApplyType with a {@link Function}.
	 * 
	 * @param function The function to place in the {@link ApplyType}.
	 * @param expressions The {@link ExpressionType}s to place into the {@link ApplyType}
	 * @return The created {@link ApplyType} within a {@link JAXBElement}.
	 */
	private JAXBElement<ApplyType> initApply(Function function,
			JAXBElement<?>[] expressions) {
		ApplyType apply = new ApplyType();
		apply.setFunction(function);
		apply.getExpressions().addAll(Arrays.asList(expressions));

		return factory.createApply(apply);
	}

	/**
	 * Creates the {@link AttributeValueType}.
	 * @param content The content of the {@link AttributeValueType}.
	 * @param dataType The data type of the content.
	 * @return The created {@link AttributeValueType} within a {@link JAXBElement}.
	 */
	private JAXBElement<AttributeValueType> initAttributeValue(String content,
			DataTypeAttribute<?> dataType) {
		AttributeValueType attrVal = new AttributeValueType();
		attrVal.setDataType(dataType);
		attrVal.getContent().add(content);

		return factory.createAttributeValue(attrVal);
	}

	/**
	 * Creates a new {@link ResourceAttributeDesignatorType}.
	 * 
	 * @param attrId The attribute ID of the {@link ResourceAttributeDesignatorType}.
	 * @param dataType The data type of the {@link ResourceAttributeDesignatorType}.
	 * @param issuer The issuer of the {@link ResourceAttributeDesignatorType}.
	 * @param mustBePresent True if the {@link ResourceAttributeDesignatorType} shall have mustBePresent set on, false otherwise.
	 * @return The created {@link ResourceAttributeDesignatorType} within a {@link JAXBElement}.
	 */
	private JAXBElement<ResourceAttributeDesignatorType> initResAttrDesignator(
			String attrId, DataTypeAttribute<?> dataType, String issuer,
			Boolean mustBePresent) {
		ResourceAttributeDesignatorType designator = new ResourceAttributeDesignatorType();
		designator.setAttributeId(attrId);
		designator.setDataType(dataType);
		designator.setIssuer(issuer);
		designator.setMustBePresent(mustBePresent);

		return factory.createResourceAttributeDesignator(designator);
	}

	/**
	 * Initializes the {@link ResourceType}.
	 * @param attrId The attribute ID of the attribute of the {@link ResourceType}.
	 * @param dataType The {@link Datatype} of the attribute of the {@link ResourceType}.
	 * @param issuer The issuer of the attribute of the {@link ResourceType}.
	 * @param value The attribute value.
	 * @return The created {@link ResourceType}.
	 */
	private ResourceType initializeResource(String attrId,
			DataTypeAttribute<?> dataType, String issuer, String value) {

		ResourceType res = new ResourceType();

		AttributeType attr = new AttributeType();
		attr.setAttributeId(attrId);
		attr.setDataType(dataType);
		attr.setIssuer(issuer);

		org.herasaf.xacml.core.context.impl.AttributeValueType attrVal = new org.herasaf.xacml.core.context.impl.AttributeValueType();
		attrVal.getContent().add(value);

		attr.getAttributeValues().add(attrVal);

		res.getAttributes().add(attr);

		return res;
	}

	/**
	 * Initializes a {@link RequestType} containing a {@link ResourceType}.
	 * @param r1 The {@link ResourceType} to place in the {@link RequestType}.
	 * @return The created {@link RequestType}.
	 */
	private RequestType initializeRequest(ResourceType r1) {
		RequestType req = new RequestType();
		req.getResources().add(r1);
		return req;
	}

	/**
	 * Initializes the {@link FunctionType} with a concrete {@link Function}.
	 * @param function The {@link Function} to place into the {@link FunctionType}.
	 * @return The created {@link FunctionType} within a {@link JAXBElement}.
	 */
	private JAXBElement<FunctionType> initFunction(Function function) {
		FunctionType func = new FunctionType();
		func.setFunction(function);

		return factory.createFunction(func);
	}
}