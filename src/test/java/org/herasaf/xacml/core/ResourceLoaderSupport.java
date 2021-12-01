package org.herasaf.xacml.core;

import java.io.InputStream;

import org.herasaf.xacml.core.context.RequestMarshaller;
import org.herasaf.xacml.core.context.ResponseMarshaller;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResponseType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.PolicyMarshaller;

public class ResourceLoaderSupport {

    /**
     * Loads an {@link Evaluatable} from the class path.
     * 
     * @param file The path to the resource.
     * @return The created {@link Evaluatable}.
     * @throws SyntaxException
     */
    public static Evaluatable loadPolicy(String filePattern, Object... arguments) throws SyntaxException {
        String file = String.format(filePattern, arguments);
        InputStream is = ResourceLoaderSupport.class.getResourceAsStream(file);
        return PolicyMarshaller.unmarshal(is);
    }

    /**
     * Loads an {@link RequestType} from the class path.
     * 
     * @param file The path to the resource.
     * @return The created {@link RequestType}.
     * @throws SyntaxException
     */
    public static RequestType loadRequest(String filePattern, String testCase) throws SyntaxException {
        String file = String.format(filePattern, testCase);
        InputStream is = ResourceLoaderSupport.class.getResourceAsStream(file);
        return RequestMarshaller.unmarshal(is);
    }

    /**
     * Loads an {@link ResponseType} from the class path.
     * 
     * @param file The path to the resource.
     * @return The created {@link ResponseType}.
     * @throws SyntaxException
     */
    public static ResponseType loadResponse(String filePattern, String testCase) throws SyntaxException {
        String file = String.format(filePattern, testCase);
        InputStream is = ResourceLoaderSupport.class.getResourceAsStream(file);
        return ResponseMarshaller.unmarshal(is);
    }

}
