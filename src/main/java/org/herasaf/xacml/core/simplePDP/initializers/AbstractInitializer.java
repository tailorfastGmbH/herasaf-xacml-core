/*
 * Copyright 2009-2010 HERAS-AF (www.herasaf.org)
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
package org.herasaf.xacml.core.simplePDP.initializers;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.herasaf.xacml.core.simplePDP.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This initializer is the base class for all initializers that load classes for
 * the classpath and make them available for the JAXB type-adapters.
 * 
 * @param <T>
 *            The type this initializer is made for.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public abstract class AbstractInitializer<T> implements Initializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractInitializer.class);
	private static final String CLASS_ENDING = ".class";
	private static final String DOLLAR_SIGN = "$";
	private static final String MOCK_KEYWORD = "Mock";
	private static final String ABSTRACT_KEYWORD = "Abstract";
	private static final String URL_PROTOCOL_JAR = "jar";
	private static final String URL_PROTOCOL_FILE = "file";
	private static final String URL_PROTOCOL_ZIP = "zip";
	private static final String URL_PROTOCOL_WSJAR = "wsjar";
	private static final String URL_PROTOCOL_CODE_SOURCE = "code-source";
	private static final String JAR_URL_SEPARATOR = "!/";

	/**
	 * This method returns the search context within the classpath (the search
	 * context's delimiter is a &quot;.&quot;).
	 * 
	 * @return The String containing the search context.
	 */
	protected abstract String getSearchContext();

	/**
	 * This method returns the search context within the classpath (the search
	 * context's delimiter is a &quot;/&quot;).
	 * 
	 * @return The String containing the search context.
	 */
	protected abstract String getSearchContextPath();

	/**
	 * Returns the {@link Class} of the type T. This is needed for a proper
	 * instantiation.
	 * 
	 * @return Returns the {@link Class} of the type T.
	 */
	protected abstract Class<T> getTargetClass();

	/**
	 * Returns the URI, id, of the instance of type T. This method is needed
	 * because it cannot be determined from T how the id can be obtained.
	 * 
	 * @param instance
	 *            The type from which the URI shall be returned.
	 * @return The URI, id, of the given instance.
	 */
	protected abstract String getURIFromType(T instance);

	/**
	 * Sets the instances into the converter map. The converter map is the map
	 * needed by the JAXB type adapter.
	 * 
	 * @param instancesMap
	 *            The map containing the instances. Key is the id of the object
	 *            of type T, the value is the object itself.
	 */
	protected abstract void setInstancesIntoConverter(Map<String, T> instancesMap);

	/**
	 * {@inheritDoc}
	 */
	public void run() {
		Set<String> classNames = new HashSet<String>();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		// Get all resources that are at the location given by the
		// searchContext.
		Enumeration<URL> resourceURLs;
		try {
			resourceURLs = cl.getResources(getSearchContextPath());
		} catch (IOException e1) {
			InitializationException ie = new InitializationException("Unable to get resources from classpath.");
			LOGGER.error(ie.getMessage());
			throw ie;
		}

		while (resourceURLs.hasMoreElements()) {
			URL url = resourceURLs.nextElement();
			if (isJarURL(url)) {
				/* JAR handling */
				classNames = loadClassNamesFromJar(url);
			} else if (URL_PROTOCOL_FILE.equals(url.getProtocol())) {
				/* Directory handling */
				classNames = collectClassNamesFromFile(url);
			} else {
				InitializationException e = new InitializationException(
						"The search context path must either point to a JAR (.jar, .zip (BEA WebLogic, WebSphere), .wsjar (BEA WebLogic, WebSphere), code-source (Oracle OC4J)) file or to a directory");
				LOGGER.error(e.getMessage());
				throw e;
			}
		}

		Set<T> instances = createInstancesFromClassNames(classNames, getTargetClass());
		Map<String, T> instancesMap = convertSetToMap(instances);
		setInstancesIntoConverter(instancesMap);
		furtherInitializations(instances);
	}

	/**
	 * This method allows to do further initialization steps on the instantiated
	 * classes of type T. By default this method does nothing. It may be
	 * overridden by an implementing subclass.
	 * 
	 * @param instances
	 *            The instances of type T.
	 */
	protected void furtherInitializations(Set<T> instances) {
		// to be overridden in concrete subclass if needed.
	}

	/**
	 * Converts the list of instances into a map where the key is the id of the
	 * instance.
	 * 
	 * @param instances
	 *            The {@link List} of instances that shall be converted into a
	 *            {@link Map}.
	 * @return The {@link Map} containing the instances.
	 */
	private Map<String, T> convertSetToMap(Set<T> instances) {
		Map<String, T> targetMap = new HashMap<String, T>();
		for (T instance : instances) {
			targetMap.put(getURIFromType(instance), instance);
		}
		return targetMap;
	}

	/**
	 * This method determines if the given {@link URL} points to a JAR file.
	 * This is if it has protocol "jar", "zip", "wsjar" or "code-source".
	 * <p>
	 * "zip" and "wsjar" are used by BEA WebLogic Server and IBM WebSphere,
	 * respectively, but can be treated like JAR files. The same applies to
	 * &quot;code-source&quot; URLs on Oracle OC4J, provided that the path
	 * contains a JAR separator.
	 * 
	 * @param url
	 *            The {@link URL} to check.
	 * @return True if the {@link URL} has been identified as JAR, false
	 *         otherwise.
	 */
	private static boolean isJarURL(URL url) {
		String protocol = url.getProtocol();
		return (URL_PROTOCOL_JAR.equals(protocol) || URL_PROTOCOL_ZIP.equals(protocol)
				|| URL_PROTOCOL_WSJAR.equals(protocol) || (URL_PROTOCOL_CODE_SOURCE.equals(protocol) && url.getPath()
				.contains(JAR_URL_SEPARATOR)));
	}

	/**
	 * Checks if the JAR entry is a valid class. Means if the class can be
	 * instantiated.
	 * 
	 * @param entry
	 *            The JAR entry to check.
	 * @return True if the entry is valid, false otherwise.
	 */
	private static boolean isJarEntryValid(JarEntry entry) {
		String name = entry.getName();
		return name.endsWith(CLASS_ENDING) && !entry.isDirectory() && !name.contains(ABSTRACT_KEYWORD)
				&& !name.contains(MOCK_KEYWORD) && !name.contains(DOLLAR_SIGN);
	}

	/**
	 * Gets a {@link JarFile} from the given {@link URL}.
	 * 
	 * @param url
	 *            The URL from that the file shall be retrieved.
	 * @return The {@link JarFile} of the given {@link URL}.
	 */
	private static JarFile getJarFileFromURL(URL url) {
		JarURLConnection jarUrlConnection;
		URLConnection urlConnection;
		JarFile jarFile;

		try {
			urlConnection = url.openConnection();
		} catch (IOException e) {
			InitializationException ie = new InitializationException("Unable to open URL connection to JAR file.", e);
			LOGGER.error(ie.getMessage());
			throw ie;
		}
		if (urlConnection instanceof JarURLConnection) {
			jarUrlConnection = (JarURLConnection) urlConnection;
			try {
				jarFile = jarUrlConnection.getJarFile();
			} catch (IOException e) {
				InitializationException ie = new InitializationException(
						"Unable to retrieve JAR file from the jar url connection.", e);
				LOGGER.error(ie.getMessage());
				throw ie;
			}
		} else {
			InitializationException e = new InitializationException("Unable to read the JAR file.");
			LOGGER.error(e.getMessage());
			throw e;
		}

		return jarFile;
	}

	/**
	 * Loads all valid class names from the given URL.
	 * 
	 * @return A {@link Set} containing all class names.
	 */
	private Set<String> loadClassNamesFromJar(URL url) {
		JarFile jarFile = getJarFileFromURL(url);
		Set<String> classNames = new HashSet<String>();
		for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
			JarEntry entry = entries.nextElement();
			if (isJarEntryValid(entry) && entry.getName().startsWith(getSearchContextPath())) {
				String name = entry.getName();
				// Cut off the .class ending.
				name = name.substring(0, name.indexOf(".class"));
				// replace all filepath parameters with a "." (transform
				// name into package.class name)
				name = name.replaceAll("\\/", ".");
				classNames.add(name);
			}
		}
		return classNames;
	}

	/**
	 * Collect all files that are in the given search context. <br />
	 * <b> Excluded files are:</b><br />
	 * non-java classes, abstract classes, mock classes and ananymous inner
	 * classes.
	 * 
	 * @param searchContext
	 *            The search context where the files shall be searched.
	 * @return A list containing all valid files contained within the search
	 *         context.
	 */
	private Set<String> collectClassNamesFromFile(final URL url) {
		Set<String> classNames = new HashSet<String>();

		File directory = new File(url.getFile());
		if (directory.isDirectory()) {
			File[] allFiles = directory.listFiles();
			for (int i = 0; i < allFiles.length; i++) {
				if (allFiles[i].isDirectory()) {
					try {
						classNames.addAll(collectClassNamesFromFile(new URL(URL_PROTOCOL_FILE, url.getHost(), url
								.getPath()
								+ "/" + allFiles[i].getName())));
					} catch (MalformedURLException e) {
						InitializationException ie = new InitializationException(
								"Unable to load classes from file system.", e);
						LOGGER.error(ie.getMessage());
						throw ie;
					}
				} else {
					// Only add files that are possible candidates. Exclude
					// non-java classes, abstract classes, mock classes and
					// anonymous inner classes.
					if (allFiles[i].getName().endsWith(CLASS_ENDING)
							&& !allFiles[i].getName().startsWith(ABSTRACT_KEYWORD)
							&& !allFiles[i].getName().contains(MOCK_KEYWORD)
							&& !allFiles[i].getName().contains(DOLLAR_SIGN)) {

						String path = url.getPath();
						path = path.substring(path.indexOf(getSearchContextPath()));

						path = path.replaceAll("/", ".");

						String name = path + "." + allFiles[i].getName();
						classNames.add(name.substring(0, name.indexOf(CLASS_ENDING)));
					}
				}
			}
		} else {
			InitializationException ie = new InitializationException("The URL pointing to " + url.getFile()
					+ " must be a directory.");
			LOGGER.error(ie.getMessage());
			throw ie;
		}

		return classNames;
	}

	/**
	 * Instantiates all classes given in the {@link Set} of class names.
	 * 
	 * @param T
	 *            The type of classes to be instantiated.
	 * @param classType
	 *            The Class of the classes to be instantiated.
	 * @param classNames
	 *            The {@link Set} containing the names of the classes to be
	 *            instantiated.
	 * @return
	 */
	private Set<T> createInstancesFromClassNames(Set<String> classNames, Class<T> classType) {
		Set<T> classes = new HashSet<T>();
		for (String name : classNames) {
			Class<?> clazz;
			try {
				// load the class from the classpath
				clazz = Class.forName(name);
			} catch (ClassNotFoundException e) {
				// Must not occur. This would mean an illegal state.
				InitializationException ie = new InitializationException("Illegal state. Cannot load class " + name
						+ " from JAR.");
				LOGGER.error(ie.getMessage(), e);
				throw ie;
			}
			// Create an instance of the class
			Object instance;
			try {
				instance = clazz.newInstance();
			} catch (InstantiationException e) {
				InitializationException ie = new InitializationException("Cannot read class file from JAR.", e);
				LOGGER.error(ie.getMessage());
				throw ie;
			} catch (IllegalAccessException e) {
				InitializationException ie = new InitializationException("Cannot read class file from JAR.", e);
				LOGGER.error(ie.getMessage());
				throw ie;
			}

			// Checks if the created instance is of the right type. If
			// not it is ignored.
			if (classType.isInstance(instance)) {
				classes.add(classType.cast(instance));
			}
		}
		return classes;
	}
}
