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

package org.herasaf.xacml.core.targetMatcher;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.impl.TargetType;

/**
 * TODO REVIEW René.
 * 
 * The target matcher is responsible of matching a policy's, a policy set's or a
 * rule's target to an incoming request. This component determines based on the
 * provided attributes (in the request) whether a policy (or policy, rule) is
 * applicable for the current request.
 * 
 * @author Sacha Dolski
 */
public interface TargetMatcher {
	/**
	 * TODO REVIEW René.
	 * 
	 * This method matches the given target with the given requests. It returns
	 * true if the target matched, false otherwise.
	 * 
	 * @param request
	 *            The {@link RequestType} containing the attributes.
	 * @param target
	 *            The target of a policy, policy set or rule that shall be
	 *            checked for applicability.
	 * @return True if the target matched the request, false otherwise.
	 * @throws SyntaxException
	 *             If the request contains illegal attributes (e.g. wrong
	 *             attribute data type).
	 * @throws ProcessingException
	 *             If more than one value is provided in an attribute value.
	 * @throws MissingAttributeException
	 *             If a must-be-present attribute cannot be resolved (neither
	 *             from the request nor from a PIP).
	 */
	boolean match(RequestType request, TargetType target, RequestInformation requestInformation)
			throws SyntaxException, ProcessingException, MissingAttributeException;
}