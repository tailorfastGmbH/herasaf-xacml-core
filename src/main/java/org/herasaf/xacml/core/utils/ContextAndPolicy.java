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

package org.herasaf.xacml.core.utils;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;

import org.herasaf.xacml.core.NotInitializedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO JAVADOC
 * 
 * Singleton which creates and configures the {@link Marshaller}s and
 * {@link Unmarshaller}s.
 * 
 * The marshaller and unmarshaller configuration is saved in
 * {@link ContextAndPolicyConfiguration} instances.
 * 
 * 
 * @author Stefan Oberholzer
 * @author Florian Huonder
 * @author René Eggenschwiler
 * @version 1.0
 */
public class ContextAndPolicy {
	private static final Logger logger = LoggerFactory.getLogger(ContextAndPolicy.class);

	/**
	 * This enum contains the possible {@link JAXBProfile}s.
	 * 
	 * @author Florian Huonder
	 * @version 1.0
	 */
	public enum JAXBProfile {
		/**
		 * Represents the policy profile should be loaded.
		 */
		POLICY,
		/**
		 * Represents the response profile should be loaded.
		 */
		RESPONSE_CTX,
		/**
		 * Represents the request profile should be loaded.
		 */
		REQUEST_CTX
	}

	private static ContextAndPolicyConfiguration policyProfile;
	private static ContextAndPolicyConfiguration requestCtxProfile;
	private static ContextAndPolicyConfiguration responseCtxProfile;
	private static ValidationEventHandler validationEventHandler;

	/**
	 * Returns the marshaller for the given context.
	 * 
	 * @param profile
	 *            The {@link JAXBProfile} of the {@link Marshaller}.
	 * @return The obtained marshaller.
	 * @throws JAXBException
	 */
	public static Marshaller getMarshaller(JAXBProfile profile) throws JAXBException {
		ContextAndPolicyConfiguration conf;
		switch (profile) {
		case POLICY:
			conf = policyProfile;
			break;
		case REQUEST_CTX:
			conf = requestCtxProfile;
			break;
		case RESPONSE_CTX:
		default:
			conf = responseCtxProfile;
		}

		Marshaller marshaller = conf.getContext().createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, conf.isFormatted_output());
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, conf.isFragment());

		if (conf.isWriteSchemaLocation()) {
			if (conf.getSchemaLocationAsString().equals("")) {
				logger.error("SchemaLocation not initialized.");
				throw new NotInitializedException("SchemaLocation not initialized.");
			}
			marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, conf.getSchemaLocationAsString());
		}
		if (conf.isValidateWriting()) {
			if (conf.getSchema() == null) {
				logger.error("Schema not initialized.");
				throw new NotInitializedException("Schema not initialized");
			}
			marshaller.setSchema(conf.getSchema());
		}
		if (validationEventHandler == null) {
			marshaller.setEventHandler(new DefaultValidationEventHandler());
		} else {
			marshaller.setEventHandler(validationEventHandler);
		}
		return marshaller;
	}

	/**
	 * Returns the unmarshaller for the given context.
	 * 
	 * @param profile
	 *            The {@link JAXBProfile} of the {@link Unmarshaller}.
	 * @return The obtained unmarshaller.
	 * @throws JAXBException
	 */
	public static Unmarshaller getUnmarshaller(JAXBProfile profile) throws JAXBException {
		ContextAndPolicyConfiguration conf;
		switch (profile) {
		case POLICY:
			conf = policyProfile;
			break;
		case REQUEST_CTX:
			conf = requestCtxProfile;
			break;
		case RESPONSE_CTX:
		default:
			conf = responseCtxProfile;
		}

		Unmarshaller unmarshaller = conf.getContext().createUnmarshaller();
		if (conf.isValidateParsing()) {
			unmarshaller.setSchema(conf.getSchema());
		}
		if (validationEventHandler == null) {
			unmarshaller.setEventHandler(new DefaultValidationEventHandler());
		} else {
			unmarshaller.setEventHandler(validationEventHandler);
		}
		return unmarshaller;
	}

	/**
	 * Returns the {@link ContextAndPolicyConfiguration} of the policy profile.
	 * 
	 * @return The {@link ContextAndPolicyConfiguration} of the policy profile.
	 */
	public static ContextAndPolicyConfiguration getPolicyProfile() {
		return policyProfile;
	}

	/**
	 * Sets the {@link ContextAndPolicyConfiguration} for the policy profile.
	 * 
	 * @param policyProfile
	 *            The {@link ContextAndPolicyConfiguration} for the policy
	 *            profile.
	 */
	public static void setPolicyProfile(ContextAndPolicyConfiguration policyProfile) {
		ContextAndPolicy.policyProfile = policyProfile;
	}

	/**
	 * Returns the {@link ContextAndPolicyConfiguration} of the request profile.
	 * 
	 * @return The {@link ContextAndPolicyConfiguration} of the request profile.
	 */
	public static ContextAndPolicyConfiguration getRequestCtxProfile() {
		return requestCtxProfile;
	}

	/**
	 * Sets the {@link ContextAndPolicyConfiguration} for the request profile.
	 * 
	 * @param requestCtxProfile
	 *            The {@link ContextAndPolicyConfiguration} for the request
	 *            profile.
	 */
	public static void setRequestCtxProfile(ContextAndPolicyConfiguration requestCtxProfile) {
		ContextAndPolicy.requestCtxProfile = requestCtxProfile;
	}

	/**
	 * Returns the {@link ContextAndPolicyConfiguration} of the response
	 * profile.
	 * 
	 * @return The {@link ContextAndPolicyConfiguration} of the response
	 *         profile.
	 */
	public static ContextAndPolicyConfiguration getResponseCtxProfile() {
		return responseCtxProfile;
	}

	/**
	 * Sets the {@link ContextAndPolicyConfiguration} for the response profile.
	 * 
	 * @param responseCtxProfile
	 *            The {@link ContextAndPolicyConfiguration} for the response
	 *            profile.
	 */
	public static void setResponseCtxProfile(ContextAndPolicyConfiguration responseCtxProfile) {
		ContextAndPolicy.responseCtxProfile = responseCtxProfile;
	}

	/**
	 * Set a {@link ValidationEventHandler} for JAXB.
	 * 
	 * @param validationEventHandler
	 *            The {@link ValidationEventHandler} to set.
	 */
	public static void setValidationEventHandler(ValidationEventHandler validationEventHandler) {
		ContextAndPolicy.validationEventHandler = validationEventHandler;
	}
}