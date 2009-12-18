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
package org.herasaf.xacml.core.api;

import org.herasaf.xacml.core.policy.Evaluatable;

/**
 * TODO REVIEW René
 * 
 * This policy enhances to capability of the {@link PolicyRepository} with
 * <u>ordered</u> deployment of {@link Evaluatable}s (see
 * {@link #deploy(Evaluatable, int)}).
 * 
 * @author Florian Huonder
 */
public interface OrderedPolicyRepository extends PolicyRepository {

	/**
	 * TODO REVIEW René.
	 * 
	 * Inserts a new {@link Evaluatable} into the policy repository at the given
	 * position. It requires an ordered root combining algorithm to work.
	 * 
	 * @param evaluatable
	 *            The {@link Evaluatable} to add to the policy repository.
	 * @param position
	 *            The position where the {@link Evaluatable} shall be added to
	 *            the list.
	 */
	void deploy(Evaluatable evaluatable, int position);
}
