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
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;

import org.herasaf.xacml.SyntaxException;
import org.herasaf.xacml.core.context.impl.ObjectFactory;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.transformable.ActionTransformable;
import org.herasaf.xacml.core.context.transformable.EnvironmentTransformable;
import org.herasaf.xacml.core.context.transformable.ResourceTransformable;
import org.herasaf.xacml.core.context.transformable.SubjectTransformable;
import org.herasaf.xacml.core.utils.ContextAndPolicy;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * Factory to create a {@link RequestCtx}.Provides various unmarshal methods to
 * create a RequestCtx containing a RequestType. Because the {@link Unmarshaller}
 * from JAXB <b>is not</b> thread safe it must be newly created in each
 * marshal-method. This class fully relies on the underlying JAXB
 * implementation.
 *
 * @author Florian Huonder
 * @version 1.0
 */
public class RequestCtxFactory {
	private static final ContextAndPolicy.JAXBProfile REQUESTCTX;
	private static final ObjectFactory objectFactory;

	/**
	 * Static constructor of the RequestCtxFactory. It initializes Class
	 * variables.
	 */
	static {
		REQUESTCTX = ContextAndPolicy.JAXBProfile.REQUEST_CTX;
		objectFactory = new ObjectFactory();
	}

	/**
	 * Method to create a RequestCtx using Transformables.
	 *
	 * @param subjectTransformable
	 *            SubjectTransformable containing the subject informations.
	 * @param resourceTransformable
	 *            ResourceTransformable containing the resource informations.
	 * @param actionTransformable
	 *            ActionTransformable containing the action informations.
	 * @param environmentTransformable
	 *            EnvironmentTransformable containing the Environment
	 *            informations
	 * @return A RequestCtx containing the information given in the
	 *         Transformables.
	 */
	public static RequestCtx create(SubjectTransformable subjectTransformable,
			ResourceTransformable resourceTransformable,
			ActionTransformable actionTransformable,
			EnvironmentTransformable environmentTransformable) {
		RequestType req = objectFactory.createRequestType();
		req.getSubjects().addAll(subjectTransformable.transform());
		req.getResources().addAll(resourceTransformable.transform());
		req.setAction(actionTransformable.transform());
		req.setEnvironment(environmentTransformable.transform());
		return new RequestCtx(req);
	}

	/**
	 * Unmarshal XML data from the specified file and return the resulting
	 * RequestCtx.
	 *
	 * @param file
	 *            the file to unmarshal the request from.
	 * @return The newly created RequestCtx.
	 * @throws SyntaxException -
	 *             Exception if an error occures.
	 */
	@SuppressWarnings("unchecked")
	public static RequestCtx unmarshal(File file) throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(REQUESTCTX);
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(file)).getValue();
			return new RequestCtx(rt);
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * Unmarshal XML data from the specified InputStream and return the
	 * resulting RequestCtx. Validation event location information may be
	 * incomplete.
	 *
	 * @param inputStream
	 *            the inputStream to unmarshal the request from.
	 * @return The newly created RequestCtx.
	 * @throws SyntaxException -
	 *             Exception if an error occures.
	 */
	@SuppressWarnings("unchecked")
	public static RequestCtx unmarshal(InputStream inputStream)
			throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(REQUESTCTX);
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(inputStream)).getValue();
			return new RequestCtx(rt);
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * Unmarshal XML data from the specified Reader and return the resulting
	 * RequestCtx. Validation event location information may be incomplete.
	 *
	 * @param reader
	 *            the Reader to unmarshal XML data from.
	 * @return The newly created RequestCtx.
	 * @throws SyntaxException -
	 *             Exception if an error occures.
	 */
	@SuppressWarnings("unchecked")
	public static RequestCtx unmarshal(Reader reader) throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(REQUESTCTX);
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(reader)).getValue();
			return new RequestCtx(rt);
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * Unmarshal XML data from the specified URL and return the resulting
	 * RequestCtx.
	 *
	 * @param url
	 *            the url to unmarshal the request from.
	 * @return The newly created RequestCtx.
	 * @throws SyntaxException -
	 *             Exception if an error occures.
	 */
	@SuppressWarnings("unchecked")
	public static RequestCtx unmarshal(URL url) throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(REQUESTCTX);
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(url)).getValue();
			return new RequestCtx(rt);
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * Unmarshal XML data from the specified SAX InputSource and return the
	 * resulting RequestCtx.
	 *
	 * @param inputSource
	 *            the input source to unmarshal XML data from
	 *
	 * @return The newly created RequestCtx.
	 * @throws SyntaxException -
	 *             Exception if an error occures.
	 */
	@SuppressWarnings("unchecked")
	public static RequestCtx unmarshal(InputSource inputSource)
			throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(REQUESTCTX);
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(inputSource)).getValue();
			return new RequestCtx(rt);
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * Unmarshal global XML data from the specified DOM tree and return the
	 * resulting RequestCtx.
	 *
	 * @param node
	 *            the input source to unmarshal XML data from
	 *
	 * @return The newly created RequestCtx.
	 * @throws SyntaxException -
	 *             Exception if an error occures.
	 */
	@SuppressWarnings("unchecked")
	public static RequestCtx unmarshal(Node node) throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(REQUESTCTX);
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(node)).getValue();
			return new RequestCtx(rt);
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * <p>
	 * Unmarshal XML data from the specified XML Source and return the resulting
	 * RequestCtx.
	 * </p>
	 * <p>
	 * <b>SAX 2.0 Parser Pluggability</b>
	 * </p>
	 * <p>
	 * A client application can choose not to use the default parser mechanism.
	 * Any SAX 2.0 compliant parser can be substituted for the default
	 * mechanism. To do so, the client application must properly configure a
	 * SAXSource containing an XMLReader implemented by the SAX 2.0 parser
	 * provider. If the XMLReader has an org.xml.sax.ErrorHandler registered on
	 * it, it will be replaced. If the SAXSource does not contain an XMLReader,
	 * then the default parser mechanism will be used.
	 * </p>
	 * <p>
	 * This parser replacement mechanism can also be used to replace the
	 * unmarshal-time validation engine. The client application must properly
	 * configure their SAX 2.0 compliant parser to perform validation (as shown
	 * in the example above). Any SAXParserExceptions encountered by the parser
	 * during the unmarshal operation will be processed and reported as
	 * SyntaxError. Note: specifying a substitute validating SAX 2.0 parser for
	 * unmarshalling does not necessarily replace the validation engine used for
	 * performing on-demand validation.
	 * </p>
	 * <p>
	 * The only way for a client application to specify an alternate parser
	 * mechanism to be used during unmarshal is via the unmarshal(SAXSource)
	 * API. All other forms of the unmarshal method (File, URL, Node, etc) will
	 * use the default parser and validator mechanisms.
	 * </p>
	 *
	 * @param source
	 *            the input source to unmarshal XML data from
	 *
	 * @return The newly created RequestCtx.
	 * @throws SyntaxException -
	 *             Exception if an error occures.
	 */
	@SuppressWarnings("unchecked")
	public static RequestCtx unmarshal(Source source) throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(REQUESTCTX);
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(source)).getValue();
			return new RequestCtx(rt);
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * <p>
	 * Unmarshal XML data from the specified pull parser and return the
	 * resulting RequestCtx.
	 * </p>
	 * <p>
	 * This method assumes that the parser is on a START_DOCUMENT or
	 * START_ELEMENT event. Unmarshalling will be done from this start event to
	 * the corresponding end event. If this method returns successfully, the
	 * reader will be pointing at the token right after the end event.
	 * </p>
	 *
	 *
	 *
	 * @param xmlStreamReader
	 *            The parser to be read.
	 *
	 * @return The newly created RequestCtx.
	 * @throws SyntaxException -
	 *             Exception if an error occures.
	 */
	@SuppressWarnings("unchecked")
	public static RequestCtx unmarshal(XMLStreamReader xmlStreamReader)
			throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(REQUESTCTX);
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(xmlStreamReader)).getValue();
			return new RequestCtx(rt);
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * <p>
	 * Unmarshal XML data from the specified pull parser and return the
	 * resulting RequestCtx.
	 * </p>
	 * <p>
	 * This method assumes that the parser is on a START_DOCUMENT or
	 * START_ELEMENT event. Unmarshalling will be done from this start event to
	 * the corresponding end event. If this method returns successfully, the
	 * reader will be pointing at the token right after the end event.
	 * </p>
	 *
	 *
	 *
	 * @param xmlEventReader
	 *            The parser to be read.
	 *
	 * @return The newly created RequestCtx.
	 * @throws SyntaxException -
	 *             Exception if an error occures.
	 */
	@SuppressWarnings("unchecked")
	public static RequestCtx unmarshal(XMLEventReader xmlEventReader)
			throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(REQUESTCTX);
			RequestType rt = ((JAXBElement<RequestType>) unmarshaller
					.unmarshal(xmlEventReader)).getValue();
			return new RequestCtx(rt);
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}
}