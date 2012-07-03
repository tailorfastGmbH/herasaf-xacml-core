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
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can parse and print a time of type http://www.w3.org/2001/XMLSchema#time with the pattern
 * 
 * HH ':' mm ':' ss ('.' SSS)? ZZ?"
 * 
 * The fractional seconds and timezone parts are optional on creation. The {@link #toString()} method will always print
 * the fractional seconds and the timezone. <b>note</b>24:00:00 will be converted into 00:00:00 (the following day)
 * 
 * @author Florian Huonder
 */
public class Time implements Comparable<Time> {
	private final Logger logger = LoggerFactory.getLogger(Time.class);
	private static DateTimeFormatter DATE_TIME_PARSER;
	private static DateTimeFormatter DATE_TIME_PARSER_CLOCKHOUR;
	private static DateTimeFormatter DATE_TIME_PRINTER_WITH_MILLIS;
	private static DateTimeFormatter DATE_TIME_PRINTER_WITHOUT_MILLIS;
	private static DateTimeFormatter MILLIS_PARSER;
	private static DateTimeComparator COMPARATOR;
	private DateTime time;
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
		COMPARATOR = DateTimeComparator.getTimeOnlyInstance();

		// The default formatter for the timezone that can handle only +-00:00 for UTC.
		DateTimeFormatter defaultTimezoneFormatter = new DateTimeFormatterBuilder().appendTimeZoneOffset(null, true, 2,
				2).toFormatter();
		// The formatter for the timezone that can handle Zulu value 'Z'.
		DateTimeFormatter zuluTimezoneFormatter = new DateTimeFormatterBuilder().appendTimeZoneOffset("Z", true, 2, 2)
				.toFormatter();
		// The formatter for the fractional (3 digit) seconds
		DateTimeFormatter fractionalSecondsFormatter = new DateTimeFormatterBuilder().appendLiteral('.')
				.appendFractionOfSecond(0, 3).toFormatter();

		// Here a parser is created that parses a string of the form HH:mm:ss. Further the string may have
		// 3 digit fractional seconds (with a dot as prefix) and may have a timezone.
		// Zulu timezone formatter is used because it can handle +-00:00 and 'Z' for UTC.
		DATE_TIME_PARSER = new DateTimeFormatterBuilder().appendHourOfDay(2).appendLiteral(':').appendMinuteOfHour(2)
				.appendLiteral(':').appendSecondOfMinute(2).appendOptional(fractionalSecondsFormatter.getParser())
				.appendOptional(zuluTimezoneFormatter.getParser()).toFormatter();

		DATE_TIME_PARSER_CLOCKHOUR = new DateTimeFormatterBuilder().appendLiteral("24:00:00")
				.appendOptional(zuluTimezoneFormatter.getParser()).toFormatter();

		// Determine timezone formatter to be used.
		DateTimeFormatter dateTimeFormatterToBeUsedInPrinter;
		if (useZuluUtcRepresentation) {
			dateTimeFormatterToBeUsedInPrinter = zuluTimezoneFormatter;
		} else {
			dateTimeFormatterToBeUsedInPrinter = defaultTimezoneFormatter;
		}

		// Here a printer is created that prints this dateTime in the form HH:mm:ss.SSS ZZ (.SSS is not printed when its
		// .000)
		DATE_TIME_PRINTER_WITHOUT_MILLIS = new DateTimeFormatterBuilder().appendHourOfDay(2).appendLiteral(':')
				.appendMinuteOfHour(2).appendLiteral(':').appendSecondOfMinute(2)
				.append(dateTimeFormatterToBeUsedInPrinter).toFormatter();

		DATE_TIME_PRINTER_WITH_MILLIS = new DateTimeFormatterBuilder().appendHourOfDay(2).appendLiteral(':')
				.appendMinuteOfHour(2).appendLiteral(':').appendSecondOfMinute(2).append(fractionalSecondsFormatter)
				.append(dateTimeFormatterToBeUsedInPrinter).toFormatter();

		MILLIS_PARSER = new DateTimeFormatterBuilder().appendFractionOfSecond(0, 3).toFormatter();
	}

	public Time(String timeString) throws SyntaxException {
		timeString = timeString.trim();
		try {
			time = DATE_TIME_PARSER.withOffsetParsed().parseDateTime(timeString);
		} catch (IllegalArgumentException e) {
			try {
				// If parsing failed check if the time is midnight as clockhour.
				time = DATE_TIME_PARSER_CLOCKHOUR.withOffsetParsed().parseDateTime(timeString);
				// The parser accepts 24:00:00 but it is saved as 00:00:00 the same day. Due to this the day must be
				// shifted plus one
				time = time.plus(Period.days(1));
			} catch (IllegalArgumentException e2) {
				SyntaxException se = new SyntaxException("The time '" + timeString
						+ "' is not a valid time according to http://www.w3.org/2001/XMLSchema#time", e);
				logger.error(se.getMessage());
				throw se;
			}
		} finally {
			if (time != null) {
				// Check if the time has fractional seconds
				String millis = MILLIS_PARSER.print(time);
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
			return DATE_TIME_PRINTER_WITHOUT_MILLIS.print(time);
		} else {
			return DATE_TIME_PRINTER_WITH_MILLIS.print(time);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(Time o) {
		return COMPARATOR.compare(time, o.getTime());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !obj.getClass().isAssignableFrom(Time.class)) {
			// Check if types are the same
			return false;
		}
		if (COMPARATOR.compare(this, obj) == 0) {
			// If types are the same check if they are equal
			return true;
		}
		// If they are not equal return false
		return false;
	}

	/**
	 * Returns the internal representation of this dateTime.
	 * 
	 * @return internal representation of this dateTime.
	 */
	public DateTime getTime() {
		return time;
	}
}