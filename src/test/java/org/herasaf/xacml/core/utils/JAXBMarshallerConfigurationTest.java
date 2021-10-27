package org.herasaf.xacml.core.utils;

import java.net.MalformedURLException;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.validation.SchemaFactory;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

/**
 * Unittest to test the <class>JAXBMarshallerConfiguration</class> class.
 * 
 * @author Stefan Oberholzer (soberhol@herasaf.org)
 * 
 */
public class JAXBMarshallerConfigurationTest {

	private JAXBMarshallerConfiguration config;

	@BeforeTest
	public void setUp() {
		config = new JAXBMarshallerConfiguration();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetSchemaByPathNoParameter() throws MalformedURLException,
			SAXException {
		config.setSchemaByPath();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSetSchemaByPathEmptyList() throws MalformedURLException,
			SAXException {
		config.setSchemaByPath();
	}

	// TODO Implement
	public void testCreateSchema(SchemaFactory t, Source... s) {
	}

	// TODO Implement
	public void testLeadingSlash(String t) {
	}

	// TODO Implement
	public void testGetSchemaLocationAsString() {
	}

	// TODO Implement
	public void testSetSchemaLocation(List<String> t) {
	}

	// TODO Implement
	public void testIsValidateParsing() {
	}

	// TODO Implement
	public void testSetValidateParsing(boolean t) {
	}

	// TODO Implement
	public void testIsValidateWriting() {
	}

	// TODO Implement
	public void testSetValidateWriting(boolean t) {
	}

	// TODO Implement
	public void testIsWriteSchemaLocation() {
	}

	// TODO Implement
	public void testSetWriteSchemaLocation(boolean t) {
	}

	// TODO Implement
	public void testToString() {
	}
}
