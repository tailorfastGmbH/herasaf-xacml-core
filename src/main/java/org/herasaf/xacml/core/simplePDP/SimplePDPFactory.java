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

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.PIP;
import org.herasaf.xacml.core.api.PolicyRepository;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyUnorderedCombiningAlgorithm;
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
 * TODO JAVADOC!!
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 * 
 */
public class SimplePDPFactory {
	private static final Logger logger = LoggerFactory
			.getLogger(SimplePDPFactory.class);
	private static boolean respectAbandonedEvaluatables;
	private static List<Initializer> initializers;
	private static Class<? extends PolicyUnorderedCombiningAlgorithm> defaultRootCombiningAlgorithm = PolicyOnlyOneApplicableAlgorithm.class;
	private static Class<? extends PolicyRepository> defaultPolicyRepository = MapBasedSimplePolicyRepository.class;

	/**
	 * TODO JAVADOC
	 * 
	 * @param initalizers
	 */
	public static void setInitalizers(List<Initializer> initalizers) {
		logger.info("Custom initializers are in use.");
		SimplePDPFactory.initializers = initalizers;
	}

	/**
	 * TODO JAVADOC
	 * 
	 * @param respect
	 */
	public static void respectAbandonedEvaluatables(boolean respect){
		respectAbandonedEvaluatables = respect;
	}
	
	/**
	 * TODO JAVADOC
	 * 
	 * @param useDefaultInitializers
	 */
	public static void useDefaultInitializers(boolean useDefaultInitializers) {
		logger.info("The default initializers are in use.");
		if (useDefaultInitializers) {
			initializers = new ArrayList<Initializer>();
			initializers.add(new FunctionsInitializer());
			initializers.add(new DataTypesInitializer());
			initializers.add(new RuleCombiningAlgorithmsInitializer());
			initializers.add(new PolicyCombiningAlgorithmsInitializer(respectAbandonedEvaluatables));
			initializers.add(new ContextAndPolicyInitializer());
		}
	}
	
	/**
	 * TODO JAVADOC
	 * 
	 * @param rootCombiningAlgorithm
	 * @param policyRepository
	 * @param pip
	 */
	public static PDP getSimplePDP(
			PolicyUnorderedCombiningAlgorithm rootCombiningAlgorithm,
			PolicyRepository policyRepository, PIP pip) {
		if(rootCombiningAlgorithm == null || policyRepository == null){
			String msg = "The root combining algorithm and the policy repository must not be null.";
			logger.error(msg);
			throw new InitializationException(msg);
		}
		
		if (initializers == null) {
			logger
					.error("SimplePDPFactory is not initialized. Initializers must be set.");
			throw new InitializationException(
					"SimplePDPFactory is not initialized. Initializers must be set.");
		}
		runInitializers();
		
		rootCombiningAlgorithm.setRespectAbandondEvaluatables(respectAbandonedEvaluatables);
		
		return new SimplePDP(rootCombiningAlgorithm, policyRepository, pip);
	}
	
	/**
	 * TODO JAVADOC
	 * 
	 * @param rootCombiningAlgorithm
	 * @param pip
	 */
	public static PDP getSimplePDP(
			PolicyUnorderedCombiningAlgorithm rootCombiningAlgorithm,PIP pip) throws InitializationException {
		PolicyRepository policyRepository;
		
		try {
			policyRepository = defaultPolicyRepository.newInstance();
		} catch (InstantiationException e) {
			String msg = "Unable to instantiate the policy repository: " + defaultPolicyRepository.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "Unable to instantiate the policy repository: " + defaultPolicyRepository.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		}
		
		logger.info("Using the default policy repository: {}", defaultPolicyRepository.getCanonicalName());
		logger.warn("The default policy repository is in use. This must not be used in a productive environment.");
		
		return getSimplePDP(rootCombiningAlgorithm, policyRepository, pip);
	}
	
	/**
	 * TODO JAVADOC
	 * 
	 * @param policyRepository
	 * @param pip
	 */
	public static PDP getSimplePDP(PolicyRepository policyRepository, PIP pip) throws InitializationException {
		PolicyUnorderedCombiningAlgorithm rootCombiningAlgorithm;
		
		try {
			rootCombiningAlgorithm = defaultRootCombiningAlgorithm.newInstance();
		} catch (InstantiationException e) {
			String msg = "Unable to instantiate the default root combining algorithm: " + defaultRootCombiningAlgorithm.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "Unable to instantiate the default root combining algorithm: " + defaultRootCombiningAlgorithm.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		}
		
		logger.info("Using the default root combining algorithm : {}", defaultRootCombiningAlgorithm.getCanonicalName());

		return getSimplePDP(rootCombiningAlgorithm, policyRepository, pip);
	}

	/**
	 * TODO JAVADOC
	 * 
	 * @param rootCombiningAlgorithm
	 */
	public static PDP getSimplePDP(
			PolicyUnorderedCombiningAlgorithm rootCombiningAlgorithm) {
		PolicyRepository policyRepository;
		
		try {
			policyRepository =  defaultPolicyRepository.newInstance();
		} catch (InstantiationException e) {
			String msg = "Unable to instantiate the default policy repository: " + defaultPolicyRepository.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "Unable to instantiate the default policy repository: " + defaultPolicyRepository.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		}
		
		logger.info("There is no Policy Information Point (PIP) in use.");
		logger.info("Using the default policy repository: {}", defaultPolicyRepository.getCanonicalName());
		logger.warn("The default policy repository is in use. This must not be used in a productive environment.");
		
		return getSimplePDP(rootCombiningAlgorithm, policyRepository, null);
	}

	/**
	 * TODO JAVADOC
	 * 
	 * @param policyRepository
	 */
	public static PDP getSimplePDP(PolicyRepository policyRepository) {
		PolicyUnorderedCombiningAlgorithm rootCombiningAlgorithm;
		try {
			rootCombiningAlgorithm = defaultRootCombiningAlgorithm.newInstance();
		} catch (InstantiationException e) {
			String msg = "Unable to instantiate the default root combining algorithm: " + defaultRootCombiningAlgorithm.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "Unable to instantiate the default root combining algorithm: " + defaultRootCombiningAlgorithm.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		}
		
		logger.info("There is no Policy Information Point (PIP) in use.");
		logger.info("Using the default root combining algorithm : {}", defaultRootCombiningAlgorithm.getCanonicalName());
		
		return getSimplePDP(rootCombiningAlgorithm, policyRepository, null);
	}

	/**
	 * TODO JAVADOC
	 * 
	 * @param pip
	 */
	public static PDP getSimplePDP(PIP pip) {
		PolicyUnorderedCombiningAlgorithm rootCombiningAlgorithm;
		PolicyRepository policyRepository;
		
		try {
			rootCombiningAlgorithm = defaultRootCombiningAlgorithm.newInstance();
		} catch (InstantiationException e) {
			String msg = "Unable to instantiate the default root combining algorithm: " + defaultRootCombiningAlgorithm.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "Unable to instantiate the default root combining algorithm: " + defaultRootCombiningAlgorithm.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		}
		
		try {
			policyRepository = defaultPolicyRepository.newInstance();
			
		} catch (InstantiationException e) {
			String msg = "Unable to instantiate the default policy repository: " + defaultPolicyRepository.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "Unable to instantiate the default policy repository: " + defaultPolicyRepository.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		}
		
		logger.info("Using the default root combining algorithm : {}", defaultRootCombiningAlgorithm.getCanonicalName());
		logger.info("Using the default policy repository: {}", defaultPolicyRepository.getCanonicalName());
		logger.warn("The default policy repository is in use. This must not be used in a productive environment.");
		
		return getSimplePDP(rootCombiningAlgorithm, policyRepository, pip);
	}

	/**
	 * TODO JAVADOC
	 * 
	 * The root combining algorithm must be unordered because it is not possible
	 * to set a position where to add the eval.
	 * 
	 * @param rootCombiningAlgorithm
	 *            The root combining algorithm of the PDP.
	 * @param policyRepository
	 *            the {@link PolicyRepository} of the PDP.
	 * @return The PDP (singleton).
	 */
	public static PDP getSimplePDP(
			PolicyUnorderedCombiningAlgorithm rootCombiningAlgorithm,
			PolicyRepository policyRepository) {
		
		logger.info("There is no Policy Information Point (PIP) in use.");
		
		return getSimplePDP(rootCombiningAlgorithm, policyRepository, null);
	}

	/**
	 * TODO JAVADOC
	 * 
	 * @return The PDP (singleton).
	 */
	public static PDP getSimplePDP() throws InitializationException {
		PolicyUnorderedCombiningAlgorithm rootCombiningAlgorithm;
		PolicyRepository policyRepository;
		
		try {
			rootCombiningAlgorithm = defaultRootCombiningAlgorithm.newInstance();
		} catch (InstantiationException e) {
			String msg = "Unable to instantiate the default root combining algorithm: " + defaultRootCombiningAlgorithm.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "Unable to instantiate the default root combining algorithm: " + defaultRootCombiningAlgorithm.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		}
		
		try {
			policyRepository = defaultPolicyRepository.newInstance();
			
		} catch (InstantiationException e) {
			String msg = "Unable to instantiate the default policy repository: " + defaultPolicyRepository.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "Unable to instantiate the default policy repository: " + defaultPolicyRepository.getCanonicalName();
			logger.error(msg);
			throw new InitializationException(msg, e);
		}
		
		logger.info("There is no Policy Information Point (PIP) in use.");
		logger.info("Using the default root combining algorithm : {}", defaultRootCombiningAlgorithm.getCanonicalName());
		logger.info("Using the default policy repository: {}", defaultPolicyRepository.getCanonicalName());
		logger.warn("The default policy repository is in use. This must not be used in a productive environment.");
		
		return getSimplePDP(rootCombiningAlgorithm, policyRepository, null);
	}

	private static void runInitializers() {
		for (Initializer initializer : initializers) {
			initializer.run();
		}
	}
}