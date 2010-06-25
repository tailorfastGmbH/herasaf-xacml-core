/*
 * Copyright 2009-2010 HERAS-AF (www.herasaf.org)
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

import java.util.List;

import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;

/**
 * The evaluation policy repository contains the deployed and currently active
 * {@link Evaluatable}s. It is the entry point of an evaluation client, like
 * e.g. a PDP, to a policy repository. Thus it provides methods to retrieve
 * {@link Evaluatable}s<br />
 * Further the policy repository is responsible for resolving
 * {@link Evaluatable}s from "remote" repositories. <br />
 * <br />
 * The policy repository may store and/or index the {@link Evaluatable}s. <br />
 * 
 * @author Ylli Sylejmani
 */
public interface PolicyRepositoryEvaluation {

	/**
	 * Returns the {@link Evaluatable} with the given {@link EvaluatableID} from
	 * this or a remote policy repository.
	 * 
	 * @param evaluatableID
	 *            The {@link EvaluatableID} of the requested {@link Evaluatable}
	 * @return The {@link Evaluatable} with the given {@link EvaluatableID}.
	 */
	Evaluatable getEvaluatable(EvaluatableID evaluatableID);

	/**
	 * Returns all {@link Evaluatable}s that match the given request. It may be
	 * possible that {@link Evaluatable}s are returned that do not match the
	 * request. <br />
	 * <br />
	 * An advanced PolicyRepository implementation could e.g. use an index to
	 * reduce the amount of returned policies, so that the PDP can evaluate less
	 * polices. That could speed up overall performance. It is very important
	 * that the implementation does only "not return" policies which can be
	 * explicitly "not be applicable" for the given RequestCtx.
	 * 
	 * @param request
	 *            The request for whom all returned {@link Evaluatable} shall
	 *            match.
	 * @return A {@link List} of {@link Evaluatable}s that may match onto the
	 *         given {@link RequestCtx}.
	 */
	List<Evaluatable> getEvaluatables(RequestCtx request);

}
