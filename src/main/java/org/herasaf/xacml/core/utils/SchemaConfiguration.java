package org.herasaf.xacml.core.utils;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.validation.Schema;

import org.herasaf.xacml.core.utils.xml.SchemaLoader;
import org.xml.sax.SAXException;

/**
 * Contains the cconfiguration for a schema.
 * 
 * @author Stefan Oberholzer
 */
public class SchemaConfiguration {

	/** The list with the schema locations. */
	private List<String> schemaLocation;
	/** The given schemaPaths for creating the schema used at validation. */
	private List<String> schemaPaths;
	/** The used Schema. */
	private Schema schema;

	public SchemaConfiguration() {
		schemaLocation = new ArrayList<String>();
		schemaPaths = new ArrayList<String>();

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
	 * Sets the Path to the schema file(s).
	 * 
	 * The files can be loaded from three different resources. URL, Classpath and File (no prefix results in classpath:)
	 * 
	 * Example strings: <br />
	 * <code>url:http://schemas.herasaf.org/mySchema.xsd<br />
	 * classpath:/org/herasaf/schemas/mySchema.xsd<br />
	 * file:C:/herasaf/schemas/mySchema.xsd<br />
	 * /org/herasaf/schemas/mySchema.xsd</code> (no prefix results in classpath:)
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
	public void setSchemaByPath(String... schemaPaths) throws SAXException, MalformedURLException {
		if (ValidationUtils.isEmpty(schemaPaths)) {
			throw new IllegalArgumentException("The parameter SchemaPaths must contain at least one path.");
		}
		this.schemaPaths = Arrays.asList(schemaPaths);

		this.schema = new SchemaLoader().loadSchemas(schemaPaths);

	}

	public Schema getSchema() {
		return this.schema;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("SchemaConfiguration [");
		builder.append("schema = ");
		builder.append(this.schemaPaths.toString());
		builder.append(", ");
		builder.append("schemaLocation = ");
		builder.append(getSchemaLocationAsString());
		builder.append("]");
		return builder.toString();
	}

}
