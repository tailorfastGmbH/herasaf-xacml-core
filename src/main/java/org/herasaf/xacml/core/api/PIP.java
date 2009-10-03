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

package org.herasaf.xacml.core.api;

import java.util.List;

import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.RequestType;

/**
 * When a request doesn't contain an attribute, the PDP has to call the PIP for
 * the information of this attribute. An PIP implementation is
 * responsible to resolve Attributes not included in the request. In the
 * standard XACML dataflow, this means, that the PIP is asked for the information.
 * See: <a
 * href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 78, chapter attribute retrieval for further information.
 * 
 * @author Stefan Oberholzer
 * @version 1.0
 */
public interface PIP {

	/**
	 * Extends the given {@link RequestType} with the attributes given through
	 * the resolved missing attributes and returnes the added
	 * {@link AttributeValueType}s.
	 * 
	 * @param request -
	 *            The Request to append
	 * @param attributeId -
	 *            The AttributeID of the requested AttributeValues
	 * @param dataType -
	 *            The DataType of the requested AttributeValues
	 * @param issuer -
	 *            The Issuer of the requested AttributeValues
	 * @param subjectCategory -
	 *            The SubjectCategory of the requestedValues
	 * @return The found AttributeValues. If no AttributeValues could be found
	 *         or an error occurred, an empty list is returned.
	 */
	List<AttributeValueType> requestSubjectAttributes(RequestType request,
			String attributeId, String dataType, String issuer,
			String subjectCategory);

	/**
	 * Extends the given {@link RequestType} with the attributes given through
	 * the resolved missing attributes and returnes the added
	 * {@link AttributeValueType}s.
	 * 
	 * @param request -
	 *            The Request to append
	 * @param attributeId -
	 *            The AttributeID of the requested AttributeValues
	 * @param dataType -
	 *            The DataType of the requested AttributeValues
	 * @param issuer -
	 *            The Issuer of the requested AttributeValues
	 * @return The found AttributeValues. If no AttributeValues could be found
	 *         or an error occurred, an empty list is returned.
	 */
	List<AttributeValueType> requestResourceAttributes(RequestType request,
			String attributeId, String dataType, String issuer);

	/**
	 * Extends the given {@link RequestType} with the attributes given through
	 * the resolved missing attributes and returnes the added
	 * {@link AttributeValueType}s.
	 * 
	 * @param request -
	 *            The Request to append
	 * @param attributeId -
	 *            The AttributeID of the requested AttributeValues
	 * @param dataType -
	 *            The DataType of the requested AttributeValues
	 * @param issuer -
	 *            The Issuer of the requested AttributeValues
	 * @return The found AttributeValues. If no AttributeValues could be found
	 *         or an error occurred, an empty list is returned.
	 */
	List<AttributeValueType> requestActionAttributes(RequestType request,
			String attributeId, String dataType, String issuer);

	/**
	 * Extends the given {@link RequestType} with the attributes given through
	 * the resolved missing attributes and returnes the added
	 * {@link AttributeValueType}s.
	 * 
	 * @param request -
	 *            The Request to append
	 * @param attributeId -
	 *            The AttributeID of the requested AttributeValues
	 * @param dataType -
	 *            The DataType of the requested AttributeValues
	 * @param issuer -
	 *            The Issuer of the requested AttributeValues
	 * @return The found AttributeValues. If no AttributeValues could be found
	 *         or an error occurred, an empty list is returned.
	 */
	List<AttributeValueType> requestEnvironmentAttributes(RequestType request,
			String attributeId, String dataType, String issuer);
}
