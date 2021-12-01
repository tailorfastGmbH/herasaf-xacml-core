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

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Represents a "urn:oasis:names:tc:xacml:2.0:data-type:yearMonthDuration".<br>
 * See: <a href= "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20"> OASIS eXtensible Access
 * Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 111, for further information.
 * 
 * @author Florian Huonder
 */
public class YearMonthDuration implements Comparable<YearMonthDuration> {
	private Period period;
	private boolean negative = false;


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
		try {
			this.period = Period.parse(durationString);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return (negative) ? "-" + period : period.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    public int compareTo(YearMonthDuration o) {
		return LocalDate.now().plus(getDuration()).compareTo(LocalDate.now().plus(o.getDuration()));
	}

	@Override
	public int hashCode() {
		return Objects.hash(period, negative);
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
		if (!Objects.equals(period, other.period)) {
			return false;
		}
		if (negative != other.negative)
			return false;
		return true;
	}

	public Period getDuration() {
		return period;
	}

	public boolean isNegative() {
		return negative;
	}
}