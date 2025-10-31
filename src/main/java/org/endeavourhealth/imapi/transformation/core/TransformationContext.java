package org.endeavourhealth.imapi.transformation.core;

import org.endeavourhealth.imapi.model.imq.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Maintains state and context information during a QOF to IMQ transformation.
 * Provides access to the Query being built, error tracking, and reference mappings
 * between QOF and IMQ elements.
 */
public class TransformationContext {

  private static final Logger log = LoggerFactory.getLogger(TransformationContext.class);

  private final Query targetQuery;
  private final String correlationId;
  private final TransformationErrorCollector errorCollector;
  private final Map<String, Object> referenceMap; // Maps QOF IDs to IMQ equivalents
  private final Map<String, Object> metadata; // Generic metadata storage

  /**
   * Creates a new transformation context.
   *
   * @param targetQuery The Query object being built
   */
  public TransformationContext(Query targetQuery) {
    this.targetQuery = targetQuery;
    this.correlationId = UUID.randomUUID().toString();
    this.errorCollector = new TransformationErrorCollector();
    this.referenceMap = new HashMap<>();
    this.metadata = new HashMap<>();
    log.debug("Created transformation context with ID: {}", correlationId);
  }

  /**
   * Gets the target Query object being built.
   */
  public Query getTargetQuery() {
    return targetQuery;
  }

  /**
   * Gets the unique correlation ID for this transformation.
   */
  public String getCorrelationId() {
    return correlationId;
  }

  /**
   * Gets the error collector for tracking transformation errors.
   */
  public TransformationErrorCollector getErrorCollector() {
    return errorCollector;
  }

  /**
   * Stores a reference mapping from QOF element to IMQ equivalent.
   *
   * @param qofId The QOF element identifier
   * @param imqElement The corresponding IMQ element
   */
  public void mapReference(String qofId, Object imqElement) {
    referenceMap.put(qofId, imqElement);
  }

  /**
   * Retrieves a previously mapped reference.
   *
   * @param qofId The QOF element identifier
   * @return The mapped IMQ element, or null if not found
   */
  public Object getReference(String qofId) {
    return referenceMap.get(qofId);
  }

  /**
   * Stores generic metadata for transformation state.
   *
   * @param key The metadata key
   * @param value The metadata value
   */
  public void putMetadata(String key, Object value) {
    metadata.put(key, value);
  }

  /**
   * Retrieves previously stored metadata.
   *
   * @param key The metadata key
   * @return The metadata value, or null if not found
   */
  public Object getMetadata(String key) {
    return metadata.get(key);
  }

  /**
   * Checks if this transformation has any errors.
   */
  public boolean hasErrors() {
    return errorCollector.hasErrors();
  }

  /**
   * Gets all collected errors.
   */
  public List<TransformationError> getErrors() {
    return errorCollector.getErrors();
  }

  /**
   * Logs the transformation context state for debugging.
   */
  public void logContextState() {
    log.debug("Transformation context [{}]: {} errors, {} references, {} metadata items",
        correlationId,
        errorCollector.getErrorCount(),
        referenceMap.size(),
        metadata.size()
    );
  }
}