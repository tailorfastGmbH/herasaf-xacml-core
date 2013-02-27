package org.herasaf.xacml.core.dataTypeAttribute.impl;

import java.util.List;

import org.herasaf.xacml.core.SyntaxException;

public abstract class AbstractSimpleDataTypeAttribute<T> extends AbstractDataTypeAttribute<T> {

	private static final long serialVersionUID = 1L;

	public T convertTo(List<?> jaxbRepresentation) throws SyntaxException {
//		if (jaxbRepresentation.size() != 1) {
//			throw new SyntaxException("Unexpected list size, should only contain 1 element.");
//		}
		Object simpleType = jaxbRepresentation.get(0);
//		if (!(simpleType instanceof String)) {
//			throw new SyntaxException("Elemenet should be a string, but is a " + simpleType.getClass().getName() + ".");
//		}
		return convertTo(((String) simpleType).trim());
	}
	
	public abstract T convertTo(String jaxbRepresentation) throws SyntaxException;
}
