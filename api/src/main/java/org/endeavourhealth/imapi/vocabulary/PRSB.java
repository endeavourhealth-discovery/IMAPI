package org.endeavourhealth.imapi.vocabulary;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum PRSB implements Vocabulary {
	NAMESPACE("http://prsb.info/rs#"),
	PREFIX("rs");

	public final String iri;
	PRSB(String iri) {
		this.iri = iri;
	}

	@Override
	public TTIriRef asTTIriRef() {
		return iri(this.iri);
	}

	@Override
	@JsonValue
	public String getIri() {
		return iri;
	}

	public static boolean contains(String iri) {
		try {
			PRSB.valueOf(iri);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
