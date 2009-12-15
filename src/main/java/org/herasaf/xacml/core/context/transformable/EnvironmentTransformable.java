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

package org.herasaf.xacml.core.context.transformable;

import org.herasaf.xacml.core.context.impl.EnvironmentType;

/**
 * TODO REVIEW René.
 * 
 * Transforms environment attributes from any form into an {@link EnvironmentType}.
 * This interface shall be implemented by a PEP-component to create XACML
 * conform request without creating an XML.
 * 
 * @author Stefan Oberholzer
 */
public interface EnvironmentTransformable {

	/**
	 * TODO REVIEW René.
	 * 
	 * Transforms the environment attribute in the transformable into a
	 * {@link EnvironmentType}.
	 * 
	 * @return The {@link EnvironmentType}.
	 */
	EnvironmentType transform();
}