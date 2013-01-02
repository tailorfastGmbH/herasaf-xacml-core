/*
 * Copyright 2008 - 2012 HERAS-AF (www.herasaf.org)
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

package org.herasaf.xacml.core.targetMatcher.impl;

import java.util.List;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.EvaluationContext;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.impl.ActionType;
import org.herasaf.xacml.core.policy.impl.ActionsType;
import org.herasaf.xacml.core.policy.impl.AttributeDesignatorType;
import org.herasaf.xacml.core.policy.impl.AttributeValueType;
import org.herasaf.xacml.core.policy.impl.EnvironmentType;
import org.herasaf.xacml.core.policy.impl.EnvironmentsType;
import org.herasaf.xacml.core.policy.impl.Match;
import org.herasaf.xacml.core.policy.impl.ResourceType;
import org.herasaf.xacml.core.policy.impl.ResourcesType;
import org.herasaf.xacml.core.policy.impl.SubjectType;
import org.herasaf.xacml.core.policy.impl.SubjectsType;
import org.herasaf.xacml.core.policy.impl.TargetType;
import org.herasaf.xacml.core.targetMatcher.TargetMatcher;
import org.herasaf.xacml.core.targetMatcher.TargetMatchingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Matches a policy's, a policy set's or a rule's target against a given
 * request.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class TargetMatcherImpl implements TargetMatcher {
	private static final long serialVersionUID = 1L;
	private transient final Logger logger = LoggerFactory
			.getLogger(TargetMatcherImpl.class);

	/**
	 * {@inheritDoc}
	 */
	public TargetMatchingResult match(RequestType request, TargetType target,
			EvaluationContext evaluationContext) throws SyntaxException,
			ProcessingException, MissingAttributeException {
		if (target != null) {
			logger.debug("Starting subjects match.");
			boolean subjectsMatches = subjectsMatch(target.getSubjects(),
					request, evaluationContext);
			logger.debug("Subjects match resulted in: {}", subjectsMatches);
			if (!subjectsMatches) {
				return TargetMatchingResult.NO_MATCH;
			}

			logger.debug("Starting recources match.");
			boolean resourcesMatches = resourcesMatch(target.getResources(),
					request, evaluationContext);
			logger.debug("Resources match resulted in: {}", resourcesMatches);
			if (!resourcesMatches) {
				return TargetMatchingResult.NO_MATCH;
			}

			logger.debug("Starting actions match.");
			boolean actionsMatches = actionMatch(target.getActions(), request,
					evaluationContext);
			logger.debug("Actions match resulted in: {}", actionsMatches);
			if (!actionsMatches) {
				return TargetMatchingResult.NO_MATCH;
			}

			logger.debug("Starting environments match.");
			boolean environmentsMatches = environmentMatch(
					target.getEnvironments(), request, evaluationContext);
			logger.debug("Environments match resulted in: {}",
					environmentsMatches);
			if (!environmentsMatches) {
				return TargetMatchingResult.NO_MATCH;
			}
		}
		// If there was no target, or all the subjects, resources, actions, and
		// environments matched
		// then the overall target is considered a match.
		//
		// This part references OASIS eXtensible Access Control Markup Language
		// (XACML) 2.0, Errata 29 January 2008
		// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
		// on page 48 (5.5).
		return TargetMatchingResult.MATCH;
	}

	/**
	 * Checks if the <code>&lt;Subjects&gt;</code> element matches the given
	 * request.
	 * 
	 * @param subjects
	 *            The <code>&lt;Subjects&gt;</code> element to be matched.
	 * @param request
	 *            The requests containing the attributes to be matched against
	 *            the <code>&lt;Subjects&gt;</code>.
	 * @param evaluationContext
	 *            The {@link EvaluationContext} containing additional
	 *            information.
	 * @return
	 * @throws ProcessingException
	 *             If an exception occurred while processing the matching
	 *             functions.
	 * @throws SyntaxException
	 *             If the request or the policy contained a syntax error.
	 * @throws MissingAttributeException
	 *             If a <i>must-be-present</i> attribute is missing.
	 */
	private boolean subjectsMatch(SubjectsType subjects, RequestType request,
			EvaluationContext evaluationContext) throws ProcessingException,
			SyntaxException, MissingAttributeException {
		if (subjects == null) {
			logger.debug("No subjects present.");
			return true;
		}
		for (int i = 0; i < subjects.getSubjects().size(); i++) {
			SubjectType targetSubject = subjects.getSubjects().get(i);
			logger.debug("Starting subject match. (id:{})", targetSubject);
			boolean matches = match(targetSubject.getSubjectMatches(), request,
					evaluationContext);
			if (matches) {
				// If one subject matches the subjects matches and returns true.
				//
				// This part references OASIS eXtensible Access Control Markup
				// Langugage (XACML) 2.0, Errata 29 January 2008
				// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
				// on page 49 (5.6).
				logger.debug("Subject match resulted in: {}", matches);
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the <code>&lt;Resources&gt;</code> element matches the given
	 * request.
	 * 
	 * @param resources
	 *            The <code>&lt;Resources&gt;</code> element to be matched.
	 * @param request
	 *            The requests containing the attributes to be matched against
	 *            the <code>&lt;Resources&gt;</code>.
	 * @param evaluationContext
	 *            The {@link EvaluationContext} containing additional
	 *            information.
	 * @return
	 * @throws ProcessingException
	 *             If an exception occurred while processing the matching
	 *             functions.
	 * @throws SyntaxException
	 *             If the request or the policy contained a syntax error.
	 * @throws MissingAttributeException
	 *             If a <i>must-be-present</i> attribute is missing.
	 */
	private boolean resourcesMatch(ResourcesType resources,
			RequestType request, EvaluationContext evaluationContext)
			throws ProcessingException, SyntaxException,
			MissingAttributeException {
		if (resources == null) {
			logger.debug("No resources present.");
			return true;
		}
		for (int i = 0; i < resources.getResources().size(); i++) {
			ResourceType targetResource = resources.getResources().get(i);
			logger.debug("Starting resource match. (id:{})", targetResource);
			boolean matches = match(targetResource.getResourceMatches(),
					request, evaluationContext);
			if (matches) {
				// If one resource matches the resources matches and returns
				// true.
				//
				// This part references OASIS eXtensible Access Control Markup
				// Langugage (XACML) 2.0, Errata 29 January 2008
				// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
				// on page 50 (5.9).
				logger.debug("Resource match resulted in: {}", matches);
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the <code>&lt;Actions&gt;</code> element matches the given
	 * request.
	 * 
	 * @param actions
	 *            The <code>&lt;Actions&gt;</code> element to be matched.
	 * @param request
	 *            The requests containing the attributes to be matched against
	 *            the <code>&lt;Actions&gt;</code>.
	 * @param evaluationContext
	 *            The {@link EvaluationContext} containing additional
	 *            information.
	 * @return
	 * @throws ProcessingException
	 *             If an exception occurred while processing the matching
	 *             functions.
	 * @throws SyntaxException
	 *             If the request or the policy contained a syntax error.
	 * @throws MissingAttributeException
	 *             If a <i>must-be-present</i> attribute is missing.
	 */
	private boolean actionMatch(ActionsType actions, RequestType request,
			EvaluationContext evaluationContext) throws ProcessingException,
			SyntaxException, MissingAttributeException {
		if (actions == null) {
			logger.debug("No actions present.");
			return true;
		}
		for (int i = 0; i < actions.getActions().size(); i++) {
			ActionType targetAction = actions.getActions().get(i);
			logger.debug("Starting action match. (id:{})", targetAction);
			boolean matches = match(targetAction.getActionMatches(), request,
					evaluationContext);
			if (matches) {
				// If one action matches the actions matches and returns true.
				//
				// This part references OASIS eXtensible Access Control Markup
				// Langugage (XACML) 2.0, Errata 29 January 2008
				// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
				// on page 51 (5.12).
				logger.debug("Action match resulted in: {}", matches);
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the <code>&lt;Environments&gt;</code> element matches the given
	 * request.
	 * 
	 * @param environments
	 *            The <code>&lt;Environments&gt;</code> element to be matched.
	 * @param request
	 *            The requests containing the attributes to be matched against
	 *            the <code>&lt;Environments&gt;</code>.
	 * @param evaluationContext
	 *            The {@link EvaluationContext} containing additional
	 *            information.
	 * @return
	 * @throws ProcessingException
	 *             If an exception occurred while processing the matching
	 *             functions.
	 * @throws SyntaxException
	 *             If the request or the policy contained a syntax error.
	 * @throws MissingAttributeException
	 *             If a <i>must-be-present</i> attribute is missing.
	 */
	private boolean environmentMatch(EnvironmentsType environments,
			RequestType request, EvaluationContext evaluationContext)
			throws ProcessingException, SyntaxException,
			MissingAttributeException {
		if (environments == null) {
			logger.debug("No environments present.");
			return true;
		}
		for (int i = 0; i < environments.getEnvironments().size(); i++) {
			EnvironmentType targetEnvironment = environments.getEnvironments()
					.get(i);
			logger.debug("Starting environment match. (id:{})",
					targetEnvironment);
			boolean matches = match(targetEnvironment.getEnvironmentMatches(),
					request, evaluationContext);
			if (matches) {
				// If one environment matches the environments matches and
				// returns true.
				//
				// This part references OASIS eXtensible Access Control Markup
				// Langugage (XACML) 2.0, Errata 29 January 2008
				// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
				// on page 53 (5.15).
				logger.debug("Environment match resulted in: {}", matches);
				return true;
			}
		}
		return false;
	}

	/**
	 * @param matches
	 *            The matching element (either <code>&lt;SubjectMatch&gt;</code>
	 *            , <code>&lt;ResourceMatch&gt;</code>,
	 *            <code>&lt;ActionMatch&gt;</code> or
	 *            <code>&lt;EnvironmentMatch&gt;</code>) to be matched against
	 *            the request.
	 * @param request
	 *            The requests containing the attributes to be matched against
	 *            the <code>&lt;Environments&gt;</code>.
	 * @param evaluationContext
	 *            The {@link EvaluationContext} containing additional
	 *            information.
	 * @return
	 * @throws ProcessingException
	 *             If an exception occurred while processing the matching
	 *             functions.
	 * @throws SyntaxException
	 *             If the request or the policy contained a syntax error.
	 * @throws MissingAttributeException
	 *             If a <i>must-be-present</i> attribute is missing.
	 */
	private boolean match(List<? extends Match> matches, RequestType request,
			EvaluationContext evaluationContext) throws ProcessingException,
			SyntaxException, MissingAttributeException {
		for (int i = 0; i < matches.size(); i++) {
			Match match = matches.get(i);
			Function matchFunction = match.getMatchFunction();
			logger.debug("Matching with function: {}", matchFunction);
			if (matchFunction == null) {
				String message = "Match function cannot be retrieved.";
				logger.error(message);
				throw new SyntaxException(message);
			}
			AttributeDesignatorType designator = match.getAttributeDesignator();
			List<?> requestAttributeValues = (List<?>) designator.handle(
					request, evaluationContext); // Fetches
			// all
			// AttributeValue-contents
			// from the element (element = subject,
			// resource, action or environment)
			// AttributeDesignator.

			// If the list returned by the designator is empty, than no more
			// processing is needed for this element (element = subject,
			// resource, action or environment) and the next subject will be
			// checked.
			//
			// This part references OASIS eXtensible Access Control Markup
			// Langugage (XACML) 2.0, Errata 29 January 2008
			// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
			// on page 84 (Match evaluation).
			if (requestAttributeValues.size() == 0) {
				logger.debug("Request did not contain the required attributes.");
				return false;
			}

			// Tells if a single match is true for a specific element (element =
			// subject, resource, action or environment).
			boolean matchMatches = false;

			for (int k = 0; k < requestAttributeValues.size(); k++) {
				Object requestAttributeValue = requestAttributeValues.get(k);
				// The attribute value specified in the matching element must be
				// supplied as the first argument.
				//
				// This part references OASIS eXtensible Access Control Markup
				// Langugage (XACML) 2.0, Errata 29 January 2008
				// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
				// on page 84 (Match evaluation).
				AttributeValueType policyAttributeValue = match
						.getAttributeValue();
				matchMatches = (Boolean) matchFunction.handle(
						policyAttributeValue.getDataType().convertTo(
								(String) policyAttributeValue.getContent().get(
										0)), requestAttributeValue);
				logger.debug(
						"Match function resulted in {} with policy attribute datatype:{} value:{} and request attribute value:{}",
						new Object[] { matchMatches,
								policyAttributeValue.getDataType(),
								policyAttributeValue.getContent().get(0),
								requestAttributeValue });

				// If the call of the match function (above) returns true for at
				// least one attribute value in the request
				// than the match is true and no more processing is needed
				// (therefore the break)
				//
				// This part references OASIS eXtensible Access Control Markup
				// Langugage (XACML) 2.0, Errata 29 January 2008
				// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
				// on page 84 (Match evaluation).
				if (matchMatches) {
					break;
				}
			}

			// All matches in element (element = subject, resource, action or
			// environment) must return true that the element
			// (element = subject, resource, action or environment) matches.
			// If the first match returned false than the whole element (element
			// = subject, resource, action or environment)
			// does not match the request.
			//
			// This part references OASIS eXtensible Access Control Markup
			// Langugage (XACML) 2.0, Errata 29 January 2008
			// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
			// on page 49 (5.7).
			if (!matchMatches) {
				return false;
			}
		}
		return true;
	}
}