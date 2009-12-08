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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.herasaf.xacml.core.PolicyRepositoryException;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.PolicyRepository;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.RequestCtxFactory;
import org.herasaf.xacml.core.context.ResponseCtx;
import org.herasaf.xacml.core.context.ResponseCtxFactory;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;
import org.herasaf.xacml.core.policy.PolicyConverter;
import org.herasaf.xacml.core.policy.impl.EvaluatableIDImpl;
import org.herasaf.xacml.core.simplePDP.initializers.Initializer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the {@link SimplePDP} and {@link SimplePDPFactory} behave as expected.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class SimplePDPTest {
	private PDP simplePDP;

	/**
	 * Creates various test cases.
	 * 
	 * @return The created test cases.
	 * @throws Exception If an error occurs.
	 */
	@DataProvider(name = "policy-request-response-combinations")
	public Object[][] initializeTestCases() throws Exception {
		SimplePDPFactory.getSimplePDP(); //This line is needed that the JAXB stuff is initialized.
		
		
		return new Object[][] {
				new Object[] {
						loadPolicy("/org/herasaf/xacml/core/simplePDP/policies/Policy01.xml"),
						loadRequest("/org/herasaf/xacml/core/simplePDP/requests/Request01.xml"),
						loadResponse("/org/herasaf/xacml/core/simplePDP/responses/Response01.xml") },

				new Object[] {
						loadPolicy("/org/herasaf/xacml/core/simplePDP/policies/Policy02.xml"),
						loadRequest("/org/herasaf/xacml/core/simplePDP/requests/Request01.xml"),
						loadResponse("/org/herasaf/xacml/core/simplePDP/responses/Response01.xml") },
		};
	}

	/**
	 * Initializes the {@link SimplePDPFactory} to use the default {@link Initializer}s.
	 * Creates a new {@link SimplePDP}.
	 */
	@BeforeClass
	public void init() {
		SimplePDPFactory.useDefaultInitializers();
		simplePDP = SimplePDPFactory.getSimplePDP();
	}

	/**
	 * Tests if the {@link SimplePDP} behaves properly.
	 * 
	 * @param policy The policy tree to deploy and test against.
	 * @param request The {@link RequestCtx} that shall be evaluated.
	 * @param expectedResponse The expected {@link ResponseCtx}.
	 * @throws Exception If an error occurs.
	 */
	@Test(dataProvider = "policy-request-response-combinations")
	public void testSimplePDP(Evaluatable policy, RequestCtx request,
			ResponseCtx expectedResponse) throws Exception {
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
		System.out.println();
	}
	
	/**
	 * Tests if the deployment of multiple {@link Evaluatable}s in a {@link List} works properly.
	 * 
	 * @throws Exception If an error occurs.
	 */
	@Test
	public void testMultiDeploy() throws Exception {
		PolicyRepository repo = simplePDP.getPolicyRepository();
		Evaluatable eval1 = loadPolicy("/org/herasaf/xacml/core/simplePDP/policies/Policy01.xml");
		EvaluatableID evalId1 = eval1.getId();
		Evaluatable eval2 = loadPolicy("/org/herasaf/xacml/core/simplePDP/policies/Policy02.xml");
		EvaluatableID evalId2 = eval2.getId();
		List<Evaluatable> evals = new ArrayList<Evaluatable>();
		evals.add(eval1);
		evals.add(eval2);
		List<EvaluatableID> evalIds = new ArrayList<EvaluatableID>();
		evalIds.add(evalId1);
		evalIds.add(evalId2);
		
		repo.deploy(evals);
		
		assertEquals(repo.getDeployment(), evals);
		
		repo.undeploy(evalIds);
		
		assertEquals(repo.getDeployment().size(), 0);
	}
	
	/**
	 * Tests if the PolicyRepository does return an {@link Evaluatable} by id properly.
	 * 
	 * @throws Exception If an error occurs.
	 */
	@Test
	public void testGetByIdPositive() throws Exception {
		PolicyRepository repo = simplePDP.getPolicyRepository();
		Evaluatable eval = loadPolicy("/org/herasaf/xacml/core/simplePDP/policies/Policy02.xml");
		
		repo.deploy(eval);
		
		Evaluatable eval1 = repo.getEvaluatable(new EvaluatableIDImpl("urn:oasis:names:tc:example:SimplePolicy2"));
		Evaluatable eval2 = repo.getEvaluatable(new EvaluatableIDImpl("urn:oasis:names:tc:xacml:2.0:conformance-test:IID006:policyset"));
		
		assertNotNull(eval1);
		assertNotNull(eval2);
		
		repo.undeploy(eval2.getId()); //only the root policy must be undeployed.
	}
	
	/**
	 * Tests that the PDP does not return an {@link Evaluatable} if a wrong id is given.
	 * 
	 * @throws Exception If an error occurs.
	 */
	@Test (expectedExceptions={PolicyRepositoryException.class})
	public void testGetByIdNegative() throws Exception {
		PolicyRepository repo = simplePDP.getPolicyRepository();

		repo.getEvaluatable(new EvaluatableIDImpl("does not exist"));
	}
	
	/**
	 * Tests if the PDP returns the right {@link Evaluatable} in case the {@link Evaluatable} are retrieved by request.
	 * 
	 * @throws Exception If an error occurs.
	 */
	@Test
	public void testGetByRequest() throws Exception {
		PolicyRepository repo = simplePDP.getPolicyRepository();
		Evaluatable eval1 = loadPolicy("/org/herasaf/xacml/core/simplePDP/policies/Policy01.xml");
		EvaluatableID evalId1 = eval1.getId();
		Evaluatable eval2 = loadPolicy("/org/herasaf/xacml/core/simplePDP/policies/Policy02.xml");
		EvaluatableID evalId2 = eval2.getId();
		List<Evaluatable> evals = new ArrayList<Evaluatable>();
		evals.add(eval1);
		evals.add(eval2);
		List<EvaluatableID> evalIds = new ArrayList<EvaluatableID>();
		evalIds.add(evalId1);
		evalIds.add(evalId2);
		
		repo.deploy(evals);
		
		RequestCtx request = loadRequest("/org/herasaf/xacml/core/simplePDP/requests/Request01.xml");
		
		assertEquals(repo.getEvaluatables(request), evals);
		
		repo.undeploy(evalIds);
		
		assertEquals(repo.getDeployment().size(), 0);
	}
	
	/**
	 * Tests if internal references are correctly resloved.
	 * 
	 * @throws Exception If an error occurs.
	 */
	@Test
	public void testPSInternalReference() throws Exception {
		PolicyRepository repo = simplePDP.getPolicyRepository();
		Evaluatable eval1 = loadPolicy("/org/herasaf/xacml/core/simplePDP/policies/Policy03.xml");
		repo.deploy(eval1);
		
		Evaluatable eval = repo.getEvaluatable(new EvaluatableIDImpl("urn:oasis:names:tc:xacml:2.0:conformance-test:IID006:policyset3-root"));
		assertNotNull(eval);
		
		eval = repo.getEvaluatable(new EvaluatableIDImpl("urn:oasis:names:tc:xacml:2.0:conformance-test:IID006:policyset3-left"));
		assertNotNull(eval);
		
		eval = repo.getEvaluatable(new EvaluatableIDImpl("urn:oasis:names:tc:example:SimplePolicy"));
		assertNotNull(eval);
		
		repo.undeploy(eval1.getId());
		assertEquals(repo.getDeployment().size(), 0);
	}
	
	/**
	 * Tests if the simple PDP cannot resolv remote references.
	 * 
	 * @throws ExceptionIf an error occurs.
	 */
	@Test (expectedExceptions={PolicyRepositoryException.class})
	public void testPSRemoteReference() throws Exception {
		PolicyRepository repo = simplePDP.getPolicyRepository();
		Evaluatable eval1 = loadPolicy("/org/herasaf/xacml/core/simplePDP/policies/Policy04.xml");
		repo.deploy(eval1);
	}
	
	/**
	 * Tests if a local reference to an other PolicySet as this PolicySet cannot be resolved.
	 * 
	 * @throws Exception If an error occurs.
	 */
	@Test  (expectedExceptions={PolicyRepositoryException.class})
	public void testPSLocalReferenceToOtherPS() throws Exception {
		PolicyRepository repo = simplePDP.getPolicyRepository();
		Evaluatable eval1 = loadPolicy("/org/herasaf/xacml/core/simplePDP/policies/Policy04.xml");
		Evaluatable eval2 = loadPolicy("/org/herasaf/xacml/core/simplePDP/policies/Policy05.xml");
		List<Evaluatable> evals = new ArrayList<Evaluatable>();
		evals.add(eval1);
		evals.add(eval2);
		repo.deploy(evals);
	}
	
	/**
	 * Tests if the PDP Factory returns multiple PDP objects.
	 * 
	 * @throws Exception If an error occurs.
	 */
	@Test
	public void testMultiplePDPInstances() throws Exception {
		PDP secondSimplePDP = SimplePDPFactory.getSimplePDP();
		assertNotSame(secondSimplePDP, simplePDP);
	}

	/**
	 * Loads an {@link Evaluatable} from the class path.
	 * @param file The path to the resource.
	 * @return The created {@link Evaluatable}.
	 * @throws SyntaxException
	 */
	private Evaluatable loadPolicy(String file) throws SyntaxException {
		InputStream is = SimplePDPTest.class.getResourceAsStream(file);
		return PolicyConverter.unmarshal(is);
	}

	/**
	 * Loads an {@link RequestCtx} from the class path.
	 * @param file The path to the resource.
	 * @return The created {@link RequestCtx}.
	 * @throws SyntaxException
	 */
	private RequestCtx loadRequest(String file) throws SyntaxException {
		InputStream is = SimplePDPTest.class.getResourceAsStream(file);
		return RequestCtxFactory.unmarshal(is);
	}

	/**
	 * Loads an {@link ResponseCtx} from the class path.
	 * @param file The path to the resource.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 */
	private ResponseCtx loadResponse(String file) throws SyntaxException {
		InputStream is = SimplePDPTest.class.getResourceAsStream(file);
		return ResponseCtxFactory.unmarshal(is);
	}
}