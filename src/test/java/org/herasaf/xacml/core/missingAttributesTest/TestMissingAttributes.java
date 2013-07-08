package org.herasaf.xacml.core.missingAttributesTest;

import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.UnorderedPolicyRepository;
import org.herasaf.xacml.core.context.RequestMarshaller;
import org.herasaf.xacml.core.context.ResponseMarshaller;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResponseType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.PolicyMarshaller;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestMissingAttributes {
	private static final String EXPECTED_ERROR = "Function urn:oasis:names:tc:xacml:1.0:function:string-unknown-equal unknown";
	private PDP pdp;
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
						RequestMarshaller
								.unmarshal(new File(
										"src/test/resources/missingAttributes/Request01.xml")),
						ResponseMarshaller
								.unmarshal(new File(
										"src/test/resources/missingAttributes/Response01.xml")) },
				{
						"Policy02",
						PolicyMarshaller
								.unmarshal(new File(
										"src/test/resources/missingAttributes/Policy02.xml")),
						RequestMarshaller
								.unmarshal(new File(
										"src/test/resources/missingAttributes/Request02.xml")),
						ResponseMarshaller
								.unmarshal(new File(
										"src/test/resources/missingAttributes/Response02.xml")) }, };
	}

	@Test(dataProvider = "TestData")
	public void testit(String id, Evaluatable eval, RequestType request,
			ResponseType expectedResponse) throws Exception {
		repo = (UnorderedPolicyRepository) pdp.getPolicyRepository();
		repo.deploy(eval);

		ResponseType response = pdp.evaluate(request);

		OutputStream actualResponseOS = new ByteArrayOutputStream();
		ResponseMarshaller.marshal(response, actualResponseOS);

		OutputStream expectedResponseOS = new ByteArrayOutputStream();
		ResponseMarshaller.marshal(expectedResponse, expectedResponseOS);

		XMLUnit.setIgnoreWhitespace(true);

		Diff diff = new Diff(expectedResponseOS.toString(),
				actualResponseOS.toString());

		assertTrue(diff.identical()); // identical test also values. needed
		// because it may differ here within the
		// attribute values
		
		repo.undeploy(eval.getId());
	}

	@Test
	public void testMissingFunction() throws Exception {
		try {
			PolicyMarshaller.unmarshal(new File(
					"src/test/resources/missingAttributes/Policy03.xml"));
			Assert.fail("Failure expected on an unknown Function ID");
		} catch (SyntaxException ex) {
			Assert.assertNotNull(ex.getCause());
			Assert.assertTrue(ex.getCause().getMessage()
					.contains(EXPECTED_ERROR));
		}
	}
}
