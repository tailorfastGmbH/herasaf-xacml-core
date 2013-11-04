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

package org.herasaf.xacml.core.policy;

import org.herasaf.xacml.core.context.impl.MissingAttributeDetailType;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;

/**
 * This exception is thrown if a <i>must-be-present</i> attribute that is
 * missing in the request cannot be resolved (e.g. from a PIP).
 * 
 * The exception then contains the IDs of the missing attributes.
 * 
 * @author Florian Huonder
 */
public class MissingAttributeException extends Exception {
	private static final long serialVersionUID = 1L;
	private MissingAttributeDetailType missingAttribute;

	/**
	 * Initializes a new {@link MissingAttributeException}.
	 * 
	 * @param attributeId
	 *            The id of the attribute that cannot be obtained.
	 * @param dataType
	 *            The data type of the attribute that cannot be obtained.
	 * @param issuer
	 *            The issuer of the attribute that cannot be obtained.
	 */
	public MissingAttributeException(String attributeId, DataTypeAttribute<?> dataType, String issuer) {
		missingAttribute = new MissingAttributeDetailType();
		missingAttribute.setAttributeId(attributeId);
		missingAttribute.setIssuer(issuer);
		missingAttribute.setDataType(dataType);
	}

	/**
	 * Returns the {@link MissingAttributeDetailType} of the missing attribute
	 * that cannot be obtained.
	 * 
	 * @return The {@link MissingAttributeDetailType}.
	 */
	public MissingAttributeDetailType getMissingAttribute() {
		return missingAttribute;
	}
}