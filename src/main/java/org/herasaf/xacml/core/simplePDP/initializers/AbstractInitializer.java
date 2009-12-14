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
import org.herasaf.xacml.core.utils.InternalJarClassInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO REVIEW René.
 * 
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
    private final Logger logger = LoggerFactory.getLogger(AbstractInitializer.class);
    private static final String CLASS_ENDING = ".class";
    public static final String JARPATH_PROPERTY_NAME = "org:herasaf:xacml:core:jarPath";
    private String jarPath;

    /**
     * TODO REVIEW René.
     * 
     * Initializes the initializer and sets the path of the jarPath to that set
     * in the given system property. The path remains <code>null</code> if no
     * path is provided.
     */
    public AbstractInitializer() {
        jarPath = System.getenv(JARPATH_PROPERTY_NAME);
        logger.info("JarPath system property is set to " + jarPath);
    }

    /**
     * TODO REVIEW René.
     * 
     * This method returns the search context within the classpath (the search
     * context's delimiter is a &quot;.&quot;).
     * 
     * @return The String containing the search context.
     */
    protected abstract String getSearchContext();

    /**
     * TODO REVIEW René.
     * 
     * This method returns the search context within the classpath (the search
     * context's delimiter is a &quot;/&quot;).
     * 
     * @return The String containing the search context.
     */
    protected abstract String getSearchContextPath();

    /**
     * TODO REVIEW René.
     * 
     * Returns the {@link Class} of the type T. This is needed for a proper
     * instantiation.
     * 
     * @return Returns the {@link Class} of the type T.
     */
    protected abstract Class<T> getTargetClass();

    /**
     * TODO REVIEW René.
     * 
     * Here a initializer-specific property can be set. The abstract initializer
     * only knows the jarPath property.<br />
     * 
     * <b>Note:</b><br />
     * If this method is overridden in a subclass and the property
     * org:herasaf:xacml:core:jarPath is not set. this method must be called
     * with super.setProperty.
     */
    public void setProperty(String id, Object value) {
        if (JARPATH_PROPERTY_NAME.equals(id)) {
            jarPath = (String) value;
            return;
        }
        IllegalArgumentException e = new IllegalArgumentException("Property " + id + "not recognized.");
        logger.error(e.getMessage());
        throw e;
    }

    /**
     * {@inheritDoc}
     */
    public void run() {
        List<T> instances;

        if (isCodeLocationJAR()) {
            /*
             * JAR-based (code runs inside a JAR).
             */
            InternalJarClassInitializer jarClassLoader = new InternalJarClassInitializer(jarPath);
            instances = jarClassLoader.loadAllClasses(getSearchContextPath(), getTargetClass());
        } else {
            /*
             * File-based (code runs not in a JAR).
             */
            List<File> files = collectFiles(getSearchContextPath());
            instances = instantiateClasses(files);
        }
        /*
         * Further steps for initialization.
         */
        Map<String, T> instancesMap = convertListToMap(instances);
        setInstancesIntoConverter(instancesMap);
        furtherInitializations(instances);
    }

    /**
     * 
     * TODO REVIEW René.
     * 
     * Checks if the code source is a directory or a file. in case it is a file
     * it must be a jar.
     * 
     * @return True if the location is a JAR file, false otherwise.
     */
    private boolean isCodeLocationJAR() {

        try {
            File f = null;
            if (jarPath == null) {
                f = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
            } else {
                f = new File(jarPath);
            }
            if (f.isDirectory()) {
                return false;
            }
            return true;
        } catch (NullPointerException e) {
            InitializationException ie = new InitializationException("Unable to determine code source.", e);
            logger.error(ie.getMessage());
            throw ie;
        }

    }

    /**
     * TODO REVIEW René.
     * 
     * This method allows to do further initialization steps on the instantiated
     * classes of type T. By default this method does nothing. It may be
     * overridden by an implementing subclass.
     * 
     * @param instances
     *            The instances of type T.
     */
    protected void furtherInitializations(List<T> instances) {
        // to be overridden in concrete subclass if needed.
    }

    /**
     * TODO REVIEW René.
     * 
     * Sets the instances into the converter map. The converter map is the map
     * needed by the JAXB type adapter.
     * 
     * @param instancesMap
     *            The map containing the instances. Key is the id of the object
     *            of type T, the value is the object itself.
     */
    protected abstract void setInstancesIntoConverter(Map<String, T> instancesMap);

    /**
     * TODO REVIEW René.
     * 
     * Converts the list of instances into a map where the key is the id of the
     * instance.
     * 
     * @param instances
     *            The {@link List} of instances that shall be converted into a
     *            {@link Map}.
     * @return The {@link Map} containing the instances.
     */
    private Map<String, T> convertListToMap(List<T> instances) {
        Map<String, T> targetMap = new HashMap<String, T>();
        for (T instance : instances) {
            targetMap.put(getURIFromType(instance), instance);
        }
        return targetMap;
    }

    /**
     * TODO REVIEW René.
     * 
     * Returns the URI, id, of the instance of type T. This method is needed
     * because it cannot be determined from T how the id can be obtained.
     * 
     * @param instance
     *            The type from which the URI shall be returned.
     * @return The URI, id, of the given instance.
     */
    protected abstract String getURIFromType(T instance);

    /**
     * TODO REVIEW René.
     * 
     * Is needed in case the files to instantiate from the classpath are not in
     * a JAR. Then a list of class files is provided and these are instantiated.
     * 
     * @param files
     *            The {@link List} of files from which instances shall be
     *            created.
     * @return The {@link List} containing the instances.
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
            // cuts of the .class ending from the file.
            path = path.substring(0, path.indexOf(CLASS_ENDING));
            // replace the file delimiter with a dot.
            if (File.separator.equals("\\")) {
                path = path.replaceAll("\\\\", ".");
            } else {
                String separator;
                if (File.separatorChar == '\\') { // Avoids problems with the
                    // replaceAll-regex function
                    // in case of windows file
                    // separator.
                    separator = "\\\\";
                } else {
                    separator = File.separator;
                }
                path = path.replaceAll(separator, ".");
            }
            // Retrieves the class name.
            String className = path.substring(path.indexOf(getSearchContext()));

            try {
                // Gets the class object of the current class
                Class clazz = Class.forName(className);
                if (Modifier.isAbstract(clazz.getModifiers())) {
                    continue;
                }
                // Instantiates the class.
                Object instance = clazz.newInstance();
                try {
                    // Try to cast the instance.
                    getTargetClass().cast(instance);
                    // If successful, add the instance to the list of instances
                    // to return.
                    listOfInstances.add((T) instance);
                    logger.debug("The type {} is successfully initialized.", instance.getClass().getCanonicalName());
                } catch (ClassCastException e) {
                    // nop -- expected
                    logger.trace("Ignoring type: {}", instance.getClass());
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

    /**
     * TODO REVIEW René.
     * 
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
    private List<File> collectFiles(final String searchContext) {
        List<URL> urls = new ArrayList<URL>();
        Enumeration<URL> resourceURLs;
        List<File> files = new ArrayList<File>();

        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            // Get all resources that are at the location given by the
            // searchContext.
            resourceURLs = cl.getResources(searchContext);
        } catch (IOException e) {
            logger.error("Unable to load classes.", e);
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
                logger.error("Unable to load classes.", e);
                throw new InitializationException(e);
            }
            // recursivley call this method for all subfolders.
            File[] allFiles = folder.listFiles();
            for (int i = 0; i < allFiles.length; i++) {
                if (allFiles[i].isDirectory()) {
                    files.addAll(collectFiles(searchContext + "/" + allFiles[i].getName()));
                } else {
                    // Only add files that are possible candidates. Exclude
                    // non-java classes, abstract classes, mock classes and
                    // ananymous inner classes.
                    if (allFiles[i].getName().endsWith(".class") && !allFiles[i].getName().startsWith("Abstract")
                            && !allFiles[i].getName().contains("Mock") && !allFiles[i].getName().contains("$")) {
                        files.add(allFiles[i]);
                    }
                }
            }
        }
        return files;
    }
}
