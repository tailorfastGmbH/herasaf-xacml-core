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
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObligationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ObligationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}AttributeAssignment" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ObligationId" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="FulfillOn" use="required" type="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}EffectType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * See:	<a href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June 2006</a> page 66, for further information.
 * 
 * @version 1.0
 * @author <i>generated</i>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObligationType", propOrder = {
    "attributeAssignments"
})
public class ObligationType
    implements Serializable
{

    private final static long serialVersionUID = 632768732L;
    @XmlElement(name = "AttributeAssignment")
    protected List<AttributeAssignmentType> attributeAssignments;
    @XmlAttribute(name = "ObligationId", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String obligationId;
    @XmlAttribute(name = "FulfillOn", required = true)
    protected EffectType fulfillOn;

    /**
     * Gets the value of the attributeAssignments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributeAssignments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributeAssignments().add(newItem);
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
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObligationId() {
        return obligationId;
    }

    /**
     * Sets the value of the obligationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObligationId(String value) {
        this.obligationId = value;
    }

    /**
     * Gets the value of the fulfillOn property.
     * 
     * @return
     *     possible object is
     *     {@link EffectType }
     *     
     */
    public EffectType getFulfillOn() {
        return fulfillOn;
    }

    /**
     * Sets the value of the fulfillOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link EffectType }
     *     
     */
    public void setFulfillOn(EffectType value) {
        this.fulfillOn = value;
    }
}