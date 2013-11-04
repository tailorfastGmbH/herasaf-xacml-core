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

package org.herasaf.xacml.core.dataTypeAttribute.impl;

import java.util.List;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.types.HexBinary;

/**
 * This data type represents a http://www.w3.org/2001/XMLSchema#hexBinary.
 * See: <A HREF="http://www.w3.org/TR/xmlschema-2/#hexBinary"
 * target="_blank">http://www.w3.org/TR/xmlschema-2/#hexBinary</A> for further
 * information.
 * 
 * @author Stefan Oberholzer
 */
public class HexBinaryDataTypeAttribute extends AbstractDataTypeAttribute<HexBinary> {
	public static final String ID = "http://www.w3.org/2001/XMLSchema#hexBinary";
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	public HexBinary convertTo(List<?> jaxbRepresentation) throws SyntaxException {
		try {
			return new HexBinary(((String) jaxbRepresentation.get(0)).trim());
		} catch (IllegalArgumentException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDatatypeURI() {
		return ID;
	}
}