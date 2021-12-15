package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class CONFIG {
    public static final String NAMESPACE = "http://endhealth.info/config#";
    public static final String DOMAIN= "http://endhealth.info/";
    public static final String PREFIX = "cfg";

    // Config entries
    public static final TTIriRef DEFINITION = iri(NAMESPACE + "definition");
    public static final TTIriRef FILTER_DEFAULTS = iri(NAMESPACE + "filterDefaults");
    public static final TTIriRef INFERRED_PREDICATES = iri(NAMESPACE + "inferredPredicates");
    public static final TTIriRef INFERRED_EXCLUDE_PREDICATES = iri(NAMESPACE + "inferredExcludePredicates");
    public static final TTIriRef CONCEPT_DASHBOARD = iri(NAMESPACE + "conceptDashboard");
    public static final TTIriRef DEFAULT_PREDICATE_NAMES = iri(NAMESPACE + "defaultPredicateNames");
    public static final TTIriRef XML_SCHEMA_DATATYPES = iri(NAMESPACE + "xmlSchemaDataTypes");

    private CONFIG() {}
}

