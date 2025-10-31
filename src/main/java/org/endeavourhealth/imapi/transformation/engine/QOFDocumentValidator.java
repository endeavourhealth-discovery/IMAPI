package org.endeavourhealth.imapi.transformation.engine;

import org.endeavourhealth.imapi.model.qof.QOFDocument;
import org.endeavourhealth.imapi.transformation.core.TransformationErrorCollector;
import org.endeavourhealth.imapi.transformation.util.QOFModelValidator;
import org.endeavourhealth.imapi.transformation.util.TransformationLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  private final QOFModelValidator modelValidator;
  private final TransformationErrorCollector errorCollector;
  private final TransformationLogger transformationLogger;

  /**
   * Creates a new QOFDocumentValidator.
   *
   * @param modelValidator for detailed model validation
   * @param errorCollector for aggregating validation errors
   * @param transformationLogger for structured logging
   */
  public QOFDocumentValidator(
      QOFModelValidator modelValidator,
      TransformationErrorCollector errorCollector,
      TransformationLogger transformationLogger
  ) {
    this.modelValidator = modelValidator;
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
      errorCollector.addError("document", "QOF document is null", "VALIDATION", "NULL_DOCUMENT");
      transformationLogger.warn("QOF document is null");
      return false;
    }

    boolean isValid = true;

    // Validate document name
    if (document.getName() == null || document.getName().isBlank()) {
      errorCollector.addError(
          "name",
          "QOF document name is required and cannot be empty",
          "VALIDATION",
          "MISSING_NAME"
      );
      isValid = false;
    }

    // Validate selections
    if (document.getSelections() == null || document.getSelections().isEmpty()) {
      errorCollector.addWarning(
          "selections",
          "QOF document has no selections defined",
          "VALIDATION",
          "EMPTY_SELECTIONS"
      );
    } else {
      for (var selection : document.getSelections()) {
        if (!modelValidator.validateSelection(selection, errorCollector)) {
          isValid = false;
        }
      }
    }

    // Validate registers
    if (document.getRegisters() == null || document.getRegisters().isEmpty()) {
      errorCollector.addWarning(
          "registers",
          "QOF document has no registers defined",
          "VALIDATION",
          "EMPTY_REGISTERS"
      );
    } else {
      for (var register : document.getRegisters()) {
        if (!modelValidator.validateRegister(register, errorCollector)) {
          isValid = false;
        }
      }
    }

    // Validate extraction fields
    if (document.getExtractionFields() == null || document.getExtractionFields().isEmpty()) {
      errorCollector.addWarning(
          "extractionFields",
          "QOF document has no extraction fields defined",
          "VALIDATION",
          "EMPTY_EXTRACTION_FIELDS"
      );
    } else {
      for (var field : document.getExtractionFields()) {
        if (!modelValidator.validateExtractionField(field, errorCollector)) {
          isValid = false;
        }
      }
    }

    // Validate indicators
    if (document.getIndicators() == null || document.getIndicators().isEmpty()) {
      errorCollector.addWarning(
          "indicators",
          "QOF document has no indicators defined",
          "VALIDATION",
          "EMPTY_INDICATORS"
      );
    } else {
      for (var indicator : document.getIndicators()) {
        if (!modelValidator.validateIndicator(indicator, errorCollector)) {
          isValid = false;
        }
      }
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
    return new ValidationResult(valid, errorCollector.getErrors(), errorCollector.getWarnings());
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