package org.endeavourhealth.imapi.vocabulary;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum SNOMED implements Vocabulary {
    NAMESPACE("http://snomed.info/sct#"),
    PREFIX("sn"),
    GRAPH_SNOMED(NAMESPACE.iri),
    ATTRIBUTE(NAMESPACE.iri + "246061005");

    public final String iri;
    SNOMED(String iri) {
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
            SNOMED.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
