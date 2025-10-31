package org.endeavourhealth.imapi.transformation.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.qof.QOFDocument;
import org.endeavourhealth.imapi.transformation.component.*;
import org.endeavourhealth.imapi.transformation.core.TransformationContext;
import org.endeavourhealth.imapi.transformation.core.TransformationError;
import org.endeavourhealth.imapi.transformation.core.TransformationException;
import org.endeavourhealth.imapi.transformation.util.TransformationLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

/**
 * Main orchestrator for QOF to IMQ transformation.
 * Coordinates the entire transformation pipeline including parsing, validation,
 * component transformations, and result serialization.
 *
 * Transformation Lifecycle:
 * 1. Load and parse QOF document
 * 2. Validate input document structure
 * 3. Create empty Query and transformation context
 * 4. Execute component transformers in sequence:
 *    - MetadataTransformer
 *    - SelectionTransformer
 *    - RegisterTransformer
 *    - ExtractionFieldTransformer
 *    - IndicatorTransformer
 * 5. Validate intermediate transformation state
 * 6. Validate output Query
 * 7. Return result or throw exception with accumulated errors
 *
 * Thread Safety: NOT thread-safe - each transformation requires a new instance
 */
public class QOFToIMQTransformer {

  private static final Logger log = LoggerFactory.getLogger(QOFToIMQTransformer.class);

  // Component transformers
  private final MetadataTransformer metadataTransformer;
  private final SelectionTransformer selectionTransformer;
  private final RegisterTransformer registerTransformer;
  private final ExtractionFieldTransformer extractionFieldTransformer;
  private final IndicatorTransformer indicatorTransformer;

  // Validation and state management
  private final QOFDocumentValidator qofDocumentValidator;
  private final TransformationValidator transformationValidator;
  private final QOFDocumentLoader qofDocumentLoader;

  // Transformation state
  private TransformationContext context;
  private Query targetQuery;
  private long transformationStartTime;
  private long transformationEndTime;

  /**
   * Creates a new QOF to IMQ transformer with all component dependencies.
   */
  public QOFToIMQTransformer() {
    // Create a temporary correlation ID for initialization
    String initCorrelationId = UUID.randomUUID().toString();
    TransformationLogger initLogger = new TransformationLogger(initCorrelationId, QOFToIMQTransformer.class);

    this.metadataTransformer = new MetadataTransformer(initLogger);
    this.selectionTransformer = new SelectionTransformer(new MatchBuilder(), initLogger);
    this.registerTransformer = new RegisterTransformer(initLogger);
    this.extractionFieldTransformer = new ExtractionFieldTransformer(
        new PathBuilder(), new NodeBuilder(), new ReturnBuilder(), initLogger);
    this.indicatorTransformer = new IndicatorTransformer(initLogger);
    this.qofDocumentValidator = new QOFDocumentValidator(
        new org.endeavourhealth.imapi.transformation.core.TransformationErrorCollector(), initLogger);
    this.transformationValidator = new TransformationValidator();
    this.qofDocumentLoader = new QOFDocumentLoader(new ObjectMapper(), initLogger);
    log.info("Initialized QOFToIMQTransformer");
  }

  /**
   * Transforms a QOF document from file path to IMQ Query.
   *
   * @param filePath Path to the QOF JSON document
   * @return The transformed IMQ Query
   * @throws TransformationException If transformation fails
   */
  public Query transformFromFile(String filePath) throws TransformationException {
    log.info("Starting transformation from file: {}", filePath);
    transformationStartTime = System.currentTimeMillis();

    try {
      QOFDocument qofDocument = qofDocumentLoader.loadFromFile(filePath);
      return transform(qofDocument);
    } catch (TransformationException e) {
      log.error("Transformation failed: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error during transformation", e);
      throw new TransformationException("Transformation failed: " + e.getMessage(), e);
    } finally {
      transformationEndTime = System.currentTimeMillis();
    }
  }

  /**
   * Transforms a QOF document string to IMQ Query.
   *
   * @param jsonString The QOF document as JSON string
   * @return The transformed IMQ Query
   * @throws TransformationException If transformation fails
   */
  public Query transformFromString(String jsonString) throws TransformationException {
    log.info("Starting transformation from JSON string");
    transformationStartTime = System.currentTimeMillis();

    try {
      QOFDocument qofDocument = qofDocumentLoader.loadFromString(jsonString);
      return transform(qofDocument);
    } catch (TransformationException e) {
      log.error("Transformation failed: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error during transformation", e);
      throw new TransformationException("Transformation failed: " + e.getMessage(), e);
    } finally {
      transformationEndTime = System.currentTimeMillis();
    }
  }

  /**
   * Transforms a QOF document to IMQ Query.
   *
   * @param qofDocument The QOF document to transform
   * @return The transformed IMQ Query
   * @throws TransformationException If transformation fails
   */
  public Query transform(QOFDocument qofDocument) throws TransformationException {
    log.info("Starting QOF to IMQ transformation for document: {}", qofDocument.getName());
    transformationStartTime = System.currentTimeMillis();

    try {
      // Phase 1: Validate input document
      log.debug("Phase 1: Validating input QOF document");
      TransformationValidator.ValidationResult validationResult =
          transformationValidator.validateInputDocument(qofDocument);

      if (!validationResult.isValid()) {
        String errorMsg = formatValidationErrors(validationResult.getErrors());
        log.error("Input validation failed: {}", errorMsg);
        throw new TransformationException("Input validation failed: " + errorMsg);
      }

      // Phase 2: Initialize transformation context and target query
      log.debug("Phase 2: Initializing transformation context");
      targetQuery = new Query();
      context = new TransformationContext(targetQuery);

      // Phase 3: Execute component transformers
      log.debug("Phase 3: Executing component transformers");
      executeComponentTransformers(qofDocument);

      // Phase 4: Check for transformation errors
      if (context.hasErrors()) {
        String errorMsg = formatTransformationErrors(context.getErrors());
        log.error("Transformation encountered errors: {}", errorMsg);
        throw new TransformationException("Transformation failed with errors: " + errorMsg);
      }

      // Phase 5: Validate output Query
      log.debug("Phase 4: Validating output Query");
      validationResult = transformationValidator.validateOutputQuery(targetQuery);

      if (!validationResult.isValid()) {
        String errorMsg = formatValidationErrors(validationResult.getErrors());
        log.error("Output validation failed: {}", errorMsg);
        throw new TransformationException("Output validation failed: " + errorMsg);
      }

      // Phase 6: Log transformation success
      transformationEndTime = System.currentTimeMillis();
      long duration = transformationEndTime - transformationStartTime;

      log.info("Transformation completed successfully for document: {} ({}ms)",
          qofDocument.getName(), duration);
      logTransformationSummary();

      return targetQuery;

    } catch (TransformationException e) {
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error during transformation", e);
      throw new TransformationException("Transformation failed: " + e.getMessage(), e);
    }
  }

  /**
   * Executes all component transformers in sequence.
   */
  private void executeComponentTransformers(QOFDocument qofDocument) throws TransformationException {
    try {
      // Step 1: Transform metadata (sets up Query name, description, etc.)
      log.debug("Executing MetadataTransformer");
      metadataTransformer.transformMetadata(qofDocument, context);

      // Step 2: Transform selections to Where clauses
      if (qofDocument.getSelections() != null && !qofDocument.getSelections().isEmpty()) {
        log.debug("Executing SelectionTransformer");
        selectionTransformer.transformSelections(qofDocument.getSelections(), targetQuery, context);
      }

      // Step 3: Transform registers to dataSet references
      if (qofDocument.getRegisters() != null && !qofDocument.getRegisters().isEmpty()) {
        log.debug("Executing RegisterTransformer");
        registerTransformer.transformRegisters(qofDocument.getRegisters(), targetQuery, context);
      }

      // Step 4: Transform extraction fields to Path objects and Return clause
      if (qofDocument.getExtractionFields() != null && !qofDocument.getExtractionFields().isEmpty()) {
        log.debug("Executing ExtractionFieldTransformer");
        extractionFieldTransformer.transformExtractionFields(qofDocument.getExtractionFields(), targetQuery, context);
      }

      // Step 5: Transform indicators to groupBy and calculation clauses
      if (qofDocument.getIndicators() != null && !qofDocument.getIndicators().isEmpty()) {
        log.debug("Executing IndicatorTransformer");
        indicatorTransformer.transformIndicators(qofDocument.getIndicators(), targetQuery, context);
      }

      log.debug("All component transformers completed successfully");

    } catch (Exception e) {
      log.error("Error during component transformer execution", e);
      throw new TransformationException("Component transformation failed: " + e.getMessage(), e);
    }
  }

  /**
   * Formats validation errors for logging.
   */
  private String formatValidationErrors(List<Object> errors) {
    if (errors.isEmpty()) {
      return "No errors";
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < Math.min(errors.size(), 5); i++) {
      sb.append("\n  - ").append(errors.get(i));
    }
    if (errors.size() > 5) {
      sb.append("\n  ... and ").append(errors.size() - 5).append(" more");
    }
    return sb.toString();
  }

  /**
   * Formats transformation errors for logging.
   */
  private String formatTransformationErrors(List<TransformationError> errors) {
    if (errors.isEmpty()) {
      return "No errors";
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < Math.min(errors.size(), 5); i++) {
      TransformationError error = errors.get(i);
      sb.append("\n  - [").append(error.getPhase()).append("] ")
          .append(error.getMessage());
      if (error.getField() != null) {
        sb.append(" (field: ").append(error.getField()).append(")");
      }
    }
    if (errors.size() > 5) {
      sb.append("\n  ... and ").append(errors.size() - 5).append(" more");
    }
    return sb.toString();
  }

  /**
   * Logs a summary of the transformation result.
   */
  private void logTransformationSummary() {
    long duration = transformationEndTime - transformationStartTime;

    log.info("=== Transformation Summary ===");
    log.info("Duration: {}ms", duration);
    log.info("Correlation ID: {}", context.getCorrelationId());
    log.info("Document: {}", targetQuery.getName());
    log.info("Reference mappings: {}", context.getErrorCollector().getErrorCount());

    context.logContextState();
  }

  /**
   * Gets the current transformation context.
   */
  public TransformationContext getContext() {
    return context;
  }

  /**
   * Gets the current target query being built.
   */
  public Query getTargetQuery() {
    return targetQuery;
  }

  /**
   * Gets the transformation duration in milliseconds.
   */
  public long getTransformationDuration() {
    if (transformationStartTime == 0) {
      return 0;
    }
    if (transformationEndTime == 0) {
      return System.currentTimeMillis() - transformationStartTime;
    }
    return transformationEndTime - transformationStartTime;
  }
}