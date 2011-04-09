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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.converter.PolicyCombiningAlgorithmJAXBTypeAdapter;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;

/**
 * <p>
 * Java class for PolicySetType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;PolicySetType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Description&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}PolicySetDefaults&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Target&quot;/&gt;
 *         &lt;choice maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;&gt;
 *           &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}PolicySet&quot;/&gt;
 *           &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Policy&quot;/&gt;
 *           &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}PolicySetIdReference&quot;/&gt;
 *           &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}PolicyIdReference&quot;/&gt;
 *           &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}CombinerParameters&quot;/&gt;
 *           &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}PolicyCombinerParameters&quot;/&gt;
 *           &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}PolicySetCombinerParameters&quot;/&gt;
 *         &lt;/choice&gt;
 *         &lt;element ref=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Obligations&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;PolicySetId&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}anyURI&quot; /&gt;
 *       &lt;attribute name=&quot;Version&quot; type=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}VersionType&quot; default=&quot;1.0&quot; /&gt;
 *       &lt;attribute name=&quot;PolicyCombiningAlgId&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}anyURI&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 58, for further information.
 * 
 * @author <i>generated</i>
 * @author Stefan Oberholzer
 * @author Patrik Dietschweiler
 * @author Florian Huonder
 */
@XmlRootElement
@XmlType(name = "PolicySetType", propOrder = { "description", "policySetDefaults", "target", "additionalInformation",
		"obligations" })
public class PolicySetType implements Evaluatable, Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Description")
	private String description;
	@XmlElement(name = "PolicySetDefaults")
	private DefaultsType policySetDefaults;
	@XmlElement(name = "Target", required = true)
	private TargetType target;
	@XmlElementRefs({
			@XmlElementRef(name = "PolicyIdReference", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = JAXBElement.class),
			@XmlElementRef(name = "PolicySetIdReference", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = JAXBElement.class),
			@XmlElementRef(name = "PolicySetCombinerParameters", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = JAXBElement.class),
			@XmlElementRef(name = "CombinerParameters", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = JAXBElement.class),
			@XmlElementRef(name = "PolicySet", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = JAXBElement.class),
			@XmlElementRef(name = "PolicyCombinerParameters", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = JAXBElement.class),
			@XmlElementRef(name = "Policy", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = JAXBElement.class) })
	private List<JAXBElement<?>> additionalInformation;
	@XmlElement(name = "Obligations")
	private ObligationsType obligations;
	@XmlAttribute(name = "PolicySetId", required = true)
	@XmlSchemaType(name = "anyURI")
	private String policySetId;
	@XmlAttribute(name = "Version")
	private String version;

	// This field is transient because it is only marshal-/unamrshal-able
	// together with JAXB.
	@XmlAttribute(name = "PolicyCombiningAlgId", required = true)
	@XmlJavaTypeAdapter(PolicyCombiningAlgorithmJAXBTypeAdapter.class)
	@XmlSchemaType(name = "anyURI")
	private PolicyCombiningAlgorithm policyCombiningAlg;

	// True if the PolicySet or a SubPolicy (SubPolicySet) contains any
	// Obligations.
	@XmlTransient
	private boolean hasObligations;

	/**
	 * Initializes and sets (by default) to true that this policy set (or sub
	 * policies or policy sets, respectively) contains obligations. This flag is
	 * intended to be set by an other component (e.g. a pre processor) to
	 * another value.
	 */
	public PolicySetType() {
		// Must by default be true. It cannot be assumed that a preprocessor
		// correctly sets this field.
		setHasObligations(true);
	}

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
	 * Gets the value of the policySetDefaults property.
	 * 
	 * @return possible object is {@link DefaultsType }
	 * 
	 */
	public DefaultsType getPolicySetDefaults() {
		return policySetDefaults;
	}

	/**
	 * Sets the value of the policySetDefaults property.
	 * 
	 * @param value
	 *            allowed object is {@link DefaultsType }
	 * 
	 */
	public void setPolicySetDefaults(DefaultsType value) {
		this.policySetDefaults = value;
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
	 * Objects of the following type(s) are allowed in the list
	 * {@link JAXBElement }{@code <}{@link IdReferenceType }{@code >}
	 * {@link JAXBElement }{@code <}{@link IdReferenceType }{@code >}
	 * {@link JAXBElement }{@code <}{@link PolicySetCombinerParametersType }
	 * {@code >} {@link JAXBElement }{@code <}{@link CombinerParametersType }
	 * {@code >} {@link JAXBElement }{@code <}{@link PolicySetType }{@code >}
	 * {@link JAXBElement }{@code <}{@link PolicyCombinerParametersType }{@code >}
	 * {@link JAXBElement }{@code <}{@link PolicyType }{@code >}
	 * 
	 * 
	 */
	public List<JAXBElement<?>> getAdditionalInformation() {
		if (additionalInformation == null) {
			additionalInformation = new ArrayList<JAXBElement<?>>();
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
	 * Gets the value of the policySetId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPolicySetId() {
		return policySetId;
	}

	/**
	 * Sets the value of the policySetId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPolicySetId(String value) {
		this.policySetId = value;
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

	/**
	 * Gets the value of the policyCombiningAlg property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public PolicyCombiningAlgorithm getCombiningAlg() {
		return policyCombiningAlg;
	}

	/**
	 * Sets the value of the policyCombiningAlg property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCombiningAlg(PolicyCombiningAlgorithm value) {
		this.policyCombiningAlg = value;
	}

	/**
	 * Returns an ordered {@link List} of the containing {@link Evaluatable}s.
	 * This {@link List} may contain null-values in case that a remote reference
	 * was not resolvable.
	 * 
	 * @param evaluationContext
	 *            The {@link EvaluationContext} is provided for setting the
	 *            references.
	 * @return A {@link List} of ordered {@link Evaluatable}s.
	 */
	public List<Evaluatable> getOrderedEvaluatables(EvaluationContext evaluationContext) {
		List<Evaluatable> evals = new ArrayList<Evaluatable>();
		// No foreach iterator to ensure thread safety.
		for (int i = 0; i < getAdditionalInformation().size(); i++) {
			JAXBElement<?> jaxbElem = getAdditionalInformation().get(i);
			evals.add((Evaluatable) jaxbElem.getValue());
		}
		return evals;
	}

	/**
	 * Returns an unordered {@link List} of the containing {@link Evaluatable}s.
	 * This {@link List} may contain null-values in case that a remote reference
	 * was not resolvable.
	 * 
	 * @param evaluationContext
	 *            The {@link EvaluationContext} is provided for setting the
	 *            references.
	 * @return A {@link List} of {@link Evaluatable}s.
	 */
	public List<Evaluatable> getUnorderedEvaluatables(EvaluationContext evaluationContext) {
		return getOrderedEvaluatables(evaluationContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.xacml.core.policy.impl.Evaluatable#getId()
	 */
	public EvaluatableID getId() {
		return new EvaluatableIDImpl(getPolicySetId());
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
	 * Set the field hasObligations to the proper value. True if the current
	 * PolicySet has Obligations or a subpolicy or policyset, respectively.
	 * False otherwise.
	 * 
	 * It is intended that this method is called by e.g. a preprocessor.
	 * 
	 * @param value
	 *            The boolean value to set.
	 */
	public void setHasObligations(boolean value) {
		hasObligations = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasObligations() {
		return hasObligations;
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
