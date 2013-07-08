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
package org.herasaf.xacml.core.simplePDP.initializers.jaxb.typeadapter.xacml20.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.herasaf.xacml.core.converter.FunctionsJAXBTypeAdapter;
import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.DoubleAbsFunction;
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.DoubleAddFunction;
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.DoubleDivideFunction;
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.DoubleMultiplyFunction;
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.DoubleSubtractFunction;
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.FloorFunction;
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.IntegerAbsFunction;
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.IntegerAddFunction;
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.IntegerDivideFunction;
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.IntegerModFunction;
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.IntegerMultiplyFunction;
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.IntegerSubtractFunction;
import org.herasaf.xacml.core.function.impl.arithmeticFunctions.RoundFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.AnyUriBagFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.AnyUriBagSizeFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.AnyUriIsInFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.AnyUriOneAndOnlyFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.Base64BinaryBagFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.Base64BinaryBagSizeFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.Base64BinaryIsInFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.Base64BinaryOneAndOnlyFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.BooleanBagFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.BooleanBagSizeFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.BooleanIsInFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.BooleanOneAndOnlyFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DateBagFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DateBagSizeFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DateIsInFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DateOneAndOnlyFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DateTimeBagFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DateTimeBagSizeFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DateTimeIsInFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DateTimeOneAndOnlyFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DayTimeDurationBagFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DayTimeDurationBagSizeFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DayTimeDurationIsInFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DayTimeDurationOneAndOnlyFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DoubleBagFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DoubleBagSizeFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DoubleIsInFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.DoubleOneAndOnlyFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.HexBinaryBagFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.HexBinaryBagSizeFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.HexBinaryIsInFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.HexBinaryOneAndOnlyFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.IntegerBagFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.IntegerBagSizeFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.IntegerIsInFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.IntegerOneAndOnlyFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.RFC822NameBagFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.RFC822NameBagSizeFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.RFC822NameIsInFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.RFC822NameOneAndOnlyFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.StringBagFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.StringBagSizeFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.StringIsInFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.StringOneAndOnlyFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.TimeBagFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.TimeBagSizeFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.TimeIsInFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.TimeOneAndOnlyFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.X500NameBagFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.X500NameBagSizeFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.X500NameIsInFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.X500NameOneAndOnlyFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.YearMonthDurationBagFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.YearMonthDurationBagSizeFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.YearMonthDurationIsInFunction;
import org.herasaf.xacml.core.function.impl.bagFunctions.YearMonthDurationOneAndOnlyFunction;
import org.herasaf.xacml.core.function.impl.dateAndTimeArithmeticFunctions.DateAddYearMonthDurationFunction;
import org.herasaf.xacml.core.function.impl.dateAndTimeArithmeticFunctions.DateSubtractYearMonthDurationFunction;
import org.herasaf.xacml.core.function.impl.dateAndTimeArithmeticFunctions.DateTimeAddDayTimeDurationFunction;
import org.herasaf.xacml.core.function.impl.dateAndTimeArithmeticFunctions.DateTimeAddYearMonthDurationFunction;
import org.herasaf.xacml.core.function.impl.dateAndTimeArithmeticFunctions.DateTimeSubtractDayTimeDurationFunction;
import org.herasaf.xacml.core.function.impl.dateAndTimeArithmeticFunctions.DateTimeSubtractYearMonthDurationFunction;
import org.herasaf.xacml.core.function.impl.equalityPredicates.AnyURIEqualFunction;
import org.herasaf.xacml.core.function.impl.equalityPredicates.Base64BinaryEqualFunction;
import org.herasaf.xacml.core.function.impl.equalityPredicates.BooleanEqualFunction;
import org.herasaf.xacml.core.function.impl.equalityPredicates.DateEqualFunction;
import org.herasaf.xacml.core.function.impl.equalityPredicates.DateTimeEqualFunction;
import org.herasaf.xacml.core.function.impl.equalityPredicates.DayTimeDurationEqualFunction;
import org.herasaf.xacml.core.function.impl.equalityPredicates.DoubleEqualFunction;
import org.herasaf.xacml.core.function.impl.equalityPredicates.HexBinaryEqualFunction;
import org.herasaf.xacml.core.function.impl.equalityPredicates.IntegerEqualFunction;
import org.herasaf.xacml.core.function.impl.equalityPredicates.Rfc822NameEqualFunction;
import org.herasaf.xacml.core.function.impl.equalityPredicates.StringEqualFunction;
import org.herasaf.xacml.core.function.impl.equalityPredicates.TimeEqualFunction;
import org.herasaf.xacml.core.function.impl.equalityPredicates.X500NameEqualFunction;
import org.herasaf.xacml.core.function.impl.equalityPredicates.YearMonthDurationEqualFunction;
import org.herasaf.xacml.core.function.impl.higherOrderBagFunctions.AllOfAllFunction;
import org.herasaf.xacml.core.function.impl.higherOrderBagFunctions.AllOfAnyFunction;
import org.herasaf.xacml.core.function.impl.higherOrderBagFunctions.AllOfFunction;
import org.herasaf.xacml.core.function.impl.higherOrderBagFunctions.AnyOfAllFunction;
import org.herasaf.xacml.core.function.impl.higherOrderBagFunctions.AnyOfAnyFunction;
import org.herasaf.xacml.core.function.impl.higherOrderBagFunctions.AnyOfFunction;
import org.herasaf.xacml.core.function.impl.higherOrderBagFunctions.MapFunction;
import org.herasaf.xacml.core.function.impl.logicalFunctions.ANDFunction;
import org.herasaf.xacml.core.function.impl.logicalFunctions.NOFFunction;
import org.herasaf.xacml.core.function.impl.logicalFunctions.NotFunction;
import org.herasaf.xacml.core.function.impl.logicalFunctions.ORFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.DateGreaterThanFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.DateGreaterThanOrEqualFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.DateLessThanFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.DateLessThanOrEqualFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.DateTimeGreaterThanFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.DateTimeGreaterThanOrEqualFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.DateTimeLessThanFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.DateTimeLessThanOrEqualFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.StringGreaterThanFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.StringGreaterThanOrEqualFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.StringLessThanFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.StringLessThanOrEqualFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.TimeGreaterThanFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.TimeGreaterThanOrEqualFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.TimeInRangeFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.TimeLessThanFunction;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.TimeLessThanOrEqualFunction;
import org.herasaf.xacml.core.function.impl.numericComparisonFunctions.DoubleGreaterThanFunction;
import org.herasaf.xacml.core.function.impl.numericComparisonFunctions.DoubleGreaterThanOrEqualFunction;
import org.herasaf.xacml.core.function.impl.numericComparisonFunctions.DoubleLessThanFunction;
import org.herasaf.xacml.core.function.impl.numericComparisonFunctions.DoubleLessThanOrEqualFunction;
import org.herasaf.xacml.core.function.impl.numericComparisonFunctions.IntegerGreaterThanFunction;
import org.herasaf.xacml.core.function.impl.numericComparisonFunctions.IntegerGreaterThanOrEqualFunction;
import org.herasaf.xacml.core.function.impl.numericComparisonFunctions.IntegerLessThanFunction;
import org.herasaf.xacml.core.function.impl.numericComparisonFunctions.IntegerLessThanOrEqualFunction;
import org.herasaf.xacml.core.function.impl.numericDataTypeConversionFunctions.DoubleToIntegerFunction;
import org.herasaf.xacml.core.function.impl.numericDataTypeConversionFunctions.IntegerToDoubleFunction;
import org.herasaf.xacml.core.function.impl.regularExpressionBasedFunctions.AnyURIRegexpMatchFunction;
import org.herasaf.xacml.core.function.impl.regularExpressionBasedFunctions.DNSNameRegexpMatchFunction;
import org.herasaf.xacml.core.function.impl.regularExpressionBasedFunctions.IPAddressRegexpMatchFunction;
import org.herasaf.xacml.core.function.impl.regularExpressionBasedFunctions.RFC822NameRegexpMatchFunction;
import org.herasaf.xacml.core.function.impl.regularExpressionBasedFunctions.StringRegexpMatchFunction;
import org.herasaf.xacml.core.function.impl.regularExpressionBasedFunctions.X500NameRegexpMatchFunction;
import org.herasaf.xacml.core.function.impl.setFunction.AnyURIAtLeastOneMemberOfFunction;
import org.herasaf.xacml.core.function.impl.setFunction.AnyURIIntersectionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.AnyURISetEqualsFunction;
import org.herasaf.xacml.core.function.impl.setFunction.AnyURISubsetFunction;
import org.herasaf.xacml.core.function.impl.setFunction.AnyURIUnionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.Base64BinaryAtLeastOneMemberOfFunction;
import org.herasaf.xacml.core.function.impl.setFunction.Base64BinaryIntersectionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.Base64BinarySetEqualsFunction;
import org.herasaf.xacml.core.function.impl.setFunction.Base64BinarySubsetFunction;
import org.herasaf.xacml.core.function.impl.setFunction.Base64BinaryUnionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.BooleanAtLeastOneMemberOfFunction;
import org.herasaf.xacml.core.function.impl.setFunction.BooleanIntersectionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.BooleanSetEqualsFunction;
import org.herasaf.xacml.core.function.impl.setFunction.BooleanSubsetFunction;
import org.herasaf.xacml.core.function.impl.setFunction.BooleanUnionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DateAtLeastOneMemberOfFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DateIntersectionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DateSetEqualsFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DateSubsetFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DateTimeAtLeastOneMemberOfFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DateTimeIntersectionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DateTimeSetEqualsFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DateTimeSubsetFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DateTimeUnionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DateUnionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DayTimeDurationAtLeastOneMemberOfFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DayTimeDurationIntersectionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DayTimeDurationSetEqualsFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DayTimeDurationSubsetFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DayTimeDurationUnionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DoubleAtLeastOneMemberOfFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DoubleIntersectionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DoubleSetEqualsFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DoubleSubsetFunction;
import org.herasaf.xacml.core.function.impl.setFunction.DoubleUnionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.HexBinaryAtLeastOneMemberOfFunction;
import org.herasaf.xacml.core.function.impl.setFunction.HexBinaryIntersectionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.HexBinarySetEqualsFunction;
import org.herasaf.xacml.core.function.impl.setFunction.HexBinarySubsetFunction;
import org.herasaf.xacml.core.function.impl.setFunction.HexBinaryUnionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.IntegerAtLeastOneMemberOfFunction;
import org.herasaf.xacml.core.function.impl.setFunction.IntegerIntersectionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.IntegerSetEqualsFunction;
import org.herasaf.xacml.core.function.impl.setFunction.IntegerSubsetFunction;
import org.herasaf.xacml.core.function.impl.setFunction.IntegerUnionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.RFC822NameAtLeastOneMemberOfFunction;
import org.herasaf.xacml.core.function.impl.setFunction.RFC822NameIntersectionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.RFC822NameSetEqualsFunction;
import org.herasaf.xacml.core.function.impl.setFunction.RFC822NameSubsetFunction;
import org.herasaf.xacml.core.function.impl.setFunction.RFC822NameUnionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.StringAtLeastOneMemberOfFunction;
import org.herasaf.xacml.core.function.impl.setFunction.StringIntersectionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.StringSetEqualsFunction;
import org.herasaf.xacml.core.function.impl.setFunction.StringSubsetFunction;
import org.herasaf.xacml.core.function.impl.setFunction.StringUnionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.TimeAtLeastOneMemberOfFunction;
import org.herasaf.xacml.core.function.impl.setFunction.TimeIntersectionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.TimeSetEqualsFunction;
import org.herasaf.xacml.core.function.impl.setFunction.TimeSubsetFunction;
import org.herasaf.xacml.core.function.impl.setFunction.TimeUnionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.X500NameAtLeastOneMemberOfFunction;
import org.herasaf.xacml.core.function.impl.setFunction.X500NameIntersectionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.X500NameSetEqualsFunction;
import org.herasaf.xacml.core.function.impl.setFunction.X500NameSubsetFunction;
import org.herasaf.xacml.core.function.impl.setFunction.X500NameUnionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.YearMonthDurationAtLeastOneMemberOfFunction;
import org.herasaf.xacml.core.function.impl.setFunction.YearMonthDurationIntersectionFunction;
import org.herasaf.xacml.core.function.impl.setFunction.YearMonthDurationSetEqualsFunction;
import org.herasaf.xacml.core.function.impl.setFunction.YearMonthDurationSubsetFunction;
import org.herasaf.xacml.core.function.impl.setFunction.YearMonthDurationUnionFunction;
import org.herasaf.xacml.core.function.impl.specialMatchFunctions.RFC822NameMatchFunction;
import org.herasaf.xacml.core.function.impl.specialMatchFunctions.X500NameMatchFunction;
import org.herasaf.xacml.core.function.impl.stringConversionFunctions.StringNormalizeSpaceFunction;
import org.herasaf.xacml.core.function.impl.stringConversionFunctions.StringNormalizeToLowerCaseFunction;
import org.herasaf.xacml.core.function.impl.stringFunctions.StringConcatenateFunction;
import org.herasaf.xacml.core.function.impl.stringFunctions.UriStringConcatenateFunction;

/**
 * This initializer initializes all internal/build-n functions and puts them in
 * the {@link FunctionsJAXBTypeAdapter} JAXB type adapter.
 * 
 * @author Alexander Broekhuis
 * @author Florian Huonder
 */
public class Xacml20DefaultFunctionsJaxbInitializer extends
		AbstractFunctionsJaxbTypeAdapterInitializer {

	/**
	 * {@inheritDoc}<br />
	 * <b>This implementation:</b><br />
	 * Instantiates all default XACML 2.0 {@link Function Functions}.
	 */
	@Override
	protected Map<String, Function> createTypeInstances() {
		List<Function> arithmeticFunctions = createArithmeticFunctions();
		List<Function> bagFunctions = createBagFunctions();
		List<Function> higherOrderBagFunctions = createHigherOrderBagFunctions();
		List<Function> dateAndTimeArithmeticFunctions = createDateAndTimeArithmeticFunctions();
		List<Function> equalityPredicateFunctions = createEqualityPredicateFunctions();
		List<Function> logicalFunctions = createLogicalFunctions();
		List<Function> nonNumericComparisonFunctions = createNonNumericComparisonFunctions();
		List<Function> numericComparisonFunctions = createNumericComparisonFunctions();
		List<Function> numericDataTypeConversionFunctions = createNumericDataTypeConversionFunctions();
		List<Function> regularExpressionBasedFunctions = createRegularExpressionBasedFunctions();
		List<Function> setFunctions = createSetFunctions();
		List<Function> specialMatchFunctions = createSpecialMatchFunctions();
		List<Function> stringConversionFunctions = createStringConversionFunctions();
		List<Function> stringFunctions = createStringFunctions();

		@SuppressWarnings("unchecked")
		List<Function> allFunctions = concatenate(arithmeticFunctions,
				bagFunctions, higherOrderBagFunctions,
				dateAndTimeArithmeticFunctions, equalityPredicateFunctions,
				logicalFunctions, nonNumericComparisonFunctions,
				numericComparisonFunctions, numericDataTypeConversionFunctions,
				regularExpressionBasedFunctions, setFunctions,
				specialMatchFunctions, stringConversionFunctions,
				stringFunctions);

		Map<String, Function> instancesMap = new HashMap<String, Function>();
		for (Function function : allFunctions) {
			instancesMap.put(function.toString(), function);
		}

		return instancesMap;
	}

	private List<Function> createArithmeticFunctions() {
		List<Function> arithmeticFunctions = createInstances(
				DoubleAbsFunction.class, DoubleAddFunction.class,
				DoubleDivideFunction.class, DoubleMultiplyFunction.class,
				DoubleSubtractFunction.class, FloorFunction.class,
				IntegerAbsFunction.class, IntegerAddFunction.class,
				IntegerDivideFunction.class, IntegerModFunction.class,
				IntegerMultiplyFunction.class, IntegerSubtractFunction.class,
				RoundFunction.class);
		return arithmeticFunctions;
	}

	private List<Function> createBagFunctions() {
		List<Function> bagFunctions = createInstances(AnyUriBagFunction.class,
				AnyUriBagSizeFunction.class, AnyUriIsInFunction.class,
				AnyUriOneAndOnlyFunction.class, Base64BinaryBagFunction.class,
				Base64BinaryBagSizeFunction.class,
				Base64BinaryIsInFunction.class,
				Base64BinaryOneAndOnlyFunction.class, BooleanBagFunction.class,
				BooleanBagSizeFunction.class, BooleanIsInFunction.class,
				BooleanOneAndOnlyFunction.class, DateBagFunction.class,
				DateBagSizeFunction.class, DateIsInFunction.class,
				DateOneAndOnlyFunction.class, DateTimeBagFunction.class,
				DateTimeBagSizeFunction.class, DateTimeIsInFunction.class,
				DateTimeOneAndOnlyFunction.class,
				DayTimeDurationBagFunction.class,
				DayTimeDurationBagSizeFunction.class,
				DayTimeDurationIsInFunction.class,
				DayTimeDurationOneAndOnlyFunction.class,
				DoubleBagFunction.class, DoubleBagSizeFunction.class,
				DoubleIsInFunction.class, DoubleOneAndOnlyFunction.class,
				HexBinaryBagFunction.class, HexBinaryBagSizeFunction.class,
				HexBinaryIsInFunction.class, HexBinaryOneAndOnlyFunction.class,
				IntegerBagFunction.class, IntegerBagSizeFunction.class,
				IntegerIsInFunction.class, IntegerOneAndOnlyFunction.class,
				RFC822NameBagFunction.class, RFC822NameBagSizeFunction.class,
				RFC822NameIsInFunction.class,
				RFC822NameOneAndOnlyFunction.class, StringBagFunction.class,
				StringBagSizeFunction.class, StringIsInFunction.class,
				StringOneAndOnlyFunction.class, TimeBagFunction.class,
				TimeBagSizeFunction.class, TimeIsInFunction.class,
				TimeOneAndOnlyFunction.class, X500NameBagFunction.class,
				X500NameBagSizeFunction.class, X500NameIsInFunction.class,
				X500NameOneAndOnlyFunction.class,
				YearMonthDurationBagFunction.class,
				YearMonthDurationBagSizeFunction.class,
				YearMonthDurationIsInFunction.class,
				YearMonthDurationOneAndOnlyFunction.class);
		return bagFunctions;
	}

	private List<Function> createHigherOrderBagFunctions() {
		List<Function> higherOrderBagFunctions = createInstances(
				AllOfAllFunction.class, AllOfAnyFunction.class,
				AllOfFunction.class, AnyOfAllFunction.class,
				AnyOfAnyFunction.class, AnyOfFunction.class, MapFunction.class);
		return higherOrderBagFunctions;
	}

	private List<Function> createDateAndTimeArithmeticFunctions() {
		List<Function> dateAndTimeArithmeticFunctions = createInstances(
				DateAddYearMonthDurationFunction.class,
				DateSubtractYearMonthDurationFunction.class,
				DateTimeAddDayTimeDurationFunction.class,
				DateTimeAddYearMonthDurationFunction.class,
				DateTimeSubtractDayTimeDurationFunction.class,
				DateTimeSubtractYearMonthDurationFunction.class);
		return dateAndTimeArithmeticFunctions;
	}

	private List<Function> createEqualityPredicateFunctions() {
		List<Function> equalityPredicates = createInstances(
				AnyURIEqualFunction.class, Base64BinaryEqualFunction.class,
				BooleanEqualFunction.class, DateEqualFunction.class,
				DateTimeEqualFunction.class,
				DayTimeDurationEqualFunction.class, DoubleEqualFunction.class,
				HexBinaryEqualFunction.class, IntegerEqualFunction.class,
				Rfc822NameEqualFunction.class, StringEqualFunction.class,
				TimeEqualFunction.class, X500NameEqualFunction.class,
				YearMonthDurationEqualFunction.class);
		return equalityPredicates;
	}

	private List<Function> createLogicalFunctions() {
		List<Function> logicalFunctions = createInstances(ANDFunction.class,
				NOFFunction.class, NotFunction.class, ORFunction.class);
		return logicalFunctions;
	}

	private List<Function> createNonNumericComparisonFunctions() {
		List<Function> nonNumericComparisonFunctions = createInstances(
				DateGreaterThanFunction.class,
				DateGreaterThanOrEqualFunction.class,
				DateLessThanFunction.class, DateLessThanOrEqualFunction.class,
				DateTimeGreaterThanFunction.class,
				DateTimeGreaterThanOrEqualFunction.class,
				DateTimeLessThanFunction.class,
				DateTimeLessThanOrEqualFunction.class,
				StringGreaterThanFunction.class,
				StringGreaterThanOrEqualFunction.class,
				StringLessThanFunction.class,
				StringLessThanOrEqualFunction.class,
				TimeGreaterThanFunction.class,
				TimeGreaterThanOrEqualFunction.class,
				TimeInRangeFunction.class, TimeLessThanFunction.class,
				TimeLessThanOrEqualFunction.class);
		return nonNumericComparisonFunctions;
	}

	private List<Function> createNumericComparisonFunctions() {
		List<Function> numericComparisonFunctions = createInstances(
				DoubleGreaterThanFunction.class,
				DoubleGreaterThanOrEqualFunction.class,
				DoubleLessThanFunction.class,
				DoubleLessThanOrEqualFunction.class,
				IntegerGreaterThanFunction.class,
				IntegerGreaterThanOrEqualFunction.class,
				IntegerLessThanFunction.class,
				IntegerLessThanOrEqualFunction.class);
		return numericComparisonFunctions;
	}

	private List<Function> createNumericDataTypeConversionFunctions() {
		List<Function> numericDataTypeConversionFunctions = createInstances(
				DoubleToIntegerFunction.class, IntegerToDoubleFunction.class);
		return numericDataTypeConversionFunctions;
	}

	private List<Function> createRegularExpressionBasedFunctions() {
		List<Function> regularExpressionBasedFunctions = createInstances(
				AnyURIRegexpMatchFunction.class,
				DNSNameRegexpMatchFunction.class,
				IPAddressRegexpMatchFunction.class,
				RFC822NameRegexpMatchFunction.class,
				StringRegexpMatchFunction.class,
				X500NameRegexpMatchFunction.class);
		return regularExpressionBasedFunctions;
	}

	private List<Function> createSetFunctions() {
		List<Function> setFunctions = createInstances(
				AnyURIAtLeastOneMemberOfFunction.class,
				AnyURIIntersectionFunction.class,
				AnyURISetEqualsFunction.class, AnyURISubsetFunction.class,
				AnyURIUnionFunction.class,
				Base64BinaryAtLeastOneMemberOfFunction.class,
				Base64BinaryIntersectionFunction.class,
				Base64BinarySetEqualsFunction.class,
				Base64BinarySubsetFunction.class,
				Base64BinaryUnionFunction.class,
				BooleanAtLeastOneMemberOfFunction.class,
				BooleanIntersectionFunction.class,
				BooleanSetEqualsFunction.class, BooleanSubsetFunction.class,
				BooleanUnionFunction.class,
				DateAtLeastOneMemberOfFunction.class,
				DateIntersectionFunction.class, DateSetEqualsFunction.class,
				DateSubsetFunction.class,
				DateTimeAtLeastOneMemberOfFunction.class,
				DateTimeIntersectionFunction.class,
				DateTimeSetEqualsFunction.class, DateTimeSubsetFunction.class,
				DateTimeUnionFunction.class, DateUnionFunction.class,
				DayTimeDurationAtLeastOneMemberOfFunction.class,
				DayTimeDurationIntersectionFunction.class,
				DayTimeDurationSetEqualsFunction.class,
				DayTimeDurationSubsetFunction.class,
				DayTimeDurationUnionFunction.class,
				DoubleAtLeastOneMemberOfFunction.class,
				DoubleIntersectionFunction.class,
				DoubleSetEqualsFunction.class, DoubleSubsetFunction.class,
				DoubleUnionFunction.class,
				HexBinaryAtLeastOneMemberOfFunction.class,
				HexBinaryIntersectionFunction.class,
				HexBinarySetEqualsFunction.class,
				HexBinarySubsetFunction.class, HexBinaryUnionFunction.class,
				IntegerAtLeastOneMemberOfFunction.class,
				IntegerIntersectionFunction.class,
				IntegerSetEqualsFunction.class, IntegerSubsetFunction.class,
				IntegerUnionFunction.class,
				RFC822NameAtLeastOneMemberOfFunction.class,
				RFC822NameIntersectionFunction.class,
				RFC822NameSetEqualsFunction.class,
				RFC822NameSubsetFunction.class, RFC822NameUnionFunction.class,
				StringAtLeastOneMemberOfFunction.class,
				StringIntersectionFunction.class,
				StringSetEqualsFunction.class, StringSubsetFunction.class,
				StringUnionFunction.class,
				TimeAtLeastOneMemberOfFunction.class,
				TimeIntersectionFunction.class, TimeSetEqualsFunction.class,
				TimeSubsetFunction.class, TimeUnionFunction.class,
				X500NameAtLeastOneMemberOfFunction.class,
				X500NameIntersectionFunction.class,
				X500NameSetEqualsFunction.class, X500NameSubsetFunction.class,
				X500NameUnionFunction.class,
				YearMonthDurationAtLeastOneMemberOfFunction.class,
				YearMonthDurationIntersectionFunction.class,
				YearMonthDurationSetEqualsFunction.class,
				YearMonthDurationSubsetFunction.class,
				YearMonthDurationUnionFunction.class);
		return setFunctions;
	}

	private List<Function> createSpecialMatchFunctions() {
		List<Function> specialMatchFunctions = createInstances(
				RFC822NameMatchFunction.class, X500NameMatchFunction.class);
		return specialMatchFunctions;
	}

	private List<Function> createStringConversionFunctions() {
		List<Function> stringConversionFunctions = createInstances(
				StringNormalizeSpaceFunction.class,
				StringNormalizeToLowerCaseFunction.class);
		return stringConversionFunctions;
	}

	private List<Function> createStringFunctions() {
		List<Function> stringFunctions = createInstances(
				StringConcatenateFunction.class,
				UriStringConcatenateFunction.class);
		return stringFunctions;
	}

	private <T> List<T> concatenate(List<T>... lists) {
		List<T> allFunctions = new ArrayList<T>();

		for (List<T> list : lists) {
			allFunctions.addAll(list);
		}

		return allFunctions;
	}
}