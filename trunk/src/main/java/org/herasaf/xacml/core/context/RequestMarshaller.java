/*
 * Copyright 2008 - 2011 HERAS-AF (www.herasaf.org)
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
package org.herasaf.xacml.core.context;

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
import org.herasaf.xacml.core.context.impl.ObjectFactory;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.utils.DefaultValidationEventHandler;
import org.herasaf.xacml.core.utils.JAXBMarshallerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;

/**
 * This utility class can be used to marshal and unmarshal {@link RequestType}s.
 * Provides various unmarshal methods to create a ResponseCtx. Because the
 * {@link Unmarshaller} from JAXB <b>is not</b> thread safe it must be created
 * in each unmarshal-method. This class fully relies on the underlying JAXB
 * implementation.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class RequestMarshaller {
	private transient static final Logger LOGGER = LoggerFactory
			.getLogger(RequestMarshaller.class);
	private static JAXBContext CONTEXT;
	private static JAXBMarshallerConfiguration CONFIGURATION;
	private static final ObjectFactory OBJECT_FACTORY;

	/**
	 * Initializes the object factory.
	 */
	static {
		OBJECT_FACTORY = new ObjectFactory();
	}

	public static void setJAXBContext(JAXBContext context) {
		RequestMarshaller.CONTEXT = context;
	}

	public static void setJAXBMarshallerConfiguration(
			JAXBMarshallerConfiguration configuration) {
		RequestMarshaller.CONFIGURATION = configuration;
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
			LOGGER.error("JAXB context and/or configuration not initialized.");
			throw new NotInitializedException(
					"JAXB context and/or configuration not initialized.");
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
			LOGGER.error("JAXB context and/or configuration not initialized.");
			throw new NotInitializedException(
					"JAXB context and/or configuration not initialized.");
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
	 * Marshals this {@link RequestType} to the given content handler.
	 * 
	 * @param response
	 *            The {@link RequestType} to marshal.
	 * 
	 * @param ch
	 *            The {@link ContentHandler} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(RequestType request, ContentHandler ch)
			throws WritingException {
		try {
			createMarshaller().marshal(OBJECT_FACTORY.createRequest(request),
					ch);
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to write to the content handler.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * Marshals this {@link RequestType} to the given file.
	 * 
	 * @param response
	 *            The {@link RequestType} to marshal.
	 * 
	 * @param file
	 *            The {@link File} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(RequestType request, File file)
			throws WritingException {
		try {
			createMarshaller().marshal(OBJECT_FACTORY.createRequest(request),
					file);
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to write to the file.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * Marshals this {@link RequestType} to the given result.
	 * <p>
	 * <b>Note:</b><br />
	 * At least DOMResult, SAXResult and StreamResult are supported. If more
	 * results are supported, depends on the JAXBImplementation included in this
	 * Module.
	 * </p>
	 * 
	 * @param response
	 *            The {@link RequestType} to marshal.
	 * 
	 * @param result
	 *            The {@link Result} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(RequestType request, Result result)
			throws WritingException {
		try {
			createMarshaller().marshal(OBJECT_FACTORY.createRequest(request),
					result);
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to write to the result.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * Marshals this {@link RequestType} to the given output stream.
	 * 
	 * @param response
	 *            The {@link RequestType} to marshal.
	 * 
	 * @param out
	 *            The {@link OutputStream} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(RequestType request, OutputStream out)
			throws WritingException {
		try {
			createMarshaller().marshal(OBJECT_FACTORY.createRequest(request),
					out);
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to write to the output stream.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * Marshals this {@link RequestType} to the given writer.
	 * 
	 * @param response
	 *            The {@link RequestType} to marshal.
	 * 
	 * @param writer
	 *            The {@link Writer} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(RequestType request, Writer writer)
			throws WritingException {
		try {
			createMarshaller().marshal(OBJECT_FACTORY.createRequest(request),
					writer);
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to write to the writer.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * Marshals this {@link RequestType} to the given node.
	 * 
	 * @param response
	 *            The {@link RequestType} to marshal.
	 * 
	 * @param node
	 *            The {@link Node} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(RequestType request, Node node)
			throws WritingException {
		try {
			createMarshaller().marshal(OBJECT_FACTORY.createRequest(request),
					node);
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to write to the node.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * Marshals this {@link RequestType} to the given xml stream writer.
	 * 
	 * @param response
	 *            The {@link RequestType} to marshal.
	 * 
	 * @param xmlStreamWriter
	 *            The {@link XMLStreamWriter} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(RequestType request,
			XMLStreamWriter xmlStreamWriter) throws WritingException {
		try {
			createMarshaller().marshal(OBJECT_FACTORY.createRequest(request),
					xmlStreamWriter);
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to write to the xml stream writer.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * Marshals this {@link RequestType} to the given xml event writer.
	 * 
	 * @param response
	 *            The {@link RequestType} to marshal.
	 * 
	 * @param xmlEventWriter
	 *            The {@link XMLEventWriter} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public static void marshal(RequestType request,
			XMLEventWriter xmlEventWriter) throws WritingException {
		try {
			createMarshaller().marshal(OBJECT_FACTORY.createRequest(request),
					xmlEventWriter);
		} catch (JAXBException e) {
			WritingException we = new WritingException(
					"Unable to write to the xml event writer.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * Creates an {@link RequestType} from the given {@link File}.
	 * 
	 * @param file
	 *            The {@link File} from which the {@link RequestType} is
	 *            created.
	 * @return The created {@link RequestType}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static RequestType unmarshal(File file) throws SyntaxException {
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
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(file)).getValue();
			return rt;
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
	 * Creates an {@link RequestType} from the given {@link InputStream}.
	 * 
	 * @param inputStream
	 *            The {@link InputStream} from which the {@link RequestType} is
	 *            created.
	 * @return The created {@link RequestType}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static RequestType unmarshal(InputStream inputStream)
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
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(inputStream)).getValue();
			return rt;
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
	 * Creates an {@link RequestType} from the given {@link Reader}.
	 * 
	 * @param reader
	 *            The {@link Reader} from which the {@link RequestType} is
	 *            created.
	 * @return The created {@link RequestType}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static RequestType unmarshal(Reader reader) throws SyntaxException {
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
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(reader)).getValue();
			return rt;
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
	 * Creates an {@link RequestType} from the given {@link URL}.
	 * 
	 * @param url
	 *            The {@link URL} from which the {@link RequestType} is created.
	 * @return The created {@link RequestType}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static RequestType unmarshal(URL url) throws SyntaxException {
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
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(url)).getValue();
			return rt;
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
	 * Creates an {@link RequestType} from the given {@link InputSource}.
	 * 
	 * @param inputSource
	 *            The {@link InputSource} from which the {@link RequestType} is
	 *            created.
	 * @return The created {@link RequestType}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static RequestType unmarshal(InputSource inputSource)
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
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(inputSource)).getValue();
			return rt;
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
	 * Creates an {@link RequestType} from the given {@link Node}.
	 * 
	 * @param node
	 *            The {@link Node} from which the {@link RequestType} is
	 *            created.
	 * @return The created {@link RequestType}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static RequestType unmarshal(Node node) throws SyntaxException {
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
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(node)).getValue();
			return rt;
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
	 * Creates an {@link RequestType} from the given {@link Source}.
	 * 
	 * @param source
	 *            The {@link Node} from which the {@link RequestType} is
	 *            created.
	 * @return The created {@link RequestType}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static RequestType unmarshal(Source source) throws SyntaxException {
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
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(source)).getValue();
			return rt;
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
	 * Creates an {@link RequestType} from the given {@link XMLStreamReader}.
	 * 
	 * @param xmlStreamReader
	 *            The {@link XMLStreamReader} from which the {@link RequestType}
	 *            is created.
	 * @return The created {@link RequestType}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static RequestType unmarshal(XMLStreamReader xmlStreamReader)
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
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(xmlStreamReader)).getValue();
			return rt;
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
	 * Creates an {@link RequestType} from the given {@link XMLEventReader}.
	 * 
	 * @param xmlEventReader
	 *            The {@link XMLEventReader} from which the {@link RequestType}
	 *            is created.
	 * @return The created {@link RequestType}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static RequestType unmarshal(XMLEventReader xmlEventReader)
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
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(xmlEventReader)).getValue();
			return rt;
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
