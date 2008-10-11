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

import org.herasaf.xacml.core.types.IPv6Address;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestIPv6Address {
	@DataProvider (name = "positiveCases")
	public Object[][] createPositiveCases(){
		return new Object[][] {
				new Object[]{"[1080:0:0:0:8:800:200C:417A]", "[1080:0:0:0:8:800:200C:417A]"},
				new Object[]{"[FF01:0:0:0:0:0:0:101]/[::FFFF]", "[FF01:0:0:0:0:0:0:101]/[0:0:0:0:0:0:0:FFFF]"},
				new Object[]{"[0:0:0:0:0:0:0:1]/[234::AAAA]:12", "[0:0:0:0:0:0:0:1]/[234:0:0:0:0:0:0:AAAA]:12"},
				new Object[]{"[0:0:0:0:0:0:0:0]", "[0:0:0:0:0:0:0:0]"},
				new Object[]{"[1080::8:800:200C:417A]:-12", "[1080:0:0:0:8:800:200C:417A]:1-12"},
				new Object[]{"[FF01::101]:12-", "[FF01:0:0:0:0:0:0:101]:12-65535"},
				new Object[]{"[::1]", "[0:0:0:0:0:0:0:1]"},
				new Object[]{"[::]", "[0:0:0:0:0:0:0:0]"},
				new Object[]{"[0:0:0:0:0:0:13.1.68.3]", "[0:0:0:0:0:0:d01:4403]"},
				new Object[]{"[0:0:0:0:0:FFFF:129.144.52.38]", "[0:0:0:0:0:FFFF:129.144.52.38]"},
				new Object[]{"[::13.1.68.12]", "[0:0:0:0:0:0:d01:440c]"},
				new Object[]{"[::FFFF:129.144.52.38]", "[0:0:0:0:0:FFFF:129.144.52.38]"},
				new Object[]{"[12AB:0000:0000:CD30:0000:0000:0000:0000]", "[12AB:0:0:CD30:0:0:0:0]"},
				new Object[]{"[12AB::CD30:0:0:0:0]", "[12AB:0:0:CD30:0:0:0:0]"},
				new Object[]{"[12AB:0:0:CD30::]", "[12AB:0:0:CD30:0:0:0:0]"},
				new Object[]{"[12AB:0:0:CD30:123:4567:89AB:CDEF]", "[12AB:0:0:CD30:123:4567:89AB:CDEF]"},
				new Object[]{"[12AB:0:0:CD30::]", "[12AB:0:0:CD30:0:0:0:0]"},
		};
	}

	@DataProvider (name = "negativeCases")
	public Object[][] createNegativeCases(){
		return new Object[][] {
				new Object[]{ "[12AB:0:0:CD3/60]"},
				new Object[]{ "[12AB::CD30/60]"},
				new Object[]{ "[12AB::CD3/60]"},
		};
	}
	
	@Test(dataProvider="positiveCases", enabled = true)
	public void testAllowed(String input, String expected) throws Exception {
		assertEquals(new IPv6Address(input).toString(), expected.toLowerCase());
	}

	@Test(dataProvider="negativeCases", expectedExceptions={IllegalArgumentException.class}, enabled = true)
	public void testNotAllowed(String input) throws Exception {		
		new IPv6Address(input);
	}
}