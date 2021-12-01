package org.herasaf.xacml.core.conformanceTests;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.herasaf.xacml.core.ResourceLoaderSupport;
import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.UnorderedPolicyRepository;
import org.herasaf.xacml.core.context.ResponseMarshaller;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResponseType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestConformanceTargetMatching {

	private static final String RESPONSE_FILE_PATTERN = "/conformanceTests/targetMatching/%sResponse.xml";
	private static final String REQUEST_FILE_PATTERN = "/conformanceTests/targetMatching/%sRequest.xml";
	private static final String POLICY_FILE_PATTERN = "/conformanceTests/targetMatching/%sPolicy.xml";

	private PDP pdp;

	@DataProvider(name = "requestResponse")
	public Object[][] requestResponse() throws Exception {
		return new Object[][] {
				new Object[] { "IIB001" }, new Object[] { "IIB002" }, new Object[] { "IIB003" },
				new Object[] { "IIB004" }, new Object[] { "IIB005" }, new Object[] { "IIB006" },
				new Object[] { "IIB007" }, new Object[] { "IIB008" }, new Object[] { "IIB009" },
				new Object[] { "IIB010" }, new Object[] { "IIB011" }, new Object[] { "IIB012" },
				new Object[] { "IIB013" }, new Object[] { "IIB014" }, new Object[] { "IIB015" },
				new Object[] { "IIB016" }, new Object[] { "IIB017" }, new Object[] { "IIB018" },
				new Object[] { "IIB019" }, new Object[] { "IIB020" }, new Object[] { "IIB021" },
				new Object[] { "IIB022" }, new Object[] { "IIB023" }, new Object[] { "IIB024" },
				new Object[] { "IIB025" }, new Object[] { "IIB026" }, new Object[] { "IIB027" },
				new Object[] { "IIB028" }, new Object[] { "IIB029" }, new Object[] { "IIB030" },
				new Object[] { "IIB031" }, new Object[] { "IIB032" }, new Object[] { "IIB033" },
				new Object[] { "IIB034" }, new Object[] { "IIB035" }, new Object[] { "IIB036" },
				new Object[] { "IIB037" }, new Object[] { "IIB038" }, new Object[] { "IIB039" },
				new Object[] { "IIB040" }, new Object[] { "IIB041" }, new Object[] { "IIB042" },
				new Object[] { "IIB043" }, new Object[] { "IIB044" }, new Object[] { "IIB045" },
				new Object[] { "IIB046" }, new Object[] { "IIB047" }, new Object[] { "IIB048" },
				new Object[] { "IIB049" }, new Object[] { "IIB050" }, new Object[] { "IIB051" },
				new Object[] { "IIB052" }, new Object[] { "IIB053" },
		};
	}

	@BeforeClass
	public void init() {
		pdp = SimplePDPFactory.getSimplePDP();
	}

	public void deployPolicies(String testCase) throws Exception {
		List<Evaluatable> evals = new ArrayList<Evaluatable>();
		evals.add(ResourceLoaderSupport.loadPolicy(POLICY_FILE_PATTERN, testCase));
		UnorderedPolicyRepository deploymentRepo = (UnorderedPolicyRepository) pdp.getPolicyRepository();
		deploymentRepo.deploy(evals);
	}

	@AfterMethod
	public void afterMethod() throws Exception {
		UnorderedPolicyRepository deploymentRepo = (UnorderedPolicyRepository) pdp.getPolicyRepository();
		List<EvaluatableID> evaluatableIds = deploymentRepo.getDeployment().stream()
				.map((evaluatable) -> evaluatable.getId()).collect(Collectors.toList());
		deploymentRepo.undeploy(evaluatableIds);
	}

	@Test
	public void testSingleUseCase() throws Exception {
		String testCase = "IIB001";
		testTargetMatchingConformance(testCase);
	}

	@Test(enabled = true, dataProvider = "requestResponse")
	public void testTargetMatchingConformance(String testCase) throws Exception {
		deployPolicies(testCase);

		RequestType request = ResourceLoaderSupport.loadRequest(REQUEST_FILE_PATTERN, testCase);
		ResponseType expectedResponse = ResourceLoaderSupport.loadResponse(RESPONSE_FILE_PATTERN, testCase);

		ResponseType response = pdp.evaluate(request);

		OutputStream responseOS = new ByteArrayOutputStream();
		ResponseMarshaller.marshal(response, responseOS);

		OutputStream expectedOS = new ByteArrayOutputStream();
		ResponseMarshaller.marshal(expectedResponse, expectedOS);

		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLAssert.assertXMLEqual("Testcase: " + testCase + " failed!", expectedOS.toString(), responseOS.toString());

	}
}