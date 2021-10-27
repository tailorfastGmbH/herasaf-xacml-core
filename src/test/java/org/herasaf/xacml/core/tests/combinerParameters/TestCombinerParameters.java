package org.herasaf.xacml.core.tests.combinerParameters;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.PolicyMarshaller;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.herasaf.xacml.core.simplePDP.initializers.InitializerExecutor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestCombinerParameters {

    @BeforeClass
    public void setup() throws Exception {
        InitializerExecutor.runInitializers();
    }

    @Test
    public void testCP() throws Exception {
        Evaluatable eval = PolicyMarshaller.unmarshal(TestCombinerParameters.class
                .getResource("/org/herasaf/xacml/core/tests/combinerParameterPolicy/PolicyWithCombinerParameters.xml"));
        assertNotNull(eval);

        PolicyType p = ((PolicyType) eval);

        // Two (2) because the CombinerParameters element (before) the Target, is mapped to the element in
        // AdditionalInformation.
        assertEquals(2, p.getAdditionalInformation().size());
    }
}
