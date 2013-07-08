package org.herasaf.xacml.core.dataTypeAttribute.impl;

import java.util.List;

import org.herasaf.xacml.core.SyntaxException;

public abstract class AbstractSimpleDataTypeAttribute<T> extends
		AbstractDataTypeAttribute<T> {

	private static final long serialVersionUID = 1L;

	public T convertTo(List<?> jaxbRepresentation) throws SyntaxException {
		Object simpleType = jaxbRepresentation.get(0);
		return convertTo(((String) simpleType).trim());
	}

	public abstract T convertTo(String jaxbRepresentation)
			throws SyntaxException;
}
