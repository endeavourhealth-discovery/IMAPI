package org.endeavourhealth.imapi.transformation.engine;

import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.transformation.core.TransformationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Builder for creating TransformationContext instances with pre-configured state.
 *
 * Useful for testing scenarios where you need a context with specific
 * reference mappings, metadata, or error states pre-configured.
 *
 * Example usage:
 * <pre>
 * TransformationContext context = new TransformationContextBuilder()
 *   .withQuery(query)
 *   .withMetadata("key", "value")
 *   .withReference("qof-id-123", imqElement)
 *   .build();
 * </pre>
 */
public class TransformationContextBuilder {

  private static final Logger log = LoggerFactory.getLogger(TransformationContextBuilder.class);

  private Query targetQuery;
  private final Map<String, Object> referenceMap = new HashMap<>();
  private final Map<String, Object> metadata = new HashMap<>();

  /**
   * Creates a new context builder.
   */
  public TransformationContextBuilder() {
    log.debug("Created new TransformationContextBuilder");
  }

  /**
   * Sets the target Query for the context.
   *
   * @param query The Query to use
   * @return This builder for chaining
   */
  public TransformationContextBuilder withQuery(Query query) {
    this.targetQuery = query;
    log.debug("Set target query: {}", query != null ? query.getName() : "null");
    return this;
  }

  /**
   * Adds a reference mapping to the context.
   *
   * @param qofId The QOF element identifier
   * @param imqElement The corresponding IMQ element
   * @return This builder for chaining
   */
  public TransformationContextBuilder withReference(String qofId, Object imqElement) {
    this.referenceMap.put(qofId, imqElement);
    log.debug("Added reference mapping: {} -> {}", qofId, 
        imqElement != null ? imqElement.getClass().getSimpleName() : "null");
    return this;
  }

  /**
   * Adds multiple reference mappings to the context.
   *
   * @param references Map of QOF IDs to IMQ elements
   * @return This builder for chaining
   */
  public TransformationContextBuilder withReferences(Map<String, Object> references) {
    if (references != null) {
      this.referenceMap.putAll(references);
      log.debug("Added {} reference mappings", references.size());
    }
    return this;
  }

  /**
   * Adds metadata to the context.
   *
   * @param key The metadata key
   * @param value The metadata value
   * @return This builder for chaining
   */
  public TransformationContextBuilder withMetadata(String key, Object value) {
    this.metadata.put(key, value);
    log.debug("Added metadata: {} = {}", key, value);
    return this;
  }

  /**
   * Adds multiple metadata entries to the context.
   *
   * @param metadata Map of metadata entries
   * @return This builder for chaining
   */
  public TransformationContextBuilder withMetadata(Map<String, Object> metadata) {
    if (metadata != null) {
      this.metadata.putAll(metadata);
      log.debug("Added {} metadata entries", metadata.size());
    }
    return this;
  }

  /**
   * Builds the TransformationContext with the configured state.
   *
   * @return A new TransformationContext with the configured state
   */
  public TransformationContext build() {
    // Create context with target query (or empty Query if not provided)
    Query query = targetQuery != null ? targetQuery : new Query();
    TransformationContext context = new TransformationContext(query);

    // Add pre-configured references
    for (Map.Entry<String, Object> entry : referenceMap.entrySet()) {
      context.mapReference(entry.getKey(), entry.getValue());
    }

    // Add pre-configured metadata
    for (Map.Entry<String, Object> entry : metadata.entrySet()) {
      context.putMetadata(entry.getKey(), entry.getValue());
    }

    log.debug("Built TransformationContext with {} references and {} metadata entries",
        referenceMap.size(), metadata.size());

    return context;
  }

  /**
   * Gets a human-readable summary of the builder's configuration.
   */
  public String getSummary() {
    return "TransformationContextBuilder{" +
        "query=" + (targetQuery != null ? targetQuery.getName() : "null") +
        ", references=" + referenceMap.size() +
        ", metadata=" + metadata.size() +
        '}';
  }
}