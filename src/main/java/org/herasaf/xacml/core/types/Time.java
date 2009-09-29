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
 * TODO JAVADOC
 * 
 * Represents a http://www.w3.org/2001/XMLSchema#time. The specification can be
 * found at <A HREF="http://www.w3.org/2001/XMLSchema#time"
 * TARGET="_blank">http://www.w3.org/2001/XMLSchema#time</A>.
 *
 * @author Stefan Oberholzer 
 * @author Florian Huonder 
 * @version 1.0
 */
public class Time implements Comparable<Time> {
	private XMLGregorianCalendar xmlCalendar;

	/**
	 * Initializes a new {@link Time} object.
	 *
	 * @param lexicalRepresentation
	 *            The {@link String} representation of the time to created with
	 *            this class.
	 * @throws ConvertException
	 */
	public Time(String lexicalRepresentation) {
		try {
			DatatypeFactory factory = DatatypeFactory.newInstance();
			
			if (lexicalRepresentation.matches("\\d\\d:\\d\\d:\\d\\d(\\.\\d(\\d)?(\\d)?)?([-+]\\d\\d:\\d\\d)?")) {
				this.xmlCalendar = factory
						.newXMLGregorianCalendar(lexicalRepresentation);
			} else {
				throw new IllegalArgumentException();
			}
		} catch (DatatypeConfigurationException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Returns the instance of the {@link XMLGregorianCalendar} of this
	 * {@link Time} class.
	 *
	 * @return The {@link XMLGregorianCalendar} instance.
	 */
	public XMLGregorianCalendar getCalendar() {
		return xmlCalendar;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return xmlCalendar.toXMLFormat();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Time time) {
		return xmlCalendar.compare(time.getCalendar());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Time) {
			if (this.compareTo((Time) obj) == 0){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return xmlCalendar.hashCode();
	}
}