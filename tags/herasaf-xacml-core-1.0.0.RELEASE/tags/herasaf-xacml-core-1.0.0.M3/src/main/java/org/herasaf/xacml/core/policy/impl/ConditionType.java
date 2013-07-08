/*
 * Copyright 2008 - 2012 HERAS-AF (www.herasaf.org)
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

import java.io.Serializable;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ConditionType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ConditionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Expression"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
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
@XmlRootElement(name = "Condition", namespace="urn:oasis:names:tc:xacml:2.0:policy:schema:os")
@XmlType(name = "ConditionType", propOrder = { "expression" })
public class ConditionType implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElementRef(name = "Expression", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = JAXBElement.class)
	private JAXBElement<?> expression;

	/**
	 * Gets the value of the expression property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}
	 *         {@link AttributeSelectorType }{@code >} {@link JAXBElement }{@code
	 *         <}{@link ExpressionType }{@code >} {@link JAXBElement }{@code <}
	 *         {@link FunctionType }{@code >} {@link JAXBElement }{@code <}
	 *         {@link ApplyType }{@code >} {@link JAXBElement }{@code <}
	 *         {@link SubjectAttributeDesignatorType }{@code >}
	 *         {@link JAXBElement }{@code <}
	 *         {@link ResourceAttributeDesignatorType }{@code >}
	 *         {@link JAXBElement }{@code <}
	 *         {@link EnvironmentAttributeDesignatorType }{@code >}
	 *         {@link JAXBElement }{@code <}{@link VariableReferenceType }{@code
	 *         >} {@link JAXBElement }{@code <}{@link AttributeValueType }{@code
	 *         >} {@link JAXBElement }{@code <}
	 *         {@link ActionAttributeDesignatorType }{@code >}
	 * 
	 */
	public JAXBElement<?> getExpression() {
		return expression;
	}

	/**
	 * Sets the value of the expression property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}
	 *            {@link AttributeSelectorType }{@code >} {@link JAXBElement }
	 *            {@code <}{@link ExpressionType }{@code >} {@link JAXBElement }
	 *            {@code <}{@link FunctionType }{@code >} {@link JAXBElement }
	 *            {@code <}{@link ApplyType }{@code >} {@link JAXBElement }{@code
	 *            <}{@link SubjectAttributeDesignatorType }{@code >}
	 *            {@link JAXBElement }{@code <}
	 *            {@link ResourceAttributeDesignatorType }{@code >}
	 *            {@link JAXBElement }{@code <}
	 *            {@link EnvironmentAttributeDesignatorType }{@code >}
	 *            {@link JAXBElement }{@code <}{@link VariableReferenceType }
	 *            {@code >} {@link JAXBElement }{@code <}
	 *            {@link AttributeValueType }{@code >} {@link JAXBElement }{@code
	 *            <}{@link ActionAttributeDesignatorType }{@code >}
	 * 
	 */
	public void setExpression(JAXBElement<?> value) {
		this.expression = value;
	}
}