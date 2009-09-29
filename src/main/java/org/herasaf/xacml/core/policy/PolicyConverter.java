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

package org.herasaf.xacml.core.policy;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.WritingException;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.impl.ObjectFactory;
import org.herasaf.xacml.core.policy.impl.PolicySetType;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.herasaf.xacml.core.utils.ContextAndPolicy;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;

/**
 * TODO JAVADOC
 * 
 * Marshalls and Unmarshalls {@link Evaluatable}s to and from the given sources
 * and sinks respectively. Every access creates a new {@link Marshaller} or
 * {@link Unmarshaller} respectively because the JAXB Specification currently
 * does not address the thread safety of any of the runtime classes. In the case
 * of the Sun JAXB RI, the JAXBContext class <b>is</b> thread safe, but the
 * {@link Marshaller}, {@link Unmarshaller}, and Validator classes are <b>not</b>
 * thread safe. (for further information see the <a
 * href="https://jaxb.dev.java.net/faq/index.html#threadSafety">Jaxb-FAQ</a>)
 *
 * @author Florian Huonder 
 * @version 1.0
 */
public final class PolicyConverter {
	private static final ObjectFactory objectFactory;
	private static final ContextAndPolicy.JAXBProfile CONTEXT = ContextAndPolicy.JAXBProfile.POLICY;

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
	 * It should not be possible to instantiate such an object. The
	 * {@link PolicyConverter} must be accessed in a static way.
	 */
	private PolicyConverter() {
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Marshals the {@link RequestType} to the given {@link ContentHandler}.
	 *
	 * @param ch
	 *            The {@link ContentHandler} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshall.
	 * @throws WritingException
	 */
	public static void marshall(Evaluatable evaluatable, ContentHandler ch)
			throws WritingException {
		try {
			if (evaluatable instanceof PolicySetType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory
								.createPolicySet((PolicySetType) evaluatable),
						ch);
			} else if (evaluatable instanceof PolicyType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory.createPolicy((PolicyType) evaluatable),
						ch);
			} else {
				throw new WritingException(
						"Cannot marshall an object of type: "
								+ evaluatable.getClass());
			}

		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Marshals the {@link RequestType} to the given {@link File}.
	 *
	 * @param file
	 *            The {@link File} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshall.
	 * @throws WritingException
	 */
	public static void marshall(Evaluatable evaluatable, File file)
			throws WritingException {
		try {
			if (evaluatable instanceof PolicySetType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory
								.createPolicySet((PolicySetType) evaluatable),
						file);
			} else if (evaluatable instanceof PolicyType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory.createPolicy((PolicyType) evaluatable),
						file);
			} else {
				throw new WritingException(
						"Cannot marshall an object of type: "
								+ evaluatable.getClass());
			}

		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Marshals the {@link RequestType} to the given {@link Result}.
	 *
	 * @param result
	 *            The {@link Result} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshall.
	 * @throws WritingException
	 */
	public static void marshall(Evaluatable evaluatable, Result result)
			throws WritingException {
		try {
			if (evaluatable instanceof PolicySetType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory
								.createPolicySet((PolicySetType) evaluatable),
						result);
			} else if (evaluatable instanceof PolicyType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory.createPolicy((PolicyType) evaluatable),
						result);
			} else {
				throw new WritingException(
						"Cannot marshall an object of type: "
								+ evaluatable.getClass());
			}

		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Marshals the {@link RequestType} to the given {@link OutputStream}.
	 *
	 * @param out
	 *            The {@link OutputStream} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshall.
	 * @throws WritingException
	 */
	public static void marshall(Evaluatable evaluatable, OutputStream out)
			throws WritingException {
		try {
			if (evaluatable instanceof PolicySetType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory
								.createPolicySet((PolicySetType) evaluatable),
						out);
			} else if (evaluatable instanceof PolicyType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory.createPolicy((PolicyType) evaluatable),
						out);
			} else {
				throw new WritingException(
						"Cannot marshall an object of type: "
								+ evaluatable.getClass());
			}

		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Marshals the {@link RequestType} to the given {@link Writer}.
	 *
	 * @param writer
	 *            The {@link Writer} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshall.
	 * @throws WritingException
	 */
	public static void marshall(Evaluatable evaluatable, Writer writer)
			throws WritingException {
		try {
			if (evaluatable instanceof PolicySetType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory
								.createPolicySet((PolicySetType) evaluatable),
						writer);
			} else if (evaluatable instanceof PolicyType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory.createPolicy((PolicyType) evaluatable),
						writer);
			} else {
				throw new WritingException(
						"Cannot marshall an object of type: "
								+ evaluatable.getClass());
			}

		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Marshals the {@link RequestType} to the given {@link Node}.
	 *
	 * @param node
	 *            The {@link Node} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshall.
	 * @throws WritingException
	 */
	public static void marshall(Evaluatable evaluatable, Node node)
			throws WritingException {
		try {
			if (evaluatable instanceof PolicySetType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory
								.createPolicySet((PolicySetType) evaluatable),
						node);
			} else if (evaluatable instanceof PolicyType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory.createPolicy((PolicyType) evaluatable),
						node);
			} else {
				throw new WritingException(
						"Cannot marshall an object of type: "
								+ evaluatable.getClass());
			}

		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Marshals the {@link RequestType} to the given {@link XMLStreamWriter}.
	 *
	 * @param xmlStreamWriter
	 *            The {@link XMLStreamWriter} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshall.
	 * @throws WritingException
	 */
	public static void marshall(Evaluatable evaluatable,
			XMLStreamWriter xmlStreamWriter) throws WritingException {
		try {
			if (evaluatable instanceof PolicySetType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory
								.createPolicySet((PolicySetType) evaluatable),
						xmlStreamWriter);
			} else if (evaluatable instanceof PolicyType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory.createPolicy((PolicyType) evaluatable),
						xmlStreamWriter);
			} else {
				throw new WritingException(
						"Cannot marshall an object of type: "
								+ evaluatable.getClass());
			}

		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Marshals the {@link RequestType} to the given {@link XMLEventWriter}.
	 *
	 * @param xmlEventWriter
	 *            The {@link XMLEventWriter} to use.
	 * @param evaluatable
	 *            The {@link Evaluatable} to marshall.
	 * @throws WritingException
	 */
	public static void marshall(Evaluatable evaluatable,
			XMLEventWriter xmlEventWriter) throws WritingException {
		try {
			if (evaluatable instanceof PolicySetType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory
								.createPolicySet((PolicySetType) evaluatable),
						xmlEventWriter);
			} else if (evaluatable instanceof PolicyType) {
				ContextAndPolicy.getMarshaller(CONTEXT).marshal(
						objectFactory.createPolicy((PolicyType) evaluatable),
						xmlEventWriter);
			} else {
				throw new WritingException(
						"Cannot marshall an object of type: "
								+ evaluatable.getClass());
			}

		} catch (JAXBException e) {
			throw new WritingException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Creates a {@link Evaluatable} object from the given {@link File}.
	 *
	 * @param file
	 *            The {@link File} from which the {@link Evaluatable} is
	 *            created.
	 * @return The newly created {@link Evaluatable}.
	 * @throws SyntaxException
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(File file) throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(CONTEXT);
			return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(file))
					.getValue();
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Creates a {@link Evaluatable} object from the given {@link InputStream}.
	 *
	 * @param inputStream
	 *            The {@link InputStream} from which the {@link Evaluatable} is
	 *            created.
	 * @return The newly created {@link Evaluatable}.
	 * @throws SyntaxException
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(InputStream inputStream)
			throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(CONTEXT);
			return ((JAXBElement<Evaluatable>) unmarshaller
					.unmarshal(inputStream)).getValue();
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Creates a {@link Evaluatable} object from the given {@link Reader}.
	 *
	 * @param reader
	 *            The {@link Reader} from which the {@link Evaluatable} is
	 *            created.
	 * @return The newly created {@link Evaluatable}.
	 * @throws SyntaxException
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(Reader reader) throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(CONTEXT);
			return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(reader))
					.getValue();
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Creates a {@link Evaluatable} object from the given {@link URL}.
	 *
	 * @param url
	 *            The {@link URL} from which the {@link Evaluatable} is created.
	 * @return The newly created {@link Evaluatable}.
	 * @throws SyntaxException
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(URL url) throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(CONTEXT);
			return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(url))
					.getValue();
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Creates a {@link Evaluatable} object from the given {@link InputSource}.
	 *
	 * @param inputSource
	 *            The {@link InputSource} from which the {@link Evaluatable} is
	 *            created.
	 * @return The newly created {@link Evaluatable}.
	 * @throws SyntaxException
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(InputSource inputSource)
			throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(CONTEXT);
			return ((JAXBElement<Evaluatable>) unmarshaller
					.unmarshal(inputSource)).getValue();
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Creates a {@link Evaluatable} object from the given {@link Node}.
	 *
	 * @param node
	 *            The {@link Node} from which the {@link Evaluatable} is
	 *            created.
	 * @return The newly created {@link Evaluatable}.
	 * @throws SyntaxException
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(Node node) throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(CONTEXT);
			return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(node))
					.getValue();
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * TODO JAVADOC

	 * Creates a {@link Evaluatable} object from the given {@link Node}.
	 *
	 * @param source
	 *            The {@link Node} from which the {@link Evaluatable} is
	 *            created.
	 * @return The newly created {@link Evaluatable}.
	 * @throws SyntaxException
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(Source source) throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(CONTEXT);
			return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(source))
					.getValue();
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Creates a {@link Evaluatable} object from the given
	 * {@link XMLStreamReader}.
	 *
	 * @param xmlStreamReader
	 *            The {@link XMLStreamReader} from which the {@link Evaluatable}
	 *            is created.
	 * @return The newly created {@link Evaluatable}.
	 * @throws SyntaxException
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(XMLStreamReader xmlStreamReader)
			throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(CONTEXT);
			return ((JAXBElement<Evaluatable>) unmarshaller
					.unmarshal(xmlStreamReader)).getValue();
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}

	/**
	 * TODO JAVADOC
	 * 
	 * Creates a {@link Evaluatable} object from the given
	 * {@link XMLEventReader}.
	 *
	 * @param xmlEventReader
	 *            The {@link XMLEventReader} from which the {@link Evaluatable}
	 *            is created.
	 * @return The newly created {@link Evaluatable}.
	 * @throws SyntaxException
	 */
	@SuppressWarnings("unchecked")
	public static Evaluatable unmarshal(XMLEventReader xmlEventReader)
			throws SyntaxException {
		try {
			Unmarshaller unmarshaller = ContextAndPolicy
					.getUnmarshaller(CONTEXT);
			return ((JAXBElement<Evaluatable>) unmarshaller
					.unmarshal(xmlEventReader)).getValue();
		} catch (JAXBException e) {
			throw new SyntaxException(e);
		} catch (ClassCastException e) {
			throw new SyntaxException(e);
		}
	}
}