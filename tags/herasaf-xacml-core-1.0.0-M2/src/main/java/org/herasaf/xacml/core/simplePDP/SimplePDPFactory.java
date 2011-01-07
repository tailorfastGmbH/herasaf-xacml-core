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

package org.herasaf.xacml.core.simplePDP;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.PolicyRetrievalPoint;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyOnlyOneApplicableAlgorithm;
import org.herasaf.xacml.core.simplePDP.initializers.DataTypesJAXBInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.FunctionsJAXBInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.Initializer;
import org.herasaf.xacml.core.simplePDP.initializers.JAXBInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.PolicyCombiningAlgorithmsJAXBInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.RuleCombiningAlgorithmsJAXBInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This factory represents an easy to use entry point into HERAS-AF XACML. <br/>
 * <br />
 * 
 * The Factory is responsible of creating a {@link PDP} with the given
 * configuration and doing all the needed initializations. <br />
 * Especially the JAXB relevant stuff is being initialized according to the PDP
 * setup with the corresponding XACML data types, functions, rule and policy
 * combining algorithms. <br />
 * All needed initialization is done automatically with reasonable defaults (see
 * below).<br />
 * Various hooks are defined to enhance the factory with further capabilities
 * and customizations.<br />
 * <br />
 * The default PDP returned by this factory has the following configuration:
 * <ul>
 * <li>Policy repository: {@link MapBasedSimplePolicyRepository}</li>
 * <li>Root combining algorithm: {@link PolicyOnlyOneApplicableAlgorithm}</li>
 * <li>PIP: <code>null</code> (No Policy Information Point used)</li>
 * <li>Data types used: see {@link DataTypesJAXBInitializer}</li>
 * <li>Functions used: see {@link DataTypesJAXBInitializer}</li>
 * <li>Policy Combining Algorithms used: see
 * {@link PolicyCombiningAlgorithmsJAXBInitializer}</li>
 * <li>Rule Combining Algorithms used: see
 * {@link RuleCombiningAlgorithmsJAXBInitializer}</li>
 * <li>JAXB Marshaller/Unmarshaller used: see
 * {@link ContextAndPolicyInitializer}
 * </ul>
 * <b>Usage:</b><br />
 * <blockquote><code><pre>
 * PDP simplePDP = SimplePDPFactory.getSimplePDP();
 * </pre></code></blockquote>
 * 
 * <b>Note:</b><br />
 * All getSimplePDP(...) methods exist in two ways. On with a flag
 * {@code respectAbandonedEvaluatables } and one without this flag. If this flag
 * is set to true the returning {@link PDP} respectes abandoned Evaluatables. <br />
 * <br />
 * <b>Abandoned Evaluatables</b><br />
 * When rule combining algorithms, which can optimize the evaluation process,
 * are used (e.g. DenyOverridesCombining) it could be that not all obligations
 * are processed. If an algorithm has evaluated a first result which overrides
 * the other results, it is not necessary to process all other policies. But
 * then the obligations of these non-processed policies will abandon.<br />
 * <br />
 * By calling this method the behavior of HERAS-AF combining algorithms will be
 * changed, so that all obligations are collected. This can lead to a
 * performance decrease in evaluation.<br />
 * <br />
 * We decided this default (false) because of the pseudo-code description in the
 * XACML 2.0 specification which does not respect abandoned obligations.<br />
 * <br />
 * See also:
 * <ul>
 * <li>
 * <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20" >
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 132, "Appendix C. Combining algorithms (normative)", for
 * further information.</li>
 * <li><a href="http://forum.herasaf.org/index.php/topic,3.0.html">Discussion
 * about abandoned obligations</a> in the HERAS-AF Forum</li>
 * </ul>
 * 
 * @see {@link PDP}, {@link SimplePDP}, {@link PolicyRetrievalPoint},
 *      {@link MapBasedSimplePolicyRepository}, {@link Initializer}
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public final class SimplePDPFactory {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SimplePDPFactory.class);

	private static Set<Initializer> initializers;

	/**
	 * The constructor is private to avoid instantiation of the factory. It is
	 * intended to be used in a static way.
	 */
	private SimplePDPFactory() {
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
		SimplePDPFactory.initializers = initalizers;
	}

	/**
	 * Resets the factory to only use the default initializers.
	 */
	public static void resetInitializers() {
		SimplePDPFactory.initializers = getDefaultInitializers();
	}

	/**
	 * Returns a new {@link SimplePDP} instance that has set a custom
	 * {@link SimplePDPConfiguration}.
	 * 
	 * @param simplePDPConfiguration
	 *            The root {@link SimplePDPConfiguration} to use in the
	 *            {@link PDP}.
	 * 
	 * @return The created {@link PDP}.
	 */
	public static PDP getSimplePDP(SimplePDPConfiguration simplePDPConfiguration) {
		runInitializers();
		return new SimplePDP(simplePDPConfiguration);
	}

	/**
	 * Returns a new {@link SimplePDP} instance that has set a custom
	 * {@link SimplePDPConfiguration} that will be handed over to the given
	 * custom PDP instance.
	 * 
	 * @param simplePDPConfiguration
	 *            The root {@link SimplePDPConfiguration} to use in the
	 *            {@link PDP}.
	 * @param customPDP
	 *            The {@link Class} of the custom PDP to use. This PDP must
	 *            extend {@link SimplePDP}.
	 * 
	 * @return The created {@link PDP}.
	 */
	public static PDP getSimplePDP(
			SimplePDPConfiguration simplePDPConfiguration,
			Class<? extends SimplePDP> customPDP) {
		runInitializers();
		try {
			Constructor<? extends SimplePDP> constructor = customPDP
					.getConstructor(SimplePDPConfiguration.class);
			SimplePDP pdp = constructor.newInstance(simplePDPConfiguration);
			return pdp;
		} catch (SecurityException e) {
			InitializationException ie = new InitializationException(
					"Cannot create custom PDP: " + customPDP.getCanonicalName(),
					e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (NoSuchMethodException e) {
			InitializationException ie = new InitializationException(
					"Cannot create custom PDP: " + customPDP.getCanonicalName(),
					e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (IllegalArgumentException e) {
			InitializationException ie = new InitializationException(
					"Cannot create custom PDP: " + customPDP.getCanonicalName(),
					e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (InstantiationException e) {
			InitializationException ie = new InitializationException(
					"Cannot create custom PDP: " + customPDP.getCanonicalName(),
					e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (IllegalAccessException e) {
			InitializationException ie = new InitializationException(
					"Cannot create custom PDP: " + customPDP.getCanonicalName(),
					e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (InvocationTargetException e) {
			InitializationException ie = new InitializationException(
					"Cannot create custom PDP: " + customPDP.getCanonicalName(),
					e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}
	}

	/**
	 * Returns a new {@link SimplePDP} instance that has set a custom root
	 * {@link PolicyCombiningAlgorithm}. <br />
	 * <br />
	 * <b>Important:</b> The default {@link SimplePDPConfiguration} with default
	 * values in all other configuration fields (except
	 * {@link PolicyCombiningAlgorithm} is used.
	 * 
	 * @param rootCombiningAlgorithm
	 *            The root {@link PolicyCombiningAlgorithm} to use in the
	 *            {@link PDP}.
	 * 
	 * <br />
	 * @see {@link SimplePDPConfiguration}
	 * 
	 * @return The created {@link PDP}.
	 */
	public static PDP getSimplePDP(
			PolicyCombiningAlgorithm rootCombiningAlgorithm) {
		SimplePDPConfiguration simplePDPConfiguration = new SimplePDPConfiguration();
		simplePDPConfiguration
				.setRootCombiningAlgorithm(rootCombiningAlgorithm);
		return getSimplePDP(simplePDPConfiguration);
	}

	/**
	 * Returns a new {@link SimplePDP} instance that has set a custom
	 * {@link PolicyRetrievalPoint}. <br />
	 * <br />
	 * <b>Important:</b> The default {@link SimplePDPConfiguration} with default
	 * values in all other configuration fields (except
	 * {@link PolicyRetrievalPoint} is used.
	 * 
	 * @param policyRetrievalPoint
	 *            The root {@link PolicyRetrievalPoint} to use in the
	 *            {@link PDP}.
	 * 
	 * <br />
	 * @see {@link SimplePDPConfiguration}
	 * 
	 * @return The created {@link PDP}.
	 */
	public static PDP getSimplePDP(PolicyRetrievalPoint policyRetrievalPoint) {
		SimplePDPConfiguration simplePDPConfiguration = new SimplePDPConfiguration();
		simplePDPConfiguration.setPolicyRetrievalPoint(policyRetrievalPoint);
		return getSimplePDP(simplePDPConfiguration);
	}

	/**
	 * Returns a new {@link SimplePDP} instance that has set a custom root
	 * {@link PolicyCombiningAlgorithm}. <br />
	 * and a custom {@link PolicyRetrievalPoint}. <br />
	 * <br />
	 * <b>Important:</b> The default {@link SimplePDPConfiguration} with default
	 * values in all other configuration fields (except
	 * {@link PolicyRetrievalPoint} and {@link PolicyCombiningAlgorithm} is used.
	 * 
	 * @param rootCombiningAlgorithm
	 *            The root {@link PolicyCombiningAlgorithm} to use in the
	 *            {@link PDP}.
	 * @param policyRetrievalPoint
	 *            The root {@link PolicyRetrievalPoint} to use in the
	 *            {@link PDP}.
	 * 
	 * <br />
	 * @see {@link SimplePDPConfiguration}
	 * 
	 * @return The created {@link PDP}.
	 */
	public static PDP getSimplePDP(
			PolicyCombiningAlgorithm rootCombiningAlgorithm,
			PolicyRetrievalPoint policyRetrievalPoint) {
		SimplePDPConfiguration simplePDPConfiguration = new SimplePDPConfiguration();
		simplePDPConfiguration
				.setRootCombiningAlgorithm(rootCombiningAlgorithm);
		simplePDPConfiguration.setPolicyRetrievalPoint(policyRetrievalPoint);
		return getSimplePDP(simplePDPConfiguration);
	}

	/**
	 * Returns a new {@link SimplePDP} instance. <br />
	 * <br />
	 * <b>Important:</b> All default values defined in
	 * {@link SimplePDPConfiguration} will be used in the resulting PDP.
	 * 
	 * @return The created {@link PDP}.
	 */
	public static PDP getSimplePDP() {
		return getSimplePDP(new SimplePDPConfiguration());
	}

	/**
	 * Runs all {@link Initializer}s. If no custom initializers were set then
	 * use the default initializers as retrieved by
	 * {@link #getDefaultInitializers()}.
	 */
	private static void runInitializers() {
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