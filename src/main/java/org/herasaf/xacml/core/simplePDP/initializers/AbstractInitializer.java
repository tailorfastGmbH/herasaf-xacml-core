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
package org.herasaf.xacml.core.simplePDP.initializers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.simplePDP.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO JAVADOC!!
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 * 
 */
public abstract class AbstractInitializer<T> implements Initializer {

	private final Logger logger = LoggerFactory
			.getLogger(AbstractInitializer.class);

	protected abstract String getSearchContext();

	protected abstract String getSearchContextPath();

	protected abstract Class<T> getTargetClass();

	private final String CLASS_ENDING = ".class";

	/**
	 * {@inheritDoc}
	 */
	public void run() {
		List<File> files = collectFiles(getSearchContextPath());
		List<T> instances = instantiateClasses(files);
		Map<String, T> instancesMap = convertListToMap(instances);
		setInstancesIntoConverter(instancesMap);
		furtherInitializations(instances);
	}

	/**
	 * @param instances
	 */
	protected void furtherInitializations(List<T> instances) {
		// to be overridden in concrete subclass if needed.
	}

	/**
	 * @param instancesMap
	 */
	protected abstract void setInstancesIntoConverter(
			Map<String, T> instancesMap);

	/**
	 * @param dataTypeAttributes
	 * @return
	 */
	private Map<String, T> convertListToMap(List<T> instances) {
		Map<String, T> targetMap = new HashMap<String, T>();
		for (T instance : instances) {
			targetMap.put(getURIFromType(instance), instance);
		}
		return targetMap;
	}

	/**
	 * @return
	 */
	protected abstract String getURIFromType(T instance);

	/**
	 * @param files
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<T> instantiateClasses(List<File> files) {
		List<T> listOfInstances = new ArrayList<T>();
		String path = null;
		for (File file : files) {
			try {
				path = file.getCanonicalPath();
			} catch (IOException e) {
				logger.error("Unable to load classes.", e);
				throw new InitializationException(e);
			}
			path = path.substring(0, path.indexOf(CLASS_ENDING));
			if (File.separator.equals("\\")) {
				path = path.replaceAll("\\\\", ".");
			} else {
				path = path.replaceAll(File.separator, "."); // TODO Verify Unix
				// based OS
				// behavior
			}
			String className = path.substring(path.indexOf(getSearchContext()));

			try {
				Class clazz = Class.forName(className);
				if (Modifier.isAbstract(clazz.getModifiers())) {
					continue;
				}
				Object instance = clazz.newInstance();
				try {
					getTargetClass().cast(instance);
					listOfInstances.add((T) instance);
				} catch (ClassCastException e) {
					// nop -- expected
				}
			} catch (ClassNotFoundException e) {
				logger.error("Unable to load classes.", e);
				throw new InitializationException(e);
			} catch (InstantiationException e) {
				logger.error("Unable to load classes.", e);
				throw new InitializationException(e);
			} catch (IllegalAccessException e) {
				logger.error("Unable to load classes.", e);
				throw new InitializationException(e);
			}
		}
		return listOfInstances;
	}

	private List<File> collectFiles(final String searchContext) {
		List<URL> urls = new ArrayList<URL>();
		Enumeration<URL> resourceURLs;
		List<File> files = new ArrayList<File>();

		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			resourceURLs = cl.getResources(searchContext); // TODO VERIFY
															// behaviour on
															// other OS!!!!
		} catch (IOException e) {
			logger.error("Unable to load DataTypeAttributes.", e);
			throw new InitializationException(e);
		}
		while (resourceURLs.hasMoreElements()) {
			urls.add(resourceURLs.nextElement());
		}
		File folder = null;
		for (URL url : urls) {
			try {
				folder = new File(url.toURI());
			} catch (URISyntaxException e) {
				logger.error("Unable to load DataTypeAttributes.", e);
				throw new InitializationException(e);
			}
			File[] allFiles = folder.listFiles();
			for (int i = 0; i < allFiles.length; i++) {
				if (allFiles[i].isDirectory()) {
					files.addAll(collectFiles(searchContext + "/"
							+ allFiles[i].getName()));
				} else {
					if (allFiles[i].getName().endsWith(".class")
							&& !allFiles[i].getName().startsWith("Abstract")
							&& !allFiles[i].getName().contains("Mock")) {
						files.add(allFiles[i]);
					}
				}
			}
		}
		return files;
	}
}
