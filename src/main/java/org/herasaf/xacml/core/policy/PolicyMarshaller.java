/*
 * Copyright 2008-2010 HERAS-AF (www.herasaf.org)
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
package org.herasaf.xacml.core.policy;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.herasaf.xacml.core.NotInitializedException;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.WritingException;
import org.herasaf.xacml.core.policy.impl.ObjectFactory;
import org.herasaf.xacml.core.policy.impl.PolicySetType;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.herasaf.xacml.core.utils.DefaultValidationEventHandler;
import org.herasaf.xacml.core.utils.JAXBMarshallerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;

/**
 * This utility class can be used to marshal and unmarshal {@link Evaluatable}s.
 * Provides various unmarshal methods to create a ResponseCtx. Because the
 * {@link Unmarshaller} from JAXB <b>is not</b> thread safe it must be created
 * in each unmarshal-method. This class fully relies on the underlying JAXB
 * implementation.
 * 
 * @author Florian Huonder
 */
public final class PolicyMarshaller {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PolicyMarshaller.class);
	private static JAXBContext CONTEXT;
	private static JAXBMarshallerConfiguration CONFIGURATION;
	private static final ObjectFactory OBJECT_FACTORY;

	/**
	 * Initializes the object factory.
	 */
	static {
		OBJECT_FACTORY = new ObjectFactory();
	}

	public static void setJAXBContext(JAXBContext policyContext) {
		CONTEXT = policyContext;
	}

	public static void setJAXBMarshallerConfiguration(
			JAXBMarshallerConfiguration jmc) {
		CONFIGURATION = jmc;
	}

	/**
	 * A utility class must not be instantiated.
	 */
	private PolicyMarshaller() {

	}

	/**
	 * This method creates a new JAXB marshaller. For each request a new
	 * marshaller is created due to the fact that JAXB is not thread-safe.
	 * 
	 * @return The newly created JAXB marshaller.
	 * @throws JAXBException
	 * @throws PropertyException
	 */
	private static Marshaller createMarshaller() throws JAXBException,
			PropertyException {

		if (CONTEXT == null || CONFIGURATION == null) {
			throw new NotInitializedException(
					"JAXB Context and/or Configuration not initialized.");
		}

		Marshaller marshaller = CONTEXT.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, CONFIGURATION
				.isFormattedOutput());
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, CONFIGURATION
				.isFragment());

		if (CONFIGURATION.isWriteSchemaLocation()) {
			if ("".equals(CONFIGURATION)) {
				LOGGER.error("SchemaLocation not initialized.");
				throw new NotInitializedException(
						"SchemaLocation not initialized.");
			}
			marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
					CONFIGURATION.getSchemaLocationAsString());
		}
		if (CONFIGURATION.isValidateWriting()) {
			if (CONFIGURATION.getSchema() == null) {
				LOGGER.error("Schema not initialized.");
				throw new NotInitializedException("Schema not initialized");
			}
			marshaller.setSchema(CONFIGURATION.getSchema());
		}
		if (CONFIGURATION.getValidationEventHandler() == null) {
			marshaller.setEventHandler(new DefaultValidationEventHandler());
		} else {
			marshaller.setEventHandler(CONFIGURATION
					.getValidationEventHandler());
		}
		return marshaller;
	}

	/**
	 * This method creates a new JAXB unmarshaller. For each request a new
	 * unmarshaller is created due to the fact that JAXB is not thread-safe.
	 * 
	 * @return The newly created JAXB unmarshaller.
	 * @throws JAXBException
	 * @throws PropertyException
	 */
	private static Unmarshaller createUnmarshaller() throws JAXBException,
			PropertyException {

		if (CONTEXT == null || CONFIGURATION == null) {
			throw new NotInitializedException(
					"JAXB Context and/or Configuration not initialized.");
		}

		Unmarshaller unmarshaller = CONTEXT.createUnmarshaller();

		if (CONFIGURATION.isValidateParsing()) {
			if (CONFIGURATION.getSchema() == null) {
				LOGGER.error("Schema not initialized.");
				throw new NotInitializedException("Schema not initialized");
			}
			unmarshaller.setSchema(CONFIGURATION.getSchema());
		}
		if (CONFIGURATION.getValidationEventHandler() == null) {
			unmarshaller.setEventHandler(new DefaultValidationEventHandler());
		} else {
			unmarshaller.setEventHandler(CONFIGURATION
					.getValidationEventHandler());
		}
		return unmarshaller;
	}

	/**
	 * Marshals the given {@link Evaluatable} to the given
	 * {@link ContentHandler}.
	 * 
	 * @param ch
	 *            The {@link ContentHandler} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshal.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(Evaluatable evaluatable, ContentHandler ch)
			throws WritingException {
		Marshaller marshaller;
		try {
			marshaller = createMarshaller();
		} catch (PropertyException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}

		try {
			if (evaluatable instanceof PolicySetType) {

				marshaller.marshal(OBJECT_FACTORY
						.createPolicySet((PolicySetType) evaluatable), ch);
			} else if (evaluatable instanceof PolicyType) {
				marshaller.marshal(OBJECT_FACTORY
						.createPolicy((PolicyType) evaluatable), ch);
			} else {
				WritingException e = new WritingException(
						"Unable to marshal an object of type: "
								+ evaluatable.getClass());
				LOGGER.error(e.getMessage());
				throw e;
			}

		} catch (JAXBException e) {
			WritingException ie = new WritingException(
					"Unable to write to the context handler.", e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}
	}

	/**
	 * Marshals the given {@link Evaluatable} to the given {@link File}.
	 * 
	 * @param file
	 *            The {@link File} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshal.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(Evaluatable evaluatable, File file)
			throws WritingException {
		Marshaller marshaller;
		try {
			marshaller = createMarshaller();
		} catch (PropertyException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}

		try {
			if (evaluatable instanceof PolicySetType) {
				marshaller.marshal(OBJECT_FACTORY
						.createPolicySet((PolicySetType) evaluatable), file);
			} else if (evaluatable instanceof PolicyType) {
				marshaller.marshal(OBJECT_FACTORY
						.createPolicy((PolicyType) evaluatable), file);
			} else {
				WritingException e = new WritingException(
						"Unable to marshal an object of type: "
								+ evaluatable.getClass());
				LOGGER.error(e.getMessage());
				throw e;
			}

		} catch (JAXBException e) {
			WritingException ie = new WritingException(
					"Unable to write to the file.", e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}
	}

	/**
	 * Marshals the given {@link Evaluatable} to the given {@link Result}.
	 * 
	 * @param result
	 *            The {@link Result} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshal.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(Evaluatable evaluatable, Result result)
			throws WritingException {
		Marshaller marshaller;
		try {
			marshaller = createMarshaller();
		} catch (PropertyException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}

		try {
			if (evaluatable instanceof PolicySetType) {
				marshaller.marshal(OBJECT_FACTORY
						.createPolicySet((PolicySetType) evaluatable), result);
			} else if (evaluatable instanceof PolicyType) {
				marshaller.marshal(OBJECT_FACTORY
						.createPolicy((PolicyType) evaluatable), result);
			} else {
				WritingException e = new WritingException(
						"Unable to marshal an object of type: "
								+ evaluatable.getClass());
				LOGGER.error(e.getMessage());
				throw e;
			}

		} catch (JAXBException e) {
			WritingException ie = new WritingException(
					"Unable to write to the result.", e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}
	}

	/**
	 * Marshals the given {@link Evaluatable} to the given {@link OutputStream}.
	 * 
	 * @param out
	 *            The {@link OutputStream} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshal.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(Evaluatable evaluatable, OutputStream out)
			throws WritingException {
		Marshaller marshaller;
		try {
			marshaller = createMarshaller();
		} catch (PropertyException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}

		try {
			if (evaluatable instanceof PolicySetType) {
				marshaller.marshal(OBJECT_FACTORY
						.createPolicySet((PolicySetType) evaluatable), out);
			} else if (evaluatable instanceof PolicyType) {
				marshaller.marshal(OBJECT_FACTORY
						.createPolicy((PolicyType) evaluatable), out);
			} else {
				WritingException e = new WritingException(
						"Unable to marshal an object of type: "
								+ evaluatable.getClass());
				LOGGER.error(e.getMessage());
				throw e;
			}

		} catch (JAXBException e) {
			WritingException ie = new WritingException(
					"Unable to write to the output stream.", e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}
	}

	/**
	 * Marshals the given {@link Evaluatable} to the given {@link Writer}.
	 * 
	 * @param writer
	 *            The {@link Writer} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshal.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(Evaluatable evaluatable, Writer writer)
			throws WritingException {
		Marshaller marshaller;
		try {
			marshaller = createMarshaller();
		} catch (PropertyException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}

		try {
			if (evaluatable instanceof PolicySetType) {
				marshaller.marshal(OBJECT_FACTORY
						.createPolicySet((PolicySetType) evaluatable), writer);
			} else if (evaluatable instanceof PolicyType) {
				marshaller.marshal(OBJECT_FACTORY
						.createPolicy((PolicyType) evaluatable), writer);
			} else {
				WritingException e = new WritingException(
						"Unable to marshal an object of type: "
								+ evaluatable.getClass());
				LOGGER.error(e.getMessage());
				throw e;
			}

		} catch (JAXBException e) {
			WritingException ie = new WritingException(
					"Unable to write to the writer.", e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}
	}

	/**
	 * Marshals the given {@link Evaluatable} to the given {@link Node}.
	 * 
	 * @param node
	 *            The {@link Node} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshal.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(Evaluatable evaluatable, Node node)
			throws WritingException {
		Marshaller marshaller;
		try {
			marshaller = createMarshaller();
		} catch (PropertyException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}

		try {
			if (evaluatable instanceof PolicySetType) {
				marshaller.marshal(OBJECT_FACTORY
						.createPolicySet((PolicySetType) evaluatable), node);
			} else if (evaluatable instanceof PolicyType) {
				marshaller.marshal(OBJECT_FACTORY
						.createPolicy((PolicyType) evaluatable), node);
			} else {
				WritingException e = new WritingException(
						"Unable to marshal an object of type: "
								+ evaluatable.getClass());
				LOGGER.error(e.getMessage());
				throw e;
			}

		} catch (JAXBException e) {
			WritingException ie = new WritingException(
					"Unable to write to the node.", e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}
	}

	/**
	 * Marshals the given {@link Evaluatable} to the given
	 * {@link XMLStreamWriter}.
	 * 
	 * @param xmlStreamWriter
	 *            The {@link XMLStreamWriter} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshal.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(Evaluatable evaluatable,
			XMLStreamWriter xmlStreamWriter) throws WritingException {
		Marshaller marshaller;
		try {
			marshaller = createMarshaller();
		} catch (PropertyException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}

		try {
			if (evaluatable instanceof PolicySetType) {
				marshaller.marshal(OBJECT_FACTORY
						.createPolicySet((PolicySetType) evaluatable),
						xmlStreamWriter);
			} else if (evaluatable instanceof PolicyType) {
				marshaller.marshal(OBJECT_FACTORY
						.createPolicy((PolicyType) evaluatable),
						xmlStreamWriter);
			} else {
				WritingException e = new WritingException(
						"Unable to marshal an object of type: "
								+ evaluatable.getClass());
				LOGGER.error(e.getMessage());
				throw e;
			}

		} catch (JAXBException e) {
			WritingException ie = new WritingException(
					"Unable to write to the xml stream writer.", e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}
	}

	/**
	 * Marshals the given {@link Evaluatable} to the given
	 * {@link XMLEventWriter}.
	 * 
	 * @param xmlEventWriter
	 *            The {@link XMLEventWriter} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshal.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(Evaluatable evaluatable,
			XMLEventWriter xmlEventWriter) throws WritingException {
		Marshaller marshaller;
		try {
			marshaller = createMarshaller();
		} catch (PropertyException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to create a JAXB Marshaller.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}

		try {
			if (evaluatable instanceof PolicySetType) {
				marshaller.marshal(OBJECT_FACTORY
						.createPolicySet((PolicySetType) evaluatable),
						xmlEventWriter);
			} else if (evaluatable instanceof PolicyType) {
				marshaller
						.marshal(OBJECT_FACTORY
								.createPolicy((PolicyType) evaluatable),
								xmlEventWriter);
			} else {
				WritingException e = new WritingException(
						"Unable to marshal an object of type: "
								+ evaluatable.getClass());
				LOGGER.error(e.getMessage());
				throw e;
			}

		} catch (JAXBException e) {
			WritingException ie = new WritingException(
					"Unable to write to the xml event writer.", e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}
	}

	/**
	 * Creates an {@link Evaluatable} from the given {@link File}.
	 * 
	 * @param file
	 *            The {@link File} from which the {@link Evaluatable} is
	 *            created.
	 * @return The created {@link Evaluatable}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(File file) throws SyntaxException {
		Unmarshaller unmarshaller;
		try {
			unmarshaller = createUnmarshaller();
		} catch (PropertyException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		}

		try {
			return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(file))
					.getValue();
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the file.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the file.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		}
	}

	/**
	 * Creates an {@link Evaluatable} from the given {@link InputStream}.
	 * 
	 * @param inputStream
	 *            The {@link InputStream} from which the {@link Evaluatable} is
	 *            created.
	 * @return The created {@link Evaluatable}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(InputStream inputStream)
			throws SyntaxException {
		Unmarshaller unmarshaller;
		try {
			unmarshaller = createUnmarshaller();
		} catch (PropertyException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		}

		try {
			return ((JAXBElement<Evaluatable>) unmarshaller
					.unmarshal(inputStream)).getValue();
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the input stream.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the input stream.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		}
	}

	/**
	 * Creates an {@link Evaluatable} from the given {@link Reader}.
	 * 
	 * @param reader
	 *            The {@link Reader} from which the {@link Evaluatable} is
	 *            created.
	 * @return The created {@link Evaluatable}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(Reader reader) throws SyntaxException {
		Unmarshaller unmarshaller;
		try {
			unmarshaller = createUnmarshaller();
		} catch (PropertyException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		}

		try {
			return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(reader))
					.getValue();
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the reader.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the reader.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		}
	}

	/**
	 * Creates an {@link Evaluatable} from the given {@link URL}.
	 * 
	 * @param url
	 *            The {@link URL} from which the {@link Evaluatable} is created.
	 * @return The created {@link Evaluatable}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(URL url) throws SyntaxException {
		Unmarshaller unmarshaller;
		try {
			unmarshaller = createUnmarshaller();
		} catch (PropertyException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		}

		try {
			return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(url))
					.getValue();
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the url.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the url.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		}
	}

	/**
	 * Creates an {@link Evaluatable} from the given {@link InputSource}.
	 * 
	 * @param inputSource
	 *            The {@link InputSource} from which the {@link Evaluatable} is
	 *            created.
	 * @return The created {@link Evaluatable}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(InputSource inputSource)
			throws SyntaxException {
		Unmarshaller unmarshaller;
		try {
			unmarshaller = createUnmarshaller();
		} catch (PropertyException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		}

		try {
			return ((JAXBElement<Evaluatable>) unmarshaller
					.unmarshal(inputSource)).getValue();
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the input source.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the input source.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		}
	}

	/**
	 * Creates an {@link Evaluatable} from the given {@link Node}.
	 * 
	 * @param node
	 *            The {@link Node} from which the {@link Evaluatable} is
	 *            created.
	 * @return The created {@link Evaluatable}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(Node node) throws SyntaxException {
		Unmarshaller unmarshaller;
		try {
			unmarshaller = createUnmarshaller();
		} catch (PropertyException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		}

		try {
			return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(node))
					.getValue();
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the node.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the node.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		}
	}

	/**
	 * Creates an {@link Evaluatable} from the given {@link Source}.
	 * 
	 * @param source
	 *            The {@link Node} from which the {@link Evaluatable} is
	 *            created.
	 * @return The created {@link Evaluatable}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(Source source) throws SyntaxException {
		Unmarshaller unmarshaller;
		try {
			unmarshaller = createUnmarshaller();
		} catch (PropertyException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		}

		try {
			return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(source))
					.getValue();
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the source.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the source.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		}
	}

	/**
	 * Creates an {@link Evaluatable} from the given {@link XMLStreamReader}.
	 * 
	 * @param xmlStreamReader
	 *            The {@link XMLStreamReader} from which the {@link Evaluatable}
	 *            is created.
	 * @return The created {@link Evaluatable}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(XMLStreamReader xmlStreamReader)
			throws SyntaxException {
		Unmarshaller unmarshaller;
		try {
			unmarshaller = createUnmarshaller();
		} catch (PropertyException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		}

		try {
			return ((JAXBElement<Evaluatable>) unmarshaller
					.unmarshal(xmlStreamReader)).getValue();
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the xml stream reader.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the xml stream reader.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		}
	}

	/**
	 * Creates an {@link Evaluatable} from the given {@link XMLEventReader}.
	 * 
	 * @param xmlEventReader
	 *            The {@link XMLEventReader} from which the {@link Evaluatable}
	 *            is created.
	 * @return The created {@link Evaluatable}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(XMLEventReader xmlEventReader)
			throws SyntaxException {
		Unmarshaller unmarshaller;
		try {
			unmarshaller = createUnmarshaller();
		} catch (PropertyException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to create a JAXB Unmarshaller.", e);
			LOGGER.error(se.getMessage());
			throw se;
		}

		try {
			return ((JAXBElement<Evaluatable>) unmarshaller
					.unmarshal(xmlEventReader)).getValue();
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the xml event reader.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the xml event reader.", e);
			LOGGER.error(se.getMessage(), e);
			throw se;
		}
	}
}