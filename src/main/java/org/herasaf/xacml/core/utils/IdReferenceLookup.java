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
package org.herasaf.xacml.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import javax.xml.bind.JAXBElement;

import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.IdReferenceType;
import org.herasaf.xacml.core.policy.impl.PolicySetType;

/**
 * Looks for IdReferencesType in a PolicySetType
 * 
 * @author Patrik Dietschweiler
 * 
 */
public class IdReferenceLookup {

	/**
	 * Looks for IdReferencesType in a Evaluatables
	 * 
	 * @param eval {@link Evaluatable}s to look for {@link IdReferenceType}.
	 * 
	 * @return Return the result of search. If no IdReferenceType was found
	 *  the resturn value is an empty List
	 */
	public static List<IdReferenceType> getIdReference(Evaluatable eval){
		List<IdReferenceType> references = new ArrayList<IdReferenceType>();
		Stack<PolicySetType> setTypes = new Stack<PolicySetType>();
		if(eval instanceof PolicySetType){
			setTypes.push((PolicySetType)eval);
			while(!setTypes.isEmpty()){
				PolicySetType setType = setTypes.pop();
				for (JAXBElement<?> currentContent : setType.getAdditionalInformation()) {
					if(currentContent.getValue() instanceof IdReferenceType){
						references.add((IdReferenceType)currentContent.getValue());
					}else if (currentContent.getValue() instanceof PolicySetType){
						setTypes.push((PolicySetType)currentContent.getValue());
					}
				}
			}
		}
		return references;
	}
	
	/**
	 * Looks for IdReferencesType in a List of Evaluatables
	 * 
	 * @param evals {@link Collection}  of {@link Evaluatable}s to look for {@link IdReferenceType}.
	 * 
	 * @return Return the result of search. If no IdReferenceType was found
	 *  the resturn value is an empty List
	 */
	public static List<IdReferenceType> getIdReference(Collection<Evaluatable> evals){
		List<IdReferenceType> references = new ArrayList<IdReferenceType>();
		for (Evaluatable evaluatable : evals) {
			references.addAll(getIdReference(evaluatable));
		}	
		return references;
	}
	
}
