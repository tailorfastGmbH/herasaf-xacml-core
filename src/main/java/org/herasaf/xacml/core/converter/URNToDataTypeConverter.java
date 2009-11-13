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

package org.herasaf.xacml.core.converter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.herasaf.xacml.core.NotInitializedException;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO JAVADOC
 * 
 * Converts an URN to a data type. The data types are defined in the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> appendix A.2, page 103. <br>
 * <br>
 * The {@link Map} containing the mapping between URNs and data type is static.
 * The setter for this {@link Map} is NOT static. The filling of this
 * {@link Map} takes place through the <a
 * href="http://www.springframework.org/">Springframework</a>.
 * 
 * @author Sacha Dolski
 * @author Florian Huonder
 * @author René Eggenschwiler
 * @version 1.1
 */
public class URNToDataTypeConverter extends XmlAdapter<String, DataTypeAttribute<?>> {
	private final Logger logger = LoggerFactory.getLogger(URNToDataTypeConverter.class);

	private static Map<String, DataTypeAttribute<?>> dataTypeAttributes;

	/**
	 * TODO JAVADOC
	 * 
	 * Is used by the <a
	 * href="http://www.springframework.org/">Springframework</a> to fill the
	 * static {@link Map} containing the mapping between URNs and data types.
	 * 
	 * @param dataTypes
	 *            The map containing the mapping between URNs and data types.
	 */
	public static void setDataTypeAttributes(Map<String, DataTypeAttribute<?>> dataTypes) {
		// TODO is a concurrent hashmap really needed?
		URNToDataTypeConverter.dataTypeAttributes = new ConcurrentHashMap<String, DataTypeAttribute<?>>(dataTypes); // ConcurrentHashMap
																													// because
																													// of
																													// concurrent
																													// access
																													// possible
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String marshal(DataTypeAttribute<?> dataTypeAttr) {
		String dataTypeAttrString;
		try {
			dataTypeAttrString = dataTypeAttr.toString();
		} catch (NullPointerException e) {
			logger.error("Argument dataTypeAttr must not be null: ", e);
			throw new IllegalArgumentException(e);
		}
		return dataTypeAttrString;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataTypeAttribute<?> unmarshal(String dataTypeId) {
		DataTypeAttribute<?> dta;
		try {
			dta = dataTypeAttributes.get(dataTypeId);
		} catch (NullPointerException e) {
			logger.error("URNToDataTypeConverter not properly initialized.");
			throw new NotInitializedException(e);
		}
		if (dta != null) {
			return dta;
		}
		throw new IllegalArgumentException("DataTypeAttribute " + dataTypeId + " unknown.");
	}
}
