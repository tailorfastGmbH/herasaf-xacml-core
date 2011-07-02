/*
 * Copyright 2008 - 2011 HERAS-AF (www.herasaf.org)
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

package org.herasaf.xacml.core.function;

import java.io.Serializable;

/**
 * All functions that shall be used in the PDP must implement this interface.
 * All XACML 2.0 default functions can be found here: Appendix A.3 of the <a
 * href
 * ="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a>, page 113.
 * 
 * @author Sacha Dolski
 * @author Florian Huonder
 */
public interface Function extends Serializable {

	/**
	 * Returns the URI ID for this Function.
	 * 
	 * @return URI ID for this function.
	 */
	String getFunctionId();

	/**
	 * The handler that takes an unspecified number of arguments to process. The
	 * exact number of arguments depends on the implementation of the function.
	 * 
	 * @param args
	 *            The arguments needed to process the function.
	 * @return The result of the execution of the function.
	 * @throws FunctionProcessingException
	 *             In case an error occurs while processing the function.
	 */
	Object handle(Object... args) throws FunctionProcessingException;
}