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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * This class can parse and print a date of type http://www.w3.org/2001/XMLSchema#date with the pattern
 * 
 * yyyy '-' MM '-' dd ZZ?"
 * 
 * The timezone part is optional on creation. The {@link #toString()} method will always print the timezone.
 * 
 * <b>Note</b> Having set a standard timezone means that for all calculations the offset is set to the offset to UTC.
 * This means if the timezone is e.g. GMT+1 (for Zurich) the date is printed as yyyy-mm-dd-01:00. This is the timezone
 * calculated back to UTC. So a comparison is possible.
 * 
 * @author Florian Huonder
 */
public class Date implements Comparable<Date> {
	private static final Logger logger = LoggerFactory.getLogger(Date.class);
	private LocalDate date;
	private ZoneOffset offset;
	private static boolean useZuluRepresentation = false;
	private static ZoneId defaultZoneId = ZoneOffset.UTC;

	/**
	 * Is used to set whether the UTC timezone shall be represented in Zulu ('Z') or standard (+00:00).
	 */
	public static void useZuluUtcRepresentation(boolean useZuluUtcRepresentation) {
		Date.useZuluRepresentation = useZuluUtcRepresentation;
	}
	
	/**
	 * @param useZuluUtcRepresentation - whether the UTC timezone shall be represented in Zulu ('Z') or standard (+00:00)
	 * @param defaultZoneId -  ZoneId used to handle local dates.
	 */
	public static void configureWith(boolean useZuluUtcRepresentation, ZoneId defaultZoneId) {
		useZuluUtcRepresentation(useZuluUtcRepresentation);
		Date.defaultZoneId = defaultZoneId;
	}

	public Date(String dateString) throws SyntaxException {
		String trimmedDate = dateString.trim();
		try {
			// Java's Time API does not support date's having a zone offset, so manually handle this case,
			// by splitting date's having a offset.
			if (trimmedDate.length() >=11) {
				date = LocalDate.parse(trimmedDate.substring(0, 10));
				offset = ZoneOffset.of(trimmedDate
						.subSequence(10, trimmedDate.length()).toString());
			} else {
				date = LocalDate.parse(trimmedDate);
				offset = date.atStartOfDay().atZone(defaultZoneId).getOffset();
			}
		} catch (DateTimeException e) {
			String message = String.format(
					"The date '%s' is not a valid date according to http://www.w3.org/2001/XMLSchema#date", dateString);
			logger.error(message);
			throw new SyntaxException(message, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String formatOffset = useZuluRepresentation
				? offset.toString()
				: offset.toString().replace("Z", "+00:00");
		return date.toString() + formatOffset;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Date o) {
		LocalDate thisDate = this.getDate();
		LocalDate thatDate = o.getDate();
		return thisDate.compareTo(thatDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !Date.class.isAssignableFrom(obj.getClass())) {
			// Check if types are the same
			return false;
		}
		LocalDate jodaThisDate = getDate();
		LocalDate jodaThatDate = ((Date) obj).getDate();
		boolean isEqual = jodaThisDate.isEqual(jodaThatDate);
		return isEqual;
	}

	/**
	 * Returns the internal representation of this date.
	 * 
	 * @return internal representation of this date.
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * TODO Javadoc
	 * 
	 * @param yearMonthDuration
	 */
	public void add(YearMonthDuration yearMonthDuration) {
		date = date.plus(yearMonthDuration.getDuration());
	}

	/**
	 * TODO Javadoc
	 * 
	 * @param yearMonthDuration
	 */
	public void subtract(YearMonthDuration yearMonthDuration) {
		date = date.minus(yearMonthDuration.getDuration());
	}
}