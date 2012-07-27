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

import javax.security.auth.x500.X500Principal;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.impl.TimeDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.X500DataTypeAttribute;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests if the {@link X500DataTypeAttribute} works properly.
 * 
 * @author Florian Huonder
 */
public class TestX500NameDataTypeAttribute {
	private X500DataTypeAttribute dataType;

	/**
	 * Initializes a new {@link X500DataTypeAttribute}.
	 * 
	 * @throws Exception In case of an error.
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new X500DataTypeAttribute();
	}

	/**
	 * Tests the positive data.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testInput1() throws Exception {
		assertEquals(dataType
				.convertTo("OU=Steve Kille,O=Isolde Limited, C=GB"),
				new X500Principal("OU=Steve Kille,O=Isolde Limited, C=GB"));
	}

	/**
	 * Tests the negative data.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test(expectedExceptions = { SyntaxException.class })
	public void testInputtrueWrongSpelled() throws Exception {
		dataType.convertTo("OU=Steve; Kille,O=Isolde Limited, C=GB");
	}

	/**
	 * Tests if the {@link TimeDataTypeAttribute} returns the proper ID.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(),
				"urn:oasis:names:tc:xacml:1.0:data-type:x500Name");
	}
}
