/*
 * Copyright 2008 - 2011 HERAS-AF (www.herasaf.org)
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

package org.herasaf.xacml.core.policy.impl;

import org.herasaf.xacml.core.policy.EvaluatableID;

/**
 * This class represents the ID of an evaluatable.
 * 
 * @author Florian Huonder
 */
public class EvaluatableIDImpl implements EvaluatableID {
    private String id;

    /**
     * Creates a new {@link EvaluatableID} with the given id.
     * 
     * @param id
     *            The id of this {@link EvaluatableID}.
     */
    public EvaluatableIDImpl(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof EvaluatableID) {
            return ((EvaluatableID) obj).getId().equals(id);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}