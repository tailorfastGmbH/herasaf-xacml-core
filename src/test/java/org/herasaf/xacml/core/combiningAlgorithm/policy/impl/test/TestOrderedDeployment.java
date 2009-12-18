/*
 * Copyright 2009 HERAS-AF (www.herasaf.org)
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
package org.herasaf.xacml.core.combiningAlgorithm.policy.impl.test;

import static org.testng.Assert.assertSame;

import java.util.List;

import org.herasaf.xacml.core.api.OrderedPolicyRepository;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.herasaf.xacml.core.simplePDP.OrderedMapBasedSimplePolicyRepository;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * This tests test the ordered deployment of policies.
 * 
 * @author Florian Huonder
 */
public class TestOrderedDeployment {

	/**
	 * Initializes JAXB stuff.
	 */
	@BeforeTest
	public void init() {
		SimplePDPFactory.useDefaultInitializers();
	}

	/**
	 * 
	 * This method tests if the ordered deployment works as expected.
	 */
	@Test
	public void testOrderedDeployment() {
		OrderedPolicyRepository repo = new OrderedMapBasedSimplePolicyRepository();

		PolicyType policy1 = new PolicyType();
		policy1.setPolicyId("policy1");
		PolicyType policy2 = new PolicyType();
		policy2.setPolicyId("policy2");
		PolicyType policy3 = new PolicyType();
		policy3.setPolicyId("policy3");

		repo.deploy(policy1);
		repo.deploy(policy3);
		repo.deploy(policy2, 1); // deploy policy2 between the two others.

		List<Evaluatable> evals = repo.getDeployment();

		assertSame(evals.get(0), policy1);
		assertSame(evals.get(1), policy2);
		assertSame(evals.get(2), policy3);

		repo.undeploy(policy1.getId());
		repo.undeploy(policy2.getId());
		repo.undeploy(policy3.getId());
	}
}