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

import java.math.BigInteger;

import org.herasaf.xacml.core.SyntaxException;

/**
 * This data type represents a http://www.w3.org/2001/XMLSchema#integer. See: <A
 * HREF="http://www.w3.org/TR/xmlschema-2/#integer"
 * target="_blank">http://www.w3.org/TR/xmlschema-2/#integer</A> for further
 * information.
 * 
 * @author Stefan Oberholzer
 */
public class IntegerDataTypeAttribute extends AbstractDataTypeAttribute<BigInteger> {
	public static final String ID = "http://www.w3.org/2001/XMLSchema#integer";
	private static final long serialVersionUID = 1L;

	/** {@inheritDoc} */
	public BigInteger convertTo(String jaxbRepresentation) throws SyntaxException {
		try {
			return new BigInteger(jaxbRepresentation.trim());
		} catch (Exception e) {
			throw new SyntaxException(e);
		}
	}

	/** {@inheritDoc} */
	public String getDatatypeURI() {
		return ID;
	}
}