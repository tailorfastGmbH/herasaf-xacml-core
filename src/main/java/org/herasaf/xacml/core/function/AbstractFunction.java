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

package org.herasaf.xacml.core.function;

/**
 * This abstract class may be used as basis for the implementation of a function.
 * It has some default implementations of common methods (like {@link #equals(Object)}).
 * 
 * @author Ylli Sylejmani
 */
public abstract class AbstractFunction implements Function {
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getFunctionId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return getFunctionId().hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
	@Override
	public abstract String getFunctionId();
}