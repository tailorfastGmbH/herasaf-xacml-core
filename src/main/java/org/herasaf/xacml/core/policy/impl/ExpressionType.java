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

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;

/**
 * <p>
 * Java class for ExpressionType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ExpressionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 63, for further information.
 * 
 * @author <i>generated</i>
 * @author Sacha Dolski
 */
@XmlRootElement(name = "Expression", namespace="urn:oasis:names:tc:xacml:2.0:policy:schema:os")
@XmlType(name = "ExpressionType")
@XmlSeeAlso({ AttributeSelectorType.class, ApplyType.class, FunctionType.class, VariableReferenceType.class,
		AttributeDesignatorType.class })
public abstract class ExpressionType implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Executes the expression.
	 * 
	 * @param request
	 * @param evaluationContext
	 * @return The result of the expression.
	 * @throws ProcessingException
	 * @throws MissingAttributeException
	 * @throws SyntaxException
	 */
	public abstract Object handle(RequestType request, EvaluationContext evaluationContext) throws ProcessingException,
			MissingAttributeException, SyntaxException;
}