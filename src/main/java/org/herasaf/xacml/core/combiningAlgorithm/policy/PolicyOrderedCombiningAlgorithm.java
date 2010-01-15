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

import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.PolicySetType;

/**
 * This class may be extended when implementing an ordered policy combining
 * algorithm. It contains some common code all ordered combining algorithms must
 * implement.
 * 
 * @author Stefan Oberholzer
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public abstract class PolicyOrderedCombiningAlgorithm extends AbstractPolicyCombiningAlgorithm {

	/**
	 * {@inheritDoc}
	 */
	public DecisionType evaluate(final RequestType request, final Evaluatable evals,
			final RequestInformation requestInfo) {
		final DecisionType decision = matchTarget(request, evals.getTarget(), requestInfo);

		if (decision != DecisionType.PERMIT) {
			return decision;
		}
		try {
			final DecisionType dec = this.evaluateEvaluatableList(request, ((PolicySetType) evals)
					.getOrderedEvaluatables(requestInfo), requestInfo);
			/*
			 * The evaluateEvaluatableList method may set the targetMatched
			 * value to false. So it has to be set to true to go sure that it is
			 * true.
			 */
			requestInfo.setTargetMatched(true);
			return dec;
		} catch (ClassCastException e) {
			/*
			 * If an error occures, indeterminate has to be returned. See: OASIS
			 * eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29
			 * June 2006</a> page 86, chapter "Syntax and type errors" for
			 * further information.
			 */
			requestInfo.updateStatusCode(StatusCode.SYNTAX_ERROR);
			return DecisionType.INDETERMINATE;
		}
	}
}