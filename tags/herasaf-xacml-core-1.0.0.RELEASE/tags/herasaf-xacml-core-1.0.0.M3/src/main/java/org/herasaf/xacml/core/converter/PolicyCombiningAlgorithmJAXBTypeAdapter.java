/*
 * Copyright 2008 - 2012 HERAS-AF (www.herasaf.org)
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

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.herasaf.xacml.core.NotInitializedException;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts an URN to a {@link PolicyCombiningAlgorithm}. The default
 * {@link PolicyCombiningAlgorithm}s are defined in the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29. January 2008</a> appendix C page 140. <br>
 * 
 * @author Sacha Dolski
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class PolicyCombiningAlgorithmJAXBTypeAdapter extends
		XmlAdapter<String, PolicyCombiningAlgorithm> {
	private transient final Logger logger = LoggerFactory
			.getLogger(PolicyCombiningAlgorithmJAXBTypeAdapter.class);
	private static Map<String, PolicyCombiningAlgorithm> combiningAlgorithms;

	/**
	 * This method sets the {@link Map} containing the mapping between policy
	 * combining algorithms and their ID's into the converter.
	 * 
	 * @param algorithms
	 *            The {@link Map} containing the mapping between ID's and policy
	 *            combining algorithms.
	 */
	public static void setCombiningAlgorithms(
			final Map<String, PolicyCombiningAlgorithm> algorithms) {
		combiningAlgorithms = algorithms;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String marshal(final PolicyCombiningAlgorithm combAlg) {
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
	public PolicyCombiningAlgorithm unmarshal(final String combAlgId) {
		PolicyCombiningAlgorithm combAlg;
		try {
			combAlg = combiningAlgorithms.get(combAlgId);
		} catch (NullPointerException e) {
			logger
					.error("PolicyCombiningAlgorithmJAXBTypeAdapter not properly initialized.");
			throw new NotInitializedException(e);
		}
		if (combAlg != null) {
			return combAlg;
		}
		throw new IllegalArgumentException("Combining Algorithm " + combAlgId
				+ " unknown.");
	}
}