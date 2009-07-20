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
package org.herasaf.xacml.core.combiningAlgorithm.policy.impl.test;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.EvaluatableNotFoundException;
import org.herasaf.xacml.core.combiningAlgorithm.CombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;
import org.herasaf.xacml.core.policy.impl.EffectType;
import org.herasaf.xacml.core.policy.impl.ObligationType;
import org.herasaf.xacml.core.policy.impl.TargetType;

/**
 * @author Florian Huonder
 * @author Stefan Oberholzer
 */
public class EvaluatableMock implements Evaluatable, Cloneable {

	private DecisionType decision;
	private ObligationType denyObligation;
	private ObligationType permitObligation;
	private RequestInformation reqInfo;

	public EvaluatableMock(boolean hasDenyObligation,
			boolean hasPermitObligation, DecisionType decision) {
		this.decision = decision;
		if (hasDenyObligation) {
			this.denyObligation = new ObligationType("deny", EffectType.DENY);
		}
		if (hasPermitObligation) {
			this.permitObligation = new ObligationType("permit",
					EffectType.PERMIT);
		}
		
	}

	public CombiningAlgorithm getCombiningAlg()
			throws EvaluatableNotFoundException {

		CombiningAlgorithmMock mock = new CombiningAlgorithmMock(decision);
		mock.setReqInfo(reqInfo);
		return mock;
	}

	public List<ObligationType> getContainedObligations(EffectType effect) {
		List<ObligationType> obligations = new ArrayList<ObligationType>();
		switch (effect) {
		case DENY:
			if (denyObligation != null) {
				obligations.add(denyObligation);
			}
			break;
		case PERMIT:
			if (permitObligation != null) {
				obligations.add(permitObligation);
			}
			break;
		}
		return obligations;
	}

	public String getEvalutableVersion() throws EvaluatableNotFoundException {
		return "MOCK";
	}

	public EvaluatableID getId() throws EvaluatableNotFoundException {
		return null;
	}

	public TargetType getTarget() throws EvaluatableNotFoundException {
		return null;
	}

	public boolean hasObligations() {
		return (denyObligation != null || permitObligation != null || reqInfo.getObligations().getObligations().size() > 0);
	}

	public DecisionType getDecision() {
		return decision;
	}

	public ObligationType getDenyObligation() {
		return denyObligation;
	}

	public void setDenyObligation(ObligationType obl){
		this.denyObligation = obl;
	}
	
	public ObligationType getPermitObligation() {
		return permitObligation;
	}
	
	public void setPermitObligation(ObligationType obl){
		this.permitObligation = obl;
	}
	
	

	public void setReqInfo(RequestInformation reqInfo) {
		this.reqInfo = reqInfo;
	}

	public String toString() {
		return "EvaluatableMock[decision=" + decision + ", hasDenyObligation="
				+ (denyObligation != null) + ", hasPermitObligation="
				+ (permitObligation != null) + "]";

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EvaluatableMock clone() throws CloneNotSupportedException {

		EvaluatableMock clone = (EvaluatableMock) super.clone();
		if (this.denyObligation != null) {
			clone.setDenyObligation(new ObligationType(this.denyObligation
					.getObligationId(), this.denyObligation.getFulfillOn()));
		}
		if (this.permitObligation != null) {
			clone.setPermitObligation(new ObligationType(this.permitObligation
					.getObligationId(), this.permitObligation.getFulfillOn()));
		}
		return clone;

	}
}
