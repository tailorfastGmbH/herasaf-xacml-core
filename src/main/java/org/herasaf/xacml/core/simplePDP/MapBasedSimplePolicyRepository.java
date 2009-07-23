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
package org.herasaf.xacml.core.simplePDP;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.PolicyRepositoryException;
import org.herasaf.xacml.core.api.PolicyRepository;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;

/**
 * TODO JAVADOC!!
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 * 
 */
public class MapBasedSimplePolicyRepository implements PolicyRepository {

	private Map<EvaluatableID, Evaluatable> repository;
	
	public MapBasedSimplePolicyRepository(){
		repository = new HashMap<EvaluatableID, Evaluatable>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deploy(Collection<Evaluatable> evaluatables)
			throws PolicyRepositoryException {
		for(Evaluatable eval : evaluatables){
			deploy(eval);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void deploy(Evaluatable evaluatable)
			throws PolicyRepositoryException {
		repository.put(evaluatable.getId(), evaluatable);
	}

	/**
	 * {@inheritDoc}
	 */
	public Evaluatable getEvaluatable(EvaluatableID id)
			throws PolicyRepositoryException {
		return repository.get(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Evaluatable> getEvaluatables(RequestCtx requestCtx)
			throws PolicyRepositoryException {
		return new ArrayList<Evaluatable>(repository.values());
	}

	/**
	 * {@inheritDoc}
	 */
	public void undeploy(EvaluatableID id) throws PolicyRepositoryException {
		repository.remove(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public void undeploy(Collection<EvaluatableID> ids)
			throws PolicyRepositoryException {
		for(EvaluatableID id : ids){
			undeploy(id);
		}
	}
}