package org.herasaf.xacml.core.initializers;

import java.util.HashMap;
import java.util.Map;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.typeadapter.xacml20.functions.AbstractFunctionsJaxbTypeAdapterInitializer;

public class TestFunctionInitializerExtension extends AbstractFunctionsJaxbTypeAdapterInitializer {

	@Override
	protected Map<String, Function> createTypeInstances() {
		Map<String, Function> instancesMap = new HashMap<String, Function>();
		TestFunction testfunction = new TestFunction();
		instancesMap.put(testfunction.getFunctionId(), testfunction);
		
		return instancesMap;
	}
	
}
