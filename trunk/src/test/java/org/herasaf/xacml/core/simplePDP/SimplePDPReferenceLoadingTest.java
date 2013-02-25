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
package org.herasaf.xacml.core.simplePDP;

import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.context.RequestMarshaller;
import org.herasaf.xacml.core.context.ResponseMarshaller;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResponseType;
import org.herasaf.xacml.core.simplePDP.initializers.InitializerExecutor;
import org.herasaf.xacml.core.simplePDP.initializers.api.Initializer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests if the {@link SimplePDP} and {@link SimplePDPFactory} behave as
 * expected, when asked to resolve policy references.
 */
public class SimplePDPReferenceLoadingTest {
	private PDP simplePDP;

	/**
	 * Initializes the {@link SimplePDPFactory} to use the default
	 * {@link Initializer}s. Creates a new {@link SimplePDP}.
	 * @throws SyntaxException 
	 */
	@BeforeClass
	public void init() throws SyntaxException {
	        String policiesURL = "/org/herasaf/xacml/core/simplePDP/policies/rbac";
	        SimpleRBACPolicyRetrievalPoint prp = new SimpleRBACPolicyRetrievalPoint();
	        prp.setPoliciesURL(SimplePDPReferenceLoadingTest.class.getResource(policiesURL));

		simplePDP = SimplePDPFactory.getSimplePDP(null, prp);
	}

	/**
         * Creates various test cases.
         * 
         * @return The created test cases.
         * @throws Exception
         *             If an error occurs.
         */
        @DataProvider(name = "request-response-combinations")
        public Object[][] initializeTestCases() throws Exception {
                InitializerExecutor.runInitializers();
                // stuff is initialized.

                return new Object[][] {
                                new Object[] {
                                                loadRequest("/org/herasaf/xacml/core/simplePDP/requests/DoctorExamineRequest.xml"),
                                                loadResponse("/org/herasaf/xacml/core/simplePDP/responses/Response01.xml") 
                                },
                                new Object[] {
                                                loadRequest("/org/herasaf/xacml/core/simplePDP/requests/DoctorPrescribeRequest.xml"),
                                                loadResponse("/org/herasaf/xacml/core/simplePDP/responses/Response01.xml") 
                                },
                                new Object[] {
                                                loadRequest("/org/herasaf/xacml/core/simplePDP/requests/NurseExamineRequest.xml"),
                                                loadResponse("/org/herasaf/xacml/core/simplePDP/responses/Response01.xml") 
                                },
                                new Object[] {
                                                loadRequest("/org/herasaf/xacml/core/simplePDP/requests/NursePrescribeRequest.xml"),
                                                loadResponse("/org/herasaf/xacml/core/simplePDP/responses/Response02.xml") 
                                },
                                new Object[] {
                                                loadRequest("/org/herasaf/xacml/core/simplePDP/requests/SurgeonExamineRequest.xml"),
                                                loadResponse("/org/herasaf/xacml/core/simplePDP/responses/Response03.xml") 
                                },
                                new Object[] {
                                                loadRequest("/org/herasaf/xacml/core/simplePDP/requests/ConsultantExamineRequest.xml"),
                                                loadResponse("/org/herasaf/xacml/core/simplePDP/responses/Response03.xml") 
                                }
                       };
        }
        
	/**
	 * Tests if the {@link SimplePDP} behaves properly.
	 */
	@Test(dataProvider = "request-response-combinations")
	public void testSimplePDP(RequestType request,
                        ResponseType expectedResponse) throws Exception {

		ResponseType response = simplePDP.evaluate(request);

		OutputStream responseOS = new ByteArrayOutputStream();
		ResponseMarshaller.marshal(response, responseOS);

		OutputStream expectedOS = new ByteArrayOutputStream();
		ResponseMarshaller.marshal(expectedResponse, expectedOS);

		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreAttributeOrder(true);
		Diff diff = new Diff(expectedOS.toString(), responseOS.toString());
		assertTrue(diff.similar(), "The expected response was: "
				+ expectedResponse.getResults().get(0)
						.getDecision() + ", but the response was: "
				+ response.getResults().get(0).getDecision());
	}

	/**
	 * Loads an {@link RequestType} from the class path.
	 * 
	 * @param file
	 *            The path to the resource.
	 * @return The created {@link RequestType}.
	 * @throws SyntaxException
	 */
	private RequestType loadRequest(String file) throws SyntaxException {
		InputStream is = SimplePDPReferenceLoadingTest.class.getResourceAsStream(file);
		return RequestMarshaller.unmarshal(is);
	}

	/**
	 * Loads an {@link ResponseType} from the class path.
	 * 
	 * @param file
	 *            The path to the resource.
	 * @return The created {@link ResponseType}.
	 * @throws SyntaxException
	 */
	private ResponseType loadResponse(String file) throws SyntaxException {
		InputStream is = SimplePDPReferenceLoadingTest.class.getResourceAsStream(file);
		return ResponseMarshaller.unmarshal(is);
	}
}