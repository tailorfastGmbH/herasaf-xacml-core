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

package org.herasaf.xacml.core.function.impl.numericDataTypeConversionFunctions;

import static org.testng.Assert.assertEquals;

import java.math.BigInteger;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.impl.numericDataTypeConversionFunctions.DoubleToIntegerFunction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the DoubleToInteger function works properly.
 * 
 * @author Florian Huonder
 */
public class TestDoubleToIntegerFunction {
	private Function ia;

	/**
	 * Creates various test cases.
	 * @return The created test cases.
	 */
	@DataProvider(name="args")
	public Object[][] createData2Args(){
		return new Object[][]{
			new Object[] { "1.0", "1"},
			new Object[] { "11.0", "11"},
			new Object[] { "0.0", "0"},
			new Object[] { "-12.0", "-12"},
		};
	}

	/**
	 * Initializes the function.
	 */
	@BeforeMethod
	public void init(){
		ia = new DoubleToIntegerFunction();
	}

	/**
	 * Tests all test cases.
	 * 
	 * @param i1 The double value.
	 * @param result The expected integer value.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider="args")
	public void testArgs(String i1, String result) throws Exception {
		assertEquals(((BigInteger)ia.handle(new Double(i1))).toString(), result);
	}

	/**
	 * Tests if the DoubleToInteger function returns the proper ID.
	 */
	@Test
	public void testID(){
		assertEquals(ia.toString(), "urn:oasis:names:tc:xacml:1.0:function:double-to-integer");
	}
}