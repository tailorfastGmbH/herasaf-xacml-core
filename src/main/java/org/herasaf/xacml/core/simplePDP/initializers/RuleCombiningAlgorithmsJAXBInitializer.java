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

import org.herasaf.xacml.core.combiningAlgorithm.rule.AbstractRuleCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleCombiningAlgorithm;
import org.herasaf.xacml.core.converter.RuleCombiningAlgorithmJAXBTypeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This initializer initializes all rule combining algorithms and puts them in
 * the {@link RuleCombiningAlgorithmJAXBTypeAdapter} JAXB type adapter.
 * 
 * @author Florian Huonder
 * @author Ren� Eggenschwiler
 */
public class RuleCombiningAlgorithmsJAXBInitializer extends
		AbstractInitializer<AbstractRuleCombiningAlgorithm> {
	private transient static Logger logger = LoggerFactory
			.getLogger(RuleCombiningAlgorithmsJAXBInitializer.class);
	private static final String SEARCH_CONTEXT_PATH = "org/herasaf/xacml/core/combiningAlgorithm/rule/impl";
	private static final Class<AbstractRuleCombiningAlgorithm> TARGET_CLASS = AbstractRuleCombiningAlgorithm.class;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getDefaultSearchContextPath() {
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
	protected void setInstancesIntoConverter(
			Map<String, AbstractRuleCombiningAlgorithm> instancesMap) {
		Map<String, RuleCombiningAlgorithm> instances = new HashMap<String, RuleCombiningAlgorithm>();
		instances.putAll(instancesMap);
		RuleCombiningAlgorithmJAXBTypeAdapter.setCombiningAlgorithms(instances);
		logger.info("{} rule combining algorithms are initialized.", instances
				.size());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<AbstractRuleCombiningAlgorithm> getTargetClass() {
		return TARGET_CLASS;
	}

	/** {@inheritDoc} */
	public int hashCode() {
		return getClass().getName().hashCode();
	}

	/** {@inheritDoc} */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof RuleCombiningAlgorithmsJAXBInitializer)) {
			return false;
		}

		return hashCode() == obj.hashCode();
	}
}
