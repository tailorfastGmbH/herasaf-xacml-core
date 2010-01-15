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

import java.util.Map;

import org.herasaf.xacml.core.converter.URNToFunctionConverter;
import org.herasaf.xacml.core.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This initializer initializes all functions and puts them in the
 * {@link URNToFunctionConverter} JAXB type adapter.
 * 
 * @author Florian Huonder
 * @author Ren� Eggenschwiler
 */
public class FunctionsInitializer extends AbstractInitializer<Function> {
	private final Logger logger = LoggerFactory.getLogger(FunctionsInitializer.class);
	private static final String SEARCH_CONTEXT = "org.herasaf.xacml.core.function.impl";
	private static final String SEARCH_CONTEXT_PATH = "org/herasaf/xacml/core/function/impl";
	private static final Class<Function> TARGET_CLASS = Function.class;

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
	protected String getURIFromType(Function instance) {
		return instance.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setInstancesIntoConverter(Map<String, Function> instancesMap) {
		URNToFunctionConverter.setFunctions(instancesMap);
		logger.info("{} functions are initialized.", instancesMap.size());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<Function> getTargetClass() {
		return TARGET_CLASS;
	}
}