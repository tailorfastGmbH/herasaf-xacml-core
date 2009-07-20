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

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.impl.IpAddressDataTypeAttribute;
import org.herasaf.xacml.core.types.IPAddress;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestIPAddressDataTypeAttribute {
	private IpAddressDataTypeAttribute dataType;

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

	@BeforeTest
	public void beforeTest() throws Exception {
		dataType = new IpAddressDataTypeAttribute();
	}

	@Test(dataProvider = "positiveCases")
	public void testInput(String input) throws Exception {
		assertEquals(dataType.convertTo(input), IPAddress.newInstance(input));
	}

	@Test(dataProvider = "negativeCases", expectedExceptions = { SyntaxException.class })
	public void testWrongInput(String input) throws Exception {
		dataType.convertTo(input);
	}

	@Test
	public void testToString() throws Exception {
		assertEquals(dataType.toString(),
				"urn:oasis:names:tc:xacml:2.0:data-type:ipAddress");
	}
}