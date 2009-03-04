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

import org.herasaf.xacml.SyntaxException;

/**
 * The Name of this data type is http://www.w3.org/2001/XMLSchema#boolean.<br>
 * See: <A HREF="http://www.w3.org/TR/xmlschema-2/#boolean"
 * target="_blank">http://www.w3.org/TR/xmlschema-2/#boolean</A> for further
 * information.
 *
 * @author Stefan Oberholzer
 * @version 1.0
 */
public class BooleanDataTypeAttribute extends AbstractDataTypeAttribute<Boolean> {
	
	/** Data type ID URI. */
	public static final String ID = "http://www.w3.org/2001/XMLSchema#boolean";

	/** Serial version UID. */
	private static final long serialVersionUID = -4881189643269560315L;

	/** {@inheritDoc} */
	public Boolean convertTo(String jaxbRepresentation) throws SyntaxException {
		String value = jaxbRepresentation.trim();
		if (value.equals("1") || value.equals("true")) {
			return true;
		}
		if (value.equals("0") || value.equals("false")) {
			return false;
		}
		throw new SyntaxException();
	}

	/** {@inheritDoc} */
	public String getDatatypeURI() {
		return ID;
	}
}