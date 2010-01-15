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

package org.herasaf.xacml.core.context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;

import org.herasaf.xacml.core.WritingException;
import org.herasaf.xacml.core.context.impl.ObjectFactory;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.utils.ContextAndPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;

/**
 * This request context represents a XACML request.
 * <br />
 * This request context provides various marshalling methods. Because the {@link Marshaller} of JAXB <b>is
 * not</b> thread safe it must be created in each marshal-method. This
 * class fully relies on the underlying JAXB implementation.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class RequestCtx {
	private final Logger LOGGER = LoggerFactory.getLogger(RequestCtx.class);
	private static final ContextAndPolicy.JAXBProfile REQUESTCTX = ContextAndPolicy.JAXBProfile.REQUEST_CTX;
	private static ObjectFactory objectFactory;
	private RequestType request;

	/**
	 * Initializes the Object factory.
	 */
	static {
		objectFactory = new ObjectFactory();
	}

	/**
	 * Creates a new {@link RequestCtx} from the given {@link RequestType}.
	 * 
	 * @param request
	 *            The {@link RequestType} which will be placed into this
	 *            {@link RequestCtx}.
	 */
	public RequestCtx(RequestType request) {
		this.request = request;
	}

	/**
	 * Returns the containing {@link RequestType}.
	 * 
	 * @return The {@link RequestType} contained in this object.
	 */
	public RequestType getRequest() {
		return request;
	}

	/**
	 * Marshals this {@link RequestCtx} to the given content handler.
	 * 
	 * @param ch
	 *            The {@link ContentHandler} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public void marshal(ContentHandler ch) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(REQUESTCTX).marshal(objectFactory.createRequest(request), ch);
		} catch (JAXBException e) {
			WritingException we = new WritingException("Unable to write to the content handler.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * Marshals this {@link RequestCtx} to the given file.
	 * 
	 * @param file
	 *            The {@link File} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public void marshal(File file) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(REQUESTCTX).marshal(objectFactory.createRequest(request), file);
		} catch (JAXBException e) {
			WritingException we = new WritingException("Unable to write to the file.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * Marshals this {@link RequestCtx} to the given result.
	 * 
	 * <p>
	 * <b>Note:</b><br />
	 * At least DOMResult, SAXResult and StreamResult are supported. If more
	 * results are supported, depends on the JAXBImplementation included in this
	 * Module.
	 * </p>
	 * 
	 * @param result
	 *            The {@link Result} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public void marshal(Result result) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(REQUESTCTX).marshal(objectFactory.createRequest(request), result);
		} catch (JAXBException e) {
			WritingException we = new WritingException("Unable to write to the result.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * Marshals this {@link RequestCtx} to the given output stream.
	 * 
	 * @param out
	 *            The {@link OutputStream} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public void marshal(OutputStream out) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(REQUESTCTX).marshal(objectFactory.createRequest(request), out);
		} catch (JAXBException e) {
			WritingException we = new WritingException("Unable to write to the output stream.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * Marshals this {@link RequestCtx} to the given writer.
	 * 
	 * @param writer
	 *            The {@link Writer} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public void marshal(Writer writer) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(REQUESTCTX).marshal(objectFactory.createRequest(request), writer);
		} catch (JAXBException e) {
			WritingException we = new WritingException("Unable to write to the writer.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * Marshals this {@link RequestCtx} to the given node.
	 * 
	 * @param node
	 *            The {@link Node} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public void marshal(Node node) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(REQUESTCTX).marshal(objectFactory.createRequest(request), node);
		} catch (JAXBException e) {
			WritingException we = new WritingException("Unable to write to the node.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * Marshals this {@link RequestCtx} to the given xml stream writer.
	 * 
	 * @param xmlStreamWriter
	 *            The {@link XMLStreamWriter} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public void marshal(XMLStreamWriter xmlStreamWriter) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(REQUESTCTX).marshal(objectFactory.createRequest(request), xmlStreamWriter);
		} catch (JAXBException e) {
			WritingException we = new WritingException("Unable to write to the xml stream writer.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * Marshals this {@link RequestCtx} to the given xml event writer.
	 * 
	 * @param xmlEventWriter
	 *            The {@link XMLEventWriter} to use.
	 * @throws WritingException
	 *             In case an error occurs.
	 */
	public void marshal(XMLEventWriter xmlEventWriter) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(REQUESTCTX).marshal(objectFactory.createRequest(request), xmlEventWriter);
		} catch (JAXBException e) {
			WritingException we = new WritingException("Unable to write to the xml event writer.", e);
			LOGGER.error(we.getMessage());
			throw we;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		OutputStream os = new ByteArrayOutputStream();
		try {
			this.marshal(os);
		} catch (WritingException e) {
			LOGGER.warn("Could not marshal RequestCtx to OutputStream.", e);
			return null;
		}
		return os.toString();
	}
}