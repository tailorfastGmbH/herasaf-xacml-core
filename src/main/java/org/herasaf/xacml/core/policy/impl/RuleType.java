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
 * Java class for RuleType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RuleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Description" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Target" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Condition" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="RuleId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Effect" use="required" type="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}EffectType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 57, for further information.
 * 
 * @version 1.0
 * @author <i>generated</i>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RuleType", propOrder = { "description", "target", "condition" })
public class RuleType implements Serializable {

	private static final long serialVersionUID = 632768732L;
	@XmlElement(name = "Description")
	protected String description;
	@XmlElement(name = "Target")
	protected TargetType target;
	@XmlElement(name = "Condition")
	protected ConditionType condition;
	@XmlAttribute(name = "RuleId", required = true)
	protected String ruleId;
	@XmlAttribute(name = "Effect", required = true)
	protected EffectType effect;

	/**
	 * Gets the value of the description property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the value of the description property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDescription(String value) {
		this.description = value;
	}

	/**
	 * Gets the value of the target property.
	 * 
	 * @return possible object is {@link TargetType }
	 * 
	 */
	public TargetType getTarget() {
		return target;
	}

	/**
	 * Sets the value of the target property.
	 * 
	 * @param value
	 *            allowed object is {@link TargetType }
	 * 
	 */
	public void setTarget(TargetType value) {
		this.target = value;
	}

	/**
	 * Gets the value of the condition property.
	 * 
	 * @return possible object is {@link ConditionType }
	 * 
	 */
	public ConditionType getCondition() {
		return condition;
	}

	/**
	 * Sets the value of the condition property.
	 * 
	 * @param value
	 *            allowed object is {@link ConditionType }
	 * 
	 */
	public void setCondition(ConditionType value) {
		this.condition = value;
	}

	/**
	 * Gets the value of the ruleId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRuleId() {
		return ruleId;
	}

	/**
	 * Sets the value of the ruleId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRuleId(String value) {
		this.ruleId = value;
	}

	/**
	 * Gets the value of the effect property.
	 * 
	 * @return possible object is {@link EffectType }
	 * 
	 */
	public EffectType getEffect() {
		return effect;
	}

	/**
	 * Sets the value of the effect property.
	 * 
	 * @param value
	 *            allowed object is {@link EffectType }
	 * 
	 */
	public void setEffect(EffectType value) {
		this.effect = value;
	}
}