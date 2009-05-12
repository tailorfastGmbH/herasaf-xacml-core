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

package org.herasaf.xacml.core.policy;

import java.util.List;

import org.herasaf.xacml.EvaluatableNotFoundException;
import org.herasaf.xacml.core.combiningAlgorithm.CombiningAlgorithm;
import org.herasaf.xacml.core.policy.impl.EffectType;
import org.herasaf.xacml.core.policy.impl.ObligationType;
import org.herasaf.xacml.core.policy.impl.TargetType;

/**
 * Represents an type that is evaluatable.
 * 
 * @author Florian Huonder, Patrik Dietschweiler
 * @version 1.1
 */
public interface Evaluatable {

	/**
	 * Returns the id of the evaluatable.
	 * 
	 * @return The Id as string.
	 * 
	 * @throws EvaluatableNotFound if the Evaluatable is an IdReferenceType
	 * and it didnt find the Evaluatable (Only in Last Loading Strategy)
	 */
	public EvaluatableID getId() throws EvaluatableNotFoundException; 
	
	/**
	 * Returns the {@link TargetType} of the evaluatable.
	 * 
	 * @return The {@link TargetType} of the evaluatable.
	 * 
	 * @throws EvaluatableNotFound if the Evaluatable is an IdReferenceType
	 * and it didnt find the Evaluatable (Only in Last Loading Strategy)
	 */
	public TargetType getTarget() throws EvaluatableNotFoundException;
	
	/**
	 * Returns the {@link CombiningAlgorithm} of the evaluatable.
	 * 
	 * @return The {@link CombiningAlgorithm} of the evaluatable.
	 * 
	 * @throws EvaluatableNotFound if the Evaluatable is an IdReferenceType
	 * and it didnt find the Evaluatable (Only in Last Loading Strategy)
	 */
	public CombiningAlgorithm getCombiningAlg() throws EvaluatableNotFoundException;
	
	/**
	 * Returns the version of the evaluatable.
	 * 
	 * @return The version as string.
	 * 
	 * @throws EvaluatableNotFound if the Evaluatable is an IdReferenceType
	 * and it didnt find the Evaluatable (Only in Last Loading Strategy)
	 */
	public String getEvalutableVersion() throws EvaluatableNotFoundException;
	
	/**
	 * Returns a boolean value indicating if the current {@link Evaluatable} or a sub- {@link Evaluatable} contains
	 * one or more Obligations.
	 * 
	 * @return True if the current or a sub- {@link Evaluatable} contains one or more Obligations.
	 *         False otherwise.
	 */
	public boolean hasObligations();
	
	/**
	 * Returns the Obligations of this {@link Evaluatable} which match the given effect.
	 * 	
	 * @param effect the {@link EffectType} by which the Obligations should be returned.
	 * @return The Obligations which match the {@link EffectType}.
	 */
	public List<ObligationType> getContainedObligations(EffectType effect);
}