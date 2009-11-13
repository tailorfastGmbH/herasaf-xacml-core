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
import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleCombiningAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO JAVADOC
 * 
 * Converts an URN to a {@link RuleCombiningAlgorithm}. The
 * {@link RuleCombiningAlgorithm}s are defined in the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> appendix C, page 133. <br>
 * <br>
 * The {@link Map} containing the mapping between URNs and
 * {@link RuleCombiningAlgorithm}s is static. The setter for this {@link Map} is
 * NOT static. The filling of this {@link Map} takes place through the <a
 * href="http://www.springframework.org/">Springframework</a>.
 * 
 * @author Sacha Dolski
 * @author René Eggenschwiler
 * @author Florian Huonder
 * @version 1.0
 */
public class URNToRuleCombiningAlgorithmConverter extends XmlAdapter<String, RuleCombiningAlgorithm> {
	private final Logger logger = LoggerFactory.getLogger(URNToRuleCombiningAlgorithmConverter.class);
	private static Map<String, RuleCombiningAlgorithm> combiningAlgorithms;

	/**
	 * TODO JAVADOC
	 * 
	 * Is used by the <a
	 * href="http://www.springframework.org/">Springframework</a> to fill the
	 * static {@link Map} containing the mapping between URNs and
	 * {@link RuleCombiningAlgorithm}s.
	 * 
	 * @param algorithms
	 *            The map containing the mapping between URNs and
	 *            policy combining algorithms.
	 */
	public static void setCombiningAlgorithms(Map<String, RuleCombiningAlgorithm> algorithms) {
		// TODO is a concurrent hashmap really needed?
		combiningAlgorithms = new ConcurrentHashMap<String, RuleCombiningAlgorithm>(algorithms); // ConcurrentHashMap
																									// because
																									// of
																									// concurrent
																									// access
		// possible
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(RuleCombiningAlgorithm combAlg) {
		String combAlgString;
		try {
			combAlgString = combAlg.toString();
		} catch (NullPointerException e) {
			logger.error("Argument combAlg must not be null: ", e);
			throw new IllegalArgumentException(e);
		}
		return combAlgString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public RuleCombiningAlgorithm unmarshal(String combAlgId) {
		RuleCombiningAlgorithm combAlg;
		try {
			combAlg = combiningAlgorithms.get(combAlgId);
		} catch (NullPointerException e) {
			logger.error("URNToRuleCombiningAlgorithmConverter not properly initialized.");
			throw new NotInitializedException(e);
		}
		if (combAlg != null) {
			return combAlg;
		}
		throw new IllegalArgumentException("Combining Algorithm " + combAlgId + " unknown.");
	}
}