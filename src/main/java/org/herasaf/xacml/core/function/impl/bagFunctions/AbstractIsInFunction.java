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

package org.herasaf.xacml.core.function.impl.bagFunctions;

import java.util.List;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.FunctionProcessingException;

/**
 * <p>
 * The implementation of the urn:oasis:names:tc:xacml:1.0:function:anyURI-is-in
 * functions logic.
 * </p>
 * <p>
 * See: Apendix A.3 of the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 116, for further information.
 * </p>
 * 
 * @author Stefan Oberholzer
 * @version 1.0
 * 
 * @param <T>
 *            Implemented Type.
 */
public abstract class AbstractIsInFunction<T> implements Function {

	private static final long serialVersionUID = -7932230812638065901L;

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method SHALL take an argument of ‘type’ as the first argument and a
	 * bag of type values as the second argument and SHALL return an
	 * “http://www.w3.org/2001/XMLSchema#boolean”. The function SHALL evaluate
	 * to "True" if and only if the first argument matches by the
	 * "urn:oasis:names:tc:xacml:x.x:function:typeequal" any value in the bag.
	 * Otherwise, it SHALL return “False”.
	 * </p>
	 * <p>
	 * takes a value as first parameter and a {@link List} as second parameter
	 * and returns a {@link Boolean} specifying if the value is in the list or
	 * not.
	 * </p>
	 */
	@SuppressWarnings("unchecked")
	public Object handle(Object... args) throws FunctionProcessingException {
		try {
			if (args.length != 2) {
				throw new FunctionProcessingException("Invalid number of parameters");
			}
			List<T> bagValues = ((List<T>) args[1]);
			for (T val : bagValues) {
				if (val.equals(args[0])) {
					return true;
				}
			}
			return false;
		} catch (ClassCastException e) {
			throw new FunctionProcessingException(e);
		} catch (Exception e) {
			throw new FunctionProcessingException(e);
		}
	}
}
