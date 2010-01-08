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
 * <li>respectAbandonedObligations: false (see:
 * {@link #respectAbandonedEvaluatables()})</li>
 * <li>Data types used: see {@link DataTypesInitializer}</li>
 * <li>Functions used: see {@link DataTypesInitializer}</li>
 * <li>Policy Combining Algorithms used: see
 * {@link PolicyCombiningAlgorithmsInitializer}</li>
 * <li>Rule Combining Algorithms used: see
 * {@link RuleCombiningAlgorithmsInitializer}</li>
 * <li>JAXB Marshaller/Unmarshaller used: see
 * {@link ContextAndPolicyInitializer}
 * </ul>
 * <b>Usage:</b><br />
 * <blockquote><code><pre>
 * SimplePDPFactory.useDefaultInitializers();       // 1. Setup the factory to use the default 
 *                                                                      configuration/initializers <br />
 * PDP simplePDP = SimplePDPFactory.getSimplePDP(); // 2. Retrieve an instance of a PDP </pre>
 * </code>
 * </blockquote>
 * 
 * @see {@link PDP}, {@link SimplePDP}, {@link PolicyRepository},
 *      {@link MapBasedSimplePolicyRepository}, {@link Initializer}
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public final class SimplePDPFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimplePDPFactory.class);
    private static boolean respectAbandonedEvaluatables = false;
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
     * Extension point for usage of custom initializers to instantiate
     * functions, data types, combining algorithms and JAXB. <br />
     * <br />
     * If other initializers than the default initializers are needed. The
     * custom list should be created and handed over to this method. <br />
     * <br />
     * If custom initializers are used the <code>useDefaultInitializers()</code>
     * must not be called!
     * 
     * @param initalizers
     *            A list containing the initializers that shall run when
     *            retrieving a new {@link PDP}.
     * 
     * @see {@link #useDefaultInitializers()}
     */
    public static void setInitalizers(List<Initializer> initalizers) {
        LOGGER.info("Custom initializers are in use.");
        SimplePDPFactory.initializers = initalizers;
    }

    /**
     * Enables all policy combining algorithms to respect abandoned
     * evaluatables. <br />
     * By default abandoned evaluatables are not respected. <br />
     * <br />
     * When rule combining algorithms, which can optimize the evaluation
     * process, are used (e.g. DenyOverridesCombining) it could be that not all
     * obligations are processed. If an algorithm has evaluated a first result
     * which overrides the other results, it is not necessary to process all
     * other policies. But then the obligations of these non-processed policies
     * will abandon.<br />
     * <br />
     * By calling this method the behavior of HERAS-AF combining algorithms will
     * be changed, so that all obligations are collected. This can lead to a
     * performance decrease in evaluation.<br />
     * <br />
     * By default <code>respectAbanondedObligations</code> will be
     * <code>false</code>. We decided this default because of the pseudo-code
     * description in the XACML 2.0 specification which does not respect
     * abandoned obligations.<br />
     * <br />
     * See also:
     * <ul>
     * <li>
     * <a href=
     * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20"
     * > OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29
     * June 2006</a> page 132, "Appendix C. Combining algorithms (normative)",
     * for further information.</li>
     * <li><a
     * href="http://forum.herasaf.org/index.php/topic,3.0.html">Discussion about
     * abandoned obligations</a> in the HERAS-AF Forum</li>
     * </ul>
     */
    public static void respectAbandonedEvaluatables() {
        respectAbandonedEvaluatables = true;
    }

    /**
     * Enables all default initializers to initialize functions, data types,
     * combining algorithms and JAXB. <br />
     * <br />
     * By default the default initializers are not in use.<br />
     * <br />
     * If the default initializers will be used (by calling this method), the
     * factory will be configured to use {@link FunctionsInitializer},
     * {@link DataTypesInitializer}, {@link RuleCombiningAlgorithmsInitializer},
     * {@link PolicyCombiningAlgorithmsInitializer}, and
     * {@link ContextAndPolicyInitializer}.
     * 
     * @see {@link #setInitalizers(List)}
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
     * Returns a new {@link SimplePDP} instance that has set a custom root
     * {@link PolicyCombiningAlgorithm}, a custom {@link PolicyRepository} and a
     * custom {@link PIP}.
     * 
     * @param rootCombiningAlgorithm
     *            The root {@link PolicyCombiningAlgorithm} to use in the
     *            {@link PDP}.
     * @param policyRepository
     *            The {@link PolicyRepository} to use in the {@link PDP}.
     * @param pip
     *            The {@link PIP} (may be <code>null</code>) to use in the
     *            {@link PDP}.
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
     * Returns a new {@link SimplePDP} instance that has set a custom root
     * {@link PolicyCombiningAlgorithm} and a custom {@link PIP}. <br />
     * <br />
     * <b>Important:</b> The default {@link PolicyRepository} (
     * {@link MapBasedSimplePolicyRepository}) is used.
     * 
     * @param rootCombiningAlgorithm
     *            The root {@link PolicyCombiningAlgorithm} to use in the
     *            {@link PDP}.
     * @param pip
     *            The {@link PIP} (may be <code>null</code>) to use in the
     *            {@link PDP} .
     * 
     * @return The created {@link PDP}.
     */
    public static PDP getSimplePDP(PolicyCombiningAlgorithm rootCombiningAlgorithm, PIP pip) {
        PolicyRepository policyRepository;

        try {
            policyRepository = defaultPolicyRepository.newInstance();
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
        }

        LOGGER.info("Using the default policy repository: {}", defaultPolicyRepository.getCanonicalName());

        return getSimplePDP(rootCombiningAlgorithm, policyRepository, pip);
    }

    /**
     * Returns a new {@link SimplePDP} instance that has set a custom a custom
     * {@link PolicyRepository} and a custom {@link PIP} (may be
     * <code>null</code>). <br />
     * <br />
     * <b>Important:</b> The default root {@link PolicyCombiningAlgorithm} (
     * {@link PolicyOnlyOneApplicableAlgorithm}) is used.
     * 
     * @param policyRepository
     *            The {@link PolicyRepository} to use in the {@link PDP}.
     * @param pip
     *            The {@link PIP} (may be <code>null</code>) to use in the
     *            {@link PDP} .
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
     * Returns a new {@link SimplePDP} instance that has set a custom root
     * {@link PolicyCombiningAlgorithm}. <br />
     * <br />
     * <b>Important:</b> The default {@link PIP} ( <code>null</code>) and the
     * default {@link PolicyRepository} ( {@link MapBasedSimplePolicyRepository}
     * ) is used.
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
            policyRepository = defaultPolicyRepository.newInstance();
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
        }

        LOGGER.info("There is no Policy Information Point (PIP) in use.");
        LOGGER.info("Using the default policy repository: {}", defaultPolicyRepository.getCanonicalName());

        return getSimplePDP(rootCombiningAlgorithm, policyRepository, null);
    }

    /**
     * Returns a new {@link SimplePDP} instance that has set a custom root
     * {@link PolicyRepository}. <br />
     * <br />
     * <b>Important:</b> The default root {@link PolicyCombiningAlgorithm} (
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
     * Returns a new {@link SimplePDP} instance that has set a custom
     * {@link PIP}. <br />
     * <br />
     * <b>Important:</b> The default root {@link PolicyCombiningAlgorithm} (
     * {@link PolicyOnlyOneApplicableAlgorithm}) and the default
     * {@link PolicyRepository} ({@link MapBasedSimplePolicyRepository}) is
     * used.
     * 
     * @param pip
     *            The {@link PIP} (may be <code>null</code) to use in the
     *            {@link PDP}.
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
            policyRepository = defaultPolicyRepository.newInstance();

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
        }

        LOGGER
                .info("Using the default root combining algorithm : {}", defaultRootCombiningAlgorithm
                        .getCanonicalName());
        LOGGER.info("Using the default policy repository: {}", defaultPolicyRepository.getCanonicalName());

        return getSimplePDP(rootCombiningAlgorithm, policyRepository, pip);
    }

    /**
     * Returns a new {@link SimplePDP} instance that has set a custom root
     * {@link PolicyCombiningAlgorithm} and a custom {@link PolicyRepository}. <br />
     * <br />
     * <b>Important:</b> The default {@link PIP} (<code>null</code>) is used.
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
     * Returns a new {@link SimplePDP} instance. <br />
     * <br />
     * <b>Important:</b> The default root {@link PolicyCombiningAlgorithm} (
     * {@link PolicyOnlyOneApplicableAlgorithm}), the default
     * {@link PolicyRepository} ({@link MapBasedSimplePolicyRepository}) and the
     * default {@link PIP} (<code>null</code>) is used.
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
            policyRepository = defaultPolicyRepository.newInstance();

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
        }

        LOGGER.info("There is no Policy Information Point (PIP) in use.");
        LOGGER
                .info("Using the default root combining algorithm : {}", defaultRootCombiningAlgorithm
                        .getCanonicalName());
        LOGGER.info("Using the default policy repository: {}", defaultPolicyRepository.getCanonicalName());

        return getSimplePDP(rootCombiningAlgorithm, policyRepository, null);
    }

    /**
     * Runs all {@link Initializer}s sequentially.
     */
    private static void runInitializers() {
        for (Initializer initializer : initializers) {
            initializer.run();
        }
    }
}