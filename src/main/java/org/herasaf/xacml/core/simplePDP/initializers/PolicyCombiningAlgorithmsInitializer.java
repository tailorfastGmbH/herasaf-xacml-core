/*
 * Copyright 2009 HERAS-AF (www.herasaf.org)
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.combiningAlgorithm.policy.AbstractPolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.converter.URNToPolicyCombiningAlgorithmConverter;
import org.herasaf.xacml.core.targetMatcher.TargetMatcher;
import org.herasaf.xacml.core.targetMatcher.impl.TargetMatcherImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO JAVADOC!!
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 * 
 */
public class PolicyCombiningAlgorithmsInitializer extends AbstractInitializer<AbstractPolicyCombiningAlgorithm> {
	private static Logger logger = LoggerFactory.getLogger(PolicyCombiningAlgorithmsInitializer.class);
	private final static String SEARCH_CONTEXT = "org.herasaf.xacml.core.combiningAlgorithm.policy.impl";
	private final static String SEARCH_CONTEXT_PATH = "org/herasaf/xacml/core/combiningAlgorithm/policy/impl";
	private final static Class<AbstractPolicyCombiningAlgorithm> TARGET_CLASS = AbstractPolicyCombiningAlgorithm.class;
	private final TargetMatcher targetMatcher = new TargetMatcherImpl(); // The
																			// default
																			// target
																			// matcher.
	private static boolean respectAbandondEvaluatables;

	/**
	 * TODO JAVADOC
	 */
	public PolicyCombiningAlgorithmsInitializer(boolean respectAbandondEvaluatables) {
		logger.info("Respect abandoned Evaluatables: {}", respectAbandondEvaluatables);
		PolicyCombiningAlgorithmsInitializer.respectAbandondEvaluatables = respectAbandondEvaluatables;
	}

	@Override
	protected void furtherInitializations(List<AbstractPolicyCombiningAlgorithm> instances) {
		for (AbstractPolicyCombiningAlgorithm algorithm : instances) {
			algorithm.setTargetMatcher(targetMatcher);
			algorithm.setRespectAbandondEvaluatables(respectAbandondEvaluatables);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getSearchContext() {
		return SEARCH_CONTEXT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getSearchContextPath() {
		return SEARCH_CONTEXT_PATH;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getURIFromType(AbstractPolicyCombiningAlgorithm instance) {
		return instance.getCombiningAlgorithmId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setInstancesIntoConverter(Map<String, AbstractPolicyCombiningAlgorithm> instancesMap) {
		Map<String, PolicyCombiningAlgorithm> instances = new HashMap<String, PolicyCombiningAlgorithm>();
		instances.putAll(instancesMap);
		URNToPolicyCombiningAlgorithmConverter.setCombiningAlgorithms(instances);
		logger.info("{} policy combining algorithms are initialized.", instances.size());
	}

	/**
	 * TODO JAVADOC
	 */
	@Override
	protected Class<AbstractPolicyCombiningAlgorithm> getTargetClass() {
		return TARGET_CLASS;
	}
}
