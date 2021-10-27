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
import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleCombiningAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts an URN to a {@link RuleCombiningAlgorithm}. The default
 * {@link RuleCombiningAlgorithm}s are defined in the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29. January 2008</a> appendix C, page 140. <br>
 * 
 * @author Sacha Dolski
 * @author René Eggenschwiler
 * @author Florian Huonder
 */
public class RuleCombiningAlgorithmJAXBTypeAdapter extends
		XmlAdapter<String, RuleCombiningAlgorithm> {
	private static final Logger logger = LoggerFactory
			.getLogger(RuleCombiningAlgorithmJAXBTypeAdapter.class);
	private static Map<String, RuleCombiningAlgorithm> combiningAlgorithms = new HashMap<String, RuleCombiningAlgorithm>();

	/**
	 * This method sets the {@link Map} containing the mapping between rule
	 * combining algorithms and their ID's into the converter.
	 * 
	 * @param algorithms
	 *            The {@link Map} containing the mapping between ID's and rule
	 *            combining algorithms.
	 */
	public static void addCombiningAlgorithms(
			final Map<String, RuleCombiningAlgorithm> algorithms) {
		combiningAlgorithms.putAll(algorithms);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String marshal(final RuleCombiningAlgorithm combAlg) {
		String combAlgString;
		try {
			combAlgString = combAlg.toString();
		} catch (NullPointerException e) {
			logger.error("Argument combAlg must not be null: ", e);
			throw new IllegalArgumentException(e);
		}
		return combAlgString;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RuleCombiningAlgorithm unmarshal(final String combAlgId) {
		RuleCombiningAlgorithm combAlg;
		try {
			combAlg = combiningAlgorithms.get(combAlgId);
		} catch (NullPointerException e) {
			logger
					.error("RuleCombiningAlgorithmJAXBTypeAdapter not properly initialized.");
			throw new NotInitializedException(e);
		}
		if (combAlg != null) {
			return combAlg;
		}
		throw new IllegalArgumentException("Combining Algorithm " + combAlgId
				+ " unknown.");
	}
}