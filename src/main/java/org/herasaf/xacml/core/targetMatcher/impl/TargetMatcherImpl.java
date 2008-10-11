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

package org.herasaf.xacml.core.targetMatcher.impl;

import java.util.List;

import org.herasaf.xacml.SyntaxException;
import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.policy.impl.ActionType;
import org.herasaf.xacml.core.policy.impl.ActionsType;
import org.herasaf.xacml.core.policy.impl.AttributeDesignatorType;
import org.herasaf.xacml.core.policy.impl.EnvironmentType;
import org.herasaf.xacml.core.policy.impl.EnvironmentsType;
import org.herasaf.xacml.core.policy.impl.Match;
import org.herasaf.xacml.core.policy.impl.ResourceType;
import org.herasaf.xacml.core.policy.impl.ResourcesType;
import org.herasaf.xacml.core.policy.impl.SubjectType;
import org.herasaf.xacml.core.policy.impl.SubjectsType;
import org.herasaf.xacml.core.policy.impl.TargetType;
import org.herasaf.xacml.core.targetMatcher.TargetMatcher;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link TargetMatcher} interface.
 *
 * @author Florian Huonder
 * @version 1.0
 */
@Component
public class TargetMatcherImpl implements TargetMatcher {
	/**
	 *
	 */
	private static final long serialVersionUID = 9099144198373918560L;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.herasaf.core.targetMatcher.TargetMatcher#match(org.herasaf.core.context.impl.RequestType,
	 *      org.herasaf.core.policy.impl.TargetType)
	 */
	public boolean match(RequestType request, TargetType target, RequestInformation reqInfo)
			throws SyntaxException, ProcessingException, MissingAttributeException {
		if (target != null) {
			boolean subjectsMatches = subjectsMatch(target.getSubjects(),
					request, reqInfo);
			boolean resourcesMatches = resourcesMatch(target.getResources(),
					request, reqInfo);
			boolean actionsMatches = actionMatch(target.getActions(), request, reqInfo);
			boolean environmentsMatches = environmentMatch(target
					.getEnvironments(), request, reqInfo);

			// Each of the elements subjects, resources, actions and
			// environments
			// must return true that the target matches.
			//
			// This part references OASIS eXtensible Access Control Markup
			// Langugage
			// (XACML) 2.0, Errata 29 June 2006
			// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
			// on page 45 (5.5).
			return subjectsMatches && resourcesMatches && actionsMatches
					&& environmentsMatches;
		}
		return true;
	}

	private boolean subjectsMatch(SubjectsType subjects, RequestType request, RequestInformation reqInfo)
			throws ProcessingException, SyntaxException, MissingAttributeException {
		if (subjects == null) {
			return true;
		}
		for (int i = 0; i < subjects.getSubjects().size(); i++) {
			SubjectType targetSubject = subjects.getSubjects().get(i);
			boolean matches = match(targetSubject.getSubjectMatches(), request, reqInfo);
			if (matches) {
				// If one subject matches the subjects matches and returns true.
				//
				// This part references OASIS eXtensible Access Control Markup
				// Langugage (XACML) 2.0, Errata 29 June 2006
				// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
				// on page 46 (5.6).
				return true;
			}
		}
		return false;
	}

	private boolean resourcesMatch(ResourcesType resources, RequestType request, RequestInformation reqInfo)
			throws ProcessingException, SyntaxException, MissingAttributeException {
		if (resources == null) {
			return true;
		}
		for (int i = 0; i < resources.getResources().size(); i++) {
			ResourceType targetResource = resources.getResources().get(i);
			boolean matches = match(targetResource.getResourceMatches(),
					request, reqInfo);
			if (matches) {
				// If one resource matches the resources matches and returns
				// true.
				//
				// This part references OASIS eXtensible Access Control Markup
				// Langugage (XACML) 2.0, Errata 29 June 2006
				// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
				// on page 47 (5.9).
				return true;
			}
		}
		return false;
	}

	private boolean actionMatch(ActionsType actions, RequestType request, RequestInformation reqInfo)
			throws ProcessingException, SyntaxException, MissingAttributeException {
		if (actions == null) {
			return true;
		}
		for (int i = 0; i < actions.getActions().size(); i++) {
			ActionType targetAction = actions.getActions().get(i);
			boolean matches = match(targetAction.getActionMatches(), request, reqInfo);
			if (matches) {
				// If one action matches the actions matches and returns true.
				//
				// This part references OASIS eXtensible Access Control Markup
				// Langugage (XACML) 2.0, Errata 29 June 2006
				// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
				// on page 48 (5.12).
				return true;
			}
		}
		return false;
	}

	private boolean environmentMatch(EnvironmentsType environments,
			RequestType request, RequestInformation reqInfo) throws ProcessingException, SyntaxException, MissingAttributeException {
		if (environments == null) {
			return true;
		}
		for (int i = 0; i < environments.getEnvironments().size(); i++) {
			EnvironmentType targetEnvironment = environments.getEnvironments()
					.get(i);
			boolean matches = match(targetEnvironment.getEnvironmentMatches(),
					request, reqInfo);
			if (matches) {
				// If one environment matches the environments matches and
				// returns true.
				//
				// This part references OASIS eXtensible Access Control Markup
				// Langugage (XACML) 2.0, Errata 29 June 2006
				// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
				// on page 50 (5.15).
				return true;
			}
		}
		return false;
	}

	private boolean match(List<? extends Match> matches, RequestType request, RequestInformation reqInfo)
			throws ProcessingException, SyntaxException, MissingAttributeException {
		for (int i = 0; i < matches.size(); i++) {
			Match match = matches.get(i);
			Function matchFunction = match.getMatchFunction();
			AttributeDesignatorType designator = match.getAttributeDesignator();
			List<?> requestAttributeValues = (List<?>) designator
					.handle(request, reqInfo); // Fetches all AttributeValue-contents
			// from the element (element = subject,
			// resource, action or environment)
			// AttributeDesignator.

			// If the list returned by the designator is empty, than no more
			// processing is needed for this element (element = subject,
			// resource, action or environment) and the next subject will be
			// checked.
			//
			// This part references OASIS eXtensible Access Control Markup
			// Langugage (XACML) 2.0, Errata 29 June 2006
			// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
			// on page 79 (Match evaluation, line 3394).
			if (requestAttributeValues.size() == 0) {
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
				// Langugage (XACML) 2.0, Errata 29 June 2006
				// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
				// on page 79 (Match evaluation, line 3371).
				matchMatches = (Boolean) matchFunction.handle(designator
						.getDataType().convertTo(
								(String) match.getAttributeValue().getContent()
										.get(0)), requestAttributeValue);

				// If the call of the match function (above) returns true for at
				// least one attribute value in the request
				// than the match is true and no more processing is needed
				// (therefore the break)
				//
				// This part references OASIS eXtensible Access Control Markup
				// Langugage (XACML) 2.0, Errata 29 June 2006
				// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
				// on page 79 (Match evaluation, line 3398).
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
			// Langugage (XACML) 2.0, Errata 29 June 2006
			// (http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20)
			// on page 46 (5.7).
			if (!matchMatches) {
				return false;
			}
		}
		return true;
	}
}