/*
 * Copyright 2009 - 2012 HERAS-AF (www.herasaf.org)
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
package org.herasaf.xacml.core.simplePDP.initializers.jaxb;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import org.herasaf.xacml.core.InitializationException;
import org.herasaf.xacml.core.context.RequestMarshaller;
import org.herasaf.xacml.core.context.ResponseMarshaller;
import org.herasaf.xacml.core.policy.PolicyMarshaller;
import org.herasaf.xacml.core.simplePDP.SimplePDPConfiguration;
import org.herasaf.xacml.core.simplePDP.initializers.api.Initializer;
import org.herasaf.xacml.core.utils.JAXBMarshallerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * This initializer initializes all JAXB basics. The request context, the response context and the policy context are initialized. Further a
 * default JAXB configuration is initialized.
 *
 *
 *
 * The default values of the configuration are (see JAXB supported properties
 * https://jaxb.dev.java.net/nonav/2.2/docs/api/javax/xml/bind/Marshaller.html):
 * <ul>
 * <li>jaxb.fragment = true</li>
 * <li>jaxb.formatted.output = true</li>
 * <li>validation for Request, Response and Policy is enabled</li>
 * </ul>
 *
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class JaxbContextInitializer implements Initializer {

    private static final Logger logger = LoggerFactory.getLogger(JaxbContextInitializer.class);

    private static final String CONTEXT_SCHEMA_PATH = "classpath:/access_control-xacml-2.0-context-schema-os.xsd";
    private static final String POLICY_SCHEMA_PATH = "classpath:/access_control-xacml-2.0-policy-schema-os.xsd";

    // jaxb default settings (by HERAS-AF)
    private static final boolean JAXB_FRAGMENT = true;
    private static final boolean JAXB_VALIDATE = false;
    private static final boolean JAXB_VALIDATE_WRITING = false;
    private static final boolean JAXB_FORMATTED_OUTPUT = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(SimplePDPConfiguration configuration) {
        initializePolicyContext(configuration);
        initializeRequestContext(configuration);
        initializeResponseContext(configuration);
    }

    /**
     * Initializes the default policy context that contains all standard XACML policy types and sets the schema for validation.
     */
    private void initializePolicyContext(SimplePDPConfiguration configuration) {

        // Create and set JAXB Context for Policy

        JAXBContext policyContext;
        try {
            List<Class<?>> contextClasses = new ArrayList<Class<?>>();
            contextClasses.add(org.herasaf.xacml.core.policy.impl.ObjectFactory.class);
            if (configuration != null) {
                contextClasses.addAll(configuration.getJaxbContexts());
            }

            policyContext = JAXBContext.newInstance(contextClasses.toArray(new Class[0]));
        } catch (JAXBException e) {
            InitializationException ie = new InitializationException("Unable to load JAXBContext for org.herasaf.xacml.core.policy.impl.",
                    e);
            logger.error(ie.getMessage());
            throw ie;
        }

        PolicyMarshaller.setJAXBContext(policyContext);

        // Create and set JAXB Marshaller Configuration for Policy

        JAXBMarshallerConfiguration jmc = getCommonMarshallerConfiguration();
        try {
            jmc.setSchemaByPath(POLICY_SCHEMA_PATH);
        } catch (SAXException e) {
            InitializationException ie = new InitializationException("Unable to load Schema " + POLICY_SCHEMA_PATH, e);
            logger.error(ie.getMessage());
            throw ie;
        } catch (MalformedURLException e) {
            InitializationException ie = new InitializationException("Unable to load Schema " + POLICY_SCHEMA_PATH, e);
            logger.error(ie.getMessage());
            throw ie;
        }
        PolicyMarshaller.setJAXBMarshallerConfiguration(jmc);
        logger.info("PolicyMarshaller is configured with JAXB Marshaller Configuration {}", jmc.toString());
    }

    /**
     * Initializes the default request context that contains all standard XACML context types and sets the schema for validation.
     */
    private void initializeRequestContext(SimplePDPConfiguration configuration) {

        // Create and set JAXB Context for Request

        JAXBContext requestContext;
        try {
            List<Class<?>> contextClasses = new ArrayList<Class<?>>();
            contextClasses.add(org.herasaf.xacml.core.context.impl.ObjectFactory.class);
            if (configuration != null) {
                contextClasses.addAll(configuration.getJaxbContexts());
            }

            requestContext = JAXBContext.newInstance(contextClasses.toArray(new Class[0]));
        } catch (JAXBException e) {
            InitializationException ie = new InitializationException("Unable to load JAXBContext for org.herasaf.xacml.core.context.impl.",
                    e);
            logger.error(ie.getMessage());
            throw ie;
        }

        RequestMarshaller.setJAXBContext(requestContext);

        // Create and set JAXB Marshaller Configuration for Request

        JAXBMarshallerConfiguration jmc = getCommonMarshallerConfiguration();
        try {
            jmc.setSchemaByPath(POLICY_SCHEMA_PATH, CONTEXT_SCHEMA_PATH);
        } catch (SAXException e) {
            InitializationException ie = new InitializationException("Unable to load Schema " + CONTEXT_SCHEMA_PATH, e);
            logger.error(ie.getMessage());
            throw ie;
        } catch (MalformedURLException e) {
            InitializationException ie = new InitializationException("Unable to load Schema " + CONTEXT_SCHEMA_PATH, e);
            logger.error(ie.getMessage());
            throw ie;
        }
        RequestMarshaller.setJAXBMarshallerConfiguration(jmc);
        logger.info("RequestMarshaller is configured with JAXB Marshaller Configuration {}", jmc.toString());
    }

    /**
     * Initializes the default response context that contains all standard XACML context types and sets the schema for validation.
     */
    private void initializeResponseContext(SimplePDPConfiguration configuration) {

        // Create and set JAXB Context for Response
        JAXBContext responseContext;
        try {
            List<Class<?>> contextClasses = new ArrayList<Class<?>>();
            contextClasses.add(org.herasaf.xacml.core.context.impl.ObjectFactory.class);
            if (configuration != null) {
                contextClasses.addAll(configuration.getJaxbContexts());
            }

            responseContext = JAXBContext.newInstance(contextClasses.toArray(new Class[0]));
        } catch (JAXBException e) {
            InitializationException ie = new InitializationException("Unable to load JAXBContext for org.herasaf.xacml.core.context.impl.",
                    e);
            logger.error(ie.getMessage());
            throw ie;
        }

        ResponseMarshaller.setJAXBContext(responseContext);

        // Create and set JAXB Marshaller Configuration for Response

        JAXBMarshallerConfiguration jmc = getCommonMarshallerConfiguration();
        try {
            jmc.setSchemaByPath(POLICY_SCHEMA_PATH, CONTEXT_SCHEMA_PATH);
        } catch (SAXException e) {
            InitializationException ie = new InitializationException("Unable to load Schema " + CONTEXT_SCHEMA_PATH, e);
            logger.error(ie.getMessage());
            throw ie;
        } catch (MalformedURLException e) {
            InitializationException ie = new InitializationException("Unable to load Schema " + CONTEXT_SCHEMA_PATH, e);
            logger.error(ie.getMessage());
            throw ie;
        }
        ResponseMarshaller.setJAXBMarshallerConfiguration(jmc);
        logger.info("ResponseMarshaller is configured with JAXB Marshaller Configuration {}", jmc.toString());
    }

    /**
     * Creates a JAXB default configuration that is set on all JAXB components within HERAS-AF by default.
     * 
     * @return The common {@link JAXBMarshallerConfiguration}.
     */
    private JAXBMarshallerConfiguration getCommonMarshallerConfiguration() {
        JAXBMarshallerConfiguration jmc = new JAXBMarshallerConfiguration();
        jmc.setFormattedOutput(JAXB_FORMATTED_OUTPUT);
        jmc.setFragment(JAXB_FRAGMENT);
        jmc.setValidateParsing(JAXB_VALIDATE);
        jmc.setValidateWriting(JAXB_VALIDATE_WRITING);
        return jmc;

    }

}
