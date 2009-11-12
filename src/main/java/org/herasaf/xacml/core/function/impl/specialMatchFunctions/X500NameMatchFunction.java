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

package org.herasaf.xacml.core.function.impl.specialMatchFunctions;

import javax.security.auth.x500.X500Principal;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.FunctionProcessingException;

/**
 * <p>
 * The implementation of the
 * urn:oasis:names:tc:xacml:1.0:function:x500Name-match function.
 * </p>
 * <p>
 * See: Apendix A.3 of the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 105, for further information.
 * </p>
 * 
 * @author Sacha Dolski (sdolski@solnet.ch)
 * @version 1.0
 */
public class X500NameMatchFunction implements Function {
	private static final long serialVersionUID = -3491926450245801282L;
	private static final String ID = "urn:oasis:names:tc:xacml:1.0:function:x500Name-match";

	/**
	 * {@inheritDoc} <br>
	 * <br>
	 * Returns true if the two arguments of type {@link X500Principal} match.
	 */
	public Object handle(Object... args) throws FunctionProcessingException {
		try {
			if (args.length != 2) {
				throw new FunctionProcessingException("Invalid number of parameters");
			}
			String[] expectedName = ((X500Principal) args[0]).getName(X500Principal.RFC2253).split(",");
			String[] comparedName = ((X500Principal) args[1]).getName(X500Principal.RFC2253).split(",");
			for (int i = 0; i < expectedName.length; i += 1) {
				boolean found = false;
				for (int k = 0; k < comparedName.length; k += 1) {
					if (expectedName[i].trim().equals(comparedName[k].trim())) {
						found = true;
					}
				}
				if (!found) {
					return false;
				}
			}
			return (true);
		} catch (ClassCastException e) {
			throw new FunctionProcessingException("The arguments were of the wrong datatype.");
		} catch (FunctionProcessingException e) {
			throw e;
		} catch (Exception e) {
			throw new FunctionProcessingException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.core.function.FunctionAC#toString()
	 */
	@Override
	public String toString() {
		return ID;
	}
}