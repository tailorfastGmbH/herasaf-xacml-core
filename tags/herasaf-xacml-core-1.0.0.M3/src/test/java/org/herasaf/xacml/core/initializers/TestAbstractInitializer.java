package org.herasaf.xacml.core.initializers;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.herasaf.xacml.core.converter.FunctionsJAXBTypeAdapter;
import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
import org.herasaf.xacml.core.simplePDP.initializers.DataTypesJAXBInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.Initializer;
import org.herasaf.xacml.core.simplePDP.initializers.JAXBInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.PolicyCombiningAlgorithmsJAXBInitializer;
import org.herasaf.xacml.core.simplePDP.initializers.RuleCombiningAlgorithmsJAXBInitializer;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class TestAbstractInitializer {

	@Test
	public void testAdditionalSearchContextPathForFunctions() throws Exception {
		Set<Initializer> initializers = new HashSet<Initializer>();
		initializers.add(new TestFunctionInitializerExtension());
		initializers.add(new DataTypesJAXBInitializer());
		initializers.add(new RuleCombiningAlgorithmsJAXBInitializer());
		initializers.add(new PolicyCombiningAlgorithmsJAXBInitializer());
		initializers.add(new JAXBInitializer());

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