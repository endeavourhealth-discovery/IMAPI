package org.endeavourhealth.imapi.transformation.util;

import org.endeavourhealth.imapi.model.qof.QOFDocument;
import org.endeavourhealth.imapi.transformation.core.TransformationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Validates QOF model objects for transformation readiness.
 * Checks for required fields, valid structure, and data integrity.
 */
public class QOFModelValidator {

  private static final Logger log = LoggerFactory.getLogger(QOFModelValidator.class);

  /**
   * Validates a QOFDocument for transformation.
   *
   * @param document The document to validate
   * @return List of validation errors (empty if valid)
   */
  public static List<TransformationError> validateQOFDocument(QOFDocument document) {
    List<TransformationError> errors = new ArrayList<>();

    if (document == null) {
      errors.add(new TransformationError.Builder()
          .errorId(UUID.randomUUID().toString())
          .message("QOFDocument is null")
          .phase("Validation")
          .field("QOFDocument")
          .reason("Document cannot be null for transformation")
          .build()
      );
      return errors;
    }

    // Check required fields
    if (document.getName() == null || document.getName().trim().isEmpty()) {
      errors.add(new TransformationError.Builder()
          .errorId(UUID.randomUUID().toString())
          .message("QOFDocument name is missing or empty")
          .phase("Validation")
          .field("name")
          .reason("Document name is required for transformation")
          .build()
      );
    }

    // Check structure
    if (document.getSelections() == null) {
      document.setSelections(new ArrayList<>());
      log.debug("QOFDocument selections list was null, initialized to empty");
    }

    if (document.getRegisters() == null) {
      document.setRegisters(new ArrayList<>());
      log.debug("QOFDocument registers list was null, initialized to empty");
    }

    if (document.getExtractionFields() == null) {
      document.setExtractionFields(new ArrayList<>());
      log.debug("QOFDocument extractionFields list was null, initialized to empty");
    }

    if (document.getIndicators() == null) {
      document.setIndicators(new ArrayList<>());
      log.debug("QOFDocument indicators list was null, initialized to empty");
    }

    if (errors.isEmpty()) {
      log.debug("QOFDocument validation passed: {}", document.getName());
    } else {
      log.warn("QOFDocument validation found {} error(s)", errors.size());
    }

    return errors;
  }

  /**
   * Checks if a QOFDocument is valid for transformation.
   *
   * @param document The document to check
   * @return true if valid
   */
  public static boolean isValid(QOFDocument document) {
    return validateQOFDocument(document).isEmpty();
  }

  /**
   * Generates validation error summary.
   *
   * @param errors List of validation errors
   * @return Formatted error summary
   */
  public static String getErrorSummary(List<TransformationError> errors) {
    StringBuilder sb = new StringBuilder();
    sb.append("Validation Errors (").append(errors.size()).append("):\n");
    for (TransformationError error : errors) {
      sb.append("  - ").append(error.getMessage()).append(" [Field: ")
          .append(error.getField()).append("]\n");
    }
    return sb.toString();
  }
}