/*
 * Copyright 2009 HERAS-AF (www.herasaf.org)
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

import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;

/**
 * TODO JAVADOC
 * 
 * Base class for {@link DataTypeAttribute} implementations.
 * 
 * @param <DataType>
 *            the type of data handled by this data type
 */
public abstract class AbstractDataTypeAttribute<DataType> implements DataTypeAttribute<DataType> {

	/** Serial version UID. */
	private static final long serialVersionUID = -7160783590484665708L;

	/** {@inheritDoc} */
	public String toString() {
		return getDatatypeURI();
	}

	/** {@inheritDoc} */
	public int hashCode() {
		return getDatatypeURI().hashCode();
	}

	/** {@inheritDoc} */
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
}