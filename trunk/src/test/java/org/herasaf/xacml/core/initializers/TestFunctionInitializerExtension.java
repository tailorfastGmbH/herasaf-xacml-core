package org.herasaf.xacml.core.initializers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.herasaf.xacml.core.simplePDP.initializers.FunctionsJAXBInitializer;

public class TestFunctionInitializerExtension extends FunctionsJAXBInitializer {

	@Override
	protected Collection<String> getCustomSearchContexts() {
		List<String> contextPaths = new ArrayList<String>();
		
		contextPaths.add("org/herasaf/xacml/core/initializers");
		
		return contextPaths;
	}
	
}
