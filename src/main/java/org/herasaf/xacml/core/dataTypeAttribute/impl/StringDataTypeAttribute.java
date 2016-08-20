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

package org.herasaf.xacml.core.dataTypeAttribute.impl;

import java.util.List;

import org.herasaf.xacml.core.SyntaxException;

/**
 * This data type represents a http://www.w3.org/2001/XMLSchema#string. See: <A HREF="http://www.w3.org/TR/xmlschema-2/#string"
 * target="_blank">http://www.w3.org/TR/xmlschema-2/#string</A> for further information.
 */
public class StringDataTypeAttribute extends AbstractDataTypeAttribute<String> {
    public static final String ID = "http://www.w3.org/2001/XMLSchema#string";
    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     * <p>
     * <b>This implementation:</b>
     * <p>
     * Interprets an empty list as an empty {@link String} value.
     */
    @Override
    public String convertTo(List<?> jaxbRepresentation) throws SyntaxException {
        boolean isEmptyList = jaxbRepresentation.isEmpty();
        if (isEmptyList) {
            return "";
        }
        String convertedValue = super.convertTo(jaxbRepresentation);
        return convertedValue;
    }

    /** {@inheritDoc} */
    public String convertTo(String jaxbRepresentation) {
        return jaxbRepresentation;
    }

    /** {@inheritDoc} */
    public String getDatatypeURI() {
        return ID;
    }
}