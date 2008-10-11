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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

import org.herasaf.xacml.SyntaxException;
import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;

/**
 * <p>Java class for VariableDefinitionType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="VariableDefinitionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Expression"/>
 *       &lt;/sequence>
 *       &lt;attribute name="VariableId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 * See:	<a href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June 2006</a> page 58, for further information.
 *
 * @version 1.0
 * @author <i>generated</i>
 * @author Sacha Dolski
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VariableDefinitionType", propOrder = {
    "expression"
})
public class VariableDefinitionType implements Serializable, Variable
{

    private final static long serialVersionUID = 632768732L;
    @XmlElementRef(name = "Expression", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = JAXBElement.class)
    protected JAXBElement<? extends Serializable> expression;
    @XmlAttribute(name = "VariableId", required = true)
    protected String variableId;

    /**
     * Gets the value of the expression property.
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AttributeSelectorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link FunctionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ApplyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SubjectAttributeDesignatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link EnvironmentAttributeDesignatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ResourceAttributeDesignatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link VariableReferenceType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AttributeValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ActionAttributeDesignatorType }{@code >}
     */
    public JAXBElement<? extends Serializable> getExpression() {
        return expression;
    }

    /**
     * Sets the value of the expression property.
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AttributeSelectorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link FunctionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ApplyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SubjectAttributeDesignatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link EnvironmentAttributeDesignatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ResourceAttributeDesignatorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link VariableReferenceType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AttributeValueType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ActionAttributeDesignatorType }{@code >}
     *
     */
    public void setExpression(JAXBElement<? extends Serializable> value) {
        this.expression = value;
    }

    /**
     * Gets the value of the variableId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     */
    public String getVariableId() {
        return variableId;
    }

    /**
     * Sets the value of the variableId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     */
    public void setVariableId(String value) {
        this.variableId = value;
    }

    /*
     * (non-Javadoc)
     * @see org.herasaf.xacml.core.policy.impl.Variable#getValue(org.herasaf.xacml.core.context.impl.RequestType, java.util.Map)
     */
	public Object getValue(RequestType request,
			RequestInformation reqInfo)
			throws MissingAttributeException, SyntaxException,
			ProcessingException {
		return ((ExpressionType)getExpression().getValue()).handle(request, reqInfo);
	}
}