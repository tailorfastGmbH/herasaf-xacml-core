package org.herasaf.xacml.core.simplePDP.initializers.jaxb.xacml20.rulecombiningalgorithms;

import java.util.HashMap;
import java.util.Map;

import org.herasaf.xacml.core.combiningAlgorithm.rule.AbstractRuleCombiningAlgorithm;
import org.herasaf.xacml.core.combiningAlgorithm.rule.RuleCombiningAlgorithm;
import org.herasaf.xacml.core.converter.RuleCombiningAlgorithmJAXBTypeAdapter;
import org.herasaf.xacml.core.simplePDP.initializers.jaxb.AbstractJaxbTypeAdapterInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This abstract class provides the basis for all {@link AbstractRuleCombiningAlgorithm} initializers.
 * To add AbstractPolicyCombiningAlgorithms to the system this class must be extended and the subclass must be added to 
 * the initializers of the PDP. 
 * 
 * @author Alexander Broekhuis
 *
 */
public abstract class AbstractRuleCombiningAlgorithmsJaxbTypeAdapterInitializer extends AbstractJaxbTypeAdapterInitializer<AbstractRuleCombiningAlgorithm> {
	
	private transient static Logger logger = LoggerFactory.getLogger(AbstractRuleCombiningAlgorithmsJaxbTypeAdapterInitializer.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void setInstancesIntoTypeAdapter(Map<String, AbstractRuleCombiningAlgorithm> instancesMap) {
		Map<String, RuleCombiningAlgorithm> instances = new HashMap<String, RuleCombiningAlgorithm>();
		instances.putAll(instancesMap);
		RuleCombiningAlgorithmJAXBTypeAdapter.addCombiningAlgorithms(instances);
		logger.info("{} rule combining algorithms are added.", instances.size());
	}
}
