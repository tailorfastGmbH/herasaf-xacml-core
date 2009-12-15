/*
 * Copyright 2008 HERAS-AF (www.herasaf.org)
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

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * TODO REVIEW René.
 * 
 * Represents a http://www.w3.org/2001/XMLSchema#dateTime. The specification can
 * be found at <A HREF="http://www.w3.org/2001/XMLSchema#dateTime"
 * TARGET="_blank">http://www.w3.org/2001/XMLSchema#dateTime</A>.
 * 
 * @author Stefan Oberholzer
 * @author Florian Huonder
 */
public class DateTime implements Comparable<DateTime> {
	private static final String MATCH_PATTERN = "\\S*-\\S*:\\S*";
	private XMLGregorianCalendar xmlCalendar;

	/**
	 * Initializes a new {@link DateTime} object.
	 * 
	 * @param lexicalRepresentation
	 *            The {@link String} representation of the time to created with
	 *            this class.
	 * @throws ConvertException
	 */
	public DateTime(String lexicalRepresentation) {
		try {
			DatatypeFactory factory = DatatypeFactory.newInstance();
			if (lexicalRepresentation.matches(MATCH_PATTERN)) {
				this.xmlCalendar = factory.newXMLGregorianCalendar(lexicalRepresentation);
			} else {
				throw new IllegalArgumentException("Only a Date is allowed");
			}
		} catch (DatatypeConfigurationException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Returns the instance of the {@link XMLGregorianCalendar} of this
	 * {@link DateTime} class.
	 * 
	 * @return The {@link XMLGregorianCalendar} instance.
	 */
	public XMLGregorianCalendar getCalendar() {
		return xmlCalendar;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return xmlCalendar.toXMLFormat();
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(DateTime dateTime) {
		return xmlCalendar.compare(dateTime.getCalendar());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DateTime) {
			if (this.compareTo((DateTime) obj) == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return xmlCalendar.hashCode();
	}

	/**
	 * Adds the given {@link DayTimeDuration} to the {@link DateTime}.
	 * 
	 * @param duration
	 *            The {@link DayTimeDuration} to add.
	 */
	public void add(DayTimeDuration duration) {
		this.xmlCalendar.add(duration.getDuration());
	}

	/**
	 * The {@link YearMonthDuration} to add to this {@link DateTime}.
	 * 
	 * @param duration
	 *            The {@link YearMonthDuration} to add.
	 */
	public void add(YearMonthDuration duration) {
		this.xmlCalendar.add(duration.getDuration());
	}

	/**
	 * Subtracts the given {@link DayTimeDuration} from this {@link DateTime}.
	 * 
	 * @param duration
	 *            The {@link DayTimeDuration} to subtract.
	 */
	public void subtract(DayTimeDuration duration) {
		if (duration.getDuration().toString().charAt(0) == '-') {
			this.xmlCalendar.add(new DayTimeDuration(duration.getDuration().toString().substring(1)).getDuration());
		} else {
			this.xmlCalendar.add(new DayTimeDuration("-" + duration.getDuration().toString()).getDuration());
		}
	}

	/**
	 * The {@link YearMonthDuration} to subtract from this {@link DateTime}.
	 * 
	 * @param duration
	 *            The {@link YearMonthDuration} to subtract.
	 */
	public void subtract(YearMonthDuration duration) {
		if (duration.getDuration().toString().charAt(0) == '-') {
			this.xmlCalendar.add(new YearMonthDuration(duration.getDuration().toString().substring(1)).getDuration());
		} else {
			this.xmlCalendar.add(new YearMonthDuration("-" + duration.getDuration().toString()).getDuration());
		}
	}
}