package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum FHIR implements Vocabulary {
	DOMAIN("http://hl7.org/fhir/"),
	PREFIX ("fhir"),
	DSTU2("http://hl7.org/2-0/fhir/StructureDefinition#"),
	GRAPH_FHIR(DOMAIN.iri),
	VALUESET_FOLDER("http://endhealth.info/im#VSET_FHIR");

	public final String iri;

	FHIR(String url) {this.iri = url;}

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
			FHIR.valueOf(iri);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
