package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum RDFS implements Vocabulary {
    NAMESPACE("http://www.w3.org/2000/01/rdf-schema#"),
    PREFIX("rdfs"),
    LABEL(NAMESPACE.iri +"label"),
    COMMENT(NAMESPACE.iri +"comment"),
    SUBCLASSOF(NAMESPACE.iri +"subClassOf"),
    SUBPROPERTYOF(NAMESPACE.iri +"subPropertyOf"),
    DOMAIN(NAMESPACE.iri +"domain"),
    RANGE(NAMESPACE.iri +"range"),
    RESOURCE(NAMESPACE.iri +"Resource"),
    CLASS(NAMESPACE.iri +"Class"),
    DATATYPE(NAMESPACE.iri +"Datatype"),
    IS_DEFINED_BY(NAMESPACE.iri +"isDefinedBy");

    public final String iri;
    RDFS(String iri) {
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
            RDFS.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
