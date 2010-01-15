/*
 * Copyright 2008-2010 HERAS-AF (www.herasaf.org)
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

package org.herasaf.xacml.core.api;

import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.ResponseCtx;

/**
 * The PDP interface represents a core component in XACML - The Policy Decision Point.<br />
 * A PDP is responsible for evaluating authorization requests against the deployed policies from a PolicyRepository. <br />
 * <br />
 * The PDP evaluates a request ({@link RequestCtx}) against all policies retrieved from the Policy Repository, evaluates
 * them and returns a response inlcuding the decision <br />
 * It can use a PIP (Policy Information Point) if missing attributes should be resolved during evaluation.<br />
 * A PDP has a root Policy Combining Algorithm configured that is responsible for combining decisions in case multiple
 * policies matched. <br />
 * <br />
 * XACML specifies: The Policy Decision Point is used to evaluate access control requests. For further information see
 * <a href= "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20"> OASIS eXtensible Access Control
 * Markup Langugage (XACML) 2.0, Errata 29 June 2006</a>.
 * 
 * @see {@link PolicyRepository}, {@link PIP}, {@link RequestCtx}, {@link ResponseCtx}
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public interface PDP {

    /**
     * This method evaluates an XACML access control request. This request is encapsulated in a {@link RequestCtx}
     * object. After the evaluation the result is returned in a {@link ResponseCtx} object.
     * 
     * @see The chapter about funtional requirements in the <a href=
     *      "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20"> OASIS eXtensible Access Control
     *      Markup Langugage (XACML) 2.0, Errata 29 June 2006</a> page 77, specifies in detail the evaluation process.
     * 
     * @param request
     *            The {@link RequestCtx} that shall be evaluated.
     * @return The {@link ResponseCtx} containing the result of the evaluation.
     */
    ResponseCtx evaluate(RequestCtx request);

    /**
     * Returns the {@link PolicyRepository} that this PDP uses for evaluation. <br />
     * If the used implementation is an OrderedPolicyRepository a cast must be done manually.
     * 
     * @return The {@link PolicyRepository} contained in this {@link PDP}.
     */
    PolicyRepository getPolicyRepository();

    /**
     * Returns the {@link PIP} that this PDP during evaluation for retrieving missing attributes. The {@link PIP} is an
     * optional component so it may be <code>null</code>.<br />
     * The PIP will be used during evaluation for resolving missing attributes.
     * 
     * @return The {@link PIP} contained in this {@link PDP} or <code>null</code> if none is set.
     */
    PIP getPIP();

    /**
     * Returns the {@link PolicyCombiningAlgorithm} that this PDP uses for evaluation. This {@link CombiningAlgorithm}
     * combines the evaluation results on the root level.
     * 
     * @return The root {@link CombiningAlgorithm} of this {@link PDP}.
     */
    PolicyCombiningAlgorithm getRootCombiningAlgorithm();
}