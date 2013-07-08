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

import org.herasaf.xacml.core.SyntaxException;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can parse and print a dateTime of type http://www.w3.org/2001/XMLSchema#dateTime with the pattern
 * 
 * yyyy '-' MM '-' dd 'T' HH ':' mm ':' ss ('.' SSS)? ZZ?"
 * 
 * The fractional seconds and timezone parts are optional on creation. The {@link #toString()} method will always print
 * the fractional seconds and the timezone.
 * 
 * @author Florian Huonder
 */
public class DateTime implements Comparable<DateTime> {
	private final Logger logger = LoggerFactory.getLogger(DateTime.class);
	private static DateTimeFormatter DATE_TIME_PARSER_CLOCKHOUR;
	private static DateTimeFormatter DATE_TIME_PARSER;
	private static DateTimeFormatter DATE_TIME_PRINTER_WITH_MILLIS;
	private static DateTimeFormatter DATE_TIME_PRINTER_WITHOUT_MILLIS;
	private static DateTimeFormatter MILLIS_PARSER;
	private org.joda.time.DateTime dateTime;
	private boolean noFractionalSeconds;

	/**
	 * Initializes the formatters when the type is initialized.
	 */
	static {
		useZuluUtcRepresentation(false);
	}

	/**
	 * Is used to set whether the UTC timezone shall be represented in Zulu ('Z') or standard (+00:00).
	 */
	public static void useZuluUtcRepresentation(boolean useZuluUtcRepresentation) {
		// The default formatter for the timezone that can handle only +-00:00 for UTC.
		DateTimeFormatter defaultTimezoneFormatter = new DateTimeFormatterBuilder().appendTimeZoneOffset(null, true, 2,
				2).toFormatter();
		// The formatter for the timezone that can handle Zulu value 'Z'.
		DateTimeFormatter zuluTimezoneFormatter = new DateTimeFormatterBuilder().appendTimeZoneOffset("Z", true, 2, 2)
				.toFormatter();
		// The formatter for the fractional (3 digit) seconds
		DateTimeFormatter fractionalSecondsFormatter = new DateTimeFormatterBuilder().appendLiteral('.')
				.appendFractionOfSecond(3, 3).toFormatter();
		// This formatter equals: yyyy-MM-dd'T'HH:mm:ss
		DateTimeFormatter dhmsFormatter = ISODateTimeFormat.dateHourMinuteSecond();

		// This formatter equals: yyyy-MM-dd
		DateTimeFormatter dateFormatter = ISODateTimeFormat.date();

		// Accepts yyyy-MM-dd'T'24:00:00, Zulu timezone formatter is used because it can handle +-00:00 and 'Z' for UTC.
		DATE_TIME_PARSER_CLOCKHOUR = new DateTimeFormatterBuilder().append(dateFormatter).appendLiteral("T24:00:00")
				.appendOptional(zuluTimezoneFormatter.getParser()).toFormatter();

		// Here a parser is created that parses a string of the form yyyy-MM-dd'T'HH:mm:ss. Further the string may have
		// 3 digit fractional seconds (with a dot as prefix) and may have a timezone.
		// Zulu timezone formatter is used because it can handle +-00:00 and 'Z' for UTC.
		DATE_TIME_PARSER = new DateTimeFormatterBuilder().append(dhmsFormatter)
				.appendOptional(fractionalSecondsFormatter.getParser())
				.appendOptional(zuluTimezoneFormatter.getParser()).toFormatter();

		// Here a printer is created that prints this dateTime in the form yyyy-MM-dd'T'HH:mm:ss.SSS ZZ (.SSS is not
		// printed when its .000)

		// Determine timezone formatter to be used.
		DateTimeFormatter dateTimeFormatterToBeUsedInPrinter;
		if (useZuluUtcRepresentation) {
			dateTimeFormatterToBeUsedInPrinter = zuluTimezoneFormatter;
		} else {
			dateTimeFormatterToBeUsedInPrinter = defaultTimezoneFormatter;
		}

		DATE_TIME_PRINTER_WITHOUT_MILLIS = new DateTimeFormatterBuilder().append(dhmsFormatter)
				.append(dateTimeFormatterToBeUsedInPrinter).toFormatter();

		DATE_TIME_PRINTER_WITH_MILLIS = new DateTimeFormatterBuilder().append(dhmsFormatter)
				.append(fractionalSecondsFormatter).append(dateTimeFormatterToBeUsedInPrinter).toFormatter();

		MILLIS_PARSER = new DateTimeFormatterBuilder().appendFractionOfSecond(0, 3).toFormatter();
	}

	public DateTime(String dateTimeString) throws SyntaxException {
		dateTimeString = dateTimeString.trim();
		try {
			dateTime = DATE_TIME_PARSER.withOffsetParsed().parseDateTime(dateTimeString);
		} catch (IllegalArgumentException e) {
			try {
				// If parsing failed check if the time part is midnight as clockhour.
				dateTime = DATE_TIME_PARSER_CLOCKHOUR.withOffsetParsed().parseDateTime(dateTimeString);
				// The parser accepts 24:00:00 but it is saved as 00:00:00 the same day. Due to this the day must be
				// shifted plus one.
				dateTime = dateTime.plus(Period.days(1));
			} catch (IllegalArgumentException e2) {
				SyntaxException se = new SyntaxException("The dateTime '" + dateTimeString
						+ "' is not a valid dateTime according to http://www.w3.org/2001/XMLSchema#dateTime", e);
				logger.error(se.getMessage());
				throw se;
			}
		} finally {
			if (dateTime != null) {
				// Check if the time part has fractional seconds
				String millis = MILLIS_PARSER.print(dateTime);
				noFractionalSeconds = millis.length() == 0;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		if (noFractionalSeconds) {
			return DATE_TIME_PRINTER_WITHOUT_MILLIS.print(dateTime.withZoneRetainFields(dateTime.getZone()));
		} else {
			return DATE_TIME_PRINTER_WITH_MILLIS.print(dateTime.withZoneRetainFields(dateTime.getZone()));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(DateTime o) {
		org.joda.time.DateTime jodaThisDateTime = this.getDateTime();
		org.joda.time.DateTime jodaThatDateTime = o.getDateTime();
		int comparisonResult = jodaThisDateTime.compareTo(jodaThatDateTime);
		return comparisonResult;
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
		org.joda.time.DateTime jodaThisDateTime = getDateTime();
		org.joda.time.DateTime jodaThatDateTime = ((DateTime) obj).getDateTime();
		boolean isEqual = jodaThisDateTime.isEqual(jodaThatDateTime);
		return isEqual;
	}

	/**
	 * Returns the internal representation of this dateTime.
	 * 
	 * @return internal representation of this dateTime.
	 */
	public org.joda.time.DateTime getDateTime() {
		return dateTime;
	}

	public void add(DayTimeDuration dayTimeDuration) {
		dateTime = dateTime.plus(dayTimeDuration.getDuration());
	}

	public void add(YearMonthDuration yearMonthDuration) {
		dateTime = dateTime.plus(yearMonthDuration.getDuration());
	}

	public void subtract(DayTimeDuration dayTimeDuration) {
		dateTime = dateTime.minus(dayTimeDuration.getDuration());
	}

	public void subtract(YearMonthDuration yearMonthDuration) {
		dateTime = dateTime.minus(yearMonthDuration.getDuration());
	}
}