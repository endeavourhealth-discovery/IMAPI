package org.endeavourhealth.imapi.transformation.core;

import java.time.Instant;

/**
 * Represents a single error that occurred during transformation.
 * Contains detailed information about what went wrong and where.
 */
public class TransformationError {

  private final String errorId;
  private final String message;
  private final String phase;
  private final String field;
  private final String reason;
  private final Throwable cause;
  private final Instant timestamp;
  private final String correlationId;

  private TransformationError(Builder builder) {
    this.errorId = builder.errorId;
    this.message = builder.message;
    this.phase = builder.phase;
    this.field = builder.field;
    this.reason = builder.reason;
    this.cause = builder.cause;
    this.timestamp = builder.timestamp;
    this.correlationId = builder.correlationId;
  }

  // Getters
  public String getErrorId() {
    return errorId;
  }

  public String getMessage() {
    return message;
  }

  public String getPhase() {
    return phase;
  }

  public String getField() {
    return field;
  }

  public String getReason() {
    return reason;
  }

  public Throwable getCause() {
    return cause;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  @Override
  public String toString() {
    return String.format(
        "TransformationError{id=%s, message=%s, phase=%s, field=%s, reason=%s}",
        errorId, message, phase, field, reason
    );
  }

  /**
   * Builder for creating TransformationError instances.
   */
  public static class Builder {
    private String errorId;
    private String message;
    private String phase;
    private String field;
    private String reason;
    private Throwable cause;
    private Instant timestamp = Instant.now();
    private String correlationId;

    public Builder errorId(String errorId) {
      this.errorId = errorId;
      return this;
    }

    public Builder message(String message) {
      this.message = message;
      return this;
    }

    public Builder phase(String phase) {
      this.phase = phase;
      return this;
    }

    public Builder field(String field) {
      this.field = field;
      return this;
    }

    public Builder reason(String reason) {
      this.reason = reason;
      return this;
    }

    public Builder cause(Throwable cause) {
      this.cause = cause;
      return this;
    }

    public Builder timestamp(Instant timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Builder correlationId(String correlationId) {
      this.correlationId = correlationId;
      return this;
    }

    public TransformationError build() {
      return new TransformationError(this);
    }
  }
}