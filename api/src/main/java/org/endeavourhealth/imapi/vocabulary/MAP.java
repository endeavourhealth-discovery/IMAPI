package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum MAP implements Vocabulary {
	NAMESPACE("http://endhealth.info/map#"),
	DOMAIN("http://endhealth.info/"),
	PREFIX("map");

	public final String iri;
	MAP(String url) {
		this.iri = url;
	}

	@Override
	public TTIriRef asTTIriRef() {
		return iri(this.iri);
	}

	@Override
	public String getIri() {
		return iri;
	}

	public static boolean contains(String iri) {
		try {
			MAP.valueOf(iri);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
