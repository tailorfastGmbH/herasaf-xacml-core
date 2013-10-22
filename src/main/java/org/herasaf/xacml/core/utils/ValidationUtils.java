package org.herasaf.xacml.core.utils;

/**
 * Contains validation utilities.
 * 
 * @author Stefan Oberholzer
 */
public class ValidationUtils {

	/**
	 * Returns true when the given array is empty or null.
	 * 
	 * @param values
	 *            The array to validate.
	 * 
	 * @return True when the array is empty or null, false otherwise.
	 */
	public static <T> boolean isEmpty(T[] values) {
		return (values == null || values.length == 0);
	}

	/**
	 * Returns true when the given String is empty or null.
	 * 
	 * @param value
	 *            The string to validate.
	 * 
	 * @return True when the string is null or empty, false otherwise.
	 */
	public static boolean isEmpty(String value) {
		return (value == null || value.length() == 0);
	}
}
