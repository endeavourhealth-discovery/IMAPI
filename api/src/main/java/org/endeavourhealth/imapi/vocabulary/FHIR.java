package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class FHIR {
	public static final String DSTU2 = "http://hl7.org/2-0/fhir/StructureDefinition#";
	public static final String DOMAIN = "http://hl7.org/fhir/";
	public static final String PREFIX = "fhir";
	public static final TTIriRef GRAPH_FHIR = iri(IM.DOMAIN);
}
