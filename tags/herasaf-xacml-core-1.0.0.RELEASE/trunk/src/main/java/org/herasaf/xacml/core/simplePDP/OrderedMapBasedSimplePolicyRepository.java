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
package org.herasaf.xacml.core.simplePDP;

import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.api.OrderedPolicyRepository;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;

/**
 * This is a very simple implementation of the
 * {@link OrderedMapBasedSimplePolicyRepository}. This policy repository has a
 * limited functionality. It only works as a "provider" of locally deployed
 * {@link Evaluatable}s. It does not:
 * <ul>
 * <li>persist the {@link Evaluatable}s</li>
 * <li>index the {@link Evaluatable}s</li>
 * <li>resolve {@link Evaluatable}s from remote (only local) repositories</li>
 * </ul>
 * <b>It is not recommended to use this repository in a productive
 * environment.</b
 * 
 * @author Florian Huonder
 */
public class OrderedMapBasedSimplePolicyRepository extends
		MapBasedSimplePolicyRepository implements
		OrderedPolicyRepository {

	/**
	 * {@inheritDoc}
	 */
	public void deploy(Evaluatable evaluatable, int position) {
		Map<EvaluatableID, List<Evaluatable>> newIndividualEvaluatables = splitIntoIndividuals(
				evaluatable, evaluatable.getId());

		checkEvaluatable(newIndividualEvaluatables);

		individualEvaluatables.putAll(newIndividualEvaluatables);
		rootEvaluatables.add(position, evaluatable);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deploy(Map<Integer, Evaluatable> evaluatables) {
		for (Integer key : evaluatables.keySet()) {
			deploy(evaluatables.get(key), key.intValue());
		}
	}
}