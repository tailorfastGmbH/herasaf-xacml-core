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
import org.herasaf.xacml.core.api.PolicyRepository;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyUnorderedCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.OnlyOneApplicableAlgorithm;
import org.herasaf.xacml.core.simplePDP.initializers.ContextAndPolicyInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.DataTypesInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.FunctionsInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.Initializer;
import org.herasaf.xacml.core.simplePDP.initializers.RuleCombiningAlgorithmsInitializer;

/**
 * TODO JAVADOC!!
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 * 
 */
public class SimplePDPFactory {

	private static PDP pdp;
	private static ThreadLocal<Boolean> singletonControl = new ThreadLocal<Boolean>();

	private static List<Initializer> initializers;

	public static void setInitalizers(List<Initializer> initalizers) {
		SimplePDPFactory.initializers = initalizers;
	}

	public static void useDefaultInitializers(boolean useDefaultInitializers) {
		if (useDefaultInitializers) {
			initializers = new ArrayList<Initializer>();
			initializers.add(new FunctionsInitializer());
			initializers.add(new DataTypesInitializer());
			initializers.add(new RuleCombiningAlgorithmsInitializer());
			//initializers.add(new PolicyCombiningAlgorithmsInitializer());
			initializers.add(new ContextAndPolicyInitializer());
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * @return
	 */
	public static PDP getSimplePDP(
			PolicyUnorderedCombiningAlgorithm rootCombiningAlgorithm,
			PolicyRepository policyRepository) {
		if (singletonControl.get() == null) {
			synchronized (SimplePDPFactory.class) {
				if (pdp == null) {
					if (initializers == null) {
						throw new InitializationException(
								"SimplePDPFactory is not initialized.");
					}
					runInitializers();
					pdp = new SimplePDP(rootCombiningAlgorithm,
							policyRepository);
				}
				singletonControl.set(Boolean.TRUE);
			}
		}
		return pdp;
	}

	/**
	 * TODO JAVADOC
	 * 
	 * @return
	 */
	public static PDP getSimplePDP() {
		return getSimplePDP(new OnlyOneApplicableAlgorithm(),
				new MapBasedSimplePolicyRepository());
	}

	private static void runInitializers() {
		for (Initializer initializer : initializers) {
			initializer.run();
		}
	}
}