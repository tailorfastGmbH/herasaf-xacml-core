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

import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;

/**
 * <p>
 * Java class for VariableReferenceType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="VariableReferenceType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}ExpressionType">
 *       &lt;attribute name="VariableId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 63, for further information.
 * 
 * @author <i>generated</i>
 */
@XmlRootElement(name = "VariableReference", namespace="urn:oasis:names:tc:xacml:2.0:policy:schema:os")
@XmlType(name = "VariableReferenceType")
public class VariableReferenceType extends ExpressionType {
	private static final long serialVersionUID = 1L;
	@XmlAttribute(name = "VariableId", required = true)
	private String variableId;

	/**
	 * Gets the value of the variableId property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getVariableId() {
		return variableId;
	}

	/**
	 * Sets the value of the variableId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setVariableId(String value) {
		this.variableId = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.xacml.core.policy.impl.ExpressionType#handle(org.herasaf.
	 * xacml.core.context.impl.RequestType, java.util.Map)
	 */
	@Override
	public Object handle(RequestType request, EvaluationContext evaluationContext) throws MissingAttributeException,
			SyntaxException, ProcessingException {

		Map<String, Variable> variableDefinitions = evaluationContext.getVariableDefinitions();
		Variable var = variableDefinitions.get(variableId);
		Object value = var.getValue(request, evaluationContext);
		if (var instanceof VariableDefinitionType) {
			variableDefinitions.put(variableId, new VariableValue(value));
		}
		return value;
	}
}