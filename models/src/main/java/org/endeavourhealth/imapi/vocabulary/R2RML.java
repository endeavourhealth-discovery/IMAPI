package org.endeavourhealth.imapi.vocabulary;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class R2RML {

    public static final String NAMESPACE = "http://www.w3.org/ns/r2rml#";
    public static final String PREFIX = "r2r";

    public static final TTIriRef BLANK_NODE = iri(NAMESPACE + "BlankNode");
    public static final TTIriRef TEMPLATE = iri(NAMESPACE + "template");
    public static final TTIriRef PARENT_TRIPLES_MAP = iri(NAMESPACE + "parentTriplesMap");
    public static final TTIriRef PREDICATE = iri(NAMESPACE + "predicate");
    public static final TTIriRef PREDICATE_MAP = iri(NAMESPACE + "predicateMap");
    public static final TTIriRef CLASS = iri(NAMESPACE + "class");
    public static final TTIriRef TERM_TYPE = iri(NAMESPACE + "termType");
    public static final TTIriRef SUBJECT_MAP = iri(NAMESPACE + "subjectMap");
    public static final TTIriRef GRAPH = iri(NAMESPACE + "graph");
    public static final TTIriRef CONSTANT = iri(NAMESPACE + "constant");

    private R2RML() {
    }

}
