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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.5-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.10.03 at 07:56:30 AM CEST 
//

package org.herasaf.xacml.core.context.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.herasaf.xacml.core.converter.DataTypeJAXBTypeAdapter;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;

/**
 * <p>
 * Java class for MissingAttributeDetailType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MissingAttributeDetailType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:context:schema:os}AttributeValue" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="AttributeId" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="DataType" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="Issuer" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * See: <a href= "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20"> OASIS eXtensible Access
 * Control Markup Langugage (XACML) 2.0, Errata 29. January 2008</a> page 81, for further information.
 * 
 * @version 1.0
 * @author <i>generated</i>
 */
@XmlRootElement(name = "MissingAttributeDetail", namespace="urn:oasis:names:tc:xacml:2.0:context:schema:os")
@XmlType(name = "MissingAttributeDetailType", propOrder = { "attributeValues" })
public class MissingAttributeDetailType implements Serializable {

    private static final long serialVersionUID = 1L;
    @XmlElement(name = "AttributeValue")
    private List<AttributeValueType> attributeValues;
    @XmlAttribute(name = "AttributeId", required = true)
    @XmlSchemaType(name = "anyURI")
    private String attributeId;
    @XmlAttribute(name = "DataType", required = true)
    @XmlJavaTypeAdapter(DataTypeJAXBTypeAdapter.class)
    @XmlSchemaType(name = "anyURI")
    private DataTypeAttribute<?> dataType;
    @XmlAttribute(name = "Issuer")
    private String issuer;

    /**
     * Gets the value of the attributeValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the attributeValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getAttributeValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link AttributeValueType }
     * 
     * @return The list of {@link AttributeValueType}s
     * 
     */
    public List<AttributeValueType> getAttributeValues() {
        if (attributeValues == null) {
            attributeValues = new ArrayList<AttributeValueType>();
        }
        return this.attributeValues;
    }

    /**
     * Gets the value of the attributeId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAttributeId() {
        return attributeId;
    }

    /**
     * Sets the value of the attributeId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAttributeId(String value) {
        this.attributeId = value;
    }

    /**
     * Gets the value of the dataType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public DataTypeAttribute<?> getDataType() {
        return dataType;
    }

    /**
     * Sets the value of the dataType property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDataType(DataTypeAttribute<?> value) {
        this.dataType = value;
    }

    /**
     * Gets the value of the issuer property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets the value of the issuer property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setIssuer(String value) {
        this.issuer = value;
    }

}
