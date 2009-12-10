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

package org.herasaf.xacml.core.api;

import org.herasaf.xacml.core.combiningAlgorithm.CombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.ResponseCtx;

/**
 * TODO REVIEW René.
 * 
 * This interface describes the Policy Decision Point. The Policy Decision Point
 * is used to evaluate access control requests. For further information see <a
 * href="http://docs.oasis-open.org/xacml/2.0/access_control
 * -xacml-2.0-mult-profile-spec-os.pdf" >Multiple resource profile of XACML
 * v2.0, 1 February 2005</a>..
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public interface PDP {

	/**
	 * TODO REVIEW René.
	 * 
	 * This method evaluates an XACML access control request. This request is
	 * encapsulated in a {@link RequestCtx} object. After the evaluation the
	 * result is returned in a {@link ResponseCtx} object.
	 * 
	 * @param request
	 *            The {@link RequestCtx} that shall be evaluated.
	 * @return The {@link ResponseCtx} containing the result of the evaluation.
	 */
	ResponseCtx evaluate(RequestCtx request);

	/**
	 * TODO REVIEW René.
	 * 
	 * Returns the {@link PolicyRepository} that this PDP uses for evaluation.
	 * 
	 * @return The {@link PolicyRepository} contained in this {@link PDP}.
	 */
	PolicyRepository getPolicyRepository();

	/**
	 * TODO REVIEW René.
	 * 
	 * Returns the {@link PIP} that this PDP uses for evaluation. The
	 * {@link PIP} is an optional component so it may be <code>null</code>.
	 * 
	 * @return The {@link PIP} contained in this {@link PDP} or
	 *         <code>null</code> if none is set.
	 */
	PIP getPIP();

	/**
	 * TODO REVIEW René.
	 * 
	 * Returns the {@link PolicyCombiningAlgorithm} that this PDP uses for evaluation.
	 * This {@link CombiningAlgorithm} combines the evaluation results on the root level.
	 * 
	 * @return The root {@link CombiningAlgorithm} of this {@link PDP}.
	 */
	PolicyCombiningAlgorithm getRootCombiningAlgorithm();
}