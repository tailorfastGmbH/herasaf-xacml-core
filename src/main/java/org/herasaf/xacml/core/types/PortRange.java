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
 * TODO REVIEW
 * 
 * This class represents a (network) port range. The values can be between 1 and
 * 65535.
 * 
 * <br />
 * <b>Examples:</b><br />
 * <code>
 * {@link PortRange} range1 = new {@link PortRange}("123");<br />
 * {@link PortRange} range2 = new {@link PortRange}("-4657");<br />
 * {@link PortRange} range3 = new {@link PortRange}("17383-");<br />
 * {@link PortRange} range4 = new {@link PortRange}("1204-3845");
 * </code> <br />
 * <br />
 * A -&lt;number&gt; indicates that the lower boundary of the range is the
 * minimum value of 1. A &lt;number&gt;- indicates that the upper boundary of
 * the range is the maximum value of 35535.
 * 
 * 
 * @author Florian Huonder
 * @author Stefan Oberholzer
 */
public class PortRange {
	private static final String MATCHPATTERN = "(([1-9]\\d*)??\\-??([1-9]\\d*)??)??";
	private static final int UPPER_BOUNDARY = 65535;
	private static final int LOWER_BOUNDARY = 1;
	private int lowerValue = LOWER_BOUNDARY;
	private int upperValue = UPPER_BOUNDARY;

	/**
	 * TODO REVIEW
	 * 
	 * Creates a new {@link PortRange}.
	 * 
	 * @param portRange
	 *            The {@link String} representation of the port range.
	 */
	public PortRange(String portRange) {
		if (!portRange.matches(MATCHPATTERN)) {
			throw new IllegalArgumentException("No port range: " + portRange);
		}
		int position = portRange.indexOf("-");
		if (position == -1) {
			try {
				lowerValue = upperValue = Integer.valueOf(portRange.substring(0, portRange.length()));
			} catch (Exception e) {
				throw new IllegalArgumentException("No port range: " + portRange);
			}
		} else {
			try {
				String value = portRange.substring(0, position);
				if (!value.equals("")) {
					lowerValue = new Integer(value);
				}
				value = portRange.substring(position + 1, portRange.length());
				if (!value.equals("")) {
					upperValue = new Integer(value);
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("No port range: " + portRange);
			}
		}
		if (lowerValue < 0) {
			throw new IllegalArgumentException("No port range: " + portRange);
		}
		if (upperValue - lowerValue < 0) {
			throw new IllegalArgumentException("No port range: " + portRange);
		}
		if (upperValue > UPPER_BOUNDARY) {
			throw new IllegalArgumentException("No port range: " + portRange);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		if (upperValue != lowerValue) {
			return lowerValue + "-" + upperValue;
		}
		return Integer.toString(lowerValue);
	}
}