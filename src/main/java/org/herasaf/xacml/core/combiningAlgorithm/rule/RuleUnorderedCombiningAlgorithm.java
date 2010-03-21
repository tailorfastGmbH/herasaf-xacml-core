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

package org.herasaf.xacml.core.combiningAlgorithm.rule;

import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.PolicyType;

/**
 * This class may be extended when implementing an unordered rule combining
 * algorithm. It contains some common code all ordered combining algorithms must
 * implement.
 * 
 * @author Stefan Oberholzer
 */
public abstract class RuleUnorderedCombiningAlgorithm extends AbstractRuleCombiningAlgorithm {
    private static final long serialVersionUID = 1L;

    /**
	 * {@inheritDoc}
	 */
	public DecisionType evaluate(final RequestType request, final Evaluatable evals,
			final EvaluationContext evaluationContext) {
		final DecisionType decision = matchTarget(request, evals.getTarget(), evaluationContext);

		if (decision != DecisionType.PERMIT) {
			return decision;
		}

		try {
			evaluationContext.setVariableDefinitions(((PolicyType) evals).getVariables());
			final DecisionType dec = this.evaluateRuleList(request, ((PolicyType) evals).getUnorderedRules(),
					evaluationContext);
			/*
			 * If the decision was made, the evaluation process might have set
			 * the targetMatched variable to false. so it has to be sure that
			 * true is returned in this variable.
			 */
			evaluationContext.setTargetMatched(true);
			return dec;
		} catch (ClassCastException e) {
			/*
			 * If an error occures, indeterminate has to be returned. See: OASIS
			 * eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29
			 * June 2006</a> page 86, chapter "Syntax and type errors" for
			 * further information.
			 */
			evaluationContext.updateStatusCode(StatusCode.SYNTAX_ERROR);
			return DecisionType.INDETERMINATE;
		}
	}
}
