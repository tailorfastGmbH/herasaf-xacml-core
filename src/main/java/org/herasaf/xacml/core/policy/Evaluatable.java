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

import org.herasaf.xacml.core.combiningAlgorithm.CombiningAlgorithm;
import org.herasaf.xacml.core.policy.impl.TargetType;

/**
 * Represents an type that is evaluatable.
 *
 * @author Florian Huonder
 * @version 1.0
 */
public interface Evaluatable {

	/**
	 * Returns the id of the evaluatable.
	 * 
	 * @return The Id as string.
	 */
	public EvaluatableID getId(); 
	
	/**
	 * Returns the {@link TargetType} of the evaluatable.
	 * 
	 * @return The {@link TargetType} of the evaluatable.
	 */
	public TargetType getTarget();
	
	/**
	 * Returns the {@link CombiningAlgorithm} of the evaluatable.
	 * 
	 * @return The {@link CombiningAlgorithm} of the evaluatable.
	 */
	public CombiningAlgorithm getCombiningAlg();
}