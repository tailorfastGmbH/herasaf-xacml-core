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

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Objects;

/**
 * This class can parse and print a dateTime of type
 * http://www.w3.org/2001/XMLSchema#dateTime with the pattern
 * 
 * yyyy '-' MM '-' dd 'T' HH ':' mm ':' ss ('.' SSS)? ZZ?"
 * 
 * The fractional seconds and timezone parts are optional on creation. The
 * {@link #toString()} method will always print the fractional seconds and the
 * timezone.
 * 
 * @author Florian Huonder
 */
public class DateTime implements Comparable<DateTime> {
	private static final Logger logger = LoggerFactory.getLogger(DateTime.class);
	private static DateTimeFormatter dateTimeFormatter;
	private static ZoneId defaultZoneId;
	private OffsetDateTime dateTime;

	/**
	 * Initializes the formatters when the type is initialized.
	 */
	static {
		configureWith(false, ZoneOffset.UTC);
	}

	/**
	 * Is used to set whether the UTC timezone shall be represented in Zulu ('Z') or
	 * standard (+00:00).
	 */
	public static void useZuluUtcRepresentation(boolean useZuluUtcRepresentation) {
		if (useZuluUtcRepresentation) {
			dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		} else {
			dateTimeFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive().parseLenient()
					.append(DateTimeFormatter.ISO_LOCAL_DATE_TIME).appendOffset("+HH:MM", "+00:00").toFormatter();
		}
	}

	/**
	 * @param useZuluUtcRepresentation - whether the UTC timezone shall be
	 *                                 represented in Zulu ('Z') or standard
	 *                                 (+00:00)
	 * @param defaultZoneId            - ZoneId used to handle local dates.
	 */
	public static void configureWith(boolean useZuluUtcRepresentation, ZoneId defaultZoneId) {
		DateTime.defaultZoneId = defaultZoneId;
		useZuluUtcRepresentation(useZuluUtcRepresentation);
	}

	public DateTime(String dateTimeString) throws SyntaxException {
		try {
			// Java implement strict ISO8601 which does not allow 24:00, but XML-DateTime
			// does.
			boolean handle24hours = dateTimeString.contains("T24:00");
			if (handle24hours) {
				dateTimeString = dateTimeString.replace("T24:00", "T00:00");
			}
			TemporalAccessor parsed = DateTimeFormatter.ISO_DATE_TIME.parseBest(dateTimeString.trim(),
					OffsetDateTime::from, LocalDateTime::from);
			if (parsed instanceof LocalDateTime) {
				dateTime = ((LocalDateTime) parsed).atZone(defaultZoneId).toOffsetDateTime();
			} else {
				dateTime = (OffsetDateTime) parsed;
			}
			if (handle24hours) {
				dateTime = dateTime.plus(1, ChronoUnit.DAYS);
			}
		} catch (DateTimeParseException e) {
			String message = String.format("Parsing dateTime %s is not supported.", dateTimeString);
			logger.error(message);
			throw new SyntaxException(message, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return dateTime.format(dateTimeFormatter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(DateTime o) {
		OffsetDateTime thisDateTime = this.getDateTime();
		OffsetDateTime thatDateTime = o.getDateTime();
		return OffsetDateTime.timeLineOrder().compare(thisDateTime, thatDateTime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !obj.getClass().isAssignableFrom(DateTime.class)) {
			// Check if types are the same
			return false;
		}
		OffsetDateTime thisDateTime = getDateTime();
		OffsetDateTime thatDateTime = ((DateTime) obj).getDateTime();
		return thisDateTime.isEqual(thatDateTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateTime);
	}

	/**
	 * Returns the internal representation of this dateTime.
	 * 
	 * @return internal representation of this dateTime.
	 */
	public OffsetDateTime getDateTime() {
		return dateTime;
	}

	public void add(DayTimeDuration dayTimeDuration) {
		dateTime = dateTime.plus(dayTimeDuration.getDuration());
	}

	public void add(YearMonthDuration yearMonthDuration) {
		if (yearMonthDuration.isNegative()) {
			dateTime = dateTime.minus(yearMonthDuration.getDuration());
		} else {
			dateTime = dateTime.plus(yearMonthDuration.getDuration());
		}
	}

	public void subtract(DayTimeDuration dayTimeDuration) {
		dateTime = dateTime.minus(dayTimeDuration.getDuration());
	}

	public void subtract(YearMonthDuration yearMonthDuration) {
		if (yearMonthDuration.isNegative()) {
			dateTime = dateTime.plus(yearMonthDuration.getDuration());
		} else {
			dateTime = dateTime.minus(yearMonthDuration.getDuration());
		}
	}
}