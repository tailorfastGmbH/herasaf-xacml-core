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

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

/**
 * Represents a "urn:oasis:names:tc:xacml:2.0:data-type:dayTimeDuration" (see
 * Page 105 of the XACML 2.0 specification). The specification contains an
 * error. Therefore the implementation has the following except for the
 * following discrepancy to the specification:
 * <ul>
 * <li>The calculation of the dayTimeDuration in units of seconds is:<br />
 * <code>( ( ('value of the day component' * 24)<br />+ ('value of the hour component') * 60)<br />
 * + ('value of the minute component')*60)<br />+ ('value of the second component')</code><br />
 * <br />
 * </li>
 * <li>The duration must be a valid http://www.w3.org/2001/XMLSchema#duration
 * data type (See: <a
 * href="http://www.w3.org/TR/2001/REC-xmlschema-2-20010502/#duration"
 * >http://www.w3.org/TR/2001/REC-xmlschema-2-20010502/#duration</a> for further
 * information.) Therefore the data type accepts a shortened range of values.</li>
 * </ul>
 * 
 * @author Stefan Oberholzer
 */
public class DayTimeDuration implements Comparable<DayTimeDuration> {
	// According to the XACML specification match algorithm a dayTimeDuration
	// can only have a one digit for a day. Because this does not make sense and
	// the XACML conformance tests are designed to allow multiple digits for a
	// day,
	// we adjusted the match pattern.
	private static final String PATTERNSTRING = "(\\-)?P(\\d+D)?(T(\\d+H)?(\\d+M)?(\\d+(\\.\\d+)?S)?)?";
	private Duration duration;

	/**
	 * Creates a new {@link DayTimeDuration} with the given duration.
	 * 
	 * @param duration
	 *            The duration to convert into a {@link DayTimeDuration}.
	 * @throws ConvertException
	 */
	public DayTimeDuration(String duration) {

		if (!duration.matches(PATTERNSTRING)) {
			throw new IllegalArgumentException("The format of the argument: \"" + duration + "\" isn't correct");
		}
		try {
			DatatypeFactory factory = DatatypeFactory.newInstance();
			this.duration = factory.newDuration(duration);
		} catch (DatatypeConfigurationException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return duration.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(DayTimeDuration o) {
		return duration.compare(o.duration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DayTimeDuration) {
			DayTimeDuration object = (DayTimeDuration) obj;
			return this.duration.equals(object.duration);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return duration.hashCode();
	}

	/**
	 * Returns the {@link Duration} of this {@link DayTimeDuration}.
	 * 
	 * @return The {@link Duration}.
	 */
	protected Duration getDuration() {
		return duration;
	}
}