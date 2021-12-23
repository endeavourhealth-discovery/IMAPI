package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class PROV {
	public static final String NAMESPACE = "http://www.w3.org/ns/prov#";
	public static final String DOMAIN= "http://www.w3.org/ns/";
	public static final String PREFIX = "prov";
	public static final TTIriRef ENTITY = iri(NAMESPACE + "Entity");
	public static final TTIriRef GRAPH_PROV= TTIriRef.iri(DOMAIN +"prov#");
}
