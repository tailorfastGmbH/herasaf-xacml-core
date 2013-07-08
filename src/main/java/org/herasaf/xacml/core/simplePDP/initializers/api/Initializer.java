/*
 * Copyright 2008 - 2012 HERAS-AF (www.herasaf.org)
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
package org.herasaf.xacml.core.simplePDP.initializers.api;

import org.herasaf.xacml.core.simplePDP.SimplePDPConfiguration;

/**
 * An initializer that shall be executed by the SimplePDPFactory must implement this
 * interface.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public interface Initializer {
	/**
	 * Within this method the initialization must be done.
	 * @param configuration The PDP configuration to use
	 */
	void run(SimplePDPConfiguration configuration);
}