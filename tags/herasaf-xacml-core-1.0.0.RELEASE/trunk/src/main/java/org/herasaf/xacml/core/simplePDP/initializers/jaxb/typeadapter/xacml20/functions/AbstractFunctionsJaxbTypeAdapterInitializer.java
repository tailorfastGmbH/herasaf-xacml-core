package org.herasaf.xacml.core.simplePDP.initializers.jaxb.typeadapter.xacml20.functions;

import java.util.Map;

import org.herasaf.xacml.core.converter.FunctionsJAXBTypeAdapter;
import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.typeadapter.AbstractJaxbTypeAdapterInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This abstract class provides the basis for all {@link Function} initializers.
 * To add Functions to the system this class must be extended and the subclass must be added to the initializers of the 
 * PDP. 
 * 
 * @author Alexander Broekhuis
 *
 */
public abstract class AbstractFunctionsJaxbTypeAdapterInitializer extends AbstractJaxbTypeAdapterInitializer<Function> {
	
	private transient static Logger logger = LoggerFactory.getLogger(AbstractFunctionsJaxbTypeAdapterInitializer.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void setInstancesIntoTypeAdapter(Map<String, Function> instancesMap) {
		FunctionsJAXBTypeAdapter.addFunctions(instancesMap);
		logger.info("{} functions are addded.", instancesMap.size());
	}
}
