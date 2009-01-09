/*
 * Copyright 2008 HERAS-AF (www.herasaf.org)
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
package org.herasaf.xacml.core.referenceloader.remotemap.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.IdReferenceType;
import org.herasaf.xacml.core.referenceloader.ReferenceLoader;
import org.herasaf.xacml.core.utils.IdReferenceLookup;
/**
 * First Remote Map
 * 
 * Resolve the IdReferenceType on constructor initialization with a reference Loader
 * 
 * The ReferenceLoader must be static initialization
 * because of the FirstRemoteMap try to resolve the IdReferenceTypes on construction initialization
 * 
 * The Map contains only resolved Evaluatables
 * 
 * @author Patrik Dietschweiler
 *     
 */
public class FirstRemoteMap extends HashMap<String, Evaluatable>{

	private Log logger = LogFactory.getLog(FirstRemoteMap.class);
	
	private static ReferenceLoader referenceLoader = null;
	
	
	/**
     * Sets the ReferenceLoader which can resolve
     *  the reference on construction initialization.
     * 
     *     
     */
	public static void setReferenceLoader(ReferenceLoader referenceLoader){
		FirstRemoteMap.referenceLoader = referenceLoader;
	}
	
	/**
     * Initialize the FirstRemoteMap and resolve the IdReferencesTypes
     * 
     *     
     */
	public FirstRemoteMap(List<IdReferenceType> idReferences) {
		loadRecivedReferences(idReferences);
	    
	}
	
	private void loadRecivedReferences(List<IdReferenceType> results){
		if(results.isEmpty()){
			if(logger.isErrorEnabled())
			{
				logger.error(Thread.currentThread().getId() + ": nothing to load");
			}
			return;
		}
		Map<String, Evaluatable> resultMap = referenceLoader.load(results);
		putAll(resultMap);
		loadRecivedReferences(IdReferenceLookup.getIdReference(resultMap.values()));
	}
	
}
