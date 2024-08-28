package org.endeavourhealth.imapi.transformengine;

import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TransformFunctions {

  private TransformFunctions() {
    throw new IllegalStateException("Utility class");
  }

  public static Object runFunction(String iri, Map<String, Object> args) {
    return switch (iri) {
      case IM.NAMESPACE + "Concatenate" -> concatenate(args);
      case IM.NAMESPACE + "StringJoin" -> stringJoin(args);
      case IM.NAMESPACE + "SchemedCodeConceptMap" -> schemeCodeConcept();
      default -> throw new IllegalArgumentException("FunctionClause not supported : " + iri);
    };
  }

  private static Object schemeCodeConcept() {
    return null;
  }


  private static Object stringJoin(Map<String, Object> args) {
    try {
      String delimiter = String.valueOf(args.get("delimiter"));
      Object value = args.get("elements");
      if (value instanceof List<?> valueList) {
        List<String> elements = (valueList.stream().map(String::valueOf).toList());
        return String.join(delimiter, elements);
      } else
        return String.valueOf(value);
    } catch (Exception e) {
      throw new IllegalStateException("String join function must have 'delimiter' as a string and 'elements' as a list");
    }
  }

  private static Object concatenate(Map<String, Object> args) {
    StringBuilder result = new StringBuilder();
    for (Map.Entry<String, Object> entry : args.entrySet()) {
      Object value = entry.getValue();
      if (value instanceof Collection<?> valueCollection) {
        valueCollection.forEach(v -> result.append(value));
      } else
        result.append(value);
    }
    return result.toString();
  }
}
