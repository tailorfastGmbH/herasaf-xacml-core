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
package org.herasaf.xacml.core.jaxb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.custommonkey.xmlunit.XMLAssert;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.WritingException;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RulePermitOverridesAlgorithm;
import org.herasaf.xacml.core.context.RequestMarshaller;
import org.herasaf.xacml.core.context.impl.ActionType;
import org.herasaf.xacml.core.context.impl.EnvironmentType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResourceType;
import org.herasaf.xacml.core.context.impl.SubjectType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.PolicyMarshaller;
import org.herasaf.xacml.core.policy.impl.EvaluatableIDImpl;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.herasaf.xacml.core.policy.impl.TargetType;
import org.herasaf.xacml.core.simplePDP.initializers.InitializerExecutor;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * Minimal test checking if marshalling and unmarshalling of Requests and
 * Policies works.
 * 
 * @author René Eggenschwiler
 * @author Florian Huonder
 * 
 */
public class MinimalJAXBMarshallUnmarshallTest {

	private File file;

	@BeforeClass
	public static void setup() {
		InitializerExecutor.runInitializers();
	}

	@BeforeTest
	public void setupTest() throws IOException {
		file = File.createTempFile("test-temp", ".xml");
	}

	@AfterTest
	public void tearDownTest() {
		file.delete();
	}

	/**
	 * Tests if the marshalling result is the same if a {@link RequestType} is
	 * first marshalled to a file, unmarshalled and then marshalled to a
	 * outputStream again.
	 * 
	 * @throws WritingException
	 * @throws SyntaxException
	 * @throws IOException
	 * @throws SAXException
	 */
	@Test
	public void testMarshalUnmarshalRequestTypes() throws WritingException,
			SyntaxException, IOException, SAXException {

		RequestType req = new RequestType();
		req.getSubjects().add(new SubjectType());
		req.getResources().add(new ResourceType());
		req.setAction(new ActionType());
		req.setEnvironment(new EnvironmentType());
		RequestMarshaller.marshal(req, file);

		RequestType req2 = RequestMarshaller.unmarshal(file);

		ByteArrayOutputStream marshalToStreamResult = new ByteArrayOutputStream();

		RequestMarshaller.marshal(req2, marshalToStreamResult);

		assertByteOutputStreamEqualsFile(marshalToStreamResult);

	}

	/**
	 * 
	 * Tests if the marshalling result is the same if a {@link PolicyType} is
	 * first marshalled to a file, unmarshalled and then marshalled to a
	 * outputStream again.
	 * 
	 * @throws WritingException
	 * @throws SyntaxException
	 * @throws IOException
	 * @throws SAXException
	 */
	@Test
	public void testMarshalUnmarshalPolicyType() throws WritingException,
			SyntaxException, IOException, SAXException {
		PolicyType policy = new PolicyType();
		policy.setCombiningAlg(new RulePermitOverridesAlgorithm());
		policy.setPolicyId(new EvaluatableIDImpl(
				"urn:org:herasaf:xacml:test:dummyPolicy"));
		policy.setTarget(new TargetType());
		PolicyMarshaller.marshal(policy, file);

		Evaluatable policy2 = PolicyMarshaller.unmarshal(file);

		ByteArrayOutputStream marshalToStreamResult = new ByteArrayOutputStream();
		PolicyMarshaller.marshal(policy2, marshalToStreamResult);

		assertByteOutputStreamEqualsFile(marshalToStreamResult);
	}

	private void assertByteOutputStreamEqualsFile(ByteArrayOutputStream marshalToStreamResult) throws SAXException,
			IOException, FileNotFoundException {
		XMLAssert.assertXMLEqual(
				new InputSource(new FileInputStream(file)),
				new InputSource(new ByteArrayInputStream(marshalToStreamResult
						.toByteArray())));
	}

}
