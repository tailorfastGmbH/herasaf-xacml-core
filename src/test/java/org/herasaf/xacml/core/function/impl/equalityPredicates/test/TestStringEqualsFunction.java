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

package org.herasaf.xacml.core.function.impl.equalityPredicates.test;

import static org.testng.Assert.assertEquals;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.FunctionProcessingException;
import org.herasaf.xacml.core.function.impl.equalityPredicates.StringEqualFunction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestStringEqualsFunction {
	private Function sef;

	@BeforeMethod
	public void beforeMethod(){
		sef = new StringEqualFunction();
	}

	@Test
	public void testHandleEqualArgs() throws Exception {
		String arg1 = "Test";
		String arg2 = "Test";
		assertEquals(true, sef.handle(arg1, arg2));
	}

	@Test
	public void testHandleNotEqualsArgs() throws Exception {
		String arg1 = "Test";
		String arg2 = "Test2";
		assertEquals(false, sef.handle(arg1, arg2));
	}

	@Test(expectedExceptions=FunctionProcessingException.class )
	public void testHandleInvalidArgs() throws Exception{
		String arg1 = "Test";
		String arg2 = "Test";
		String arg3 = "Test";


		assertEquals(true, sef.handle(arg1, arg2, arg3));
	}
}