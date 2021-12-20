package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class RDF {
    public static final String NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static final String PREFIX = "rdf";
    public static final TTIriRef TYPE = iri(NAMESPACE + "type");
    public static final TTIriRef PROPERTY = iri(NAMESPACE + "Property");
    public static final TTIriRef LIST = iri(NAMESPACE + "List");
    public static final TTIriRef PREDICATE = iri(NAMESPACE + "predicate");
    public static final TTIriRef SUBJECT = iri(NAMESPACE + "subject");
    private RDF() {}
}
