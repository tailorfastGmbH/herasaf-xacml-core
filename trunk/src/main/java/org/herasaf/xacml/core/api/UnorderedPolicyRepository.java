/*
 * Copyright 2009 - 2011 HERAS-AF (www.herasaf.org)
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
package org.herasaf.xacml.core.api;

import java.util.Collection;

import org.herasaf.xacml.core.policy.Evaluatable;

/**
 * This policy enhances to capability of the {@link PolicyRepository} with
 * <u>unordered</u> deployment of {@link Evaluatable}s.
 * 
 * @author Ylli Sylejmani
 */
public interface UnorderedPolicyRepository extends PolicyRepository {

	/**
	 * Inserts a new {@link Evaluatable} into the policy repository. It is put
	 * at the end of the list. It does not require a specific (ordered or
	 * unordered) combining algorithm.
	 * 
	 * @param evaluatable
	 *            The {@link Evaluatable} to add to the policy repository.
	 */
	void deploy(Evaluatable evaluatable);

	/**
	 * Inserts a collection of new {@link Evaluatable}s into the policy
	 * repository. The {@link Evaluatable} are added randomly to the deployed
	 * collections. It does not require a specific (ordered or unordered)
	 * combining algorithm.
	 * 
	 * @param evaluatables
	 *            The collection of {@link Evaluatable}s to add to the policy
	 *            repository.
	 */
	void deploy(Collection<Evaluatable> evaluatables);

}
