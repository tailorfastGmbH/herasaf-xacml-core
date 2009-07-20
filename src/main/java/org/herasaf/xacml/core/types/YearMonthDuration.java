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
import javax.xml.datatype.Duration;


/**
 * The Name of this datatype is urn:oasis:names:tc:xacml:2.0:data-type:yearMonthDuration.<br>
 * See:	<a href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June 2006</a> page 103, for further information.
 *
 * @author Stefan Oberholzer 
 * @version 1.0
 */
public class YearMonthDuration implements Comparable<YearMonthDuration> {
	private static final String PATTERNSTRING = "(\\-)?P\\d+(Y(\\d+M)?|M)";
	private Duration duration;
	/**
	 * Creates a new {@link YearMonthDuration} with the given duration.
	 *
	 * @param duration The duration to convert into a {@link YearMonthDuration}.
	 * @throws ConvertException
	 */
	public YearMonthDuration(String duration){
		if (!duration.matches(PATTERNSTRING)) {
			throw new IllegalArgumentException(
					"The format of the argument isn't correct");
		}

		try {
			DatatypeFactory factory = DatatypeFactory.newInstance();
				this.duration = factory.newDuration(duration);
		} catch (DatatypeConfigurationException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return duration.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(YearMonthDuration o) {
		return duration.compare(o.duration);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof YearMonthDuration) {
			YearMonthDuration object = (YearMonthDuration) obj;
			return this.duration.equals(object.duration);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return duration.hashCode();
	}

	protected Duration getDuration(){
		return duration;
	}
}