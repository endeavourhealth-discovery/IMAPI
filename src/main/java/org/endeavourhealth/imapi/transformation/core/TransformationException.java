package org.endeavourhealth.imapi.transformation.core;

/**
 * Exception thrown when a transformation operation fails.
 * Provides detailed context about what went wrong during transformation.
 */
public class TransformationException extends Exception {

  private final String transformationPhase;
  private final String correlationId;
  private final String context;

  /**
   * Creates a transformation exception with basic information.
   *
   * @param message Error message
   */
  public TransformationException(String message) {
    super(message);
    this.transformationPhase = null;
    this.correlationId = null;
    this.context = null;
  }

  /**
   * Creates a transformation exception with full context.
   *
   * @param message Error message
   * @param transformationPhase The phase during which the error occurred
   * @param correlationId Correlation ID for tracking
   * @param context Additional context information
   */
  public TransformationException(String message, String transformationPhase,
      String correlationId, String context) {
    super(message);
    this.transformationPhase = transformationPhase;
    this.correlationId = correlationId;
    this.context = context;
  }

  /**
   * Creates a transformation exception with a cause.
   *
   * @param message Error message
   * @param cause The underlying exception
   */
  public TransformationException(String message, Throwable cause) {
    super(message, cause);
    this.transformationPhase = null;
    this.correlationId = null;
    this.context = null;
  }

  /**
   * Creates a transformation exception with full context and cause.
   *
   * @param message Error message
   * @param transformationPhase The phase during which the error occurred
   * @param correlationId Correlation ID for tracking
   * @param context Additional context information
   * @param cause The underlying exception
   */
  public TransformationException(String message, String transformationPhase,
      String correlationId, String context, Throwable cause) {
    super(message, cause);
    this.transformationPhase = transformationPhase;
    this.correlationId = correlationId;
    this.context = context;
  }

  public String getTransformationPhase() {
    return transformationPhase;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public String getContext() {
    return context;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    if (transformationPhase != null) {
      sb.append(" [Phase: ").append(transformationPhase).append("]");
    }
    if (correlationId != null) {
      sb.append(" [ID: ").append(correlationId).append("]");
    }
    if (context != null) {
      sb.append(" [Context: ").append(context).append("]");
    }
    return sb.toString();
  }
}