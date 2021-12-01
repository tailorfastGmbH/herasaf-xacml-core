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

public class TestConformanceCombiningAlgorithms {

	private static final String RESPONSE_FILE_PATTERN = "/conformanceTests/combiningAlgorithms/%sResponse.xml";
	private static final String REQUEST_FILE_PATTERN = "/conformanceTests/combiningAlgorithms/%sRequest.xml";
	private static final String POLICY_FILE_PATTERN = "/conformanceTests/combiningAlgorithms/%sPolicy.xml";
	private static final String POLICY_WITH_INDEX_FILE_PATTERN = "/conformanceTests/combiningAlgorithms/%sPolicy%s.xml";

	private PDP pdp;

	@DataProvider(name = "requestResponse")
	public Object[][] requestResponse() throws Exception {
		return new Object[][] { new Object[] { "IID001", false }, new Object[] { "IID002", false },
				new Object[] { "IID003", false }, new Object[] { "IID005", false }, new Object[] { "IID006", false },
				new Object[] { "IID007", false }, new Object[] { "IID008", false }, new Object[] { "IID009", false },
				new Object[] { "IID010", false }, new Object[] { "IID011", false }, new Object[] { "IID012", false },
				new Object[] { "IID013", false }, new Object[] { "IID014", false }, new Object[] { "IID015", false },
				new Object[] { "IID016", false }, new Object[] { "IID017", false }, new Object[] { "IID018", false },
				new Object[] { "IID019", false }, new Object[] { "IID020", false }, new Object[] { "IID021", false },
				new Object[] { "IID022", false }, new Object[] { "IID023", false }, new Object[] { "IID024", false },
				new Object[] { "IID025", false }, new Object[] { "IID026", false }, new Object[] { "IID027", false },
				new Object[] { "IID028", false }, new Object[] { "IID029", true }, new Object[] { "IID030", true } };
	}

	@BeforeClass
	public void init() {
		pdp = SimplePDPFactory.getSimplePDP();
	}

	public void deployPolicies(String testCase, boolean hasMultiplePolicies) throws Exception {
		List<Evaluatable> evals = new ArrayList<Evaluatable>();
		if (hasMultiplePolicies) {
			evals.add(ResourceLoaderSupport.loadPolicy(POLICY_WITH_INDEX_FILE_PATTERN, testCase, "1"));
			evals.add(ResourceLoaderSupport.loadPolicy(POLICY_WITH_INDEX_FILE_PATTERN, testCase, "2"));
		} else {
			evals.add(ResourceLoaderSupport.loadPolicy(POLICY_FILE_PATTERN, testCase));
		}

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

	@Test(enabled = true, dataProvider = "requestResponse")
	public void testAttrRefConformance(String testCase, boolean hasMultiplePolicies) throws Exception {
		deployPolicies(testCase, hasMultiplePolicies);

		RequestType request = ResourceLoaderSupport.loadRequest(REQUEST_FILE_PATTERN, testCase);
		ResponseType expectedResponse = ResourceLoaderSupport.loadResponse(RESPONSE_FILE_PATTERN, testCase);

		ResponseType response = pdp.evaluate(request);

		OutputStream responseOS = new ByteArrayOutputStream();
		ResponseMarshaller.marshal(response, responseOS);

		OutputStream expectedOS = new ByteArrayOutputStream();
		ResponseMarshaller.marshal(expectedResponse, expectedOS);

		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLAssert.assertXMLEqual(expectedOS.toString(), responseOS.toString());

	}

}