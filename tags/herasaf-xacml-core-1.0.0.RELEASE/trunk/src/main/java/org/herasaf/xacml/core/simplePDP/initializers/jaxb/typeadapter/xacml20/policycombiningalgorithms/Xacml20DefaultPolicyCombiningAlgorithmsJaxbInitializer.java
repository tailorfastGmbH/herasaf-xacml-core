/*
 * Copyright 2009 - 2012 HERAS-AF (www.herasaf.org)
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
package org.herasaf.xacml.core.simplePDP.initializers.jaxb.typeadapter.xacml20.policycombiningalgorithms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.combiningAlgorithm.policy.AbstractPolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyDenyOverridesAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyFirstApplicableAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyOnlyOneApplicableAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyOrderedDenyOverridesAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyOrderedPermitOverridesAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyPermitOverridesAlgorithm;
import org.herasaf.xacml.core.converter.PolicyCombiningAlgorithmJAXBTypeAdapter;

/**
 * This initializer initializes all internal/build-in policy combining
 * algorithms and puts them in the
 * {@link PolicyCombiningAlgorithmJAXBTypeAdapter} JAXB type adapter.
 * 
 * @author Alexander Broekhuis
 * @author Florian Huonder
 */
public final class Xacml20DefaultPolicyCombiningAlgorithmsJaxbInitializer
		extends AbstractPolicyCombiningAlgorithmsJaxbTypeAdapterInitializer {

	/**
	 * {@inheritDoc}<br />
	 * <b>This implementation:</b><br />
	 * Instantiates all default XACML 2.0 {@link PolicyCombiningAlgorithm
	 * PolicyCombiningAlgorithms}.
	 */
	@Override
	protected Map<String, AbstractPolicyCombiningAlgorithm> createTypeInstances() {
		List<AbstractPolicyCombiningAlgorithm> policyCombiningAlgorithms = createInstances(
				PolicyDenyOverridesAlgorithm.class,
				PolicyFirstApplicableAlgorithm.class,
				PolicyOnlyOneApplicableAlgorithm.class,
				PolicyOrderedDenyOverridesAlgorithm.class,
				PolicyOrderedPermitOverridesAlgorithm.class,
				PolicyPermitOverridesAlgorithm.class);

		Map<String, AbstractPolicyCombiningAlgorithm> instancesMap = new HashMap<String, AbstractPolicyCombiningAlgorithm>();
		for (AbstractPolicyCombiningAlgorithm algorithm : policyCombiningAlgorithms) {
			instancesMap.put(algorithm.getCombiningAlgorithmId(), algorithm);
		}

		return instancesMap;
	}
}