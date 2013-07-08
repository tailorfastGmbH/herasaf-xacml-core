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
 * Java class for EnvironmentMatchType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;EnvironmentMatchType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}AttributeValue&quot;/&gt;
 *         &lt;choice&gt;
 *           &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}EnvironmentAttributeDesignator&quot;/&gt;
 *           &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}AttributeSelector&quot;/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;MatchId&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}anyURI&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 53, for further information.
 * 
 * @author <i>generated</i>
 */
@XmlRootElement(name = "EnvironmentMatch", namespace="urn:oasis:names:tc:xacml:2.0:policy:schema:os")
@XmlType(name = "EnvironmentMatchType", propOrder = { "attributeValue", "environmentAttributeDesignator",
		"attributeSelector" })
public class EnvironmentMatchType implements Serializable, Match {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "AttributeValue", required = true)
	private AttributeValueType attributeValue;
	@XmlElement(name = "EnvironmentAttributeDesignator")
	private EnvironmentAttributeDesignatorType environmentAttributeDesignator;
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
	 * Gets the value of the environmentAttributeDesignator property.
	 * 
	 * @return possible object is {@link AttributeDesignatorType }
	 * 
	 */
	public AttributeDesignatorType getEnvironmentAttributeDesignator() {
		return environmentAttributeDesignator;
	}

	/**
	 * Sets the value of the environmentAttributeDesignator property.
	 * 
	 * @param value
	 *            allowed object is {@link EnvironmentAttributeDesignatorType }
	 * 
	 */
	public void setEnvironmentAttributeDesignator(EnvironmentAttributeDesignatorType value) {
		this.environmentAttributeDesignator = value;
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
		return getEnvironmentAttributeDesignator();
	}
}