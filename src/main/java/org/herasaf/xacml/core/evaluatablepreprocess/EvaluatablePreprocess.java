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
package org.herasaf.xacml.core.evaluatablepreprocess;

import java.util.Collection;

import org.herasaf.xacml.DataIntegrityException;
import org.herasaf.xacml.core.policy.Evaluatable;

/**
 * {@code EvaluatablePreprocess} walks through a list of evaluatables to resolve the local policies
 * or resolves a single evaluatable 
 *
 * @author Christoph Egger
 * @version 1.0
 */
public interface EvaluatablePreprocess {

	/**
	 * resolves every contained local Reference in the given List of evaluatables
	 * 
	 * @param evaluatables
	 * @return returns a list where all the local resolvable References are exchanged with the real
	 * Policies
	 * @throws DataIntegrityException
	 */	
	public Collection<Evaluatable> process(Collection<Evaluatable> evaluatables) throws DataIntegrityException;
	
	
	/**
	 * resolves local existing Policy/Policies in a single Evaluatable
	 * 
	 * @param evaluatables
	 * @return returns the resolved Evaluatable
	 * @throws DataIntegrityException
	 */
	public Evaluatable process(Evaluatable evaluatables) throws DataIntegrityException;

}
