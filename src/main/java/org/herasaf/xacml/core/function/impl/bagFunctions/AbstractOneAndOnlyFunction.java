/*
 * Copyright 2008 - 2012 HERAS-AF (www.herasaf.org)
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

package org.herasaf.xacml.core.function.impl.bagFunctions;

import java.util.List;

import org.herasaf.xacml.core.function.AbstractFunction;
import org.herasaf.xacml.core.function.FunctionProcessingException;

/**
 * <p>
 * The implementation of the
 * urn:oasis:names:tc:xacml:1.0:function:anyURI-one-and-only functions logic.
 * </p>
 * <p>
 * See: Apendix A.3 of the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 121, for further information.
 * </p>
 * 
 * @author Sacha Dolski (sdolski@solnet.ch)
 * @version 1.0
 * 
 */
public abstract class AbstractOneAndOnlyFunction<T> extends AbstractFunction {

	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method SHALL take a bag of the Type values as an argument and SHALL
	 * return a value of the Type. It SHALL return the only value in the bag. If
	 * the bag does not have one and only one value, then the expression SHALL
	 * evaluate to "Indeterminate".
	 * </p>
	 * <p>
	 * Takes a list of values and returnes the value contained in the list or an
	 * exception if more than value is in the list.
	 * </p>
	 */
	public Object handle(Object... args) throws FunctionProcessingException {
		try {
			if (args.length != 1) {
				throw new FunctionProcessingException(
						"Invalid number of parameters");
			}
			if (((List<?>) args[0]).size() != 1) {
				throw new FunctionProcessingException(
						"Invalid number of elements");
			}
			Object returnValue = ((List<?>) args[0]).get(0);

			return returnValue;

		} catch (ClassCastException e) {
			throw new FunctionProcessingException(e);
		} catch (Exception e) {
			throw new FunctionProcessingException(e);
		}
	}

}
