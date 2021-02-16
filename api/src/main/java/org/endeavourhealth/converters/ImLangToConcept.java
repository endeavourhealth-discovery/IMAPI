package org.endeavourhealth.converters;

import org.endeavourhealth.imapi.model.Concept;
import org.springframework.stereotype.Component;

@Component
public class ImLangToConcept {

	public Concept translateDefintionToConcept(String conceptDefintion) {
		return new Concept();
	}

}
