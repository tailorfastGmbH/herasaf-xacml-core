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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.converter.URNToDataTypeConverter;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.herasaf.xacml.core.policy.ExpressionProcessingException;

/**
 * <p>
 * Java class for AttributeValueType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;AttributeValueType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}ExpressionType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;any/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;DataType&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}anyURI&quot; /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 65, for further information.
 * 
 * @version 1.0
 * @author <i>generated</i>
 * @author Sacha Dolski
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttributeValueType", propOrder = { "content" })
@XmlSeeAlso( { AttributeAssignmentType.class })
public class AttributeValueType extends ExpressionType {

	private static final long serialVersionUID = 632768732L;
	@XmlMixed
	@XmlAnyElement(lax = true)
	private List<Object> content;
	@XmlAttribute(name = "DataType", required = true)
	@XmlJavaTypeAdapter(URNToDataTypeConverter.class)
	@XmlSchemaType(name = "anyURI")
	private DataTypeAttribute<?> dataType;
	@XmlAnyAttribute
	private Map<QName, String> otherAttributes;

	/**
	 * TODO REVIEW René.
	 * 
	 * Creates a new attribute value type and initializes a {@link Map} that can
	 * contain any attribute value.
	 */
	public AttributeValueType() {
		otherAttributes = new HashMap<QName, String>();
	}

	/**
	 * Gets the value of the content property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the content property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getContent().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * org.w3c.dom.Element, {@link String } {@link Object }
	 * 
	 * 
	 */
	public List<Object> getContent() {
		if (content == null) {
			content = new ArrayList<Object>();
		}
		return this.content;
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
	 * Gets a map that contains attributes that aren't bound to any typed
	 * property on this class.
	 * 
	 * <p>
	 * the map is keyed by the name of the attribute and the value is the string
	 * value of the attribute.
	 * 
	 * the map returned by this method is live, and you can add new attribute by
	 * updating the map directly. Because of this design, there's no setter.
	 * 
	 * 
	 * @return always non-null
	 */
	public Map<QName, String> getOtherAttributes() {
		return otherAttributes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.core.policy.impl.ExpressionType#handle(org.herasaf.core.context
	 * .impl.RequestType, java.util.Map)
	 */
	@Override
	public Object handle(RequestType request, RequestInformation reqInfo) throws ExpressionProcessingException,
			SyntaxException {
		if (content.size() > 1) {
			throw new ExpressionProcessingException("The content of the AttributeValueType can't be greater than 1");
		}
		try {
			return dataType.convertTo((String) content.get(0));
		} catch (ClassCastException e) {
			throw new ExpressionProcessingException(e);
		}
	}
}