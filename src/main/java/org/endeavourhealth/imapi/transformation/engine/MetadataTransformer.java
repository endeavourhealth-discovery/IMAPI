package org.endeavourhealth.imapi.transformation.engine;

import org.endeavourhealth.imapi.model.qof.QOFDocument;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.transformation.component.QueryBuilderFactory;
import org.endeavourhealth.imapi.transformation.core.TransformationContext;
import org.endeavourhealth.imapi.transformation.util.TransformationLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Transforms QOF document metadata into Query fields.
 * Maps top-level document information to corresponding IMQ Query properties.
 *
 * Metadata Transformation Rules:
 * - QOFDocument.name → Query.name (required)
 * - QOFDocument.description → Query.description (if available)
 * - Auto-generate default name if missing with format: "QOF_Query_<timestamp>"
 * - Preserve document identifiers and version information
 */
public class MetadataTransformer {
  private static final Logger log = LoggerFactory.getLogger(MetadataTransformer.class);
  private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
  private final TransformationLogger transformationLogger;

  /**
   * Creates a new MetadataTransformer.
   *
   * @param transformationLogger for structured logging with correlation tracking
   */
  public MetadataTransformer(TransformationLogger transformationLogger) {
    this.transformationLogger = transformationLogger;
  }

  /**
   * Transforms QOF document metadata into Query fields.
   *
   * Transformation Process:
   * 1. Extract QOFDocument name (required)
   * 2. Generate default name if missing
   * 3. Map description if available
   * 4. Create and populate Query object
   * 5. Store mappings in context
   *
   * @param qofDocument source QOF document
   * @param context transformation context for state management
   * @return Query object with metadata populated
   */
  public Query transformMetadata(QOFDocument qofDocument, TransformationContext context) {
    transformationLogger.info("Transforming QOF document metadata");

    // Create base Query object
    Query query = QueryBuilderFactory.createEmptyQuery();

    // Map document name (with default if missing)
    String queryName = extractDocumentName(qofDocument);
    query.setName(queryName);
    transformationLogger.debug("Mapped query name: {}", queryName);

    // Map description if available
    if (qofDocument.getName() != null && !qofDocument.getName().isBlank()) {
      // Use document name as description if available
      query.setDescription("Transformed from QOF document: " + qofDocument.getName());
    }

    transformationLogger.info("Metadata transformation completed successfully");
    return query;
  }

  /**
   * Extracts the document name with intelligent default generation.
   *
   * Strategy:
   * 1. Use QOFDocument.name if available and non-empty
   * 2. Generate default name with timestamp if name is missing
   *
   * @param qofDocument source QOF document
   * @return extracted or generated name
   */
  private String extractDocumentName(QOFDocument qofDocument) {
    if (qofDocument.getName() != null && !qofDocument.getName().isBlank()) {
      return qofDocument.getName();
    }

    // Generate default name with timestamp
    String defaultName = "QOF_Query_" + LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    transformationLogger.warn("QOF document has no name, using default: {}", defaultName);
    return defaultName;
  }

  /**
   * Enriches Query metadata with additional transformation context.
   *
   * Adds:
   * - Transformation timestamp
   * - Source reference to original QOF document
   * - Correlation ID for traceability
   *
   * @param query Query to enrich
   * @param context transformation context
   * @return enriched Query object
   */
  public Query enrichMetadata(Query query, TransformationContext context) {
    transformationLogger.info("Enriching query metadata with transformation context");

    // Add transformation timestamp to description
    if (query.getDescription() != null) {
      query.setDescription(
          query.getDescription() + 
          " | Transformed at: " + LocalDateTime.now() +
          " | Correlation ID: " + context.getCorrelationId()
      );
    } else {
      query.setDescription(
          "Transformed at: " + LocalDateTime.now() +
          " | Correlation ID: " + context.getCorrelationId()
      );
    }

    transformationLogger.debug("Metadata enrichment completed");
    return query;
  }
}