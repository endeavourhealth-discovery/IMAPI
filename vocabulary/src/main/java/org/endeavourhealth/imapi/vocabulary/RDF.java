package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class RDF {
    public static final String NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static String PREFIX = "rdf";
    public static final TTIriRef TYPE = iri(NAMESPACE + "type");
}
