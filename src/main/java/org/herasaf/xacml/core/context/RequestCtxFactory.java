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

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.impl.ObjectFactory;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.transformable.ActionTransformable;
import org.herasaf.xacml.core.context.transformable.EnvironmentTransformable;
import org.herasaf.xacml.core.context.transformable.ResourceTransformable;
import org.herasaf.xacml.core.context.transformable.SubjectTransformable;
import org.herasaf.xacml.core.utils.ContextAndPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * Factory to create a {@link RequestCtx}. Provides various unmarshal methods to create a {@link RequestCtx}. Because
 * the {@link Unmarshaller} from JAXB <b>is not</b> thread safe it must be created in each unmarshal-method. This class
 * fully relies on the underlying JAXB implementation.
 * 
 * @author Florian Huonder
 */
public final class RequestCtxFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestCtxFactory.class);
    private static final ContextAndPolicy.JAXBProfile REQUESTCTX;
    private static final ObjectFactory OBJECT_FACTORY;

    /**
     * Static constructor of the RequestCtxFactory. It initializes Class variables.
     */
    static {
        REQUESTCTX = ContextAndPolicy.JAXBProfile.REQUEST_CTX;
        OBJECT_FACTORY = new ObjectFactory();
    }

    /**
     * A utility class must not bi instantiated.
     */
    private RequestCtxFactory() {

    }

    /**
     * To simply create a {@link RequestCtx} without having the request attributes in any kind of XML format (file,
     * input stream). This method is intended to be used by a PEP to simply create {@link RequestCtx}s that then can be
     * evaluated.
     * 
     * @param subjectTransformable
     *            Contains the subject attributes.
     * @param resourceTransformable
     *            Contains the resource attributes.
     * @param actionTransformable
     *            Contains the action attributes.
     * @param environmentTransformable
     *            Contains the environment attributes
     * @return The created {@link RequestCtx}.
     */
    public static RequestCtx create(SubjectTransformable subjectTransformable,
            ResourceTransformable resourceTransformable, ActionTransformable actionTransformable,
            EnvironmentTransformable environmentTransformable) {
        RequestType req = OBJECT_FACTORY.createRequestType();
        req.getSubjects().addAll(subjectTransformable.transform());
        req.getResources().addAll(resourceTransformable.transform());
        req.setAction(actionTransformable.transform());
        req.setEnvironment(environmentTransformable.transform());
        return new RequestCtx(req);
    }

    /**
     * Creates a {@link RequestCtx} from the given {@link File}.
     * 
     * @param file
     *            The {@link File} from which the {@link RequestCtx} is created.
     * @return The created {@link RequestCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static RequestCtx unmarshal(File file) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(REQUESTCTX);
            RequestType rt = ((JAXBElement<RequestType>) unmarshaller.unmarshal(file)).getValue();
            return new RequestCtx(rt);
        } catch (JAXBException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the file.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        } catch (ClassCastException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the file.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        }
    }

    /**
     * Creates a {@link RequestCtx} from the given {@link InputStream}.
     * 
     * @param inputStream
     *            The {@link InputStream} from which the {@link RequestCtx} is created.
     * @return The created {@link RequestCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static RequestCtx unmarshal(InputStream inputStream) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(REQUESTCTX);
            RequestType rt = ((JAXBElement<RequestType>) unmarshaller.unmarshal(inputStream)).getValue();
            return new RequestCtx(rt);
        } catch (JAXBException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the input stream.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        } catch (ClassCastException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the input stream.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        }
    }

    /**
     * Creates a {@link RequestCtx} from the given {@link Reader}.
     * 
     * @param reader
     *            The {@link Reader} from which the {@link RequestCtx} is created.
     * @return The created {@link RequestCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static RequestCtx unmarshal(Reader reader) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(REQUESTCTX);
            RequestType rt = ((JAXBElement<RequestType>) unmarshaller.unmarshal(reader)).getValue();
            return new RequestCtx(rt);
        } catch (JAXBException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the reader.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        } catch (ClassCastException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the reader.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        }
    }

    /**
     * Creates a {@link RequestCtx} from the given {@link URL}.
     * 
     * @param url
     *            The {@link URL} from which the {@link RequestCtx} is created.
     * @return The created {@link RequestCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static RequestCtx unmarshal(URL url) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(REQUESTCTX);
            RequestType rt = ((JAXBElement<RequestType>) unmarshaller.unmarshal(url)).getValue();
            return new RequestCtx(rt);
        } catch (JAXBException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the url.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        } catch (ClassCastException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the url.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        }
    }

    /**
     * Creates a {@link RequestCtx} from the given {@link InputSource}.
     * 
     * @param inputSource
     *            The {@link InputSource} from which the {@link RequestCtx} is created.
     * @return The created {@link RequestCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static RequestCtx unmarshal(InputSource inputSource) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(REQUESTCTX);
            RequestType rt = ((JAXBElement<RequestType>) unmarshaller.unmarshal(inputSource)).getValue();
            return new RequestCtx(rt);
        } catch (JAXBException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the input source.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        } catch (ClassCastException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the input source.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        }
    }

    /**
     * Creates a {@link RequestCtx} from the given {@link Node}.
     * 
     * @param node
     *            The {@link Node} from which the {@link RequestCtx} is created.
     * @return The created {@link RequestCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static RequestCtx unmarshal(Node node) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(REQUESTCTX);
            RequestType rt = ((JAXBElement<RequestType>) unmarshaller.unmarshal(node)).getValue();
            return new RequestCtx(rt);
        } catch (JAXBException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the node.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        } catch (ClassCastException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the node.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        }
    }

    /**
     * Creates a {@link RequestCtx} from the given {@link Source}.
     * 
     * <p>
     * <b>SAX 2.0 Parser Pluggability</b> A client application can choose not to use the default parser mechanism. Any
     * SAX 2.0 compliant parser can be substituted for the default mechanism. To do so, the client application must
     * properly configure a SAXSource containing an XMLReader implemented by the SAX 2.0 parser provider. If the
     * XMLReader has an org.xml.sax.ErrorHandler registered on it, it will be replaced. If the SAXSource does not
     * contain an XMLReader, then the default parser mechanism will be used.
     * </p>
     * <p>
     * This parser replacement mechanism can also be used to replace the unmarshal-time validation engine. The client
     * application must properly configure their SAX 2.0 compliant parser to perform validation (as shown in the example
     * above). Any SAXParserExceptions encountered by the parser during the unmarshal operation will be processed and
     * reported as SyntaxError. Note: specifying a substitute validating SAX 2.0 parser for unmarshalling does not
     * necessarily replace the validation engine used for performing on-demand validation.
     * </p>
     * <p>
     * The only way for a client application to specify an alternate parser mechanism to be used during unmarshal is via
     * the unmarshal(SAXSource) API. All other forms of the unmarshal method (File, URL, Node, etc) will use the default
     * parser and validator mechanisms.
     * </p>
     * 
     * @param source
     *            The {@link Source} from which the {@link RequestCtx} is created.
     * @return The created {@link RequestCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static RequestCtx unmarshal(Source source) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(REQUESTCTX);
            RequestType rt = ((JAXBElement<RequestType>) unmarshaller.unmarshal(source)).getValue();
            return new RequestCtx(rt);
        } catch (JAXBException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the source.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        } catch (ClassCastException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the source.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        }
    }

    /**
     * Creates a {@link RequestCtx} from the given {@link XMLStreamReader}.
     * 
     * <p>
     * <b>Note:</b><br />
     * This method assumes that the parser is on a START_DOCUMENT or START_ELEMENT event. Unmarshalling will be done
     * from this start event to the corresponding end event. If this method returns successfully, the reader will be
     * pointing at the token right after the end event.
     * </p>
     * 
     * @param xmlStreamReader
     *            The {@link XMLStreamReader} from which the {@link RequestCtx} is created.
     * @return The created {@link RequestCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static RequestCtx unmarshal(XMLStreamReader xmlStreamReader) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(REQUESTCTX);
            RequestType rt = ((JAXBElement<RequestType>) unmarshaller.unmarshal(xmlStreamReader)).getValue();
            return new RequestCtx(rt);
        } catch (JAXBException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the xml stream reader.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        } catch (ClassCastException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the xml stream reader.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        }
    }

    /**
     * Creates a {@link RequestCtx} from the given {@link XMLEventReader}.
     * 
     * <p>
     * <b>Note:</b><br />
     * This method assumes that the parser is on a START_DOCUMENT or START_ELEMENT event. Unmarshalling will be done
     * from this start event to the corresponding end event. If this method returns successfully, the reader will be
     * pointing at the token right after the end event.
     * </p>
     * 
     * @param xmlEventReader
     *            The {@link XMLEventReader} from which the {@link RequestCtx} is created.
     * @return The created {@link RequestCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static RequestCtx unmarshal(XMLEventReader xmlEventReader) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(REQUESTCTX);
            RequestType rt = ((JAXBElement<RequestType>) unmarshaller.unmarshal(xmlEventReader)).getValue();
            return new RequestCtx(rt);
        } catch (JAXBException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the xml event reader.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        } catch (ClassCastException e) {
            SyntaxException se = new SyntaxException("Unable to unmarshal the xml event reader.");
            LOGGER.error(se.getMessage(), e);
            throw se;
        }
    }
}