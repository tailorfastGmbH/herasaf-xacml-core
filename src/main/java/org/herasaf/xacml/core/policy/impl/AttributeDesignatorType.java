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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.converter.DataTypeJAXBTypeAdapter;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.herasaf.xacml.core.policy.ExpressionProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;

/**
 * <p>
 * Java class for AttributeDesignatorType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;AttributeDesignatorType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}ExpressionType&quot;&gt;
 *       &lt;attribute name=&quot;AttributeId&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}anyURI&quot; /&gt;
 *       &lt;attribute name=&quot;DataType&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}anyURI&quot; /&gt;
 *       &lt;attribute name=&quot;Issuer&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;MustBePresent&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; default=&quot;false&quot; /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 65, for further information.
 * 
 * @author <i>generated</i>
 * @author Sacha Dolski
 */
@XmlRootElement(name = "AttributeDesignator", namespace="urn:oasis:names:tc:xacml:2.0:policy:schema:os")
@XmlType(name = "AttributeDesignatorType")
@XmlSeeAlso({ SubjectAttributeDesignatorType.class, ResourceAttributeDesignatorType.class,
		EnvironmentAttributeDesignatorType.class, ActionAttributeDesignatorType.class })
public abstract class AttributeDesignatorType extends ExpressionType {
	private static final long serialVersionUID = 1L;
	@XmlAttribute(name = "AttributeId", required = true)
	@XmlSchemaType(name = "anyURI")
	private String attributeId;
	@XmlAttribute(name = "DataType", required = true)
	@XmlJavaTypeAdapter(DataTypeJAXBTypeAdapter.class)
	@XmlSchemaType(name = "anyURI")
	private DataTypeAttribute<?> dataType;
	@XmlAttribute(name = "Issuer")
	private String issuer;
	@XmlAttribute(name = "MustBePresent")
	private Boolean mustBePresent;

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
	public abstract Object handle(RequestType request, EvaluationContext evaluationContext)
			throws ExpressionProcessingException, MissingAttributeException, SyntaxException;

	protected List<Object> handle(List<AttributeType> requestAttributes) throws ExpressionProcessingException,
			MissingAttributeException, SyntaxException {
		List<Object> returnValues = new ArrayList<>();

		for (AttributeType attr : requestAttributes) {
			validateAttributeType(attr);
			if (getAttributeId().equals(attr.getAttributeId())
					&& getDataType().toString().equals(attr.getDataType().toString())) {
				if (getIssuer() != null) {
					if (getIssuer().equals(attr.getIssuer())) {
						addAndConvertAttrValue(returnValues, attr.getAttributeValues());
					}
				} else {
					addAndConvertAttrValue(returnValues, attr.getAttributeValues());
				}
			}
		}
		return returnValues;
	}

	
	/**
	 * Receives an AttributeType and a list. Converts containing
	 * AttributeValueTypes of the AttributeType in the specified dataType and
	 * adds it to the list returnValues.
	 * 
	 * @param returnValues
	 * @param attrValues
	 * @throws ExpressionProcessingException
	 * @throws SyntaxException
	 */
	protected void addAndConvertAttrValue(List<Object> returnValues, List<AttributeValueType> attrValues)
			throws ExpressionProcessingException, SyntaxException {
		for (AttributeValueType attrVal : attrValues) {
			if (attrVal.getContent().size() > 1) {
				throw new ExpressionProcessingException("The content of the AttributeValueType can't be greater than 1");
			}
			try {
				returnValues.add(dataType.convertTo(attrVal.getContent()));
			} catch (ClassCastException e) {
				throw new SyntaxException(e);
			}
		}
	}

	protected void validateAttributeDesignator() throws SyntaxException {
		if (getAttributeId() == null) {
			String message = "Attribute AttributeId of AttributeDesignator must be present.";
			throw new SyntaxException(message);
		}
		if (getDataType() == null) {
			String message = "Attribute DataType of AttributeDesignator must be present.";
			throw new SyntaxException(message);
		}
	}

	private void validateAttributeType(AttributeType requestAttributeType) throws SyntaxException {
		if (requestAttributeType.getAttributeId() == null) {
			String message = "Attribute AttributeId of AttributeType must be present.";
			throw new SyntaxException(message);
		}
		if (requestAttributeType.getDataType() == null) {
			String message = "Attribute DataType of AttributeType must be present.";
			throw new SyntaxException(message);
		}
	}
}