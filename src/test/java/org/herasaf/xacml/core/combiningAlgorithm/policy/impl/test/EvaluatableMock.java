/*
 * Copyright 2008 - 2012 HERAS-AF (www.herasaf.org)
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
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;
import org.herasaf.xacml.core.policy.impl.EffectType;
import org.herasaf.xacml.core.policy.impl.ObligationType;
import org.herasaf.xacml.core.policy.impl.TargetType;

/**
 * Represents a moch object for an {@link Evaluatable}. The following points are
 * configurable:; (1) if it has deny obligations, (2) if it has permit
 * obligations, (3) the decision.
 * 
 * @author Florian Huonder
 * @author Stefan Oberholzer
 */
public class EvaluatableMock implements Evaluatable, Cloneable {
    private static final long serialVersionUID = 1L;
    private DecisionType decision;
	private ObligationType denyObligation;
	private ObligationType permitObligation;
	private EvaluationContext evaluationContext;

	/**
	 * Creates a new mock with or without deny/permit obligations and a specific
	 * decision.
	 * 
	 * @param hasDenyObligation
	 *            True if the {@link Evaluatable} shall have deny obligations,
	 *            false otherwise.
	 * @param hasPermitObligation
	 *            True if the {@link Evaluatable} shall have permit obligations,
	 *            false otherwise.
	 * @param decision
	 *            The decision that the {@link Evaluatable} shall return.
	 */
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

	/**
	 * Returns the combining algorithm of this {@link Evaluatable} that is of
	 * type {@link CombiningAlgorithmMock}.
	 */
	public CombiningAlgorithm getCombiningAlg()
			throws EvaluatableNotFoundException {

		CombiningAlgorithmMock mock = new CombiningAlgorithmMock(decision);
		mock.setEvaluationContext(evaluationContext);
		return mock;
	}

	/**
	 * Returns all {@link ObligationType}s from the {@link Evaluatable}
	 * regarding the given effect.
	 * 
	 * @param effect
	 *            The effect for which the matching {@link ObligationType}s
	 *            shall be returned.
	 * @return A list of all {@link ObligationType}s in the {@link Evaluatable}
	 *         that match the effect.
	 */
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

	/**
	 * {@inheritDoc}
	 * 
	 * The version of the {@link Evaluatable}. Because the {@link Evaluatable}
	 * is a mock it always returns <b>MOCK</b>.
	 */
	public String getEvalutableVersion() throws EvaluatableNotFoundException {
		return "MOCK";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Because it is a mock it always returns null.
	 */
	public EvaluatableID getId() throws EvaluatableNotFoundException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This mock does not have a target.
	 */
	public TargetType getTarget() throws EvaluatableNotFoundException {
		return null;
	}

	/**
	 * {@inheritDoc} 
	 */
	public boolean hasObligations() {
		return (denyObligation != null || permitObligation != null || evaluationContext
				.getObligations().getObligations().size() > 0);
	}

	/**
	 * Returns the decision that is set in this {@link Evaluatable}.
	 * 
	 * @return The decision of this {@link Evaluatable}
	 */
	public DecisionType getDecision() {
		return decision;
	}

	/**
	 * Returns the deny {@link ObligationType} of this {@link Evaluatable}.
	 * 
	 * @return The deny {@link ObligationType} of this {@link Evaluatable}.
	 */
	public ObligationType getDenyObligation() {
		return denyObligation;
	}

	/**
	 * Sets the deny {@link ObligationType} for this {@link Evaluatable}.
	 * 
	 * @param obl The deny {@link ObligationType} for this {@link Evaluatable}.
	 */
	public void setDenyObligation(ObligationType obl) {
		this.denyObligation = obl;
	}

	/**
	 * Returns the permit {@link ObligationType} of this {@link Evaluatable}.
	 * 
	 * @return The permit {@link ObligationType} of this {@link Evaluatable}.
	 */
	public ObligationType getPermitObligation() {
		return permitObligation;
	}

	/**
	 * Sets the permit {@link ObligationType} for this {@link Evaluatable}.
	 * 
	 * @param obl The permit {@link ObligationType} for this {@link Evaluatable}.
	 */
	public void setPermitObligation(ObligationType obl) {
		this.permitObligation = obl;
	}

	/**
	 * Sets the {@link EvaluationContext} for this {@link Evaluatable}.
	 * 
	 * @param evaluationContext The {@link EvaluationContext} to set into this {@link Evaluatable}.
	 */
	public void setEvaluationContext(EvaluationContext evaluationContext) {
		this.evaluationContext = evaluationContext;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Returns the decision and whether it has or has not permit/deny {@link ObligationType}s.
	 */
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
