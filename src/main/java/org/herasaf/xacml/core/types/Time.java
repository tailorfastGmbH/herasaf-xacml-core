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

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Objects;

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
	private static final Logger logger = LoggerFactory.getLogger(Time.class);
	private static DateTimeFormatter timeFormatter;
	private OffsetTime time;

	/**
	 * Initializes the formatters when the type is initialized.
	 */
	static {
		useZuluUtcRepresentation(false);
	}

	/**
	 * @param useZuluUtcRepresentation - whether the UTC timezone shall be represented in Zulu ('Z') or standard (+00:00)
	 */
	public static void useZuluUtcRepresentation(
			boolean useZuluUtcRepresentation) {
		if (useZuluUtcRepresentation) {
			timeFormatter = new DateTimeFormatterBuilder()
					.parseCaseInsensitive()
					.append(DateTimeFormatter.ISO_LOCAL_TIME).optionalStart()
					.appendOffsetId().toFormatter();
		} else {
			timeFormatter = new DateTimeFormatterBuilder()
					.parseCaseInsensitive()
					.append(DateTimeFormatter.ISO_LOCAL_TIME).optionalStart()
					.appendOffset("+HH:MM", "+00:00").toFormatter();
		}
	}

	public Time(String timeString) throws SyntaxException {
		try {
			TemporalAccessor parsed = timeFormatter.parseBest(
					timeString.trim().replace("Z", "+00:00"), OffsetTime::from, LocalTime::from);
			if (parsed instanceof LocalTime) {
				time = ((LocalTime) parsed).atOffset(ZoneOffset.UTC);
			} else {
				time = (OffsetTime) parsed;
			}
		} catch (DateTimeParseException e) {
			String message = String.format("Parsing time %s is not supported.", timeString);
			logger.warn(message);
			throw new SyntaxException(message, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return time.format(timeFormatter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Time o) {
		OffsetTime thisTime = this.getTime();
		OffsetTime thatTime = o.getTime();
		return thisTime.withOffsetSameInstant(thatTime.getOffset()).compareTo(thatTime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !Time.class.isAssignableFrom(obj.getClass())) {
			// Check if types are the same
			return false;
		}
		OffsetTime thisTime = getTime();
		OffsetTime thatTime = ((Time) obj).getTime();
		return thisTime.withOffsetSameInstant(thatTime.getOffset()).equals(thatTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(time);
	}

	/**
	 * Returns the internal representation of this dateTime.
	 * 
	 * @return internal representation of this dateTime.
	 */
	public OffsetTime getTime() {
		return time;
	}
}