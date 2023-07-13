/*
 * Copyright 2008 - 2013 HERAS-AF (www.herasaf.org)
 * Holistic Enterprise-Ready Application Security Architecture Framework
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.herasaf.xacml.core.policy.impl;

import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.ExpressionProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;

/**
 * <p>
 * Java class for ActionAttributeDesignatorType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;ActionAttributeDesignatorType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}AttributeDesignatorType&quot;&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 67, for further information.
 * 
 * @author <i>generated</i>
 * @author Sacha Dolski
 */
@XmlRootElement(name = "ActionAttributeDesignator", namespace="urn:oasis:names:tc:xacml:2.0:policy:schema:os")
@XmlType(name = "ActionAttributeDesignatorType")
public class ActionAttributeDesignatorType extends AttributeDesignatorType {
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object handle(RequestType request, EvaluationContext evaluationContext) throws ExpressionProcessingException,
			MissingAttributeException, SyntaxException {
		validateAttributeDesignator();
		List<Object> returnValues = handle(request);

		/*
		 * If no Attribute could be found, the attribute has to be requested
		 * from a Policy Information Point.
		 * 
		 * See: the OASIS eXtensible Access Control Markup Langugage (XACML)
		 * 2.0, Errata 29 January 2008 page 83, chapter Attribute Retrieval, for
		 * further information.
		 */
		if (returnValues.size() == 0 && evaluationContext.getPIP() != null) {
			List<AttributeValueType> attrValues = evaluationContext.getPIP().fetchActionAttributes(request, getAttributeId(),
					getDataType().toString(), getIssuer());
			addAndConvertAttrValue(returnValues, attrValues);
		}
		if (returnValues.size() == 0 && isMustBePresent()) {
			throw new MissingAttributeException(getAttributeId(), getDataType(), getIssuer());
		}
		return returnValues;
	}
	
	public List<Object> handle(RequestType request) throws ExpressionProcessingException,
			MissingAttributeException, SyntaxException {
		List<Object> returnValues = handle(request.getAction().getAttributes());
		return returnValues;
	}
}