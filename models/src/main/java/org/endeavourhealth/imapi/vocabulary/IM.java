package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class IM {
    public static final String NAMESPACE = "http://endhealth.info/im#";
    public static final String PREFIX = "im";

    //Entity top level predicates
    public static final TTIriRef CODE = iri(NAMESPACE + "code");
    public static final TTIriRef HAS_SCHEME = iri(NAMESPACE + "scheme");
    public static final TTIriRef STATUS = iri(NAMESPACE + "status");
    public static final TTIriRef ALTERNATIVE_CODE = iri(NAMESPACE + "alternativeCode");
    public static final TTIriRef SHORT_NAME = iri(NAMESPACE + "shortName");

    //Entity tope level triples
    public static final TTIriRef HAS_DEFINITION = iri(NAMESPACE + "hasDefinition");



    //Core model types
    public static final TTIriRef RECORD = iri(NAMESPACE + "RecordType");
    public static final TTIriRef CONCEPT_SET = iri(NAMESPACE + "EntitySet");
    public static final TTIriRef FOLDER = iri(NAMESPACE + "Folder");
    public static final TTIriRef QUERY_TEMPLATE = iri(NAMESPACE +"QueryTemplate");
    public static final TTIriRef LEGACY = iri(NAMESPACE +"LegacyEntity");
    public static final TTIriRef INDIVIDUAL = iri(NAMESPACE +"Individual");
    public static final TTIriRef GRAPH= iri(NAMESPACE+"Graph");
    public static final TTIriRef VALUESET= iri(NAMESPACE+"ValueSet");
    public static final TTIriRef DATA_MODEL = iri(NAMESPACE + "DataModel");

    //Collection predicates
    public static final TTIriRef HAS_MEMBER = iri(NAMESPACE + "hasMembers");
    public static final TTIriRef HAS_SUBSETS = iri(NAMESPACE + "hasSubsets");
    public static final TTIriRef HAS_MEMBER_CODE = iri(NAMESPACE + "hasMemberCodes");
    public static final TTIriRef NOT_MEMBER = iri(NAMESPACE + "notMembers");
    public static final TTIriRef HAS_EXPANSION = iri(NAMESPACE + "hasExpansion");
    public static final TTIriRef IS_CONTAINED_IN = iri(NAMESPACE +"isContainedIn");
    public static final TTIriRef ONE_OF = iri(NAMESPACE +"oneOf");
    public static final TTIriRef COMBINATION_OF = iri(NAMESPACE +"combinationOf");
    public static final TTIriRef ANY_OF = iri(NAMESPACE +"anyOf");

    //Document collection predicates
    public static final TTIriRef INDIVIDUAL_SET = iri(NAMESPACE +"individuals");

    //Transitive  isa predicates
    public static final TTIriRef IS_A= iri(NAMESPACE +"isA");
    public static final TTIriRef IS_CHILD_OF= iri(NAMESPACE +"isChildOf");

    //Inferred grouping predicates
    public static final TTIriRef PROPERTY_GROUP = iri(NAMESPACE +"propertyGroup");
    public static final TTIriRef INHERITED_FROM = iri(NAMESPACE+"inheritedFrom");
    public static final TTIriRef GROUP_NUMBER = iri(NAMESPACE +"groupNumber");
    public static final TTIriRef ROLE_GROUP = iri(NAMESPACE +"roleGroup");
    public static final TTIriRef ROLE = iri(NAMESPACE +"role");

    //Entity status values
    public static final TTIriRef DRAFT = iri(NAMESPACE +"Draft");
    public static final TTIriRef ACTIVE = iri(NAMESPACE +"Active");
    public static final TTIriRef INACTIVE = iri(NAMESPACE +"Inactive");


    //Legacy Mapping
    public static final TTIriRef HAS_MAP = iri(NAMESPACE +"hasMap");
    public static final TTIriRef HAS_TERM_CODE = iri(NAMESPACE +"hasTermCode");
    public static final TTIriRef MATCHED_TO = iri(NAMESPACE +"matchedTo");
    public static final TTIriRef MATCHED_TERM_CODE = iri(NAMESPACE +"matchedTermCode");
    public static final TTIriRef SIMILAR = iri(NAMESPACE +"similarTo");
    public static final TTIriRef MATCHED_AS_SUBCLASS = iri(NAMESPACE +"matchedAsSubclassOf");
    public static final TTIriRef SYNONYM = iri(NAMESPACE + "synonym");
    public static final TTIriRef MAP_PRIORITY = iri(NAMESPACE + "mapPriority");
    public static final TTIriRef ASSURANCE_LEVEL = iri(NAMESPACE + "assuranceLevel");
    public static final TTIriRef MAP_ADVICE = iri(NAMESPACE + "mapAdvice");
    public static final TTIriRef NATIONALLY_ASSURED = iri(NAMESPACE + "NationallyAssuredUK");
    public static final TTIriRef SUPPLIER_ASSURED = iri(NAMESPACE + "SupplierAssured");


    //SPARQL parameters
    public static final TTIriRef QUERY_VARIABLE = iri(NAMESPACE +"queryVariable");
    public static final TTIriRef CALL_QUERY= iri(NAMESPACE +"callQuery");



    //Code schemes
    public static final TTIriRef CODE_SCHEME_SNOMED = iri(NAMESPACE +"SnomedCodeScheme");
    public static final TTIriRef CODE_SCHEME_READ = iri(NAMESPACE +"Read2CodeScheme");
    public static final TTIriRef CODE_SCHEME_ICD10 = iri(NAMESPACE +"ICD10CodeScheme");
    public static final TTIriRef CODE_SCHEME_OPCS4 = iri(NAMESPACE +"OPSC49CodeScheme");
    public static final TTIriRef CODE_SCHEME_EMIS = iri(NAMESPACE +"EMISCodeScheme");
    public static final TTIriRef CODE_SCHEME_BARTS = iri(NAMESPACE +"BartsCernerCodeScheme");
    public static final TTIriRef CODE_SCHEME_VISION = iri(NAMESPACE +"VisionCodeScheme");
    public static final TTIriRef CODE_SCHEME_CTV3 = iri(NAMESPACE +"CTV3TPPCodeScheme");
    public static final TTIriRef CODE_SCHEME_ENCOUNTER_TERMS = iri(NAMESPACE+"EncounterTermsCodeScheme");
    public static final TTIriRef CODE_SCHEME_ODS= iri(NAMESPACE+"ODSCodeScheme");
    public static final TTIriRef DISCOVERY_CODE = iri(NAMESPACE +"DiscoveryCodeScheme");
    public static final TTIriRef CODE_SCHEME_EMIS_CODEID = iri(NAMESPACE +"EMISCodeIdScheme");
    public static final TTIriRef CODE_SCHEME_PRSB = iri(NAMESPACE +"PRSBCodeScheme");
    public static final TTIriRef CODE_SCHEME_APEX_KINGS = iri(NAMESPACE +"KingsApexPathologyCodeScheme");
    public static final TTIriRef CODE_SCHEME_WINPATH_KINGS = iri(NAMESPACE +"KingsWinPathCodeScheme");


    public static final TTIriRef COUNTER = iri(NAMESPACE +"counter");
    public static final TTIriRef HAS_CONTEXT = iri(NAMESPACE +"hasContext");
    public static final TTIriRef GRAPH_DISCOVERY= iri("http://endhealth.info/im");
    public static final TTIriRef GRAPH_REPORTS= iri(NAMESPACE + "Reports");
    public static final TTIriRef GRAPH_ICD10 = iri(NAMESPACE +"ICD10");
    public static final TTIriRef GRAPH_EMIS = TTIriRef.iri(NAMESPACE +"EMIS");
    public static final TTIriRef GRAPH_OPCS4 = TTIriRef.iri(NAMESPACE +"OPCS4");
    public static final TTIriRef GRAPH_READ2 = TTIriRef.iri(NAMESPACE +"READ2");
    public static final TTIriRef GRAPH_CTV3 = TTIriRef.iri(NAMESPACE +"CTV3");
    public static final TTIriRef GRAPH_TPP = TTIriRef.iri(NAMESPACE +"TPP");
    public static final TTIriRef GRAPH_MAP_SNOMED_OPCS = TTIriRef.iri(NAMESPACE +"SnomedOPCSMaps");
    public static final TTIriRef GRAPH_MAP_SNOMED_ICD10 = TTIriRef.iri(NAMESPACE +"SnomedICD10Maps");
    public static final TTIriRef GRAPH_SNOMED = TTIriRef.iri(NAMESPACE +"SNOMED");
    public static final TTIriRef GRAPH_MAPS_DISCOVERY = TTIriRef.iri(NAMESPACE +"DiscoveryMaps");
    public static final TTIriRef GRAPH_VALUESETS = TTIriRef.iri(NAMESPACE +"VALUESETS");
    public static final TTIriRef GRAPH_PRSB = TTIriRef.iri(NAMESPACE +"PRSB");
    public static final TTIriRef GRAPH_APEX_KINGS = TTIriRef.iri(NAMESPACE +"ApexKings");
    public static final TTIriRef GRAPH_WINPATH_KINGS = TTIriRef.iri(NAMESPACE +"WinPathKings");




    //Crud operations

    public static final TTIriRef UPDATE = TTIriRef.iri(NAMESPACE +"Update");
    public static final TTIriRef ADD = TTIriRef.iri(NAMESPACE +"Add");
    public static final TTIriRef REPLACE = TTIriRef.iri(NAMESPACE +"Replace");
}

