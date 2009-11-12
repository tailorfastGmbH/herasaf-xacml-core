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

package org.herasaf.xacml.core.function.impl.arithmeticFunctions;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.FunctionProcessingException;

/**
 * <p>
 * The implementation of the urn:oasis:names:tc:xacml:1.0:function:double-add
 * function.
 * </p>
 * <p>
 * See: Apendix A.3 of the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 108, for further information.
 * </p>
 * 
 * @author Florian Huonder
 * @version 1.0
 */
public class DoubleAddFunction implements Function {

	private static final long serialVersionUID = -5221448849355031705L;
	private static final String ID = "urn:oasis:names:tc:xacml:1.0:function:double-add";

	/**
	 * {@inheritDoc} takes at least 2 {@link Double} values and returnes the sum
	 * of them as {@link Double} value.
	 */
	public Object handle(Object... args) throws FunctionProcessingException {
		try {
			if (args.length < 2) {
				throw new FunctionProcessingException("Invalid number of parameters.");
			}
			Double[] integers = new Double[args.length];
			for (int i = 0; i < args.length; i++) {
				integers[i] = (Double) args[i];
			}

			Double result = new Double("0");
			for (Double i : integers) {
				result = result.doubleValue() + i.doubleValue();
			}
			return result;
		} catch (ClassCastException e) {
			throw new FunctionProcessingException("The arguments were of the wrong datatype.");
		} catch (Exception e) {
			throw new FunctionProcessingException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ID;
	}
}