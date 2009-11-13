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

package org.herasaf.xacml.core.function;

/**
 * <p>
 * This is a base class for {@link Function} implementations.
 * </p>
 * 
 * @author Ylli Sylejmani
 * @version 1.0
 */
public abstract class AbstractFunction implements Function {

	/** Serial version UID. */
	private static final long serialVersionUID = 7067251654981893558L;

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return getFunctionId();
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return getFunctionId().hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj == this) {
			return true;
		}

		if (this.getClass().isInstance(obj)) {
			return hashCode() == obj.hashCode();
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract String getFunctionId();

}
