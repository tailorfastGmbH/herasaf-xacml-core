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
 * This means if the timezone is e.g. GMT+1 (for Zurich) the date is printed as yyyy-mm-dd-01:00. This is the timezone calculated
 * back to UTC. So a comparison is possible.
 * 
 * @author Florian Huonder
 */
public class Date implements Comparable<Date> {
	private final Logger logger = LoggerFactory.getLogger(Date.class);
	private static final DateTimeFormatter DATE_TIME_PARSER;
	private static final DateTimeFormatter DATE_TIME_PRINTER;
	private static final DateTimeComparator COMPARATOR;
	private DateTime date;

	/**
	 * Initializes the comparator, the parser and the printer for this DateTime.
	 */
	static {
		COMPARATOR = DateTimeComparator.getDateOnlyInstance();

		// The formatter for the timezone
		DateTimeFormatter timezoneFormatter = new DateTimeFormatterBuilder().appendTimeZoneOffset(null, true, 2, 2)
				.toFormatter();

		// This formatter equals: yyyy-MM-dd
		DateTimeFormatter dhmsFormatter = ISODateTimeFormat.date();

		// Here a parser is created that parses a string of the form yyyy-MM-dd. Further the string may have
		// a timezone. withOffsetParsed makes the parser to respect the set timezone (if one is set)
		DATE_TIME_PARSER = new DateTimeFormatterBuilder().append(dhmsFormatter)
				.appendOptional(timezoneFormatter.getParser()).toFormatter().withOffsetParsed();

		// Here a printer is created that prints this dateTime in the form yyyy-MM-dd ZZ
		DATE_TIME_PRINTER = new DateTimeFormatterBuilder().append(dhmsFormatter).append(timezoneFormatter.getPrinter()).toFormatter();
	}

	/**
	 * TODO Javadoc
	 * 
	 * @param dateString
	 * @throws SyntaxException
	 */
	public Date(String dateString) throws SyntaxException {
		dateString = dateString.trim();
		try {
			date = DATE_TIME_PARSER.parseDateTime(dateString);
		} catch (IllegalArgumentException e) {
			SyntaxException se = new SyntaxException("The date '" + dateString
					+ "' is not a valid date according to http://www.w3.org/2001/XMLSchema#date", e);
			logger.error(se.getMessage());
			throw se;
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
		return COMPARATOR.compare(date, o.getDate());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !obj.getClass().isAssignableFrom(Date.class)) {
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