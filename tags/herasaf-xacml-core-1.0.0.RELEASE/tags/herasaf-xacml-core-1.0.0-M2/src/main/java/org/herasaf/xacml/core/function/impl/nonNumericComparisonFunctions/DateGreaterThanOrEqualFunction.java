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

package org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions;

import org.herasaf.xacml.core.function.AbstractFunction;
import org.herasaf.xacml.core.function.FunctionProcessingException;
import org.herasaf.xacml.core.types.Date;

/**
 * <p>
 * The implementation of the
 * urn:oasis:names:tc:xacml:1.0:function:date-greater-than-or-equal function.
 * </p>
 * <p>
 * See: Apendix A.3 of the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 105, for further information.
 * </p>
 * 
 * @author Stefan Oberholzer
 * @version 1.0
 */
public class DateGreaterThanOrEqualFunction extends AbstractFunction {

	/** XACML function ID. */
	public static final String ID = "urn:oasis:names:tc:xacml:1.0:function:date-greater-than-or-equal";

	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc} <br>
	 * <br>
	 * Returns true if the first argument of type
	 * http://www.w3.org/2001/XMLSchema#date is greater than or equal the second
	 * argument.<br>
	 * * <br>
	 * <code style="color:red"> <b>Important Hint:</b><br>The OASIS eXtensible Access Control Markup Langugage (XACML) 2.0,
	 * Errata 29 June
	 * <a href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20</a>
	 * page 114 function urn:oasis:names:tc:xacml:1.0:function:date-greater-than-or-equal
	 * must provide an implicit time zone if no one is set.
	 * This MUST is not considered in this implementation of the function.</code>
	 */
	// FIXME Time zone awareness (see HERASAFXACMLCORE-28).
	// The OASIS eXtensible Access Control Markup Langugage (XACML) 2.0,
	// Errata 29 June
	// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
	// page 114 function
	// urn:oasis:names:tc:xacml:1.0:function:date-greater-than-or-equal
	// must provide an implicit time zone if no one is set.
	// This MUST is not considered in this implementation of the function and
	// must be fixed.
	public Object handle(Object... args) throws FunctionProcessingException {
		try {
			if (args.length != 2) {
				throw new FunctionProcessingException(
						"Invalid number of parameters");
			}
			int result = ((Date) args[0]).compareTo((Date) args[1]);
			if (result < 0) {
				return false;
			} else if (result > 0) {
				return true;
			}
			return true;

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
