package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum QR implements Vocabulary {
	NAMESPACE("http://apiqcodes.org/qcodes#"),
	DOMAIN("http://apiqcodes.org/"),
	PREFIX("qc");

	public final String iri;
	QR(String iri) {
		this.iri = iri;
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
			QR.valueOf(iri);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
