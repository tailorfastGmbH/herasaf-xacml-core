package org.herasaf.xacml.core.converter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import org.herasaf.xacml.core.policy.EvaluatableID;
import org.herasaf.xacml.core.policy.impl.EvaluatableIDImpl;

/**
 * TODO Javadoc
 * 
 * @author Florian Huonder
 */
public class EvaluatableIdJAXBTypeAdapter extends
		XmlAdapter<String, EvaluatableID> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String marshal(EvaluatableID evalId) throws Exception {
		return evalId.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EvaluatableID unmarshal(String idStringRepresentation) throws Exception {
		return new EvaluatableIDImpl(idStringRepresentation);
	}

}
