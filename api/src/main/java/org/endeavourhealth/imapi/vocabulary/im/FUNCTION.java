package org.endeavourhealth.imapi.vocabulary.im;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum FUNCTION implements Vocabulary {
    NAMESPACE(IM.NAMESPACE.iri + "Function_"),
    SNOMED_CONCEPT_GENERATOR(NAMESPACE.iri + "SnomedConceptGenerator"),
    LOCAL_NAME_RETRIEVER(NAMESPACE.iri + "LocalNameRetriever"),
    GET_ADDITIONAL_ALLOWABLE_TYPES(NAMESPACE.iri + "GetAdditionalAllowableTypes"),
    GET_LOGIC_OPTIONS(NAMESPACE.iri + "GetLogicOptions"),
    GET_SET_EDITOR_IRI_SCHEMES(NAMESPACE.iri + "GetSetEditorIriSchemes"),
    IM1_SCHEME_OPTIONS(NAMESPACE.iri + "IM1SchemeOptions"),
    SCHEME_FROM_IRI(NAMESPACE.iri + "SchemeFromIri"),
    GET_USER_EDITABLE_SCHEMES(NAMESPACE.iri + "GetUserEditableSchemes"),
    GENERATE_IRI_CODE(NAMESPACE.iri + "GenerateIriCode"),
    IS_TYPE(NAMESPACE.iri + "IsType"),
    ALLOWABLE_PROPERTIES(NAMESPACE.iri + "AllowableProperties"),
    ALLOWABLE_RANGES(NAMESPACE.iri + "AllowableRanges");

    public final String iri;

    FUNCTION(String url) {
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
            FUNCTION.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
