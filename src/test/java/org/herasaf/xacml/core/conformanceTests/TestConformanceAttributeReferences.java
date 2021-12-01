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

public class TestConformanceAttributeReferences {

    private static final String RESPONSE_FILE_PATTERN = "/conformanceTests/attributeReferences/%sResponse.xml";
    private static final String REQUEST_FILE_PATTERN = "/conformanceTests/attributeReferences/%sRequest.xml";
    private static final String POLICY_FILE_PATTERN = "/conformanceTests/attributeReferences/%sPolicy.xml";

    private PDP pdp;

    @DataProvider(name = "requestResponse")
    public Object[][] requestResponse() throws Exception {
        return new Object[][] { new Object[] { "IIA001" }, new Object[] { "IIA003" }, new Object[] { "IIA004" },
                new Object[] { "IIA005" }, new Object[] { "IIA006" }, new Object[] { "IIA008" },
                new Object[] { "IIA010" }, new Object[] { "IIA011" }, new Object[] { "IIA012" },
                new Object[] { "IIA013" }, new Object[] { "IIA014" }, new Object[] { "IIA015" },
                new Object[] { "IIA016" }, new Object[] { "IIA017" }, new Object[] { "IIA018" },
                new Object[] { "IIA019" }, new Object[] { "IIA020" }, new Object[] { "IIA021" } };

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
        String testCase = "IIA004";
        testAttrRefConformance(testCase);
    }

    @Test(enabled = true, dataProvider = "requestResponse")
    public void testAttrRefConformance(String testCase) throws Exception {
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

    // @BeforeMethod
    // public void beforeMethod() throws Exception {
    // Evaluatable eval;

    // // Test IIA002 will not be tested because no contextHandler exists in
    // // this implementation.
    // if (i == 2) {
    // i++;
    // }
    // // Test IIA007 will not be tested because it doesn't make sense to
    // // return the status missing-attributes in the targetmatcher.
    // if (i == 7) {
    // i++;
    // }

    // if (i < 10) {
    // eval = PolicyConverter.unmarshal(new File(
    // "src/test/resources/conformanceTests/attributeReferences/IIA00"
    // + i + "Policy.xml"));
    // } else {
    // eval = PolicyConverter.unmarshal(new File(
    // "src/test/resources/conformanceTests/attributeReferences/IIA0"
    // + i + "Policy.xml"));

    // }

    // List<Evaluatable> evals = new ArrayList<Evaluatable>();
    // evals.add(eval);
    // pdp.deploy(evals);

    // i++;
    // }

    // @AfterMethod
    // public void afterMethod() throws Exception {
    // pdp.undeployAll();
    // }

    // @Test(dataProvider = "requestResponse")
    // public void testAttrRefConformance(RequestCtx req, String expectedRes) throws
    // Exception {
    // ResponseCtx response = pdp.evaluate(req);

    // OutputStream os = new ByteArrayOutputStream();
    // response.marshal(os);

    // XMLUnit.setIgnoreWhitespace(true);
    // XMLUnit.setIgnoreAttributeOrder(true);

    // XMLAssert.assertXMLEqual(expectedRes, os.toString());
    // }

    // private RequestCtx loadUnmarshalledMarshalledFile(int reqNr) throws Exception
    // {
    // OutputStream os = new ByteArrayOutputStream();

    // RequestCtx req = RequestCtxFactory.unmarshal(
    // new File("src/test/resources/conformanceTests/attributeReferences/IIA0" +
    // reqNr + "Request.xml"));
    // req.marshal(os);
    // InputStream is = new ByteArrayInputStream(os.toString().getBytes());
    // return RequestCtxFactory.unmarshal(is);

    // }

    // private byte[] loadFile(String fileName) {
    // OutputStream stream = new ByteArrayOutputStream();
    // File f = new File(fileName);
    // FileReader fr = null;
    // try {
    // fr = new FileReader(f);
    // } catch (FileNotFoundException e1) {
    // e1.printStackTrace();
    // }
    // BufferedReader br = new BufferedReader(fr);
    // String line = null;
    // try {
    // while ((line = br.readLine()) != null) {
    // stream.write((line + "\n").getBytes());
    // }
    // } catch (IOException e1) {
    // fail("IO Exception occured.");
    // } finally {
    // try {
    // if(br != null){
    // br.close();
    // }
    // } catch (IOException e) {
    // fail("Unable to close the BufferedReader.");
    // }
    // }
    // return stream.toString().getBytes();
    // }
}