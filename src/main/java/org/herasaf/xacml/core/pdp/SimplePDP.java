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

package org.herasaf.xacml.core.pdp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.herasaf.xacml.DataIntegrityException;
import org.herasaf.xacml.EvaluatableNotFoundException;
import org.herasaf.xacml.SyntaxException;
import org.herasaf.xacml.core.attributeFinder.AttributeFinder;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyUnorderedCombiningAlgorithm;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.RequestInformationFactory;
import org.herasaf.xacml.core.context.ResponseCtx;
import org.herasaf.xacml.core.context.ResponseCtxFactory;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.evaluatablepreprocess.EvaluatablePreprocess;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;
import org.herasaf.xacml.core.referenceloader.ReferenceLoader;
import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.pdp.PDPInitializationException;
import org.herasaf.xacml.pdp.locator.Locator;
import org.herasaf.xacml.pdp.locator.impl.index.impl.PolicyContainer;
import org.herasaf.xacml.pdp.persistence.DataAccessException;
import org.herasaf.xacml.pdp.persistence.PersistenceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of the {@link PDP}. <br>
 * <br>
 * <br>
 * <b>IMPORTANT NOTICE:</b><br>
 * After deploying ({@link #deploy(Collection)} and
 * {@link #deploy(Evaluatable)}) and and undeploying ({@link #undeploy(Collection)},
 * {@link #undeploy(EvaluatableID)} and {@link #undeployAll()}) the {@link PDP}
 * <b>must</b> be restarted. If not the {@link PDP} will continue working with
 * the old set of {@link Evaluatable}s.<br>
 * This is a limitation to this implementation of the {@link PDP} and refers to
 * the fact that hot-deploy is not possible in the current release.
 * 
 * @author Florian Huonder
 * @author Patrik Dietschweiler
 * @author Christoph Egger
 * @version 1.2
 */
public class SimplePDP implements PDP {
	private AttributeFinder attributeFinder;
	private volatile Locator locator;
	private ReferenceLoader referenceLoader;
	private PolicyUnorderedCombiningAlgorithm policyCombiningAlgorithm;
	private final Logger logger = LoggerFactory.getLogger(SimplePDP.class);
	private List<Evaluatable> evaluatables;
	private EvaluatablePreprocess evaluatablePreprocess;
	private RequestInformationFactory requestInformationFactory;

	/**
	 * Initializes the PDP.
	 * 
	 * @throws PDPInitializationException
	 */
	@PostConstruct
	public void init() throws PDPInitializationException {
		try {
			evaluatables = persistenceManager.loadAll();
			evaluatablePreprocess.process(evaluatables);
			locator.initialize(evaluatables);
			referenceLoader.initialize(evaluatables);
		} catch (DataAccessException e) {
			logger.error("Evaluatables cannot be loaded from the persistence layer", e);
			throw new PDPInitializationException(e);
		} catch (SyntaxException e) {
			logger.error("Locator cannot be initialized", e);
			throw new PDPInitializationException(e);
		} catch (DataIntegrityException e) {
				logger.error("Evaluatables are inconsistent", e);
			throw new PDPInitializationException(e);
		}
	}

	public void setAttributeFinder(AttributeFinder attributeFinder) {
		this.attributeFinder = attributeFinder;
	}

	public void setLocator(Locator locator) {
		this.locator = locator;
	}

	public synchronized void setPersistenceManager(
			PersistenceManager persistenceManager) { // This method is
		// synchronized that no
		// "Inconsistent
		// synchronization"
		// could happen with the
		// persistenceManager
		this.persistenceManager = persistenceManager;
	}

	public void setReferenceLoader(ReferenceLoader referenceLoader) {
		this.referenceLoader = referenceLoader;
	}

	public void setEvaluatablePreprocess(
			EvaluatablePreprocess evaluatablePreprocess) {
		this.evaluatablePreprocess = evaluatablePreprocess;
	}

	public void setRequestInformationFactory(
			RequestInformationFactory requestInformationFactory) {
		this.requestInformationFactory = requestInformationFactory;
	}

	public void setPolicyCombiningAlgorithm(
			PolicyUnorderedCombiningAlgorithm policyCombiningAlgorithm) {
		this.policyCombiningAlgorithm = policyCombiningAlgorithm;
	}

	/**
	 * {@inheritDoc} <br>
	 * <br>
	 * The {@link #evaluate(RequestCtx)} of the {@link PDPImpl} has the
	 * following flow:
	 * <ol>
	 * <li> The {@link RequestType} is passed to the {@link Locator} which
	 * returns a {@link PolicyContainer} containing the {@link Evaluatable}s
	 * and the references on remote-{@link Evaluatable}s.
	 * <li> The {@link List} containing the references to the remote-{@link Evaluatable}s
	 * is passed to the {@link ReferenceLoader} which returns a {@link Map} of
	 * {@link Evaluatable}s with their ID as key.
	 * <li> A new {@link RequestInformation} object is created with the obtained
	 * {@link Map} of remote-{@link Evaluatable}s in it.
	 * <li> The {@link RequestInformation} together with the {@link RequestType}
	 * and the {@link List} of local-{@link Evaluatable}s is passed to the
	 * root-{@link PolicyCombiningAlgorithm}.
	 * <li> A new {@link ResponseCtx} is created containing the decision.
	 * afterwards it is returned.</li>
	 * </ol>
	 */
	public ResponseCtx evaluate(RequestCtx request) {
		PolicyContainer policyContainer;
		try {
			policyContainer = locator.locate(request.getRequest());
		} catch (SyntaxException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(Thread.currentThread().getId()
						+ ": RequestType corrupt", e);
			}
			return ResponseCtxFactory.create(request.getRequest(),
					DecisionType.INDETERMINATE, StatusCode.SYNTAX_ERROR);
		}

		RequestInformation reqInfo = null;
		reqInfo = requestInformationFactory.createRequestInformation(
				policyContainer.getPolicyRefs(), attributeFinder);

		DecisionType decision = policyCombiningAlgorithm
				.evaluateEvaluatableList(request.getRequest(),
						new ArrayList<Evaluatable>(policyContainer
								.getPolicies()), reqInfo);

		return ResponseCtxFactory.create(request.getRequest(), decision,
				reqInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.xacml.pdp.PDP#getEvaluatable(org.herasaf.xacml.pdp.EvaluatableID)
	 */
	public Evaluatable getEvaluatable(EvaluatableID id)
			throws EvaluatableNotFoundException {
		for (Evaluatable eval : evaluatables) {
			if (eval.getId().equals(id))
				return eval;
		}
		if (logger.isDebugEnabled()) {
			logger.debug(Thread.currentThread().getId()
					+ ": Evaluatable not Found");
		}
		throw new EvaluatableNotFoundException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.pdp.PDP#deploy(java.util.Collection)
	 */
	public synchronized void deploy(Collection<Evaluatable> evals)
			throws DataAccessException, DataIntegrityException, SyntaxException {
		if (logger.isInfoEnabled()) {
			logger.info(Thread.currentThread().getId()
					+ ": deploy new Collection Evaluatables");
		}
		persistenceManager.persistAll(evals);
		evaluatablePreprocess.process(evals);
		evaluatables.addAll(evals);
		locator.initialize(evaluatables);
		referenceLoader.initialize(evaluatables);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.pdp.PDP#deploy(org.herasaf.core.policy.impl.Evaluatable)
	 */
	public synchronized void deploy(Evaluatable evaluatable)
			throws DataAccessException, DataIntegrityException, SyntaxException {
		if (logger.isInfoEnabled()) {
			logger.info(Thread.currentThread().getId()
					+ ": deploy new single Evaluatable");
		}
		persistenceManager.persist(evaluatable);
		evaluatablePreprocess.process(evaluatable);
		evaluatables.add(evaluatable);
		locator.initialize(evaluatables);
		referenceLoader.initialize(evaluatables);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.pdp.PDP#undeploy(java.lang.String)
	 */
	public synchronized void undeploy(EvaluatableID id)
			throws DataAccessException, SyntaxException {
		if (logger.isInfoEnabled()) {
			logger.info(Thread.currentThread().getId()
					+ ": undeploy single Evaluatable");
		}
		persistenceManager.delete(id);
		for (Evaluatable eval : evaluatables) {
			if (eval.getId().equals(id)) {
				evaluatables.remove(eval);
				break;
			}
		}
		locator.initialize(evaluatables);
		referenceLoader.initialize(evaluatables);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.pdp.PDP#undeploy(java.util.Collection)
	 */
	public synchronized void undeploy(Collection<EvaluatableID> ids)
			throws DataAccessException, SyntaxException {
		if (logger.isInfoEnabled()) {
			logger.info(Thread.currentThread().getId()
					+ ": undeploy Collection of Evaluatables");
		}
		for (EvaluatableID id : ids) {
			persistenceManager.delete(id);
			for (Evaluatable eval : evaluatables) {
				if (eval.getId().equals(id)) {
					evaluatables.remove(eval);
					break;
				}
			}
		}
		locator.initialize(evaluatables);
		referenceLoader.initialize(evaluatables);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.pdp.PDP#undeployAll()
	 */
	public synchronized void undeployAll() throws DataAccessException,
			SyntaxException {
		if (logger.isInfoEnabled()) {
			logger.info(Thread.currentThread().getId()
					+ ": All Evaluatables have been undeployed");
		}
		persistenceManager.deleteAll();
		evaluatables.clear();
		locator.initialize(evaluatables);
		referenceLoader.initialize(evaluatables);
	}

}