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

package org.herasaf.xacml;

/**
 * Is thrown if an integrity problem with the data occured.
 *
 * @author Florian Huonder
 * @version 1.0
 */
public class DataIntegrityException extends Exception {
	private static final long serialVersionUID = -1652615076673142164L;

	/**
	 * Constructs a new exception with null as its detail message.
	 */
	public DataIntegrityException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 */
	public DataIntegrityException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 */
	public DataIntegrityException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message
	 * of (cause==null ? null : cause.toString()) (which typically contains the
	 * class and detail message of cause).
	 */
	public DataIntegrityException(Throwable cause) {
		super(cause);
	}
}