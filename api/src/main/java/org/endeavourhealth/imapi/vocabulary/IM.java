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
    //public static final TTIriRef STATUS = iri(NAMESPACE + "Status");
    public static final TTIriRef SHORT_NAME = iri(NAMESPACE + "shortName");
    public static final TTIriRef USAGE_STATS = iri(NAMESPACE + "usageStats");
    //public static final TTIriRef USAGE_TOTAL = iri(NAMESPACE + "usageTotal");

    //Entity top level triples
    public static final TTIriRef HAS_DEFINITION = iri(NAMESPACE + "hasDefinition");


    //Core model types
    public static final TTIriRef CONCEPT = iri(NAMESPACE + "Concept");
    public static final TTIriRef CONCEPT_SET = iri(NAMESPACE + "ConceptSet");
    public static final TTIriRef FOLDER = iri(NAMESPACE + "Folder");
    public static final TTIriRef VALUESET= iri(NAMESPACE+"ValueSet");
    public static final TTIriRef TEXT_MAPS= iri(NAMESPACE+"TextMaps");
    public static final TTIriRef PROFILE= iri(NAMESPACE+"Profile");
    public static final TTIriRef CONFIG = iri(NAMESPACE + "Config");
    public static final TTIriRef GRAPH = iri(NAMESPACE + "Graph");
    public static final TTIriRef MATCH_CLAUSE = iri(NAMESPACE + "MatchClause");
    public static final TTIriRef COMPARE_CLAUSE = iri(NAMESPACE + "CompareClause");
    public static final TTIriRef ARGUMENT_CLAUSE = iri(NAMESPACE + "ArgumentClause");
    public static final TTIriRef RANGE_CLAUSE = iri(NAMESPACE + "RangeClause");
    public static final TTIriRef FUNCTION_CLAUSE = iri(NAMESPACE + "FunctionClause");


    //Collection predicates
    public static final TTIriRef DEFINITION= iri(NAMESPACE + "definition");
    public static final TTIriRef IS_CONTAINED_IN = iri(NAMESPACE +"isContainedIn");
    public static final TTIriRef ONE_OF = iri(NAMESPACE +"oneOf");
    public static final TTIriRef COMBINATION_OF = iri(NAMESPACE +"combinationOf");
    public static final TTIriRef USE_PREDICATES= iri(NAMESPACE+"usePredicates");
    public static final TTIriRef SOME_OF= iri(NAMESPACE+"someOf");
    public static final TTIriRef HAS_CHILDREN = iri(NAMESPACE + "hasChildren");



    //Transitive  isa predicates
    public static final TTIriRef IS_A= iri(NAMESPACE +"isA");
    public static final TTIriRef IS_CHILD_OF= iri(NAMESPACE +"isChildOf");


    // Config predicate
  public static final TTIriRef HAS_CONFIG = iri(NAMESPACE + "hasConfig");

    //Inferred grouping predicates
    public static final TTIriRef PROPERTY_GROUP = iri(NAMESPACE +"propertyGroup");
    public static final TTIriRef INHERITED_FROM = iri(NAMESPACE+"inheritedFrom");
    public static final TTIriRef GROUP_NUMBER = iri(NAMESPACE +"groupNumber");
    public static final TTIriRef ROLE_GROUP = iri(NAMESPACE +"roleGroup");
    public static final TTIriRef ROLE = iri(NAMESPACE +"role");
  public static final TTIriRef HAS_INHERITED_PROPERTIES = iri(NAMESPACE +"hasInheritedProperties");

    //Entity status values
    public static final TTIriRef DRAFT = iri(NAMESPACE +"Draft");
    public static final TTIriRef ACTIVE = iri(NAMESPACE +"Active");
    public static final TTIriRef INACTIVE = iri(NAMESPACE +"Inactive");
    public static final TTIriRef DEFINITIONAL_STATUS = iri(NAMESPACE+"1261000252108");
    public static final TTIriRef SUFFICIENTLY_DEFINED = iri(NAMESPACE+"1251000252106");
    public static final TTIriRef NECESSARY_NOT_SUFFICIENT = iri(NAMESPACE+"2771000252102");

    //Legacy Mapping
    public static final TTIriRef HAS_MAP = iri(NAMESPACE +"hasMap");
    public static final TTIriRef MAPPED_TO = iri(NAMESPACE +"mappedTo");
    public static final TTIriRef SOURCE_TEXT = iri(NAMESPACE + "sourceText");
    public static final TTIriRef TARGET_TEXT = iri(NAMESPACE + "targetText");
    public static final TTIriRef HAS_TERM_CODE = iri(NAMESPACE +"hasTermCode");
    public static final TTIriRef ALTERNATIVE_CODE = iri(NAMESPACE +"alternativeCode");
    public static final TTIriRef DESCRIPTION_ID= iri(NAMESPACE+"descriptionId");
    public static final TTIriRef CODE_ID= iri(NAMESPACE+"codeId");
    public static final TTIriRef MATCHED_TO = iri(NAMESPACE +"matchedTo");
    public static final TTIriRef SIMILAR = iri(NAMESPACE +"similarTo");
    public static final TTIriRef MAP_PRIORITY = iri(NAMESPACE + "mapPriority");
    public static final TTIriRef ASSURANCE_LEVEL = iri(NAMESPACE + "assuranceLevel");
    public static final TTIriRef MAP_ADVICE = iri(NAMESPACE + "mapAdvice");
    public static final TTIriRef NATIONALLY_ASSURED = iri(NAMESPACE + "NationallyAssuredUK");
    public static final TTIriRef SUPPLIER_ASSURED = iri(NAMESPACE + "SupplierAssured");



    //Graphs
    public static final TTIriRef GRAPH_DISCOVERY= iri(IM.DOMAIN+"im#");
    public static final TTIriRef GRAPH_ICD10 = iri(DOMAIN +"icd10#");
    public static final TTIriRef GRAPH_EMIS = TTIriRef.iri(DOMAIN +"emis#");
    public static final TTIriRef GRAPH_OPCS4 = TTIriRef.iri(DOMAIN +"opcs4#");
    public static final TTIriRef GRAPH_TPP = TTIriRef.iri(DOMAIN +"tpp#");
    public static final TTIriRef GRAPH_ODS = TTIriRef.iri(DOMAIN +"ods#");
    public static final TTIriRef GRAPH_PRSB = TTIriRef.iri(DOMAIN +"prsb#");
    public static final TTIriRef GRAPH_KINGS_APEX = TTIriRef.iri(DOMAIN +"kpax#");
    public static final TTIriRef GRAPH_KINGS_WINPATH = TTIriRef.iri(DOMAIN +"kwp#");
    public static final TTIriRef GRAPH_VISION = TTIriRef.iri(DOMAIN +"vis#");
    public static final TTIriRef GRAPH_BARTS_CERNER = TTIriRef.iri(DOMAIN +"bc#");
    public static final TTIriRef GRAPH_NHSDD_ETHNIC_2001= TTIriRef.iri(DOMAIN +"nhsethnic2001#");
    public static final TTIriRef GRAPH_IM1= TTIriRef.iri(DOMAIN +"im1Maps#");
    public static final TTIriRef GRAPH_ENCOUNTERS= TTIriRef.iri(DOMAIN +"enc#");
    public static final TTIriRef GRAPH_CONFIG= TTIriRef.iri(DOMAIN +"config#");
    public static final TTIriRef GRAPH_CEG_QUERY= TTIriRef.iri(DOMAIN +"ceg/qry#");
    public static final TTIriRef GRAPH_NHS_TFC= TTIriRef.iri(DOMAIN +"nhstfc#");


    // redant code schemes are now the same as graphs

    public static final TTIriRef CODE_SCHEME_DISCOVERY= IM.GRAPH_DISCOVERY;
    public static final TTIriRef CODE_SCHEME_ICD10 = IM.GRAPH_ICD10;
    public static final TTIriRef CODE_SCHEME_EMIS = IM.GRAPH_EMIS;
    public static final TTIriRef CODE_SCHEME_OPCS4 = IM.GRAPH_OPCS4;
    public static final TTIriRef CODE_SCHEME_TPP = IM.GRAPH_TPP;
    public static final TTIriRef CODE_SCHEME_ODS = IM.GRAPH_ODS;
    public static final TTIriRef CODE_SCHEME_VISION= IM.GRAPH_VISION;
    public static final TTIriRef CODE_SCHEME_NHSDD_ETHNIC_2001= IM.GRAPH_NHSDD_ETHNIC_2001;
    public static final TTIriRef CODE_SCHEME_KINGS_APEX = IM.GRAPH_KINGS_APEX;
    public static final TTIriRef CODE_SCHEME_KINGS_WINPATH = IM.GRAPH_KINGS_WINPATH;
    public static final TTIriRef CODE_SCHEME_BARTS_CERNER = IM.GRAPH_BARTS_CERNER;
    public static final TTIriRef CODE_SCHEME_ENCOUNTERS = IM.GRAPH_ENCOUNTERS;


    //Crud and provenance operations

    public static final TTIriRef UPDATE = TTIriRef.iri(NAMESPACE +"Update");
    public static final TTIriRef ADD = TTIriRef.iri(NAMESPACE +"Add");
    public static final TTIriRef REPLACE = TTIriRef.iri(NAMESPACE +"Replace");
    public static final TTIriRef CREATION = TTIriRef.iri(NAMESPACE +"2001000252109");



    //Query
  public static final TTIriRef ORDER_LIMIT= iri(NAMESPACE + "OrderLimit");
  public static final TTIriRef AND= iri(NAMESPACE + "and");
  public static final TTIriRef OR= iri(NAMESPACE + "or");
  public static final TTIriRef NOT= iri(NAMESPACE + "not");
  public static final TTIriRef MATCH = iri(NAMESPACE + "match");
  public static final TTIriRef ORDER = iri(NAMESPACE + "order");
  public static final TTIriRef PATH_TO = iri(NAMESPACE + "pathTo");
  public static final TTIriRef ENTITY_TYPE = iri(NAMESPACE + "entityType");
  public static final TTIriRef PROPERTY = iri(NAMESPACE + "property");
  public static final TTIriRef VALUE_COMPARE = iri(NAMESPACE + "valueCompare");
  public static final TTIriRef NOT_EXIST = iri(NAMESPACE+ "notExist");
  public static final TTIriRef VALUE_IN = iri(NAMESPACE + "valueIn");
  public static final TTIriRef VALUE_NOTIN = iri(NAMESPACE + "valueNotIn");
  public static final TTIriRef FROM = iri(NAMESPACE + "from");
  public static final TTIriRef TO = iri(NAMESPACE + "to");
  public static final TTIriRef VALUE_RANGE  = iri(NAMESPACE+ "valueRange");
  public static final TTIriRef VALUE_FUNCTION = iri(NAMESPACE + "valueFunction");
  public static final TTIriRef COMPARISON = iri(NAMESPACE + "comparison");
  public static final TTIriRef FUNCTION_IRI = iri(NAMESPACE + "functionIri");
  public static final TTIriRef ARGUMENT = iri(NAMESPACE + "argument");
  public static final TTIriRef PARAMETER = iri(NAMESPACE + "parameter");
  public static final TTIriRef LIMIT = iri(NAMESPACE + "limit");
  public static final TTIriRef SORT_FIELD = iri(NAMESPACE + "sortField");
  public static final TTIriRef TEST = iri(NAMESPACE + "test");
  public static final TTIriRef VALUE_VAR = iri(NAMESPACE + "valueVar");
  public static final TTIriRef FUNCTION = iri(NAMESPACE + "function");
  public static final TTIriRef ASCENDING = iri(NAMESPACE +"ASCENDING");
  public static final TTIriRef DESCENDING = iri(NAMESPACE +"DESCENDING");
  public static final TTIriRef SORT_ORDER = iri(NAMESPACE +"sortOrder");
  public static final TTIriRef VALUE_MATCH = iri(NAMESPACE +"valueMatch");
  public static final TTIriRef VALUE_IRI = iri(NAMESPACE +"valueIrI");
  public static final TTIriRef VALUE_DATA = iri(NAMESPACE +"valueData");



  public static final TTIriRef HAS_PROFILE = iri(NAMESPACE+"hasProfile");
  public static final TTIriRef GMS_PATIENT = iri(NAMESPACE + "2751000252106");

  //Provenance
  public static final TTIriRef PROV_ACIVITY = iri(NAMESPACE+"ProvenanceActivity");
  public static final TTIriRef PROV_TARGET = iri(NAMESPACE+"provenanceTarget");
  public static final TTIriRef PROV_ACIVITY_TYPE= iri(NAMESPACE+"provenanceActivityType");
  public static final TTIriRef PROV_AGENT = iri(NAMESPACE+"provenanceAgent");
  public static final TTIriRef START_TIME = iri(NAMESPACE+"startTime");
  public static final TTIriRef EFFECTIVE_DATE = iri(NAMESPACE+"effectiveDate");
  public static final TTIriRef PROV_USED = iri(NAMESPACE+"usedEntity");



  //Authors and agents
  public static final TTIriRef AUTHOR_ROLE = iri(NAMESPACE+"1001911000252102");
  public static final TTIriRef VERSION = iri(NAMESPACE +"version");
  public static final TTIriRef HAS_ROLE_IN = iri(NAMESPACE +"hasRoleInOrganisation");
  public static final TTIriRef IS_PERSON = iri(NAMESPACE +"isPerson");

    //Miscalleneous
    public static final TTIriRef COUNTER = iri(NAMESPACE +"counter");
    public static final TTIriRef HAS_CONTEXT = iri(NAMESPACE +"hasContext");
    public static final TTIriRef HAS_REPLACED = iri(NAMESPACE+"hasReplaced");
    public static final TTIriRef DISPLAY_ORDER = iri(NAMESPACE+"displayOrder");
  public static final TTIriRef USAGE_TOTAL = iri(NAMESPACE+"usageTotal");
  public static final TTIriRef PLABEL= iri(NAMESPACE+"pLabel");
  public static final TTIriRef OLABEL= iri(NAMESPACE+"oLabel");
    private IM() {}
}
