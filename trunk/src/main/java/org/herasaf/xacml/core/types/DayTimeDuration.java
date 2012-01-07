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
package org.herasaf.xacml.core.types;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * Represents a "urn:oasis:names:tc:xacml:2.0:data-type:dayTimeDuration" (see Page 111 of the XACML 2.0 specification,
 * Errata, 29 January 2008). The specification contains an error. Therefore the implementation has the following except
 * for the following discrepancy to the specification:
 * <ul>
 * <li>The calculation of the dayTimeDuration in units of seconds is:<br />
 * <code>( ( ('value of the day component' * 24)<br />+ ('value of the hour component') * 60)<br />
 * + ('value of the minute component')*60)<br />+ ('value of the second component')</code><br />
 * <br />
 * </li>
 * <li>The duration must be a valid http://www.w3.org/2001/XMLSchema#duration data type (See: <a
 * href="http://www.w3.org/TR/2001/REC-xmlschema-2-20010502/#duration"
 * >http://www.w3.org/TR/2001/REC-xmlschema-2-20010502/#duration</a> for further information.) Therefore the data type
 * accepts a shortened range of values.</li>
 * </ul>
 * 
 * <b>Note:</b><br /> According to the XACML specification match algorithm a dayTimeDuration can only have a one digit for
 * a day. Because this does not make sense and the XACML conformance tests are designed to allow multiple digits for
 * a day, the match pattern is adjusted.
 * 
 * @author Florian Huonder
 */
public class DayTimeDuration implements Comparable<DayTimeDuration> {
	private Period duration;
	private boolean negative = false;
	private static final PeriodFormatter PERIOD_FORMATTER;
	
	static {
		// This formatter only accepts positive periods. The reason is that Joda Time Period can be negative on each
		// place. Means this would be valid "P-3DT-34M"
		// The urn:oasis:names:tc:xacml:2.0:data-type:yearMonthDuration allows only something like "-P3DT34M". Due to
		// this fact here only positive values are saved
		// and the negative case is tracked separately.
		PERIOD_FORMATTER = new PeriodFormatterBuilder().rejectSignedValues(true).appendLiteral("P").appendDays()
		.appendSuffix("D").appendSeparatorIfFieldsAfter("T").appendHours().appendSuffix("H").appendMinutes()
		.appendSuffix("M").appendSecondsWithOptionalMillis().appendSuffix("S").toFormatter();
	}
	
	/**
	 * Creates a new {@link DayTimeDuration} with the given duration.
	 * 
	 * @param durationString
	 *            The duration to convert into a {@link DayTimeDuration}.
	 * @throws ConvertException
	 */
	public DayTimeDuration(String durationString) {
		durationString = durationString.trim();
		if(durationString.startsWith("-")){
			negative = true;
			durationString = durationString.substring(1);
		}
		
		this.duration = PERIOD_FORMATTER.parsePeriod(durationString);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return (negative) ? "-" + PERIOD_FORMATTER.print(duration) : PERIOD_FORMATTER.print(duration);
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(DayTimeDuration o) {
		DateTime startInstant = new DateTime(0L);
		Duration thisDuration = duration.toDurationFrom(startInstant);
		Duration compareDuration = o.getDuration().toDurationFrom(startInstant);
		
		return thisDuration.compareTo(compareDuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !obj.getClass().isAssignableFrom(DayTimeDuration.class)){
			//Check if types are the same
			return false;
		}
		if(this.compareTo((DayTimeDuration) obj) == 0){
			//If types are the same check if they are equal
			return true;
		}
		//If they are not equal return false
		return false;
	}

	/**
	 * Returns the {@link Duration} of this {@link DayTimeDuration}.
	 * 
	 * @return The {@link Duration}.
	 */
	protected Period getDuration() {
		return duration;
	}
}