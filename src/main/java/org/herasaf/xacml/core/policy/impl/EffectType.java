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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for EffectType.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="EffectType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Permit"/>
 *     &lt;enumeration value="Deny"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 62, for further information.
 * 
 * @version 1.0
 * @author <i>generated</i>
 * @author Florian Huonder
 */
@XmlRootElement(name = "Effect", namespace="urn:oasis:names:tc:xacml:2.0:policy:schema:os")
@XmlType(name = "EffectType")
@XmlEnum
public enum EffectType {

	@XmlEnumValue("Permit")
	PERMIT("PERMIT"), @XmlEnumValue("Deny")
	DENY("DENY");
	private final String value;

	EffectType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	/**
	 * The effect type is an enumeration. This method is needed to find the
	 * Enum-Value from its String-representation.
	 * 
	 * @param value
	 *            String representation of the enumeration.
	 * @return The {@link EffectType} of the string representation
	 */
	public static EffectType fromValue(String value) {
		for (int i = 0; i < EffectType.values().length; i++) {
			EffectType c = EffectType.values()[i];
			if (c.value.equalsIgnoreCase(value)) {
				return c;
			}
		}
		throw new IllegalArgumentException(value);
	}
}