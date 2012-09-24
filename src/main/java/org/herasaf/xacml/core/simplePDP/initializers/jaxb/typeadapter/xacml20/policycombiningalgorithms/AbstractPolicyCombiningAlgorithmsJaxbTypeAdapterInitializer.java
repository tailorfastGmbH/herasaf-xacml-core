package org.herasaf.xacml.core.simplePDP.initializers.jaxb.typeadapter.xacml20.policycombiningalgorithms;

import java.util.HashMap;
import java.util.Map;

import org.herasaf.xacml.core.combiningAlgorithm.policy.AbstractPolicyCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.policy.PolicyCombiningAlgorithm;
import org.herasaf.xacml.core.converter.PolicyCombiningAlgorithmJAXBTypeAdapter;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.typeadapter.AbstractJaxbTypeAdapterInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This abstract class provides the basis for all {@link AbstractPolicyCombiningAlgorithm} initializers.
 * To add AbstractPolicyCombiningAlgorithms to the system this class must be extended and the subclass must be added to 
 * the initializers of the PDP. 
 * 
 * @author Alexander Broekhuis
 *
 */
public abstract class AbstractPolicyCombiningAlgorithmsJaxbTypeAdapterInitializer extends AbstractJaxbTypeAdapterInitializer<AbstractPolicyCombiningAlgorithm> {
	
	private transient static Logger logger = LoggerFactory.getLogger(AbstractPolicyCombiningAlgorithmsJaxbTypeAdapterInitializer.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void setInstancesIntoTypeAdapter(Map<String, AbstractPolicyCombiningAlgorithm> instancesMap) {
		Map<String, PolicyCombiningAlgorithm> instances = new HashMap<String, PolicyCombiningAlgorithm>();
		instances.putAll(instancesMap);
		PolicyCombiningAlgorithmJAXBTypeAdapter.addCombiningAlgorithms(instances);
		logger.info("{} policy combining algorithms are added.", instances.size());
	}
}
