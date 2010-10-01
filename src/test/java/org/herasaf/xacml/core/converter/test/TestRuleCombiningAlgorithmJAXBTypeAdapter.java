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

package org.herasaf.xacml.core.converter.test;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.AbstractRuleCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RuleDenyOverridesAlgorithm;
import org.herasaf.xacml.core.converter.PolicyCombiningAlgorithmJAXBTypeAdapter;
import org.herasaf.xacml.core.converter.RuleCombiningAlgorithmJAXBTypeAdapter;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests the {@link RuleCombiningAlgorithmJAXBTypeAdapter} JAXB converter.
 * 
 * @author Sacha Dolski
 */
public class TestRuleCombiningAlgorithmJAXBTypeAdapter {
	static final String DENY_OVERRIDES_ID = "urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:deny-overrides";
	private RuleCombiningAlgorithmJAXBTypeAdapter converter;
	private AbstractRuleCombiningAlgorithm comAlg;
	private Map<String, RuleCombiningAlgorithm> map;

	/**
	 * Initializes {@link PolicyCombiningAlgorithmJAXBTypeAdapter} with a
	 * {@link PolicyCombiningAlgorithm}.
	 */
	@BeforeTest
	public void beforeTest() {
		converter = new RuleCombiningAlgorithmJAXBTypeAdapter();
		comAlg = new RuleDenyOverridesAlgorithm();
		map = new HashMap<String, RuleCombiningAlgorithm>();
		map.put(DENY_OVERRIDES_ID, comAlg);
		RuleCombiningAlgorithmJAXBTypeAdapter.setCombiningAlgorithms(map);
	}

	/**
	 * Tests if the unmarshalling works correctly. That means that the
	 * {@link RuleCombiningAlgorithmJAXBTypeAdapter#unmarshal(String)} returns
	 * the proper object.
	 * 
	 * @throws IllegalArgumentException
	 *             In case of an improper argument.
	 */
	@Test
	public void testConvertToDenyOverridesAlgo()
			throws IllegalArgumentException {
		assertEquals(converter.unmarshal(DENY_OVERRIDES_ID), comAlg);
	}

	/**
	 * Tests if the marshalling works correctly. That means that the
	 * {@link RuleCombiningAlgorithmJAXBTypeAdapter#marshal(RuleCombiningAlgorithm)}
	 * returns the proper {@link String}.
	 * 
	 * @throws IllegalArgumentException
	 *             In case of an improper {@link DataTypeAttribute}.
	 */
	@Test
	public void testConvertToCombingAlgoId() throws IllegalArgumentException {
		assertEquals(converter.marshal(comAlg), DENY_OVERRIDES_ID);
	}

	/**
	 * Expects an {@link IllegalArgumentException} because an improper argument
	 * is given to the
	 * {@link PolicyCombiningAlgorithmJAXBTypeAdapter#unmarshal(String)} method.
	 * 
	 * @throws IllegalArgumentException
	 */
	@Test(enabled = true, expectedExceptions = { IllegalArgumentException.class })
	public void testConvertException() throws IllegalArgumentException {
		comAlg = (AbstractRuleCombiningAlgorithm) converter.unmarshal("test");
	}
}