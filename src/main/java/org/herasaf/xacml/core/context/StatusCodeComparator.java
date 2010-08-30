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
package org.herasaf.xacml.core.context;

import java.util.Comparator;

import org.herasaf.xacml.core.simplePDP.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This comparator implementation compares two Status Codes.
 * 
 * The "bigger" Status Code has the higher priority and shall override the
 * "smaller" one.
 * 
 * @author Florian Huonder
 */
public class StatusCodeComparator implements Comparator<StatusCode> {
	private final Logger logger = LoggerFactory
			.getLogger(StatusCodeComparator.class);

	/**
	 * {@inheritDoc}
	 */
	public int compare(StatusCode sc1, StatusCode sc2) {
		if (sc1 instanceof XACMLDefaultStatusCode
				&& sc2 instanceof XACMLDefaultStatusCode) {
			switch ((XACMLDefaultStatusCode) sc1) {
			case OK:
				switch ((XACMLDefaultStatusCode) sc2) {
				case OK:
					return 0;
				case MISSING_ATTRIBUTE:
					return -1;
				case PROCESSING_ERROR:
					return -1;
				case SYNTAX_ERROR:
					return -1;
				}
				break;
			case MISSING_ATTRIBUTE:
				switch ((XACMLDefaultStatusCode) sc2) {
				case OK:
					return 1;
				case MISSING_ATTRIBUTE:
					return 0;
				case PROCESSING_ERROR:
					return 1;
				case SYNTAX_ERROR:
					return -1;
				}
				break;
			case PROCESSING_ERROR:
				switch ((XACMLDefaultStatusCode) sc2) {
				case OK:
					return 1;
				case MISSING_ATTRIBUTE:
					return -1;
				case PROCESSING_ERROR:
					return 0;
				case SYNTAX_ERROR:
					return -1;
				}
				break;
			case SYNTAX_ERROR:
				switch ((XACMLDefaultStatusCode) sc2) {
				case OK:
					return 1;
				case MISSING_ATTRIBUTE:
					return 1;
				case PROCESSING_ERROR:
					return 1;
				case SYNTAX_ERROR:
					return 0;
				}
				break;
			}
			InitializationException ie = new InitializationException(
					"Illegal state of status codes.");
			logger.error(ie.getMessage());
			throw ie;
		} else {
			InitializationException ie = new InitializationException(
					"Non-default status codes in use. Use a custom status code comparator.");
			logger.error(ie.getMessage());
			throw ie;
		}
	}
}
