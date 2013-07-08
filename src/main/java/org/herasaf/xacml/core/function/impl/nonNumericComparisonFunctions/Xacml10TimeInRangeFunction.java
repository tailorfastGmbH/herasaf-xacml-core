package org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions;

/**
 * This function has the same functionality the {@link TimeInRangeFunction}.
 * This function provides the same functionality under a different function id.
 * The reason is that the XACML 2.0 specification is ambiguous in terms of which
 * identifier is to be used for the 'time-in-range' function. See
 * https://lists.oasis-open.org/archives/xacml-comment/200808/msg00004.html
 * 
 * We assume that the function is meant to have the XACML version 2.0 because in
 * the XACML 1.0 specification no 'time-in-range' function was specified. So
 * this "XAMCL 1.0" 'time-in-range' function is only for compatibility that
 * everything works as expected if someone uses the identifier for XACML 1.0.
 */
public class Xacml10TimeInRangeFunction extends TimeInRangeFunction {
	private static final long serialVersionUID = 1L;

	public static final String ID = "urn:oasis:names:tc:xacml:1.0:function:time-in-range";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFunctionId() {
		return ID;
	}
}