/*
 * Copyright 2008 - 2012 HERAS-AF (www.herasaf.org)
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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;

import org.herasaf.xacml.core.NotInitializedException;
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
import org.herasaf.xacml.core.policy.impl.ObligationsType;
import org.herasaf.xacml.core.utils.DefaultValidationEventHandler;
import org.herasaf.xacml.core.utils.JAXBMarshallerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * Factory to create a {@link ResponseCtx}. Provides various unmarshal methods
 * to create a ResponseCtx. Because the {@link Unmarshaller} from JAXB <b>is
 * not</b> thread safe it must be created in each unmarshal-method. This class
 * fully relies on the underlying JAXB implementation.
 * 
 * This {@link ResponseCtxFactory} does only support XACML default behavior.
 * 
 * @author Florian Huonder
 * 
 * @deprecated For unmarshalling use {@link ResponseMarshaller}.unmarshal(...).
 */
@Deprecated
public final class ResponseCtxFactory {
	private transient static final Logger LOGGER = LoggerFactory
			.getLogger(ResponseCtxFactory.class);
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
	 * This is a utility class and must not be instantiated.
	 */
	private ResponseCtxFactory() {

	}

	/**
	 * Creates a {@link ResponseCtx} with the given {@link DecisionType} and
	 * adds the {@link MissingAttributeDetailType} s and {@link ObligationsType}
	 * , if there are any of these.
	 * 
	 * @param req
	 *            The relating {@link RequestType}.
	 * @param decision
	 *            The {@link DecisionType} of the evaluation.
	 * @param evaluationContext
	 *            The {@link EvaluationContext} containing the information about
	 *            status and missing attributes.
	 * @return The created {@link ResponseCtx}.
	 */
	public static ResponseCtx create(RequestType req, DecisionType decision,
			EvaluationContext evaluationContext) {
		ResponseCtx resCtx = create(req, decision, evaluationContext
				.getStatusCode());
		// This check is here because only MissingAttributeDetails are
		// supported in the the StatusDetail
		if (evaluationContext.getMissingAttributes().size() > 0) {
			StatusDetailType statusDetail = OBJECT_FACTORY
					.createStatusDetailType();
			/*
			 * If the request contains any MissingAttributeDetails those must be
			 * added to the List in the StatusDetailType contained in a
			 * JAXBElement object. This because the List in the StatusDetailType
			 * takes a type of ##any (in Java this is Object). Therefore without
			 * the JAXBElement containing the MissingAttributeDetailType JAXB is
			 * unable to marshal the MissingAttributeDetail correctly.
			 */
			List<JAXBElement<MissingAttributeDetailType>> missingAttributesJaxb = new ArrayList<JAXBElement<MissingAttributeDetailType>>();
			for (MissingAttributeDetailType madt : evaluationContext
					.getMissingAttributes()) {
				missingAttributesJaxb.add(OBJECT_FACTORY
						.createMissingAttributeDetail(madt));
			}
			statusDetail.getContent().addAll(missingAttributesJaxb);
			resCtx.getResponse().getResults().get(0).getStatus()
					.setStatusDetail(statusDetail);
		}
		// Add the Obligations to the response
		if (evaluationContext.getObligations().getObligations().size() > 0) {
			resCtx.getResponse().getResults().get(0).setObligations(
					evaluationContext.getObligations());
		}
		return resCtx;
	}

	/**
	 * Creates a {@link ResponseCtx} with the given {@link DecisionType} and
	 * status code. Here no missing attributes or obligations can be added to
	 * the repsonse.
	 * 
	 * @param req
	 *            The relating {@link RequestType}.
	 * @param decision
	 *            The {@link DecisionType} of the evaluation.
	 * @param code
	 *            The {@link StatusCode} of the decision.
	 * @return The created {@link ResponseCtx}.
	 */
	public static ResponseCtx create(RequestType req, DecisionType decision,
			StatusCode code) {
		ResponseType res = OBJECT_FACTORY.createResponseType();
		ResultType result = OBJECT_FACTORY.createResultType();

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

		StatusCodeType statusCode = OBJECT_FACTORY.createStatusCodeType();
		statusCode.setValue(code.getValue());
		StatusType status = OBJECT_FACTORY.createStatusType();
		status.setStatusCode(statusCode);
		result.setStatus(status);

		res.getResults().add(result);

		return new ResponseCtx(res);
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
	 * Creates an {@link ResponseCtx} from the given {@link File}.
	 * 
	 * @param file
	 *            The {@link File} from which the {@link ResponseCtx} is
	 *            created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static ResponseCtx unmarshal(File file) throws SyntaxException {
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
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(file)).getValue();
			return new ResponseCtx(rt);
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
	 * Creates an {@link ResponseCtx} from the given {@link InputStream}.
	 * 
	 * @param inputStream
	 *            The {@link InputStream} from which the {@link ResponseCtx} is
	 *            created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static ResponseCtx unmarshal(InputStream inputStream)
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
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(inputStream)).getValue();
			return new ResponseCtx(rt);
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
	 * Creates an {@link ResponseCtx} from the given {@link Reader}.
	 * 
	 * @param reader
	 *            The {@link Reader} from which the {@link ResponseCtx} is
	 *            created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static ResponseCtx unmarshal(Reader reader) throws SyntaxException {
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
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(reader)).getValue();
			return new ResponseCtx(rt);
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
	 * Creates an {@link ResponseCtx} from the given {@link URL}.
	 * 
	 * @param url
	 *            The {@link URL} from which the {@link ResponseCtx} is created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static ResponseCtx unmarshal(URL url) throws SyntaxException {
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
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(url)).getValue();
			return new ResponseCtx(rt);
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
	 * Creates an {@link ResponseCtx} from the given {@link InputSource}.
	 * 
	 * @param inputSource
	 *            The {@link InputSource} from which the {@link ResponseCtx} is
	 *            created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static ResponseCtx unmarshal(InputSource inputSource)
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
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(inputSource)).getValue();
			return new ResponseCtx(rt);
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
	 * Creates an {@link ResponseCtx} from the given {@link Node}.
	 * 
	 * @param node
	 *            The {@link Node} from which the {@link ResponseCtx} is
	 *            created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static ResponseCtx unmarshal(Node node) throws SyntaxException {
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
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(node)).getValue();
			return new ResponseCtx(rt);
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
	 * Creates an {@link ResponseCtx} from the given {@link Source}.
	 * 
	 * @param source
	 *            The {@link Node} from which the {@link ResponseCtx} is
	 *            created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static ResponseCtx unmarshal(Source source) throws SyntaxException {
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
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(source)).getValue();
			return new ResponseCtx(rt);
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
	 * Creates an {@link ResponseCtx} from the given {@link XMLStreamReader}.
	 * 
	 * @param xmlStreamReader
	 *            The {@link XMLStreamReader} from which the {@link ResponseCtx}
	 *            is created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static ResponseCtx unmarshal(XMLStreamReader xmlStreamReader)
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
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(xmlStreamReader)).getValue();
			return new ResponseCtx(rt);
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
	 * Creates an {@link ResponseCtx} from the given {@link XMLEventReader}.
	 * 
	 * @param xmlEventReader
	 *            The {@link XMLEventReader} from which the {@link ResponseCtx}
	 *            is created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	@SuppressWarnings("unchecked")
	public static ResponseCtx unmarshal(XMLEventReader xmlEventReader)
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
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(xmlEventReader)).getValue();
			return new ResponseCtx(rt);
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