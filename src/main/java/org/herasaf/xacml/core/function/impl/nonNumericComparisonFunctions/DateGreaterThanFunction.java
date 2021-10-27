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

package org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions;

import org.herasaf.xacml.core.function.AbstractFunction;
import org.herasaf.xacml.core.function.FunctionProcessingException;
import org.herasaf.xacml.core.types.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * The implementation of the
 * urn:oasis:names:tc:xacml:1.0:function:date-greater-than function.
 * </p>
 * <p>
 * See: Apendix A.3 of the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 118, for further information.
 * </p>
 * 
 * @author Florian Huonder
 */
public class DateGreaterThanFunction extends AbstractFunction {

	/** XACML function ID. */
	public static final String ID = "urn:oasis:names:tc:xacml:1.0:function:date-greater-than";
	private static final Logger logger = LoggerFactory.getLogger(DateGreaterThanFunction.class);
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc} <br>
	 * <br>
	 * Returns true if the first argument of type
	 * http://www.w3.org/2001/XMLSchema#date is greater than the second
	 * argument.<br>
	 * <br>
	 * <br>OASIS eXtensible Access Control Markup Langugage (XACML) 2.0,
	 * Errata 29 January 2008
	 * <a href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20</a>
	 * function urn:oasis:names:tc:xacml:1.0:function:date-greater-than page 120.
	 * 
	 * <b>If no time zone is provided an implicit default time zone must be used.
	 * The default time zone is derived from the system property {@code user.timezone}.
     * If that is {@code null} or is not a valid identifier, then the value of the
     * JDK {@code TimeZone} default is converted. If that fails, {@code UTC} is used.</b>
	 */
	@Override
	public Object handle(Object... args) throws FunctionProcessingException {
		try {
			if (args.length != 2) {
				logger.error("Function {} requires two arguments but {} arguments were passed.", ID, args.length);
				throw new FunctionProcessingException(
						"Invalid number of parameters");
			}
			
			int result = ((Date) args[0]).compareTo((Date) args[1]);
			
			if (result < 0) {
				return false;
			} else if (result > 0) {
				return true;
			}
			return false;

		} catch (IllegalArgumentException e) {
			logger.error("Either argument is not supported by this function.");
			throw new FunctionProcessingException(e);
		} catch (Exception e) {
			logger.error("An unexpected error occured.");
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