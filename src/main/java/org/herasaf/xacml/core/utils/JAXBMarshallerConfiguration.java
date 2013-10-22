/*
 * Copyright 2009 - 2012 HERAS-AF (www.herasaf.org)
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

import java.net.MalformedURLException;
import java.util.List;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;

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

	/** Tells if the output should be formatted. */
	private boolean formattedOutput;
	/** Tells if the output should be fragmented. */
	private boolean fragment;

	/** Contains the schema and its location. */
	private SchemaConfiguration schemaConfiguration;
	/** Tells if the schema location should be added to the output. */
	private boolean writeSchemaLocation;

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
		schemaConfiguration = new SchemaConfiguration();
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
		return this.schemaConfiguration.getSchema();
	}

	/**
	 * Sets the Path to the schema file(s).
	 * 
	 * The files can be loaded from three different resources. URL, Classpath
	 * and File (no prefix results in classpath:)
	 * 
	 * Example strings: <br />
	 * <code>url:http://schemas.herasaf.org/mySchema.xsd<br />
	 * classpath:/org/herasaf/schemas/mySchema.xsd<br />
	 * file:C:/herasaf/schemas/mySchema.xsd<br />
	 * /org/herasaf/schemas/mySchema.xsd</code> (no prefix results in
	 * classpath:)
	 * 
	 * @param schemaPaths
	 *            The pathes to the schema files. At leat one path is mandatory.
	 * @throws SAXException
	 *             Error while parsing the schemas occured.
	 * @throws IllegalArgumentException
	 *             no schemaPath was given the referenced schema was invalid.
	 * @throws MalformedURLException
	 *             The path to the schema is invalid.
	 */
	public void setSchemaByPath(String... schemaPaths) throws SAXException,
			MalformedURLException {
		try {
			schemaConfiguration.setSchemaByPath(schemaPaths);
		} catch (SAXException e) {
			setValidateParsing(false);
			setValidateWriting(false);
			logger.warn("Validating turned off because schema could not be initialized.");
		}
	}

	/**
	 * Gets the location of the schema file as string.
	 * 
	 * @return The location of the schema file.
	 */
	public String getSchemaLocationAsString() {
		return schemaConfiguration.getSchemaLocationAsString();
	}

	/**
	 * Sets the schema locations.
	 * 
	 * @param schemaLocation
	 *            The list of schema locations.
	 */
	public void setSchemaLocation(List<String> schemaLocation) {
		schemaConfiguration.setSchemaLocation(schemaLocation);
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
		builder.append("schemaConfiguraiton = {");
		builder.append(this.schemaConfiguration.toString());
		builder.append("}, ");
		builder.append("writeSchemaLocation = ");
		builder.append(isWriteSchemaLocation());
		builder.append("}");
		return builder.toString();
	}

}
