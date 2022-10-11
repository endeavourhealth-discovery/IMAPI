package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class RDFS {
    public static final String NAMESPACE = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String PREFIX = "rdfs";
    public static final TTIriRef LABEL= iri(NAMESPACE +"label");
    public static final TTIriRef COMMENT= iri(NAMESPACE +"comment");
    public static final TTIriRef SUBCLASSOF= iri(NAMESPACE +"subClassOf");
    public static final TTIriRef SUBPROPERTYOF= iri(NAMESPACE +"subPropertyOf");
    public static final TTIriRef DOMAIN= iri(NAMESPACE +"domain");
    public static final TTIriRef RANGE= iri(NAMESPACE +"range");
    public static final TTIriRef RESOURCE= iri(NAMESPACE +"Resource");
    public static final TTIriRef CLASS= iri(NAMESPACE +"Class");
    public static final TTIriRef DATATYPE= iri(NAMESPACE +"Datatype");
    public static final TTIriRef IS_DEFINED_BY= iri(NAMESPACE +"isDefinedBy");

    private RDFS() {}
}
