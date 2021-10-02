/*
 * Copyright 2008 - 2013 HERAS-AF (www.herasaf.org)
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


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.impl.DoubleDataTypeAttribute;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the {@link DoubleDataTypeAttribute} works properly.
 * 
 * @author Florian Huonder
 */
public class TestDoubleDataTypeAttribute {
	private DoubleDataTypeAttribute dataType;

	/**
	 * Creates primitive positive test cases for the test.
	 * 
	 * @return The primitive positive test cases.
	 */
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

	/**
	 * Creates complex positive test cases for the test.
	 * 
	 * @return The complex primitive test cases.
	 */
	@DataProvider(name = "positiveDataComplex")
	public Object[][] initPositiveDataComplex() {
		return new Object[][] {
				new Object[] { new Double(12), "12" },
				new Object[] { new Double("-0"), "-0" },
				new Object[] { new Double(0), "0" }, };
	}

	/**
	 * Initializes a new {@link DoubleDataTypeAttribute}.
	 * 
	 * @throws Exception In case of an error.
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new DoubleDataTypeAttribute();
	}

	/**
	 * Tests the primitive positive data.
	 * 
	 * @param input The positive data.
	 * @throws Exception In case of an error.
	 */
	@Test(dataProvider = "positiveDataPrimitive")
	public void testInputPrimitive(double input, String result)
			throws Exception {
		List<String> data = new ArrayList<String>();
		data.add(Double.toString(input));
		assertEquals(input, dataType.convertTo(data), 0.0d);
	}

	/**
	 * Tests the complex positive data.
	 * 
	 * @param input The positive data.
	 * @throws Exception In case of an error.
	 */
	@Test(dataProvider = "positiveDataComplex")
	public void testInputInputComplex(Double input, String result)
			throws Exception {
		List<String> data = new ArrayList<String>();
		data.add(input.toString());
		assertEquals(input, dataType.convertTo(data));
	}

	/**
	 * Tests if the {@link DoubleDataTypeAttribute} returns the proper ID.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(),
				"http://www.w3.org/2001/XMLSchema#double");
	}

	/**
	 * Tests a negative case. Tests if the expected {@link SyntaxException} is thrown.
	 * @throws Exception In case of an error
	 */
	@Test(expectedExceptions = { SyntaxException.class })
	public void testWrongInput() throws Exception {
		List<String> data = new ArrayList<String>();
		data.add("h");
		dataType.convertTo(data);
	}
}