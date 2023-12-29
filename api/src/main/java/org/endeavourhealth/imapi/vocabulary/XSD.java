package org.endeavourhealth.imapi.vocabulary;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum XSD implements Vocabulary {
    NAMESPACE("http://www.w3.org/2001/XMLSchema#"),
    PREFIX("xsd"),
    PATTERN(NAMESPACE.iri +"pattern"),
    MININCLUSIVE(NAMESPACE.iri +"minInclusive"),
    MINEXCLUSIVE(NAMESPACE.iri +"minExclusive"),
    MAXINCLUSIVE(NAMESPACE.iri +"maxInclusive"),
    MAXEXCLUSIVE(NAMESPACE.iri +"maxExclusive"),
    INTEGER(NAMESPACE.iri +"integer"),
    STRING(NAMESPACE.iri +"string"),
    BOOLEAN(NAMESPACE.iri +"boolean"),
    LONG(NAMESPACE.iri +"long"),
    DOUBLE(NAMESPACE.iri +"double");

    public final String iri;
    XSD(String url) {
        this.iri = url;
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
            XSD.valueOf(iri);
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
