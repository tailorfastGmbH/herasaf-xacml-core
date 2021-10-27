/*
 * Copyright 2008 - 2013 HERAS-AF (www.herasaf.org)
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

package org.herasaf.xacml.core.policy.combiningAlgorithm.mock;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.ExpressionProcessingException;
import org.herasaf.xacml.core.policy.impl.TargetType;
import org.herasaf.xacml.core.targetMatcher.TargetMatcher;
import org.herasaf.xacml.core.targetMatcher.TargetMatchingResult;

/**
 * This is a mock of a {@link TargetMatcher}.
 * 
 * @author Sacha Dolski
 */
public class TargetMatcherMock implements TargetMatcher {
	private static final long serialVersionUID = 7237566385772657474L;
	private Decisions[] decisions;
	private int counter = 0;

	/**
	 * Creates a mock that returns always false.
	 */
	public TargetMatcherMock() {
	}

	/**
	 * Creates a mock that returns the given decisions.
	 * 
	 * @param decisions
	 *            The decisions that the mock shall evaluate to.
	 */
	public TargetMatcherMock(Decisions[] decisions) {
		this.decisions = decisions.clone();
	}

	/**
	 * Creates a mock that returns the given decision.
	 * 
	 * @param decisions
	 *            The decision the moch shall evaluate to.
	 */
	public TargetMatcherMock(Decisions decisions) {
		this.decisions = new Decisions[] { decisions };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TargetMatchingResult match(RequestType req, TargetType target,
			EvaluationContext evaluationContext) throws SyntaxException,
			ProcessingException {
		Decisions dec = decisions[counter];
		setCounter(counter);
		counter++;
		switch (dec) {
		case FALSE:
			return TargetMatchingResult.NO_MATCH;
		case PROCESSINGEXCEPTION:
			throw new ExpressionProcessingException();
		case SYNTAXEXCEPTION:
			throw new SyntaxException();
		case NULLPOINTEREXCEPTION:
			throw new NullPointerException();
		case TRUE:
			return TargetMatchingResult.MATCH;
		}
		return TargetMatchingResult.NO_MATCH;
	}

	/**
	 * Returns a clone of the decisions.
	 * 
	 * @return The clone of the decisions.
	 */
	public Decisions[] getDecision() {
		return decisions.clone();
	}

	/**
	 * Sets a clone of the decisions.
	 * 
	 * @param decisions
	 *            The decisions to set to this mock.
	 */
	public void setDecision(Decisions[] decisions) {
		this.decisions = decisions.clone();
	}

	/**
	 * Get the state of the counter.
	 * 
	 * @return The counter.
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * Set a new value to the counter.
	 * 
	 * @param counter
	 *            The new value.
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}

	/**
	 * The enum representing the possible decisions (including errors).
	 * 
	 * @author Florian Huonder
	 */
	public enum Decisions {
		TRUE, FALSE, SYNTAXEXCEPTION, PROCESSINGEXCEPTION, NULLPOINTEREXCEPTION
	}
}