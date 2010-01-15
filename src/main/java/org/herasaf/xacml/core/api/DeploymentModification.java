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
package org.herasaf.xacml.core.api;

/**
 * This interface is a marker interface for implementations of deployment modifications. A deployment modification is a
 * modification of the currently deployed policy tree in a policy repository. Such a modification could be add, delete,
 * move, update, ...
 * 
 * This interface does not specify any methods yet because these are specific for each modification implementation.
 * Further the policy repository implementation is only able to handle a certain subset of modifications which must be
 * known.
 * 
 * @author Florian Huonder
 */
public interface DeploymentModification {

}
