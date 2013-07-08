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
package org.herasaf.xacml.core.combiningAlgorithm.policy.impl.test;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.combiningAlgorithm.CombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.EffectType;
import org.herasaf.xacml.core.policy.impl.ObligationType;

/**
 * This mock object represents a combining algorithm that returns a predefined
 * decision and the matching obligations.
 * 
 * @author Florian Huonder
 * @author Stefan Oberholzer
 */
public class CombiningAlgorithmMock implements CombiningAlgorithm {
	RequestInformation reqInfo;
	DecisionType decision;

	/**
	 * Sets the decision that the combing algorithm shall return.
	 * 
	 * @param decision
	 */
	public CombiningAlgorithmMock(DecisionType decision) {
		this.decision = decision;
	}

	/**
	 * {@inheritDoc}
	 */
	public DecisionType evaluate(RequestType request, Evaluatable evals, RequestInformation requestInfo) {
		List<ObligationType> obligations = new ArrayList<ObligationType>();
		for (ObligationType obligation : reqInfo.getObligations().getObligations()) {
			obligations.add(new ObligationType(obligation.getObligationId(), obligation.getFulfillOn()));
		}

		requestInfo.updateStatusCode(reqInfo.getStatusCode());
		requestInfo.addObligations(obligations, EffectType.PERMIT);
		requestInfo.addObligations(obligations, EffectType.DENY);
		return decision;
	}

	/**
	 * Returns the {@link RequestInformation} that is set in this combining
	 * algorithm.
	 * 
	 * @return The {@link RequestInformation} that is set in this combining
	 *         algorithm.
	 */
	public RequestInformation getReqInfo() {
		return reqInfo;
	}

	/**
	 * Sets a {@link RequestInformation} into this combining algorithm.
	 * 
	 * @param reqInfo
	 *            The {@link RequestInformation} to set into this combining
	 *            algorithm.
	 */
	public void setReqInfo(RequestInformation reqInfo) {
		this.reqInfo = reqInfo;
	}

	/**
	 * Returns false because it is an unordered combining algorithm.
	 */
	public boolean isOrderedCombiningAlgorithm() {
		return false;
	}

	/**
	 * nop because its ordered.
	 */
	public void setOrderedCombiningAlgorithm(boolean isOrderedCombiningAlgorithm) {
		// nop because its ordered.

	}
}