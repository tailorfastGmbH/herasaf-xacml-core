/*
 * Copyright 2008-2010 HERAS-AF (www.herasaf.org)
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.herasaf.xacml.core.combiningAlgorithm.rule.AbstractRuleCombiningAlgorithm;
import org.herasaf.xacml.core.converter.URNToRuleCombiningAlgorithmConverter;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;

/**
 * <p>
 * Java class for PolicyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;PolicyType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Description&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}PolicyDefaults&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Target&quot;/&gt;
 *         &lt;choice maxOccurs=&quot;unbounded&quot;&gt;
 *           &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}CombinerParameters&quot; minOccurs=&quot;0&quot;/&gt;
 *           &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}RuleCombinerParameters&quot; minOccurs=&quot;0&quot;/&gt;
 *           &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}VariableDefinition&quot;/&gt;
 *           &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Rule&quot;/&gt;
 *         &lt;/choice&gt;
 *         &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Obligations&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;PolicyId&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}anyURI&quot; /&gt;
 *       &lt;attribute name=&quot;Version&quot; type=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}VersionType&quot; default=&quot;1.0&quot; /&gt;
 *       &lt;attribute name=&quot;RuleCombiningAlgId&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}anyURI&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 52, for further information.
 * 
 * @version 1.0
 * @author <i>generated</i>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlType(name = "PolicyType", propOrder = { "description", "policyDefaults", "target", "additionalInformation",
		"obligations" })
public class PolicyType implements Evaluatable, Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Description")
	private String description;
	@XmlElement(name = "PolicyDefaults")
	private DefaultsType policyDefaults;
	@XmlElement(name = "Target", required = true)
	private TargetType target;
	@XmlElements({ @XmlElement(name = "Rule", type = RuleType.class),
			@XmlElement(name = "VariableDefinition", type = VariableDefinitionType.class),
			@XmlElement(name = "RuleCombinerParameters", type = RuleCombinerParametersType.class),
			@XmlElement(name = "CombinerParameters", type = CombinerParametersType.class) })
	private List<Object> additionalInformation;
	@XmlElement(name = "Obligations")
	private ObligationsType obligations;
	@XmlAttribute(name = "PolicyId", required = true)
	@XmlSchemaType(name = "anyURI")
	private String policyId;
	@XmlAttribute(name = "Version")
	private String version;
	@XmlAttribute(name = "RuleCombiningAlgId", required = true)
	@XmlJavaTypeAdapter(URNToRuleCombiningAlgorithmConverter.class)
	@XmlSchemaType(name = "anyURI")
	private AbstractRuleCombiningAlgorithm ruleCombiningAlg;

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
	 * Gets the value of the policyDefaults property.
	 * 
	 * @return possible object is {@link DefaultsType }
	 * 
	 */
	public DefaultsType getPolicyDefaults() {
		return policyDefaults;
	}

	/**
	 * Sets the value of the policyDefaults property.
	 * 
	 * @param value
	 *            allowed object is {@link DefaultsType }
	 * 
	 */
	public void setPolicyDefaults(DefaultsType value) {
		this.policyDefaults = value;
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
	 * Gets the value of the additionalInformation property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the additionalInformation property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getAdditionalInformation().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link RuleType }
	 * {@link VariableDefinitionType } {@link RuleCombinerParametersType }
	 * {@link CombinerParametersType }
	 * 
	 * 
	 */
	public List<Object> getAdditionalInformation() {
		if (additionalInformation == null) {
			additionalInformation = new ArrayList<Object>();
		}
		return this.additionalInformation;
	}

	/**
	 * Gets the value of the obligations property.
	 * 
	 * @return possible object is {@link ObligationsType }
	 * 
	 */
	public ObligationsType getObligations() {
		return obligations;
	}

	/**
	 * Sets the value of the obligations property.
	 * 
	 * @param value
	 *            allowed object is {@link ObligationsType }
	 * 
	 */
	public void setObligations(ObligationsType value) {
		this.obligations = value;
	}

	/**
	 * Gets the value of the policyId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPolicyId() {
		return policyId;
	}

	/**
	 * Sets the value of the policyId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPolicyId(String value) {
		this.policyId = value;
	}

	/**
	 * Gets the value of the version property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVersion() {
		if (version == null) {
			return "1.0";
		}
		return version;
	}

	/**
	 * Sets the value of the version property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVersion(String value) {
		this.version = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.xacml.core.policy.impl.Evaluatable#getCombiningAlg()
	 */
	public AbstractRuleCombiningAlgorithm getCombiningAlg() {
		return ruleCombiningAlg;
	}

	/**
	 * Sets the value of the ruleCombiningAlg property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setCombiningAlg(AbstractRuleCombiningAlgorithm value) {
		this.ruleCombiningAlg = value;
	}

	/**
	 * Returns an ordered {@link List} of the containing {@link RuleType}s.
	 * 
	 * @return A {@link List} of ordered {@link RuleType}s.
	 */
	public List<RuleType> getOrderedRules() {
		List<RuleType> rules = new ArrayList<RuleType>();
		for (int i = 0; i < getAdditionalInformation().size(); i++) {
			Object obj = getAdditionalInformation().get(i);
			if (obj instanceof RuleType) {
				rules.add((RuleType) obj);
			}
		}
		return rules;
	}

	/**
	 * Returns an unordered {@link List} of the containing {@link RuleType}s.
	 * 
	 * @return A {@link List} of unordered {@link RuleType}s.
	 */
	public List<RuleType> getUnorderedRules() {
		return getOrderedRules();
	}

	/**
	 * Returns a {@link Map} containing the {@link VariableDefinitionType}s with
	 * their Id as key.
	 * 
	 * @return The {@link Map} contianing the {@link VariableDefinitionType}s.
	 */
	public Map<String, Variable> getVariables() {
		Map<String, Variable> variables = new HashMap<String, Variable>();
		for (Object obj : getAdditionalInformation()) {
			if (obj instanceof VariableDefinitionType) {
				VariableDefinitionType variable = (VariableDefinitionType) obj;
				variables.put(variable.getVariableId(), variable);
			}
		}
		return variables;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.xacml.core.policy.impl.Evaluatable#getId()
	 */
	public EvaluatableID getId() {
		return new EvaluatableIDImpl(getPolicyId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.xacml.core.policy.impl.Evaluatable#getId()
	 */
	public String getEvalutableVersion() {
		return getVersion();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasObligations() {
		if (obligations == null) {
			return false;
		}
		return obligations.getObligations().size() > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ObligationType> getContainedObligations(EffectType effect) {
		List<ObligationType> result = new ArrayList<ObligationType>();
		if (obligations != null) {
			List<ObligationType> oblis = obligations.getObligations();
			for (int i = 0; i < oblis.size(); i++) {
				ObligationType obli = oblis.get(i);
				if (obli.getFulfillOn() == effect) {
					result.add(obli);
				}
			}
		}
		return result;
	}
}