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
 * TODO JAVADOC
 * 
 * The {@link TargetMatcher} is used to match a given {@link TargetType} to a
 * {@link RequestType}.
 * 
 * @author Sacha Dolski (sdolski@solnet.ch)
 */
public interface TargetMatcher {
	/**
	 * TODO JAVADOC
	 * 
	 * Matches a {@link TargetType} to a {@link RequestType}.
	 * 
	 * @param req
	 *            The {@link RequestType} for the match.
	 * @param target
	 *            The {@link TargetType} for the match.
	 * @return An boolean containing the result of the matching.
	 * @throws SyntaxException
	 * @throws ProcessingException
	 */
	public boolean match(RequestType req, TargetType target, RequestInformation requestInformation)
			throws SyntaxException, ProcessingException, MissingAttributeException;
}