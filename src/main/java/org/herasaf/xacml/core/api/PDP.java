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

package org.herasaf.xacml.core.api;

import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.ResponseCtx;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;

/**
 * TODO JAVADOC!!!!!!!!
 * 
 * The {@link PDP} is used to evaluate a request provided by the context
 * handler. The context handler sends the request to the {@link PDP}. The
 * {@link PDP} evaluates the {@link RequestCtx} and returns a
 * {@link ResponseCtx}.<br>
 * See: <a href=
 * "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
 * OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
 * 2006</a> for further information. <br>
 * <br>
 * A Policy Decision Point can handle only one resource per {@link RequestType}.
 * The context handler is responsible for splitting up a {@link RequestType}
 * that contains multiple resources into many {@link RequestType}s. <br>
 * See: <a href="See: <a href="http://docs.oasis-open.org/xacml/2.0/access_control-xacml-2.0-mult-profile-spec-os.pdf"
 * >Multiple resource profile of XACML v2.0, 1 February 2005</a> for further
 * information.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 * @version 1.0
 */
public interface PDP {
	/**
	 * 
	 * TODO JAVADOC 
	 * 
	 * Evaluates a {@link RequestCtx} with the {@link Evaluatable}s stored in
	 * the {@link PDP}.
	 * 
	 * @param request
	 *            The {@link RequestCtx} to evaluate.
	 * @return The {@link ResponseCtx} with the result of the evaluation.
	 */
	public ResponseCtx evaluate(RequestCtx request);

	/**
	 * TODO JAVDOC
	 * @return The {@link PolicyRepository} attached to this {@link PDP}.
	 */
	public PolicyRepository getPolicyRepository();

	/**
	 *  TODO JAVDOC
	 * @param policyRepository
	 */
	public void setPolicyRepository(PolicyRepository policyRepository);
}