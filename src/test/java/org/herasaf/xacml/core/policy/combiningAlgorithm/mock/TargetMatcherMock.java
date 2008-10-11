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

package org.herasaf.xacml.core.policy.combiningAlgorithm.mock;

import org.herasaf.xacml.SyntaxException;
import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.ExpressionProcessingException;
import org.herasaf.xacml.core.policy.impl.TargetType;
import org.herasaf.xacml.core.targetMatcher.TargetMatcher;

/**
 *
 * @author Sacha Dolski
 * @version 1.0
 *
 */
public class TargetMatcherMock implements TargetMatcher {
	private static final long serialVersionUID = 7237566385772657474L;
	private Decisions[] decisions;
	private int counter = 0;

	public TargetMatcherMock(){

	}

	public TargetMatcherMock(Decisions[] decisions) {
		this.decisions = decisions.clone();
	}
	public TargetMatcherMock(Decisions decisions) {
		this.decisions = new Decisions[]{decisions};
	}



	public boolean match(RequestType req, TargetType target, RequestInformation reqInfo) throws SyntaxException, ProcessingException{
		Decisions dec = decisions[counter];
		setCounter(counter);
		counter++;
		switch(dec) {
		case FALSE: return false;
		case PROCESSINGEXCEPTION: throw new ExpressionProcessingException();
		case SYNTAXEXCEPTION: throw new SyntaxException();
		case NULLPOINTEREXCEPTION: throw new NullPointerException();
		case TRUE: return true;
		}
		return false;
	}

	public Decisions[] getDecision() {
		return decisions.clone();
	}

	public void setDecision(Decisions[] decisions) {
		this.decisions = decisions.clone();
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	public enum Decisions {
		TRUE, FALSE, SYNTAXEXCEPTION, PROCESSINGEXCEPTION, NULLPOINTEREXCEPTION
	}
}
