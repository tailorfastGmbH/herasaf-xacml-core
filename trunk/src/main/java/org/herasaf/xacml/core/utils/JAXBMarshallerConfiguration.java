/*
 * Copyright 2009 - 2011 HERAS-AF (www.herasaf.org)
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

package org.herasaf.xacml.core.utils;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * This class contains the configuration for the JAXB Marshaller or
 * Unmarshaller.
 * 
 * @author Stefan Oberholzer
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class JAXBMarshallerConfiguration {
	private transient final Logger logger = LoggerFactory
			.getLogger(JAXBMarshallerConfiguration.class);

	private static final String CLASSPATH_PREFIX = "classpath:";
	private static final String FILE_PREFIX = "file:";
	private static final String URL_PREFIX = "url:";

	/** Tells if the output should be formatted. */
	private boolean formattedOutput;
	/** Tells if the output should be fragmented. */
	private boolean fragment;
	/** Tells if the schema location should be added to the output. */
	private boolean writeSchemaLocation;
	/** The list with the schema locations. */
	private List<String> schemaLocation;
	/** The given schemaPath for creating the schema used at validation. */
	private String schemaPath;
	/** The used Schema. */
	private Schema schema;
	/** Tells if the input should be validated before parsing. */
	private boolean validateParsing;
	/** Tells if the output should be validated before writing. */
	private boolean validateWriting;

	/** The used validation event handler (null if default should be used). */
	private ValidationEventHandler validationEventHandler;

	/**
	 * Default Constructor
	 */
	public JAXBMarshallerConfiguration() {
		schemaLocation = new ArrayList<String>();
	}

	/**
	 * Returns the {@link ValidationEventHandler} to be used. Null if the
	 * default validation handler shall be used.
	 * 
	 * @return The {@link ValidationEventHandler} if set.
	 */
	public ValidationEventHandler getValidationEventHandler() {
		return validationEventHandler;
	}

	/**
	 * Sets a {@link ValidationEventHandler} to be used.
	 * 
	 * @param validationEventHandler
	 *            The {@link ValidationEventHandler} to be used.
	 */
	public void setValidationEventHandler(
			ValidationEventHandler validationEventHandler) {
		this.validationEventHandler = validationEventHandler;
	}

	/**
	 * Returns if the output should be formatted.
	 * 
	 * @return True if the output should be formatted, else otherwise.
	 */
	public boolean isFormattedOutput() {
		return formattedOutput;
	}

	/**
	 * Set if the output should be formatted.
	 * 
	 * @param formattedOutput
	 *            True if the output should be formatted, false otherwise.
	 */
	public void setFormattedOutput(boolean formattedOutput) {
		this.formattedOutput = formattedOutput;
	}

	/**
	 * Returns if the output should be fragmented.
	 * 
	 * @return True if the output should be fragmented, false otherwise.
	 */
	public boolean isFragment() {
		return fragment;
	}

	/**
	 * Sets if the output should be fragmented.
	 * 
	 * @param fragment
	 *            True if the output should be fragmented, false otherwise.
	 */
	public void setFragment(boolean fragment) {
		this.fragment = fragment;
	}

	/**
	 * Returns the schema.
	 * 
	 * @return The schema.
	 * @throws SAXException
	 */
	public Schema getSchema() {
		return schema;
	}

	/**
	 * Sets the Path to the schema file.
	 * 
	 * The file can be loaded from three different resources. URL, Classpath and
	 * File (no prefix results in classpath:)
	 * 
	 * Example strings: <br />
	 * <code>url:http://schemas.herasaf.org/mySchema.xsd<br />
	 * classpath:/org/herasaf/schemas/mySchema.xsd<br />
	 * file:C:/herasaf/schemas/mySchema.xsd<br />
	 * /org/herasaf/schemas/mySchema.xsd</code> (no prefix results in
	 * classpath:)
	 * 
	 * @param schemaPath
	 *            The path to the schema file.
	 * @throws SAXException
	 */
	public void setSchemaByPath(String schemaPath) throws SAXException,
			MalformedURLException {
		this.schemaPath = schemaPath;

		SchemaFactory sf = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		String schema = schemaPath.trim();
		if (schema.regionMatches(true, 0, URL_PREFIX, 0, URL_PREFIX.length())) { // if
			// the schemaPath has the url: prefix
			URL url = new URL(schema.substring(URL_PREFIX.length()));
			this.schema = createSchema(sf, new StreamSource(url
					.toExternalForm()));
		} else if (schema.regionMatches(true, 0, FILE_PREFIX, 0, FILE_PREFIX
				.length())) { // if
			// the schemaPath has the file: prefix
			File file = new File(schema.substring(FILE_PREFIX.length()));
			this.schema = createSchema(sf, new StreamSource(file));
		} else if (schema.regionMatches(true, 0, CLASSPATH_PREFIX, 0,
				CLASSPATH_PREFIX.length())) { // if
			// the schemaPath has the classpath: prefix
			InputStream schemaInput = JAXBMarshallerConfiguration.class
					.getResourceAsStream(leadingSlash(schema
							.substring(CLASSPATH_PREFIX.length())));
			if (schemaInput == null) {
				throw new IllegalArgumentException(schema);
			}
			this.schema = createSchema(sf, new StreamSource(schemaInput));
		} else { // if no prefix is provided, the default is classpath:
			logger
					.warn(
							"No prefix (file: || url: || classpath:) given for schema for JAXB validation. Falling back to classpath:{}",
							leadingSlash(schema));

			InputStream schemaInput = JAXBMarshallerConfiguration.class
					.getResourceAsStream(leadingSlash(schema));
			if (schemaInput == null) {
				throw new IllegalArgumentException(schema);
			}
			this.schema = createSchema(sf, new StreamSource(schemaInput));
		}
	}

	/**
	 * Creates a new schema for validating. If the schema given by the source
	 * cannot be loaded <code>null</code> is returned and validating is turned
	 * off.
	 * 
	 * @param sf
	 *            The factory to create the schema.
	 * @param source
	 *            The source where the schema is.
	 * @return The created schema or <code>null</code> if it fails.
	 */
	private Schema createSchema(SchemaFactory sf, Source source) {
		try {
			return sf.newSchema(source);
		} catch (SAXException e) {
			setValidateParsing(false);
			setValidateWriting(false);
			logger
					.warn("Validating turned off because schema could not be initialized.");
		}
		return null;
	}

	/**
	 * checks if a given String has a leading slash and adds one otherwise.
	 * 
	 * @param input
	 *            The String to add a leading slash if missing.
	 * @return The String with a leading slash.
	 */
	private String leadingSlash(String input) {
		if (input.startsWith("/")) {
			return input;
		} else {
			return "/" + input;
		}
	}

	/**
	 * Gets the location of the schema file as string.
	 * 
	 * @return The location of the schema file.
	 */
	public String getSchemaLocationAsString() {
		StringBuilder sb = new StringBuilder();
		for (String str : schemaLocation) {
			sb.append(str);
			sb.append(" ");
		}

		return sb.toString();
	}

	/**
	 * Sets the schema locations.
	 * 
	 * @param schemaLocation
	 *            The list of schema locations.
	 */
	public void setSchemaLocation(List<String> schemaLocation) {
		this.schemaLocation = new ArrayList<String>();
		for (String str : schemaLocation) {
			this.schemaLocation.add(str.trim());
		}
	}

	/**
	 * Tells if the input should be validated before parsing.
	 * 
	 * @return True if the input should be validated, false otherwise.
	 */
	public boolean isValidateParsing() {
		return validateParsing;
	}

	/**
	 * Sets if the input should be validated before parsing.
	 * 
	 * @param validate
	 *            True if the input should be validated, false otherwise.
	 */
	public void setValidateParsing(boolean validate) {
		this.validateParsing = validate;
	}

	/**
	 * Tells if the output should be validated before writing.
	 * 
	 * @return True if the output should be validated, false otherwise.
	 */
	public boolean isValidateWriting() {
		return validateWriting;
	}

	/**
	 * Sets if the output should be validated before writing.
	 * 
	 * @param validateWriting
	 *            True if the output should be validated, false otherwise.
	 */
	public void setValidateWriting(boolean validateWriting) {
		this.validateWriting = validateWriting;
	}

	/**
	 * Tells if the schema location should be added to the output.
	 * 
	 * @return True if the schema location should be added to the output, false
	 *         otherwise.
	 */
	public boolean isWriteSchemaLocation() {
		return writeSchemaLocation;
	}

	/**
	 * Sets if the schema location should be added to the output.
	 * 
	 * @param writeSchemaLocation
	 *            True if the schema location should be added to the output,
	 *            false otherwise.
	 */
	public void setWriteSchemaLocation(boolean writeSchemaLocation) {
		this.writeSchemaLocation = writeSchemaLocation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append("formattedOutput = ");
		builder.append(isFormattedOutput());
		builder.append(", ");
		builder.append("fragment = ");
		builder.append(isFormattedOutput());
		builder.append(", ");
		builder.append("validateParsing = ");
		builder.append(isValidateParsing());
		builder.append(", ");
		builder.append("validateWriting = ");
		builder.append(isValidateWriting());
		builder.append(", ");
		builder.append("schema = ");
		builder.append(schemaPath);
		builder.append(", ");
		builder.append("writeSchemaLocation = ");
		builder.append(isWriteSchemaLocation());
		builder.append(", ");
		builder.append("schemaLocation = ");
		builder.append(getSchemaLocationAsString());
		builder.append("}");
		return builder.toString();
	}
}