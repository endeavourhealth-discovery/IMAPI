package org.endeavourhealth.imapi.vocabulary.im;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum QUERY implements Vocabulary {
    NAMESPACE(IM.NAMESPACE.iri + "Query_"),

    ALLOWABLE_RANGES(NAMESPACE.iri + "AllowableRanges"),
    GET_ISAS(NAMESPACE.iri + "GetIsas"),
    GET_DESCENDANTS(NAMESPACE.iri + "GetDescendants"),
    SEARCH_CONTAINED_IN(NAMESPACE.iri + "SearchContainedIn"),
    ALLOWABLE_CHILD_TYPES(NAMESPACE.iri + "AllowableChildTypes"),
    PROPERTY_RANGE(NAMESPACE.iri + "PropertyRange"),
    OBJECT_PROPERTY_RANGE_SUGGESTIONS(NAMESPACE.iri + "ObjectPropertyRangeSuggestions"),
    DATA_PROPERTY_RANGE_SUGGESTIONS(NAMESPACE.iri + "DataPropertyRangeSuggestions"),
    ALLOWABLE_PROPERTIES(NAMESPACE.iri + "AllowableProperties"),
    SEARCH_PROPERTIES(NAMESPACE.iri + "SearchProperties"),
    SEARCH_ENTITIES(NAMESPACE.iri + "SearchEntities"),
    SEARCH_FOLDERS(NAMESPACE.iri + "SearchFolders"),
    SEARCH_ALLOWABLE_CONTAINED_IN(NAMESPACE.iri + "SearchAllowableContainedIn"),
    SEARCH_MAIN_TYPES(NAMESPACE.iri + "SearchmainTypes"),
    DM_PROPERTY(NAMESPACE.iri + "DataModelPropertyByShape"),
    SEARCH_SUBCLASS(NAMESPACE.iri + "SearchAllowableSubclass");

    public final String iri;

    QUERY(String url) {
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
            QUERY.valueOf(iri);
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
