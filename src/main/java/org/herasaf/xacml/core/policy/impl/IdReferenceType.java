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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.herasaf.xacml.core.combiningAlgorithm.CombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.reference.IdReferenceTypeDelegateCombiningAlgorithm;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;


/**
 * <p>Java class for IdReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IdReferenceType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>anyURI">
 *       &lt;attribute name="Version" type="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}VersionMatchType" />
 *       &lt;attribute name="EarliestVersion" type="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}VersionMatchType" />
 *       &lt;attribute name="LatestVersion" type="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}VersionMatchType" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * See:	<a href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June 2006</a> page 51 (PolicySetIdReferenceType)
 * and page 52 (PolicyIdReferenceType), for further information.
 * 
 * @version 1.0
 * @author <i>generated</i>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IdReferenceType", propOrder = {
    "value"
})
public class IdReferenceType implements Serializable, Evaluatable
{

    private final static long serialVersionUID = 632768732L;
    @XmlValue
    @XmlSchemaType(name = "anyURI")
    protected String value;
    @XmlAttribute(name = "Version")
    protected String version;
    @XmlAttribute(name = "EarliestVersion")
    protected String earliestVersion;
    @XmlAttribute(name = "LatestVersion")
    protected String latestVersion;

    public IdReferenceType(){
    	delegateCombiningAlgorihtm = new IdReferenceTypeDelegateCombiningAlgorithm();
    }
    
    @XmlTransient
    private IdReferenceTypeDelegateCombiningAlgorithm delegateCombiningAlgorihtm = null;
    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the earliestVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEarliestVersion() {
        return earliestVersion;
    }

    /**
     * Sets the value of the earliestVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEarliestVersion(String value) {
        this.earliestVersion = value;
    }

    /**
     * Gets the value of the latestVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLatestVersion() {
        return latestVersion;
    }

    /**
     * Sets the value of the latestVersion property.
     * 
     *     
     */
    public void setLatestVersion(String value) {
        this.latestVersion = value;
    }

    /**
     * Gets the CombiningAlgorithmn of the idReferencetype which delegate to the 
     * delegated evaluatable.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public CombiningAlgorithm getCombiningAlg() {
		return delegateCombiningAlgorihtm;
	}
	 /**
     * IllegalAccess
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public EvaluatableID getId() {
		return new EvaluatableIDImpl(value);
	}
	 /**
     * IllegalAccess
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public TargetType getTarget() {
		throw new IllegalAccessError();
	}

	 /**
     * Sets the DelegateCombiningAlgorithm which can delegate the evaluate method 
     * in the combining algorithm.
     * 
     *     
     */
	public void setDelegateCombiningAlgorihtm(
			IdReferenceTypeDelegateCombiningAlgorithm delegateCombiningAlgorihtm) {
		this.delegateCombiningAlgorihtm = delegateCombiningAlgorihtm;
	}


	 /**
     * IllegalAccess
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public String getEvalutableVersion() {
		throw new IllegalAccessError();
	}	
}