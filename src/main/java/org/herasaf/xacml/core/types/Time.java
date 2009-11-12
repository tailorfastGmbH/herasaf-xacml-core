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
 * TODO REVIEW
 * 
 * Represents the http://www.w3.org/2001/XMLSchema#time data type. The
 * specification can be found at <a
 * href="http://www.w3.org/2001/XMLSchema#time">
 * http://www.w3.org/2001/XMLSchema#time</a>.
 * 
 * @author Stefan Oberholzer
 * @author Florian Huonder
 */
public class Time implements Comparable<Time> {
	private XMLGregorianCalendar xmlCalendar;

	/**
	 * TODO REVIEW
	 * 
	 * Initializes a new {@link Time} object.
	 * 
	 * @param lexicalRepresentation
	 *            The {@link String} representation of the time to created with
	 *            this class.
	 * @throws ConvertException
	 *             In case the {@link String} cannot be converted into <a
	 *             href="http://www.w3.org/2001/XMLSchema#time">
	 *             http://www.w3.org/2001/XMLSchema#time</a>.
	 */
	public Time(String lexicalRepresentation) {
		try {
			DatatypeFactory factory = DatatypeFactory.newInstance();

			if (lexicalRepresentation.matches("\\d\\d:\\d\\d:\\d\\d(\\.\\d(\\d)?(\\d)?)?([-+]\\d\\d:\\d\\d)?")) {
				this.xmlCalendar = factory.newXMLGregorianCalendar(lexicalRepresentation);
			} else {
				throw new IllegalArgumentException();
			}
		} catch (DatatypeConfigurationException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * TODO REVIEW
	 * 
	 * Returns the {@link XMLGregorianCalendar} instance of this {@link Time}
	 * object.
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
	public int compareTo(Time time) {
		return xmlCalendar.compare(time.getCalendar());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Time) {
			if (this.compareTo((Time) obj) == 0) {
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
}