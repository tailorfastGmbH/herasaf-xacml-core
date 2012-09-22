/*
 * Copyright 2009 - 2012 HERAS-AF (www.herasaf.org)
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
package org.herasaf.xacml.core.simplePDP.initializers.jaxb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.InitializationException;
import org.herasaf.xacml.core.simplePDP.initializers.api.Initializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This initializer is the base class for all initializers that load classes for
 * the classpath and make them available for the JAXB type-adapters.
 * 
 * @param <T>
 *            The type this initializer is made for.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 * @author Alexander Broekhuis
 */
public abstract class AbstractJaxbTypeAdapterInitializer<T> implements
		Initializer {
	private transient static Logger logger = LoggerFactory
			.getLogger(AbstractJaxbTypeAdapterInitializer.class);

	/**
	 * {@inheritDoc}<br />
	 * <b>This implementation:</b><br />
	 * This implementation calls the abstract {@link #createTypeInstances()}
	 * method that is to be implemented by a subclass. This method returns the
	 * instantiated objects. These objectes are then set into the corresponding
	 * type adapter through {@link #setInstancesIntoTypeAdapter(Map)}.
	 */
	public final void run() {
		Map<String, T> instancesMap = createTypeInstances();
		setInstancesIntoTypeAdapter(instancesMap);
	}

	/**
	 * This is a helper method that can be used by subclasses to instatiate
	 * objects from a given types.
	 * 
	 * Attention types classes must be of type T.
	 * 
	 * @param types
	 *            The types to be instantiated. The types must have an no-arg
	 *            constructor.
	 * @return A {@link List} containing the instances.
	 */
	protected final List<T> createInstances(Class<?>... types) {
		List<T> instances = new ArrayList<T>();

		Class<T>[] concreteTypes = createTypedArray(types);
		for (Class<T> type : concreteTypes) {
			T instance = createInstance(type);
			instances.add(instance);
		}
		return instances;
	}

	private Class<T>[] createTypedArray(Class<?>... types) {
		Class<T>[] concreteTypes;
		try {
			@SuppressWarnings("unchecked")
			Class<T>[] concreteTypesTmp = (Class<T>[]) types;
			concreteTypes = concreteTypesTmp;
		} catch (ClassCastException e) {
			String message = "The given types do not match the intented type of the concrete JaxbTypeAdapterInitializer.";
			logger.error(message, e);
			throw new InitializationException(message, e);
		}
		return concreteTypes;
	}

	/**
	 * Sets the instances into the converter map. The converter map is the map
	 * needed by the JAXB type adapter.
	 * 
	 * @param instancesMap
	 *            The map containing the instances. Key is the id of the object
	 *            of type T, the value is the object itself.
	 */
	protected abstract void setInstancesIntoTypeAdapter(
			Map<String, T> instancesMap);

	/**
	 * This method shall return the instances of the types to be set into a type adapter.
	 */
	protected abstract Map<String, T> createTypeInstances();

	private T createInstance(Class<T> type) {
		T instance;
		try {
			instance = type.newInstance();
		} catch (Exception e) {
			String message = String
					.format("Unable to instantiate object of type [%s]",
							type.getName());
			logger.error(message);
			throw new InitializationException(message, e);
		}
		return instance;
	}
}