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

import org.herasaf.xacml.core.function.Function;

/**
 * Supertype of the various Match-Types (SubjectMatch, ResourceMatch,
 * ActionMatch, EnvironmentMatch).
 * 
 * This part references the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a>, pages 46, 48, 49 and 50.
 * 
 * @author Florian Huonder
 * @version 1.0
 */
public interface Match {
	/**
	 * Returns the {@link AttributeValueType} of the **Match.
	 * 
	 * @return The {@link AttributeValueType}.
	 */
	AttributeValueType getAttributeValue();

	/**
	 * Returns the {@link AttributeDesignatorType} of the **Match.
	 * 
	 * @return The {@link AttributeDesignatorType}.
	 */
	AttributeDesignatorType getAttributeDesignator();

	/**
	 * Returns the {@link AttributeSelectorType} of the **Match.
	 * 
	 * @return The {@link AttributeSelectorType}.
	 */
	AttributeSelectorType getAttributeSelector();

	/**
	 * Returns the match-{@link Function} of the **Match.
	 * 
	 * @return The match-{@link Function}.
	 */
	Function getMatchFunction();
}