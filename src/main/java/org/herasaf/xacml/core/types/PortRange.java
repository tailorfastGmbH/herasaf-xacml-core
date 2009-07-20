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

package org.herasaf.xacml.core.types;

/**
 * This class represents a port range.
 *
 * @author Florian Huonder 
 * @author Stefan Oberholzer 
 * @version 1.0
 */
public class PortRange {
	private static final String MATCHPATTERN = "(([1-9]\\d*)??\\-??([1-9]\\d*)??)??";
	private int lowerValue = 1;
	private int upperValue = 65535;

	/**
	 * Initializes the object. The range will be checked if it is a port range.
	 *
	 * @param portRange The port range.
	 */
	public PortRange(String portRange){
		if(!portRange.matches(MATCHPATTERN)) {
			throw new IllegalArgumentException("No port range: " + portRange);
		}
		int position = portRange.indexOf("-");
		if(position == -1){
			try {
				lowerValue = upperValue = new Integer(portRange.substring(0, portRange.length()));
			}
			catch (Exception e){
				throw new IllegalArgumentException("No port range: " + portRange);
			}
		}
		else {
			try {
				String value = portRange.substring(0, position);
				if(!value.equals("")){
					lowerValue = new Integer(value);
				}
				value = portRange.substring(position + 1, portRange.length());
				if(!value.equals("")){
					upperValue = new Integer(value);
				}
			}
			catch (Exception e){
				throw new IllegalArgumentException("No port range: " + portRange);
			}
		}
		if(lowerValue < 0){
			throw new IllegalArgumentException("No port range: " + portRange);
		}
		if(upperValue - lowerValue < 0){
			throw new IllegalArgumentException("No port range: " + portRange);
		}
		if(upperValue > 65535){
			throw new IllegalArgumentException("No port range: " + portRange);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if(upperValue != lowerValue){
			return lowerValue + "-" + upperValue;
		}
		return Integer.toString(lowerValue);
	}
}