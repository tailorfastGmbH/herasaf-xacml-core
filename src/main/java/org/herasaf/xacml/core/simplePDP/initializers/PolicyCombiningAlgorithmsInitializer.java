/*
 * Copyright 2009-2010 HERAS-AF (www.herasaf.org)
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
import java.util.Map;
import java.util.Set;

import org.herasaf.xacml.core.combiningAlgorithm.policy.AbstractPolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.converter.URNToPolicyCombiningAlgorithmConverter;
import org.herasaf.xacml.core.targetMatcher.TargetMatcher;
import org.herasaf.xacml.core.targetMatcher.impl.TargetMatcherImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This initializer initializes all policy combining algorithms and puts them in
 * the {@link URNToPolicyCombiningAlgorithmConverter} JAXB type adapter.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class PolicyCombiningAlgorithmsInitializer extends AbstractInitializer<AbstractPolicyCombiningAlgorithm> {
	private static Logger logger = LoggerFactory.getLogger(PolicyCombiningAlgorithmsInitializer.class);
	private static final String SEARCH_CONTEXT = "org.herasaf.xacml.core.combiningAlgorithm.policy.impl";
	private static final String SEARCH_CONTEXT_PATH = "org/herasaf/xacml/core/combiningAlgorithm/policy/impl";
	private static final Class<AbstractPolicyCombiningAlgorithm> TARGET_CLASS = AbstractPolicyCombiningAlgorithm.class;
	// The default target matcher.
	private final TargetMatcher targetMatcher = new TargetMatcherImpl();

	
	/**
	 * This method sets the flag in all combining algorithms if the abandoned
	 * evaluatables shall be respected. Further it sets the default
	 * {@link TargetMatcher} into the combining algorithms.
	 * 
	 * @param instances
	 *            The instances of type T.
	 */
	@Override
	protected void furtherInitializations(Set<AbstractPolicyCombiningAlgorithm> instances) {
		for (AbstractPolicyCombiningAlgorithm algorithm : instances) {
			algorithm.setTargetMatcher(targetMatcher);
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
	 * {@inheritDoc}.
	 */
	@Override
	protected Class<AbstractPolicyCombiningAlgorithm> getTargetClass() {
		return TARGET_CLASS;
	}
}
