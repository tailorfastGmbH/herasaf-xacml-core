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
package org.herasaf.xacml.core.policy.combiningAlgorithm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.PolicyRepository;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.RequestCtxFactory;
import org.herasaf.xacml.core.context.ResponseCtx;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.PolicyConverter;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This test tests if the respect abandoned evaluatables flag of the
 * {@link SimplePDPFactory} is set and interpreted correctly.
 * 
 * @author Florian Huonder
 */
public class TestRespectAbandonedEvaluatables {
	private PolicyRepository repo;

	/**
	 * Initializes the test data.
	 * 
	 * @return The test data
	 * @throws Exception
	 *             In case of an error.
	 */
	@DataProvider(name = "test-cases")
	public Object[][] createTestCases() throws Exception {

		List<File> evalFiles = new ArrayList<File>();
		evalFiles
				.add(new File(
						"src/test/resources/org/herasaf/xacml/core/simplePDP/policies/PS-deny-overrides.xml"));
		evalFiles
				.add(new File(
						"src/test/resources/org/herasaf/xacml/core/simplePDP/policies/PS-ordered-deny-overrides.xml"));
		evalFiles
				.add(new File(
						"src/test/resources/org/herasaf/xacml/core/simplePDP/policies/PS-permit-overrides.xml"));
		evalFiles
				.add(new File(
						"src/test/resources/org/herasaf/xacml/core/simplePDP/policies/PS-ordered-permit-overrides.xml"));
		// extend here with further test cases

		Object[][] testcases = new Object[evalFiles.size() * 2][];

		// The index starts with 1 that the calculation at the end of the method
		// works properly.
		for (int i = 1; i < evalFiles.size() + 1; i++) {
			SimplePDPFactory.respectAbandonedEvaluatables(false);
			SimplePDPFactory.useDefaultInitializers();
			SimplePDPFactory.getSimplePDP();
			Evaluatable eval1 = PolicyConverter.unmarshal(evalFiles.get(i - 1));

			SimplePDPFactory.respectAbandonedEvaluatables(true);
			SimplePDPFactory.useDefaultInitializers();
			SimplePDPFactory.getSimplePDP();
			Evaluatable eval2 = PolicyConverter.unmarshal(evalFiles.get(i - 1));

			testcases[2 * i - 1 - 1] = new Object[] { false, eval1 };
			testcases[2 * i - 1] = new Object[] { true, eval2 };
		}

		return testcases;
	}

	/**
	 * Tests if Obligations of abandoned {@link Evaluatable}s are only collected
	 * if the corresponding flag in the {@link SimplePDPFactory} is set to true.
	 * 
	 * @param respectAbandonedEvaluatables
	 *            True if the abandoned {@link Evaluatable}s shall be respected,
	 *            false otherwise.
	 * @param eval
	 *            The {@link Evaluatable} under test.
	 * @throws Exception
	 *             In case an error occurs.
	 */
	@Test(dataProvider = "test-cases")
	public void testRespectAbandonedEvalutables(
			boolean respectAbandonedEvaluatables, Evaluatable eval)
			throws Exception {
		// These factory settings are needed to properly set the root combining
		// algorithm.
		SimplePDPFactory
				.respectAbandonedEvaluatables(respectAbandonedEvaluatables);
		SimplePDPFactory.useDefaultInitializers();
		PDP pdp = SimplePDPFactory.getSimplePDP();
		RequestCtx request = RequestCtxFactory
				.unmarshal(new File(
						"src/test/resources/org/herasaf/xacml/core/simplePDP/requests/Request01.xml"));
		repo = pdp.getPolicyRepository();

		repo.deploy(eval);

		ResponseCtx response = pdp.evaluate(request);

		if (respectAbandonedEvaluatables) {
			if (response.getResponse().getResults().get(0).getObligations() != null) {
				assertEquals(response.getResponse().getResults().get(0)
						.getObligations().getObligations().get(0)
						.getObligationId(), "expectedObligation");
			} else {
				fail("No obligations.");
			}
		} else {
			assertTrue(response.getResponse().getResults().get(0)
					.getObligations() == null);
		}
	}

	/**
	 * Removes the deployment from the {@link PDP}.
	 */
	@AfterMethod
	public void cleanUp() throws Exception {
		repo.undeploy(repo.getDeployment().get(0).getId());
	}
}
