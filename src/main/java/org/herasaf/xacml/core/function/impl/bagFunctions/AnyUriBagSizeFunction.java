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

package org.herasaf.xacml.core.function.impl.bagFunctions;

import java.net.URI;

/**
 * <p>
 * The implementation of the
 * urn:oasis:names:tc:xacml:1.0:function:anyURI-bag-size function.
 * </p>
 * <p>
 * See: Apendix A.3 of the <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata, 29 January 2008</a> page 121, for further information.
 * </p>
 * 
 * @author Stefan Oberholzer
 * @version 1.0
 */
public class AnyUriBagSizeFunction extends AbstractBagSizeFunction<URI> {

	/** XACML function ID. */
	public static final String ID = "urn:oasis:names:tc:xacml:1.0:function:anyURI-bag-size";

	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFunctionId() {
		return ID;
	}

}
