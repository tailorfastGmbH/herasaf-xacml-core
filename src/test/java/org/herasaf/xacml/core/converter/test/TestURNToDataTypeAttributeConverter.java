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
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests the {@link URNToDataTypeConverter} JAXB converter.
 * 
 * @author Sacha Dolski
 */
public class TestURNToDataTypeAttributeConverter {
	static final String DATATYPE_ID = "http://www.w3.org/2001/XMLSchema#string";
	private URNToDataTypeConverter converter;
	private DataTypeAttribute<String> dataTypeAttr;
	private Map<String, DataTypeAttribute<?>> map;

	/**
	 * Initializes {@link URNToDataTypeConverter} with a
	 * {@link StringDataTypeAttribute}.
	 */
	@BeforeTest
	public void beforeTest() {
		converter = new URNToDataTypeConverter();
		dataTypeAttr = new StringDataTypeAttribute();
		map = new HashMap<String, DataTypeAttribute<?>>();
		map.put(DATATYPE_ID, dataTypeAttr);

		URNToDataTypeConverter.setDataTypeAttributes(map);
	}

	/**
	 * Tests if the unmarshalling works correctly. That means that the
	 * {@link URNToDataTypeConverter#unmarshal(String)} returns the proper
	 * object.
	 * 
	 * @throws IllegalArgumentException
	 *             In case of an improper argument.
	 */
	@Test
	public void testConvertToStringEqualsFunction()
			throws IllegalArgumentException {

		assertEquals(converter.unmarshal(DATATYPE_ID), dataTypeAttr);
	}

	/**
	 * Tests if the marshalling works correctly. That means that the
	 * {@link URNToDataTypeConverter#marshal(DataTypeAttribute)} returns the
	 * proper {@link String}.
	 * 
	 * @throws IllegalArgumentException
	 *             In case of an improper {@link DataTypeAttribute}.
	 */
	@Test
	public void testConvertToFunctionId() throws IllegalArgumentException {

		assertEquals(converter.marshal(dataTypeAttr), DATATYPE_ID);
	}

	/**
	 * Expects an {@link IllegalArgumentException} because an improper argument
	 * is given to the {@link URNToDataTypeConverter#unmarshal(String)} method.
	 * 
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	@Test(enabled = true, expectedExceptions = { IllegalArgumentException.class })
	public void testConvertException() throws IllegalArgumentException {
		dataTypeAttr = (DataTypeAttribute<String>) converter.unmarshal("test");
	}
}