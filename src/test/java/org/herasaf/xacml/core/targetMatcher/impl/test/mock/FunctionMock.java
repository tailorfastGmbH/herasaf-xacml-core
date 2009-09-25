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

package org.herasaf.xacml.core.targetMatcher.impl.test.mock;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.FunctionProcessingException;

/**
 * A mock of the {@link Function} type.
 * 
 * @author Florian Huonder
 */
public class FunctionMock implements Function {
	private static final long serialVersionUID = -5717424357552861113L;
	private boolean exceptionProcessing;

	/**
	 * Initializes the mock.
	 * @param exceptionProcessing True if the mock shall throw an exception.
	 */
	public FunctionMock(boolean exceptionProcessing) {
		this.exceptionProcessing = exceptionProcessing;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Throws an exception if set so or returns the request values.
	 */
	public Object handle(Object... args) throws FunctionProcessingException {
		if(exceptionProcessing){
			throw new FunctionProcessingException();
		}
		return args[0].equals(args[1]);
	}
}