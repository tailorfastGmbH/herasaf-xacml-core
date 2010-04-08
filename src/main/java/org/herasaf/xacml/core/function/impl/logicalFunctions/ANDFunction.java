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

package org.herasaf.xacml.core.function.impl.logicalFunctions;

import org.herasaf.xacml.core.function.AbstractFunction;
import org.herasaf.xacml.core.function.FunctionProcessingException;

/**
 * The implementation of the urn:oasis:names:tc:xacml:1.0:function:and function.
 * See: Apendix A.3 of the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 109, for further information.
 * 
 * @author Sacha Dolski (sdolski@solnet.ch)
 * @version 1.0
 */
public class ANDFunction extends AbstractFunction {

	/** XACML function ID. */
	public static final String ID = "urn:oasis:names:tc:xacml:1.0:function:and";

	private static final long serialVersionUID = 1L;

	/**
	 * Takes at least one {@link Boolean} value returns <code>true</code> if all
	 * of them are <code>true</code>. Otherwise false is returnes.
	 */
	public Object handle(Object... args) throws FunctionProcessingException {
		try {
			if (args.length == 0) {
				return true;
			}
			for (Object obj : args) {
				if (!((Boolean) obj)) {
					return false;
				}
			}
		} catch (ClassCastException e) {
			throw new FunctionProcessingException(e);
		} catch (Exception e) {
			throw new FunctionProcessingException(e);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFunctionId() {
		return ID;
	}

}
