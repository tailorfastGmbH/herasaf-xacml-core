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

package org.herasaf.xacml.core.combiningAlgorithm;

import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;

/**
 * 
 * TODO JAVADOC
 * 
 * Interface for all Combining algorithms.
 * 
 * @author Stefan Oberholzer
 * @version 1.0
 * 
 */
public interface CombiningAlgorithm {

	/**
	 * Evaluates a request against a evaluatable.
	 * 
	 * @param request
	 *            the request to evaluate.
	 * @param evals
	 *            the evaluatable which contains the rules
	 * @param requestedEvals
	 *            the additional informations used in this evaluation process.
	 * @return the decision.
	 */
	DecisionType evaluate(RequestType request, Evaluatable evals, RequestInformation requestedEvals);
}
