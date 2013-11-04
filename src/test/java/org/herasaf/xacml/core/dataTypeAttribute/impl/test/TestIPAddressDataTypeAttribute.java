/*
 * Copyright 2008 - 2013 HERAS-AF (www.herasaf.org)
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

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.impl.HexBinaryDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.IpAddressDataTypeAttribute;
import org.herasaf.xacml.core.types.IPAddress;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the {@link HexBinaryDataTypeAttribute} works properly.
 * 
 * @author Florian Huonder
 */
public class TestIPAddressDataTypeAttribute {
	private IpAddressDataTypeAttribute dataType;

	/**
	 * Creates positive test cases for the test.
	 * 
	 * @return The positive test cases.
	 */
	@DataProvider(name = "positiveCases")
	public Object[][] createPositiveCases() {
		return new Object[][] {
				new Object[] { "192.168.0.1" },
				new Object[] { "192.168.0.1/255.255.255.0" },
				new Object[] { "192.168.0.1/255.255.255.0:1" },
				new Object[] { "192.168.0.1:1-65535" },
				new Object[] { "192.168.0.1:65535" },
				new Object[] { "192.168.0.1:-65535" },
				new Object[] { "192.168.0.1:-3" },
				new Object[] { "[1080:0:0:0:8:800:200C:417A]" },
				new Object[] { "[FF01:0:0:0:0:0:0:101]/[::FFFF]" },
				new Object[] { "[0:0:0:0:0:0:0:1]/[234::AAAA]:12" },
				new Object[] { "[0:0:0:0:0:0:0:0]" },
				new Object[] { "[1080::8:800:200C:417A]:-12" },
				new Object[] { "[FF01::101]:12-" },
				new Object[] { "[::1]" },
				new Object[] { "[::]" },
				new Object[] { "[0:0:0:0:0:0:13.1.68.3]" },
				new Object[] { "[0:0:0:0:0:FFFF:129.144.52.38]" },
				new Object[] { "[::13.1.68.12]" },
				new Object[] { "[::FFFF:129.144.52.38]" },
				new Object[] { 
						"[12AB:0000:0000:CD30:0000:0000:0000:0000]" },
				new Object[] { "[12AB::CD30:0:0:0:0]" },
				new Object[] { "[12AB:0:0:CD30::]" },
				new Object[] { "[12AB:0:0:CD30:123:4567:89AB:CDEF]" },
				new Object[] { "[12AB:0:0:CD30::]" }, };
	}

	/**
	 * Creates negative test cases for the test.
	 * 
	 * @return The negative test cases.
	 */
	@DataProvider(name = "negativeCases")
	public Object[][] createNegativeCases() {
		return new Object[][] { new Object[] { "256.168.0.1" },
				new Object[] { "192.168.0.1a:34" },
				new Object[] { "192.168.0.1:12/255.255.255.0" },
				new Object[] { "192.168.0.1/255.256.0.0" },
				new Object[] { "193.168.0.1:0" },
				new Object[] { "193.168.0.1:65536" },
				new Object[] { "[12AB:0:0:CD3/60]" },
				new Object[] { "[12AB::CD30/60]" },
				new Object[] { "[12AB::CD3/60]" }, };
	}

	/**
	 * Initializes a new {@link IpAddressDataTypeAttribute}.
	 * 
	 * @throws Exception In case of an error.
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new IpAddressDataTypeAttribute();
	}

	/**
	 * Tests the positive data.
	 * 
	 * @param input The positive data.
	 * @throws Exception In case of an error.
	 */
	@Test(dataProvider = "positiveCases")
	public void testInput(String input) throws Exception {
		List<String> data = new ArrayList<String>();
		data.add(input);
		assertEquals(dataType.convertTo(data), IPAddress.newInstance(input));
	}

	/**
	 * Tests the negative data.
	 * 
	 * @param input The negative data.
	 * @throws Exception In case of an error.
	 */
	@Test(dataProvider = "negativeCases", expectedExceptions = { SyntaxException.class })
	public void testWrongInput(String input) throws Exception {
		List<String> data = new ArrayList<String>();
		data.add(input);
		dataType.convertTo(data);
	}

	/**
	 * Tests if the {@link IpAddressDataTypeAttribute} returns the proper ID.
	 * 
	 * @throws Exception In case of an error.
	 */
	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(),
				"urn:oasis:names:tc:xacml:2.0:data-type:ipAddress");
	}
}