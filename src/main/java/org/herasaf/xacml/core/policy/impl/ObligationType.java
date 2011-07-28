/*
 * Copyright 2008 - 2011 HERAS-AF (www.herasaf.org)
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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ObligationType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;ObligationType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}AttributeAssignment&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;ObligationId&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}anyURI&quot; /&gt;
 *       &lt;attribute name=&quot;FulfillOn&quot; use=&quot;required&quot; type=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}EffectType&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 70, for further information.
 * 
 * @author <i>generated</i>
 */
@XmlRootElement(name = "Obligation", namespace="urn:oasis:names:tc:xacml:2.0:policy:schema:os")
@XmlType(name = "ObligationType", propOrder = { "attributeAssignments" })
public class ObligationType implements Serializable {

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "AttributeAssignment")
	private List<AttributeAssignmentType> attributeAssignments;
	@XmlAttribute(name = "ObligationId", required = true)
	@XmlSchemaType(name = "anyURI")
	private String obligationId;
	@XmlAttribute(name = "FulfillOn", required = true)
	private EffectType fulfillOn;

	/**
	 * Initializes an empty obligation. effect and id must be set with the
	 * setters.
	 */
	public ObligationType() {

	}

	/**
	 * Initializes an obligation with the given effect and id.
	 * 
	 * @param id
	 *            The id of the obligation.
	 * @param effect
	 *            The effect of the obligation.
	 */
	public ObligationType(String id, EffectType effect) {
		this.obligationId = id;
		this.fulfillOn = effect;

	}

	/**
	 * Gets the value of the attributeAssignments property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the attributeAssignments property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getAttributeAssignments().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link AttributeAssignmentType }
	 * 
	 * 
	 */
	public List<AttributeAssignmentType> getAttributeAssignments() {
		if (attributeAssignments == null) {
			attributeAssignments = new ArrayList<AttributeAssignmentType>();
		}
		return this.attributeAssignments;
	}

	/**
	 * Gets the value of the obligationId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getObligationId() {
		return obligationId;
	}

	/**
	 * Sets the value of the obligationId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setObligationId(String value) {
		this.obligationId = value;
	}

	/**
	 * Gets the value of the fulfillOn property.
	 * 
	 * @return possible object is {@link EffectType }
	 * 
	 */
	public EffectType getFulfillOn() {
		return fulfillOn;
	}

	/**
	 * Sets the value of the fulfillOn property.
	 * 
	 * @param value
	 *            allowed object is {@link EffectType }
	 * 
	 */
	public void setFulfillOn(EffectType value) {
		this.fulfillOn = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		// FIXME Replace such code with Apache Commons Lang ToStringBuilder (see
		// HERASAFXACMLCORE-82)
		StringBuilder val = new StringBuilder("ObligationType[attributeAssignment=");
		val.append(attributeAssignments);
		val.append(", obligationId=");
		val.append(obligationId);
		val.append(", fulfillOn=");
		val.append(fulfillOn);
		val.append("]");

		return val.toString();
	}
}