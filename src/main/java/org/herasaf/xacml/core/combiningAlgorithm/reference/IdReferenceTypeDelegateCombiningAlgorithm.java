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
package org.herasaf.xacml.core.combiningAlgorithm.reference;

import org.herasaf.xacml.core.combiningAlgorithm.CombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.IdReferenceType;
/**
 * Delegate CombiningAlgorithm for IdReferenceType to delegate the delegated Evaluatable
 *
 * @author Patrik Dietschweiler
 * @version 1.0
 *
 */
public class IdReferenceTypeDelegateCombiningAlgorithm implements
		CombiningAlgorithm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Delegate Method
	 *
	 */
	public DecisionType evaluate(RequestType request, Evaluatable evals,
			RequestInformation requestedEvals) {
		Evaluatable resolvedRemoteEvaluatable = requestedEvals.getRemotePolicy(((IdReferenceType)evals).getValue());
		return resolvedRemoteEvaluatable.getCombiningAlg().evaluate(request, resolvedRemoteEvaluatable, requestedEvals);
	}

}
