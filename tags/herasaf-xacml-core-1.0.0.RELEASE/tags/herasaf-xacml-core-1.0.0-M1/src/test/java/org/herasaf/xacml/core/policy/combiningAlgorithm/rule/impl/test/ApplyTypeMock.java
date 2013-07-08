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

package org.herasaf.xacml.core.policy.combiningAlgorithm.rule.impl.test;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.impl.ApplyType;

/**
 * This is a mock for the {@link ApplyType}.
 * 
 * @author Florian Huonder
 */
public class ApplyTypeMock extends ApplyType {
	private static final long serialVersionUID = 1280368586961289345L;
	private Object returnValue;
	private Exception returnException;

	/**
	 * Creates a new mock with a given return value.
	 * 
	 * @param returnValue The return value of the mock.
	 */
	public ApplyTypeMock(Object returnValue) {
		this.returnValue = returnValue;
	}

	/**
	 * Creates a new mock with a given {@link Exception} to be thrown.
	 * 
	 * @param exception The {@link Exception} to throw.
	 */
	public ApplyTypeMock(Exception exception) {
		this.returnException = exception;
	}

	/**
	 * creates a new mock with a given {@link Exception} and a given return value.
	 * 
	 * @param returnValue The return value of the mock.
	 * @param exception The {@link Exception} that shall be thrown.
	 */
	public ApplyTypeMock(Object returnValue, Exception exception) {
		this.returnValue =  returnValue;
		returnException = exception;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Throw the given {@link Exception} or return the given returnValue. 
	 */
	@Override
	public Object handle(RequestType request,
			RequestInformation reqInfo)
			throws ProcessingException, MissingAttributeException,
			org.herasaf.xacml.core.SyntaxException {
		if (returnException != null) {
			if (returnException instanceof ProcessingException) {
				throw (ProcessingException) returnException;
			}
			if (returnException instanceof MissingAttributeException) {
				throw (MissingAttributeException) returnException;
			}
			if (returnException instanceof org.herasaf.xacml.core.SyntaxException) {
				throw (SyntaxException) returnException;
			}
		}
		return returnValue;
	}
}