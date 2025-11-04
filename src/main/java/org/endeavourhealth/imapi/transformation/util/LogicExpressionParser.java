package org.endeavourhealth.imapi.transformation.util;

import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Where;
import org.endeavourhealth.imapi.model.imq.Operator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses QOF logic expressions and converts them to IMQuery WHERE conditions.
 * Supports:
 * - Field comparisons (equality, inequality, date comparisons)
 * - Logical operators (AND, OR, NOT)
 * - Null checks
 * - Temporal references (ACHV_DAT, relative dates)
 */
public class LogicExpressionParser {
  
  private static final Pattern COMPARISON_PATTERN = Pattern.compile(
    "(?:If\\s+)?([A-Z_]+)\\s*([≠!=<>]+|=)\\s*(.+?)(?:\\s+(?:AND|OR)|$)"
  );
  
  private static final Pattern NULL_CHECK_PATTERN = Pattern.compile(
    "(?:If\\s+)?([A-Z_]+)\\s*(≠|!=|=)\\s*Null"
  );

  /**
   * Parse a QOF logic expression and convert to IMQuery Match structures.
   */
  public Match parseLogic(String logic, FieldMappingDictionary fieldMapping) {
    if (logic == null || logic.trim().isEmpty()) {
      return null;
    }

    // Clean up the logic expression
    String cleanedLogic = cleanLogic(logic);

    // Parse for AND conditions (highest precedence after parentheses)
    if (cleanedLogic.contains("AND")) {
      return parseAndConditions(cleanedLogic, fieldMapping);
    }

    // Parse for OR conditions
    if (cleanedLogic.contains("OR")) {
      return parseOrConditions(cleanedLogic, fieldMapping);
    }

    // Parse single condition
    return parseSingleCondition(cleanedLogic, fieldMapping);
  }

  private Match parseAndConditions(String logic, FieldMappingDictionary fieldMapping) {
    String[] conditions = logic.split("(?i)\\s+AND\\s+");
    Match rootMatch = new Match();
    List<Match> andMatches = new ArrayList<>();

    for (String condition : conditions) {
      Match match = parseSingleCondition(condition.trim(), fieldMapping);
      if (match != null) {
        andMatches.add(match);
      }
    }

    if (!andMatches.isEmpty()) {
      rootMatch.setAnd(andMatches);
    }
    return rootMatch;
  }

  private Match parseOrConditions(String logic, FieldMappingDictionary fieldMapping) {
    String[] conditions = logic.split("(?i)\\s+OR\\s+");
    Match rootMatch = new Match();
    List<Match> orMatches = new ArrayList<>();

    for (String condition : conditions) {
      Match match = parseSingleCondition(condition.trim(), fieldMapping);
      if (match != null) {
        orMatches.add(match);
      }
    }

    if (!orMatches.isEmpty()) {
      rootMatch.setOr(orMatches);
    }
    return rootMatch;
  }

  private Match parseSingleCondition(String condition, FieldMappingDictionary fieldMapping) {
    condition = condition.trim();
    if (condition.isEmpty()) {
      return null;
    }

    // Remove surrounding parentheses if present
    if (condition.startsWith("(") && condition.endsWith(")")) {
      condition = condition.substring(1, condition.length() - 1).trim();
    }

    // Check for null conditions
    Matcher nullMatcher = NULL_CHECK_PATTERN.matcher(condition);
    if (nullMatcher.find()) {
      return parseNullCondition(nullMatcher, fieldMapping);
    }

    // Check for comparison conditions
    Matcher comparisonMatcher = COMPARISON_PATTERN.matcher(condition);
    if (comparisonMatcher.find()) {
      return parseComparisonCondition(comparisonMatcher, fieldMapping);
    }

    return null;
  }

  private Match parseNullCondition(Matcher matcher, FieldMappingDictionary fieldMapping) {
    String fieldName = matcher.group(1);
    String operator = matcher.group(2);
    
    String iri = fieldMapping.getIri(fieldName);
    
    Match match = new Match();
    Where where = new Where();
    where.setIri(iri);
    
    // Map null operators
    if ("≠".equals(operator) || "!=".equals(operator)) {
      where.setIsNotNull(true);
    } else if ("=".equals(operator)) {
      where.setIsNull(true);
    }
    
    match.setWhere(where);
    return match;
  }

  private Match parseComparisonCondition(Matcher matcher, FieldMappingDictionary fieldMapping) {
    String fieldName = matcher.group(1);
    String operator = matcher.group(2);
    String value = matcher.group(3).trim();

    String iri = fieldMapping.getIri(fieldName);

    Match match = new Match();
    Where where = new Where();
    where.setIri(iri);

    // Map operators
    switch (operator) {
      case "=":
        where.setOperator(Operator.eq);
        break;
      case "≠", "!=":
        where.setOperator(Operator.eq);
        where.setNot(true);
        break;
      case "<":
        where.setOperator(Operator.lt);
        break;
      case ">":
        where.setOperator(Operator.gt);
        break;
      case "<=":
        where.setOperator(Operator.lte);
        break;
      case ">=":
        where.setOperator(Operator.gte);
        break;
      default:
        where.setOperator(Operator.eq);
    }

    // Process value - could be another field reference or literal
    value = processValue(value, fieldMapping);
    
    if (value != null && !value.isEmpty()) {
      where.setValue(value);
    }

    match.setWhere(where);
    return match;
  }

  private String processValue(String value, FieldMappingDictionary fieldMapping) {
    if (value == null || value.isEmpty()) {
      return value;
    }
    
    // Remove surrounding parentheses
    if (value.startsWith("(") && value.endsWith(")")) {
      value = value.substring(1, value.length() - 1).trim();
    }
    
    // Extract field reference from expressions like "PPED – 12 months"
    Pattern fieldRefPattern = Pattern.compile("([A-Z_]+)");
    Matcher fieldMatcher = fieldRefPattern.matcher(value);
    if (fieldMatcher.find()) {
      String firstFieldRef = fieldMatcher.group(1);
      // Check if this is a real field (not a keyword)
      if (!isKeyword(firstFieldRef)) {
        String fieldIri = fieldMapping.getIri(firstFieldRef);
        if (fieldIri != null) {
          // If there are additional modifiers (like "– 12 months"), append them
          if (value.length() > firstFieldRef.length()) {
            return fieldIri + " " + value.substring(firstFieldRef.length()).trim();
          }
          return fieldIri;
        }
      }
    }
    
    // Handle simple field references
    if (value.matches("[A-Z_]+")) {
      String valueIri = fieldMapping.getIri(value);
      return valueIri;
    }
    
    // Handle numeric values
    if (value.matches("\\d+")) {
      return String.valueOf(Integer.parseInt(value));
    }
    
    // Handle age comparisons like "17 years"
    if (value.matches("\\d+\\s+years?")) {
      String[] parts = value.split("\\s+");
      return String.valueOf(Integer.parseInt(parts[0]));
    }
    
    // Return as-is for other string values
    return value;
  }

  private String cleanLogic(String logic) {
    if (logic == null) return "";
    
    // Remove leading "If" prefix if present
    String cleaned = logic.replaceAll("^\\s*If\\s+", "");
    
    // Add spaces around AND/OR keywords if missing
    // This handles patterns like "140ANDIf" -> "140 AND If"
    cleaned = cleaned.replaceAll("(?i)([^\\s])AND(?=[A-Z])", "$1 AND ");
    cleaned = cleaned.replaceAll("(?i)([^\\s])OR(?=[A-Z])", "$1 OR ");
    
    // Also handle "ANDIf" and "ORIf" patterns specifically
    cleaned = cleaned.replaceAll("(?i)AND\\s+If\\s+", " AND If ");
    cleaned = cleaned.replaceAll("(?i)OR\\s+If\\s+", " OR If ");
    
    // Normalize multiple spaces
    cleaned = cleaned.replaceAll("\\s+", " ");
    
    return cleaned.trim();
  }

  /**
   * Extract all field references from a logic expression.
   */
  public Set<String> extractFieldReferences(String logic) {
    Set<String> fields = new HashSet<>();
    if (logic == null) return fields;

    Pattern fieldPattern = Pattern.compile("([A-Z_]+)");
    Matcher matcher = fieldPattern.matcher(logic);

    while (matcher.find()) {
      String potential = matcher.group(1);
      // Filter out SQL/logic keywords
      if (!isKeyword(potential)) {
        fields.add(potential);
      }
    }

    return fields;
  }

  private boolean isKeyword(String word) {
    Set<String> keywords = Set.of("AND", "OR", "NOT", "IF", "NULL", "TRUE", "FALSE");
    return keywords.contains(word);
  }
}