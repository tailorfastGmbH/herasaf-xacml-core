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

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.MissingAttributeDetailType;
import org.herasaf.xacml.core.context.impl.ObjectFactory;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResponseType;
import org.herasaf.xacml.core.context.impl.ResultType;
import org.herasaf.xacml.core.context.impl.StatusCodeType;
import org.herasaf.xacml.core.context.impl.StatusDetailType;
import org.herasaf.xacml.core.context.impl.StatusType;
import org.herasaf.xacml.core.utils.ContextAndPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * Factory to create a {@link ResponseCtx}. Provides various unmarshal methods to create a ResponseCtx. Because the
 * {@link Unmarshaller} from JAXB <b>is not</b> thread safe it must be created in each unmarshal-method. This class
 * fully relies on the underlying JAXB implementation.
 * 
 * @author Florian Huonder
 */
public final class ResponseCtxFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseCtxFactory.class);
    private static final ContextAndPolicy.JAXBProfile RESPONSECTX = ContextAndPolicy.JAXBProfile.RESPONSE_CTX;
    private static ObjectFactory factory;

    /**
     * Initializes the object factory.
     */
    static {
        factory = new ObjectFactory();
    }

    /**
     * This is a utility class and must not be instantiated.
     */
    private ResponseCtxFactory() {

    }

    /**
     * Creates a {@link ResponseCtx} from the given {@link File}.
     * 
     * @param file
     *            The {@link File} from which the {@link ResponseCtx} is created.
     * @return The created {@link ResponseCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static ResponseCtx unmarshal(File file) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(RESPONSECTX);
            ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller.unmarshal(file)).getValue();
            return new ResponseCtx(rt);
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
     * Creates a {@link ResponseCtx} from the given {@link InputStream}.
     * 
     * @param inputStream
     *            The {@link InputStream} from which the {@link ResponseCtx} is created.
     * @return The created {@link ResponseCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static ResponseCtx unmarshal(InputStream inputStream) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(RESPONSECTX);
            ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller.unmarshal(inputStream)).getValue();
            return new ResponseCtx(rt);
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
     * Creates a {@link ResponseCtx} from the given {@link Reader}.
     * 
     * @param reader
     *            The {@link Reader} from which the {@link ResponseCtx} is created.
     * @return The created {@link ResponseCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static ResponseCtx unmarshal(Reader reader) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(RESPONSECTX);
            ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller.unmarshal(reader)).getValue();
            return new ResponseCtx(rt);
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
     * Creates a {@link ResponseCtx} from the given {@link URL}.
     * 
     * @param url
     *            The {@link URL} from which the {@link ResponseCtx} is created.
     * @return The created {@link ResponseCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static ResponseCtx unmarshal(URL url) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(RESPONSECTX);
            ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller.unmarshal(url)).getValue();
            return new ResponseCtx(rt);
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
     * Creates a {@link ResponseCtx} from the given {@link InputSource}.
     * 
     * @param inputSource
     *            The {@link InputSource} from which the {@link ResponseCtx} is created.
     * @return The created {@link ResponseCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static ResponseCtx unmarshal(InputSource inputSource) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(RESPONSECTX);
            ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller.unmarshal(inputSource)).getValue();
            return new ResponseCtx(rt);
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
     * Creates a {@link ResponseCtx} from the given {@link Node}.
     * 
     * @param node
     *            The {@link Node} from which the {@link ResponseCtx} is created.
     * @return The created {@link ResponseCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static ResponseCtx unmarshal(Node node) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(RESPONSECTX);
            ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller.unmarshal(node)).getValue();
            return new ResponseCtx(rt);
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
     * Creates a {@link ResponseCtx} from the given {@link Source}.
     * 
     * *
     * <p>
     * <b>SAX 2.0 Parser Pluggability</b> <br />
     * A client application can choose not to use the default parser mechanism. Any SAX 2.0 compliant parser can be
     * substituted for the default mechanism. To do so, the client application must properly configure a SAXSource
     * containing an XMLReader implemented by the SAX 2.0 parser provider. If the XMLReader has an
     * org.xml.sax.ErrorHandler registered on it, it will be replaced. If the SAXSource does not contain an XMLReader,
     * then the default parser mechanism will be used.
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
     *            The {@link Source} from which the {@link ResponseCtx} is created.
     * @return The created {@link ResponseCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     * 
     */
    @SuppressWarnings("unchecked")
    public static ResponseCtx unmarshal(Source source) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(RESPONSECTX);
            ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller.unmarshal(source)).getValue();
            return new ResponseCtx(rt);
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
     * Creates a {@link ResponseCtx} from the given {@link XMLStreamReader}.
     * 
     * <p>
     * <b>Note:</b><br />
     * This method assumes that the parser is on a START_DOCUMENT or START_ELEMENT event. Unmarshalling will be done
     * from this start event to the corresponding end event. If this method returns successfully, the reader will be
     * pointing at the token right after the end event.
     * </p>
     * 
     * @param xmlStreamReader
     *            The {@link XMLStreamReader} from which the {@link ResponseCtx} is created.
     * @return The created {@link ResponseCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static ResponseCtx unmarshal(XMLStreamReader xmlStreamReader) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(RESPONSECTX);
            ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller.unmarshal(xmlStreamReader)).getValue();
            return new ResponseCtx(rt);
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
     * Creates a {@link ResponseCtx} from the given {@link XMLEventReader}.
     * 
     * <p>
     * <b>Note:</b><br />
     * This method assumes that the parser is on a START_DOCUMENT or START_ELEMENT event. Unmarshalling will be done
     * from this start event to the corresponding end event. If this method returns successfully, the reader will be
     * pointing at the token right after the end event.
     * </p>
     * 
     * @param xmlEventReader
     *            The {@link XMLEventReader} from which the {@link ResponseCtx} is created.
     * @return The created {@link ResponseCtx}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static ResponseCtx unmarshal(XMLEventReader xmlEventReader) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(RESPONSECTX);
            ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller.unmarshal(xmlEventReader)).getValue();
            return new ResponseCtx(rt);
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

    /**
     * Creates a {@link ResponseCtx} with the given {@link DecisionType} and adds the {@link MissingAttributeDetailType}
     * s and {@link ObligationsType}, if there are any of these.
     * 
     * @param req
     *            The relating {@link RequestType}.
     * @param decision
     *            The {@link DecisionType} of the evaluation.
     * @param evaluationContext
     *            The {@link EvaluationContext} containing the information about status and missing attributes.
     * @return The created {@link ResponseCtx}.
     */
    public static ResponseCtx create(RequestType req, DecisionType decision, EvaluationContext evaluationContext) {
        ResponseCtx resCtx = create(req, decision, evaluationContext.getStatusCode());
        // This check is here because only MissingAttributeDetails are
        // supported in the the StatusDetail
        if (evaluationContext.getMissingAttributes().size() > 0) {
            StatusDetailType statusDetail = factory.createStatusDetailType();
            /*
             * If the request contains any MissingAttributeDetails those must be added to the List in the
             * StatusDetailType contained in a JAXBElement object. This because the List in the StatusDetailType takes a
             * type of ##any (in Java this is Object). Therefore without the JAXBElement containing the
             * MissingAttributeDetailType JAXB is unable to marshal the MissingAttributeDetail correctly.
             */
            List<JAXBElement<MissingAttributeDetailType>> missingAttributesJaxb = new ArrayList<JAXBElement<MissingAttributeDetailType>>();
            for (MissingAttributeDetailType madt : evaluationContext.getMissingAttributes()) {
                missingAttributesJaxb.add(factory.createMissingAttributeDetail(madt));
            }
            statusDetail.getContent().addAll(missingAttributesJaxb);
            resCtx.getResponse().getResults().get(0).getStatus().setStatusDetail(statusDetail);
        }
        // Add the Obligations to the response
        if (evaluationContext.getObligations().getObligations().size() > 0) {
            resCtx.getResponse().getResults().get(0).setObligations(evaluationContext.getObligations());
        }
        return resCtx;
    }

    /**
     * Creates a {@link ResponseCtx} with the given {@link DecisionType} and status code. Here no missing attributes or
     * obligations can be added to the repsonse.
     * 
     * @param req
     *            The relating {@link RequestType}.
     * @param decision
     *            The {@link DecisionType} of the evaluation.
     * @param code
     *            The {@link StatusCode} of the decision.
     * @return The created {@link ResponseCtx}.
     */
    public static ResponseCtx create(RequestType req, DecisionType decision, StatusCode code) {
        ResponseType res = factory.createResponseType();
        ResultType result = factory.createResultType();

        if (req.getResources().size() == 1) {
            // The ResourceId could be set here as well. This is explicitly not
            // done because of performance reasons.
            // For setting the ResourceId in the response, all attributes in the
            // request-resource must be walked through
            // to find the id. Then this id must be planted into the response.
            // This is an absolute overhead because each response is
            // automatically assigned to its request by the caller.

            result.setDecision(decision);
        } else {
            result.setDecision(DecisionType.INDETERMINATE);
        }

        StatusCodeType statusCode = factory.createStatusCodeType();
        statusCode.setValue(code.getValue());
        StatusType status = factory.createStatusType();
        status.setStatusCode(statusCode);
        result.setStatus(status);

        res.getResults().add(result);

        return new ResponseCtx(res);
    }
}