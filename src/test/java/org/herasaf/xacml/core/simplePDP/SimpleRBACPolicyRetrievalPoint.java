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
package org.herasaf.xacml.core.simplePDP;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.api.PolicyRetrievalPoint;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.EvaluatableID;
import org.herasaf.xacml.core.policy.PolicyMarshaller;
import org.herasaf.xacml.core.simplePDP.initializers.InitializerExecutor;

/**
 * A simple PolicyRetrievalPoint implementation that can be used to retrieve policies for the
 * RBAC 2.0 profile of XACML 2.0. Policies are retrieved from a specified URL (directory path).
 * Primary/Role policies contain "Role" in the filename, and are loaded on startup. 
 * Secondary/permissions policies contain "Permissions" in the filename, and are loaded as needed.
 */
public class SimpleRBACPolicyRetrievalPoint implements PolicyRetrievalPoint {
                    
    private URL policiesURL;
    private Map<String, Evaluatable> policyCache = new HashMap<String, Evaluatable>();
    
    public SimpleRBACPolicyRetrievalPoint() {
            InitializerExecutor.runInitializers();
    }
    
    @Deprecated
    public List<Evaluatable> getEvaluatables(
        org.herasaf.xacml.core.context.RequestCtx request
    ) {
            return getEvaluatables((RequestType)null);
    }
    
    public List<Evaluatable> getEvaluatables(RequestType request) {
            return new ArrayList<Evaluatable>(policyCache.values());
    }   
    
    public Evaluatable getEvaluatable(EvaluatableID id) {
            // See if it's a primary policy
            if (policyCache.containsKey(id.toString())) {
                    return policyCache.get(id.toString());
            }
            return findSecondaryPolicy(id.toString(), policiesURL);
    }

    public URL getPoliciesURL() {
            return policiesURL;
    }

    public void setPoliciesURL(URL policiesURL) throws SyntaxException {
            if (policiesURL != null && !policiesURL.equals(this.policiesURL)) {
                    deployPrimaryPolicies(policiesURL);
            }
            this.policiesURL = policiesURL;
    }

    private void deployPrimaryPolicies(URL policyURL) throws SyntaxException {
            File primaryURLPath = new File(policyURL.getPath());
            File[] policies = null;
            if (primaryURLPath.isDirectory()) {
                    policies = primaryURLPath.listFiles();
            } else {
                    policies = new File[]{primaryURLPath};
            }

            for (File f : policies) {
                    if (f.getName().contains("Role")) {
                            try {
                                    Evaluatable eval = PolicyMarshaller.unmarshal(f);
                                    policyCache.put(eval.getId().toString(), eval);
                            } catch (SyntaxException e) {
                                    // continue
                            }
                    }
            }
    }
    
    private Evaluatable findSecondaryPolicy(String policyId, URL policyURL) {
            File urlPath = new File(policyURL.getPath());
            File[] policies = null;
            if (urlPath.isDirectory()) {
                    policies = urlPath.listFiles();
            } else {
                    policies = new File[]{urlPath};
            }

            for (File f : policies) {
                    if (f.getName().contains("Permissions")) {
                            try {
                                    Evaluatable eval = PolicyMarshaller.unmarshal(f);
                                    if (policyId.equals(eval.getId().toString())) {
                                            return eval;
                                    }
                            } catch (SyntaxException e) {
                                    // continue
                            }
                    }
            }
            return null;
    }
}
