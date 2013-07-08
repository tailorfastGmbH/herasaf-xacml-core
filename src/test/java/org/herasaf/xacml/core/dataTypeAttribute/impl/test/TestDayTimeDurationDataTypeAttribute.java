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
import org.herasaf.xacml.core.dataTypeAttribute.impl.DayTimeDurationDataTypeAttribute;
import org.herasaf.xacml.core.types.DayTimeDuration;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests if the {@link DayTimeDurationDataTypeAttribute} works properly.
 * 
 * @author Florian Huonder
 */
public class TestDayTimeDurationDataTypeAttribute {

	private DayTimeDurationDataTypeAttribute dataType;

	/**
	 * Initializes a new {@link DayTimeDurationDataTypeAttribute}.
	 * 
	 * @throws Exception In case of an error.
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new DayTimeDurationDataTypeAttribute();
	}

	/**
	 * Test a case with a day-time-duration that is valid.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testInput1() throws Exception {
		List<String> data = new ArrayList<String>();
		data.add("-P9DT4H");
		assertEquals(dataType.convertTo(data), new DayTimeDuration(
				"-P9DT4H"));
	}

	/**
	 * Test an illegal value.
	 * 
	 * @throws Exception
	 */
	@Test(expectedExceptions = { SyntaxException.class })
	public void testInputtrueWrongSpelled() throws Exception {
		List<String> data = new ArrayList<String>();
		data.add("+P9DT4H");
		dataType.convertTo(data);
	}

	/**
	 * Tests if the {@link DayTimeDurationDataTypeAttribute} returns the proper ID.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(),
				"urn:oasis:names:tc:xacml:2.0:data-type:dayTimeDuration");
	}
}
