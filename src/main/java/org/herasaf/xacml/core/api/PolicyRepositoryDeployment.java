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
package org.herasaf.xacml.core.api;

import java.util.Collection;
import java.util.List;

import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;

/**
 * The deployment policy repository manages the deployed and currently active
 * {@link Evaluatable}s. It is the entry point of an deployment client, like
 * e.g. a PAP, to a policy repository. On this level it provides methods to
 * undeploy {@link Evaluatable}s and retrieve the current deployment as these
 * are general. Methods for the deployment of {@link Evaluatable}s are provided
 * in the sub-interfaces as they can be <u>ordered</u> or <u>unordered</u>.<br />
 * 
 * <br />
 * The policy repository may store and/or index the {@link Evaluatable}s. <br />
 * 
 * @author Ylli Sylejmani
 */
public interface PolicyRepositoryDeployment {

	/**
	 * Removes the {@link Evaluatable} with the given {@link EvaluatableID} from
	 * the policy repository.
	 * 
	 * @param evaluatableID
	 *            The id of the {@link Evaluatable} to remove.
	 */
	void undeploy(EvaluatableID evaluatableID);

	/**
	 * Removes the {@link Evaluatable}s with the given {@link EvaluatableID}s
	 * from the policy repository.
	 * 
	 * @param evaluatableIDs
	 *            The collection containg the {@link EvaluatableID} of the
	 *            {@link Evaluatable}s to be removed.
	 */
	void undeploy(Collection<EvaluatableID> evaluatableIDs);

	/**
	 * Returns the currently deployed {@link Evaluatable}s.
	 * 
	 * @return A list containing the currently deployed {@link Evaluatable}s.
	 */
	List<Evaluatable> getDeployment();

}
