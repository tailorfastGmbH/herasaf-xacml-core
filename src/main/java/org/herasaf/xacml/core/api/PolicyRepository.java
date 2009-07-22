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

package org.herasaf.xacml.core.api;

import java.util.Collection;

import org.herasaf.xacml.core.EvaluatableNotFoundException;
import org.herasaf.xacml.core.PolicyRepositoryException;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;

/**
 * The {@link PolicyRepository} 
 * TODO JAVADOC!!!!
 */
public interface PolicyRepository {

	/**
	 * Retrives an {@link Evaluatable} from the local or a remote {@link PolicyRepository}.
	 * 
	 * @param id The id of the {@link Evaluatable}
	 * @return The retrieved {@link Evaluatable}
	 * @throws EvaluatableNotFoundException Thrown if the {@link Evaluatable} cannot be found.
	 */
	public Evaluatable getEvaluatable(EvaluatableID id)
			throws PolicyRepositoryException;

	/**
	 * Deploys the given {@link Collection} of {@link Evaluatable}s to this
	 * {@link PolicyRepository}.
	 * 
	 * @param evaluatables
	 *            The {@link Collection} of {@link Evaluatable}s to deploy.
	 * @throws DataIntegrityException
	 * @throws DataAccessException
	 * @throws SyntaxException 
	 */
	void deploy(Collection<Evaluatable> evaluatables)
			throws PolicyRepositoryException, SyntaxException;

	/**
	 * Deploys the given {@link Evaluatable} to this {@link PolicyRepository}.
	 * 
	 * @param evaluatable
	 *            The {@link Evaluatable} to deploy.
	 * @throws DataAccessException
	 * @throws DataIntegrityException
	 * @throws SyntaxException 
	 */
	void deploy(Evaluatable evaluatable) throws PolicyRepositoryException, SyntaxException;

	/**
	 * Undeploys the {@link Evaluatable} identified by the given id.
	 * 
	 * @param id
	 *            The id of the {@link Evaluatable} to undeploy.
	 * @throws DataAccessException
	 * @throws SyntaxException 
	 */
	void undeploy(EvaluatableID id) throws PolicyRepositoryException;

	/**
	 * Undeploys the {@link Evaluatable}s identified by the given
	 * {@link Collection} ids.
	 * 
	 * @param ids
	 *            The {@link Collection} of ids of the {@link Evaluatable}s.
	 * @throws DataAccessException
	 * @throws SyntaxException 
	 */
	void undeploy(Collection<EvaluatableID> ids) throws PolicyRepositoryException;
}