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

import org.herasaf.xacml.core.SyntaxException;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * Represents a "urn:oasis:names:tc:xacml:2.0:data-type:yearMonthDuration".<br>
 * See: <a href= "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20"> OASIS eXtensible Access
 * Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 111, for further information.
 * 
 * @author Florian Huonder
 */
public class YearMonthDuration implements Comparable<YearMonthDuration> {
	private Period duration;
	private boolean negative = false;
	private static final PeriodFormatter PERIOD_FORMATTER;

	static {
		// This formatter only accepts positive periods. The reason is that Joda Time Period can be negative on each
		// place. Means this would be valid "P-123Y-34M"
		// The urn:oasis:names:tc:xacml:2.0:data-type:yearMonthDuration allows only something like "-P123Y34M". Due to
		// this fact here only positive values are saved
		// and the negative case is tracked separately.
		PERIOD_FORMATTER = new PeriodFormatterBuilder().rejectSignedValues(true).appendLiteral("P").appendYears()
				.appendSuffix("Y").appendMonths().appendSuffix("M").toFormatter();
	}

	/**
	 * Creates a new {@link YearMonthDuration} with the given duration.
	 * 
	 * @param durationString
	 *            The duration to convert into a {@link YearMonthDuration}.
	 * @throws SyntaxException
	 */
	public YearMonthDuration(String durationString) throws SyntaxException {
		durationString = durationString.trim();
		if (durationString.startsWith("-")) {
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
	public int compareTo(YearMonthDuration o) {
		DateTime startInstant = new DateTime(0L);
		Duration thisDuration = duration.toDurationFrom(startInstant);
		Duration compareDuration = o.getDuration().toDurationFrom(startInstant);

		return thisDuration.compareTo(compareDuration);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + (negative ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YearMonthDuration other = (YearMonthDuration) obj;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (negative != other.negative)
			return false;
		return true;
	}

	public Period getDuration() {
		return duration;
	}
}