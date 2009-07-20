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

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.impl.DoubleDataTypeAttribute;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestDoubleDataTypeAttribute {
	private DoubleDataTypeAttribute dataType;

	@DataProvider(name = "positiveDataPrimitive")
	public Object[][] initPositiveDataPrimitive() {
		return new Object[][] { new Object[] { 1.0, "1.0" },
				new Object[] { -1.2, "-1.2" },
				new Object[] { -1E4, "-1E4" },
				new Object[] { 1267.43233E12, "1267.43233E12" },
				new Object[] { 12.78e-2, "12.78e-2" },
				new Object[] { new Double(12), "12" },
				new Object[] { new Double("-0"), "-0" },
				new Object[] { new Double(0), "0" }, };
	}

	@DataProvider(name = "positiveDataComplex")
	public Object[][] initPositiveDataComplex() {
		return new Object[][] {
				new Object[] { new Double(12), "12" },
				new Object[] { new Double("-0"), "-0" },
				new Object[] { new Double(0), "0" }, };
	}

	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new DoubleDataTypeAttribute();
	}

	@Test(dataProvider = "positiveDataPrimitive")
	public void testInputPrimitive(double input, String result)
			throws Exception {
		assertEquals(input, dataType.convertTo(result));
	}

	@Test(dataProvider = "positiveDataComplex")
	public void testInputInputComplex(Double input, String result)
			throws Exception {
		assertEquals(input, dataType.convertTo(result));
	}

	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(),
				"http://www.w3.org/2001/XMLSchema#double");
	}

	@Test(expectedExceptions = { SyntaxException.class })
	public void testWrongInput() throws Exception {
		dataType.convertTo("h");
	}
}