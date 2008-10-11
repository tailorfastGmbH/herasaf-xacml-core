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
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.herasaf.xacml.core.types.DnsName;

/**
 * The Name of this data type is urn:oasis:names:tc:xacml:2.0:data-type:dnsName.<br>
 * See: <a
 * href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 103, for further information.
 *
 * @author Florian Huonder
 * @version 1.0
 */
public class DnsNameDataTypeAttribute implements DataTypeAttribute<DnsName> {
	private static final long serialVersionUID = -6319529854643178474L;
	private static final String ID = "urn:oasis:names:tc:xacml:2.0:data-type:dnsName";

	/*
	 * (non-Javadoc)
	 *
	 * @see org.herasaf.core.dataTypeAttribute.DataTypeAttribute#convertTo(java.lang.String)
	 */
	public DnsName convertTo(String jaxbRepresentation) throws SyntaxException {
		try {
			return new DnsName(jaxbRepresentation.trim());
		} catch (IllegalArgumentException e) {
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