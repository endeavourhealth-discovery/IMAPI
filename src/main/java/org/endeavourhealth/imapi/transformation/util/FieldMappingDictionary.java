package org.endeavourhealth.imapi.transformation.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps QOF field names to semantic IRIs for IMQuery generation.
 * Maintains a comprehensive mapping of QOF fields to their semantic representations.
 */
public class FieldMappingDictionary {
  private static final Map<String, String> STANDARD_MAPPINGS = new HashMap<>();

  static {
    // Base QOF field mappings to semantic IRIs
    STANDARD_MAPPINGS.put("PAT_ID", "http://endhealth.info/im#Patient.identifier");
    STANDARD_MAPPINGS.put("PAT_AGE", "http://endhealth.info/im#Patient.age");
    STANDARD_MAPPINGS.put("REG_DAT", "http://endhealth.info/im#Patient.registrationDate");
    STANDARD_MAPPINGS.put("DEREG_DAT", "http://endhealth.info/im#Patient.deregistrationDate");
    STANDARD_MAPPINGS.put("ACHV_DAT", "http://endhealth.info/im#Achievement.date");
    
    // Diabetes-specific mappings
    STANDARD_MAPPINGS.put("DM_DAT", "http://endhealth.info/im#DiabetesDiagnosis.date");
    STANDARD_MAPPINGS.put("DMLAT_DAT", "http://endhealth.info/im#DiabetesDiagnosis.latestDate");
    STANDARD_MAPPINGS.put("DMRES_DAT", "http://endhealth.info/im#DiabetesResolved.date");
    STANDARD_MAPPINGS.put("DMMAX_DAT", "http://endhealth.info/im#DiabetesMaxTreatment.date");
    
    // HbA1c and monitoring
    STANDARD_MAPPINGS.put("IFCCHBA_DAT", "http://endhealth.info/im#HbA1c.date");
    STANDARD_MAPPINGS.put("IFCCHBA_VAL", "http://endhealth.info/im#HbA1c.value");
    STANDARD_MAPPINGS.put("SERFRUC_DAT", "http://endhealth.info/im#SerumFructosamine.date");
    
    // Blood pressure monitoring
    STANDARD_MAPPINGS.put("CLINBP_DAT", "http://endhealth.info/im#BloodPressure.date");
    STANDARD_MAPPINGS.put("CLINBPSYS_VAL", "http://endhealth.info/im#BloodPressure.systolic");
    STANDARD_MAPPINGS.put("CLINBPDIA_VAL", "http://endhealth.info/im#BloodPressure.diastolic");
    STANDARD_MAPPINGS.put("CLINBPLAT_DAT", "http://endhealth.info/im#BloodPressure.latestDate");
    STANDARD_MAPPINGS.put("CLINBPSYSLAT_VAL", "http://endhealth.info/im#BloodPressure.latestSystolic");
    STANDARD_MAPPINGS.put("CLINBPDIALAT_VAL", "http://endhealth.info/im#BloodPressure.latestDiastolic");
  }

  private final Map<String, String> customMappings = new HashMap<>();

  /**
   * Get IRI for a QOF field name.
   * Checks custom mappings first, then falls back to standard mappings.
   * If not found, generates a default IRI based on field name.
   */
  public String getIri(String fieldName) {
    if (customMappings.containsKey(fieldName)) {
      return customMappings.get(fieldName);
    }
    if (STANDARD_MAPPINGS.containsKey(fieldName)) {
      return STANDARD_MAPPINGS.get(fieldName);
    }
    // Generate default IRI from field name
    return "http://endhealth.info/qof#" + toSlugFormat(fieldName);
  }

  /**
   * Register a custom field mapping.
   */
  public void registerMapping(String fieldName, String iri) {
    customMappings.put(fieldName, iri);
  }

  /**
   * Get all standard mappings.
   */
  public static Map<String, String> getStandardMappings() {
    return new HashMap<>(STANDARD_MAPPINGS);
  }

  /**
   * Get all custom mappings.
   */
  public Map<String, String> getCustomMappings() {
    return new HashMap<>(customMappings);
  }

  /**
   * Convert field name to slug format for IRI generation.
   * Example: "DM_DAT" -> "dm-dat"
   */
  private String toSlugFormat(String input) {
    return input.toLowerCase().replace("_", "-");
  }

  /**
   * Clear all custom mappings.
   */
  public void clearCustomMappings() {
    customMappings.clear();
  }
}