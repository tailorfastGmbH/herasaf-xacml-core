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

import java.math.BigInteger;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.IntegerSubtractFunction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestIntegerSubtractFunction {
	private Function ia;

	@DataProvider(name="data2Args")
	public Object[][] createData2Args(){
		return new Object[][]{
			new Object[] { new BigInteger("1"), new BigInteger("1"), "0"},
			new Object[] { new BigInteger("6777"), new BigInteger("3"), "6774"},
			new Object[] { new BigInteger("-10"), new BigInteger("-12"), "2"},
			new Object[] { new BigInteger("-1"), new BigInteger("3"), "-4"},
		};
	}

	@BeforeMethod
	public void init(){
		ia = new IntegerSubtractFunction();
	}

	@Test(dataProvider="data2Args")
	public void testAdd2Args(BigInteger i1, BigInteger i2, String result) throws Exception {
		assertEquals(ia.handle(i1, i2).toString(), result);
	}

	@Test
	public void testID(){
		assertEquals(ia.toString(), "urn:oasis:names:tc:xacml:1.0:function:integer-subtract");
	}
}