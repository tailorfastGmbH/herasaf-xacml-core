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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herasaf.xacml.EvaluatableNotFoundException;
import org.herasaf.xacml.EvaluatableVersionMissMatchException;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.IdReferenceType;
import org.herasaf.xacml.core.referenceloader.ReferenceLoader;
import org.herasaf.xacml.core.utils.IdReferenceLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This util {@link LazyRemoteMap} contains the ReferencLoader and IdReferenceType to resolve the
 * references on demand
 * 
 * Include a cache for already resolved Evaluatables
 * 
 * @author Patrik Dietschweiler
 * @author Christoph Egger
 * @version 1.1
 * 
 */
public class LazyRemoteMap extends HashMap<String, Evaluatable> {

	/** Serial version UID. */
	private static final long serialVersionUID = 822879862176104685L;

	transient static ReferenceLoader referenceLoader = null;
	
	/** Class logger. */
	private final Logger logger = LoggerFactory.getLogger(LazyRemoteMap.class);
	
	private Map<String, Evaluatable> cache = null;
	/**
	 * Sets the ReferenceLoader which can resolve the reference on demand.
	 * 
	 * 
	 */
	public static void setReferenceLoader(ReferenceLoader referenceLoader) {
		LazyRemoteMap.referenceLoader = referenceLoader;
	}

	public LazyRemoteMap(List<IdReferenceType> idReferenceTypes) {
		cache = new HashMap<String, Evaluatable>();
		for (IdReferenceType idReferenceType : idReferenceTypes) {
			put(idReferenceType.getValue(), idReferenceType);
		}
	}

	/**
	 * Overide the get to resolve the reference first
	 * 
	 * 
	 */
	@Override
	public Evaluatable get(Object key) {
		return load((IdReferenceType) super.get(key));
		
	}

	/**
	 * Overide the values to resolve the references first
	 * 
	 * 
	 */
	@Override
	public Collection<Evaluatable> values() {
		Collection<Evaluatable> result = new ArrayList<Evaluatable>();
		for (Evaluatable eval : super.values()) {
			result.add(load((IdReferenceType)eval));
		}
		return result;
	}

	/**
	 * Check if the Evaluatable is in the cache if it has the right version
	 * 
	 * Resolving Order:
	 * 
	 * Cache (validate version)
	 * Local Evaluatable (validate version)
	 * Remote Evaluatable 
	 *
	 * if the evaluatable found local or remote i will write in the cache 
	 * 
	 * @throws EvaluatableNotFoundException, EvaluatableVersionMissMatchException
	 */
	public Evaluatable load(IdReferenceType idReferenceType)
			throws EvaluatableNotFoundException {

		boolean missMatchCacheVersion = false;
		String id = idReferenceType.getValue();
		Evaluatable result = cache.get(id);
		if (result != null) {
			if (!validateVersion(idReferenceType, result)) {
				missMatchCacheVersion = true;
				result = null;
				if(logger.isInfoEnabled())
				{
					logger.info(Thread.currentThread().getId() + ": version missmatch with cache");
				}
			} else {
				return result;
			}
		}
		if (result == null) {
			result = referenceLoader.load(idReferenceType);
		}
		if (result == null) {
			if (missMatchCacheVersion) {
				if(logger.isDebugEnabled())
				{
					logger.debug(Thread.currentThread().getId() + ": evaluatable version missmatch");
				}
				throw new EvaluatableVersionMissMatchException();
			}
			if(logger.isDebugEnabled())
			{
				logger.debug(Thread.currentThread().getId() + ": unable to load evaluatable");
			}
			
			throw new EvaluatableNotFoundException();
		} else {
			cache.put(id, result);
			List<IdReferenceType> references = IdReferenceLookup.getIdReference(result);
			for (IdReferenceType idRef : references) {
				put(idRef.getValue(), idRef);
			}
			if (!validateVersion(idReferenceType, result)) {
				if(logger.isDebugEnabled())
				{
					logger.debug(Thread.currentThread().getId() + ": evaluatable version missmatch");
				}
				throw new EvaluatableVersionMissMatchException();
			}
		}
		return result;
	}
	/**
	 * validate the Version of the requested Evaluatable and the idReferenceType
	 * 
	 * Return true if the optinal param in idReferenceType version, earliestversion, 
	 * latestversion are null
	 * 
	 * Return true if the versions are equal
	 * 
	 * Return true if the IdReferenceType version is null and the earliestVersion 
	 * is lower then the Evaluatable version
	 * 
	 * Return true if the IdReferenceType version is null and the latestVersion 
	 * is higher then  the Evaluatable version
	 * 
	 * @param IdReferenceType 
	 * @param Evalauatable
	 * 
	 * @return boolean
	 */
	private boolean validateVersion(IdReferenceType idReferenceType,
			Evaluatable evaluatable) {
		if (idReferenceType.getVersion() == null
				&& idReferenceType.getEarliestVersion() == null
				&& idReferenceType.getLatestVersion() == null) {
			return true;
		}
		if (idReferenceType.getVersion() != null
				&& evaluatable.getEvalutableVersion().equals(
						idReferenceType.getVersion())) {
			return true;
		}
		if (idReferenceType.getEarliestVersion() != null
				&& idReferenceType.getEarliestVersion().compareTo(
						evaluatable.getEvalutableVersion()) <= 0) {
			return true;
		} else if (idReferenceType.getLatestVersion() != null
				&& idReferenceType.getLatestVersion().compareTo(
						evaluatable.getEvalutableVersion()) >= 0) {
			return true;
		}

		return false;
	}
}
