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

package org.herasaf.xacml.core.simplePDP;

import org.herasaf.xacml.core.api.PIP;
import org.herasaf.xacml.core.api.PolicyRetrievalPoint;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyOnlyOneApplicableAlgorithm;
import org.herasaf.xacml.core.context.StatusCodeComparator;
import org.herasaf.xacml.core.targetMatcher.TargetMatcher;
import org.herasaf.xacml.core.targetMatcher.impl.TargetMatcherImpl;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a configuration container for configuring a {@link SimplePDP}.<br />
 * The configuration is customizable and default values can be overridden. Please read field level javadoc for
 * explanation of default values.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class SimplePDPConfiguration {
	private Logger logger = LoggerFactory.getLogger(SimplePDPConfiguration.class);

	/**
	 * The root {@link PolicyCombiningAlgorithm} to be used in the {@link SimplePDP} on which this
	 * {@link SimplePDPConfiguration} will be applied. <br>
	 * <b>Default value is:</b> a new instance of {@link PolicyOnlyOneApplicableAlgorithm}
	 */
	// default value is set in the getter, if needed.
	private PolicyCombiningAlgorithm rootCombiningAlgorithm;

	/**
	 * The {@link PolicyRetrievalPoint} to be used in the {@link SimplePDP} on which this {@link SimplePDPConfiguration}
	 * will be applied. <br>
	 * <b>Default value is:</b> a new instance of {@link MapBasedSimplePolicyRepository}
	 */
	// default value is set in the getter, if needed.
	private PolicyRetrievalPoint policyRetrievalPoint;

	/**
	 * The {@link PIP} to be used in the {@link SimplePDP} on which this {@link SimplePDPConfiguration} will be applied. <br>
	 * <b>Default value is:</b> {@code}null{@code}
	 */
	private PIP pip = null;

	/**
	 * The flag indicating if the {@link SimplePDP} on which this {@link SimplePDPConfiguration} will be applied should
	 * respect abandoned evaluatables.<br>
	 * <b>Default value is:</b> {@code}false{@code}
	 */
	private boolean respectAbandonedEvaluatables = false;

	/**
	 * The {@link TargetMatcher} implementation to be used in the {@link SimplePDP} on which this
	 * {@link SimplePDPConfiguration} will be applied. <br>
	 * <b>Default value is:</b> {@link TargetMatcherImpl}
	 */
	// default value is set in the getter, if needed.
	private TargetMatcher targetMatcher;

	/**
	 * The {@link StatusCodeComparator} to be used in the {@link SimplePDP} on which this {@link SimplePDPConfiguration}
	 * will be applied. <br>
	 * <b>Default value is:</b> {@link StatusCodeComparator}
	 */
	// default value is set in the getter, if needed.
	private StatusCodeComparator statusCodeComparator;

	/**
	 * The {@link DateTimeZone} to be used in the {@link SimplePDP} on which this {@link SimplePDPConfiguration} will be
	 * abblied. <b>Default value is GMT</b>.
	 */
	// default value is set in the getter, if needed.
	private DateTimeZone timeZone;

	/**
	 * @return The configured root {@link PolicyCombiningAlgorithm}
	 */
	public PolicyCombiningAlgorithm getRootCombiningAlgorithm() {
		if (rootCombiningAlgorithm != null) {
			logger.info("Using custom root combining algorithm: {}", rootCombiningAlgorithm.getClass()
					.getCanonicalName());
			return rootCombiningAlgorithm;
		}
		logger.info("Using default root combining algorithm: {}",
				PolicyOnlyOneApplicableAlgorithm.class.getCanonicalName());
		return new PolicyOnlyOneApplicableAlgorithm(); // default
	}

	/**
	 * @param RootCombiningAlgorithm
	 *            the rootCombiningAlgorithm to be applied in the configuration. If the setter is not called explicitly
	 *            the default configuration will be used.
	 */
	public void setRootCombiningAlgorithm(PolicyCombiningAlgorithm rootCombiningAlgorithm) {
		this.rootCombiningAlgorithm = rootCombiningAlgorithm;
	}

	/**
	 * @return The configured {@link PolicyRetrievalPoint}
	 */
	public PolicyRetrievalPoint getPolicyRetrievalPoint() {
		if (policyRetrievalPoint != null) {
			logger.info("Using custom policy retrieval point: {}", policyRetrievalPoint.getClass().getCanonicalName());
			return policyRetrievalPoint;
		}
		logger.info("Using default policy retrieval point: {}", MapBasedSimplePolicyRepository.class.getCanonicalName());
		return new MapBasedSimplePolicyRepository(); // default
	}

	/**
	 * @param PolicyRetrievalPoint
	 *            to be applied in the configuration. If the setter is not called explicitly the default configuration
	 *            will be used.
	 */
	public void setPolicyRetrievalPoint(PolicyRetrievalPoint policyRetrievalPoint) {
		this.policyRetrievalPoint = policyRetrievalPoint;
	}

	/**
	 * @return The configured {@link PIP}
	 */
	public PIP getPip() {
		return pip;
	}

	/**
	 * @param pip
	 *            The {@link PIP} to set.
	 */
	public void setPip(PIP pip) {
		this.pip = pip;
	}

	/**
	 * @return The configured boolean for respectAbandonedObligations behavior.
	 */
	public boolean isRespectAbandonedEvaluatables() {
		return respectAbandonedEvaluatables;
	}

	/**
	 * @param respectAbandonedEvaluatables
	 *            The respectAbandonedEvaluatables to be applied in the configuration. If the setter is not called
	 *            explicitly the default configuration will be used.
	 */
	public void setRespectAbandonedEvaluatables(boolean respectAbandonedEvaluatables) {
		this.respectAbandonedEvaluatables = respectAbandonedEvaluatables;
	}

	/**
	 * @return The configured {@link TargetMatcher}
	 */
	public TargetMatcher getTargetMatcher() {
		if (targetMatcher != null) {
			logger.info("Using custom target matcher: {}", targetMatcher.getClass().getCanonicalName());
			return targetMatcher;
		}
		logger.info("Using default target matcher: {}", TargetMatcherImpl.class.getCanonicalName());
		return new TargetMatcherImpl(); // default
	}

	/**
	 * @param targetMatcher
	 *            The targetMatcher to be applied in the configuration. If the setter is not called explicitly the
	 *            default configuration will be used.
	 */
	public void setTargetMatcher(TargetMatcher targetMatcher) {
		this.targetMatcher = targetMatcher;
	}

	/**
	 * @return The configured {@link StatusCodeComparator}
	 */
	public StatusCodeComparator getStatusCodeComparator() {
		if (statusCodeComparator != null) {
			logger.info("Using custom status code comparator: {}", statusCodeComparator.getClass().getCanonicalName());
			return statusCodeComparator;
		}
		logger.info("Using default status code comparator: {}", StatusCodeComparator.class.getCanonicalName());
		return new StatusCodeComparator(); // default
	}

	/**
	 * @param statusCodeComparator
	 *            The statusCodeComparator to be applied in the configuration. If the setter is not called explicitly
	 *            the default configuration will be used.
	 */
	public void setStatusCodeComparator(StatusCodeComparator statusCodeComparator) {
		this.statusCodeComparator = statusCodeComparator;
	}

	/**
	 * Allowed timezones to set are listed here http://joda-time.sourceforge.net/timezones.html.
	 * 
	 * @param timeZone
	 *            The id-string of the timezone that shall be default.
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = DateTimeZone.forID(timeZone);
	}

	/**
	 * The configured time zone to use or GMT as default.
	 * 
	 * @return The configured time zone.
	 */
	public DateTimeZone getTimeZone() {
		if (timeZone != null) {
			logger.info("Using custom timezone: {}", timeZone.getID());
			return timeZone;
		}
		logger.info("Using default timezone: {}", timeZone.getID());
		return DateTimeZone.forID("GMT");
	}
}