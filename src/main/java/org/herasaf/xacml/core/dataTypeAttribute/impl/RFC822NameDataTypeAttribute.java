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
import org.herasaf.xacml.core.types.RFC822Name;

/**
 * TODO JAVADOC
 * 
 * The Name of this data type is
 * urn:oasis:names:tc:xacml:1.0:data-type:rfc822Name.<br>
 * See: <a
 * href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 103, for further information.
 *
 * @author Stefan Oberholzer
 * @version 1.0
 */
public class RFC822NameDataTypeAttribute extends AbstractDataTypeAttribute<RFC822Name> {
	
	/** Data type ID URI. */
	public static final String ID = "urn:oasis:names:tc:xacml:1.0:data-type:rfc822Name";

	/** Serial version UID. */
	private static final long serialVersionUID = 3529004809034784066L;

	/** {@inheritDoc} */
	public RFC822Name convertTo(String jaxbRepresentation)
			throws SyntaxException {
		try {
			return new RFC822Name(jaxbRepresentation.trim());
		} catch (Exception e) {
			throw new SyntaxException(e);
		}
	}

	/** {@inheritDoc} */
	public String getDatatypeURI() {
		return ID;
	}
}