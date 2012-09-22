package org.herasaf.xacml.core.simplePDP.initializers.jaxb.xacml20.datatypes;

import java.util.Map;

import org.herasaf.xacml.core.converter.DataTypeJAXBTypeAdapter;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.AbstractJaxbTypeAdapterInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This abstract class provides the basis for all {@link DataTypeAttribute} initializers.
 * To add DataTypes to the system this class must be extended and the subclass must be added to the initializers of the 
 * PDP. 
 * 
 * @author Alexander Broekhuis
 *
 */
public abstract class AbstractDataTypesJaxbTypeAdapterInitializer extends AbstractJaxbTypeAdapterInitializer<DataTypeAttribute<?>> {
	private transient static Logger logger = LoggerFactory.getLogger(AbstractDataTypesJaxbTypeAdapterInitializer.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void setInstancesIntoTypeAdapter(
			Map<String, DataTypeAttribute<?>> instancesMap) {
		DataTypeJAXBTypeAdapter.addDataTypeAttributes(instancesMap);
		logger.info("{} DataTypeAttributes are added.", instancesMap
				.size());
	}
}