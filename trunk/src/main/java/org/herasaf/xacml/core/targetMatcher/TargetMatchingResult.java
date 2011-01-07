/*
 * Copyright 2009 HERAS-AF (www.herasaf.org)
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
package org.herasaf.xacml.core.targetMatcher;

import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.XACMLDefaultStatusCode;

/**
 * This implementation contains the three possible values a target evaluation
 * results in. This is specified by the XACML 2.0 specification.
 * 
 * @author Florian Huonder
 * 
 */
public enum TargetMatchingResult {

	/**
	 * The Target matched.
	 */
	MATCH("Match"),
	/**
	 * The Target did not match
	 */
	NO_MATCH("No_Match"),
	/**
	 * An error occured.
	 */
	INDETERMINATE("Indeterminate");

	private String value;

	/**
	 * Sole constructor. Programmers cannot invoke this constructor. It is for
	 * use by code emitted by the compiler in response to enum type
	 * declarations.
	 * 
	 * @param value
	 *            The value to set.
	 */
	TargetMatchingResult(String value) {
		this.value = value;
	}

	/**
	 * Returns the current value.
	 * 
	 * @return The current {@link TargetMatchingResult}.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Returns the {@link StatusCode} referenced by its String-Representation.
	 * 
	 * @param value
	 *            The String value of the {@link StatusCode}.
	 * @return The {@link StatusCode}.
	 */
	public static StatusCode getStatusCode(String value) {
		for (int i = 0; i < XACMLDefaultStatusCode.values().length; i++) {
			StatusCode code = XACMLDefaultStatusCode.values()[i];
			if (code.getValue().equals(value)) {
				return code;
			}
		}
		return null;
	}
}
