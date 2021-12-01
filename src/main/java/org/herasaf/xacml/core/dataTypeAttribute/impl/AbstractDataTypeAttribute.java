/*
 * Copyright 2009 - 2012 HERAS-AF (www.herasaf.org)
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
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;

/**
 * This abstract class may be used as basis for the implementation of a data type. It has some default implementations of common methods
 * (like {@link #equals(Object)}).
 * 
 * @param <T> the target type
 */
public abstract class AbstractDataTypeAttribute<T> implements DataTypeAttribute<T> {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** {@inheritDoc} */
    @Override
    public T convertTo(List<?> jaxbRepresentation) throws SyntaxException {
        verifySize(jaxbRepresentation, 1);
        Object simpleType = jaxbRepresentation.get(0);
        String formattedSimpleType = format((String) simpleType);
        return convertTo(formattedSimpleType);
    }

    /**
     * Convert the given jaxb representation into the target type.
     * 
     * @param jaxbRepresentation the jaxb representation to convert
     * @return the converted jaxb representation
     * @throws SyntaxException when an error occurs while converting
     */
    public abstract T convertTo(String jaxbRepresentation) throws SyntaxException;

    protected String format(String jaxbRepresentation){
        return jaxbRepresentation.trim();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getDatatypeURI();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return getDatatypeURI().hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (this.getClass().isInstance(o)) {
            return hashCode() == o.hashCode();
        }

        return false;
    }

    private void verifySize(List<?> jaxbRepresentation, int expectedSize) throws SyntaxException {
        int jaxbRepresentationSize = jaxbRepresentation.size();
        if (jaxbRepresentationSize != expectedSize) {
            String message = String.format("Exactly %s values is expected in AttributeValue.", expectedSize);
            throw new SyntaxException(message);
        }
    }
}