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

package org.herasaf.xacml.core.types.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.herasaf.xacml.core.types.Base64Binary;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestBase64Binary {
	@DataProvider (name="data")
	public Object[][] createData(){
		return new Object[][]{
				new Object[] {"My test string."},
				new Object[] {""},
				new Object[] {"1234567890"},
				new Object[] {"#sfdlkj@"},
				new Object[] {"This is a sentence without a punctuation mark"},
		};
	}

	@Test (dataProvider="data")
	public void testBase64(String input) throws Exception {
		String base64Data = Base64Encoder.encodeString(input);
		Base64Binary b64b = new Base64Binary(base64Data);
		assertEquals(b64b.getValue(), input.getBytes());
	}

	@Test
	public void testHashCode() throws Exception {
		String base64Data = Base64Encoder.encodeString("hallo");
		String base64Data2 = Base64Encoder.encodeString("du");

		Base64Binary b64b = new Base64Binary(base64Data);
		Base64Binary b64b2 = new Base64Binary(base64Data2);
		Set<Base64Binary> set = new HashSet<Base64Binary>();
		set.add(b64b);
		set.add(b64b2);
		Base64Binary toFind = new Base64Binary(Base64Encoder.encodeString("hallo"));
		assertTrue(set.contains(toFind));
		toFind = new Base64Binary(Base64Encoder.encodeString("du"));
		assertTrue(set.contains(toFind));
	}
}