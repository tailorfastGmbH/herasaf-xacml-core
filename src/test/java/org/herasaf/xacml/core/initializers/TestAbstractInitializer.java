package org.herasaf.xacml.core.initializers;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.herasaf.xacml.core.converter.FunctionsJAXBTypeAdapter;
import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
import org.herasaf.xacml.core.simplePDP.initializers.api.Initializer;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.JaxbContextInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.xacml20.datatypes.Xacml20DefaultDataTypesJaxbInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.xacml20.functions.Xacml20DefaultFunctionsJaxbInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.xacml20.policycombiningalgorithms.Xacml20DefaultPolicyCombiningAlgorithmsJaxbInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.xacml20.rulecombiningalgorithms.Xacml20DefaultRuleCombiningAlgorithmsJaxbInitializer;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class TestAbstractInitializer {

	@Test
	public void testAdditionalSearchContextPathForFunctions() throws Exception {
		Set<Initializer> initializers = new HashSet<Initializer>();
		initializers.add(new Xacml20DefaultFunctionsJaxbInitializer());
		initializers.add(new Xacml20DefaultDataTypesJaxbInitializer());
		initializers.add(new Xacml20DefaultRuleCombiningAlgorithmsJaxbInitializer());
		initializers.add(new Xacml20DefaultPolicyCombiningAlgorithmsJaxbInitializer());
		initializers.add(new JaxbContextInitializer());
		initializers.add(new TestFunctionInitializerExtension());

		SimplePDPFactory.setInitalizers(initializers);

		SimplePDPFactory.getSimplePDP();

		Field f = FunctionsJAXBTypeAdapter.class.getDeclaredField("functions");
		f.setAccessible(true);

		Object fieldValue = f.get(null);
		@SuppressWarnings("unchecked")
		Map<String, Function> functions = (Map<String, Function>) fieldValue;

		Assert.assertTrue(functions.containsKey("urn:oasis:names:tc:xacml:1.0:function:date-greater-than")); // tests
																												// whether
																												// the
																												// default
																												// functions
																												// are
																												// still
																												// present
		Assert.assertTrue(functions.containsKey("org:herasaf:testFunctionID:1")); // tests whether the new functions is
																					// present
		Assert.assertEquals(functions.size(), 210); // tests whether the number of functions is correct (209 default
													// XACML functions)
	}

	@AfterTest
	public void cleanUp() {
		SimplePDPFactory.resetInitializers();
	}
}