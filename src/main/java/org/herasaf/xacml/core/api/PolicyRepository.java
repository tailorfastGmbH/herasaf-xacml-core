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
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.PolicyRepositoryException;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;

/**
 * The {@link PolicyRepository} TODO JAVADOC!!!! wait for: HERASAFXACMLCORE-14
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public interface PolicyRepository {

	/**
	 * TODO JAVADOC
	 * 
	 * @param evaluatable
	 * @throws PolicyRepositoryException
	 */
	public void deploy(Evaluatable evaluatable)
			throws PolicyRepositoryException;

	/**
	 * TODO JAVADOC
	 * 
	 * @param evaluatables
	 * @throws PolicyRepositoryException
	 */
	public void deploy(Collection<Evaluatable> evaluatables)
			throws PolicyRepositoryException;

	/**
	 * TODO JAVADOC
	 * 
	 * @param evaluatableID
	 * @throws PolicyRepositoryException
	 */
	public void undeploy(EvaluatableID evaluatableID)
			throws PolicyRepositoryException;

	/**
	 * TODO JAVADOC
	 * 
	 * @param evaluatableIDs
	 * @throws PolicyRepositoryException
	 */
	public void undeploy(Collection<EvaluatableID> evaluatableIDs)
			throws PolicyRepositoryException;

	/**
	 * TODO JAVADOC
	 * 
	 * @param diff
	 * @throws PolicyRepositoryException
	 */
	public void applyDeploymentModifications(
			List<DeploymentModification> deploymentInstructions)
			throws PolicyRepositoryException;

	/**
	 * TODO JAVADOC
	 * 
	 * @return
	 */
	public List<Evaluatable> getDeployment();

	/**
	 * TODO JAVADOC
	 * 
	 * @param dateTime
	 * @return
	 */
	public List<Evaluatable> getDeployment(Date dateTime);

	/**
	 * TODO JAVADOC
	 * 
	 * @return
	 */
	public Map<Date, List<Evaluatable>> getDeployments();

	/**
	 * TODO JAVADOC
	 * 
	 * @param evaluatableID
	 * @throws PolicyRepositoryException
	 * 
	 * @return
	 */
	public Evaluatable getEvaluatable(EvaluatableID evaluatableID)
			throws PolicyRepositoryException;

	/**
	 * TODO JAVADOC
	 * 
	 * @param request
	 * @return
	 */
	public List<Evaluatable> getEvaluatables(RequestCtx request);
}