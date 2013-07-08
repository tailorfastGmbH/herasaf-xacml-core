/*
 * Copyright 2009 - 2012 HERAS-AF (www.herasaf.org)
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

import java.io.Serializable;

import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;

/**
 * This interface describes a combining algorithm, rule and policy combining
 * algorithms. The
 * {@link #evaluate(RequestType, Evaluatable, EvaluationContext)} method is the
 * entry point for evaluating a request.
 * 
 * 
 * @author Stefan Oberholzer
 */
public interface CombiningAlgorithm extends Serializable {

	/**
	 * Returns the ID of the combining algorithm.
	 * 
	 * @return The ID of the combining algorithm.
	 */
	public String getCombiningAlgorithmId();
	
	/**
	 * Evaluates a request against the given {@link Evaluatable} (that is a
	 * policy or a policy set).
	 * 
	 * @param request
	 *            The request to evaluate.
	 * @param evals
	 *            The {@link Evaluatable} to evaluate.
	 * @param evaluationContext
	 *            The evaluation context.
	 * @return The decision of the evaluation of the given request.
	 */
	DecisionType evaluate(RequestType request, Evaluatable evals, EvaluationContext evaluationContext);
}
