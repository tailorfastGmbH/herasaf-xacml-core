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

package org.herasaf.xacml.core.function.impl.bagFunctions;

import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.function.AbstractFunction;
import org.herasaf.xacml.core.function.FunctionProcessingException;

/**
 * <p>
 * The implementation of the urn:oasis:names:tc:xacml:1.0:function:type-bag
 * functions logic.
 * </p>
 * <p>
 * See: Apendix A.3 of the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 121, for further information.
 * </p>
 * 
 * @author Sacha Dolski (sdolski@solnet.ch)
 * @version 1.0
 * @param <T>
 *            Implemented Type.
 */
public abstract class AbstractBagFunction<T> extends AbstractFunction {

	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method SHALL take any number of arguments of the Type and return a
	 * bag of the Type values containing the values of the arguments. An
	 * application of this function to zero arguments SHALL produce an empty bag
	 * of the specified data-type.
	 * </p>
	 * <p>
	 * takes any number of arguments of the type value and returnes a
	 * {@link List} containing the values.
	 * </p>
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object handle(Object... args) throws FunctionProcessingException {
		List<T> stringBag;
		try {
			stringBag = new ArrayList<T>();
			for (Object obj : args) {
				stringBag.add((T) obj);
			}
		} catch (ClassCastException e) {
			throw new FunctionProcessingException(e);
		}

		return stringBag;
	}

}
