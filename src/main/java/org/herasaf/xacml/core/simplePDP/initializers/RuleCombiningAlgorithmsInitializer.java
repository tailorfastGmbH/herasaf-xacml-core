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

import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleCombiningAlgorithm;
import org.herasaf.xacml.core.converter.URNToRuleCombiningAlgorithmConverter;
import org.herasaf.xacml.core.targetMatcher.TargetMatcher;
import org.herasaf.xacml.core.targetMatcher.impl.TargetMatcherImpl;

/**
 * TODO JAVADOC!!
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 * 
 */
public class RuleCombiningAlgorithmsInitializer extends
		AbstractInitializer<RuleCombiningAlgorithm> {

	private final static String SEARCH_CONTEXT = "org.herasaf.xacml.core.combiningAlgorithm.rule.impl";
	private final static String SEARCH_CONTEXT_PATH = "org/herasaf/xacml/core/combiningAlgorithm/rule/impl";
	private final static Class<RuleCombiningAlgorithm> TARGET_CLASS = RuleCombiningAlgorithm.class;
	private final TargetMatcher targetMatcher = new TargetMatcherImpl();
	
	@Override
	protected void furtherInitializations(List<RuleCombiningAlgorithm> instances) {
		for (RuleCombiningAlgorithm algorithm : instances) {
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
	protected String getURIFromType(RuleCombiningAlgorithm instance) {
		return instance.getCombiningAlgorithmId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setInstancesIntoConverter(
			Map<String, RuleCombiningAlgorithm> instancesMap) {
		URNToRuleCombiningAlgorithmConverter
				.setCombiningAlgorithms(instancesMap);
	}

	/* (non-Javadoc)
	 * @see org.herasaf.xacml.core.simplePDP.initializers.AbstractInitializer#getTargetClass()
	 */
	@Override
	protected Class<RuleCombiningAlgorithm> getTargetClass() {
		return TARGET_CLASS;
	}

}
