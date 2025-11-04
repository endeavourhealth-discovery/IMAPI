package org.endeavourhealth.imapi.transformation.util;

/**
 * Utility methods for QOF to IMQuery transformation.
 */
public class TransformationUtil {

  /**
   * Convert string to IRI-safe slug format.
   * Example: "Patient Age" -> "patient-age"
   */
  public static String toSlugFormat(String input) {
    if (input == null || input.isEmpty()) {
      return "";
    }
    return input.toLowerCase()
      .replaceAll("\\s+", "-")
      .replaceAll("[^a-z0-9-]", "")
      .replaceAll("-+", "-")
      .replaceAll("^-|-$", "");
  }

  /**
   * Generate an IRI from a name using QOF namespace.
   * Example: "DM_REG" -> "http://endhealth.info/qof#dm-reg"
   */
  public static String generateQOFIri(String name) {
    return "http://endhealth.info/qof#" + toSlugFormat(name);
  }

  /**
   * Generate an IRI from a name using IM namespace.
   * Example: "Patient" -> "http://endhealth.info/im#Patient"
   */
  public static String generateIMIri(String name) {
    return "http://endhealth.info/im#" + name;
  }

  /**
   * Extract field names from a logic expression.
   * Example: "If REG_DAT ≠ Null" -> ["REG_DAT"]
   */
  public static java.util.Set<String> extractFieldNames(String logic) {
    java.util.Set<String> fields = new java.util.HashSet<>();
    if (logic == null) return fields;

    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("([A-Z_]+)");
    java.util.regex.Matcher matcher = pattern.matcher(logic);

    while (matcher.find()) {
      String potential = matcher.group(1);
      if (!isKeyword(potential)) {
        fields.add(potential);
      }
    }

    return fields;
  }

  private static boolean isKeyword(String word) {
    return java.util.Set.of("AND", "OR", "NOT", "IF", "NULL", "TRUE", "FALSE").contains(word);
  }

  /**
   * Check if a value represents null.
   */
  public static boolean isNullValue(String value) {
    return value == null || 
           "Null".equalsIgnoreCase(value) || 
           "null".equalsIgnoreCase(value) ||
           "NULL".equalsIgnoreCase(value);
  }

  /**
   * Check if a value represents a field reference.
   */
  public static boolean isFieldReference(String value) {
    return value != null && value.matches("[A-Z_]+");
  }

  /**
   * Check if a value is a numeric literal.
   */
  public static boolean isNumeric(String value) {
    if (value == null) return false;
    try {
      Double.parseDouble(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Sanitize file name for safe file system usage.
   */
  public static String sanitizeFileName(String name) {
    return name.replaceAll("[^a-zA-Z0-9._-]", "_");
  }
}