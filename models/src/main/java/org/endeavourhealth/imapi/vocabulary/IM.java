package org.endeavourhealth.imapi.vocabulary;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class IM {
    public static final String NAMESPACE = "http://endhealth.info/im#";
    public static final String DOMAIN= "http://endhealth.info/";
    public static final String PREFIX = "im";
    public static final String IRI = "@id";
    public static final String VALUE = "@value";
    public static final String TYPE = "@type";

    //Entity top level predicates
    public static final TTIriRef CODE = iri(NAMESPACE + "code");
    public static final TTIriRef HAS_SCHEME = iri(NAMESPACE + "scheme");
    public static final TTIriRef HAS_STATUS = iri(NAMESPACE + "status");
    public static final TTIriRef STATUS = iri(NAMESPACE + "Status");
    public static final TTIriRef SHORT_NAME = iri(NAMESPACE + "shortName");

    //Entity tope level triples
    public static final TTIriRef HAS_DEFINITION = iri(NAMESPACE + "hasDefinition");


    //Core model types
    public static final TTIriRef RECORD = iri(NAMESPACE + "RecordType");
    public static final TTIriRef CONCEPT = iri(NAMESPACE + "Concept");
    public static final TTIriRef CONCEPT_SET = iri(NAMESPACE + "ConceptSet");
    public static final TTIriRef SET_GROUP = iri(NAMESPACE + "ConceptSetGroup");
    public static final TTIriRef FOLDER = iri(NAMESPACE + "Folder");
    public static final TTIriRef DATASET = iri(NAMESPACE +"DataSet");
    public static final TTIriRef INDIVIDUAL = iri(NAMESPACE +"Individual");
    public static final TTIriRef VALUESET= iri(NAMESPACE+"ValueSet");
    public static final TTIriRef METACLASS= iri(NAMESPACE+"MetaClass");
    public static final TTIriRef METAPROPERTY= iri(NAMESPACE+"MetaProperty");

    //Collection predicates
    public static final TTIriRef DEFINITION= iri(NAMESPACE + "definition");
    public static final TTIriRef MEMBERS = iri(NAMESPACE + "members");
    public static final TTIriRef MEMBER_OF_GROUP = iri(NAMESPACE + "memberOfGroup");
    public static final TTIriRef HAS_SUBSET = iri(NAMESPACE + "hasSubsets");
    public static final TTIriRef HAS_MEMBER_CODE = iri(NAMESPACE + "hasMemberCodes");
    public static final TTIriRef NOT_MEMBER = iri(NAMESPACE + "notMembers");
    public static final TTIriRef HAS_EXPANSION = iri(NAMESPACE + "hasExpansion");
    public static final TTIriRef IS_CONTAINED_IN = iri(NAMESPACE +"isContainedIn");
    public static final TTIriRef ONE_OF = iri(NAMESPACE +"oneOf");
    public static final TTIriRef COMBINATION_OF = iri(NAMESPACE +"combinationOf");
    public static final TTIriRef USE_PREDICATES= iri(NAMESPACE+"usePredicates");
    public static final TTIriRef SOME_OF= iri(NAMESPACE+"someOf");
    public static final TTIriRef HAS_CHILDREN = iri(NAMESPACE + "hasChildren");

    //Document collection predicates
    public static final TTIriRef INDIVIDUAL_SET = iri(NAMESPACE +"individuals");

    //Transitive  isa predicates
    public static final TTIriRef IS_A= iri(NAMESPACE +"isA");
    public static final TTIriRef IS_CHILD_OF= iri(NAMESPACE +"isChildOf");

    //

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
    public static final TTIriRef DEFINITIONAL_STATUS = iri(NAMESPACE+"definitionalStatus");
    public static final TTIriRef SUFFICIENTLY_DEFINED = iri(NAMESPACE+"1251000252106");
    public static final TTIriRef NECESSARY_NOT_SUFFICIENT = iri(NAMESPACE+"2771000252102");

    //Legacy Mapping
    public static final TTIriRef HAS_MAP = iri(NAMESPACE +"hasMap");
    public static final TTIriRef HAS_TERM_CODE = iri(NAMESPACE +"hasTermCode");
    public static final TTIriRef ALTERNATIVE_CODE = iri(NAMESPACE +"alternativeCode");
    public static final TTIriRef DESCRIPTION_ID= iri(NAMESPACE+"descriptionId");
    public static final TTIriRef MATCHED_TO = iri(NAMESPACE +"matchedTo");
    public static final TTIriRef MAPPED_TO = iri(NAMESPACE +"mappedTo");
    public static final TTIriRef SIMILAR = iri(NAMESPACE +"similarTo");
    public static final TTIriRef MAP_PRIORITY = iri(NAMESPACE + "mapPriority");
    public static final TTIriRef ASSURANCE_LEVEL = iri(NAMESPACE + "assuranceLevel");
    public static final TTIriRef MAP_ADVICE = iri(NAMESPACE + "mapAdvice");
    public static final TTIriRef NATIONALLY_ASSURED = iri(NAMESPACE + "NationallyAssuredUK");
    public static final TTIriRef SUPPLIER_ASSURED = iri(NAMESPACE + "SupplierAssured");
    public static final TTIriRef DBID = iri(NAMESPACE + "dbid");



    //SPARQL parameters
    public static final TTIriRef QUERY_VARIABLE = iri(NAMESPACE +"queryVariable");
    public static final TTIriRef CALL_QUERY= iri(NAMESPACE +"callQuery");




    //Namespaces (code schemes and graphs)

    public static final TTIriRef CODE_SCHEME_DISCOVERY= iri(DOMAIN+"im#");
    public static final TTIriRef CODE_SCHEME_SNOMED= iri(DOMAIN+"sct#");
    public static final TTIriRef CODE_SCHEME_REPORTS= iri(DOMAIN + "reports#");
    public static final TTIriRef CODE_SCHEME_ICD10 = iri(DOMAIN +"icd10#");
    public static final TTIriRef CODE_SCHEME_EMIS = TTIriRef.iri(DOMAIN +"emis#");
    public static final TTIriRef CODE_SCHEME_OPCS4 = TTIriRef.iri(DOMAIN +"opcs4#");
    public static final TTIriRef CODE_SCHEME_TPP = TTIriRef.iri(DOMAIN +"tpp#");
    public static final TTIriRef CODE_SCHEME_ODS = TTIriRef.iri(DOMAIN +"ods#");
    public static final TTIriRef CODE_SCHEME_CEG16= TTIriRef.iri(DOMAIN +"ceg16#");
    public static final TTIriRef CODE_SCHEME_VISION= TTIriRef.iri(DOMAIN +"vis#");
    public static final TTIriRef CODE_SCHEME_NHSDD_ETHNIC_2001= TTIriRef.iri(DOMAIN +"nhsethnic2001#");
    public static final TTIriRef CODE_SCHEME_KINGS_APEX = TTIriRef.iri(DOMAIN +"kingsapex#");
    public static final TTIriRef CODE_SCHEME_KINGS_WINPATH = TTIriRef.iri(DOMAIN +"kingswinpath#");
    public static final TTIriRef CODE_SCHEME_BARTS_CERNER = TTIriRef.iri(DOMAIN +"bc#");
    public static final TTIriRef CODE_SCHEME_ENCOUNTER_TYPE = TTIriRef.iri(DOMAIN +"enc#");



    public static final TTIriRef GRAPH_DISCOVERY= iri(IM.NAMESPACE+"DiscoveryGraph");
    public static final TTIriRef GRAPH_SNOMED= iri("http://snomed.info/sct#sct");
    public static final TTIriRef GRAPH_REPORTS= iri(NAMESPACE + "reports");
    public static final TTIriRef GRAPH_ICD10 = iri(NAMESPACE +"icd10");
    public static final TTIriRef GRAPH_EMIS = TTIriRef.iri(NAMESPACE +"emis");
    public static final TTIriRef GRAPH_EMIS_DESCRIPTION = iri(NAMESPACE +"emisdesc");
    public static final TTIriRef GRAPH_OPCS4 = TTIriRef.iri(NAMESPACE +"opcs4");
    public static final TTIriRef GRAPH_TPP = TTIriRef.iri(NAMESPACE +"tpp");
    public static final TTIriRef GRAPH_ODS = TTIriRef.iri(NAMESPACE +"ods");
    public static final TTIriRef MAP_SNOMED_OPCS = TTIriRef.iri(NAMESPACE +"SnomedOPCSMaps");
    public static final TTIriRef MAP_SNOMED_ICD10 = TTIriRef.iri(NAMESPACE +"SnomedICD10Maps");
    public static final TTIriRef MAP_SNOMED_EMIS = TTIriRef.iri(NAMESPACE +"SnomedEMISMaps");
    public static final TTIriRef MAP_SNOMED_TPP = TTIriRef.iri(NAMESPACE +"SnomedTPPMaps");
    public static final TTIriRef MAP_SNOMED_VISION = TTIriRef.iri(NAMESPACE +"SnomedTPPMaps");
    public static final TTIriRef MAP_SNOMED_APEX_KINGS = TTIriRef.iri(NAMESPACE +"SnomedApexKingsMaps");
    public static final TTIriRef MAP_SNOMED_WINPATH_KINGS = TTIriRef.iri(NAMESPACE +"SnomedWinpathKingsMaps");
    public static final TTIriRef MAP_DISCOVERY = TTIriRef.iri(NAMESPACE +"DiscoveryToAllMaps");
    public static final TTIriRef GRAPH_PRSB = TTIriRef.iri(NAMESPACE +"prsb");
    public static final TTIriRef GRAPH_KINGS_APEX = TTIriRef.iri(NAMESPACE +"kingsapex");
    public static final TTIriRef GRAPH_KINGS_WINPATH = TTIriRef.iri(NAMESPACE +"kingswinpath");
    public static final TTIriRef GRAPH_VISION = TTIriRef.iri(NAMESPACE +"vision");
    public static final TTIriRef GRAPH_BARTS_CERNER = TTIriRef.iri(NAMESPACE +"bc");
    public static final TTIriRef MAP_SNOMED_BC = TTIriRef.iri(NAMESPACE +"SnomedBartsCernerMap");
    public static final TTIriRef GRAPH_CEG16= TTIriRef.iri(NAMESPACE +"ceg16");
    public static final TTIriRef GRAPH_NHSDD_ETHNIC_2001= TTIriRef.iri(NAMESPACE +"nhsethnic2001#");
    public static final TTIriRef GRAPH_IM1= TTIriRef.iri(NAMESPACE +"im1Maps");




    //Crud operations

    public static final TTIriRef UPDATE = TTIriRef.iri(NAMESPACE +"Update");
    public static final TTIriRef ADD = TTIriRef.iri(NAMESPACE +"Add");
    public static final TTIriRef REPLACE = TTIriRef.iri(NAMESPACE +"Replace");

    //Predicate functions

    public static final TTIriRef relativeDate = TTIriRef.iri(NAMESPACE +"relativeDate");


    //Miscalleneous
    public static final TTIriRef COUNTER = iri(NAMESPACE +"counter");
    public static final TTIriRef HAS_CONTEXT = iri(NAMESPACE +"hasContext");
    public static final TTIriRef HAS_REPLACED = iri(NAMESPACE+"hasReplaced");
    public static final TTIriRef DISPLAY_ORDER = iri(NAMESPACE+"displayOrder");
    public static final TTIriRef HAS= iri(NAMESPACE+"has");
    private IM() {}
}

