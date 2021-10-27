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

package org.herasaf.xacml.core.converter;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.herasaf.xacml.core.NotInitializedException;
import org.herasaf.xacml.core.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts an URN to a function. The default functions are defined in the <a
 * href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29. January 2008</a> appendix A.3, page 113. <br>
 * 
 * @author Sacha Dolski
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class FunctionsJAXBTypeAdapter extends XmlAdapter<String, Function> {
	private static final Logger logger = LoggerFactory
			.getLogger(FunctionsJAXBTypeAdapter.class);
	private static Map<String, Function> functions = new HashMap<String, Function>();

	/**
	 * This method sets the {@link Map} containing the mapping between functions
	 * and their ID's into the converter.
	 * 
	 * @param functions
	 *            The {@link Map} containing the mapping between ID's and
	 *            functions
	 */
	public static void addFunctions(final Map<String, Function> functions) {
		FunctionsJAXBTypeAdapter.functions.putAll(functions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String marshal(final Function function) {
		String functionString;
		try {
			functionString = function.toString();
		} catch (NullPointerException e) {
			logger.error("Argument function must not be null: ", e);
			throw new IllegalArgumentException(e);
		}
		return functionString;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Function unmarshal(final String functionId) {
		Function func;
		try {
			func = functions.get(functionId);
		} catch (NullPointerException e) {
			logger.error("FunctionsJAXBTypeAdapter not properly initialized.");
			throw new NotInitializedException(e);
		}
		if (func != null) {
			return func;
		}
		throw new IllegalArgumentException("Function " + functionId
				+ " unknown.");
	}
}