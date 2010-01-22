/*
 * Copyright 2009-2010 HERAS-AF (www.herasaf.org)
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
package org.herasaf.xacml.core.utils;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This JAXB {@link ValidationEventHandler} is used to handle JAXB validation
 * events. Herewith it is possible to log the events properly.
 * 
 * @author Florian Huonder
 * @author René Eggenschwiler
 */
public class DefaultValidationEventHandler implements ValidationEventHandler {
	private final Logger logger = LoggerFactory.getLogger(DefaultValidationEventHandler.class);

	/**
	 * {@inheritDoc} Logs a warning message on an
	 * {@link ValidationEvent#WARNING} event and an error message an a
	 * {@link ValidationEvent#ERROR} and {@link ValidationEvent#FATAL_ERROR}
	 * event.
	 */
	public boolean handleEvent(ValidationEvent event) {
		switch (event.getSeverity()) {
		case ValidationEvent.WARNING:
			logger.warn("JAXB validation event handled: ", event.getLinkedException());
			return true;
		case ValidationEvent.ERROR:
		case ValidationEvent.FATAL_ERROR:
		default:
			logger.error("JAXB validation event handled: ", event.getLinkedException());
			return false;
		}
	}
}