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
import java.util.Map;
import java.util.Set;

import org.herasaf.xacml.core.combiningAlgorithm.rule.AbstractRuleCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleCombiningAlgorithm;
import org.herasaf.xacml.core.converter.URNToRuleCombiningAlgorithmConverter;
import org.herasaf.xacml.core.targetMatcher.TargetMatcher;
import org.herasaf.xacml.core.targetMatcher.impl.TargetMatcherImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO REVIEW René.
 * 
 * This initializer initializes all rule combining algorithms and puts them in
 * the {@link URNToRuleCombiningAlgorithmConverter} JAXB type adapter.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class RuleCombiningAlgorithmsInitializer extends AbstractInitializer<AbstractRuleCombiningAlgorithm> {
	private static Logger logger = LoggerFactory.getLogger(RuleCombiningAlgorithmsInitializer.class);
	private static final String SEARCH_CONTEXT = "org.herasaf.xacml.core.combiningAlgorithm.rule.impl";
	private static final String SEARCH_CONTEXT_PATH = "org/herasaf/xacml/core/combiningAlgorithm/rule/impl";
	private static final Class<AbstractRuleCombiningAlgorithm> TARGET_CLASS = AbstractRuleCombiningAlgorithm.class;
	private final TargetMatcher targetMatcher = new TargetMatcherImpl();

	/**
	 * TODO REVIEW René.
	 * 
	 * This method sets the default {@link TargetMatcher} into the combining
	 * algorithms.
	 * 
	 * @param instances
	 *            The instances of type T.
	 */
	@Override
	protected void furtherInitializations(Set<AbstractRuleCombiningAlgorithm> instances) {
		for (AbstractRuleCombiningAlgorithm algorithm : instances) {
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
	protected String getURIFromType(AbstractRuleCombiningAlgorithm instance) {
		return instance.getCombiningAlgorithmId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setInstancesIntoConverter(Map<String, AbstractRuleCombiningAlgorithm> instancesMap) {
		Map<String, RuleCombiningAlgorithm> instances = new HashMap<String, RuleCombiningAlgorithm>();
		instances.putAll(instancesMap);
		URNToRuleCombiningAlgorithmConverter.setCombiningAlgorithms(instances);
		logger.info("{} rule combining algorithms are initialized.", instances.size());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<AbstractRuleCombiningAlgorithm> getTargetClass() {
		return TARGET_CLASS;
	}
}
