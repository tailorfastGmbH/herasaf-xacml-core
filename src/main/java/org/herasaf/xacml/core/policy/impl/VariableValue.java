/*
 * Copyright 2008 - 2011 HERAS-AF (www.herasaf.org)
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

package org.herasaf.xacml.core.policy.impl;

import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.impl.RequestType;

/**
 * The result of the calculation if the Expression in a
 * {@link VariableDefinitionType}.
 * 
 * @author Sacha Dolski
 * @version 1.0
 */
public class VariableValue implements Variable {
	private Object value;

	/**
	 * Initzializes an empty value.
	 */
	public VariableValue() {
	}

	/**
	 * Initializes the Variable value with the given value.
	 * 
	 * @param value
	 *            The value to place in this {@link VariableValue}.
	 */
	public VariableValue(Object value) {
		this();
		this.value = value;
	}

	/**
	 * Sets the value of this {@link VariableValue}.
	 * 
	 * @param value
	 *            The value to set.
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(RequestType request, EvaluationContext evaluationContext) {
		return value;
	}
}