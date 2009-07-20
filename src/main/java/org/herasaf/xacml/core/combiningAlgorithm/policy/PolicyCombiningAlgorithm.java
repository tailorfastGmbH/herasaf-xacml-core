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

package org.herasaf.xacml.core.combiningAlgorithm.policy;

import java.util.List;

import org.herasaf.xacml.core.combiningAlgorithm.CombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;

/**
 * Interface for the PolicyCombiningAlgorithms.
 *
 * @author Sacha Dolski
 * @author Florian Huonder
 * @version 1.1
 */

public interface PolicyCombiningAlgorithm extends CombiningAlgorithm {
	/**
	 *
	 * @param request
	 *            The request that has to be evaluated.
	 * @param possibleEvaluatables
	 *            List of the evaluatables that have to be evaluated.
	 * @param requestInfo
	 *            Contains the information that has to be kept for this request.
	 * @return The decision of the evaluation of the request.
	 */
	public DecisionType evaluateEvaluatableList(RequestType request,
			List<Evaluatable> possibleEvaluatables,
			RequestInformation requestInfo);
}