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

import java.net.URI;

import org.herasaf.xacml.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.impl.AnyURIDataTypeAttribute;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestAnyURIDataTypeAttribute {

	private AnyURIDataTypeAttribute dataType;
	
	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new AnyURIDataTypeAttribute();
	}
	
	@Test
	public void testInput1() throws Exception{
		assertEquals(dataType.convertTo("www.hallo.ch"), new URI("www.hallo.ch"));
	}
	
	@Test
	public void testInput0() throws Exception{
		assertEquals(dataType.convertTo("hallo/du/.ch"), new URI("hallo/du/.ch"));
	}
	
	@Test(expectedExceptions={SyntaxException.class})
	public void testInputtrueWrongSpelled() throws Exception{
		dataType.convertTo("-\\1234");
	}
	
	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(), "http://www.w3.org/2001/XMLSchema#anyURI");
	}
}

