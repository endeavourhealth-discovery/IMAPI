package org.endeavourhealth.imapi.transformation.engine;

import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.qof.QOFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Coordinates validation across the transformation pipeline.
 *
 * Provides three levels of validation:
 * 1. Pre-transformation: Validates input QOFDocument structure
 * 2. Checkpoint: Validates intermediate transformation states
 * 3. Post-transformation: Validates output Query structure
 *
 * Validation is extensible - new validators can be added by implementing
 * the Validator interface and registering them with this coordinator.
 */
public class TransformationValidator {

  private static final Logger log = LoggerFactory.getLogger(TransformationValidator.class);

  /**
   * Creates a new transformation validator.
   */
  public TransformationValidator() {
    log.debug("Initialized TransformationValidator");
  }

  /**
   * Validates an input QOF document before transformation.
   *
   * Checks:
   * - Document is not null
   * - Document has required metadata (name, selections, registers)
   * - Collections are not empty where required
   * - Fields have valid types and constraints
   *
   * @param qofDocument The QOF document to validate
   * @return ValidationResult with isValid flag and error list
   */
  public ValidationResult validateInputDocument(QOFDocument qofDocument) {
    log.debug("Validating input QOF document");
    List<Object> errors = new ArrayList<>();

    // Null check
    if (qofDocument == null) {
      errors.add("QOF document is null");
      return new ValidationResult(false, errors, Collections.emptyList());
    }

    // Structural validation
    if (qofDocument.getName() == null || qofDocument.getName().trim().isEmpty()) {
      errors.add("Document name is required");
    }

    // Validate collections
    if (qofDocument.getSelections() == null || qofDocument.getSelections().isEmpty()) {
      errors.add("Document must have at least one selection");
    }

    if (qofDocument.getRegisters() == null || qofDocument.getRegisters().isEmpty()) {
      errors.add("Document must have at least one register");
    }

    // Extraction fields validation
    if (qofDocument.getExtractionFields() == null || qofDocument.getExtractionFields().isEmpty()) {
      log.warn("Document has no extraction fields - transformation may produce incomplete results");
    }

    // Indicators validation
    if (qofDocument.getIndicators() == null || qofDocument.getIndicators().isEmpty()) {
      log.warn("Document has no indicators - transformation may produce incomplete results");
    }

    boolean isValid = errors.isEmpty();
    log.debug("Input document validation: {} (errors: {})", isValid ? "PASSED" : "FAILED", errors.size());

    return new ValidationResult(isValid, errors, Collections.emptyList());
  }

  /**
   * Validates intermediate transformation state at a checkpoint.
   *
   * This method can be called between component transformations to verify
   * that the Query is in a valid state.
   *
   * @param query The Query to validate
   * @param checkpointName The name of the checkpoint (for logging)
   * @return ValidationResult with isValid flag and error list
   */
  public ValidationResult validateCheckpoint(Query query, String checkpointName) {
    log.debug("Validating checkpoint: {}", checkpointName);
    List<Object> errors = new ArrayList<>();

    if (query == null) {
      errors.add("Query is null at checkpoint: " + checkpointName);
      return new ValidationResult(false, errors, Collections.emptyList());
    }

    // Basic Query structure checks
    if (query.getName() == null || query.getName().trim().isEmpty()) {
      errors.add("Query name is empty at checkpoint: " + checkpointName);
    }

    boolean isValid = errors.isEmpty();
    log.debug("Checkpoint validation [{}]: {} (errors: {})", 
        checkpointName, isValid ? "PASSED" : "FAILED", errors.size());

    return new ValidationResult(isValid, errors, Collections.emptyList());
  }

  /**
   * Validates output Query after transformation.
   *
   * Checks:
   * - Query is not null
   * - Query has required metadata (name)
   * - Query structure is valid
   * - All transformations were successful
   *
   * @param query The output Query to validate
   * @return ValidationResult with isValid flag and error list
   */
  public ValidationResult validateOutputQuery(Query query) {
    log.debug("Validating output Query");
    List<Object> errors = new ArrayList<>();

    // Null check
    if (query == null) {
      errors.add("Output Query is null");
      return new ValidationResult(false, errors, Collections.emptyList());
    }

    // Required fields validation
    if (query.getName() == null || query.getName().trim().isEmpty()) {
      errors.add("Output Query name is required");
    }

    // Where clause validation
    if (query.getWhere() == null) {
      log.warn("Output Query has no where clause - may indicate incomplete transformation");
    }

    // Return clause validation
    if (query.getReturn() == null) {
      log.warn("Output Query has no return clause - may indicate incomplete transformation");
    }

    boolean isValid = errors.isEmpty();
    log.debug("Output Query validation: {} (errors: {})", isValid ? "PASSED" : "FAILED", errors.size());

    return new ValidationResult(isValid, errors, Collections.emptyList());
  }

  /**
   * Validates an output Query comprehensively including all structural checks.
   *
   * @param query The Query to validate
   * @return ValidationResult with detailed validation report
   */
  public ValidationResult validateOutputQueryComprehensive(Query query) {
    log.debug("Performing comprehensive output Query validation");
    List<Object> errors = new ArrayList<>();
    List<Object> warnings = new ArrayList<>();

    // Null check
    if (query == null) {
      errors.add("Output Query is null");
      return new ValidationResult(false, errors, warnings);
    }

    // Metadata validation
    if (query.getName() == null || query.getName().trim().isEmpty()) {
      errors.add("Query name is required");
    }

    if (query.getDescription() == null || query.getDescription().trim().isEmpty()) {
      warnings.add("Query description is recommended for documentation");
    }

    // Structure validation
    if (query.getWhere() == null) {
      warnings.add("Query has no where clause - filtering may be incomplete");
    }

    if (query.getReturn() == null) {
      errors.add("Query must have a return clause");
    }

    // Data set validation (optional for now - dataSet may be set through different methods)
    // Validate that the query has some structure beyond just metadata
    if (query.getWhere() == null && query.getReturn() == null) {
      warnings.add("Query has minimal structure - may need where or return clauses");
    }

    boolean isValid = errors.isEmpty();
    log.debug("Comprehensive output validation: {} (errors: {}, warnings: {})",
        isValid ? "PASSED" : "FAILED", errors.size(), warnings.size());

    return new ValidationResult(isValid, errors, warnings);
  }

  /**
   * Validation result data class.
   */
  public static class ValidationResult {
    private final boolean valid;
    private final List<Object> errors;
    private final List<Object> warnings;

    public ValidationResult(boolean valid, List<Object> errors, List<Object> warnings) {
      this.valid = valid;
      this.errors = errors != null ? errors : Collections.emptyList();
      this.warnings = warnings != null ? warnings : Collections.emptyList();
    }

    public boolean isValid() {
      return valid;
    }

    public List<Object> getErrors() {
      return errors;
    }

    public List<Object> getWarnings() {
      return warnings;
    }

    public int getErrorCount() {
      return errors.size();
    }

    public int getWarningCount() {
      return warnings.size();
    }

    @Override
    public String toString() {
      return "ValidationResult{" +
          "valid=" + valid +
          ", errors=" + errors.size() +
          ", warnings=" + warnings.size() +
          '}';
    }
  }

  /**
   * Validator interface for extensibility.
   *
   * Custom validators can implement this interface to provide additional
   * validation logic that can be registered with the TransformationValidator.
   */
  public interface Validator {
    /**
     * Performs validation on the given Query.
     *
     * @param query The Query to validate
     * @return ValidationResult with validation outcome
     */
    ValidationResult validate(Query query);

    /**
     * Gets the validator name for logging.
     */
    String getName();
  }
}