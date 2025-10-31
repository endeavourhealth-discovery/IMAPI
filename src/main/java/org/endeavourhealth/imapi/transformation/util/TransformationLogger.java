package org.endeavourhealth.imapi.transformation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Centralized logging utility for transformation operations.
 * Manages correlation IDs and provides structured logging with context.
 */
public class TransformationLogger {

  private static final Logger log = LoggerFactory.getLogger(TransformationLogger.class);

  private final String correlationId;
  private final Logger delegate;

  /**
   * Creates a TransformationLogger with a correlation ID.
   *
   * @param correlationId Unique correlation ID for tracking
   * @param loggerClass The class requesting the logger
   */
  public TransformationLogger(String correlationId, Class<?> loggerClass) {
    this.correlationId = correlationId;
    this.delegate = LoggerFactory.getLogger(loggerClass);
    MDC.put("correlationId", correlationId);
  }

  /**
   * Logs a debug message with correlation context.
   */
  public void debug(String message, Object... args) {
    delegate.debug("[{}] {}", correlationId, String.format(message, args));
  }

  /**
   * Logs an info message with correlation context.
   */
  public void info(String message, Object... args) {
    delegate.info("[{}] {}", correlationId, String.format(message, args));
  }

  /**
   * Logs a warning message with correlation context.
   */
  public void warn(String message, Object... args) {
    delegate.warn("[{}] {}", correlationId, String.format(message, args));
  }

  /**
   * Logs a warning message with exception.
   */
  public void warn(String message, Throwable throwable) {
    delegate.warn("[{}] {}", correlationId, message, throwable);
  }

  /**
   * Logs an error message with correlation context.
   */
  public void error(String message, Object... args) {
    delegate.error("[{}] {}", correlationId, String.format(message, args));
  }

  /**
   * Logs an error message with exception.
   */
  public void error(String message, Throwable throwable) {
    delegate.error("[{}] {}", correlationId, message, throwable);
  }

  /**
   * Logs transformation phase entry.
   */
  public void logPhaseStart(String phaseName) {
    info("=== Starting Phase: {} ===", phaseName);
  }

  /**
   * Logs transformation phase completion.
   */
  public void logPhaseComplete(String phaseName) {
    info("=== Phase Complete: {} ===", phaseName);
  }

  /**
   * Logs operation timing.
   */
  public void logOperationTiming(String operationName, long durationMs) {
    info("Operation {} completed in {}ms", operationName, durationMs);
  }

  /**
   * Gets the correlation ID.
   */
  public String getCorrelationId() {
    return correlationId;
  }

  /**
   * Gets the underlying Logger.
   */
  public Logger getDelegate() {
    return delegate;
  }

  /**
   * Cleans up MDC on completion.
   */
  public void cleanup() {
    MDC.remove("correlationId");
  }
}