package org.herasaf.xacml.core.context;

import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.attributeFinder.AttributeFinder;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.IdReferenceType;

public class RequestInformationFactory {

	private Class remoteMapClassName = null;
	public RequestInformation createRequestInformation(
			List<IdReferenceType> idReferences, AttributeFinder attributeFinder) {
		try {
			return new RequestInformation((Map<String, Evaluatable>) remoteMapClassName.getConstructor(List.class).newInstance(idReferences),
					attributeFinder);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	public void setRemoteMapClassName(Class remoteMapClassName) {
		this.remoteMapClassName = remoteMapClassName;
	}

}
