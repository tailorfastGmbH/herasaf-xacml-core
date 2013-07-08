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

package org.herasaf.xacml.core.converter.test;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.herasaf.xacml.core.converter.URNToDataTypeConverter;
import org.herasaf.xacml.core.converter.URNToFunctionConverter;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.impl.equalityPredicates.StringEqualFunction;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests the {@link URNToFunctionConverter} JAXB converter.
 * 
 * @author Sacha Dolski
 */
public class TestURNToFunctionConverter {

	static final String FUNCTION_ID = "urn:oasis:names:tc:xacml:1.0:function:string-equal";
	private URNToFunctionConverter converter;
	private Function function;
	private Map<String, Function> map;

	/**
	 * Initializes {@link URNToFunctionConverter} with a {@link Function}.
	 */
	@BeforeTest
	public void beforeTest() {
		converter = new URNToFunctionConverter();
		function = new StringEqualFunction();
		map = new HashMap<String, Function>();
		map.put(FUNCTION_ID, function);

		URNToFunctionConverter.setFunctions(map);
	}

	/**
	 * Tests if the unmarshalling works correctly. That means that the
	 * {@link URNToFunctionConverter#unmarshal(String)} returns the proper
	 * object.
	 * 
	 * @throws IllegalArgumentException
	 *             In case of an improper argument.
	 */
	@Test
	public void testConvertToStringEqualsFunction()
			throws IllegalArgumentException {
		assertEquals(converter.unmarshal(FUNCTION_ID), function);
	}

	/**
	 * Expects an {@link IllegalArgumentException} because an improper argument
	 * is given to the {@link URNToDataTypeConverter#unmarshal(String)} method.
	 * 
	 * @throws IllegalArgumentException
	 */
	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testUnknowFunction() {
		converter.unmarshal("test");
	}

	/**
	 * Tests if the marshalling works correctly. That means that the
	 * {@link URNToFunctionConverter#marshal(Function)} returns the proper
	 * {@link String}.
	 * 
	 * @throws IllegalArgumentException
	 *             In case of an improper {@link DataTypeAttribute}.
	 */
	@Test
	public void testConvertToFunctionId() throws IllegalArgumentException {
		assertEquals(converter.marshal(function), FUNCTION_ID);
	}
}