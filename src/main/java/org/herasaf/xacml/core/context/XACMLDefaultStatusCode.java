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

package org.herasaf.xacml.core.context;

/**
 * This is an implementation of the {@link StatusCode} interface. This implementation contains the four default status
 * codes of the XACML 2.0 specification.
 * 
 * @author Florian Huonder
 */
public enum XACMLDefaultStatusCode implements StatusCode {
    /**
     * This status indicates success.
     */
    OK("urn:oasis:names:tc:xacml:1.0:status:ok"),
    /**
     * This status indicates that an error occurred during policy evaluation.
     */
    PROCESSING_ERROR("urn:oasis:names:tc:xacml:1.0:status:processing-error"),
    /**
     * This status indicates that all attributes necessary to make a policy decision were not available.
     */
    MISSING_ATTRIBUTE("urn:oasis:names:tc:xacml:1.0:status:missing-attribute"),
    /**
     * This status indicates that some attribute values contained a syntax error.
     */
    SYNTAX_ERROR("urn:oasis:names:tc:xacml:1.0:status:syntax-error");

    private String value;

    /**
     * Sole constructor. Programmers cannot invoke this constructor. It is for use by code emitted by the compiler in
     * response to enum type declarations.
     * 
     * @param value
     *            The value to set.
     */
    XACMLDefaultStatusCode(String value) {
        this.value = value;
    }

    /**
     * Returns the current value.
     * 
     * @return The current {@link StatusCode}.
     */
    @Override
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