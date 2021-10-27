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
package org.herasaf.xacml.core.types;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Represents a "urn:oasis:names:tc:xacml:2.0:data-type:dayTimeDuration" (see
 * Page 111 of the XACML 2.0 specification, Errata, 29 January 2008). The
 * specification contains an error. Therefore the implementation has the
 * following except for the following discrepancy to the specification:
 * <ul>
 * <li>The calculation of the dayTimeDuration in units of seconds is:<br />
 * <code>( ( ('value of the day component' * 24)<br />+ ('value of the hour component') * 60)<br />
 * + ('value of the minute component')*60)<br />+ ('value of the second component')</code><br
 * />
 * <br />
 * </li>
 * <li>The duration must be a valid http://www.w3.org/2001/XMLSchema#duration
 * data type (See:
 * <a href="http://www.w3.org/TR/2001/REC-xmlschema-2-20010502/#duration"
 * >http://www.w3.org/TR/2001/REC-xmlschema-2-20010502/#duration</a> for further
 * information.) Therefore the data type accepts a shortened range of
 * values.</li>
 * </ul>
 * 
 * <b>Note:</b><br />
 * According to the XACML specification match algorithm a dayTimeDuration can
 * only have a one digit for a day. Because this does not make sense and the
 * XACML conformance tests are designed to allow multiple digits for a day, the
 * match pattern is adjusted.
 * 
 * @author Florian Huonder
 */
public class DayTimeDuration implements Comparable<DayTimeDuration> {
	private Duration duration;
	private String durationValue;
	private boolean negative = false;

	/**
	 * Creates a new {@link DayTimeDuration} with the given duration.
	 * 
	 * @param durationString
	 *            The duration to convert into a {@link DayTimeDuration}.
	 * @throws ConvertException
	 */
	public DayTimeDuration(String durationString) {
		try {
			// This formatter only accepts positive periods. The reason is that
			// Java Time Duration can be negative on each
			// place. Means this would be valid "P-3DT-34M"
			// The urn:oasis:names:tc:xacml:2.0:data-type:yearMonthDuration
			// allows only something like "-P3DT34M". Due to
			// this fact here only positive values are saved
			// and the negative case is tracked separately.
			durationValue =  durationString.trim();
			
			String negationParsedValue = durationValue;
			if (negationParsedValue.startsWith("-")) {
				negative = true;
				negationParsedValue = durationString.substring(1);
			}
			if (negationParsedValue.contains("-")) {
				throw new IllegalArgumentException(
						"Negation only permitted for the whole value, not at each single place.");
			}

			duration = Duration.parse(negationParsedValue);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return durationValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(DayTimeDuration o) {
		Duration compareDuration = o.getDuration();

		return duration.compareTo(compareDuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(duration, negative);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DayTimeDuration other = (DayTimeDuration) obj;
		if (!Objects.equals(duration, other.duration)) {
			return false;
		}
		if (negative != other.negative)
			return false;
		return true;
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