/*
 * Copyright 2009 HERAS-AF (www.herasaf.org)
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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;

/**
 * The policy repository contains the deployed and currently active {@link Evaluatable}s. There are various manipulation
 * methods to add and remove {@link Evaluatable}s. Further it is possible to determine the state of this policy
 * repository at any point of time in the past.<br />
 * Further the policy repository is responsible for resolving {@link Evaluatable}s from "remote" repositories. <br />
 * <br />
 * The policy repository may store and/or index the {@link Evaluatable}s. <br />
 * <br />
 * <b>Note:</b><br />
 * This policy repository does only support <u>unordered</u> deployment of {@link Evaluatable}s (see
 * {@link #deploy(Evaluatable)}).
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public interface PolicyRepository {

    /**
     * Inserts a new {@link Evaluatable} into the policy repository. It is put at the end of the list. It does not
     * require a specific (ordered or unordered) combining algorithm.
     * 
     * @param evaluatable
     *            The {@link Evaluatable} to add to the policy repository.
     */
    void deploy(Evaluatable evaluatable);

    /**
     * Inserts a collection of new {@link Evaluatable}s into the policy repository. The {@link Evaluatable} are added
     * randomly to the end of the list. It does not require a specific (ordered or unordered) combining algorithm.
     * 
     * @param evaluatables
     *            The collection of {@link Evaluatable}s to add to the policy repository.
     */
    void deploy(Collection<Evaluatable> evaluatables);

    /**
     * Removes the {@link Evaluatable} with the given {@link EvaluatableID} from the policy repository.
     * 
     * @param evaluatableID
     *            The id of the {@link Evaluatable} to remove.
     */
    void undeploy(EvaluatableID evaluatableID);

    /**
     * Removes the {@link Evaluatable}s with the given {@link EvaluatableID}s from the policy repository.
     * 
     * @param evaluatableIDs
     *            The collection containg the {@link EvaluatableID} of the {@link Evaluatable}s to be removed.
     */
    void undeploy(Collection<EvaluatableID> evaluatableIDs);

    /**
     * Applies a list of {@link DeploymentModification}s to the currently deployed {@link Evaluatable} tree in this
     * policy repository.
     * 
     * @param deploymentModifications
     *            The list containing the {@link DeploymentModification}s.
     */
    void applyDeploymentModifications(List<DeploymentModification> deploymentModifications);

    /**
     * Returns the currently deployed {@link Evaluatable}s.
     * 
     * @return A list containing the currently deployed {@link Evaluatable}s.
     */
    List<Evaluatable> getDeployment();

    /**
     * Returns the {@link Evaluatable}s that were deployed at the given time dateTime.
     * 
     * @param dateTime
     *            The active-time of the wanted deployment.
     * @return A list containing the {@link Evaluatable}s that were active at the given dateTime.
     */
    List<Evaluatable> getDeployment(Date dateTime);

    /**
     * Returns all deployments that were active since the beginning of life of this policy repository.
     * 
     * @return All deployments since the beginning of life of this policy repository.
     */
    Map<Date, List<Evaluatable>> getDeployments();

    /**
     * Returns the {@link Evaluatable} with the given {@link EvaluatableID} from this or a remote policy repository.
     * 
     * @param evaluatableID
     *            The {@link EvaluatableID} of the requested {@link Evaluatable} .
     * @return The {@link Evaluatable} with the given {@link EvaluatableID}.
     */
    Evaluatable getEvaluatable(EvaluatableID evaluatableID);

    /**
     * Returns all {@link Evaluatable}s that match the given request. It may be possible that {@link Evaluatable}s are
     * returned that do not match the request. <br />
     * <br />
     * An advanced PolicyRepository implementation could e.g. use an index to reduce the amount of returned policies, so
     * that the PDP can evaluate less polices. That could speed up overall performance. It is very important that the
     * implementation does only "not return" policies which can be explicitly "not be applicable" for the given RequestCtx.
     * 
     * @param request
     *            The request for whom all returned {@link Evaluatable} shall match.
     * @return A {@link List} of {@link Evaluatable}s that may match onto the given {@link RequestCtx}.
     */
    List<Evaluatable> getEvaluatables(RequestCtx request);
}