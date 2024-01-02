package org.endeavourhealth.imapi.vocabulary;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum RDF implements Vocabulary {
    NAMESPACE("http://www.w3.org/1999/02/22-rdf-syntax-ns#"),
    PREFIX("rdf"),
    TYPE(NAMESPACE.iri + "type"),
    PROPERTY(NAMESPACE.iri + "Property"),
    LIST(NAMESPACE.iri + "List"),
    PREDICATE(NAMESPACE.iri + "predicate"),
    SUBJECT(NAMESPACE.iri + "subject"),
    OBJECT(NAMESPACE.iri + "object");
    public final String iri;
    RDF(String iri) {
        this.iri = iri;
    }

    @Override
    public TTIriRef asTTIriRef() {
        return iri(this.iri);
    }

    @Override
    @JsonValue
    public String getIri() {
        return iri;
    }

    public static boolean contains(String iri) {
        try {
            RDF.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return iri;
    }
}
