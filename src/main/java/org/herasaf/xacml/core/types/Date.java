/*
 * Copyright 2008 - 2011 HERAS-AF (www.herasaf.org)
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
 * <p>
 * Represents a http://www.w3.org/2001/XMLSchema#date. The specification can be
 * found at <A HREF="http://www.w3.org/2001/XMLSchema#date"
 * TARGET="_blank">http://www.w3.org/2001/XMLSchema#date</A>.
 * </p>
 * <p>
 * <b>NOTE:</b> The default {@link XMLGregorianCalendar} used if none is
 * specified for the DatatypeFactory. To specify a special calendar see the
 * documentation of the {@link DatatypeFactory} class. <br />
 * <br />
 * The default {@link XMLGregorianCalendar} brings an unlovely drawback:<br />
 * It accepts values that are not valid. E.g. every month may have 31 days.<br />
 * Example: The date <i>2004-04-31</i> is valid even though April has only 30 days.
 * </p>
 * 
 * @author Stefan Oberholzer
 * @author Florian Huonder
 */
public class Date implements Comparable<Date> {
	private XMLGregorianCalendar xmlCalendar;
	private static final String PATTERNSTRING = "\\d\\d\\d(\\d)+-\\d\\d-\\d\\d((\\+|-)\\d\\d:\\d\\d)?";

	/**
	 * Initializes a new {@link Date} object.
	 * 
	 * @param lexicalRepresentation
	 *            The {@link String} representation of the time to created with
	 *            this class.
	 * @throws ConvertException
	 */
	public Date(String lexicalRepresentation) {
		if (!lexicalRepresentation.matches(PATTERNSTRING)) {
			throw new IllegalArgumentException("The format of the argument: \"" + lexicalRepresentation
					+ "\" isn't correct");
		}
		try {
			DatatypeFactory factory = DatatypeFactory.newInstance();
			this.xmlCalendar = factory.newXMLGregorianCalendar(lexicalRepresentation);
		} catch (DatatypeConfigurationException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Returns the instance of the {@link XMLGregorianCalendar} of this
	 * {@link Date} class.
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
	public int compareTo(Date date) {
		return xmlCalendar.compare(date.getCalendar());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Date) {
			if (this.compareTo((Date) obj) == 0) {
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
	 * Adds the given {@link YearMonthDuration} to this {@link Date}.
	 * 
	 * @param duration
	 *            The {@link YearMonthDuration} to add.
	 */
	public void add(YearMonthDuration duration) {
		this.xmlCalendar.add(duration.getDuration());
	}

	/**
	 * Subtracts the given {@link YearMonthDuration} from this {@link Date}.
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