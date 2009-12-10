/*
 * Copyright 2009 HERAS-AF (www.herasaf.org)
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.herasaf.xacml.core.simplePDP.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO REVIEW René.
 * 
 * This class is responsible of dynamically loading functions, data types and
 * combining algorithms from a JAR file.
 * 
 * @author Florian Huonder
 */
public class InternalJarClassInitializer {
	private final Logger logger = LoggerFactory.getLogger(InternalJarClassInitializer.class);
	private String jarPath;

	/**
	 * TODO REVIEW René.
	 * 
	 * This constructor checks if the jarPath is an existing JAR file. The path
	 * to the JAR file may be null, this means that the JAR of the current
	 * protection domain is taken. This may result in a SecurityException.
	 * 
	 * @param jarPath
	 *            The path to the JAR file.
	 */
	public InternalJarClassInitializer(String jarPath) {
		if (jarPath != null) {
			JarInputStream jip = null;
			try {
				jip = new JarInputStream(new FileInputStream(jarPath));
			} catch (FileNotFoundException e) {
				InitializationException ie = new InitializationException("No JAR file at " + jarPath, e);
				logger.error(ie.getMessage());
				throw ie;
			} catch (IOException e) {
				InitializationException ie = new InitializationException("Cannot handle JAR file at " + jarPath, e);
				logger.error(ie.getMessage());
				throw ie;
			} finally {
				if (jip != null) {
					try {
						jip.close();
					} catch (IOException e) {
						logger.warn("Unable to close stream to JAR file.");
					}
				}
			}
			this.jarPath = jarPath;
		} else {
			/*
			 * If no jarPath is provided the location from the code source is
			 * taken.
			 */
			this.jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
		}
	}

	/**
	 * TODO REVIEW René.
	 * 
	 * Loads all classes of type T from the path specified by the parameter
	 * searchContext in the JAR. The classType is only needed to enable a
	 * typesafe check and cast.
	 * 
	 * @param <T>
	 *            The type of the classes to search and instantiate.
	 * @param searchContext
	 *            The path in the classpath where to search for instances of
	 *            type <T>.
	 * @param classType
	 *            the {@link Class} type of <T>.
	 * 
	 * @return A list containing all instantiated objects of type <T> from path
	 *         searchContext.
	 */
	public <T> List<T> loadAllClasses(String searchContext, Class<T> classType) {
		JarInputStream jip = null;
		try {
			jip = new JarInputStream(new FileInputStream(jarPath));
		} catch (FileNotFoundException e) {
			// The stream must be closed here (in an error case) and not in the
			// finally block. This
			// because the stream is needed after this try-catch block.
			if (jip != null) {
				try {
					jip.close();
				} catch (IOException e2) {
					logger.warn("Unable to close stream to JAR file.");
				}
			}
			InitializationException ie = new InitializationException("No JAR file at " + jarPath, e);
			logger.error(ie.getMessage());
			throw ie;
		} catch (IOException e) {
			// The stream must be closed here (in an error case) and not in the
			// finally block. This
			// because the stream is needed after this try-catch block.
			if (jip != null) {
				try {
					jip.close();
				} catch (IOException e2) {
					logger.warn("Unable to close stream to JAR file.");
				}
			}
			InitializationException ie = new InitializationException("Cannot handle JAR file at " + jarPath, e);
			logger.error(ie.getMessage());
			throw ie;
		}

		List<T> classes = new ArrayList<T>();
		JarEntry entry = null;
		try {
			while (((entry = jip.getNextJarEntry()) != null)) {
				// Only acceppt class files that are no Abstract, Mock or inner classes
				if (entry.getName().endsWith(".class") && !entry.isDirectory()
						&& entry.getName().startsWith(searchContext) && !entry.getName().contains("Abstract")
						&& !entry.getName().contains("Mock") && !entry.getName().contains("$")) {
					String name = entry.getName();
					// Cut off the .class ending.
					name = name.substring(0, name.indexOf(".class"));
					//replace all  filepath parameters with a "." (transform name into package.class name)
					name = name.replaceAll("\\/", ".");

					Class<?> clazz;
					try {
						//load the class from the classpath
						clazz = Class.forName(name);
					} catch (ClassNotFoundException e) {
						// Must not occur. This would mean an illegal state.
						InitializationException ie = new InitializationException("Illegal state. Cannot load class "
								+ name + "from JAR " + jarPath);
						logger.error(ie.getMessage(), e);
						throw ie;
					}
					// Create an instnace of the class
					Object instance = clazz.newInstance();

					// Checks if the created instance is of the right type. If
					// not it is ignored.
					if (classType.isInstance(instance)) {
						classes.add(classType.cast(instance));
					}
				}
			}
		} catch (IOException e) {
			InitializationException ie = new InitializationException("Cannot read class file from JAR.", e);
			logger.error(ie.getMessage());
			throw ie;
		} catch (InstantiationException e) {
			InitializationException ie = new InitializationException("Cannot read class file from JAR.", e);
			logger.error(ie.getMessage());
			throw ie;
		} catch (IllegalAccessException e) {
			InitializationException ie = new InitializationException("Cannot read class file from JAR.", e);
			logger.error(ie.getMessage());
			throw ie;
		} finally {
			if (jip != null) {
				try {
					jip.close();
				} catch (IOException e) {
					logger.warn("Unable to close stream to JAR file.");
				}
			}
		}
		return classes;
	}
}
