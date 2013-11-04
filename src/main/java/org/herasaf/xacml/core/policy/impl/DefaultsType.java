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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for DefaultsType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="DefaultsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}XPathVersion"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> pages 47 (PolicySetDefaults) and page 58 (PolicyDefaults), for
 * further information.
 * 
 * @author <i>generated</i>
 */
@XmlRootElement(name = "Defaults", namespace="urn:oasis:names:tc:xacml:2.0:policy:schema:os")
@XmlType(name = "DefaultsType", propOrder = { "xPathVersion" })
public class DefaultsType implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "XPathVersion")
	@XmlSchemaType(name = "anyURI")
	private String xPathVersion;

	/**
	 * Gets the value of the xPathVersion property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getXPathVersion() {
		return xPathVersion;
	}

	/**
	 * Sets the value of the xPathVersion property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setXPathVersion(String value) {
		this.xPathVersion = value;
	}
}