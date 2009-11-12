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

package org.herasaf.xacml.core.function;

import java.io.Serializable;

/**
 * TODO JAVADOC
 * 
 * The implementation of the XACML 2.0 function. See: Appendix A.3 of the <a
 * href
 * ="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 105, for further information.
 * 
 * @author Sacha Dolski
 * @author Florian Huonder
 * @version 1.0
 */
public interface Function extends Serializable {

	/**
	 * TODO JAVADOC
	 * 
	 * The handler that takes an unspecified amount of arguments to process.
	 * 
	 * @param args
	 *            The arguments needed to process the function.
	 * @return The result of the execution of the handler.
	 * @throws FunctionProcessingException
	 */
	public Object handle(Object... args) throws FunctionProcessingException;
}