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

package org.herasaf.xacml.core.combiningAlgorithm;

import org.herasaf.xacml.core.NotInitializedException;
import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.StatusCode;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.impl.TargetType;
import org.herasaf.xacml.core.targetMatcher.TargetMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO JAVADOC
 * 
 * Abstract class for all {@link CombiningAlgorithm}s. It contains the logic for
 * the targetMatch.
 * 
 * @author Stefan Oberholzer
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public abstract class AbstractCombiningAlgorithm implements CombiningAlgorithm {
	private final Logger logger = LoggerFactory.getLogger(AbstractCombiningAlgorithm.class);
	private TargetMatcher targetMatcher;

	/**
	 * If set to true abandoned evaluatables will be included (if
	 * possible) in the evaluation.
	 */
	private boolean respectAbandonedEvaluatables;

	/**
	 * TODO JAVADOC.
	 * 
	 * @return
	 */
	public boolean isRespectAbandonedEvaluatables() {
		return respectAbandonedEvaluatables;
	}

	public void setRespectAbandondEvaluatables(final boolean respectAbandondEvaluatables) {
		this.respectAbandonedEvaluatables = respectAbandondEvaluatables;
	}

	/**
	 * Sets the TargetMatcher.
	 */
	public void setTargetMatcher(final TargetMatcher targetMatcher) {
		this.targetMatcher = targetMatcher;
	}

	/**
	 * Matches the target of the request.
	 * 
	 * @param request
	 *            the request that should be evaluated.
	 * @param target
	 *            the target of the Evaluatable or rule
	 * @param requestInfo
	 *            the additional informations of this request evaluation
	 *            process.
	 * @return The decision of matching the target.
	 */
	protected DecisionType matchTarget(final RequestType request, final TargetType target, final RequestInformation requestInfo) {
		boolean targetMatchDecision = false;
		DecisionType decision = DecisionType.INDETERMINATE;
		try {
			logger.debug("Starting target match.");
			targetMatchDecision = targetMatcher.match(request, target, requestInfo);

			/*
			 * The target match can't return "not Applicable". If the
			 * targetMatchDecision is deny, the policy has to return not
			 * applicable. See: OASIS eXtensible Access Control Markup Langugage
			 * (XACML) 2.0, Errata 29 June 2006</a> page 83, chapter Policy
			 * evaluation for further information.
			 */
			if (targetMatchDecision) {
				decision = DecisionType.PERMIT;
			} else {
				requestInfo.setTargetMatched(false);
				decision = DecisionType.NOT_APPLICABLE;
			}
		} catch (NullPointerException e) {
			logger.error("TargetMatcher not initialized.", e);
			throw new NotInitializedException(e);
		} catch (SyntaxException e) {
			requestInfo.updateStatusCode(StatusCode.SYNTAX_ERROR);
			requestInfo.setTargetMatched(false);
			logger.debug("Syntax error occurred.");
		} catch (ProcessingException e) {
			requestInfo.updateStatusCode(StatusCode.PROCESSING_ERROR);
			requestInfo.setTargetMatched(false);
			logger.debug("Processing error occurred.");
		} catch (MissingAttributeException e) {
			requestInfo.updateStatusCode(StatusCode.MISSING_ATTRIBUTE);
			requestInfo.addMissingAttributes(e.getMissingAttribute());
			requestInfo.setTargetMatched(false);
			logger.debug("Missing attribute error occurred.");
		}

		logger.debug("Target match resulted in: {}", decision);
		return decision;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getCombiningAlgorithmId();
	}

	/**
	 * Returns the ID of the combining algorithm.
	 * 
	 * @return The ID of the combining algorithm.
	 */
	public abstract String getCombiningAlgorithmId();
}