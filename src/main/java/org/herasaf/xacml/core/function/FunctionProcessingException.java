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

package org.herasaf.xacml.core.function;

import org.herasaf.xacml.core.ProcessingException;

/**
 * TODO JAVADOC
 * 
 * This exception is thrown when the processing of the a Function fails.
 *
 * @author Florian Huonder
 */
public class FunctionProcessingException extends ProcessingException {
	private static final long serialVersionUID = 8213802064654257318L;

	/**
	 * Constructs a new exception with null as its detail message.
	 */
	public FunctionProcessingException(){
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 */
	public FunctionProcessingException(String message){
		super(message);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 */
	public FunctionProcessingException(String message, Throwable cause){
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message of (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
	 */
	public FunctionProcessingException(Throwable cause){
		super(cause);
	}
}