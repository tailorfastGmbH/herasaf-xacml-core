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

package org.herasaf.xacml.core.simplePDP;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.PIP;
import org.herasaf.xacml.core.api.PolicyRepository;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyOnlyOneApplicableAlgorithm;
import org.herasaf.xacml.core.simplePDP.initializers.ContextAndPolicyInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.DataTypesInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.FunctionsInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.Initializer;
import org.herasaf.xacml.core.simplePDP.initializers.PolicyCombiningAlgorithmsInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.RuleCombiningAlgorithmsInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO REVIEW René.
 * 
 * This factory is responsible of creating a {@link PDP}. All needed
 * initialization is done automatically. Various hooks are defined to enhance
 * the factory with further capabilities.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public final class SimplePDPFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(SimplePDPFactory.class);
	private static boolean respectAbandonedEvaluatables;
	private static List<Initializer> initializers = new ArrayList<Initializer>();
	private static Class<? extends PolicyCombiningAlgorithm> defaultRootCombiningAlgorithm = PolicyOnlyOneApplicableAlgorithm.class;
	private static Class<? extends PolicyRepository> defaultPolicyRepository = MapBasedSimplePolicyRepository.class;

	/**
	 * The constructor is private to avoid instantiation of the factory. It is
	 * intended to be used in a static manner.
	 */
	private SimplePDPFactory() {

	}

	/**
	 * TODO REVIEW René.
	 * 
	 * A user has the possibility to use his/her own initializers to instantiate
	 * functions, data types, combining algorihtms and JAXB.
	 * 
	 * @param initalizers
	 *            A list containing the initializers that shall run when
	 *            retrieving a new {@link PDP}.
	 */
	public static void setInitalizers(List<Initializer> initalizers) {
		LOGGER.info("Custom initializers are in use.");
		SimplePDPFactory.initializers = initalizers;
	}

	/**
	 * TODO REVIEW René.
	 * 
	 * Enables all policy combining algorithms to respect abandoned
	 * evaluatables. By default abandoned evaluatables are not respected.
	 */
	public static void respectAbandonedEvaluatables() {
		respectAbandonedEvaluatables = true;
	}

	/**
	 * TODO REVIEW René.
	 * 
	 * Custom properties can be set for the initializers.
	 * 
	 * <b>Note:</b><br />
	 * The function-, data type- and combining algorithm initializers respect
	 * the property <code>org:herasaf:xacml.core:jarPath</code>.
	 * 
	 * @param initializerClass
	 *            The class of the initializer for which the property is.
	 * @param id
	 *            The id of the property.
	 * @param value
	 *            The value of the property.
	 */
	public static void setProperty(Class<? extends Initializer> initializerClass, String id, Object value) {
		for (Initializer initializer : initializers) {
			if (initializer.getClass().equals(initializerClass)) {
				initializer.setProperty(id, value);
			}
		}
	}

	/**
	 * TODO REVIEW René.
	 * 
	 * Enables all default initializers to initialize functions, data types,
	 * combining algorithms and JAXB. <br />
	 * By default the default initializers are not in use.
	 */
	public static void useDefaultInitializers() {
		LOGGER.info("The default initializers are in use.");
		initializers.add(new FunctionsInitializer());
		initializers.add(new DataTypesInitializer());
		initializers.add(new RuleCombiningAlgorithmsInitializer());
		initializers.add(new PolicyCombiningAlgorithmsInitializer(respectAbandonedEvaluatables));
		initializers.add(new ContextAndPolicyInitializer());
	}

	/**
	 * TODO REVIEW René.
	 * 
	 * Returns a new {@link PDP} instance that has set a custom root
	 * {@link PolicyCombiningAlgorithm}, a custom {@link PolicyRepository} and a
	 * custom {@link PIP}.
	 * 
	 * @param rootCombiningAlgorithm
	 *            The root {@link PolicyCombiningAlgorithm} to use in the
	 *            {@link PDP}.
	 * @param policyRepository
	 *            The {@link PolicyRepository} to use in the {@link PDP}.
	 * @param pip
	 *            The {@link PIP} to use in the {@link PDP}.
	 * 
	 * @return The created {@link PDP}.
	 */
	public static PDP getSimplePDP(PolicyCombiningAlgorithm rootCombiningAlgorithm, PolicyRepository policyRepository,
			PIP pip) {
		if (rootCombiningAlgorithm == null || policyRepository == null) {
			InitializationException e = new InitializationException(
					"The root combining algorithm and the policy repository must not be null.");
			LOGGER.error(e.getMessage());
			throw e;
		}

		if (initializers == null) {
			InitializationException e = new InitializationException(
					"SimplePDPFactory is not initialized. Initializers must be set.");
			LOGGER.error(e.getMessage());
			throw e;
		}
		runInitializers();

		rootCombiningAlgorithm.setRespectAbandondEvaluatables(respectAbandonedEvaluatables);

		return new SimplePDP(rootCombiningAlgorithm, policyRepository, pip);
	}

	/**
	 * TODO REVIEW René.
	 * 
	 * Returns a new {@link PDP} instance that has set a custom root
	 * {@link PolicyCombiningAlgorithm} and a custom {@link PIP}. The default
	 * {@link PolicyRepository} ({@link MapBasedSimplePolicyRepository}) is
	 * used.
	 * 
	 * @param rootCombiningAlgorithm
	 *            The root {@link PolicyCombiningAlgorithm} to use in the
	 *            {@link PDP}.
	 * @param pip
	 *            The {@link PIP} to use in the {@link PDP}.
	 * 
	 * @return The created {@link PDP}.
	 */
	public static PDP getSimplePDP(PolicyCombiningAlgorithm rootCombiningAlgorithm, PIP pip) {
		PolicyRepository policyRepository;

		try {
			policyRepository = defaultPolicyRepository.getConstructor(boolean.class).newInstance(
					rootCombiningAlgorithm.isOrderedCombiningAlgorithm());
		} catch (InstantiationException e) {
			InitializationException ie = new InitializationException("Unable to instantiate the policy repository: "
					+ defaultPolicyRepository.getCanonicalName());
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (IllegalAccessException e) {
			InitializationException ie = new InitializationException("Unable to instantiate the policy repository: "
					+ defaultPolicyRepository.getCanonicalName());
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (IllegalArgumentException e) {
			InitializationException ie = new InitializationException("Unable to instantiate the policy repository: "
					+ defaultPolicyRepository.getCanonicalName());
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (SecurityException e) {
			InitializationException ie = new InitializationException("Unable to instantiate the policy repository: "
					+ defaultPolicyRepository.getCanonicalName());
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (InvocationTargetException e) {
			InitializationException ie = new InitializationException("Unable to instantiate the policy repository: "
					+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (NoSuchMethodException e) {
			InitializationException ie = new InitializationException("Unable to instantiate the policy repository: "
					+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}

		LOGGER.info("Using the default policy repository: {}", defaultPolicyRepository.getCanonicalName());
		LOGGER.warn("The default policy repository is in use. This must not be used in a productive environment.");

		return getSimplePDP(rootCombiningAlgorithm, policyRepository, pip);
	}

	/**
	 * TODO REVIEW René.
	 * 
	 * Returns a new {@link PDP} instance that has set a custom a custom
	 * {@link PolicyRepository} and a custom {@link PIP}. The default root
	 * {@link PolicyCombiningAlgorithm} (
	 * {@link PolicyOnlyOneApplicableAlgorithm}) is used.
	 * 
	 * @param policyRepository
	 *            The {@link PolicyRepository} to use in the {@link PDP}.
	 * @param pip
	 *            The {@link PIP} to use in the {@link PDP}.
	 * 
	 * @return The created {@link PDP}.
	 */
	public static PDP getSimplePDP(PolicyRepository policyRepository, PIP pip) {
		PolicyCombiningAlgorithm rootCombiningAlgorithm;

		try {
			rootCombiningAlgorithm = defaultRootCombiningAlgorithm.newInstance();
		} catch (InstantiationException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default root combining algorithm: "
							+ defaultRootCombiningAlgorithm.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (IllegalAccessException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default root combining algorithm: "
							+ defaultRootCombiningAlgorithm.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}

		LOGGER
				.info("Using the default root combining algorithm : {}", defaultRootCombiningAlgorithm
						.getCanonicalName());

		return getSimplePDP(rootCombiningAlgorithm, policyRepository, pip);
	}

	/**
	 * TODO REVIEW René.
	 * 
	 * Returns a new {@link PDP} instance that has set a custom root
	 * {@link PolicyCombiningAlgorithm}. The default {@link PIP} (
	 * <code>null</code>) and the default {@link PolicyRepository} (
	 * {@link MapBasedSimplePolicyRepository}) is used.
	 * 
	 * @param rootCombiningAlgorithm
	 *            The root {@link PolicyCombiningAlgorithm} to use in the
	 *            {@link PDP}.
	 * 
	 * @return The created {@link PDP}.
	 */
	public static PDP getSimplePDP(PolicyCombiningAlgorithm rootCombiningAlgorithm) {
		PolicyRepository policyRepository;

		try {
			policyRepository = defaultPolicyRepository.getConstructor(boolean.class).newInstance(
					rootCombiningAlgorithm.isOrderedCombiningAlgorithm());
		} catch (InstantiationException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (IllegalAccessException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (IllegalArgumentException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (SecurityException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (InvocationTargetException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (NoSuchMethodException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}

		LOGGER.info("There is no Policy Information Point (PIP) in use.");
		LOGGER.info("Using the default policy repository: {}", defaultPolicyRepository.getCanonicalName());
		LOGGER.warn("The default policy repository is in use. This must not be used in a productive environment.");

		return getSimplePDP(rootCombiningAlgorithm, policyRepository, null);
	}

	/**
	 * TODO REVIEW René.
	 * 
	 * Returns a new {@link PDP} instance that has set a custom root
	 * {@link PolicyRepository}. The default root
	 * {@link PolicyCombiningAlgorithm} (
	 * {@link PolicyOnlyOneApplicableAlgorithm}) and the default {@link PIP} (
	 * <code>null</code>) is used.
	 * 
	 * @param policyRepository
	 *            The {@link PolicyRepository} to use in the {@link PDP}.
	 * 
	 * @return The created {@link PDP}.
	 */
	public static PDP getSimplePDP(PolicyRepository policyRepository) {
		PolicyCombiningAlgorithm rootCombiningAlgorithm;
		try {
			rootCombiningAlgorithm = defaultRootCombiningAlgorithm.newInstance();
		} catch (InstantiationException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default root combining algorithm: "
							+ defaultRootCombiningAlgorithm.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (IllegalAccessException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default root combining algorithm: "
							+ defaultRootCombiningAlgorithm.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}

		LOGGER.info("There is no Policy Information Point (PIP) in use.");
		LOGGER
				.info("Using the default root combining algorithm : {}", defaultRootCombiningAlgorithm
						.getCanonicalName());

		return getSimplePDP(rootCombiningAlgorithm, policyRepository, null);
	}

	/**
	 * TODO REVIEW René.
	 * 
	 * Returns a new {@link PDP} instance that has set a custom root {@link PIP}
	 * . The default root {@link PolicyCombiningAlgorithm} (
	 * {@link PolicyOnlyOneApplicableAlgorithm}) and the default
	 * {@link PolicyRepository} ({@link MapBasedSimplePolicyRepository}) is
	 * used.
	 * 
	 * @param pip
	 *            The {@link PIP} to use in the {@link PDP}.
	 * 
	 * @return The created {@link PDP}.
	 */
	public static PDP getSimplePDP(PIP pip) {
		PolicyCombiningAlgorithm rootCombiningAlgorithm;
		PolicyRepository policyRepository;

		try {
			rootCombiningAlgorithm = defaultRootCombiningAlgorithm.newInstance();
		} catch (InstantiationException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default root combining algorithm: "
							+ defaultRootCombiningAlgorithm.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (IllegalAccessException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default root combining algorithm: "
							+ defaultRootCombiningAlgorithm.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}

		try {
			policyRepository = defaultPolicyRepository.getConstructor(boolean.class).newInstance(
					rootCombiningAlgorithm.isOrderedCombiningAlgorithm());

		} catch (InstantiationException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (IllegalAccessException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (IllegalArgumentException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (SecurityException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (InvocationTargetException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (NoSuchMethodException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}

		LOGGER
				.info("Using the default root combining algorithm : {}", defaultRootCombiningAlgorithm
						.getCanonicalName());
		LOGGER.info("Using the default policy repository: {}", defaultPolicyRepository.getCanonicalName());
		LOGGER.warn("The default policy repository is in use. This must not be used in a productive environment.");

		return getSimplePDP(rootCombiningAlgorithm, policyRepository, pip);
	}

	/**
	 * TODO REVIEW René.
	 * 
	 * Returns a new {@link PDP} instance that has set a custom root
	 * {@link PolicyCombiningAlgorithm} and a custom {@link PolicyRepository}.
	 * The default {@link PIP} (<code>null</code>) is used.
	 * 
	 * @param rootCombiningAlgorithm
	 *            The root {@link PolicyCombiningAlgorithm} to use in the
	 *            {@link PDP}.
	 * @param policyRepository
	 *            The {@link PolicyRepository} to use in the {@link PDP}.
	 * 
	 * @return The created {@link PDP}.
	 */
	public static PDP getSimplePDP(PolicyCombiningAlgorithm rootCombiningAlgorithm, PolicyRepository policyRepository) {

		LOGGER.info("There is no Policy Information Point (PIP) in use.");

		return getSimplePDP(rootCombiningAlgorithm, policyRepository, null);
	}

	/**
	 * TODO REVIEW René.
	 * 
	 * Returns a new {@link PDP} instance that has set the default root
	 * {@link PolicyCombiningAlgorithm} (
	 * {@link PolicyOnlyOneApplicableAlgorithm}), the default
	 * {@link PolicyRepository} ({@link MapBasedSimplePolicyRepository}) and the
	 * default {@link PIP} (<code>null</code>).
	 * 
	 * @return The created {@link PDP}.
	 */
	public static PDP getSimplePDP() {
		PolicyCombiningAlgorithm rootCombiningAlgorithm;
		PolicyRepository policyRepository;

		try {
			rootCombiningAlgorithm = defaultRootCombiningAlgorithm.newInstance();
		} catch (InstantiationException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default root combining algorithm: "
							+ defaultRootCombiningAlgorithm.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (IllegalAccessException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default root combining algorithm: "
							+ defaultRootCombiningAlgorithm.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}

		try {
			policyRepository = defaultPolicyRepository.getConstructor(boolean.class).newInstance(
					rootCombiningAlgorithm.isOrderedCombiningAlgorithm());

		} catch (InstantiationException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (IllegalAccessException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (IllegalArgumentException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (SecurityException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (InvocationTargetException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		} catch (NoSuchMethodException e) {
			InitializationException ie = new InitializationException(
					"Unable to instantiate the default policy repository: "
							+ defaultPolicyRepository.getCanonicalName(), e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}

		LOGGER.info("There is no Policy Information Point (PIP) in use.");
		LOGGER
				.info("Using the default root combining algorithm : {}", defaultRootCombiningAlgorithm
						.getCanonicalName());
		LOGGER.info("Using the default policy repository: {}", defaultPolicyRepository.getCanonicalName());
		LOGGER.warn("The default policy repository is in use. This must not be used in a productive environment.");

		return getSimplePDP(rootCombiningAlgorithm, policyRepository, null);
	}

	/**
	 * TODO REVIEW René.
	 * 
	 * Runs all {@link Initializer}s sequentially.
	 */
	private static void runInitializers() {
		for (Initializer initializer : initializers) {
			initializer.run();
		}
	}
}