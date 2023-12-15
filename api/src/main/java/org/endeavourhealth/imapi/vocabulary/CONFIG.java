package org.endeavourhealth.imapi.vocabulary;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum CONFIG implements Vocabulary {
    NAMESPACE("http://endhealth.info/config#"),
    DOMAIN("http://endhealth.info/"),
    PREFIX("cfg"),
    DEFINITION(NAMESPACE.iri + "definition"),
    SUMMARY(NAMESPACE.iri + "summary"),
    FILTER_DEFAULTS(NAMESPACE.iri + "filterDefaults"),
    INFERRED_PREDICATES(NAMESPACE.iri + "inferredPredicates"),
    INFERRED_EXCLUDE_PREDICATES(NAMESPACE.iri + "inferredExcludePredicates"),
    CONCEPT_DASHBOARD(NAMESPACE.iri + "conceptDashboard"),
    DEFAULT_PREDICATE_NAMES(NAMESPACE.iri + "defaultPredicateNames"),
    XML_SCHEMA_DATA_TYPES(NAMESPACE.iri + "xmlSchemaDataTypes"),
    DEFAULT_PREFIXES(NAMESPACE.iri + "defaultPrefixes"),
    GRAPH_EXCLUDE_PREDICATES(NAMESPACE.iri + "graphExcludePredicates"),
    IM1_PUBLISH(NAMESPACE.iri + "im1Publish");

    public final String iri;
    CONFIG(String url) {
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
            CONFIG.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

