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

public class JAXBInitializer implements Initializer {
	private final Logger logger = LoggerFactory
			.getLogger(JAXBInitializer.class);

	// jaxb default settings (by HERAS-AF)
	private static final boolean JAXB_FRAGMENT = true;
	private static final boolean JAXB_VALIDATE = false;
	private static final boolean JAXB_VALIDATE_WRITING = false;
	private static final boolean JAXB_FORMATTED_OUTPUT = true;

	public void run() {
		initializePolicyContext();
		initializeRequestContext();
		initializeResponseContext();
		initializeMarshallerConfiguration();
	}

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
