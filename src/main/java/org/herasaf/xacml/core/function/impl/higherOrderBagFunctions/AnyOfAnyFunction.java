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

package org.herasaf.xacml.core.function.impl.higherOrderBagFunctions;

import java.util.List;

import org.herasaf.xacml.core.function.AbstractFunction;
import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.FunctionProcessingException;

/**
 * The implementation of the urn:oasis:names:tc:xacml:1.0:function:any-of-any
 * function. See: Apendix A.3 of the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 122, for further information.
 * 
 * @author Sacha Dolski (sdolski@solnet.ch)
 * @version 1.0
 */

public class AnyOfAnyFunction extends AbstractFunction {

	/** XACML function ID. */
	public static final String ID = "urn:oasis:names:tc:xacml:1.0:function:any-of-any";

	private static final int VALID_LENGTH = 3;
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc} Takes a boolean {@link Function} as first parameter, and
	 * two {@link List}s as second an third parameter. Applies die function with
	 * every value of the first list with every value of the second list. If one
	 * function call returnes true, <code>true</code> is returned.
	 */
	public Object handle(Object... args) throws FunctionProcessingException {
		try {
			if (args.length != VALID_LENGTH) {
				throw new FunctionProcessingException(
						"Invalid number of parameters");
			}
			Function function = (Function) args[0];
			for (Object obj1 : ((List<?>) args[1])) {
				for (Object obj2 : ((List<?>) args[2])) {
					if ((Boolean) function.handle(obj1, obj2)) {
						return true;
					}
				}
			}
			return false;
		} catch (ClassCastException e) {
			throw new FunctionProcessingException(e);
		} catch (FunctionProcessingException e) {
			throw e;
		} catch (Exception e) {
			throw new FunctionProcessingException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFunctionId() {
		return ID;
	}

}
