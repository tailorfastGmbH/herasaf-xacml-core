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

import java.io.File;
import java.io.IOException;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.WritingException;
import org.herasaf.xacml.core.combiningAlgorithm.rule.impl.RulePermitOverridesAlgorithm;
import org.herasaf.xacml.core.context.RequestMarshaller;
import org.herasaf.xacml.core.context.impl.EnvironmentType;
import org.herasaf.xacml.core.context.impl.RequestType;
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

/**
 * 
 * @author René Eggenschwiler
 * @author Florian Huonder
 * 
 */
public class MinimalJAXBTypesTest {

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

	@Test
	public void testTest() throws WritingException, SyntaxException,
			IOException {

		RequestType req = new RequestType();
		req.setEnvironment(new EnvironmentType());
		RequestMarshaller.marshal(req, file);

		RequestType req2 = RequestMarshaller.unmarshal(file);
		RequestMarshaller.marshal(req2, System.out);

	}

	@Test
	public void testPolicyTest() throws WritingException, SyntaxException,
			IOException {
		PolicyType policy = new PolicyType();
		policy.setCombiningAlg(new RulePermitOverridesAlgorithm());
		policy.setPolicyId(new EvaluatableIDImpl("urn:org:herasaf:xacml:test:dummyPolicy"));
		policy.setTarget(new TargetType());
		PolicyMarshaller.marshal(policy, file);

		Evaluatable policy2 = PolicyMarshaller.unmarshal(file);
		PolicyMarshaller.marshal(policy2, System.out);
	}

}
