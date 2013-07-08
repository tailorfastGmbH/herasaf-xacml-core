/*
 * Copyright 2008-2010 HERAS-AF (www.herasaf.org)
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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.impl.AttributeType;
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
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 63, for further information.
 * 
 * @author <i>generated</i>
 * @author Sacha Dolski
 */
@XmlRootElement
@XmlType(name = "ActionAttributeDesignatorType")
public class ActionAttributeDesignatorType extends AttributeDesignatorType {
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.core.policy.impl.AttributeDesignatorType#handle(org.herasaf
	 * .core.context.impl.RequestType, java.util.Map)
	 */
	@Override
	public Object handle(RequestType request, EvaluationContext evaluationContext) throws ExpressionProcessingException,
			MissingAttributeException, SyntaxException {
		List<Object> returnValues = new ArrayList<Object>();

		// A RequestType is not thread safe, because of this you can iterate
		// over it.
		for (AttributeType attr : request.getAction().getAttributes()) {
			if (getAttributeId().equals(attr.getAttributeId()) && getDataType().toString().equals(attr.getDataType().toString())) {
				if (getIssuer() != null) {
					if (getIssuer().equals(attr.getIssuer())) {
						addAndConvertAttrValue(returnValues, attr.getAttributeValues());
					}
				} else {
					addAndConvertAttrValue(returnValues, attr.getAttributeValues());
				}
			}
		}

		/*
		 * If no Attribute could be found, the attribute has to be requested
		 * from a Policy Information Point.
		 * 
		 * See: the OASIS eXtensible Access Control Markup Langugage (XACML)
		 * 2.0, Errata 29 June 2006 page 78, chapter Attribute Retrieval, for
		 * further information.
		 */
		if (returnValues.size() == 0 && evaluationContext.getPIP() != null) {
			List<AttributeValueType> attrValues = evaluationContext.getPIP().addActionAttributesToRequest(request, getAttributeId(),
					getDataType().toString(), getIssuer());
			addAndConvertAttrValue(returnValues, attrValues);
		}
		if (returnValues.size() == 0 && isMustBePresent()) {
			throw new MissingAttributeException(getAttributeId(), getDataType(), getIssuer());
		}
		return returnValues;
	}
}