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

package org.herasaf.xacml.core.dataTypeAttribute.impl.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.impl.BooleanDataTypeAttribute;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestBooleanDataTypeAttribute {
	private BooleanDataTypeAttribute dataType;

	@DataProvider(name = "positiveDataTrue")
	public Object[][] createPositiveDataTrue() {
		return new Object[][] { new Object[] { "1" },
				new Object[] { "true" } };
	}

	@DataProvider(name = "positiveDataFalse")
	public Object[][] createPositiveDataFalse() {
		return new Object[][] { new Object[] { "0" },
				new Object[] { "false" } };
	}

	@DataProvider(name = "negativeData")
	public Object[][] createNegativeData() {
		return new Object[][] { new Object[] { "True" },
				new Object[] { "hallo" } };
	}

	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new BooleanDataTypeAttribute();
	}

	@Test(dataProvider = "positiveDataTrue")
	public void testInputTrue(String input) throws Exception {
		assertTrue(dataType.convertTo(input));
	}

	@Test(dataProvider = "positiveDataFalse")
	public void testInputFalse(String input) throws Exception {
		assertFalse(dataType.convertTo(input));
	}

	@Test(dataProvider = "negativeData", expectedExceptions = { SyntaxException.class })
	public void testInputtrueWrongSpelled(String input) throws Exception {
		dataType.convertTo(input);
	}

	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(),
				"http://www.w3.org/2001/XMLSchema#boolean");
	}
}