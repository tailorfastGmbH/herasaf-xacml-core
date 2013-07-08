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
package org.herasaf.xacml.core.simplePDP.initializers;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.simplePDP.SimplePDPConfiguration;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
import org.herasaf.xacml.core.simplePDP.initializers.api.Initializer;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.JaxbContextInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.typeadapter.xacml20.datatypes.Xacml20DefaultDataTypesJaxbInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.typeadapter.xacml20.functions.Xacml20DefaultFunctionsJaxbInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.typeadapter.xacml20.policycombiningalgorithms.Xacml20DefaultPolicyCombiningAlgorithmsJaxbInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.typeadapter.xacml20.rulecombiningalgorithms.Xacml20DefaultRuleCombiningAlgorithmsJaxbInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link InitializerExecutor} is used as a global initializer which runs
 * the {@link Initializer}s in this package. It is intended to be used
 * standalone or from classes which have the need to execute the initializers
 * (such as e.g. {@link SimplePDPFactory}).
 * 
 * @author Ylli Sylejmani
 */
public class InitializerExecutor {

	private transient static final Logger LOGGER = LoggerFactory
			.getLogger(InitializerExecutor.class);
	private static Set<Initializer> initializers;
	private static AtomicBoolean initializersRan = new AtomicBoolean(false);

	/**
	 * The constructor is private to avoid instantiation. It is intended to be
	 * used in a static way.
	 */
	private InitializerExecutor() {
	}

	/**
	 * Gets the default list of initializers.
	 * 
	 * This list includes the 
	 * 		{@link Xacml20DefaultFunctionsJaxbInitializer},
	 * 		{@link Xacml20DefaultDataTypesJaxbInitializer},
	 * 		{@link Xacml20DefaultRuleCombiningAlgorithmsJaxbInitializer},
	 * 		{@link Xacml20DefaultPolicyCombiningAlgorithmsJaxbInitializer}, and
	 * 		{@link JaxbContextInitializer}.
	 * 
	 * @return the default list of initializers
	 */
	public static Set<Initializer> getDefaultInitializers() {
		Set<Initializer> initializers = new HashSet<Initializer>();
		initializers.add(new Xacml20DefaultFunctionsJaxbInitializer());
		initializers.add(new Xacml20DefaultDataTypesJaxbInitializer());
		initializers.add(new Xacml20DefaultRuleCombiningAlgorithmsJaxbInitializer());
		initializers.add(new Xacml20DefaultPolicyCombiningAlgorithmsJaxbInitializer());
		initializers.add(new JaxbContextInitializer());

		return initializers;
	}

	/**
	 * Extension point for usage of custom initializers to instantiate
	 * functions, data types, combining algorithms and JAXB. <br />
	 * <br />
	 * If other initializers than the default initializers are needed. The
	 * custom list should be created and handed over to this method. <br />
	 * <br />
	 * If custom initializers are used the {@link #useDefaultInitializers()}
	 * must not be called!
	 * 
	 * @param initalizers
	 *            A list containing the initializers that shall run when
	 *            retrieving a new {@link PDP}.
	 * 
	 * @see {@link #useDefaultInitializers()}
	 */
	public static void setInitalizers(Set<Initializer> initalizers) {
		LOGGER.info("Custom initializers are in use.");
		if (initalizers != InitializerExecutor.initializers) {
		    InitializerExecutor.initializers = initalizers;
		    initializersRan.set(false);
		}
	}

	/**
	 * Resets the executor to only use the default initializers.
	 */
	public static void resetInitializers() {
		InitializerExecutor.initializers = null;
		initializersRan.set(false);
	}

	/**
         * Runs all {@link Initializer}s. If no custom initializers were set then
         * use the default initializers as retrieved by
         * {@link #getDefaultInitializers()}.
         */
        public static void runInitializers() {
                runInitializers(null);
        }
        
	/**
	 * Runs all {@link Initializer}s. If no custom initializers were set then
	 * use the default initializers as retrieved by
	 * {@link #getDefaultInitializers()}.
	 */
	public static void runInitializers(SimplePDPConfiguration configuration) {
		if (!initializersRan.get()) { //if the initializers were not executed earlier.
			Set<Initializer> inits;

			if (initializers == null) {
				inits = getDefaultInitializers();
			} else {
				inits = initializers;
			}

			if (inits != null) {
				for (Initializer initializer : inits) {
					initializer.run(configuration);
				}
			}
			initializersRan.set(true);
		} 
	}
}