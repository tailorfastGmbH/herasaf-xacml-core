package org.herasaf.xacml.core.policy.requestinformationfactory;

import java.util.HashMap;
import java.util.List;

import org.herasaf.xacml.core.attributeFinder.AttributeFinder;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.IdReferenceType;

public class RequestInformationFactoryMock {

	public RequestInformation createRequestInformation(
			List<IdReferenceType> idReferences, AttributeFinder attributeFinder) {
		try {
			return new RequestInformation(new HashMap<String, Evaluatable>(),
					attributeFinder);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

}
