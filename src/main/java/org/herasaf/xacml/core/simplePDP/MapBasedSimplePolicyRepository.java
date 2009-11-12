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
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;
import org.herasaf.xacml.core.policy.impl.IdReferenceType;
import org.herasaf.xacml.core.policy.impl.PolicySetType;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO JAVADOC!!
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class MapBasedSimplePolicyRepository implements PolicyRepository {
	private Map<EvaluatableID, List<Evaluatable>> individualEvaluatables; // The
	// reason
	// that
	// a
	// List
	// is
	// put
	// as
	// value
	// here
	// is:
	// A
	// map
	// can
	// only
	// store
	// on
	// value
	// per
	// key.
	// But
	// if
	// a
	// local
	// reference
	// shall
	// be
	// in
	// the
	// list,
	// it
	// has
	// at
	// least
	// two
	// values
	// for
	// that
	// specific
	// key.
	// The
	// key
	// shall
	// be
	// a
	// tupel
	// of
	// two
	// elements
	// (root-key,eval-key).
	// The
	// root
	// key
	// shall
	// point
	// to
	// the
	// root
	// policy
	// to
	// which
	// this
	// policy
	// belongs.
	private Map<EvaluatableID, List<EvaluatableID>> rootEvaluatableMapping; // Mapping
	// that
	// tells
	// which
	// policies
	// are
	// under
	// which
	// root.
	private List<Evaluatable> rootEvaluatables; // The root evaluatables

	private final Logger logger = LoggerFactory.getLogger(MapBasedSimplePolicyRepository.class);

	public MapBasedSimplePolicyRepository() {
		individualEvaluatables = new HashMap<EvaluatableID, List<Evaluatable>>();
		rootEvaluatableMapping = new HashMap<EvaluatableID, List<EvaluatableID>>();
		rootEvaluatables = new ArrayList<Evaluatable>();
	}

	/**
	 * {@inheritDoc}
	 */
	public void deploy(Collection<Evaluatable> evaluatables) throws PolicyRepositoryException {

		for (Evaluatable eval : evaluatables) {
			deploy(eval); // Change object can be neglected here
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void deploy(Evaluatable evaluatable) throws PolicyRepositoryException {
		Map<EvaluatableID, List<Evaluatable>> newIndividualEvaluatables = splitIntoIndividuals(evaluatable, evaluatable
				.getId());

		if (!checkReferenceConsistency(newIndividualEvaluatables)) { // check
			// for
			// consistency
			throw new PolicyRepositoryException("The PolicySet is not consistent.");
		}

		for (EvaluatableID id : newIndividualEvaluatables.keySet()) { // Check
			// for
			// uniqueness
			// of
			// the
			// keys.
			if (individualEvaluatables.containsKey(id)) {
				throw new PolicyRepositoryException("The ID must be unique over all PolicySets and Policies.");
			}
		}

		individualEvaluatables.putAll(newIndividualEvaluatables);
		rootEvaluatables.add(evaluatable);
	}

	/**
	 * {@inheritDoc}
	 */
	public void undeploy(EvaluatableID id) throws PolicyRepositoryException {
		boolean foundAtLeastOneMatchingEvaluatable = false;
		for (int i = 0; i < rootEvaluatables.size(); i++) {
			Evaluatable eval = rootEvaluatables.get(i);
			if (eval.getId().equals(id)) {
				foundAtLeastOneMatchingEvaluatable = true;
				rootEvaluatables.remove(eval);
				List<EvaluatableID> ids = rootEvaluatableMapping.get(eval.getId());
				for (EvaluatableID evalId : ids) {
					individualEvaluatables.remove(evalId);
				}
				rootEvaluatableMapping.remove(eval.getId());
			}
		}
		if (!foundAtLeastOneMatchingEvaluatable) {
			throw new PolicyRepositoryException("No root policy with id: " + id.getId());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void undeploy(Collection<EvaluatableID> ids) throws PolicyRepositoryException {
		for (EvaluatableID id : ids) {
			undeploy(id);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void applyDeploymentModifications(List<DeploymentModification> deploymentInstructions)
			throws PolicyRepositoryException {
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
	public Evaluatable getEvaluatable(EvaluatableID id) throws PolicyRepositoryException {
		List<Evaluatable> evals = individualEvaluatables.get(id);
		if (evals != null) {
			for (Evaluatable eval : evals) {
				if (eval instanceof PolicyType || eval instanceof PolicySetType) {
					return eval;
				}
			}
		}
		throw new PolicyRepositoryException("No Evaluatable with ID " + id.getId() + " found.");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Due to the fact that this implementation does not implement any index.
	 * All root {@link Evaluatable}s are returned by default.
	 */
	public List<Evaluatable> getEvaluatables(RequestCtx requestCtx) {
		return rootEvaluatables;
	}

	private Map<EvaluatableID, List<Evaluatable>> splitIntoIndividuals(
			Map<EvaluatableID, List<Evaluatable>> initialMap, Evaluatable evaluatable, EvaluatableID rootId) {
		Map<EvaluatableID, List<Evaluatable>> individualEvaluatables;
		if (initialMap == null) {
			individualEvaluatables = new HashMap<EvaluatableID, List<Evaluatable>>(); // This
			// construct
			// is
			// called
			// MULTIMAP.
		} else {
			individualEvaluatables = initialMap;
		}

		// The following if-else block adds the id of this evaluatable and this
		// evaluatable to the list of the individual evaluatables
		if (individualEvaluatables.containsKey(evaluatable.getId())) { // checks
			// if an
			// entry
			// with
			// this
			// ID
			// already
			// exists.
			individualEvaluatables.get(evaluatable.getId()).add(evaluatable); // if
			// yes,
			// the
			// additional
			// evaluatable
			// is
			// added.
		} else { // if no, the first entry at this id is created.
			List<Evaluatable> value = new ArrayList<Evaluatable>();
			value.add(evaluatable);
			individualEvaluatables.put(evaluatable.getId(), value);
			addRootMapping(evaluatable.getId(), rootId);
		}
		if (evaluatable instanceof PolicySetType) { // If the evaluatable is a
			// PolicySet, this method is
			// recursively applied to
			// all sub-policies
			for (Evaluatable eval : ((PolicySetType) evaluatable).getUnorderedEvaluatables(null)) {
				individualEvaluatables.putAll(splitIntoIndividuals(individualEvaluatables, eval, rootId));
			}
		}
		return individualEvaluatables;
	}

	/*
	 * Entry Point for other methods!
	 */
	private Map<EvaluatableID, List<Evaluatable>> splitIntoIndividuals(Evaluatable evaluatable, EvaluatableID rootId) {
		return splitIntoIndividuals(null, evaluatable, rootId);
	}

	/**
	 * @param id
	 * @param rootId
	 */
	private void addRootMapping(EvaluatableID id, EvaluatableID rootId) {
		if (rootEvaluatableMapping.containsKey(rootId)) { // checks if an entry
			// with this ID
			// already exists.
			rootEvaluatableMapping.get(rootId).add(id); // if yes, the
			// additional
			// evaluatableId is
			// added.
		} else { // if no, the first entry at this id is created.
			List<EvaluatableID> value = new ArrayList<EvaluatableID>();
			value.add(id);
			rootEvaluatableMapping.put(rootId, value);
		}
	}

	/**
	 * @param individualEvaluatables2
	 */
	private boolean checkReferenceConsistency(Map<EvaluatableID, List<Evaluatable>> individualEvaluatables) {
		for (List<Evaluatable> evals : individualEvaluatables.values()) {
			if (evals.size() == 1) {
				if (evals.get(0) instanceof IdReferenceType) {
					logger
							.error("This implementation of the PolicyRepository interface does not support Remote references. Further a local reference must be within the same PolicySet.");
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
					if (!(eval instanceof IdReferenceType) && anEvaluatableFound) {
						logger.error("The ID must be unique over all PolicySets and Policies.");
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