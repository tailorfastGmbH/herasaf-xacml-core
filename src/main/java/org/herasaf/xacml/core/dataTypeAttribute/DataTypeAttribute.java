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

package org.herasaf.xacml.core.dataTypeAttribute;

import org.herasaf.xacml.SyntaxException;

/**
 * A DataType is used as an attribute in the JAXB Objects representing the XACML
 * elements. To get the correct value of the attribute-element, the
 * {@link #convertTo(String)} method should be used to convert the value to the
 * object.<br>
 * The data types are defined in the <a
 * href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> appendix A.2, page 103.
 *
 *
 * @author Florian Huonder
 * @version 1.0
 * @param <E>
 *            The data type of the containing attribute.
 */
public interface DataTypeAttribute<E> extends java.io.Serializable {
	
	/**
	 * Gets the URI the identifies this datatype.
	 * 
	 * @return URI the identifies this datatype
	 */
	public String getDatatypeURI();

	/**
	 * Converts a jaxb-representation string into the data type E.
	 *
	 * @param jaxbRepresentation
	 *            The string to convert
	 * @return Returns the created object.
	 * @throws SyntaxException
	 */
	public E convertTo(String jaxbRepresentation) throws SyntaxException;
}