package org.endeavourhealth.imapi.vocabulary.im;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum VALIDATION implements Vocabulary {
    NAMESPACE(IM.NAMESPACE.iri + "Validation_"),

    IS_DEFINITION(NAMESPACE.iri + "isDefinition"),
    HAS_PARENT(NAMESPACE.iri + "hasParent"),
    IS_IRI(NAMESPACE.iri + "isIri"),
    IS_TERMCODE(NAMESPACE.iri + "isTermcode"),
    IS_PROPERTY(NAMESPACE.iri + "isProperty"),
    IS_SCHEME(NAMESPACE.iri + "isScheme"),
    IS_STATUS(NAMESPACE.iri + "isStatus");

    public final String iri;

    VALIDATION(String url) {
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
            VALIDATION.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
