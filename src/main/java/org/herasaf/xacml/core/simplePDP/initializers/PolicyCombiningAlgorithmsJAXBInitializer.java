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

import org.herasaf.xacml.core.combiningAlgorithm.policy.AbstractPolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.converter.PolicyCombiningAlgorithmJAXBTypeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This initializer initializes all policy combining algorithms and puts them in
 * the {@link PolicyCombiningAlgorithmJAXBTypeAdapter} JAXB type adapter.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class PolicyCombiningAlgorithmsJAXBInitializer extends
		AbstractInitializer<AbstractPolicyCombiningAlgorithm> {
	private static Logger logger = LoggerFactory
			.getLogger(PolicyCombiningAlgorithmsJAXBInitializer.class);
	private static final String SEARCH_CONTEXT_PATH = "org/herasaf/xacml/core/combiningAlgorithm/policy/impl";
	private static final Class<AbstractPolicyCombiningAlgorithm> TARGET_CLASS = AbstractPolicyCombiningAlgorithm.class;

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
	protected String getURIFromType(AbstractPolicyCombiningAlgorithm instance) {
		return instance.getCombiningAlgorithmId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setInstancesIntoConverter(
			Map<String, AbstractPolicyCombiningAlgorithm> instancesMap) {
		Map<String, PolicyCombiningAlgorithm> instances = new HashMap<String, PolicyCombiningAlgorithm>();
		instances.putAll(instancesMap);
		PolicyCombiningAlgorithmJAXBTypeAdapter
				.setCombiningAlgorithms(instances);
		logger.info("{} policy combining algorithms are initialized.",
				instances.size());
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	protected Class<AbstractPolicyCombiningAlgorithm> getTargetClass() {
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

		if (!(obj instanceof PolicyCombiningAlgorithm)) {
			return false;
		}

		return hashCode() == obj.hashCode();
	}
}
