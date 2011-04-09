/*
 * Copyright 2009 - 2011 HERAS-AF (www.herasaf.org)
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
package org.herasaf.xacml.core.simplePDP.initializers;

import java.util.Map;

import org.herasaf.xacml.core.InitializationException;
import org.herasaf.xacml.core.converter.DataTypeJAXBTypeAdapter;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This initializer initializes all data types and puts them in the
 * {@link DataTypeJAXBTypeAdapter} JAXB type adapter.
 * 
 * @author Florian Huonder
 * @author Ren� Eggenschwiler
 */
@SuppressWarnings("unchecked")
public class DataTypesJAXBInitializer extends
		AbstractInitializer<DataTypeAttribute<?>> {
	private static final String CLASS_TYPE_NAME = "org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute";
	private transient static Logger logger = LoggerFactory
			.getLogger(DataTypesJAXBInitializer.class);
	private static final String SEARCH_CONTEXT_PATH = "org/herasaf/xacml/core/dataTypeAttribute/impl";
	private static final Class<DataTypeAttribute<?>> TARGET_CLASS;

	/**
	 * This block initializes a Class object of the DataTypeAttributeType. This
	 * is needed because the DataTypeAttribute type is generic.
	 */
	static {
		Class<?> clazz;
		try {
			// load the class from the classpath
			clazz = Class.forName(CLASS_TYPE_NAME);
		} catch (ClassNotFoundException e) {
			// Must not occur. This would mean an illegal state.
			InitializationException ie = new InitializationException(
					"Unable to load the class " + CLASS_TYPE_NAME + ".", e);
			logger.error(ie.getMessage(), e);
			throw ie;
		}
		TARGET_CLASS = (Class<DataTypeAttribute<?>>) clazz;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getDefaultSearchContextPath() {
		return SEARCH_CONTEXT_PATH;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getURIFromType(DataTypeAttribute<?> instance) {
		return instance.getDatatypeURI();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setInstancesIntoConverter(
			Map<String, DataTypeAttribute<?>> instancesMap) {
		DataTypeJAXBTypeAdapter.setDataTypeAttributes(instancesMap);
		logger.info("{} DataTypeAttributes are initialized.", instancesMap
				.size());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<DataTypeAttribute<?>> getTargetClass() {
		return TARGET_CLASS;
	}

	/** {@inheritDoc} */
	public int hashCode() {
		return getClass().getName().hashCode();
	}

	/** {@inheritDoc} */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof DataTypesJAXBInitializer)) {
			return false;
		}

		return hashCode() == obj.hashCode();
	}
}