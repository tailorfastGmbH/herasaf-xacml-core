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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.herasaf.xacml.core.converter.FunctionsJAXBTypeAdapter;
import org.herasaf.xacml.core.function.Function;

/**
 * <p>
 * Java class for ActionMatchType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ActionMatchType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}AttributeValue"/>
 *         &lt;choice>
 *           &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}ActionAttributeDesignator"/>
 *           &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}AttributeSelector"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="MatchId" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 52, for further information.
 * 
 * @author <i>generated</i>
 */
@XmlRootElement(name = "ActionMatch", namespace="urn:oasis:names:tc:xacml:2.0:policy:schema:os")
@XmlType(name = "ActionMatchType", propOrder = { "attributeValue", "actionAttributeDesignator", "attributeSelector" })
public class ActionMatchType implements Serializable, Match {

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "AttributeValue", required = true)
	private AttributeValueType attributeValue;
	@XmlElement(name = "ActionAttributeDesignator")
	private ActionAttributeDesignatorType actionAttributeDesignator;
	@XmlElement(name = "AttributeSelector")
	private AttributeSelectorType attributeSelector;
	@XmlAttribute(name = "MatchId", required = true)
	@XmlJavaTypeAdapter(FunctionsJAXBTypeAdapter.class)
	@XmlSchemaType(name = "anyURI")
	private Function matchFunction;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.core.policy.impl.Match#getAttributeValue()
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
	 * Gets the value of the actionAttributeDesignator property.
	 * 
	 * @return possible object is {@link AttributeDesignatorType }
	 * 
	 */
	public AttributeDesignatorType getActionAttributeDesignator() {
		return actionAttributeDesignator;
	}

	/**
	 * Sets the value of the actionAttributeDesignator property.
	 * 
	 * @param value
	 *            allowed object is {@link ActionAttributeDesignatorType }
	 * 
	 */
	public void setActionAttributeDesignator(ActionAttributeDesignatorType value) {
		this.actionAttributeDesignator = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.core.policy.impl.Match#getAttributeSelector()
	 */
	public AttributeSelectorType getAttributeSelector() {
		return attributeSelector;
	}

	/**
	 * Sets the value of the attributeSelector property.
	 * 
	 * @param value
	 *            allowed object is {@link AttributeSelectorType }
	 * 
	 */
	public void setAttributeSelector(AttributeSelectorType value) {
		this.attributeSelector = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.core.policy.impl.Match#getMatchFunction()
	 */
	public Function getMatchFunction() {
		return matchFunction;
	}

	/**
	 * Sets the value of the matchFunction property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMatchFunction(Function value) {
		this.matchFunction = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.core.policy.impl.Match#getAttributeDesignator()
	 */
	public AttributeDesignatorType getAttributeDesignator() {
		return getActionAttributeDesignator();
	}
}