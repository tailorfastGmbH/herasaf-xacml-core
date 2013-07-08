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

package org.herasaf.xacml.core.policy.combiningAlgorithm.rule.impl.test;

import javax.xml.bind.JAXBElement;

import org.herasaf.xacml.core.policy.impl.ApplyType;
import org.herasaf.xacml.core.policy.impl.ConditionType;
import org.herasaf.xacml.core.policy.impl.ExpressionType;
import org.herasaf.xacml.core.policy.impl.ObjectFactory;

/**
 * A mock for a {@link ConditionType}.
 * 
 * @author Florian Huonder
 */
public class ConditionMock extends ConditionType {
	private static final long serialVersionUID = 1L;
	private ApplyType expression;
	private static ObjectFactory factory;
	
	/**
	 * Initializes the ObjectFactory.
	 */
	static {
		factory = new ObjectFactory();
	}
	
	/**
	 * Creaes a new mock and initializes the containing {@link ExpressionType} that is an {@link ApplyTypeMock} here.
	 * 
	 * @param b The returnValue of the {@link ExpressionType}.
	 * @param exception The {@link Exception} that shall be thrown by the {@link ExpressionType}.
	 */
	public ConditionMock(Object b, Exception exception) {
		expression = new ApplyTypeMock(b, exception);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JAXBElement<?> getExpression() {
		return factory.createApply(expression);
	}
}