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

package org.herasaf.xacml.core.policy;

import java.io.Serializable;
import java.util.List;

import org.herasaf.xacml.core.combiningAlgorithm.CombiningAlgorithm;
import org.herasaf.xacml.core.policy.impl.EffectType;
import org.herasaf.xacml.core.policy.impl.ObligationType;
import org.herasaf.xacml.core.policy.impl.TargetType;

/**
 * An evaluatable is an evaluable type. Such a type represents an policy or policy set in XAMCL.
 * 
 * @author Florian Huonder
 */
public interface Evaluatable extends Serializable {

    /**
     * This method returns the ID of the evaluatable.
     * 
     * @return The ID of the evaluatable.
     */
    EvaluatableID getId();

    /**
     * Returns the target of the evaluatable.
     * 
     * @return The target of the evaluatable.
     */
    TargetType getTarget();

    /**
     * Returns the combining algorithm of the evaluatable.
     * 
     * @return The combining algorithm of the evaluatable.
     */
    CombiningAlgorithm getCombiningAlg();

    /**
     * Returns the version of the evaluatable.
     * 
     * @return The version of the evaluatable.
     */
    String getEvalutableVersion();

    /**
     * Returns a boolean value indicating if the current {@link Evaluatable} or a sub-{@link Evaluatable} contains one
     * or more <code>&lt;Obligation&gt;</code>.
     * 
     * @return True if the current or a sub-{@link Evaluatable} contains one or more <code>&lt;Obligation&gt;</code>,
     *         false otherwise.
     */
    boolean hasObligations();

    /**
     * Returns the <code>&lt;Obligation&gt;</code>s of this {@link Evaluatable} that match the given {@link EffectType}.
     * 
     * @param effect
     *            The {@link EffectType} to which the returned <code>&lt;Obligation&gt;</code>s must match.
     * @return The <code>&lt;Obligation&gt;</code>s that match the {@link EffectType}.
     */
    List<ObligationType> getContainedObligations(EffectType effect);
}