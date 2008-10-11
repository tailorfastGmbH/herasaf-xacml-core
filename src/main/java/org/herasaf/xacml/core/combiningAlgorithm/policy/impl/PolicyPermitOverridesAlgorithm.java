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

package org.herasaf.xacml.core.combiningAlgorithm.policy.impl;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyUnorderedCombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.MissingAttributeDetailType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.springframework.stereotype.Component;

/**
 * <p>
 * The implementation of the {@link PolicyCombiningAlgorithm} with the
 * Permit-Overrides strategy.
 * </p>
 * <p>
 * The Implementation of the Permit-override implementation oriented at the
 * sample implementation in the XACML 2.0 specification.
 * </p>
 *
 * <p>
 * See: <a
 * href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 135-137, for further information.
 * </p>
 *
 * @author Stefan Oberholzer
 * @version 1.0
 */
@Component
public class PolicyPermitOverridesAlgorithm extends
		PolicyUnorderedCombiningAlgorithm {
	private static final long serialVersionUID = 2540669307662754759L;
	// XACML Name of the Combining Algorithm
	private static final String COMBALGOID = "urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:permit-overrides";

//	/**
//	 * Initializes the {@link PolicyPermitOverridesAlgorithm} with the given
//	 * {@link TargetMatcher}.
//	 *
//	 * @param targetMatcher
//	 *            The {@link TargetMatcher} to place in the
//	 *            {@link PolicyPermitOverridesAlgorithm}
//	 */
//	public PolicyPermitOverridesAlgorithm(TargetMatcher targetMatcher) {
//		super(targetMatcher);
//	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.herasaf.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm#evaluate(org.herasaf.core.context.impl.RequestType,
	 *      java.util.List)
	 */
	@Override
	public DecisionType evaluateEvaluatableList(RequestType request,
			List<Evaluatable> possiblePolicies, RequestInformation requestInfos) {
		boolean atLeastOneError = false;
		boolean atLeastOneDeny = false;
		/*
		 * keeps the actual state and missing attributes of this combining
		 * process.
		 */
		List<MissingAttributeDetailType> missingAttributes = new ArrayList<MissingAttributeDetailType>();
		List<StatusCode> statusCodes = new ArrayList<StatusCode>();
		for (int i = 0; i < possiblePolicies.size(); i++) {
			Evaluatable eval = possiblePolicies.get(i);
			DecisionType decision;
			try {
				// Resets the status to go sure, that the returned statuscode is
				// the one of the evaluation.
				requestInfos.resetStatus();
				decision = eval.getCombiningAlg().evaluate(request, eval,
						requestInfos);
			} catch (NullPointerException e) {
				/*
				 * If an error occures or a reference returnes null, the answer
				 * has to be treated as indeterminate. See: OASIS eXtensible
				 * Access Control Markup Langugage (XACML) 2.0, Errata 29 June
				 * 2006</a> page 86 and page 136 for further information.
				 */
				requestInfos.updateStatusCode(StatusCode.SYNTAX_ERROR);
				decision = DecisionType.INDETERMINATE;
			}
			switch (decision) {
			case PERMIT:
				/*
				 * If the result is permit, the statuscode is always ok.
				 */
				requestInfos.resetStatus();
				return DecisionType.PERMIT;
			case DENY:
				/*
				 * If the decision of the evaluatable is deny, the status has to
				 * be saved.
				 */
				missingAttributes.addAll(requestInfos.getMissingAttributes());
				statusCodes.add(requestInfos.getStatusCode());
				atLeastOneDeny = true;
				break;
			case INDETERMINATE:
				/*
				 * If the decision of the evaluatable is Indeterminate, the
				 * status has to be saved.
				 */
				missingAttributes.addAll(requestInfos.getMissingAttributes());
				statusCodes.add(requestInfos.getStatusCode());
				atLeastOneError = true;
				break;
			case NOT_APPLICABLE:
				break;
			}
		}
		if (atLeastOneDeny) {
			requestInfos.setMissingAttributes(missingAttributes);
			requestInfos.updateStatusCode(statusCodes);
			return DecisionType.DENY;
		}
		if (atLeastOneError) {
			requestInfos.setMissingAttributes(missingAttributes);
			requestInfos.updateStatusCode(statusCodes);
			return DecisionType.INDETERMINATE;
		}
		return DecisionType.NOT_APPLICABLE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return COMBALGOID;
	}
}