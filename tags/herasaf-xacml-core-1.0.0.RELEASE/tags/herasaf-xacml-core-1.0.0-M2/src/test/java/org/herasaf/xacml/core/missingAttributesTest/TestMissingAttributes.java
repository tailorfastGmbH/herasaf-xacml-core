package org.herasaf.xacml.core.missingAttributesTest;

import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.UnorderedPolicyRepository;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.RequestCtxFactory;
import org.herasaf.xacml.core.context.ResponseCtx;
import org.herasaf.xacml.core.context.ResponseCtxFactory;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.PolicyMarshaller;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestMissingAttributes {
	private PDP pdp;
	private Evaluatable eval;
	private UnorderedPolicyRepository repo;

	@BeforeClass
	public void init() {
		pdp = SimplePDPFactory.getSimplePDP();
	}

	@DataProvider(name = "TestData")
	public Object[][] createTestData() throws Exception {
		return new Object[][] {
				{
						"Policy01",
						PolicyMarshaller
								.unmarshal(new File(
										"src/test/resources/missingAttributes/Policy01.xml")),
						RequestCtxFactory
								.unmarshal(new File(
										"src/test/resources/missingAttributes/Request01.xml")),
						ResponseCtxFactory
								.unmarshal(new File(
										"src/test/resources/missingAttributes/Response01.xml")) },
				{
						"Policy02",
						PolicyMarshaller
								.unmarshal(new File(
										"src/test/resources/missingAttributes/Policy02.xml")),
						RequestCtxFactory
								.unmarshal(new File(
										"src/test/resources/missingAttributes/Request02.xml")),
						ResponseCtxFactory
								.unmarshal(new File(
										"src/test/resources/missingAttributes/Response02.xml")) }, };
	}

	@Test(dataProvider = "TestData")
	public void testit(String id, Evaluatable eval, RequestCtx request,
			ResponseCtx expectedResponse) throws Exception {
		this.eval = eval;
		repo = (UnorderedPolicyRepository) pdp.getPolicyRepository();
		repo.deploy(eval);

		ResponseCtx response = pdp.evaluate(request);

		OutputStream actualResponseOS = new ByteArrayOutputStream();
		response.marshal(actualResponseOS);

		OutputStream expectedResponseOS = new ByteArrayOutputStream();
		expectedResponse.marshal(expectedResponseOS);

		XMLUnit.setIgnoreWhitespace(true);

		Diff diff = new Diff(expectedResponseOS.toString(),
				actualResponseOS.toString());

		assertTrue(diff.identical()); // identical test also values. needed
		// because it may differ here within the
		// attribute values
	}

	@AfterMethod
	public void cleanup() throws Exception {
		repo.undeploy(eval.getId());
	}
}
