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

package org.herasaf.xacml.core.simplePDP.initializers;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.RequestCtxFactory;
import org.herasaf.xacml.core.context.ResponseCtx;
import org.herasaf.xacml.core.context.ResponseCtxFactory;
import org.herasaf.xacml.core.policy.PolicyMarshaller;
import org.herasaf.xacml.core.simplePDP.InitializationException;
import org.herasaf.xacml.core.utils.JAXBMarshallerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This initializer initializes all JAXB basics. The request context, the
 * response context and the policy context are initialized. Further a default
 * JAXB configuration is initialized.
 * 
 * @author Florian Huonder
 */
public class JAXBInitializer implements Initializer {
	private final Logger logger = LoggerFactory
			.getLogger(JAXBInitializer.class);

	// jaxb default settings (by HERAS-AF)
	private static final boolean JAXB_FRAGMENT = true;
	private static final boolean JAXB_VALIDATE = false;
	private static final boolean JAXB_VALIDATE_WRITING = false;
	private static final boolean JAXB_FORMATTED_OUTPUT = true;

	/**
	 * {@inheritDoc}
	 */
	public void run() {
		initializePolicyContext();
		initializeRequestContext();
		initializeResponseContext();
		initializeMarshallerConfiguration();
	}

	/**
	 * Initializes the default policy context the contains all standard XACML
	 * policy types.
	 */
	private void initializePolicyContext() {
		JAXBContext policyContext;
		try {
			policyContext = JAXBContext
					.newInstance("org.herasaf.xacml.core.policy.impl");
		} catch (JAXBException e) {
			InitializationException ie = new InitializationException(
					"Unable to load JAXBContext for org.herasaf.xacml.core.policy.impl.",
					e);
			logger.error(ie.getMessage());
			throw ie;
		}

		PolicyMarshaller.setJAXBContext(policyContext);
	}

	/**
	 * Initializes the default "context" context the contains all standard XACML
	 * context types.
	 */
	private void initializeRequestContext() {
		JAXBContext requestContext;
		try {
			requestContext = JAXBContext
					.newInstance("org.herasaf.xacml.core.context.impl");
		} catch (JAXBException e) {
			InitializationException ie = new InitializationException(
					"Unable to load JAXBContext for org.herasaf.xacml.core.context.impl.",
					e);
			logger.error(ie.getMessage());
			throw ie;
		}

		RequestCtxFactory.setJAXBContext(requestContext);
		RequestCtx.setJAXBContext(requestContext);
	}

	/**
	 * Initializes the default "context" context the contains all standard XACML
	 * context types.
	 */
	private void initializeResponseContext() {
		JAXBContext responseContext;
		try {
			responseContext = JAXBContext
					.newInstance("org.herasaf.xacml.core.context.impl");
		} catch (JAXBException e) {
			InitializationException ie = new InitializationException(
					"Unable to load JAXBContext for org.herasaf.xacml.core.context.impl.",
					e);
			logger.error(ie.getMessage());
			throw ie;
		}

		ResponseCtxFactory.setJAXBContext(responseContext);
		ResponseCtx.setJAXBContext(responseContext);
	}

	/**
	 * Creates a JAXB default configuration that is set on all JAXB components
	 * within HERAS-AF by default.
	 */
	private void initializeMarshallerConfiguration() {
		JAXBMarshallerConfiguration jmc = new JAXBMarshallerConfiguration();
		jmc.setFormattedOutput(JAXB_FORMATTED_OUTPUT);
		jmc.setFragment(JAXB_FRAGMENT);
		jmc.setValidateParsing(JAXB_VALIDATE);
		jmc.setValidateWriting(JAXB_VALIDATE_WRITING);

		PolicyMarshaller.setJAXBMarshallerConfiguration(jmc);
		ResponseCtxFactory.setJAXBMarshallerConfiguration(jmc);
		ResponseCtx.setJAXBMarshallerConfiguration(jmc);
		RequestCtxFactory.setJAXBMarshallerConfiguration(jmc);
		RequestCtx.setJAXBMarshallerConfiguration(jmc);
	}
}
