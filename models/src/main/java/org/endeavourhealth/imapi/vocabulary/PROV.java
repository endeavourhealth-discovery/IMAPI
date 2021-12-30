package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class PROV {
	public static final String NAMESPACE = "http://www.w3.org/ns/prov#";
	public static final String DOMAIN= "http://www.w3.org/ns/";
	public static final String PREFIX = "prov";
	public static final TTIriRef ENTITY = iri(NAMESPACE + "Entity");
	public static final TTIriRef ACTIVITY = iri(NAMESPACE + "Activity");
	public static final TTIriRef AGENT = iri(NAMESPACE + "Agent");
	public static final TTIriRef ENDED_AT_TIME = iri(NAMESPACE + "endedAtTime");
	public static final TTIriRef ATTRIBUTED_TO = iri(NAMESPACE + "wasAttributedTo");
	public static final TTIriRef GRAPH_PROV= TTIriRef.iri(DOMAIN +"prov#");
}
