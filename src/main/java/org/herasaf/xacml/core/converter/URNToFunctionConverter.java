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

package org.herasaf.xacml.core.converter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.herasaf.xacml.core.NotInitializedException;
import org.herasaf.xacml.core.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts an URN to a function. The functions are defined in the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> appendix A.3, page 105. <br>
 * <br>
 * The {@link Map} containing the mapping between URNs and functions is static.
 * The setter for this {@link Map} is NOT static. The filling of this
 * {@link Map} takes place through the <a
 * href="http://www.springframework.org/">Springframework</a>.
 * 
 * @author Sacha Dolski
 * @author Florian Huonder
 * @author René Eggenschwiler
 * @version 1.1
 * @see Function
 */
public class URNToFunctionConverter extends XmlAdapter<String, Function> {
	private static final Logger logger = LoggerFactory
			.getLogger(URNToFunctionConverter.class);

	/**
	 * Contains all the available functions
	 */
	static Map<String, Function> functions;

	/**
	 * Is used by the <a
	 * href="http://www.springframework.org/">Springframework</a> to fill the
	 * static {@link Map} containing the mapping between URNs and functions.
	 * 
	 * @param functions
	 *            The map containing the mapping between URNs and functions.
	 */
	public static void setFunctions(Map<String, Function> functions) {
		URNToFunctionConverter.functions = new ConcurrentHashMap<String, Function>(
				functions); // ConcurrentHashMap because of concurrent access
		// possible
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(Function function) throws IllegalArgumentException {
		String functionString;
		try {
			functionString = function.toString();
		} catch (NullPointerException e) {
			logger.error("Argument function must not be null: ", e);
			throw new IllegalArgumentException(e);
		}
		return functionString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Function unmarshal(String functionId)
			throws IllegalArgumentException {
		Function func;
		try {
			func = functions.get(functionId);
		} catch (NullPointerException e) {
			logger.error("URNToFunctionConverter not properly initialized.");
			throw new NotInitializedException(e);
		}
		if (func != null) {
			return func;
		}
		throw new IllegalArgumentException("Function " + functionId
				+ " unknown.");
	}
}