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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for TargetType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="TargetType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Subjects" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Resources" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Actions" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Environments" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 45, for further information.
 * 
 * @version 1.0
 * @author <i>generated</i>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetType", propOrder = { "subjects", "resources", "actions", "environments" })
public class TargetType implements Serializable {

	private static final long serialVersionUID = 632768732L;
	@XmlElement(name = "Subjects")
	private SubjectsType subjects;
	@XmlElement(name = "Resources")
	private ResourcesType resources;
	@XmlElement(name = "Actions")
	private ActionsType actions;
	@XmlElement(name = "Environments")
	private EnvironmentsType environments;

	/**
	 * Gets the value of the subjects property.
	 * 
	 * @return possible object is {@link SubjectsType }
	 * 
	 */
	public SubjectsType getSubjects() {
		return subjects;
	}

	/**
	 * Sets the value of the subjects property.
	 * 
	 * @param value
	 *            allowed object is {@link SubjectsType }
	 * 
	 */
	public void setSubjects(SubjectsType value) {
		this.subjects = value;
	}

	/**
	 * Gets the value of the resources property.
	 * 
	 * @return possible object is {@link ResourcesType }
	 * 
	 */
	public ResourcesType getResources() {
		return resources;
	}

	/**
	 * Sets the value of the resources property.
	 * 
	 * @param value
	 *            allowed object is {@link ResourcesType }
	 * 
	 */
	public void setResources(ResourcesType value) {
		this.resources = value;
	}

	/**
	 * Gets the value of the actions property.
	 * 
	 * @return possible object is {@link ActionsType }
	 * 
	 */
	public ActionsType getActions() {
		return actions;
	}

	/**
	 * Sets the value of the actions property.
	 * 
	 * @param value
	 *            allowed object is {@link ActionsType }
	 * 
	 */
	public void setActions(ActionsType value) {
		this.actions = value;
	}

	/**
	 * Gets the value of the environments property.
	 * 
	 * @return possible object is {@link EnvironmentsType }
	 * 
	 */
	public EnvironmentsType getEnvironments() {
		return environments;
	}

	/**
	 * Sets the value of the environments property.
	 * 
	 * @param value
	 *            allowed object is {@link EnvironmentsType }
	 * 
	 */
	public void setEnvironments(EnvironmentsType value) {
		this.environments = value;
	}
}