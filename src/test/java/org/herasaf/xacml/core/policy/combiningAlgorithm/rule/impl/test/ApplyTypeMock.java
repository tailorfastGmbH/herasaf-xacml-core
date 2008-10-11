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

package org.herasaf.xacml.core.policy.combiningAlgorithm.rule.impl.test;

import org.herasaf.xacml.SyntaxException;
import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.impl.ApplyType;

public class ApplyTypeMock extends ApplyType {

	private static final long serialVersionUID = 1L;

	private Object returnValue;
	private Exception returnException;

	public ApplyTypeMock(Object returnValue) {
		this.returnValue = returnValue;
	}

	public ApplyTypeMock(Exception exception) {
		this.returnException = exception;
	}

	public ApplyTypeMock(Object b, Exception exception) {
		returnValue =  b;
		returnException = exception;
	}

	@Override
	public Object handle(RequestType request,
			RequestInformation reqInfo)
			throws ProcessingException, MissingAttributeException,
			SyntaxException {
		if (returnException != null) {
			if (returnException instanceof ProcessingException) {
				throw (ProcessingException) returnException;
			}
			if (returnException instanceof MissingAttributeException) {
				throw (MissingAttributeException) returnException;
			}
			if (returnException instanceof SyntaxException) {
				throw (SyntaxException) returnException;
			}
		}
		return returnValue;
	}
}
