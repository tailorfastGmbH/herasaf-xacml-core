/*
 * Copyright 2008 - 2013 HERAS-AF (www.herasaf.org)
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

import java.util.List;

import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.RequestType;

/**
 * When the request does not contain an attribute that is flagged with "must be present", the PDP has to ask the PIP for its resolution.<br />
 * The PIP provides methods for fetching attributes of every category (subject, resource, action, environment). <br />
 * <br />
 * See: <a href= "http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20"> OASIS eXtensible Access Control Markup Language
 * (XACML) 2.0, Errata 29. January 2008</a> page 88, chapter attribute retrieval for further information.
 */
public interface PIP {

    /**
     * Returns the requested <b>subject</b> attribute(s) (specified by <code>attibuteId</code>, <code>dataType</code>, <code>issuer</code>
     * and <code>subjectCategory</code>) if the attribute(s) could be resolved.
     *
     * The return value is a {@link List} containing the resolved attribute(s).
     *
     * @param request
     *            - The actual {@link RequestType} to provide the context of this request. Might be useful to determine the attribute
     *            values.
     * @param attributeId
     *            - The id of the requested attribute(s).
     * @param dataType
     *            - The data type of the requested attribute(s).
     * @param issuer
     *            - The issuer of the requested attribute(s).
     * @param subjectCategory
     *            - The subject category of the requested attribute(s).
     *
     * @return The found attribute(s). If no attribute(s) were found an empty {@link List} is returned.
     */
    List<AttributeValueType> fetchSubjectAttributes(RequestType request, String attributeId, String dataType, String issuer,
            String subjectCategory);

    /**
     * Does the same as {@link #fetchSubjectAttributes(RequestType, String, String, String, String)} but with an inappropriate naming. This
     * method ought not be used and will be removed in a future version.
     *
     * @deprecated Use {@link #fetchSubjectAttributes(RequestType, String, String, String, String)} instead. For interface implementors: The
     *             subject attribute designator does no longer respect this method and you have to ensure that your logic for this method is
     *             also executed when {@link #fetchSubjectAttributes(RequestType, String, String, String, String)} is called. E.g. by moving
     *             the code from {@link #addSubjectAttributesToRequest(RequestType, String, String, String, String)} to
     *             {@link #fetchSubjectAttributes(RequestType, String, String, String, String)} and call
     *             {@link #fetchSubjectAttributes(RequestType, String, String, String, String)} from
     *             {@link #addSubjectAttributesToRequest(RequestType, String, String, String, String)}.
     */
    @Deprecated
    List<AttributeValueType> addSubjectAttributesToRequest(RequestType request, String attributeId, String dataType, String issuer,
            String subjectCategory);

    /**
     * Returns the requested <b>resource</b> attribute(s) (specified by <code>attibuteId</code>, <code>dataType</code> and
     * <code>issuer</code>) if the attribute(s) could be resolved.
     *
     * The return value is a {@link List} containing the resolved attribute(s).
     *
     * @param request
     *            - The actual {@link RequestType} to provide the context of this request. Might be useful to determine the attribute
     *            values.
     * @param attributeId
     *            - The id of the requested attribute(s).
     * @param dataType
     *            - The data type of the requested attribute(s).
     * @param issuer
     *            - The issuer of the requested attribute(s).
     *
     * @return The found attribute(s). If no attribute(s) were found an empty {@link List} is returned.
     */
    List<AttributeValueType> fetchResourceAttributes(RequestType request, String attributeId, String dataType, String issuer);

    /**
     * Does the same as {@link #fetchResourceAttributes(RequestType, String, String, String)} but with an inappropriate naming. This method
     * ought not be used and will be removed in a future version.
     *
     * @deprecated Use {@link #fetchResourceAttributes(RequestType, String, String, String)} instead. For interface implementors: The
     *             resource attribute designator does no longer respect this method and you have to ensure that your logic for this method
     *             is also executed when {@link #fetchResourceAttributes(RequestType, String, String, String)} is called. E.g. by moving the
     *             code from {@link #addResourceAttributesToRequest(RequestType, String, String, String)} to
     *             {@link #fetchResourceAttributes(RequestType, String, String, String)} and call
     *             {@link #fetchResourceAttributes(RequestType, String, String, String)} from
     *             {@link #addResourceAttributesToRequest(RequestType, String, String, String)}.
     */
    @Deprecated
    List<AttributeValueType> addResourceAttributesToRequest(RequestType request, String attributeId, String dataType, String issuer);

    /**
     * Returns the requested <b>action</b> attribute(s) (specified by <code>attibuteId</code>, <code>dataType</code> and <code>issuer</code>
     * ) if the attribute(s) could be resolved.
     *
     * The return value is a {@link List} containing the resolved attribute(s).
     *
     * @param request
     *            - The actual {@link RequestType} to provide the context of this request. Might be useful to determine the attribute
     *            values.
     * @param attributeId
     *            - The id of the requested attribute(s).
     * @param dataType
     *            - The data type of the requested attribute(s).
     * @param issuer
     *            - The issuer of the requested attribute(s).
     *
     * @return The found attribute(s). If no attribute(s) were found an empty {@link List} is returned.
     */
    List<AttributeValueType> fetchActionAttributes(RequestType request, String attributeId, String dataType, String issuer);

    /**
     * Does the same as {@link #fetchActionAttributes(RequestType, String, String, String)} but with an inappropriate naming. This method
     * ought not be used and will be removed in a future version.
     *
     * @deprecated Use {@link #fetchActionAttributes(RequestType, String, String, String)} instead. For interface implementors: The action
     *             attribute designator does no longer respect this method and you have to ensure that your logic for this method is also
     *             executed when {@link #fetchActionAttributes(RequestType, String, String, String)} is called.E.g. by moving the code from
     *             {@link #addActionAttributesToRequest(RequestType, String, String, String)} to
     *             {@link #fetchActionAttributes(RequestType, String, String, String)} and call
     *             {@link #fetchActionAttributes(RequestType, String, String, String)} from
     *             {@link #addActionAttributesToRequest(RequestType, String, String, String)}.
     */
    @Deprecated
    List<AttributeValueType> addActionAttributesToRequest(RequestType request, String attributeId, String dataType, String issuer);

    /**
     * Returns the requested <b>environment</b> attribute(s) (specified by <code>attibuteId</code>, <code>dataType</code> and
     * <code>issuer</code>) if the attribute(s) could be resolved.
     *
     * The return value is a {@link List} containing the resolved attribute(s).
     *
     * @param request
     *            - The actual {@link RequestType} to provide the context of this request. Might be useful to determine the attribute
     *            values.
     * @param attributeId
     *            - The id of the requested attribute(s).
     * @param dataType
     *            - The data type of the requested attribute(s).
     * @param issuer
     *            - The issuer of the requested attribute(s).
     *
     * @return The found attribute(s). If no attribute(s) were found an empty {@link List} is returned.
     */
    List<AttributeValueType> fetchEnvironmentAttributes(RequestType request, String attributeId, String dataType, String issuer);

    /**
     * Does the same as {@link #fetchEnvironmentAttributes(RequestType, String, String, String)} but with an inappropriate naming. This
     * method ought not be used and will be removed in a future version.
     *
     * @deprecated Use {@link #fetchEnvironmentAttributes(RequestType, String, String, String)} instead. For interface implementors: The
     *             environment attribute designator does no longer respect this method and you have to ensure that your logic for this
     *             method is also executed when {@link #fetchEnvironmentAttributes(RequestType, String, String, String)} is called. E.g. by
     *             moving the code from {@link #addEnvironmentAttributesToRequest(RequestType, String, String, String)} to
     *             {@link #fetchEnvironmentAttributes(RequestType, String, String, String)} and call
     *             {@link #fetchEnvironmentAttributes(RequestType, String, String, String)} from
     *             {@link #addEnvironmentAttributesToRequest(RequestType, String, String, String)}.
     */
    @Deprecated
    List<AttributeValueType> addEnvironmentAttributesToRequest(RequestType request, String attributeId, String dataType, String issuer);
}