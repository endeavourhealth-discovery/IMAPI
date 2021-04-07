package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class IM {
    public static final String NAMESPACE = "http://endhealth.info/im#";
    public static final String PREFIX = "im";

    //Concept top level predicates
    public static final TTIriRef CODE = iri(NAMESPACE + "code");
    public static final TTIriRef HAS_SCHEME = iri(NAMESPACE + "scheme");
    public static final TTIriRef STATUS = iri(NAMESPACE + "status");

    //Synonyms
    public static final TTIriRef SYNONYM = iri(NAMESPACE + "synonym");

    //Core model types
    public static final TTIriRef RECORD = iri(NAMESPACE + "RecordType");
    public static final TTIriRef SET = iri(NAMESPACE + "Set");
    public static final TTIriRef FOLDER = iri(NAMESPACE + "Folder");
    public static final TTIriRef QUERY_TEMPLATE = iri(NAMESPACE +"QueryTemplate");
    public static final TTIriRef LEGACY = iri(NAMESPACE +"LegacyConcept");
    public static final TTIriRef INDIVIDUAL = iri(NAMESPACE +"Individual");
    public static final TTIriRef GRAPH= iri(NAMESPACE+"Graph");
    public static final TTIriRef VALUESET= iri(NAMESPACE+"ValueSet");

    //Collection predicates
    public static final TTIriRef HAS_MEMBER = iri(NAMESPACE + "hasMembers");
    public static final TTIriRef NOT_MEMBER = iri(NAMESPACE + "notMembers");
    public static final TTIriRef HAS_EXPANSION = iri(NAMESPACE + "hasExpansion");
    public static final TTIriRef IS_CONTAINED_IN = iri(NAMESPACE +"isContainedIn");

    //Document collection predicates
    public static final TTIriRef CONCEPT_SET = iri(NAMESPACE +"concepts");
    public static final TTIriRef INDIVIDUAL_SET = iri(NAMESPACE +"individuals");

    //Transitive  isa predicates
    public static final TTIriRef IS_A= iri(NAMESPACE +"isA");
    public static final TTIriRef IS_CHILD_OF= iri(NAMESPACE +"isChildOf");

    //Inferred grouping predicates
    public static final TTIriRef ROLE_GROUP= iri(NAMESPACE +"roleGroup");
    public static final TTIriRef PROPERTY_GROUP = iri(NAMESPACE +"propertyGroup");
    public static final TTIriRef INHERITED_FROM = iri(NAMESPACE+"inheritedFrom");

    //Concept status values
    public static final TTIriRef DRAFT = iri(NAMESPACE +"Draft");
    public static final TTIriRef ACTIVE = iri(NAMESPACE +"Active");
    public static final TTIriRef INACTIVE = iri(NAMESPACE +"Inactive");
    public static final TTIriRef DISCOVERY_CODE = iri(NAMESPACE +"891071000252105");

    //Legacy Mapping
    public static final TTIriRef MAPPED_FROM = iri(NAMESPACE +"mappedFrom");

    //SPARQL parameters
    public static final TTIriRef QUERY_VARIABLE = iri(NAMESPACE +"queryVariable");
    public static final TTIriRef CALL_QUERY= iri(NAMESPACE +"callQuery");





    //Code schemes
    public static final TTIriRef CODE_SCHEME_SNOMED = iri(NAMESPACE +"891101000252101");
    public static final TTIriRef CODE_SCHEME_READ = iri(NAMESPACE +"891141000252104");
    public static final TTIriRef CODE_SCHEME_CTV3 = iri(NAMESPACE +"891051000252101");
    public static final TTIriRef CODE_SCHEME_ICD10 = iri(NAMESPACE +"891021000252109");
    public static final TTIriRef CODE_SCHEME_OPCS4 = iri(NAMESPACE +"891041000252103");
    public static final TTIriRef CODE_SCHEME_EMIS = iri(NAMESPACE +"891031000252107");
    public static final TTIriRef CODE_SCHEME_TPP = iri(NAMESPACE +"631000252102");
    public static final TTIriRef CODE_SCHEME_BARTS = iri(NAMESPACE +"891081000252108");

    public static final TTIriRef COUNTER = iri(NAMESPACE +"counter");
    public static final TTIriRef HAS_CONTEXT = iri(NAMESPACE +"hasContext");
    public static final TTIriRef GRAPH_ICD10 = iri(NAMESPACE +"ICD10");
    public static final TTIriRef GRAPH_EMIS = TTIriRef.iri(NAMESPACE +"EMIS");
    public static final TTIriRef GRAPH_OPCS4 = TTIriRef.iri(NAMESPACE +"OPCS4");
    public static final TTIriRef GRAPH_READ2 = TTIriRef.iri(NAMESPACE +"READ2");
    public static final TTIriRef GRAPH_CTV3 = TTIriRef.iri(NAMESPACE +"CTV3");
    public static final TTIriRef GRAPH_TPP = TTIriRef.iri(NAMESPACE +"TPP");



}
