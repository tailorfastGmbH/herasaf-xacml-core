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

package org.herasaf.xacml.core.function.impl.logicalFunctions.test;

import static org.testng.Assert.assertEquals;

import java.math.BigInteger;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.impl.logicalFunctions.NOFFunction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the n-of function works properly.
 * 
 * @author Florian Huonder
 */
public class TestNOFFunction {
	private Function ia;

	/**
	 * Creates positive test cases.
	 * 
	 * @return The positive test cases.
	 */
	@DataProvider(name="args")
	public Object[][] createArgs(){
		return new Object[][]{
			new Object[] { new BigInteger("1"), new Boolean[]{true}, true},
			new Object[] { new BigInteger("1"), new Boolean[]{false, true}, true},
			new Object[] { new BigInteger("1"), new Boolean[]{true, true}, true},
			new Object[] { new BigInteger("3"), new Boolean[]{true, true, true}, true},
			new Object[] { new BigInteger("0"), new Boolean[]{false}, true},
			new Object[] { new BigInteger("0"), new Boolean[]{}, true},
			new Object[] { new BigInteger("-1"), new Boolean[]{true}, true},
			new Object[] { new BigInteger("1"), new Boolean[]{false}, false},
			new Object[] { new BigInteger("4"), new Boolean[]{true, true, true, false, false, false}, false},
		};
	}

	/**
	 * Creates negative test cases.
	 * 
	 * @return The negative test cases.
	 */
	@DataProvider(name="processingException")
	public Object[][] createProcessingException(){
		return new Object[][]{
			new Object[] { new BigInteger("2"), new Boolean[]{true}},
			new Object[] { new BigInteger("1"), new Boolean[]{}},
		};
	}

	/**
	 * Initializes the n-of function.
	 */
	@BeforeMethod
	public void init(){
		ia = new NOFFunction();
	}

	/**
	 * Tests the positive test cases. Tests if the n-of function works properly.
	 * 
	 * @param i1 The integer argument.
	 * @param otherArgs The list of boolean values.
	 * @param result The expected result
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider="args")
	public void testArgs(BigInteger i1, Boolean[] otherArgs, Boolean result) throws Exception {
		assertEquals(ia.handle(toObjectArray(i1, otherArgs)), result);
	}
	
	/**
	 * Tests the negative test cases. If the n-of function behaves properly.
	 * All test cases should result in an {@link ProcessingException}.
	 * 
	 * @param i The integer value.
	 * @param otherArgs The list of boolean values.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider="processingException", expectedExceptions={ProcessingException.class})
	public void testProcessingException(BigInteger i, Boolean[] otherArgs) throws Exception{
		ia.handle(toObjectArray(i, otherArgs));
	}

	/**
	 * Transforms a the boolean values and the integer value into a single object array.
	 * 
	 * @param i1 The integer value.
	 * @param otherArgs The list of boolean values.
	 * @return The object array.
	 */
	public Object[] toObjectArray(BigInteger i1, Boolean[] otherArgs){
		Object[] args = new Object[otherArgs.length+1];
		args[0] = i1;
		for (int i = 0; i < otherArgs.length; i++){
			args[i+1] = otherArgs[i];
		}
		return args;
	}

	/**
	 * Tests if the n-of function returns the proper ID.
	 */
	@Test
	public void testID(){
		assertEquals(ia.toString(), "urn:oasis:names:tc:xacml:1.0:function:n-of");
	}
}