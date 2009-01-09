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

package org.herasaf.xacml.core.referenceloader;

import java.util.List;
import java.util.Map;

import org.herasaf.xacml.EvaluatableNotFoundException;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.IdReferenceType;


/**
 * The {@code ReferenceLoader} loads {@link Evaluatable}s from a remote Policy
 * Decision Point.
 *
 * @author Florian Huonder, Patrik Dietschweiler
 * @version 1.1
 */
public interface ReferenceLoader {
	
	/**
	 * Initializes the {@link ReferenceLoader}.
	 * 
	 * @param evaluatables The{@link List} of {@link Evaluatable}s.
	 */
	public void initialize(List<Evaluatable> evaluatables);
	
	/**
	 * Returns a {@link Map} containing the {@link Evaluatable}s from the
	 * remote PDPs. The key of the {@link Map} is the id of the
	 * {@link Evaluatable} and the value is the {@link Evaluatable} itsself.
	 * This method is used by the local PDP to get an {@link Evaluatable} from a remote PDP.
	 *
	 * @param references
	 *            The List of {@link IdReferenceType}s to load the remote
	 *            {@link Evaluatable}s.
	 * @return The {@link Map} of {@link Evaluatable}s.
	 * @throws EvaluatableNotFoundException
	 */
	public Map<String, Evaluatable> load(List<IdReferenceType> references) throws EvaluatableNotFoundException;
	
	/**
	 * Returns the {@link Evaluatable} with the given id.
	 * This method is used by a remote PDP to get an {@link Evaluatable} form this PDP.
	 *
	 * @return The found {@link Evaluatable} or {@code null} in case that no
	 *         {@link Evaluatable} with the given id is found.
	 * @throws EvaluatableNotFoundException
	 */
	public Evaluatable get(String id) throws EvaluatableNotFoundException;

	/**
	 * Return the 
	 * @param  idReferenceType
	 * @return The found {@link Evaluatable} or {@code null} in case that no
	 *         {@link Evaluatable} with the given id is found.
	 *         No Lazy Loading, always try to resolve the idReferenceType
	 * @throws EvaluatableNotFoundException
	 */
	public Evaluatable load(IdReferenceType idReferenceType)throws EvaluatableNotFoundException;
}