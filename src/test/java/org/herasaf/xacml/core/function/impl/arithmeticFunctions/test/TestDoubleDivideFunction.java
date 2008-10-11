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

package org.herasaf.xacml.core.function.impl.arithmeticFunctions.test;

import static org.testng.Assert.assertEquals;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.FunctionProcessingException;
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.DoubleDivideFunction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestDoubleDivideFunction {
	private Function ia;

	@DataProvider(name="data2Args")
	public Object[][] createData2Args(){
		return new Object[][]{
			new Object[] { new Double("1.0"), new Double("1.0"), new Double("1.0")},
			new Object[] { new Double("99.0"), new Double("3.0"), new Double("33.0")},
		};
	}

	@DataProvider(name="dataBy0")
	public Object[][] createDataBy0(){
		return new Object[][]{
			new Object[] { new Double("1.0")},
			new Object[] { new Double("99.0")},
		};
	}
	@BeforeMethod
	public void init(){
		ia = new DoubleDivideFunction();
	}

	@Test(dataProvider="data2Args")
	public void testDivide2Args(Double i1, Double i2, Double result) throws Exception {
		assertEquals(ia.handle(i1, i2), result);
	}

	@Test(dataProvider="dataBy0", expectedExceptions={FunctionProcessingException.class})
	public void testDivideBy0Args(Double i1) throws Exception {
		ia.handle(i1, new Double("0.0"));
	}

	@Test
	public void testID(){
		assertEquals(ia.toString(), "urn:oasis:names:tc:xacml:1.0:function:double-divide");
	}
}