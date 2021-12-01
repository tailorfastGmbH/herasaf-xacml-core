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

public class TestConformanceFunctionEvaluation {

    private static final String RESPONSE_FILE_PATTERN = "/conformanceTests/functionEvaluation/%sResponse.xml";
    private static final String REQUEST_FILE_PATTERN = "/conformanceTests/functionEvaluation/%sRequest.xml";
    private static final String POLICY_FILE_PATTERN = "/conformanceTests/functionEvaluation/%sPolicy.xml";

    private PDP pdp;

    @DataProvider(name = "requestResponse")
    public Object[][] requestResponse() throws Exception {
        return new Object[][] { new Object[] { "IIC001" }, new Object[] { "IIC002" }, new Object[] { "IIC003" },
                new Object[] { "IIC004" }, new Object[] { "IIC005" }, new Object[] { "IIC006" },
                new Object[] { "IIC007" }, new Object[] { "IIC008" }, new Object[] { "IIC009" },
                new Object[] { "IIC010" }, new Object[] { "IIC011" }, new Object[] { "IIC012" },
                new Object[] { "IIC013" }, new Object[] { "IIC014" }, new Object[] { "IIC015" },
                new Object[] { "IIC016" }, new Object[] { "IIC017" }, new Object[] { "IIC018" },
                new Object[] { "IIC019" }, new Object[] { "IIC020" }, new Object[] { "IIC021" },
                new Object[] { "IIC022" }, new Object[] { "IIC024" }, new Object[] { "IIC025" },
                new Object[] { "IIC026" }, new Object[] { "IIC027" }, new Object[] { "IIC028" },
                new Object[] { "IIC029" }, new Object[] { "IIC030" }, new Object[] { "IIC031" },
                new Object[] { "IIC032" }, new Object[] { "IIC033" }, new Object[] { "IIC034" },
                new Object[] { "IIC035" }, new Object[] { "IIC036" }, new Object[] { "IIC037" },
                new Object[] { "IIC038" }, new Object[] { "IIC039" }, new Object[] { "IIC040" },
                new Object[] { "IIC041" }, new Object[] { "IIC042" }, new Object[] { "IIC043" },
                new Object[] { "IIC044" }, new Object[] { "IIC045" }, new Object[] { "IIC046" },
                new Object[] { "IIC047" }, new Object[] { "IIC048" }, new Object[] { "IIC049" },
                new Object[] { "IIC050" }, new Object[] { "IIC051" }, new Object[] { "IIC052" },
                new Object[] { "IIC053" }, new Object[] { "IIC056" }, new Object[] { "IIC057" },
                new Object[] { "IIC058" }, new Object[] { "IIC059" }, new Object[] { "IIC060" },
                new Object[] { "IIC061" }, new Object[] { "IIC062" }, new Object[] { "IIC063" },
                new Object[] { "IIC064" }, new Object[] { "IIC065" }, new Object[] { "IIC066" },
                new Object[] { "IIC067" }, new Object[] { "IIC068" }, new Object[] { "IIC069" },
                new Object[] { "IIC070" }, new Object[] { "IIC071" }, new Object[] { "IIC072" },
                new Object[] { "IIC073" }, new Object[] { "IIC074" }, new Object[] { "IIC075" },
                new Object[] { "IIC076" }, new Object[] { "IIC077" }, new Object[] { "IIC078" },
                new Object[] { "IIC079" }, new Object[] { "IIC080" }, new Object[] { "IIC081" },
                new Object[] { "IIC082" }, new Object[] { "IIC083" }, new Object[] { "IIC084" },
                new Object[] { "IIC085" }, new Object[] { "IIC086" }, new Object[] { "IIC087" },
                new Object[] { "IIC090" }, new Object[] { "IIC091" }, new Object[] { "IIC094" },
                new Object[] { "IIC095" }, new Object[] { "IIC096" }, new Object[] { "IIC097" },
                new Object[] { "IIC100" }, new Object[] { "IIC101" }, new Object[] { "IIC102" },
                new Object[] { "IIC103" }, new Object[] { "IIC104" }, new Object[] { "IIC105" },
                new Object[] { "IIC106" }, new Object[] { "IIC107" }, new Object[] { "IIC108" },
                new Object[] { "IIC109" }, new Object[] { "IIC110" }, new Object[] { "IIC111" },
                new Object[] { "IIC112" }, new Object[] { "IIC113" }, new Object[] { "IIC114" },
                new Object[] { "IIC115" }, new Object[] { "IIC116" }, new Object[] { "IIC117" },
                new Object[] { "IIC118" }, new Object[] { "IIC119" }, new Object[] { "IIC120" },
                new Object[] { "IIC121" }, new Object[] { "IIC122" }, new Object[] { "IIC123" },
                new Object[] { "IIC124" }, new Object[] { "IIC125" }, new Object[] { "IIC126" },
                new Object[] { "IIC127" }, new Object[] { "IIC128" }, new Object[] { "IIC129" },
                new Object[] { "IIC130" }, new Object[] { "IIC131" }, new Object[] { "IIC132" },
                new Object[] { "IIC133" }, new Object[] { "IIC134" }, new Object[] { "IIC135" },
                new Object[] { "IIC136" }, new Object[] { "IIC137" }, new Object[] { "IIC138" },
                new Object[] { "IIC139" }, new Object[] { "IIC140" }, new Object[] { "IIC141" },
                new Object[] { "IIC142" }, new Object[] { "IIC143" }, new Object[] { "IIC144" },
                new Object[] { "IIC145" }, new Object[] { "IIC146" }, new Object[] { "IIC147" },
                new Object[] { "IIC148" }, new Object[] { "IIC149" }, new Object[] { "IIC150" },
                new Object[] { "IIC151" }, new Object[] { "IIC152" }, new Object[] { "IIC153" },
                new Object[] { "IIC154" }, new Object[] { "IIC155" }, new Object[] { "IIC156" },
                new Object[] { "IIC157" }, new Object[] { "IIC158" }, new Object[] { "IIC159" },
                new Object[] { "IIC160" }, new Object[] { "IIC161" }, new Object[] { "IIC162" },
                new Object[] { "IIC163" }, new Object[] { "IIC164" }, new Object[] { "IIC165" },
                new Object[] { "IIC166" }, new Object[] { "IIC167" }, new Object[] { "IIC168" },
                new Object[] { "IIC169" }, new Object[] { "IIC170" }, new Object[] { "IIC171" },
                new Object[] { "IIC172" }, new Object[] { "IIC173" }, new Object[] { "IIC174" },
                new Object[] { "IIC175" }, new Object[] { "IIC176" }, new Object[] { "IIC177" },
                new Object[] { "IIC178" }, new Object[] { "IIC179" }, new Object[] { "IIC180" },
                new Object[] { "IIC181" }, new Object[] { "IIC182" }, new Object[] { "IIC183" },
                new Object[] { "IIC184" }, new Object[] { "IIC185" }, new Object[] { "IIC186" },
                new Object[] { "IIC187" }, new Object[] { "IIC188" }, new Object[] { "IIC189" },
                new Object[] { "IIC190" }, new Object[] { "IIC191" }, new Object[] { "IIC192" },
                new Object[] { "IIC193" }, new Object[] { "IIC194" }, new Object[] { "IIC195" },
                new Object[] { "IIC196" }, new Object[] { "IIC197" }, new Object[] { "IIC198" },
                new Object[] { "IIC199" }, new Object[] { "IIC200" }, new Object[] { "IIC201" },
                new Object[] { "IIC202" }, new Object[] { "IIC203" }, new Object[] { "IIC204" },
                new Object[] { "IIC205" }, new Object[] { "IIC206" }, new Object[] { "IIC207" },
                new Object[] { "IIC208" }, new Object[] { "IIC209" }, new Object[] { "IIC210" },
                new Object[] { "IIC211" }, new Object[] { "IIC212" }, new Object[] { "IIC213" },
                new Object[] { "IIC214" }, new Object[] { "IIC215" }, new Object[] { "IIC216" },
                new Object[] { "IIC217" }, new Object[] { "IIC218" }, new Object[] { "IIC219" },
                new Object[] { "IIC220" }, new Object[] { "IIC221" }, new Object[] { "IIC222" },
                new Object[] { "IIC223" }, new Object[] { "IIC224" }, new Object[] { "IIC225" },
                new Object[] { "IIC226" }, new Object[] { "IIC227" }, new Object[] { "IIC228" },
                new Object[] { "IIC229" }, new Object[] { "IIC230" }, new Object[] { "IIC231" },
                new Object[] { "IIC232" },

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
        String testCase = "IIC223";
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
}