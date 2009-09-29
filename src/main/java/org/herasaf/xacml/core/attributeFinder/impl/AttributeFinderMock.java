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

package org.herasaf.xacml.core.attributeFinder.impl;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.attributeFinder.AttributeFinder;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.RequestType;

/**
 * 
 * TODO JAVADOC!! wait for HERASAFXACMLCORE-2.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 *
 */
public class AttributeFinderMock implements AttributeFinder {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.xacml.core.attributeFinder.AttributeFinder#requestActionAttributes(org.herasaf.xacml.core.context.impl.RequestType,
	 *      java.lang.String,
	 *      org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute,
	 *      java.lang.String)
	 */
	public List<AttributeValueType> requestActionAttributes(
			RequestType request, String attributeId, String dataType,
			String issuer) {
		return new ArrayList<AttributeValueType>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.xacml.core.attributeFinder.AttributeFinder#requestEnvironmentAttributes(org.herasaf.xacml.core.context.impl.RequestType,
	 *      java.lang.String,
	 *      org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute,
	 *      java.lang.String)
	 */
	public List<AttributeValueType> requestEnvironmentAttributes(
			RequestType request, String attributeId, String dataType,
			String issuer) {
		return new ArrayList<AttributeValueType>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.xacml.core.attributeFinder.AttributeFinder#requestResourceAttributes(org.herasaf.xacml.core.context.impl.RequestType,
	 *      java.lang.String,
	 *      org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute,
	 *      java.lang.String)
	 */
	public List<AttributeValueType> requestResourceAttributes(
			RequestType request, String attributeId, String dataType,
			String issuer) {
		return new ArrayList<AttributeValueType>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.xacml.core.attributeFinder.AttributeFinder#requestSubjectAttributes(org.herasaf.xacml.core.context.impl.RequestType,
	 *      java.lang.String,
	 *      org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute,
	 *      java.lang.String, java.lang.String)
	 */
	public List<AttributeValueType> requestSubjectAttributes(
			RequestType request, String attributeId, String dataType,
			String issuer, String subjectCategory) {
		return new ArrayList<AttributeValueType>();
	}

}
