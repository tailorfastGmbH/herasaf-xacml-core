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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for RuleCombinerParametersType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RuleCombinerParametersType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}CombinerParametersType">
 *       &lt;attribute name="RuleIdRef" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 59, for further information.
 * 
 * @author <i>generated</i>
 */
@XmlRootElement(name = "RuleCombinerParameters", namespace="urn:oasis:names:tc:xacml:2.0:policy:schema:os")
@XmlType(name = "RuleCombinerParametersType")
public class RuleCombinerParametersType extends CombinerParametersType {
	private static final long serialVersionUID = 1L;
	@XmlAttribute(name = "RuleIdRef", required = true)
	private String ruleIdRef;

	/**
	 * Gets the value of the ruleIdRef property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRuleIdRef() {
		return ruleIdRef;
	}

	/**
	 * Sets the value of the ruleIdRef property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRuleIdRef(String value) {
		this.ruleIdRef = value;
	}
}