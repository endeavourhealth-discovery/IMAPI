package org.endeavourhealth.imapi.transformation.core;

import org.endeavourhealth.imapi.model.qof.QOFDocument;
import org.endeavourhealth.imapi.model.imq.Query;

import java.util.*;

/**
 * Context holder for QOF to IMQuery transformation.
 * Maintains state throughout the transformation pipeline.
 */
public class TransformationContext {
  private final QOFDocument qofDocument;
  private final Map<String, Query> generatedQueries = new LinkedHashMap<>();
  private final Map<String, String> fieldToIriMapping = new HashMap<>();
  private final List<String> errors = new ArrayList<>();
  private final List<String> warnings = new ArrayList<>();
  
  public TransformationContext(QOFDocument qofDocument) {
    this.qofDocument = qofDocument;
  }

  public QOFDocument getQofDocument() {
    return qofDocument;
  }

  public Map<String, Query> getGeneratedQueries() {
    return generatedQueries;
  }

  public void addQuery(String name, Query query) {
    generatedQueries.put(name, query);
  }

  public Query getQuery(String name) {
    return generatedQueries.get(name);
  }

  public Map<String, String> getFieldToIriMapping() {
    return fieldToIriMapping;
  }

  public void mapFieldToIri(String fieldName, String iri) {
    fieldToIriMapping.put(fieldName, iri);
  }

  public String getIriForField(String fieldName) {
    return fieldToIriMapping.get(fieldName);
  }

  public List<String> getErrors() {
    return errors;
  }

  public void addError(String error) {
    errors.add(error);
  }

  public List<String> getWarnings() {
    return warnings;
  }

  public void addWarning(String warning) {
    warnings.add(warning);
  }

  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  public boolean hasWarnings() {
    return !warnings.isEmpty();
  }

  public void clear() {
    generatedQueries.clear();
    fieldToIriMapping.clear();
    errors.clear();
    warnings.clear();
  }
}