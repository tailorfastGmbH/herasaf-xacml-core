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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.5-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.10.03 at 07:56:30 AM CEST 
//

package org.herasaf.xacml.core.context.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.herasaf.xacml.core.dataTypeAttribute.impl.DateDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.DateTimeDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.TimeDataTypeAttribute;

/**
 * <p>
 * Java class for RequestType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:context:schema:os}Subject" maxOccurs="unbounded"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:context:schema:os}Resource" maxOccurs="unbounded"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:context:schema:os}Action"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:context:schema:os}Environment"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 67, for further information.
 * 
 * @version 1.0
 * @author <i>generated</i>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestType", propOrder = { "subjects", "resources", "action", "environment" })
public class RequestType implements Serializable {

	private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static final String DATE_PATTERN = "yyyy-MM-ddZ";
	private static final String TIME_PATTERN = "HH:mm:ss.SSSZ";
	private static final String CURRENT_DATETIME_DATATYPEID = "urn:oasis:names:tc:xacml:1.0:environment:current-dateTime";
	private static final String CURRENT_DATE_DATATYPEID = "urn:oasis:names:tc:xacml:1.0:environment:current-date";
	private static final String CURRENT_TIME_DATATYPEID = "urn:oasis:names:tc:xacml:1.0:environment:current-time";

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Subject", required = true)
	private List<SubjectType> subjects;
	@XmlElement(name = "Resource", required = true)
	private List<ResourceType> resources;
	@XmlElement(name = "Action", required = true)
	private ActionType action;
	@XmlElement(name = "Environment", required = true)
	private EnvironmentType environment;

	/**
	 * Sets the following attributes, if not already contained, into the
	 * request:
	 * <ul>
	 * <li>urn:oasis:names:tc:xacml:1.0:environment:current-dateTime</li>
	 * <li>urn:oasis:names:tc:xacml:1.0:environment:current-date</li>
	 * <li>urn:oasis:names:tc:xacml:1.0:environment:current-time</li>
	 * </ul>
	 * This behavior is required by the XACML 2.0 specification. See: <a href=
	 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20"
	 * > OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29
	 * June 2006</a> appendix B.8. Environment attributes, for further
	 * information.
	 * 
	 * @param marshaller
	 *            The {@link Marshaller} with which this request will be
	 *            marshalled.
	 */
	public void beforeMarshal(Marshaller marshaller) {
		if (!envContainsAttributeId(CURRENT_TIME_DATATYPEID)) {
			environment.getAttributes().add(createCurrentTime());
		}
		if (!envContainsAttributeId(CURRENT_DATE_DATATYPEID)) {
			environment.getAttributes().add(createCurrentDate());
		}
		if (!envContainsAttributeId(CURRENT_DATETIME_DATATYPEID)) {
			environment.getAttributes().add(createCurrentDateTime());
		}
	}

	/**
	 * Creates a new {@link AttributeType} containing the current time.
	 * 
	 * @return The {@link AttributeType} containing the current time.
	 */
	private AttributeType createCurrentTime() {
		AttributeType currentTimeAttr = new AttributeType();
		currentTimeAttr.setAttributeId(CURRENT_TIME_DATATYPEID);
		currentTimeAttr.setDataType(new TimeDataTypeAttribute());
		currentTimeAttr.getAttributeValues().add(createDateTime(TIME_PATTERN));

		return currentTimeAttr;
	}

	/**
	 * Creates a new {@link AttributeType} containing the current date.
	 * 
	 * @return The {@link AttributeType} containing the current date.
	 */
	private AttributeType createCurrentDate() {
		AttributeType currentDateAttr = new AttributeType();
		currentDateAttr.setAttributeId(CURRENT_DATE_DATATYPEID);
		currentDateAttr.setDataType(new DateDataTypeAttribute());
		currentDateAttr.getAttributeValues().add(createDateTime(DATE_PATTERN));

		return currentDateAttr;
	}

	/**
	 * Creates a new {@link AttributeType} containing the current dateTime.
	 * 
	 * @return The {@link AttributeType} containing the current dateTime.
	 */
	private AttributeType createCurrentDateTime() {
		AttributeType currentDateTimeAttr = new AttributeType();
		currentDateTimeAttr.setAttributeId(CURRENT_DATETIME_DATATYPEID);
		currentDateTimeAttr.setDataType(new DateTimeDataTypeAttribute());
		currentDateTimeAttr.getAttributeValues().add(createDateTime(DATE_TIME_PATTERN));

		return currentDateTimeAttr;
	}

	/**
	 * FIXME Timezone awareness (see HERASAFXACMLCORE-28).
	 * 
	 * Creates a new {@link AttributeValueType} containing the current dateTime
	 * with the given pattern.
	 * 
	 * @return The current dateTime.
	 */
	private AttributeValueType createDateTime(String pattern) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		AttributeValueType attrValue = new AttributeValueType();
		String value = sdf.format(cal.getTime());
		value = value.substring(0, value.length() - 2) + ":" + value.substring(value.length() - 2, value.length());
		attrValue.getContent().add(value);

		return attrValue;
	}

	/**
	 * Checks if the environment already contains an element with ID id. It
	 * returns true if such an element is already in the environment, false
	 * otherwise.
	 * 
	 * @return true if an element with ID id is already in the environment,
	 *         false otherwise.
	 */
	private boolean envContainsAttributeId(String id) {
		for (AttributeType attr : environment.getAttributes()) {
			if (attr.getAttributeId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the value of the subjects property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the subjects property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getSubjects().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link SubjectType }
	 * 
	 * 
	 */
	public List<SubjectType> getSubjects() {
		if (subjects == null) {
			subjects = new ArrayList<SubjectType>();
		}
		return this.subjects;
	}

	/**
	 * Gets the value of the resources property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the resources property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getResources().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link ResourceType }
	 * 
	 * 
	 */
	public List<ResourceType> getResources() {
		if (resources == null) {
			resources = new ArrayList<ResourceType>();
		}
		return this.resources;
	}

	/**
	 * Gets the value of the action property.
	 * 
	 * @return possible object is {@link ActionType }
	 * 
	 */
	public ActionType getAction() {
		return action;
	}

	/**
	 * Sets the value of the action property.
	 * 
	 * @param value
	 *            allowed object is {@link ActionType }
	 * 
	 */
	public void setAction(ActionType value) {
		this.action = value;
	}

	/**
	 * Gets the value of the environment property.
	 * 
	 * @return possible object is {@link EnvironmentType }
	 * 
	 */
	public EnvironmentType getEnvironment() {
		return environment;
	}

	/**
	 * Sets the value of the environment property.
	 * 
	 * @param value
	 *            allowed object is {@link EnvironmentType }
	 * 
	 */
	public void setEnvironment(EnvironmentType value) {
		this.environment = value;
	}
}