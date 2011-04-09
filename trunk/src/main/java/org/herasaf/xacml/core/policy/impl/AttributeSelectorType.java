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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.impl.RequestType;

/**
 * <p>
 * Java class for AttributeSelectorType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="AttributeSelectorType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}ExpressionType">
 *       &lt;attribute name="RequestContextPath" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="DataType" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="MustBePresent" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 64, for further information.
 * 
 * @author <i>generated</i>
 * @author Sacha Dolski
 */
@XmlRootElement
@XmlType(name = "AttributeSelectorType")
public class AttributeSelectorType extends ExpressionType {

	private static final long serialVersionUID = 1L;
	@XmlAttribute(name = "RequestContextPath", required = true)
	private String requestContextPath;
	@XmlAttribute(name = "DataType", required = true)
	@XmlSchemaType(name = "anyURI")
	private String dataType;
	@XmlAttribute(name = "MustBePresent")
	private Boolean mustBePresent;

	/**
	 * Gets the value of the requestContextPath property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRequestContextPath() {
		return requestContextPath;
	}

	/**
	 * Sets the value of the requestContextPath property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRequestContextPath(String value) {
		this.requestContextPath = value;
	}

	/**
	 * Gets the value of the dataType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * Sets the value of the dataType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDataType(String value) {
		this.dataType = value;
	}

	/**
	 * Gets the value of the mustBePresent property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public boolean isMustBePresent() {
		if (mustBePresent == null) {
			return false;
		}
		return mustBePresent;
	}

	/**
	 * Sets the value of the mustBePresent property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setMustBePresent(Boolean value) {
		this.mustBePresent = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.xacml.core.policy.impl.ExpressionType#handle(org.herasaf.
	 * xacml.core.context.impl.RequestType, java.util.Map)
	 */
	@Override
	public Object handle(RequestType request, EvaluationContext evaluationContext) {
		return null;
	}
}