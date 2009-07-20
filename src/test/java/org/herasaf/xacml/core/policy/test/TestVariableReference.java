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

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.herasaf.xacml.core.policy.impl.AttributeValueType;
import org.herasaf.xacml.core.policy.impl.ObjectFactory;
import org.herasaf.xacml.core.policy.impl.Variable;
import org.herasaf.xacml.core.policy.impl.VariableDefinitionType;
import org.herasaf.xacml.core.policy.impl.VariableReferenceType;
import org.herasaf.xacml.core.policy.impl.VariableValue;
import org.herasaf.xacml.core.policy.requestinformationfactory.RequestInformationFactoryMock;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestVariableReference {
	ObjectFactory factory;
	private RequestInformationFactoryMock requestInformationFactory;
	
	@BeforeTest
	public void init() {
		requestInformationFactory = new RequestInformationFactoryMock();

		factory = new ObjectFactory();
	}

	@Test(enabled = true)
	public void testHandle() throws Exception {
		Object[] values = new Object[]{"test1", "test2", "test3"};
		RequestInformation reqInfo = requestInformationFactory.createRequestInformation(null, null);
		reqInfo.setVariableDefinitions(initVariableDefinitions(values));

		VariableReferenceType varRef1 = new VariableReferenceType();
		varRef1.setVariableId("1");
		assertEquals(varRef1.handle(new RequestType(), reqInfo), values[0]);

		VariableReferenceType varRef2 = new VariableReferenceType();
		varRef2.setVariableId("2");
		assertEquals(varRef2.handle(new RequestType(), reqInfo), values[1]);

		VariableReferenceType varRef3 = new VariableReferenceType();
		varRef3.setVariableId("3");
		assertEquals(varRef3.handle(new RequestType(), reqInfo), values[2]);





//		assertEquals(variableDefinitions.get("1").getValue(new RequestType(),
//				variableDefinitions), values[0]);
//		assertEquals(variableDefinitions.get("2").getValue(new RequestType(),
//				variableDefinitions), values[1]);
//		assertEquals(variableDefinitions.get("3").getValue(new RequestType(),
//				variableDefinitions), values[2]);

	}

	private Map<String, Variable> initVariableDefinitions(Object[] values) {
		Map<String, Variable> variableDefinitions = new HashMap<String, Variable>();

		VariableDefinitionType varDef1 = new VariableDefinitionType();
		varDef1.setVariableId("1");
		varDef1.setExpression(initAttributeValue(values[0],
				new StringDataTypeAttribute()));

		VariableValue value = new VariableValue(values[1]);

		VariableDefinitionType varDef3 = new VariableDefinitionType();
		varDef3.setVariableId("3");
		varDef3.setExpression(initAttributeValue(values[2],
				new StringDataTypeAttribute()));

		variableDefinitions.put("1", varDef1);
		variableDefinitions.put("2", value);
		variableDefinitions.put("3", varDef3);

		return variableDefinitions;
	}

	private JAXBElement<AttributeValueType> initAttributeValue(Object object,
			DataTypeAttribute<?> dataType) {
		AttributeValueType attrVal = new AttributeValueType();
		attrVal.setDataType(dataType);
		attrVal.getContent().add(object);

		return factory.createAttributeValue(attrVal);
	}
}
