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
import org.herasaf.xacml.core.types.Date;

/**
 * The Name of this data type is http://www.w3.org/2001/XMLSchema#date.<br>
 * See: <A HREF="http://www.w3.org/TR/xmlschema-2/#date"
 * target="_blank">http://www.w3.org/TR/xmlschema-2/#date</A> for further
 * information.
 * 
 * @author Stefan Oberholzer
 * @version 1.0
 */
public class DateDataTypeAttribute extends AbstractDataTypeAttribute<Date> {

	/** Data type ID URI. */
	public static final String ID = "http://www.w3.org/2001/XMLSchema#date";

	/** Serial version UID. */
	private static final long serialVersionUID = -5112328908612717257L;

	/** {@inheritDoc} */
	public Date convertTo(String jaxbRepresentation) throws SyntaxException {
		try {
			return new Date(jaxbRepresentation.trim());
		} catch (IllegalArgumentException e) {
			throw new SyntaxException(e);
		}
	}

	/** {@inheritDoc} */
	public String getDatatypeURI() {
		return ID;
	}
}