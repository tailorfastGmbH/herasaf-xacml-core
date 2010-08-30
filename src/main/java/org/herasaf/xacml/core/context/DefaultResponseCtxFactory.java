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
 * Factory to create a {@link ResponseCtx}. Provides various unmarshal methods
 * to create a ResponseCtx. Because the {@link Unmarshaller} from JAXB <b>is
 * not</b> thread safe it must be created in each unmarshal-method. This class
 * fully relies on the underlying JAXB implementation.
 * 
 * This {@link ResponseCtxFactory} does only support XACML default behavior.
 * 
 * @author Florian Huonder
 */
public final class DefaultResponseCtxFactory implements ResponseCtxFactory {
	private final Logger logger = LoggerFactory
			.getLogger(ResponseCtxFactory.class);
	private final ContextAndPolicy.JAXBProfile responseCtx = ContextAndPolicy.JAXBProfile.RESPONSE_CTX;
	private ObjectFactory factory;

	/**
	 * This is a utility class and must not be instantiated.
	 */
	public DefaultResponseCtxFactory() {
		factory = new ObjectFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.xacml.core.context.ResponseCtxFactory#unmarshal(java.io.File)
	 */
	@SuppressWarnings("unchecked")
	public ResponseCtx unmarshal(File file) throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(responseCtx);
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(file)).getValue();
			return new ResponseCtx(rt);
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the file.");
			logger.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the file.");
			logger.error(se.getMessage(), e);
			throw se;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.herasaf.xacml.core.context.ResponseCtxFactory#unmarshal(java.io.
	 * InputStream)
	 */
	@SuppressWarnings("unchecked")
	public ResponseCtx unmarshal(InputStream inputStream)
			throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(responseCtx);
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(inputStream)).getValue();
			return new ResponseCtx(rt);
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the input stream.");
			logger.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the input stream.");
			logger.error(se.getMessage(), e);
			throw se;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.xacml.core.context.ResponseCtxFactory#unmarshal(java.io.Reader
	 * )
	 */
	@SuppressWarnings("unchecked")
	public ResponseCtx unmarshal(Reader reader) throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(responseCtx);
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(reader)).getValue();
			return new ResponseCtx(rt);
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the reader.");
			logger.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the reader.");
			logger.error(se.getMessage(), e);
			throw se;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.xacml.core.context.ResponseCtxFactory#unmarshal(java.net.URL)
	 */
	@SuppressWarnings("unchecked")
	public ResponseCtx unmarshal(URL url) throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(responseCtx);
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(url)).getValue();
			return new ResponseCtx(rt);
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the url.");
			logger.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the url.");
			logger.error(se.getMessage(), e);
			throw se;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.xacml.core.context.ResponseCtxFactory#unmarshal(org.xml.sax
	 * .InputSource)
	 */
	@SuppressWarnings("unchecked")
	public ResponseCtx unmarshal(InputSource inputSource)
			throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(responseCtx);
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(inputSource)).getValue();
			return new ResponseCtx(rt);
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the input source.");
			logger.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the input source.");
			logger.error(se.getMessage(), e);
			throw se;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.xacml.core.context.ResponseCtxFactory#unmarshal(org.w3c.dom
	 * .Node)
	 */
	@SuppressWarnings("unchecked")
	public ResponseCtx unmarshal(Node node) throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(responseCtx);
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(node)).getValue();
			return new ResponseCtx(rt);
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the node.");
			logger.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the node.");
			logger.error(se.getMessage(), e);
			throw se;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.xacml.core.context.ResponseCtxFactory#unmarshal(javax.xml
	 * .transform.Source)
	 */
	@SuppressWarnings("unchecked")
	public ResponseCtx unmarshal(Source source) throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(responseCtx);
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(source)).getValue();
			return new ResponseCtx(rt);
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the source.");
			logger.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the source.");
			logger.error(se.getMessage(), e);
			throw se;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.xacml.core.context.ResponseCtxFactory#unmarshal(javax.xml
	 * .stream.XMLStreamReader)
	 */
	@SuppressWarnings("unchecked")
	public ResponseCtx unmarshal(XMLStreamReader xmlStreamReader)
			throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(responseCtx);
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(xmlStreamReader)).getValue();
			return new ResponseCtx(rt);
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the xml stream reader.");
			logger.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the xml stream reader.");
			logger.error(se.getMessage(), e);
			throw se;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.xacml.core.context.ResponseCtxFactory#unmarshal(javax.xml
	 * .stream.XMLEventReader)
	 */
	@SuppressWarnings("unchecked")
	public ResponseCtx unmarshal(XMLEventReader xmlEventReader)
			throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(responseCtx);
			ResponseType rt = ((JAXBElement<ResponseType>) unmarshaller
					.unmarshal(xmlEventReader)).getValue();
			return new ResponseCtx(rt);
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the xml event reader.");
			logger.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException(
					"Unable to unmarshal the xml event reader.");
			logger.error(se.getMessage(), e);
			throw se;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.xacml.core.context.ResponseCtxFactory#create(org.herasaf.
	 * xacml.core.context.impl.RequestType,
	 * org.herasaf.xacml.core.context.impl.DecisionType,
	 * org.herasaf.xacml.core.context.EvaluationContext)
	 */
	public ResponseCtx create(RequestType req, DecisionType decision,
			EvaluationContext evaluationContext) {
		ResponseCtx resCtx = create(req, decision,
				evaluationContext.getStatusCode());
		// This check is here because only MissingAttributeDetails are
		// supported in the the StatusDetail
		if (evaluationContext.getMissingAttributes().size() > 0) {
			StatusDetailType statusDetail = factory.createStatusDetailType();
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
				missingAttributesJaxb.add(factory
						.createMissingAttributeDetail(madt));
			}
			statusDetail.getContent().addAll(missingAttributesJaxb);
			resCtx.getResponse().getResults().get(0).getStatus()
					.setStatusDetail(statusDetail);
		}
		// Add the Obligations to the response
		if (evaluationContext.getObligations().getObligations().size() > 0) {
			resCtx.getResponse().getResults().get(0)
					.setObligations(evaluationContext.getObligations());
		}
		return resCtx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.herasaf.xacml.core.context.ResponseCtxFactory#create(org.herasaf.
	 * xacml.core.context.impl.RequestType,
	 * org.herasaf.xacml.core.context.impl.DecisionType,
	 * org.herasaf.xacml.core.context.StatusCode)
	 */
	public ResponseCtx create(RequestType req, DecisionType decision,
			StatusCode code) {
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