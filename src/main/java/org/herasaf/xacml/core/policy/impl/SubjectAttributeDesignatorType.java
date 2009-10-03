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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.SubjectType;
import org.herasaf.xacml.core.policy.ExpressionProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;

/**
 * <p>
 * Java class for SubjectAttributeDesignatorType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;SubjectAttributeDesignatorType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{urn:oasis:names:tc:xacml:2.0:policy:schema:os}AttributeDesignatorType&quot;&gt;
 *       &lt;attribute name=&quot;SubjectCategory&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}anyURI&quot; default=&quot;urn:oasis:names:tc:xacml:1.0:subject-category:access-subject&quot; /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> page 62, for further information.
 * 
 * @version 1.0
 * @author <i>generated</i>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubjectAttributeDesignatorType")
public class SubjectAttributeDesignatorType extends AttributeDesignatorType
		implements Serializable {

	private final static long serialVersionUID = 632768732L;
	@XmlAttribute(name = "SubjectCategory")
	@XmlSchemaType(name = "anyURI")
	protected String subjectCategory;

	/**
	 * Gets the value of the subjectCategory property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSubjectCategory() {
		if (subjectCategory == null) {
			return "urn:oasis:names:tc:xacml:1.0:subject-category:access-subject";
		}
		return subjectCategory;
	}

	/**
	 * Sets the value of the subjectCategory property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSubjectCategory(String value) {
		this.subjectCategory = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.core.policy.impl.AttributeDesignatorType#handle(org.herasaf
	 * .core.context.impl.RequestType, java.util.Map)
	 */
	@Override
	public Object handle(RequestType request, RequestInformation reqInfo)
			throws ExpressionProcessingException, MissingAttributeException,
			SyntaxException {
		List<Object> returnValues = new ArrayList<Object>();

		// A RequestType is not thread safe, because of this you can iterate
		// over it.
		List<SubjectType> subjects = request.getSubjects();

		for (SubjectType sub : subjects) {
			if (sub.getSubjectCategory().equals(getSubjectCategory())) {
				for (AttributeType attr : sub.getAttributes()) {
					if (attributeId.equals(attr.getAttributeId())
							&& dataType.toString().equals(
									attr.getDataType().toString())) {
						if (issuer != null) {
							if (issuer.equals(attr.getIssuer())) {
								addAndConvertAttrValue(returnValues, attr
										.getAttributeValues());
							}
						} else {
							addAndConvertAttrValue(returnValues, attr
									.getAttributeValues());
						}
					}
				}
			}
		}
		/*
		 * If no Attribute could be found, the attribute has to be requested
		 * from a Policy Information Point.
		 * 
		 * See: the OASIS eXtensible Access Control Markup Langugage (XACML)
		 * 2.0, Errata 29 June 2006 page 78, chapter Attribute Retrieval, for
		 * further information.
		 */
		if (returnValues.size() == 0 && reqInfo.getPIP() != null) {
			List<AttributeValueType> attrValues = reqInfo.getPIP()
					.requestSubjectAttributes(request, attributeId,
							dataType.toString(), issuer, subjectCategory);
			addAndConvertAttrValue(returnValues, attrValues);
		}
		if (returnValues.size() == 0 && isMustBePresent()) {
			throw new MissingAttributeException(attributeId, dataType, issuer);
		}
		return returnValues;
	}
}