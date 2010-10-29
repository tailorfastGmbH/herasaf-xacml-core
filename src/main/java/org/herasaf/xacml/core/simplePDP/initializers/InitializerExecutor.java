/*
 * Copyright 2008-2010 HERAS-AF (www.herasaf.org)
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

import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
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

	private static final Logger LOGGER = LoggerFactory
			.getLogger(InitializerExecutor.class);

	private static Set<Initializer> initializers;

	/**
	 * The constructor is private to avoid instantiation. It is intended to be
	 * used in a static way.
	 */
	private InitializerExecutor() {
	}

	/**
	 * Gets the default list of initializers.
	 * 
	 * This list includes the {@link FunctionsJAXBInitializer},
	 * {@link DataTypesJAXBInitializer},
	 * {@link RuleCombiningAlgorithmsJAXBInitializer},
	 * {@link PolicyCombiningAlgorithmsJAXBInitializer}, and
	 * {@link ContextAndPolicyInitializer}.
	 * 
	 * @return the default list of initializers
	 */
	public static Set<Initializer> getDefaultInitializers() {
		Set<Initializer> initializers = new HashSet<Initializer>();
		initializers.add(new FunctionsJAXBInitializer());
		initializers.add(new DataTypesJAXBInitializer());
		initializers.add(new RuleCombiningAlgorithmsJAXBInitializer());
		initializers.add(new PolicyCombiningAlgorithmsJAXBInitializer());
		initializers.add(new JAXBInitializer());

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
		InitializerExecutor.initializers = initalizers;
	}

	/**
	 * Resets the executor to only use the default initializers.
	 */
	public static void resetInitializers() {
		InitializerExecutor.initializers = getDefaultInitializers();
	}

	/**
	 * Runs all {@link Initializer}s. If no custom initializers were set then
	 * use the default initializers as retrieved by
	 * {@link #getDefaultInitializers()}.
	 */
	public static void runInitializers() {
		Set<Initializer> inits;

		if (initializers == null) {
			inits = getDefaultInitializers();
		} else {
			inits = initializers;
		}

		if (inits != null) {
			for (Initializer initializer : inits) {
				initializer.run();
			}
		}
	}
}