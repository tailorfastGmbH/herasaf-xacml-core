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
package org.herasaf.xacml.core.simplePDP;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.PolicyRepositoryException;
import org.herasaf.xacml.core.api.DeploymentModification;
import org.herasaf.xacml.core.api.PolicyRepository;
import org.herasaf.xacml.core.api.PolicyRetrievalPoint;
import org.herasaf.xacml.core.api.UnorderedPolicyRepository;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;
import org.herasaf.xacml.core.policy.impl.IdReferenceType;
import org.herasaf.xacml.core.policy.impl.PolicySetType;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the very simple implementation of the {@link PolicyRepository}. This
 * policy repository has a limited functionality. It only works as a "provider"
 * of locally deployed {@link Evaluatable}s.<br />
 * <br />
 * This implementation doesn't support all operations defined in
 * {@link PolicyRepository}. Some methods throw a UnsupportedOperationException.<br />
 * <br />
 * <b>It does not:</b>
 * <ul>
 * <li>persist the Evaluatables</li>
 * <li>index the Evaluatables</li>
 * <li>resolve Evaluatables from remote (only local) repositories</li>
 * </ul>
 * <b>WARNING: </b>It is not recommended to use this repository in a productive
 * environment.<br />
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class MapBasedSimplePolicyRepository implements
		UnorderedPolicyRepository, PolicyRetrievalPoint {
	// The reason that a List is put as value here is: A map can only store on
	// value per key. But if a local reference shall be in the list, it has at
	// least two values for that specific key. The key shall be a tupel of two
	// elements (root-key,eval-key). The root key shall point to the root policy
	// to which this policy belongs.
	// There can be multiple evaluatables for an id in case of references. a
	// reference has the same id as the evaluatables itself.
	protected Map<EvaluatableID, List<Evaluatable>> individualEvaluatables;
	// Mapping that tells which policies are under which root.
	protected Map<EvaluatableID, List<EvaluatableID>> rootEvaluatableMapping;
	protected List<Evaluatable> rootEvaluatables; // The root evaluatables
	private transient final Logger logger = LoggerFactory
			.getLogger(MapBasedSimplePolicyRepository.class);

	/**
	 * Initializes a new policy repository.
	 * 
	 * @param isOrderedCombiningAlgorithm
	 *            True if the root policy combining algorithm in the PDP is
	 *            ordered, false otherwise.
	 */
	public MapBasedSimplePolicyRepository() {
		individualEvaluatables = new HashMap<EvaluatableID, List<Evaluatable>>();
		rootEvaluatableMapping = new HashMap<EvaluatableID, List<EvaluatableID>>();
		rootEvaluatables = new ArrayList<Evaluatable>();

		logger
				.warn(
						"This policy repository ({}) must not be used in a productive environment.",
						this.getClass().getCanonicalName());
	}

	/**
	 * {@inheritDoc}
	 */
	public void deploy(Collection<Evaluatable> evaluatables) {

		for (Evaluatable eval : evaluatables) {
			deploy(eval);
		}
	}

	/**
	 * Checks for consistency of the given {@link Evaluatable}s. The provided
	 * {@link Map} of {@link Evaluatable}s (indexed by the {@link EvaluatableID}
	 * s). A root {@link Evaluatable} must be consistent in itself.
	 * 
	 * @param newIndividualEvaluatables
	 *            The list of individual (meaning all sub-{@link Evaluatable}s)
	 *            {@link Evaluatable}s to be checked for consistency.
	 */
	protected void checkEvaluatable(
			Map<EvaluatableID, List<Evaluatable>> newIndividualEvaluatables) {

		// check for reference consistency
		if (!checkReferenceConsistency(newIndividualEvaluatables)) {
			throw new PolicyRepositoryException(
					"The PolicySet is not consistent.");
		}

		// Check for uniqueness of the keys.
		for (EvaluatableID id : newIndividualEvaluatables.keySet()) {
			// Checks if the already deployed individual evaluatables contain a
			// key that already exists.
			if (individualEvaluatables.containsKey(id)) {
				throw new PolicyRepositoryException(
						"The ID must be unique over all PolicySets and Policies.");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void deploy(Evaluatable evaluatable) {
		Map<EvaluatableID, List<Evaluatable>> newIndividualEvaluatables = splitIntoIndividuals(
				evaluatable, evaluatable.getId());

		checkEvaluatable(newIndividualEvaluatables);

		individualEvaluatables.putAll(newIndividualEvaluatables);
		rootEvaluatables.add(evaluatable);
	}

	/**
	 * {@inheritDoc}
	 */
	public void undeploy(EvaluatableID id) {
		boolean foundAtLeastOneMatchingEvaluatable = false;
		for (int i = 0; i < rootEvaluatables.size(); i++) {
			Evaluatable eval = rootEvaluatables.get(i);
			if (eval.getId().equals(id)) {
				foundAtLeastOneMatchingEvaluatable = true;
				rootEvaluatables.remove(eval);
				List<EvaluatableID> ids = rootEvaluatableMapping.get(eval
						.getId());
				for (EvaluatableID evalId : ids) {
					individualEvaluatables.remove(evalId);
				}
				rootEvaluatableMapping.remove(eval.getId());
			}
		}
		if (!foundAtLeastOneMatchingEvaluatable) {
			throw new PolicyRepositoryException("No root policy with id: "
					+ id.getId());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void undeploy(Collection<EvaluatableID> ids) {
		for (EvaluatableID id : ids) {
			undeploy(id);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void applyDeploymentModifications(
			List<DeploymentModification> deploymentInstructions) {
		String msg = "The MapBasedSimplePolicyRepository does not support the application of Diffs";
		logger.error(msg);
		throw new UnsupportedOperationException(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Evaluatable> getDeployment() {
		return rootEvaluatables;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Evaluatable> getDeployment(Date dateTime) {
		String msg = "The MapBasedSimplePolicyRepository does not persist previous deployments.";
		logger.error(msg);
		throw new UnsupportedOperationException(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<Date, List<Evaluatable>> getDeployments() {
		String msg = "The MapBasedSimplePolicyRepository does not persist previous deployments.";
		logger.error(msg);
		throw new UnsupportedOperationException(msg);
	}

	/**
	 * {@inheritDoc}
	 */
	public Evaluatable getEvaluatable(EvaluatableID id) {
		List<Evaluatable> evals = individualEvaluatables.get(id);
		if (evals != null) {
			for (Evaluatable eval : evals) {
				if (eval instanceof PolicyType || eval instanceof PolicySetType) {
					return eval;
				}
			}
		}
		throw new PolicyRepositoryException("No Evaluatable with ID "
				+ id.getId() + " found.");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Due to the fact that this implementation does not implement any index.
	 * All root {@link Evaluatable}s are returned by default.
	 * 
	 * @deprecated Use {@link #getEvaluatables(RequestType)} instead.
	 */
	@Deprecated
	public List<Evaluatable> getEvaluatables(RequestCtx requestCtx) {
		return rootEvaluatables;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Due to the fact that this implementation does not implement any index.
	 * All root {@link Evaluatable}s are returned by default.
	 */
	public List<Evaluatable> getEvaluatables(RequestType request) {
		return rootEvaluatables;
	}

	/**
	 * This is the entry point to split {@link Evaluatable}s into individuals.
	 * This means that a policy tree is split into its nodes. The resulting Map
	 * contains as key all IDs that exist in this tree (of all nodes). The list
	 * associated with this ID contains all {@link Evaluatable}s that have this
	 * ID and all references that point to this ID. Here the policy tree is not
	 * checked for consistency. So illegal states are allowed here.
	 * 
	 * @param evaluatable
	 *            The root {@link Evaluatable} to be split.
	 * @param rootId
	 *            The ID of this root {@link Evaluatable}.
	 * @param initialMap
	 *            This map contains already {@link Evaluatable}s from a previous
	 *            step. This beause this method is intended to be called
	 *            recursivly down the whole policy tree.
	 * @return A {@link Map} containing the whole policy tree that started at
	 *         the given {@link Evaluatable} split up into individual policies.
	 */
	private Map<EvaluatableID, List<Evaluatable>> splitIntoIndividuals(
			Map<EvaluatableID, List<Evaluatable>> initialMap,
			Evaluatable evaluatable, EvaluatableID rootId) {
		Map<EvaluatableID, List<Evaluatable>> individualEvaluatables;
		if (initialMap == null) {
			// (This construct is called MULTIMAP.)
			individualEvaluatables = new HashMap<EvaluatableID, List<Evaluatable>>();
		} else {
			individualEvaluatables = initialMap;
		}

		// The following if-else block adds the id of this evaluatable and this
		// evaluatable to the list of the individual evaluatables.
		// Checks if an entry with this ID already exists.
		if (individualEvaluatables.containsKey(evaluatable.getId())) {
			// if yes, the additional (additional may be a reference)
			// evaluatable is added.
			individualEvaluatables.get(evaluatable.getId()).add(evaluatable);
		} else { // if no, the first entry at this id is created.
			List<Evaluatable> value = new ArrayList<Evaluatable>();
			value.add(evaluatable);
			individualEvaluatables.put(evaluatable.getId(), value);
			addRootMapping(evaluatable.getId(), rootId);
		}

		// If the evaluatable is a PolicySet, this method is recursively applied
		// to all sub-policies
		if (evaluatable instanceof PolicySetType) {
			for (Evaluatable eval : ((PolicySetType) evaluatable)
					.getUnorderedEvaluatables(null)) {
				individualEvaluatables.putAll(splitIntoIndividuals(
						individualEvaluatables, eval, rootId));
			}
		}
		return individualEvaluatables;
	}

	/**
	 * This is the entry point to split {@link Evaluatable}s into individuals.
	 * This means that a policy tree is split into its nodes. The resulting Map
	 * contains as key all IDs that exist in this tree (of all nodes). The list
	 * associated with this ID contains all {@link Evaluatable}s that have this
	 * ID and all references that point to this ID. Here the policy tree is not
	 * checked for consistency. So illegal states are allowed here.
	 * 
	 * @param evaluatable
	 *            The root {@link Evaluatable} to be split.
	 * @param rootId
	 *            The ID of this root {@link Evaluatable}.
	 * 
	 * @return A {@link Map} containing the whole policy tree that started at
	 *         the given {@link Evaluatable} split up into individual policies.
	 */
	protected Map<EvaluatableID, List<Evaluatable>> splitIntoIndividuals(
			Evaluatable evaluatable, EvaluatableID rootId) {
		return splitIntoIndividuals(null, evaluatable, rootId);
	}

	/**
	 * Every {@link Evaluatable}s must be mapped to its root {@link Evaluatable}
	 * . This because in case of an undeployment. All related sub-
	 * {@link Evaluatable}s must be removed from the list containing the
	 * individual {@link Evaluatable}s.
	 * 
	 * @param id
	 *            The ID of the {@link Evaluatable}.
	 * @param rootId
	 *            The ID of the corresponding root {@link Evaluatable}.
	 */
	private void addRootMapping(EvaluatableID id, EvaluatableID rootId) {
		// checks if an entry with this ID already exists.
		if (rootEvaluatableMapping.containsKey(rootId)) {
			// if yes, the additional (additional may be a reference)
			// evaluatableId is added.
			rootEvaluatableMapping.get(rootId).add(id);
		} else { // if no, the first entry at this id is created.
			List<EvaluatableID> value = new ArrayList<EvaluatableID>();
			value.add(id);
			rootEvaluatableMapping.put(rootId, value);
		}
	}

	/**
	 * Checks if a policy is unique (id) and if references are only local
	 * references.
	 * 
	 * @param individualEvaluatables
	 *            The {@link Map} of {@link Evaluatable} to be checked for
	 *            consitency.
	 */
	private boolean checkReferenceConsistency(
			Map<EvaluatableID, List<Evaluatable>> individualEvaluatables) {
		for (List<Evaluatable> evals : individualEvaluatables.values()) {
			if (evals.size() == 1) {
				if (evals.get(0) instanceof IdReferenceType) {
					logger
							.error("This implementation of the PolicyRepository interface does not support remote references. Further a local reference must be within the same PolicySet.");
					return false; // If the List contains only one element that
					// is a reference. It must be either a
					// remote-reference or a local-reference
					// that is not in this PolicySet. The first
					// is not supported by this PolicyRepository
					// implementation and the second is
					// prohibited by the XACML specification.
				}
			} else { // More than one element in the List. Must be local
				// references. Further it is not allowed (by specification) to
				// have more than one policy with the same id.
				boolean anEvaluatableFound = false;
				for (Evaluatable eval : evals) {
					if (!(eval instanceof IdReferenceType)
							&& anEvaluatableFound) {
						logger
								.error("The ID must be unique over all PolicySets and Policies.");
						return false;
					} else if (!(eval instanceof IdReferenceType)) {
						anEvaluatableFound = true;
					}
				}
			}
		}
		return true;
	}
}