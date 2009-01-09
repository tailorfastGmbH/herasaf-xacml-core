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

import java.util.Arrays;

import javax.xml.bind.JAXBElement;

import org.herasaf.xacml.SyntaxException;
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
import org.herasaf.xacml.core.policy.impl.FunctionType;
import org.herasaf.xacml.core.policy.impl.ObjectFactory;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.herasaf.xacml.core.policy.impl.ResourceAttributeDesignatorType;
import org.herasaf.xacml.core.policy.requestinformationfactory.RequestInformationFactoryMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:context/ApplicationContext.xml" })
public class TestApplyType extends AbstractTestNGSpringContextTests{
	ObjectFactory factory;
	@Autowired
	private RequestInformationFactoryMock requestInformationFactory;	
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

	@BeforeTest
	public void beforeTest() {
		factory = new ObjectFactory();
	}

	@Test(enabled = true, dataProvider = "successfulApplyType")
	public void testHandle(RequestType request,
			JAXBElement<ApplyType> JaxbElem, Object result) throws Exception {
		ApplyType apply = JaxbElem.getValue();
		assertEquals(apply.handle(request, requestInformationFactory.createRequestInformation(null, null)),
				result);
	}

	@Test(enabled = true, dataProvider = "applyTypeExceptions", expectedExceptions = SyntaxException.class)
	public void testHandleExceptions(RequestType request,
			JAXBElement<ApplyType> JaxbElem, Object result) throws Exception {
		ApplyType apply = JaxbElem.getValue();
		assertEquals(apply.handle(request,requestInformationFactory.createRequestInformation(null, null)),
				result);
	}

	@Test(enabled = true, dataProvider = "applyWithFunctionType")
	public void testApplyWithFunctionType(JAXBElement<ApplyType> JaxbElem,
			Object result) throws Exception {
		ApplyType apply = JaxbElem.getValue();
		assertEquals(apply.handle(new RequestType(), requestInformationFactory.createRequestInformation(null, null)),
				result);

	}

	private JAXBElement<ApplyType> initApply(Function function,
			JAXBElement<?>[] expressions) {
		ApplyType apply = new ApplyType();
		apply.setFunction(function);
		apply.getExpressions().addAll(Arrays.asList(expressions));

		return factory.createApply(apply);
	}

	private JAXBElement<AttributeValueType> initAttributeValue(String content,
			DataTypeAttribute<?> dataType) {
		AttributeValueType attrVal = new AttributeValueType();
		attrVal.setDataType(dataType);
		attrVal.getContent().add(content);

		return factory.createAttributeValue(attrVal);
	}

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

	private RequestType initializeRequest(ResourceType r1) {
		RequestType req = new RequestType();
		req.getResources().add(r1);
		return req;
	}

	private JAXBElement<FunctionType> initFunction(Function function) {
		FunctionType func = new FunctionType();
		func.setFunction(function);

		return factory.createFunction(func);
	}

	//
	//
	// private JAXBElement<AttributeDesignatorType> initSubAttrDesignator(
	// String attrId, DataTypeAttribute<?> dataType, String issuer,
	// Boolean mustBePresent, String subCat) {
	// SubjectAttributeDesignatorType designator = new
	// SubjectAttributeDesignatorType();
	// designator.setAttributeId(attrId);
	// designator.setDataType(dataType);
	// designator.setIssuer(issuer);
	// designator.setMustBePresent(mustBePresent);
	// designator.setSubjectCategory(subCat);
	//
	// return factory.createEnvironmentAttributeDesignator(designator);
	// }
	//
	//
	//
	// private JAXBElement<VariableReferenceType> initVariableRef(String varId)
	// {
	// VariableReferenceType varRef = new VariableReferenceType();
	// varRef.setVariableId(varId);
	//
	// return factory.createVariableReference(varRef);
	// }
	//
	// private JAXBElement<AttributeDesignatorType> initActAttrDesignator(
	// String attrId, DataTypeAttribute<?> dataType, String issuer,
	// Boolean mustBePresent) {
	// ActionAttributeDesignatorType designator = new
	// ActionAttributeDesignatorType();
	// designator.setAttributeId(attrId);
	// designator.setDataType(dataType);
	// designator.setIssuer(issuer);
	// designator.setMustBePresent(mustBePresent);
	//
	// return factory.createActionAttributeDesignator(designator);
	// }
	// private JAXBElement<AttributeDesignatorType> initEnvAttrDesignator(
	// String attrId, DataTypeAttribute<?> dataType, String issuer,
	// Boolean mustBePresent) {
	// EnvironmentAttributeDesignatorType designator = new
	// EnvironmentAttributeDesignatorType();
	// designator.setAttributeId(attrId);
	// designator.setDataType(dataType);
	// designator.setIssuer(issuer);
	// designator.setMustBePresent(mustBePresent);
	//
	// return factory.createEnvironmentAttributeDesignator(designator);
	// }
}
