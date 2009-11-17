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

package org.herasaf.xacml.core.function.impl.logicalFunctions;

import java.math.BigInteger;

import org.herasaf.xacml.core.function.AbstractFunction;
import org.herasaf.xacml.core.function.FunctionProcessingException;

/**
 * The implementation of the urn:oasis:names:tc:xacml:1.0:function:n-of. See:
 * Apendix A.3 of the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 109, for further information.
 * 
 * @author Sacha Dolski (sdolski@solnet.ch)
 * @version 1.0
 */
public class NOFFunction extends AbstractFunction {

	private static final long serialVersionUID = 5182684637230461983L;
	private static final String ID = "urn:oasis:names:tc:xacml:1.0:function:n-of";

	/**
	 * The first argument is a {@link BigInteger}, the second argument should be
	 * a list of boolean values. The first argument specifies the
	 * minimum amount of true values that should be in the list. if the list is
	 * shorter then than minimum. value, a {@link FunctionProcessingException}
	 * is thrown. If the amount of true values is reached, true is returned,
	 * otherwise false is returned.
	 */
	public Object handle(Object... args) throws FunctionProcessingException {
		Integer minOccurence;
		int handledArrayFields = 1;
		try {
			if (args.length < 1) {
				throw new FunctionProcessingException(
						"Invalid number of parameters");
			}

			minOccurence = (int) ((BigInteger) args[0]).longValue();

			if (minOccurence > args.length - 1) {
				throw new FunctionProcessingException(
						"n-of function has less arguments then required to evaluate to true.");
			}

			while (handledArrayFields <= args.length) {
				if (minOccurence <= 0) {
					return true;
				}
				if ((Boolean) args[handledArrayFields]) {
					minOccurence--;
				}
				handledArrayFields++;
				if (minOccurence > args.length - handledArrayFields) {
					return false;
				}
			}

		} catch (ClassCastException e) {
			throw new FunctionProcessingException(e);
		} catch (FunctionProcessingException e) {
			throw e;
		} catch (Exception e) {
			throw new FunctionProcessingException(e);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFunctionId() {
		return ID;
	}

}