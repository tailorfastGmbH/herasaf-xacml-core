/*
 * Copyright 2009 - 2012 HERAS-AF (www.herasaf.org)
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
package org.herasaf.xacml.core.simplePDP.initializers.jaxb.typeadapter.xacml20.datatypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.converter.DataTypeJAXBTypeAdapter;
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.AnyURIDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.Base64BinaryDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.BooleanDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.DateDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.DateTimeDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.DayTimeDurationDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.DnsNameDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.DoubleDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.HexBinaryDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.IntegerDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.IpAddressDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.RFC822NameDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.TimeDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.X500DataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.YearMonthDurationDataTypeAttribute;

/**
 * This initializer initializes all internal/build-in data types and puts them
 * in the {@link DataTypeJAXBTypeAdapter} JAXB type adapter.
 * 
 * @author Alexander Broekhuis
 * @author Florian Huonder
 */
public class Xacml20DefaultDataTypesJaxbInitializer extends
		AbstractDataTypesJaxbTypeAdapterInitializer {

	/**
	 * {@inheritDoc}<br />
	 * <b>This implementation:</b><br />
	 * Instantiates all default XACML 2.0 {@link DataTypeAttribute
	 * DataTypeAttributes}.
	 */
	@Override
	protected Map<String, DataTypeAttribute<?>> createTypeInstances() {

		List<DataTypeAttribute<?>> instances = createInstances(
				AnyURIDataTypeAttribute.class,
				Base64BinaryDataTypeAttribute.class,
				BooleanDataTypeAttribute.class, DateDataTypeAttribute.class,
				DateTimeDataTypeAttribute.class,
				DayTimeDurationDataTypeAttribute.class,
				DnsNameDataTypeAttribute.class, DoubleDataTypeAttribute.class,
				HexBinaryDataTypeAttribute.class,
				IntegerDataTypeAttribute.class,
				IpAddressDataTypeAttribute.class,
				RFC822NameDataTypeAttribute.class,
				StringDataTypeAttribute.class, TimeDataTypeAttribute.class,
				X500DataTypeAttribute.class,
				YearMonthDurationDataTypeAttribute.class);

		Map<String, DataTypeAttribute<?>> instancesMap = new HashMap<String, DataTypeAttribute<?>>();
		for (DataTypeAttribute<?> dataTypeAttribute : instances) {
			instancesMap.put(dataTypeAttribute.getDatatypeURI(),
					dataTypeAttribute);
		}

		return instancesMap;
	}
}