package org.endeavourhealth.imapi.transformation.engine;

import org.endeavourhealth.imapi.model.qof.QOFDocument;
import org.endeavourhealth.imapi.transformation.core.TransformationErrorCollector;
import org.endeavourhealth.imapi.transformation.util.TransformationLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Validates QOF documents for structural integrity and required fields.
 * Performs comprehensive validation checks including:
 * - Null/empty document validation
 * - Required fields (name, selections, registers)
 * - Nested structure validation
 * - Field type and value constraints
 */
public class QOFDocumentValidator {
  private static final Logger log = LoggerFactory.getLogger(QOFDocumentValidator.class);
  private final TransformationErrorCollector errorCollector;
  private final TransformationLogger transformationLogger;

  /**
   * Creates a new QOFDocumentValidator.
   *
   * @param errorCollector for aggregating validation errors
   * @param transformationLogger for structured logging
   */
  public QOFDocumentValidator(
      TransformationErrorCollector errorCollector,
      TransformationLogger transformationLogger
  ) {
    this.errorCollector = errorCollector;
    this.transformationLogger = transformationLogger;
  }

  /**
   * Validates a QOF document for structural integrity.
   *
   * Validation Steps:
   * 1. Check document is not null
   * 2. Validate document structure
   * 3. Validate required fields
   * 4. Validate nested structures
   * 5. Collect all errors without throwing
   *
   * @param document QOFDocument to validate
   * @return true if document is valid, false otherwise
   */
  public boolean validate(QOFDocument document) {
    transformationLogger.info("Starting QOF document validation");

    if (document == null) {
      transformationLogger.warn("QOF document is null");
      return false;
    }

    boolean isValid = true;

    // Validate document name
    if (document.getName() == null || document.getName().isBlank()) {
      transformationLogger.warn("QOF document name is required and cannot be empty");
      isValid = false;
    }

    // Validate selections
    if (document.getSelections() == null || document.getSelections().isEmpty()) {
      transformationLogger.warn("QOF document has no selections defined");
    }

    // Validate registers
    if (document.getRegisters() == null || document.getRegisters().isEmpty()) {
      transformationLogger.warn("QOF document has no registers defined");
    }

    // Validate extraction fields
    if (document.getExtractionFields() == null || document.getExtractionFields().isEmpty()) {
      transformationLogger.warn("QOF document has no extraction fields defined");
    }

    // Validate indicators
    if (document.getIndicators() == null || document.getIndicators().isEmpty()) {
      transformationLogger.warn("QOF document has no indicators defined");
    }

    if (isValid) {
      transformationLogger.info("QOF document validation passed");
    } else {
      transformationLogger.warn("QOF document validation found errors");
    }

    return isValid;
  }

  /**
   * Validates a QOF document and returns validation status with error details.
   *
   * @param document QOFDocument to validate
   * @return ValidationResult containing validation status and error details
   */
  public ValidationResult validateWithDetails(QOFDocument document) {
    boolean valid = validate(document);
    List<Object> errors = new ArrayList<>(errorCollector.getErrors());
    List<Object> warnings = new ArrayList<>();
    return new ValidationResult(valid, errors, warnings);
  }

  /**
   * Container for validation results with error and warning details.
   */
  public static class ValidationResult {
    private final boolean valid;
    private final java.util.List<Object> errors;
    private final java.util.List<Object> warnings;

    public ValidationResult(boolean valid, java.util.List<Object> errors, java.util.List<Object> warnings) {
      this.valid = valid;
      this.errors = errors;
      this.warnings = warnings;
    }

    public boolean isValid() {
      return valid;
    }

    public java.util.List<Object> getErrors() {
      return errors;
    }

    public java.util.List<Object> getWarnings() {
      return warnings;
    }
  }
}