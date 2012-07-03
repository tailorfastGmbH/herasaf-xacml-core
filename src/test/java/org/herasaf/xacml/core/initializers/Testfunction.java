package org.herasaf.xacml.core.initializers;

import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.FunctionProcessingException;

public class Testfunction implements Function {
	private static final long serialVersionUID = 1L;

	public String getFunctionId() {
		return "org:herasaf:testFunctionID:1";
	}

	public Object handle(Object... args) throws FunctionProcessingException {
		return null;
		// Can be null because the function will never be called.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getFunctionId();
	}
}