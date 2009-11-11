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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.converter.URNToFunctionConverter;
import org.herasaf.xacml.core.function.Function;


/**
 * <p>Java class for FunctionType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="FunctionType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}ExpressionType">
 *       &lt;attribute name="FunctionId" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 * See:	<a href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June 2006</a> page 60, for further information.
 *
 * @version 1.0
 * @author <i>generated</i>
 * @author Sacha Dolski
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FunctionType")
public class FunctionType
    extends ExpressionType {

    private final static long serialVersionUID = 632768732L;
    @XmlAttribute(name = "FunctionId", required = true)
    @XmlJavaTypeAdapter(URNToFunctionConverter.class)
    @XmlSchemaType(name = "anyURI")
    protected Function function;

    /**
     * Gets the value of the function property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public Function getFunction() {
        return function;
    }

    /**
     * Sets the value of the function property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFunction(Function value) {
        this.function = value;
    }

    /*
     * (non-Javadoc)
     * @see org.herasaf.xacml.core.policy.impl.ExpressionType#handle(org.herasaf.xacml.core.context.impl.RequestType, java.util.Map)
     */
	@Override
	public Object handle(RequestType request,
			RequestInformation reqInfo) {
		return function;
	}
}