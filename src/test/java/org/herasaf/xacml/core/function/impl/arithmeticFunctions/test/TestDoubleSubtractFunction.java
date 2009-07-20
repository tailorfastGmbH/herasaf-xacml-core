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
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.DoubleSubtractFunction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestDoubleSubtractFunction {
	private Function ia;

	@DataProvider(name="data2Args")
	public Object[][] createData2Args(){
		return new Object[][]{
				new Object[] { new Double("1.0"), new Double("1.0"), new Double("0.0")},
				new Object[] { new Double("0.5"), new Double("0.5"), new Double("0.0")},
				new Object[] { new Double("-1.0"), new Double("-3.6"), new Double("2.6")},
				new Object[] { new Double("-1.2"), new Double("3.9"), new Double("-5.1")},
				new Object[] { new Double("1.5"), new Double("4.5"), new Double("-3")},
				new Object[] { new Double("0.01"), new Double("1.95"), new Double("-1.94")},
		};
	}

	@BeforeMethod
	public void init(){
		ia = new DoubleSubtractFunction();
	}

	@Test(dataProvider="data2Args")
	public void testSubtract2Args(Double i1, Double i2, Double result) throws Exception {
		assertEquals(ia.handle(i1, i2), result);
	}

	@Test
	public void testID(){
		assertEquals(ia.toString(), "urn:oasis:names:tc:xacml:1.0:function:double-subtract");
	}
}