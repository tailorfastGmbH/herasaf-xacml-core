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
package org.herasaf.xacml.core.simplePDP.initializers.jaxb.typeadapter.xacml20.rulecombiningalgorithms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.combiningAlgorithm.rule.AbstractRuleCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RuleDenyOverridesAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RuleFirstApplicableAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RuleOrderedDenyOverridesAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RuleOrderedPermitOverridesAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RulePermitOverridesAlgorithm;
import org.herasaf.xacml.core.converter.RuleCombiningAlgorithmJAXBTypeAdapter;

/**
 * This initializer initializes all internal/build-in rule combining algorithms
 * and puts them in the {@link RuleCombiningAlgorithmJAXBTypeAdapter} JAXB type
 * adapter.
 * 
 * @author Alexander Broekhuis
 * @author Florian Huonder
 */
public final class Xacml20DefaultRuleCombiningAlgorithmsJaxbInitializer extends
		AbstractRuleCombiningAlgorithmsJaxbTypeAdapterInitializer {

	/**
	 * {@inheritDoc}<br />
	 * <b>This implementation:</b><br />
	 * Instantiates all default XACML 2.0 {@link RuleCombiningAlgorithm
	 * RuleCombiningAlgorithms}.
	 */
	@Override
	protected Map<String, AbstractRuleCombiningAlgorithm> createTypeInstances() {
		List<AbstractRuleCombiningAlgorithm> ruleCombiningAlgorithms = createInstances(
				RuleDenyOverridesAlgorithm.class,
				RuleFirstApplicableAlgorithm.class,
				RuleOrderedDenyOverridesAlgorithm.class,
				RuleOrderedPermitOverridesAlgorithm.class,
				RulePermitOverridesAlgorithm.class);

		Map<String, AbstractRuleCombiningAlgorithm> instancesMap = new HashMap<String, AbstractRuleCombiningAlgorithm>();
		for (AbstractRuleCombiningAlgorithm algorithm : ruleCombiningAlgorithms) {
			instancesMap.put(algorithm.getCombiningAlgorithmId(), algorithm);
		}

		return instancesMap;
	}
}