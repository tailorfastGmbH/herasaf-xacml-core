/*
 * Copyright 2008-2010 HERAS-AF (www.herasaf.org)
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
 * All policy combining algorithms must implement this interface. It provides
 * the entry point (
 * {@link #evaluateEvaluatableList(RequestType, List, RequestInformation)}) for
 * starting to evaluate sub-evaluatables.
 * 
 * @author Sacha Dolski
 * @author Florian Huonder
 */
public interface PolicyCombiningAlgorithm extends CombiningAlgorithm {
	/**
	 * This method is the entry point for a policy combining algorithm to start
	 * evaluating its sub-evaluatables.
	 * 
	 * @param request
	 *            The request that has to be evaluated.
	 * @param possibleEvaluatables
	 *            List of the sub-evaluatables that have to be evaluated.
	 * @param requestInfo
	 *            The context of this request evaluation.
	 * @return The combined decision of the evaluation of the request.
	 */
	DecisionType evaluateEvaluatableList(RequestType request, List<Evaluatable> possibleEvaluatables,
			RequestInformation requestInfo);

	/**
	 * Returns true if abandoned evaluatables shall be respected, false
	 * otherwise.
	 * 
	 * @return true if abandoned evaluatables shall be respected, false
	 *         otherwise.
	 */
	boolean isRespectAbandonedEvaluatables();

	/**
	 * Sets if this combining algorithm shall respect abandoned evaluatables.
	 * 
	 * @param respectAbandondEvaluatables
	 *            True if abandoned evaluatables shall be respected, false
	 *            otherwise.
	 */
	void setRespectAbandondEvaluatables(boolean respectAbandondEvaluatables);
}