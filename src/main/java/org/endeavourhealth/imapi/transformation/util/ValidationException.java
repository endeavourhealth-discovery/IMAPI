package org.endeavourhealth.imapi.transformation.util;

/**
 * Exception thrown when validation of QOF or IMQ structures fails.
 */
public class ValidationException extends Exception {

  private final String validationPhase;
  private final String invalidField;

  /**
   * Creates a validation exception.
   *
   * @param message Error message
   */
  public ValidationException(String message) {
    super(message);
    this.validationPhase = null;
    this.invalidField = null;
  }

  /**
   * Creates a validation exception with context.
   *
   * @param message Error message
   * @param validationPhase The validation phase
   * @param invalidField The field that failed validation
   */
  public ValidationException(String message, String validationPhase, String invalidField) {
    super(message);
    this.validationPhase = validationPhase;
    this.invalidField = invalidField;
  }

  /**
   * Creates a validation exception with cause.
   *
   * @param message Error message
   * @param cause The underlying exception
   */
  public ValidationException(String message, Throwable cause) {
    super(message, cause);
    this.validationPhase = null;
    this.invalidField = null;
  }

  public String getValidationPhase() {
    return validationPhase;
  }

  public String getInvalidField() {
    return invalidField;
  }
}