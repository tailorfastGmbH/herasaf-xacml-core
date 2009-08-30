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
package org.herasaf.xacml.core.simplePDP;

import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.PolicyRepository;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.RequestCtxFactory;
import org.herasaf.xacml.core.context.ResponseCtx;
import org.herasaf.xacml.core.context.ResponseCtxFactory;
import org.herasaf.xacml.core.converter.URNToRuleCombiningAlgorithmConverter;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.PolicyConverter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * 
 * TODO JAVADOC!!
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 * 
 */
public class SimplePDPTest {
	private PDP simplePDP;

	@DataProvider(name = "policy-request-response-combinations")
	public Object[][] initializeTestCases() throws Exception {
		System.out.println("XXXXXXXXXXXXXXXX: DP - START");
		
		PDP p = SimplePDPFactory.getSimplePDP();
		
		
		return new Object[][] {
				new Object[] {
						loadPolicy("/org/herasaf/xacml/core/simplePDP/policies/Policy01.xml"),
						loadRequest("/org/herasaf/xacml/core/simplePDP/requests/Request01.xml"),
						loadResponse("/org/herasaf/xacml/core/simplePDP/responses/Response01.xml") },

				new Object[] {
						loadPolicy("/org/herasaf/xacml/core/simplePDP/policies/Policy02.xml"),
						loadRequest("/org/herasaf/xacml/core/simplePDP/requests/Request01.xml"),
						loadResponse("/org/herasaf/xacml/core/simplePDP/responses/Response01.xml") },

		// TODO
		// policy with internal ref
		// policy with external ref
		};
	}

	@BeforeClass
	public void init() {
		System.out.println("XXXXXXXXXXXXXXXX: BC - START");
		SimplePDPFactory.useDefaultInitializers(true);
		simplePDP = SimplePDPFactory.getSimplePDP();
		System.out.println("XXXXXXXXXXXXXXXX: BC - END");
	}

	@Test(dataProvider = "policy-request-response-combinations")
	public void testSimplePDP(Evaluatable policy, RequestCtx request,
			ResponseCtx expectedResponse) throws Exception {
		System.out.println("XXXXXXXXXXXXXXXX: Test START");
		PolicyRepository repo = simplePDP.getPolicyRepository();
		repo.deploy(policy);

		ResponseCtx response = simplePDP.evaluate(request);

		OutputStream responseOS = new ByteArrayOutputStream();
		response.marshal(responseOS);

		OutputStream expectedOS = new ByteArrayOutputStream();
		expectedResponse.marshal(expectedOS);

		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreAttributeOrder(true);
		Diff diff = new Diff(expectedOS.toString(), responseOS.toString());
		assertTrue(diff.similar(), "The expected response was: "
				+ expectedResponse.getResponse().getResults().get(0)
						.getDecision() + ", but the response was: "
				+ response.getResponse().getResults().get(0).getDecision());

		repo.undeploy(policy.getId());
		System.out.println("XXXXXXXXXXXXXXXX: Test - END");
	}

	private Evaluatable loadPolicy(String file) throws SyntaxException {
		InputStream is = SimplePDPTest.class.getResourceAsStream(file);
		return PolicyConverter.unmarshal(is);
	}

	private RequestCtx loadRequest(String file) throws SyntaxException {
		InputStream is = SimplePDPTest.class.getResourceAsStream(file);
		return RequestCtxFactory.unmarshal(is);
	}

	private ResponseCtx loadResponse(String file) throws SyntaxException {
		InputStream is = SimplePDPTest.class.getResourceAsStream(file);
		return ResponseCtxFactory.unmarshal(is);
	}
}