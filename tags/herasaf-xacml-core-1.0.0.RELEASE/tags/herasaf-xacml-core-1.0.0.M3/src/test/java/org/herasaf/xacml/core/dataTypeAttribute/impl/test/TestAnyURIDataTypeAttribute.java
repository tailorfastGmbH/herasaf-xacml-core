/*
 * Copyright 2008 - 2012 HERAS-AF (www.herasaf.org)
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

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.impl.AnyURIDataTypeAttribute;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests if the {@link AnyURIDataTypeAttribute} works properly.
 * 
 * @author Florian Huonder
 */
public class TestAnyURIDataTypeAttribute {
	private AnyURIDataTypeAttribute dataType;
	
	/**
	 * Initializes a new {@link AnyURIDataTypeAttribute}.
	 * 
	 * @throws Exception In case of an error.
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new AnyURIDataTypeAttribute();
	}
	
	/**
	 * Tests if the datatype works with an URL.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testInput1() throws Exception{
		assertEquals(dataType.convertTo("www.hallo.ch"), new URI("www.hallo.ch"));
	}
	
	/**
	 * Tests if the datatype works with an URI.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testInput0() throws Exception{
		assertEquals(dataType.convertTo("hallo/du/.ch"), new URI("hallo/du/.ch"));
	}
	
	/**
	 * Tests if the datatype does not accept illegal arguments.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test(expectedExceptions={SyntaxException.class})
	public void testInputtrueWrongSpelled() throws Exception{
		dataType.convertTo("-\\1234");
	}
	
	/**
	 * Tests if the {@link AnyURIDataTypeAttribute} returns the proper ID.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(), "http://www.w3.org/2001/XMLSchema#anyURI");
	}
}