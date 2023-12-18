package org.endeavourhealth.imapi.vocabulary.im;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum EDITOR implements Vocabulary {
    NAMESPACE(IM.NAMESPACE.iri + "Editor_"),

    CONCEPT_SHAPE(NAMESPACE.iri + "ConceptShape"),
    CONCEPT_SET_SHAPE(NAMESPACE.iri + "ConceptSetShape"),
    VALUE_SET_SHAPE(NAMESPACE.iri + "ValueSetShape"),
    FOLDER_SHAPE(NAMESPACE.iri + "FolderShape"),
    DATA_MODEL_SHAPE(NAMESPACE.iri + "DataModelShape"),
    COHORT_QUERY_SHAPE(NAMESPACE.iri + "CohortQueryShape"),
    PROPERTY_SHAPE(NAMESPACE.iri + "PropertyShape");

    public final String iri;

    EDITOR(String url) {
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
            EDITOR.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
