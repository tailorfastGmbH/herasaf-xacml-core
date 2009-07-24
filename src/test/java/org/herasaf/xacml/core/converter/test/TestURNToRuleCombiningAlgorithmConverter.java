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

package org.herasaf.xacml.core.converter.test;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.herasaf.xacml.core.combiningAlgorithm.AbstractCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.AbstractRuleCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RuleDenyOverridesAlgorithm;
import org.herasaf.xacml.core.converter.URNToRuleCombiningAlgorithmConverter;
import org.herasaf.xacml.core.policy.combiningAlgorithm.mock.TargetMatcherMock;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author Sacha Dolski
 * @version 1.0
 *
 */

public class TestURNToRuleCombiningAlgorithmConverter {
	static final String DENY_OVERRIDES_ID = "urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:deny-overrides";
	private URNToRuleCombiningAlgorithmConverter converter;
	private AbstractRuleCombiningAlgorithm comAlg;
	private Map<String, RuleCombiningAlgorithm> map;

	@BeforeTest
	public void beforeTest() {
		converter = new URNToRuleCombiningAlgorithmConverter();
		comAlg = new RuleDenyOverridesAlgorithm();
		((AbstractCombiningAlgorithm)comAlg).setTargetMatcher(new TargetMatcherMock());
		map = new HashMap<String, RuleCombiningAlgorithm>();
		map.put(DENY_OVERRIDES_ID, comAlg);
		URNToRuleCombiningAlgorithmConverter.setCombiningAlgorithms(map);
	}

	@Test
	public void testConvertToDenyOverridesAlgo()
			throws IllegalArgumentException {
		assertEquals(converter.unmarshal(DENY_OVERRIDES_ID), comAlg);
	}

	@Test
	public void testConvertToCombingAlgoId() throws IllegalArgumentException {
		assertEquals(converter.marshal(comAlg), DENY_OVERRIDES_ID);
	}

	@Test(enabled = true, expectedExceptions = { IllegalArgumentException.class })
	public void testConvertException() throws IllegalArgumentException {
		comAlg = (AbstractRuleCombiningAlgorithm) converter.unmarshal("test");
	}

}
