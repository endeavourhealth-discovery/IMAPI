package org.endeavourhealth.imapi.vocabulary;

import com.fasterxml.jackson.annotation.JsonValue;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public enum IM implements Vocabulary {

    DOMAIN("http://endhealth.info/"),
    PREFIX ("im"),
    NAMESPACE(DOMAIN.iri + PREFIX.iri + "#"),
    IRI("@id"),
    VALUE("@value"),
    TYPE("@type"),

    //Entity top level predicates
    id(NAMESPACE.iri + "id"),
    CODE(NAMESPACE.iri + "code"),
    PREFERRED_NAME(NAMESPACE.iri + "preferredName"),
    HAS_SCHEME(NAMESPACE.iri + "scheme"),
    HAS_STATUS(NAMESPACE.iri + "status"),
    CONTENT_TYPE(NAMESPACE.iri + "contentType"),
    USAGE_STATS(NAMESPACE.iri + "usageStats"),
    IN_TASK(NAMESPACE.iri + "inTask"),

    //Entity top level triples
    DEFINITION(NAMESPACE.iri + "definition"),
    RETURN_TYPE(NAMESPACE.iri + "returnType"),
    UPDATE_PROCEDURE(NAMESPACE.iri + "updateProcedure"),

    //Specialised data model predicates
    INVERSE_PATH(NAMESPACE.iri + "inversePath"),
    //Core model types
    CONCEPT(NAMESPACE.iri + "Concept"),
    CONCEPT_SET(NAMESPACE.iri + "ConceptSet"),
    FOLDER(NAMESPACE.iri + "Folder"),
    VALUESET(NAMESPACE.iri + "ValueSet"),
    TEXT_MAPS(NAMESPACE.iri + "TextMaps"),
    CONFIG(NAMESPACE.iri + "Config"),
    GRAPH(NAMESPACE.iri + "Graph"),
    FUNCTION(NAMESPACE.iri + "FunctionClause"),
    QUERY(NAMESPACE.iri + "Query"),
    COHORT_QUERY(NAMESPACE.iri + "CohortQuery"),
    DATASET_QUERY(NAMESPACE.iri + "DatasetQuery"),
    DATA_UPDATE(NAMESPACE.iri + "DataUpdate"),
    PATH_QUERY(NAMESPACE.iri + "PathQuery"),
    PATH_TO(NAMESPACE.iri + "pathTo"),
    OPENSEARCH_QUERY(NAMESPACE.iri + "OpenSearchQuery"),
    DATAMODEL_PROPERTY(NAMESPACE.iri + "dataModelProperty"),
    TASK(IM.NAMESPACE.iri + "Task"),
    FIELD_GROUP(NAMESPACE.iri + "FieldGroup"),
    MATCH_CLAUSE(NAMESPACE.iri + "MatchClause"),
    FORM_GENERATOR(NAMESPACE.iri + "FormGenerator"),
    FUNCTION_PROPERTY(NAMESPACE.iri + "functionProperty"),
    MAP_GRAPH(NAMESPACE.iri + "GraphMap"),
    MAP_ENTITY(NAMESPACE.iri + "EntityMap"),
    SET(NAMESPACE.iri + "Set"),


    //Collection predicates

    IS_CONTAINED_IN(NAMESPACE.iri + "isContainedIn"),
    ONE_OF(NAMESPACE.iri + "oneOf"),
    COMBINATION_OF(NAMESPACE.iri + "combinationOf"),
    USE_PREDICATES(NAMESPACE.iri + "usePredicates"),
    SOME_OF(NAMESPACE.iri + "someOf"),
     HAS_CHILDREN(NAMESPACE.iri + "hasChildren"),


    //Transitive  isa predicates
    IS_A(NAMESPACE.iri + "isA"),
    IS_CHILD_OF(NAMESPACE.iri + "isChildOf"),
    SUBSUMED_BY(NAMESPACE.iri + "subsumedBy"),
    USUALLY_SUBSUMED_BY(NAMESPACE.iri + "usuallySubsumedBy"),
    APPROXIMATE_SUBSUMED_BY(NAMESPACE.iri + "approximateSubsumedBy"),
    MULTIPLE_SUBSUMED_BY(NAMESPACE.iri + "multipleSubsumedBy"),
    LOCAL_SUBCLASS_OF(NAMESPACE.iri + "localSubClassOf"),


    // Config predicate
    HAS_CONFIG(NAMESPACE.iri + "hasConfig"),

    //Inferred grouping predicates
    PROPERTY_GROUP(NAMESPACE.iri + "propertyGroup"),
    INHERITED_FROM(NAMESPACE.iri + "inheritedFrom"),
    GROUP_NUMBER(NAMESPACE.iri + "groupNumber"),
    ROLE_GROUP(NAMESPACE.iri + "roleGroup"),
    ROLE(NAMESPACE.iri + "role"),
    HAS_INHERITED_PROPERTIES(NAMESPACE.iri + "hasInheritedProperties"),

    //Entity status values
    DRAFT(NAMESPACE.iri + "Draft"),
    ACTIVE(NAMESPACE.iri + "Active"),
    INACTIVE(NAMESPACE.iri + "Inactive"),
    DEFINITIONAL_STATUS(NAMESPACE.iri + "definitionalStatus"),
    SUFFICIENTLY_DEFINED(NAMESPACE.iri + "1251000252106"),
    NECESSARY_NOT_SUFFICIENT(NAMESPACE.iri + "2771000252102"),
    UNASSIGNED(NAMESPACE.iri + "Unassigned"),

    //Legacy Mapping
    HAS_MAP(NAMESPACE.iri + "hasMap"),
    ENTITY_MAP(NAMESPACE.iri + "entityMap"),
    MAPPED_TO(NAMESPACE.iri + "mappedTo"),
    HAS_NUMERIC(NAMESPACE.iri + "hasNumericValue"),
    SOURCE_TEXT(NAMESPACE.iri + "sourceText"),
    TARGET_TEXT(NAMESPACE.iri + "targetText"),
    HAS_TERM_CODE(NAMESPACE.iri + "hasTermCode"),
    ALTERNATIVE_CODE(NAMESPACE.iri + "alternativeCode"),
    DESCRIPTION_ID(NAMESPACE.iri + "descriptionId"),
    CODE_ID(NAMESPACE.iri + "codeId"),
    MATCHED_TO(NAMESPACE.iri + "matchedTo"),
    MAP_PRIORITY(NAMESPACE.iri + "mapPriority"),
    ASSURANCE_LEVEL(NAMESPACE.iri + "assuranceLevel"),
    MAP_ADVICE(NAMESPACE.iri + "mapAdvice"),
    NATIONALLY_ASSURED(NAMESPACE.iri + "NationallyAssuredUK"),
    SUPPLIER_ASSURED(NAMESPACE.iri + "SupplierAssured"),

    //Sets
    HAS_MEMBER(NAMESPACE.iri + "hasMember"),
    IS_MEMBER_OF(NAMESPACE.iri + "isMemberOf"),
    IS_SUBSET_OF(NAMESPACE.iri + "isSubsetOf"),
    HAS_SUBSET(NAMESPACE.iri + "hasSubset"),


    //Context
    SOURCE_CONTEXT(NAMESPACE.iri + "sourceContext"),
    SOURCE_CONTEXT_TYPE(NAMESPACE.iri + "SourceContext"),
    SOURCE_PUBLISHER(NAMESPACE.iri + "sourcePublisher"),
    SOURCE_SYSTEM(NAMESPACE.iri + "sourceSystem"),
    SOURCE_SCHEMA(NAMESPACE.iri + "sourceSchema"),
    SOURCE_TABLE(NAMESPACE.iri + "sourceTable"),
    SOURCE_FIELD(NAMESPACE.iri + "sourceField"),
    SOURCE_CODE_SCHEME(NAMESPACE.iri + "sourceCodeScheme"),
    SOURCE_VALUE(NAMESPACE.iri + "sourceValue"),
    SOURCE_REGEX(NAMESPACE.iri + "sourceRegex"),
    SOURCE_HEADING(NAMESPACE.iri + "sourceHeading"),
    TARGET_PROPERTY(NAMESPACE.iri + "targetProperty"),
    CONTEXT_NODE(NAMESPACE.iri + "contextNode"),

    //Crud and provenance operations
    UPDATE_ALL(NAMESPACE.iri + "UpdateAll"),
    ADD_QUADS(NAMESPACE.iri + "AddQuads"),
    UPDATE_PREDICATES(NAMESPACE.iri + "UpdatePredicates"),
    DELETE_ALL(NAMESPACE.iri + "DeleteAll"),
    PROV_CREATION(NAMESPACE.iri + "2001000252109"),
    PROV_UPDATE(NAMESPACE.iri + "1661000252106"),

    USED_IN(NAMESPACE.iri + "usedIn"),


    IN_RESULT_SET(NAMESPACE.iri + "inResultSet"),
    HAS_PROFILE(NAMESPACE.iri + "inResultSet"),
    GMS_PATIENT(NAMESPACE.iri + "2751000252106"),

    //Provenance
    PROV_ACIVITY(NAMESPACE.iri + "ProvenanceActivity"),
    PROV_TARGET(NAMESPACE.iri + "provenanceTarget"),
    PROV_ACIVITY_TYPE(NAMESPACE.iri + "provenanceActivityType"),
    PROV_AGENT(NAMESPACE.iri + "provenanceAgent"),
    START_TIME(NAMESPACE.iri + "startTime"),
    EFFECTIVE_DATE(NAMESPACE.iri + "effectiveDate"),
    END_DATE(NAMESPACE.iri + "endDate"),
    PROV_USED(NAMESPACE.iri + "usedEntity"),


    //Authors and agents
    AUTHOR_ROLE(NAMESPACE.iri + "1001911000252102"),
    VERSION(NAMESPACE.iri + "version"),
    HAS_ROLE_IN(NAMESPACE.iri + "hasRoleInOrganisation"),
    IS_PERSON(NAMESPACE.iri + "isPerson"),

    //Miscalleneous
    HAS_CONTEXT(NAMESPACE.iri + "hasContext"),
    DISPLAY_ORDER(NAMESPACE.iri + "displayOrder"),
    USAGE_TOTAL(NAMESPACE.iri + "usageTotal"),
    PLABEL(NAMESPACE.iri + "pLabel"),
    OLABEL(NAMESPACE.iri + "oLabel"),
    EXAMPLE(NAMESPACE.iri + "example"),


    //im1
    IM1ID(NAMESPACE.iri + "im1Id"),
    WEIGHTING(NAMESPACE.iri + "weighting"),
    PRIVACY_LEVEL(NAMESPACE.iri + "privacyLevel"),
    IM1SCHEME(NAMESPACE.iri + "im1Scheme"),

    //Query
    VALUE_SELECT(NAMESPACE.iri + "valueSelect"),
    VALUE_VARIABLE(NAMESPACE.iri + "valueVariable"),
    PLACEHOLDER(NAMESPACE.iri + "placeHolder"),
    FUNCTION_DEFINITION(NAMESPACE.iri + "function"),

    // Location
    ADDRESS_CLASS(NAMESPACE.iri + "Address"),
    ADDRESS(NAMESPACE.iri + "address"),
    ADDRESS_LINE_1(NAMESPACE.iri + "addressLine1"),
    ADDRESS_LINE_2(NAMESPACE.iri + "addressLine2"),
    ADDRESS_LINE_3(NAMESPACE.iri + "addressLine3"),
    LOCALITY(NAMESPACE.iri + "locality"),
    REGION(NAMESPACE.iri + "region"),
    POST_CODE(NAMESPACE.iri + "postCode"),
    COUNTRY(NAMESPACE.iri + "country"),
    UPRN(NAMESPACE.iri + "uprn"),

    SYSTEM_NAMESPACE("http://sys.endhealth.info/im#"),

    //Editor
    ENTITY_TYPES(NAMESPACE.iri + "EntityTypes"),
    //Vue import start
    ID(NAMESPACE.iri + "id"),
    CONCEPT_SET_GROUP(NAMESPACE.iri + "ConceptSetGroup"),
    OLD_CODE(NAMESPACE.iri + "oldCode"),
    ORDER(NAMESPACE.iri + "order"),
    MAPPING_TASK(NAMESPACE.iri + "MappingTask"),
    UPDATE_TASK(NAMESPACE.iri + "UpdateTask"),
    MATCHED_FROM(NAMESPACE.iri + "matchedFrom"),
    MAPPED_FROM(NAMESPACE.iri + "mappedFrom"),
    SELECT(NAMESPACE.iri + "select"),
    NATIONALLY_ASSURED_UK(NAMESPACE.iri + "NationallyAssuredUK"),

    ENTITY(NAMESPACE.iri + "Entity"),
    QUERY_SET(NAMESPACE.iri + "QuerySet"),
    QUERY_TEMPLATE(NAMESPACE.iri + "QueryTemplate"),
    RECORD_TYPE(NAMESPACE.iri + "RecordType"),
    FEATURE(NAMESPACE.iri + "MatchClause"),
    DATA_PROPERTY(NAMESPACE.iri + "DataProperty"),
    ORGANISATION(NAMESPACE.iri + "Organisation"),
    MODELLING_ENTITY_TYPE(NAMESPACE.iri + "ModellingEntityType"),
    PROFILE(NAMESPACE.iri + "Profile"),
    STATS_REPORT_ENTRY(NAMESPACE.iri + "hasStatsReportEntry"),
    FAVOURITES(NAMESPACE.iri + "Favourites"),

    MODULE_ONTOLOGY(NAMESPACE.iri + "DiscoveryOntology"),
    MODULE_SETS(NAMESPACE.iri + "Sets"),
    MODULE_DATA_MODEL(NAMESPACE.iri + "DiscoveryCommonDataModel"),
    MODULE_CATALOGUE(NAMESPACE.iri + "Catalogue"),
    MODULE_QUERIES(NAMESPACE.iri + "Q_Queries"),
    MODULE_IM(NAMESPACE.iri + "InformationModel"),
    MODULE_TASKS(NAMESPACE.iri + "Tasks"),
    MODULE_FEATURES(NAMESPACE.iri + "M_MatchClauses"),

    // Stats reports
    CONCEPT_CATEGORY(NAMESPACE.iri + "ontologyOverview"),
    CONCEPT_TYPES(NAMESPACE.iri + "ontologyConceptTypes"),
    CONCEPT_SCHEMES(NAMESPACE.iri + "ontologyConceptSchemes"),
    CONCEPT_STATUS(NAMESPACE.iri + "ontologyConceptStatus"),
    HAS_VALUE(NAMESPACE.iri + "hasValue"),
    VALUE_SET(NAMESPACE.iri + "ValueSet"),
    DATAMODEL_ENTITY(NAMESPACE.iri + "DataModelEntity"),
    DATAMODEL_OBJECTPROPERTY(NAMESPACE.iri + "dataModelObjectProperty"),
    DATAMODEL_DATAPROPERTY(NAMESPACE.iri + "dataModelDataProperty"),
    DATAMODEL_FUNCTIONPROPERTY(NAMESPACE.iri + "functionProperty"),
    MATCH(NAMESPACE.iri + "match"),
    ENTITY_TYPE(NAMESPACE.iri + "entityType"),
    //argument options
    VALUE_DATA(NAMESPACE.iri + "valueData"),
    VALUE_OBJECT(NAMESPACE.iri + "valueObject"),
    VALUE_IRI(NAMESPACE.iri + "valueIri"),
    VALUE_IRI_LIST(NAMESPACE.iri + "valueIriList"),
    VALUE_DATA_LIST(NAMESPACE.iri + "valueDataList"),

    // IM1
    IM_1_ID(NAMESPACE.iri + "im1Id"),
    IM_1_SCHEME(NAMESPACE.iri + "im1Scheme"),
    //Provenance
    PROV_ACTIVITY_TYPE(NAMESPACE.iri + "provenanceActivityType"),
    // Main folders
    FOLDER_VALUESETS(NAMESPACE.iri + "ValueSets"),
    FOLDER_SETS(NAMESPACE.iri + "Sets"),
    FOLDER_QUERY_CONCEPT_SETS(NAMESPACE.iri + "QueryConceptSets");


    public final String iri;
    IM(String url) {
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
            IM.valueOf(iri);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

