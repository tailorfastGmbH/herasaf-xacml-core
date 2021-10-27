package org.herasaf.xacml.core.utils.xml;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.herasaf.xacml.core.utils.JAXBMarshallerConfiguration;
import org.herasaf.xacml.core.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * Helper utility for loading an xml schema.
 * 
 * @author Stefan Oberholzer
 */
public class SchemaLoader {
	private static final Logger logger = LoggerFactory.getLogger(SchemaLoader.class);

	private static final String CLASSPATH_PREFIX = "classpath:";
	private static final String FILE_PREFIX = "file:";
	private static final String URL_PREFIX = "url:";

	/**
	 * Initializes a {@link Schema} with the given schema paths.
	 * 
	 * @param schemaPaths
	 *            The paths of the {@link Schema} to create.
	 * 
	 * @return The created {@link Schema}.
	 * 
	 * @throws MalformedURLException
	 *             When there is a malformed schema path.
	 * @throws SAXException
	 *             When the given schema cannot be parsed.
	 */
	public Schema loadSchemas(String[] schemaPaths) throws MalformedURLException, SAXException {
		Source[] schemaSources = new StreamSource[schemaPaths.length];
		for (int i = 0; i < schemaPaths.length; i++) {
			String schemaPath = schemaPaths[i].trim();
			schemaSources[i] = loadSource(schemaPath);
		}
		return createSchema(schemaSources);
	}

	private Source loadSource(String schemaPath) throws MalformedURLException {
		Source schemaSource;
		if (isUrlPath(schemaPath)) { // if
			// the schemaPath has the url: prefix
			schemaSource = loadUrlSource(schemaPath);
		} else if (isFilePath(schemaPath)) { // if
			// the schemaPath has the file: prefix
			schemaSource = loadFileSource(schemaPath);
		} else if (isClassPath(schemaPath)) { // if
			// the schemaPath has the classpath: prefix
			schemaSource = loadClasspathSource(schemaPath);
		} else { // if no prefix is provided, the default is classpath:
			logger.warn(
					"No prefix (file: || url: || classpath:) given for schema for JAXB validation. Falling back to classpath:{}",
					ensureLeadingSlash(schemaPath));
			schemaSource = loadClasspathSource(schemaPath);
		}
		return schemaSource;
	}

	private boolean isClassPath(String schemaPath) {
		return schemaPath.regionMatches(true, 0, CLASSPATH_PREFIX, 0, CLASSPATH_PREFIX.length());
	}

	private Source loadClasspathSource(String schemaPath) {

		schemaPath = removePrefix(schemaPath, CLASSPATH_PREFIX);
		InputStream schemaInput = JAXBMarshallerConfiguration.class.getResourceAsStream(ensureLeadingSlash(schemaPath));
		if (schemaInput == null) {
			throw new IllegalArgumentException(schemaPath);
		}
		return new StreamSource(schemaInput);
	}

	private boolean isFilePath(String schemaPath) {
		return schemaPath.regionMatches(true, 0, FILE_PREFIX, 0, FILE_PREFIX.length());
	}

	private Source loadFileSource(String schemaPath) {
		Source schemaSource;
		File file = new File(removePrefix(schemaPath, FILE_PREFIX));
		schemaSource = new StreamSource(file);
		return schemaSource;
	}

	private boolean isUrlPath(String schema) {
		return schema.regionMatches(true, 0, URL_PREFIX, 0, URL_PREFIX.length());
	}

	private Source loadUrlSource(String schemaPath) throws MalformedURLException {
		Source schemaSource;
		URL url = new URL(removePrefix(schemaPath, URL_PREFIX));
		schemaSource = new StreamSource(url.toExternalForm());
		return schemaSource;
	}

	private String removePrefix(String schemaPath, String prefix) {
		if (schemaPath.contains(schemaPath)) {
			schemaPath = schemaPath.substring(prefix.length());
		}
		return schemaPath;
	}

	/**
	 * checks if a given String has a leading slash and adds one otherwise.
	 * 
	 * @param input
	 *            The String to add a leading slash if missing.
	 * @return The String with a leading slash.
	 */
	private String ensureLeadingSlash(String input) {
		return input.startsWith("/") ? input : "/" + input;
	}

	/**
	 * Creates a new schema for validating. If the schema given by the source(s) cannot be loaded <code>null</code> is
	 * returned and validating is turned off.
	 * 
	 * @param sf
	 *            The factory to create the schema.
	 * @param source
	 *            The source(s) where the schema is(are). At least one source is mandatory.
	 * @throw IllegalArgumentException If the parameter source is empty.
	 * 
	 * @return The created schema or <code>null</code> if it fails.
	 */
	private Schema createSchema(Source... source) throws SAXException {
		if (ValidationUtils.isEmpty(source)) {
			throw new IllegalArgumentException("The parameter source must contain at least one value");
		}
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		return sf.newSchema(source);

	}

}
