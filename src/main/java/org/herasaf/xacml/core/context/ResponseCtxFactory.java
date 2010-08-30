package org.herasaf.xacml.core.context;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.MissingAttributeDetailType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * Factory to create a {@link ResponseCtx}. Provides various unmarshal methods
 * to create a {@link ResponseCtx}.
 * 
 * <b>Note:</b><br />
 * Because the {@link Unmarshaller} from JAXB <b>is not</b> thread safe it must
 * be created in each unmarshal-method. This class fully relies on the
 * underlying JAXB implementation.
 * 
 * @author Florian Huonder
 */
public interface ResponseCtxFactory {

	/**
	 * Creates a {@link ResponseCtx} from the given {@link File}.
	 * 
	 * @param file
	 *            The {@link File} from which the {@link ResponseCtx} is
	 *            created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	public ResponseCtx unmarshal(File file) throws SyntaxException;

	/**
	 * Creates a {@link ResponseCtx} from the given {@link InputStream}.
	 * 
	 * @param inputStream
	 *            The {@link InputStream} from which the {@link ResponseCtx} is
	 *            created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	public ResponseCtx unmarshal(InputStream inputStream)
			throws SyntaxException;

	/**
	 * Creates a {@link ResponseCtx} from the given {@link Reader}.
	 * 
	 * @param reader
	 *            The {@link Reader} from which the {@link ResponseCtx} is
	 *            created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	public ResponseCtx unmarshal(Reader reader) throws SyntaxException;

	/**
	 * Creates a {@link ResponseCtx} from the given {@link URL}.
	 * 
	 * @param url
	 *            The {@link URL} from which the {@link ResponseCtx} is created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	public ResponseCtx unmarshal(URL url) throws SyntaxException;

	/**
	 * Creates a {@link ResponseCtx} from the given {@link InputSource}.
	 * 
	 * @param inputSource
	 *            The {@link InputSource} from which the {@link ResponseCtx} is
	 *            created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	public ResponseCtx unmarshal(InputSource inputSource)
			throws SyntaxException;

	/**
	 * Creates a {@link ResponseCtx} from the given {@link Node}.
	 * 
	 * @param node
	 *            The {@link Node} from which the {@link ResponseCtx} is
	 *            created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	public ResponseCtx unmarshal(Node node) throws SyntaxException;

	/**
	 * Creates a {@link ResponseCtx} from the given {@link Source}.
	 * 
	 * *
	 * <p>
	 * <b>SAX 2.0 Parser Pluggability</b> <br />
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
	 *            The {@link Source} from which the {@link ResponseCtx} is
	 *            created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 * 
	 */
	public ResponseCtx unmarshal(Source source) throws SyntaxException;

	/**
	 * Creates a {@link ResponseCtx} from the given {@link XMLStreamReader}.
	 * 
	 * <p>
	 * <b>Note:</b><br />
	 * This method assumes that the parser is on a START_DOCUMENT or
	 * START_ELEMENT event. Unmarshalling will be done from this start event to
	 * the corresponding end event. If this method returns successfully, the
	 * reader will be pointing at the token right after the end event.
	 * </p>
	 * 
	 * @param xmlStreamReader
	 *            The {@link XMLStreamReader} from which the {@link ResponseCtx}
	 *            is created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	public ResponseCtx unmarshal(XMLStreamReader xmlStreamReader)
			throws SyntaxException;

	/**
	 * Creates a {@link ResponseCtx} from the given {@link XMLEventReader}.
	 * 
	 * <p>
	 * <b>Note:</b><br />
	 * This method assumes that the parser is on a START_DOCUMENT or
	 * START_ELEMENT event. Unmarshalling will be done from this start event to
	 * the corresponding end event. If this method returns successfully, the
	 * reader will be pointing at the token right after the end event.
	 * </p>
	 * 
	 * @param xmlEventReader
	 *            The {@link XMLEventReader} from which the {@link ResponseCtx}
	 *            is created.
	 * @return The created {@link ResponseCtx}.
	 * @throws SyntaxException
	 *             In case the XML representation contains a syntax error.
	 */
	public ResponseCtx unmarshal(XMLEventReader xmlEventReader)
			throws SyntaxException;

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
	public ResponseCtx create(RequestType req, DecisionType decision,
			EvaluationContext evaluationContext);

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
	public ResponseCtx create(RequestType req, DecisionType decision,
			StatusCode code);

}