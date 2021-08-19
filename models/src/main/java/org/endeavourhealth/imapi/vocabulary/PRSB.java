package org.endeavourhealth.imapi.vocabulary;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class PRSB {
	public static final String NAMESPACE = "http://prsb.info/rs#";
	public static final String PREFIX = "rs";
	
	public static final TTIriRef OPERATIONALISATION = iri(NAMESPACE + "operationalisation");

	private PRSB() {}
}
