"use strict";
exports.__esModule = true;
exports.IM = void 0;
var IM = /** @class */ (function () {
    function IM() {
    }
    var _a;
    _a = IM;
    IM.NAMESPACE = "http://endhealth.info/im#";
    IM.PREFIX = "im";
    IM.IRI = "@id";
    IM.CODE = IM.NAMESPACE + "code";
    IM.SCHEME = IM.NAMESPACE + "scheme";
    IM.STATUS = IM.NAMESPACE + "status";
    IM.IS_A = IM.NAMESPACE + "isA";
    IM.ROLE_GROUP = IM.NAMESPACE + "roleGroup";
    IM.IS_CONTAINED_IN = IM.NAMESPACE + "isContainedIn";
    IM.PROPERTY_GROUP = IM.NAMESPACE + "propertyGroup";
    IM.INHERITED_FROM = IM.NAMESPACE + "inheritedFrom";
    // mapping
    IM.HAS_MAP = IM.NAMESPACE + "hasMap";
    IM.ONE_OF = IM.NAMESPACE + "oneOf";
    IM.MAP_ADVICE = IM.NAMESPACE + "mapAdvice";
    IM.MATCHED_TO = IM.NAMESPACE + "matchedTo";
    IM.MAP_PRIORITY = IM.NAMESPACE + "mapPriority";
    IM.ASSURANCE_LEVEL = IM.NAMESPACE + "assuranceLevel";
    IM.COMBINATION_OF = IM.NAMESPACE + "combinationOf";
    IM.SOME_OF = IM.NAMESPACE + "someOf";
    //maps assurance levels
    IM.NATIONALLY_ASSURED_UK = IM.NAMESPACE + "NationallyAssuredUK";
    IM.QUERY_SET = IM.NAMESPACE + "QuerySet";
    IM.QUERY_TEMPLATE = IM.NAMESPACE + "QueryTemplate";
    IM.CONCEPT_SET = IM.NAMESPACE + "ConceptSet";
    IM.VALUE_SET = IM.NAMESPACE + "ValueSet";
    IM.HAS_MEMBERS = IM.NAMESPACE + "hasMembers";
    IM.SET = IM.NAMESPACE + "Set";
    IM.RECORD_TYPE = IM.NAMESPACE + "RecordType";
    IM.FOLDER = IM.NAMESPACE + "Folder";
    IM.DATA_PROPERTY = IM.NAMESPACE + "DataProperty";
    // code schemes
    IM.CODE_SCHEME = IM.NAMESPACE + "CodeScheme";
    IM.CODE_SCHEME_SNOMED = IM.NAMESPACE + "SnomedCodeScheme";
    IM.CODE_SCHEME_READ = IM.NAMESPACE + "Read2CodeScheme";
    IM.CODE_SCHEME_ICD10 = IM.NAMESPACE + "ICD10CodeScheme";
    IM.CODE_SCHEME_OPCS4 = IM.NAMESPACE + "OPSC49CodeScheme";
    IM.CODE_SCHEME_EMIS = IM.NAMESPACE + "EMISCodeScheme";
    IM.CODE_SCHEME_BARTS = IM.NAMESPACE + "BartsCernerCodeScheme";
    IM.CODE_SCHEME_VISION = IM.NAMESPACE + "VisionCodeScheme";
    IM.CODE_SCHEME_CTV3 = IM.NAMESPACE + "CTV3TPPCodeScheme";
    IM.CODE_SCHEME_TERMS = IM.NAMESPACE + "TermOnlyCodeScheme";
    IM.CODE_SCHEME_ODS = IM.NAMESPACE + "ODSCodeScheme";
    IM.DISCOVERY_CODE = IM.NAMESPACE + "DiscoveryCodeScheme";
    IM.STATS_REPORT_ENTRY = IM.NAMESPACE + "hasStatsReportEntry";
    // other
    IM.DEFINITION = IM.NAMESPACE + "definition";
    IM.IN_RESULT_SET = IM.NAMESPACE + "inResultSet";
    IM.EFFECTIVE_DATE = IM.NAMESPACE + "effectiveDate";
    IM.TIME_DIFFERENCE = IM.NAMESPACE + "TimeDifference";
    IM.OBSERVATION = IM.NAMESPACE + "Observation";
    IM.PREFIXED = new /** @class */ (function () {
        function class_1(superThis) {
            var _this = this;
            this.superThis = superThis;
            Object.keys(superThis).forEach(function (key) { return _this[key] = superThis[key].replace(superThis.NAMESPACE, superThis.PREFIX + ":"); });
            return this;
        }
        return class_1;
    }())(_a);
    return IM;
}());
exports.IM = IM;
