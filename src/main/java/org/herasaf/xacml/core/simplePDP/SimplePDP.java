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

package org.herasaf.xacml.core.simplePDP;

import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.PIP;
import org.herasaf.xacml.core.api.PolicyRepository;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyUnorderedCombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.ResponseCtx;
import org.herasaf.xacml.core.context.ResponseCtxFactory;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO JAVADOC!!!!!!!!!! The implementation of the {@link PDP}. <br>
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 * @version 1.0
 */
public class SimplePDP implements PDP {
	private PolicyRepository policyRepository;
	private PIP pip; // TODO introduce PIP
	private PolicyUnorderedCombiningAlgorithm policyCombiningAlgorithm;
	private final Logger logger = LoggerFactory.getLogger(SimplePDP.class);
	
	public SimplePDP() {
	}

	/**
	 * TODO JAVADOC
	 * @param rootCombiningAlgorithm
	 * @param policyRepository
	 */
	public SimplePDP(PolicyUnorderedCombiningAlgorithm rootCombiningAlgorithm, PolicyRepository policyRepository) {
		this.policyCombiningAlgorithm = rootCombiningAlgorithm;
		this.policyRepository = policyRepository;
	}
	
	/**
	 * TODO JAVADOC
	 */
	public PolicyRepository getPolicyRepository() {
		return policyRepository;
	}

	/**
	 * TODO JAVADOC
	 */
	public void setPolicyRepository(PolicyRepository policyRepository) {
		this.policyRepository = policyRepository;
	}

	/**
	 * TODO JAVADOC
	 * @param attributeFinder
	 */
	public void setPIP(PIP pip) {
		this.pip = pip;
	}

	/**
	 * TODO JAVADOC
	 * @param policyCombiningAlgorithm
	 */
	public void setPolicyCombiningAlgorithm(
			PolicyUnorderedCombiningAlgorithm policyCombiningAlgorithm) {
		this.policyCombiningAlgorithm = policyCombiningAlgorithm;
	}

	/**
	 * TODO JAVADOC!!!!
	 * 
	 * 
	 */
	public ResponseCtx evaluate(RequestCtx request) {
		/*
		 * FIXME Introduce proper logging with correlation between
		 * request&response e.g. use generated correlationID
		 */
		logger.debug("Evaluating Request: {}", request.toString());
		RequestInformation reqInfo = new RequestInformation(pip);

		DecisionType decision = policyCombiningAlgorithm
				.evaluateEvaluatableList(request.getRequest(), policyRepository
						.getEvaluatables(request), reqInfo);

		return ResponseCtxFactory.create(request.getRequest(), decision,
				reqInfo);
	}
}