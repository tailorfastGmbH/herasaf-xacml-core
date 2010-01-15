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
import org.herasaf.xacml.core.policy.impl.ObjectFactory;
import org.herasaf.xacml.core.policy.impl.PolicySetType;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.herasaf.xacml.core.utils.ContextAndPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;

/**
 * Marshals and unmarshals {@link Evaluatable}s to and from the given sources and sinks, respectively. Every access to a
 * marshal or unmarshal method creates a new {@link Marshaller} or {@link Unmarshaller}, respectively because the JAXB
 * Specification currently does not address the thread safety of any of the runtime classes. In the case of the Sun JAXB
 * RI, the JAXBContext class <b>is</b> thread safe, but the {@link Marshaller}, {@link Unmarshaller}, and Validator
 * classes are <b>not</b> thread safe. (for further information see the <a
 * href="https://jaxb.dev.java.net/faq/index.html#threadSafety">Jaxb-FAQ</a>)
 * 
 * @author Florian Huonder
 */
public final class PolicyConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PolicyConverter.class);
    private static final ObjectFactory OBJECT_FACTORY;
    private static final ContextAndPolicy.JAXBProfile CONTEXT = ContextAndPolicy.JAXBProfile.POLICY;

    /**
     * Initializes the object factory.
     */
    static {
        OBJECT_FACTORY = new ObjectFactory();
    }

    /**
     * The default constructor is private because it must no be possible to instantiate an object of this type. It is
     * intended to be used in a static way.
     */
    private PolicyConverter() {
    }

    /**
     * Marshals the given {@link Evaluatable} to the given {@link ContentHandler}.
     * 
     * @param ch
     *            The {@link ContentHandler} to use.
     * @param evaluatable
     *            The {@link Evaluatable} to marshal.
     * @throws WritingException
     *             In case an error occurs.
     */
    public static void marshal(Evaluatable evaluatable, ContentHandler ch) throws WritingException {
        try {
            if (evaluatable instanceof PolicySetType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(
                        OBJECT_FACTORY.createPolicySet((PolicySetType) evaluatable), ch);
            } else if (evaluatable instanceof PolicyType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(OBJECT_FACTORY.createPolicy((PolicyType) evaluatable),
                        ch);
            } else {
                WritingException e = new WritingException("Unable to marshal an object of type: "
                        + evaluatable.getClass());
                LOGGER.error(e.getMessage());
                throw e;
            }

        } catch (JAXBException e) {
            WritingException ie = new WritingException("Unable to write to the context handler.", e);
            LOGGER.error(ie.getMessage());
            throw ie;
        }
    }

    /**
     * Marshals the given {@link Evaluatable} to the given {@link File}.
     * 
     * @param file
     *            The {@link File} to use.
     * @param evaluatable
     *            The {@link Evaluatable} to marshal.
     * @throws WritingException
     *             In case an error occurs.
     */
    public static void marshal(Evaluatable evaluatable, File file) throws WritingException {
        try {
            if (evaluatable instanceof PolicySetType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(
                        OBJECT_FACTORY.createPolicySet((PolicySetType) evaluatable), file);
            } else if (evaluatable instanceof PolicyType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(OBJECT_FACTORY.createPolicy((PolicyType) evaluatable),
                        file);
            } else {
                WritingException e = new WritingException("Unable to marshal an object of type: "
                        + evaluatable.getClass());
                LOGGER.error(e.getMessage());
                throw e;
            }

        } catch (JAXBException e) {
            WritingException ie = new WritingException("Unable to write to the file.", e);
            LOGGER.error(ie.getMessage());
            throw ie;
        }
    }

    /**
     * Marshals the given {@link Evaluatable} to the given {@link Result}.
     * 
     * @param result
     *            The {@link Result} to use.
     * @param evaluatable
     *            The {@link Evaluatable} to marshal.
     * @throws WritingException
     *             In case an error occurs.
     */
    public static void marshal(Evaluatable evaluatable, Result result) throws WritingException {
        try {
            if (evaluatable instanceof PolicySetType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(
                        OBJECT_FACTORY.createPolicySet((PolicySetType) evaluatable), result);
            } else if (evaluatable instanceof PolicyType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(OBJECT_FACTORY.createPolicy((PolicyType) evaluatable),
                        result);
            } else {
                WritingException e = new WritingException("Unable to marshal an object of type: "
                        + evaluatable.getClass());
                LOGGER.error(e.getMessage());
                throw e;
            }

        } catch (JAXBException e) {
            WritingException ie = new WritingException("Unable to write to the result.", e);
            LOGGER.error(ie.getMessage());
            throw ie;
        }
    }

    /**
     * Marshals the given {@link Evaluatable} to the given {@link OutputStream}.
     * 
     * @param out
     *            The {@link OutputStream} to use.
     * @param evaluatable
     *            The {@link Evaluatable} to marshal.
     * @throws WritingException
     *             In case an error occurs.
     */
    public static void marshal(Evaluatable evaluatable, OutputStream out) throws WritingException {
        try {
            if (evaluatable instanceof PolicySetType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(
                        OBJECT_FACTORY.createPolicySet((PolicySetType) evaluatable), out);
            } else if (evaluatable instanceof PolicyType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(OBJECT_FACTORY.createPolicy((PolicyType) evaluatable),
                        out);
            } else {
                WritingException e = new WritingException("Unable to marshal an object of type: "
                        + evaluatable.getClass());
                LOGGER.error(e.getMessage());
                throw e;
            }

        } catch (JAXBException e) {
            WritingException ie = new WritingException("Unable to write to the output stream.", e);
            LOGGER.error(ie.getMessage());
            throw ie;
        }
    }

    /**
     * Marshals the given {@link Evaluatable} to the given {@link Writer}.
     * 
     * @param writer
     *            The {@link Writer} to use.
     * @param evaluatable
     *            The {@link Evaluatable} to marshal.
     * @throws WritingException
     *             In case an error occurs.
     */
    public static void marshal(Evaluatable evaluatable, Writer writer) throws WritingException {
        try {
            if (evaluatable instanceof PolicySetType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(
                        OBJECT_FACTORY.createPolicySet((PolicySetType) evaluatable), writer);
            } else if (evaluatable instanceof PolicyType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(OBJECT_FACTORY.createPolicy((PolicyType) evaluatable),
                        writer);
            } else {
                WritingException e = new WritingException("Unable to marshal an object of type: "
                        + evaluatable.getClass());
                LOGGER.error(e.getMessage());
                throw e;
            }

        } catch (JAXBException e) {
            WritingException ie = new WritingException("Unable to write to the writer.", e);
            LOGGER.error(ie.getMessage());
            throw ie;
        }
    }

    /**
     * Marshals the given {@link Evaluatable} to the given {@link Node}.
     * 
     * @param node
     *            The {@link Node} to use.
     * @param evaluatable
     *            The {@link Evaluatable} to marshal.
     * @throws WritingException
     *             In case an error occurs.
     */
    public static void marshal(Evaluatable evaluatable, Node node) throws WritingException {
        try {
            if (evaluatable instanceof PolicySetType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(
                        OBJECT_FACTORY.createPolicySet((PolicySetType) evaluatable), node);
            } else if (evaluatable instanceof PolicyType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(OBJECT_FACTORY.createPolicy((PolicyType) evaluatable),
                        node);
            } else {
                WritingException e = new WritingException("Unable to marshal an object of type: "
                        + evaluatable.getClass());
                LOGGER.error(e.getMessage());
                throw e;
            }

        } catch (JAXBException e) {
            WritingException ie = new WritingException("Unable to write to the node.", e);
            LOGGER.error(ie.getMessage());
            throw ie;
        }
    }

    /**
     * Marshals the given {@link Evaluatable} to the given {@link XMLStreamWriter}.
     * 
     * @param xmlStreamWriter
     *            The {@link XMLStreamWriter} to use.
     * @param evaluatable
     *            The {@link Evaluatable} to marshal.
     * @throws WritingException
     *             In case an error occurs.
     */
    public static void marshal(Evaluatable evaluatable, XMLStreamWriter xmlStreamWriter) throws WritingException {
        try {
            if (evaluatable instanceof PolicySetType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(
                        OBJECT_FACTORY.createPolicySet((PolicySetType) evaluatable), xmlStreamWriter);
            } else if (evaluatable instanceof PolicyType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(OBJECT_FACTORY.createPolicy((PolicyType) evaluatable),
                        xmlStreamWriter);
            } else {
                WritingException e = new WritingException("Unable to marshal an object of type: "
                        + evaluatable.getClass());
                LOGGER.error(e.getMessage());
                throw e;
            }

        } catch (JAXBException e) {
            WritingException ie = new WritingException("Unable to write to the xml stream writer.", e);
            LOGGER.error(ie.getMessage());
            throw ie;
        }
    }

    /**
     * Marshals the given {@link Evaluatable} to the given {@link XMLEventWriter}.
     * 
     * @param xmlEventWriter
     *            The {@link XMLEventWriter} to use.
     * @param evaluatable
     *            The {@link Evaluatable} to marshal.
     * @throws WritingException
     *             In case an error occurs.
     */
    public static void marshal(Evaluatable evaluatable, XMLEventWriter xmlEventWriter) throws WritingException {
        try {
            if (evaluatable instanceof PolicySetType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(
                        OBJECT_FACTORY.createPolicySet((PolicySetType) evaluatable), xmlEventWriter);
            } else if (evaluatable instanceof PolicyType) {
                ContextAndPolicy.getMarshaller(CONTEXT).marshal(OBJECT_FACTORY.createPolicy((PolicyType) evaluatable),
                        xmlEventWriter);
            } else {
                WritingException e = new WritingException("Unable to marshal an object of type: "
                        + evaluatable.getClass());
                LOGGER.error(e.getMessage());
                throw e;
            }

        } catch (JAXBException e) {
            WritingException ie = new WritingException("Unable to write to the xml event writer.", e);
            LOGGER.error(ie.getMessage());
            throw ie;
        }
    }

    /**
     * Creates an {@link Evaluatable} from the given {@link File}.
     * 
     * @param file
     *            The {@link File} from which the {@link Evaluatable} is created.
     * @return The created {@link Evaluatable}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static Evaluatable unmarshal(File file) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(CONTEXT);
            return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(file)).getValue();
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
     * Creates an {@link Evaluatable} from the given {@link InputStream}.
     * 
     * @param inputStream
     *            The {@link InputStream} from which the {@link Evaluatable} is created.
     * @return The created {@link Evaluatable}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static Evaluatable unmarshal(InputStream inputStream) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(CONTEXT);
            return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(inputStream)).getValue();
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
     * Creates an {@link Evaluatable} from the given {@link Reader}.
     * 
     * @param reader
     *            The {@link Reader} from which the {@link Evaluatable} is created.
     * @return The created {@link Evaluatable}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static Evaluatable unmarshal(Reader reader) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(CONTEXT);
            return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(reader)).getValue();
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
     * Creates an {@link Evaluatable} from the given {@link URL}.
     * 
     * @param url
     *            The {@link URL} from which the {@link Evaluatable} is created.
     * @return The created {@link Evaluatable}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static Evaluatable unmarshal(URL url) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(CONTEXT);
            return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(url)).getValue();
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
     * Creates an {@link Evaluatable} from the given {@link InputSource}.
     * 
     * @param inputSource
     *            The {@link InputSource} from which the {@link Evaluatable} is created.
     * @return The created {@link Evaluatable}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static Evaluatable unmarshal(InputSource inputSource) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(CONTEXT);
            return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(inputSource)).getValue();
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
     * Creates an {@link Evaluatable} from the given {@link Node}.
     * 
     * @param node
     *            The {@link Node} from which the {@link Evaluatable} is created.
     * @return The created {@link Evaluatable}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static Evaluatable unmarshal(Node node) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(CONTEXT);
            return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(node)).getValue();
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
     * Creates an {@link Evaluatable} from the given {@link Source}.
     * 
     * @param source
     *            The {@link Node} from which the {@link Evaluatable} is created.
     * @return The created {@link Evaluatable}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static Evaluatable unmarshal(Source source) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(CONTEXT);
            return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(source)).getValue();
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
     * Creates an {@link Evaluatable} from the given {@link XMLStreamReader}.
     * 
     * @param xmlStreamReader
     *            The {@link XMLStreamReader} from which the {@link Evaluatable} is created.
     * @return The created {@link Evaluatable}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static Evaluatable unmarshal(XMLStreamReader xmlStreamReader) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(CONTEXT);
            return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(xmlStreamReader)).getValue();
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
     * Creates an {@link Evaluatable} from the given {@link XMLEventReader}.
     * 
     * @param xmlEventReader
     *            The {@link XMLEventReader} from which the {@link Evaluatable} is created.
     * @return The created {@link Evaluatable}.
     * @throws SyntaxException
     *             In case the XML representation contains a syntax error.
     */
    @SuppressWarnings("unchecked")
    public static Evaluatable unmarshal(XMLEventReader xmlEventReader) throws SyntaxException {
        try {
            Unmarshaller unmarshaller = ContextAndPolicy.getUnmarshaller(CONTEXT);
            return ((JAXBElement<Evaluatable>) unmarshaller.unmarshal(xmlEventReader)).getValue();
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