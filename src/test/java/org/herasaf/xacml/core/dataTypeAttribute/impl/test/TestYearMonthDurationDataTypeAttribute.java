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

package org.herasaf.xacml.core.dataTypeAttribute.impl.test;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.impl.YearMonthDurationDataTypeAttribute;
import org.herasaf.xacml.core.types.YearMonthDuration;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests if the {@link YearMonthDurationDataTypeAttribute} works properly.
 * 
 * @author Florian Huonder
 */
public class TestYearMonthDurationDataTypeAttribute {
	private YearMonthDurationDataTypeAttribute dataType;

	/**
	 * Initializes a new {@link YearMonthDurationDataTypeAttribute}.
	 * 
	 * @throws Exception In case of an error.
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new YearMonthDurationDataTypeAttribute();
	}

	/**
	 * Tests the positive data.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testInput1() throws Exception {
		List<String> data = new ArrayList<String>();
		data.add("-P9Y3M");
		assertEquals(dataType.convertTo(data), new YearMonthDuration(
				"-P9Y3M"));
	}

	/**
	 * Tests the negative data.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test(expectedExceptions = { SyntaxException.class })
	public void testInputtrueWrongSpelled() throws Exception {
		List<String> data = new ArrayList<String>();
		data.add("+P9Y3M");
		dataType.convertTo(data);
	}

	/**
	 * Tests if the {@link YearMonthDurationDataTypeAttribute} returns the proper ID.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(),
				"urn:oasis:names:tc:xacml:2.0:data-type:yearMonthDuration");
	}
}
