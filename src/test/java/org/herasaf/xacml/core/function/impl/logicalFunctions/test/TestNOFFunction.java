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

public class TestNOFFunction {
	private Function ia;

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

	@DataProvider(name="processingException")
	public Object[][] createProcessingException(){
		return new Object[][]{
			new Object[] { new BigInteger("2"), new Boolean[]{true}},
			new Object[] { new BigInteger("1"), new Boolean[]{}},
		};
	}



	@BeforeMethod
	public void init(){
		ia = new NOFFunction();
	}

	@Test(dataProvider="args")
	public void testArgs(BigInteger i1, Boolean[] otherArgs, Boolean result) throws Exception {
		assertEquals(ia.handle(toObjectArray(i1, otherArgs)), result);
	}

	@Test(dataProvider="processingException", expectedExceptions={ProcessingException.class})
	public void testProcessingException(BigInteger i, Boolean[] otherArgs) throws Exception{
		ia.handle(toObjectArray(i, otherArgs));
	}

	public Object[] toObjectArray(BigInteger i1, Boolean[] otherArgs){
		Object[] args = new Object[otherArgs.length+1];
		args[0] = i1;
		for (int i = 0; i < otherArgs.length; i++){
			args[i+1] = otherArgs[i];
		}
		return args;
	}

	@Test
	public void testID(){
		assertEquals(ia.toString(), "urn:oasis:names:tc:xacml:1.0:function:n-of");
	}
}