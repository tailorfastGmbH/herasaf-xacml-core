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
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private final Logger logger = LoggerFactory.getLogger(Date.class);
	private static DateTimeFormatter DATE_TIME_PARSER;
	private static DateTimeFormatter DATE_TIME_PRINTER;
	private DateTime date;

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

		// This formatter equals: yyyy-MM-dd
		DateTimeFormatter dhmsFormatter = ISODateTimeFormat.date();

		// Determine timezone formatter to be used.
		DateTimeFormatter dateTimeFormatterToBeUsedInPrinter;
		if (useZuluUtcRepresentation) {
			dateTimeFormatterToBeUsedInPrinter = zuluTimezoneFormatter;
		} else {
			dateTimeFormatterToBeUsedInPrinter = defaultTimezoneFormatter;
		}

		// Here a parser is created that parses a string of the form yyyy-MM-dd. Further the string may have
		// a timezone. withOffsetParsed makes the parser to respect the set timezone (if one is set)
		// Zulu timezone formatter is used because it can handle +-00:00 and 'Z' for UTC.
		DATE_TIME_PARSER = new DateTimeFormatterBuilder().append(dhmsFormatter)
				.appendOptional(zuluTimezoneFormatter.getParser()).toFormatter().withOffsetParsed();

		// Here a printer is created that prints this dateTime in the form yyyy-MM-dd ZZ
		DATE_TIME_PRINTER = new DateTimeFormatterBuilder().append(dhmsFormatter)
				.append(dateTimeFormatterToBeUsedInPrinter.getPrinter()).toFormatter();
	}

	public Date(String dateString) throws SyntaxException {
		dateString = dateString.trim();
		try {
			date = DATE_TIME_PARSER.withOffsetParsed().parseDateTime(dateString);
		} catch (IllegalArgumentException e) {
			String message = String.format(
					"The date '%s' is not a valid date according to http://www.w3.org/2001/XMLSchema#date", dateString);
			logger.error(message);
			throw new SyntaxException(message, e);
		} catch (UnsupportedOperationException e) {
			String message = "Parsing date is not supported.";
			logger.error(message);
			throw new SyntaxException(message, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return DATE_TIME_PRINTER.print(date);
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(Date o) {
		DateTime jodaThisDate = this.getDate();
		DateTime jodaThatDate = o.getDate();
		int comparisonResult = jodaThisDate.compareTo(jodaThatDate);
		return comparisonResult;
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
		DateTime jodaThisDate = getDate();
		DateTime jodaThatDate = ((Date) obj).getDate();
		boolean isEqual = jodaThisDate.isEqual(jodaThatDate);
		return isEqual;
	}

	/**
	 * Returns the internal representation of this date.
	 * 
	 * @return internal representation of this date.
	 */
	public DateTime getDate() {
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