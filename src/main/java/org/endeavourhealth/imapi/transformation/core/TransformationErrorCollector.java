package org.endeavourhealth.imapi.transformation.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Collects and manages transformation errors that occur during processing.
 * Supports both critical errors (that stop transformation) and warnings (that allow continuation).
 */
public class TransformationErrorCollector {

  private static final Logger log = LoggerFactory.getLogger(TransformationErrorCollector.class);

  private final List<TransformationError> errors = new CopyOnWriteArrayList<>();
  private final int maxErrors;
  private boolean stopOnError = false;

  /**
   * Creates a new error collector with default configuration.
   */
  public TransformationErrorCollector() {
    this(1000); // Default max 1000 errors before stopping collection
  }

  /**
   * Creates a new error collector with max error limit.
   *
   * @param maxErrors Maximum errors to collect before stopping
   */
  public TransformationErrorCollector(int maxErrors) {
    this.maxErrors = maxErrors;
  }

  /**
   * Adds an error to the collection.
   *
   * @param error The error to add
   */
  public void addError(TransformationError error) {
    if (errors.size() < maxErrors) {
      errors.add(error);
      log.warn("Transformation error collected: {}", error);
    } else {
      log.error("Max error collection limit ({}) reached", maxErrors);
    }
  }

  /**
   * Adds an error with builder convenience.
   *
   * @param builder Builder to create the error
   */
  public void addError(TransformationError.Builder builder) {
    addError(builder.build());
  }

  /**
   * Adds multiple errors.
   *
   * @param newErrors Errors to add
   */
  public void addErrors(Collection<TransformationError> newErrors) {
    newErrors.forEach(this::addError);
  }

  /**
   * Checks if any errors have been collected.
   */
  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  /**
   * Gets the count of collected errors.
   */
  public int getErrorCount() {
    return errors.size();
  }

  /**
   * Gets all collected errors.
   */
  public List<TransformationError> getErrors() {
    return Collections.unmodifiableList(new ArrayList<>(errors));
  }

  /**
   * Gets errors for a specific phase.
   *
   * @param phase The phase to filter by
   */
  public List<TransformationError> getErrorsByPhase(String phase) {
    List<TransformationError> result = new ArrayList<>();
    for (TransformationError error : errors) {
      if (phase.equals(error.getPhase())) {
        result.add(error);
      }
    }
    return result;
  }

  /**
   * Gets errors for a specific field.
   *
   * @param field The field to filter by
   */
  public List<TransformationError> getErrorsByField(String field) {
    List<TransformationError> result = new ArrayList<>();
    for (TransformationError error : errors) {
      if (field.equals(error.getField())) {
        result.add(error);
      }
    }
    return result;
  }

  /**
   * Sets whether to stop transformation on first error.
   *
   * @param stopOnError true to stop on first error
   */
  public void setStopOnError(boolean stopOnError) {
    this.stopOnError = stopOnError;
  }

  /**
   * Checks if transformation should stop due to errors.
   */
  public boolean shouldStop() {
    return stopOnError && hasErrors();
  }

  /**
   * Clears all collected errors.
   */
  public void clear() {
    errors.clear();
  }

  /**
   * Generates a detailed error report.
   */
  public String generateReport() {
    StringBuilder sb = new StringBuilder();
    sb.append("Transformation Error Report\n");
    sb.append("============================\n");
    sb.append("Total Errors: ").append(errors.size()).append("\n\n");

    // Group by phase
    Map<String, List<TransformationError>> byPhase = new HashMap<>();
    for (TransformationError error : errors) {
      byPhase.computeIfAbsent(error.getPhase(), k -> new ArrayList<>()).add(error);
    }

    byPhase.forEach((phase, phaseErrors) -> {
      sb.append("Phase: ").append(phase).append("\n");
      phaseErrors.forEach(error -> {
        sb.append("  - [").append(error.getErrorId()).append("] ")
            .append(error.getMessage()).append("\n");
        if (error.getField() != null) {
          sb.append("    Field: ").append(error.getField()).append("\n");
        }
        if (error.getReason() != null) {
          sb.append("    Reason: ").append(error.getReason()).append("\n");
        }
      });
      sb.append("\n");
    });

    return sb.toString();
  }

  @Override
  public String toString() {
    return String.format("TransformationErrorCollector{errorCount=%d, maxErrors=%d}",
        errors.size(), maxErrors);
  }
}