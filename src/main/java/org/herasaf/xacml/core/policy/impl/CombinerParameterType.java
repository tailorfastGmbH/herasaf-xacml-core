/*
 * Copyright 2008 HERAS-AF (www.herasaf.org)
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for CombinerParameterType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="CombinerParameterType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}AttributeValue"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ParameterName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 55, for further information.
 * 
 * @version 1.0
 * @author <i>generated</i>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CombinerParameterType", propOrder = { "attributeValue" })
public class CombinerParameterType implements Serializable {

	private static final long serialVersionUID = 632768732L;
	@XmlElement(name = "AttributeValue", required = true)
	private AttributeValueType attributeValue;
	@XmlAttribute(name = "ParameterName", required = true)
	private String parameterName;

	/**
	 * Gets the value of the attributeValue property.
	 * 
	 * @return possible object is {@link AttributeValueType }
	 * 
	 */
	public AttributeValueType getAttributeValue() {
		return attributeValue;
	}

	/**
	 * Sets the value of the attributeValue property.
	 * 
	 * @param value
	 *            allowed object is {@link AttributeValueType }
	 * 
	 */
	public void setAttributeValue(AttributeValueType value) {
		this.attributeValue = value;
	}

	/**
	 * Gets the value of the parameterName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * Sets the value of the parameterName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setParameterName(String value) {
		this.parameterName = value;
	}
}