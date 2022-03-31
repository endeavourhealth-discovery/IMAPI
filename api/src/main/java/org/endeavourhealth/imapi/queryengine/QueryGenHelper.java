package org.endeavourhealth.imapi.queryengine;

import org.endeavourhealth.imapi.model.hql.Comparison;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashMap;
import java.util.Map;

public class QueryGenHelper {

    public static String getEntitySource(String entityIri) {
        switch (entityIri) {
            case "http://endhealth.info/im#Person": return "person";
            case "http://endhealth.info/im#PersonDetails": return "person";
            case "http://endhealth.info/im#GPRegistration": return "registration_status_history";
            case "http://endhealth.info/im#PatientRegistration": return "registration_status_history";
            case "http://endhealth.info/im#Event": return "observation";
            default:
                throw new IllegalArgumentException("Unknown entity type [" + entityIri + "]");
        }
    }

    public static String getPk(String entityIri) {
        return "id";
    }

    public static String getField(Table table, String propertyIri, boolean withPrefix) {
        String prefix = withPrefix ? table.getAlias() + "." : "";
        String entityIri = table.getIri();

        if ("http://endhealth.info/im#GPRegistration".equals(entityIri) || "http://endhealth.info/im#PatientRegistration".equals(entityIri)) {
            switch (propertyIri) {
                case "http://endhealth.info/im#isSubjectOf": return prefix + "person_id";
                case "http://endhealth.info/im#patientType": return prefix + "registration_status_concept_id";
                case "http://endhealth.info/im#effectiveDate": return prefix + "start_date";
                case "http://endhealth.info/im#endDate": return prefix + "end_date";
                default: throw new IllegalArgumentException("Unknown entity property [" + entityIri + "].[" + propertyIri + "]");
            }
        } else if ("http://endhealth.info/im#Person".equals(entityIri)) {
            switch (propertyIri) {
                case "http://endhealth.info/im#age": return "TIMESTAMPDIFF(YEAR, " + prefix + "date_of_birth, $ReferenceDate)";
                default: throw new IllegalArgumentException("Unknown entity property [" + entityIri + "].[" + propertyIri + "]");
            }
        } else if ("http://endhealth.info/im#PersonDetails".equals(entityIri)) {
            switch (propertyIri) {
                case "http://endhealth.info/im#age": return "TIMESTAMPDIFF(YEAR, " + prefix + "date_of_birth, $ReferenceDate)";
                case "http://endhealth.info/im#isSubjectOf": return prefix + "id";
                default: throw new IllegalArgumentException("Unknown entity property [" + entityIri + "].[" + propertyIri + "]");
            }
        } else if ("http://endhealth.info/im#Event".equals(entityIri)) {
            switch (propertyIri) {
                case "http://endhealth.info/im#isSubjectOf": return prefix + "person_id";
                case "http://endhealth.info/im#concept": return prefix + "non_core_concept_id";
                case "http://endhealth.info/im#effectiveDate": return prefix + "clinical_effective_date";
                default: throw new IllegalArgumentException("Unknown entity property [" + entityIri + "].[" + propertyIri + "]");
            }
        }
        throw new IllegalArgumentException("Unknown entity property [" + entityIri + "].[" + propertyIri + "]");
    }

    public static String getField(Table table, String propertyIri) {
        return getField(table, propertyIri, false);
    }
    public static String getTableField(Table table, String propertyIri) {
        return getField(table, propertyIri, true);
    }

    public static String getComparison(Comparison comparison) {
        switch(comparison) {
            case LESS_THAN_OR_EQUAL: return "<=";
            case GREATER_THAN: return ">";
            case GREATER_THAN_OR_EQUAL: return ">=";
            default: throw new IllegalArgumentException("Unknown comparison [" + comparison + "]");
        }
    }

    public static String getValue(TTIriRef i) {
        // Return concept DBID (compass) or Iri (Resolution)
        String iri = i.getIri();
        switch (iri) {
            case "http://endhealth.info/im#2751000252106": return "1335286";
            case "urn:uuid:db167604-ca0a-4922-9fab-f6d6d514c065": return "1335286";
            case "urn:uuid:837c474c-f6af-4a05-83ad-7c4ee7557e11": return "1082811";
            case "urn:uuid:8ab86afb-94e0-45fc-9875-3d16705cf41c": return "1057547";
            default: throw new IllegalArgumentException("Unknown value " + iri);
        }
    }
}
