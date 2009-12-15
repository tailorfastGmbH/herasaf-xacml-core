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
package org.herasaf.xacml.core.combiningAlgorithm;

import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;

/**
 * TODO REVIEW René.
 * 
 * This interface describes a combining algorithm, rule and policy combining
 * algorithms. The
 * {@link #evaluate(RequestType, Evaluatable, RequestInformation)} method is the
 * entry point for evaluating a request.
 * 
 * 
 * @author Stefan Oberholzer
 */
public interface CombiningAlgorithm {

	/**
	 * TODO REVIEW René.
	 * 
	 * Evaluates a request against the given {@link Evaluatable} (that is a
	 * policy or a policy set).
	 * 
	 * @param request
	 *            The request to evaluate.
	 * @param evals
	 *            The {@link Evaluatable} to evaluate.
	 * @param requestInfo
	 *            The evaluation context.
	 * @return The decision of the evaluation of the given request.
	 */
	DecisionType evaluate(RequestType request, Evaluatable evals, RequestInformation requestInfo);

	/**
	 * TODO REVIEW René.
	 * 
	 * Returns true if the implementation of the combining algorithm is ordered,
	 * false otherwise.
	 * 
	 * @return true if the implementation of the combining algorithm is ordered,
	 *         false otherwise.
	 */
	boolean isOrderedCombiningAlgorithm();
}
