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

package org.herasaf.xacml.core.context;

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
import org.herasaf.xacml.core.context.impl.ResponseType;
import org.herasaf.xacml.core.utils.ContextAndPolicy;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;

/**
 * TODO JAVADOC
 * 
 * Encapsulates a {@link ResponseType}. Provides various marshalling methods for
 * the {@link ResponseType}. Because the {@link Marshaller} from JAXB <b>is
 * not</b> thread safe it must be newly created in each marshal-method. This
 * class fully relies on the underlying JAXB implementation.
 * 
 * @author Florian Huonder
 * @version 1.0
 */
public class ResponseCtx {
	private static final ContextAndPolicy.JAXBProfile RESPONSECTX = ContextAndPolicy.JAXBProfile.RESPONSE_CTX;

	private static ObjectFactory objectFactory;
	private ResponseType response;

	/**
	 * TODO JAVADOC
	 * 
	 * Initializes the object factory.
	 */
	static {
		objectFactory = new ObjectFactory();
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Creates a new {@link ResponseCtx} with the given {@link ResponseType}.
	 * 
	 * @param response
	 *            The {@link ResponseType} to place in the {@link ResponseCtx}.
	 */
	public ResponseCtx(ResponseType response) {
		this.response = response;
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Returns the containing {@link ResponseType}.
	 * 
	 * @return The {@link ResponseType} contained in this object.
	 */
	public ResponseType getResponse() {
		return response;
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Marshals the contained Response into SAX2 events.
	 * 
	 * @param ch
	 *            XML will be sent to this handler as SAX2 events.
	 * @throws WritingException
	 *             - If any unexpected problem occurs during the marshalling.
	 */
	public void marshal(ContentHandler ch) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(RESPONSECTX).marshal(objectFactory.createResponse(response), ch);
		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Marshals the contained Response to the file.
	 * 
	 * @param file
	 *            XML written into this file.
	 * @throws WritingException
	 *             - If any unexpected problem occurs during the marshalling.
	 */
	public void marshal(File file) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(RESPONSECTX).marshal(objectFactory.createResponse(response), file);
		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * <p>
	 * Marshal the contained Response into the specified
	 * javax.xml.transform.Result.
	 * </p>
	 * <p>
	 * At least DOMResult, SAXResult and StreamResult are supported. If more
	 * results are supported, depends on the JAXBImplementation included in this
	 * Module.
	 * </p>
	 * 
	 * @param result
	 *            XML will be sent to this Result .
	 * @throws WritingException
	 *             - If any unexpected problem occurs during the marshalling.
	 */
	public void marshal(Result result) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(RESPONSECTX).marshal(objectFactory.createResponse(response), result);
		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * <p>
	 * Marshal the contained Response into an output stream.
	 * </p>
	 * 
	 * @param out
	 *            XML will be added to this stream .
	 * @throws WritingException
	 *             - If any unexpected problem occurs during the marshalling.
	 */
	public void marshal(OutputStream out) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(RESPONSECTX).marshal(objectFactory.createResponse(response), out);
		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * <p>
	 * Marshal the contained Response into a Writer.
	 * </p>
	 * 
	 * @param writer
	 *            XML will be sent to this writer.
	 * @throws WritingException
	 *             - If any unexpected problem occurs during the marshalling.
	 */
	public void marshal(Writer writer) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(RESPONSECTX).marshal(objectFactory.createResponse(response), writer);
		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Marshals the contained Response into a DOM tree.
	 * 
	 * @param node
	 *            DOM nodes will be added as children of this node. This
	 *            parameter must be a Node that accepts children (Document,
	 *            DocumentFragment, or Element)
	 * 
	 * @throws WritingException
	 *             - If any unexpected problem occurs during the marshalling.
	 */
	public void marshal(Node node) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(RESPONSECTX).marshal(objectFactory.createResponse(response), node);
		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Marshal the contained Response into a XMLStreamWriter.
	 * 
	 * @param xmlStreamWriter
	 *            XML will be sent to this writer.
	 * 
	 * @throws WritingException
	 *             - If any unexpected problem occurs during the marshalling.
	 */
	public void marshal(XMLStreamWriter xmlStreamWriter) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(RESPONSECTX)
					.marshal(objectFactory.createResponse(response), xmlStreamWriter);
		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Marshal the contained Response into a XMLEventWriter.
	 * 
	 * @param xmlEventWriter
	 *            XML will be sent to this writer.
	 * 
	 * @throws WritingException
	 *             - If any unexpected problem occurs during the marshalling.
	 */
	public void marshal(XMLEventWriter xmlEventWriter) throws WritingException {
		try {
			ContextAndPolicy.getMarshaller(RESPONSECTX).marshal(objectFactory.createResponse(response), xmlEventWriter);
		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}
}