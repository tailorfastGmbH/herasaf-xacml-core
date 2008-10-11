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

import java.math.BigInteger;

import org.herasaf.xacml.SyntaxException;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;

/**
 * The Name of this datatype is http://www.w3.org/2001/XMLSchema#integer.<br>
 * See: <A HREF="http://www.w3.org/TR/xmlschema-2/#integer"
 * target="_blank">http://www.w3.org/TR/xmlschema-2/#integer</A> for further
 * information.
 *
 * @author Stefan Oberholzer
 * @version 1.0
 */
public class IntegerDataTypeAttribute implements DataTypeAttribute<BigInteger> {
	private static final long serialVersionUID = 2961063328872682725L;
	private static final String ID = "http://www.w3.org/2001/XMLSchema#integer";

	/*
	 * (non-Javadoc)
	 *
	 * @see org.herasaf.core.dataTypeAttribute.DataTypeAttribute#convertTo(java.lang.String)
	 */
	public BigInteger convertTo(String jaxbRepresentation)
			throws SyntaxException {
		try {
			return new BigInteger(jaxbRepresentation.trim());
		} catch (Exception e) {
			throw new SyntaxException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ID;
	}
}