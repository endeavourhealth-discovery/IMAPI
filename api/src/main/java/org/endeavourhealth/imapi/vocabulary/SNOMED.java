package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum SNOMED implements Vocabulary {
    NAMESPACE("http://snomed.info/sct#"),
    PREFIX("sn"),
    GRAPH_SNOMED(NAMESPACE.iri);

    public final String iri;
    SNOMED(String iri) {
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
            SNOMED.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
