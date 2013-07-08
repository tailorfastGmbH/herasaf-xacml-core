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
package org.herasaf.xacml.core.dataTypeAttribute.impl;

import java.util.List;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.types.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This data type represents a http://www.w3.org/TR/xmlschema-2/#dateTime. See: <A
 * HREF="http://www.w3.org/TR/xmlschema-2/#dateTime" target="_blank">http://www.w3.org/TR/xmlschema-2/#dateTime</A> for
 * further information.
 * 
 * @author Stefan Oberholzer
 * @author Florian Huonder
 */
public class DateTimeDataTypeAttribute extends AbstractDataTypeAttribute<DateTime> {
	public static final String ID = "http://www.w3.org/2001/XMLSchema#dateTime";
	private final Logger logger = LoggerFactory.getLogger(DateTimeDataTypeAttribute.class);
	private static final long serialVersionUID = 1L;

	/** {@inheritDoc} */
	public DateTime convertTo(List<?> jaxbRepresentation) throws SyntaxException {
		try {
			return new DateTime(((String) jaxbRepresentation.get(0)).trim());
		} catch (Exception e) {
			logger.error("An unexpected error occured.", e);
			throw new SyntaxException(e);
		}
	}

	/** {@inheritDoc} */
	public String getDatatypeURI() {
		return ID;
	}
}