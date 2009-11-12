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

package org.herasaf.xacml.core.dataTypeAttribute.impl;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.types.DateTime;

/**
 * TODO JAVADOC
 * 
 * The Name of this data type is http://www.w3.org/TR/xmlschema-2/#dateTime.<br>
 * See: <A HREF="http://www.w3.org/TR/xmlschema-2/#dateTime"
 * target="_blank">http://www.w3.org/TR/xmlschema-2/#dateTime</A> for further
 * information.
 * 
 * @author Stefan Oberholzer
 * @version 1.0
 */
public class DateTimeDataTypeAttribute extends AbstractDataTypeAttribute<DateTime> {

	/** Data type ID URI. */
	public static final String ID = "http://www.w3.org/2001/XMLSchema#dateTime";

	/** Serial version UID. */
	private static final long serialVersionUID = 258484197947468750L;

	/** {@inheritDoc} */
	public DateTime convertTo(String jaxbRepresentation) throws SyntaxException {
		try {
			return new DateTime(jaxbRepresentation.trim());
		} catch (IllegalArgumentException e) {
			throw new SyntaxException(e);
		}
	}

	/** {@inheritDoc} */
	public String getDatatypeURI() {
		return ID;
	}
}