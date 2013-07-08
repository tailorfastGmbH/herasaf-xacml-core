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

package org.herasaf.xacml.core.policy.impl;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;

/**
 * Is for the result of a {@link VariableDefinitionType} or the
 * {@link VariableDefinitionType} itself. Guarantees that the expression is
 * calculatete only once.
 * 
 * @author Sacha Dolski
 * @version 1.0
 */
public interface Variable {

	/**
	 * Returns the calculateted value directly or after calculating if that is
	 * not already done.
	 * 
	 * @param request
	 *            The corresponding request.
	 * @param reqInfo
	 *            The RequestInformation instance containing the map
	 *            which containing the {@link VariableDefinitionType}s or the
	 *            calculated values. At beginning this map only contains
	 *            {@link VariableDefinitionType}s. After using one of them the
	 *            element is replaced by a {@link VariableValue} and must not be
	 *            recalculated later.
	 * @return The value of the {@link VariableDefinitionType}.
	 * @throws ProcessingException
	 * @throws MissingAttributeException
	 * @throws SyntaxException
	 */
	Object getValue(RequestType request, RequestInformation reqInfo) throws ProcessingException,
			MissingAttributeException, SyntaxException;
}