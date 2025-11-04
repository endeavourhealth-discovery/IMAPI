package org.endeavourhealth.imapi.transformation.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.qof.*;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.transformation.core.TransformationContext;
import org.endeavourhealth.imapi.transformation.util.LogicExpressionParser;
import org.endeavourhealth.imapi.transformation.util.FieldMappingDictionary;

import java.util.*;

/**
 * Main orchestrator for QOF to IMQuery transformation.
 * Coordinates the entire transformation pipeline:
 * 1. Pre-conversion validation
 * 2. Parallel component transformations (Metadata, Selections, Registers, Indicators, ExtractionFields)
 * 3. Post-conversion validation
 * 4. Output generation
 */
public class QOFToIMQTransformer {
  
  private final LogicExpressionParser logicParser;
  private final FieldMappingDictionary fieldMapping;
  private final SelectionTransformer selectionTransformer;
  private final RegisterTransformer registerTransformer;
  private final IndicatorTransformer indicatorTransformer;
  private final ExtractionFieldTransformer extractionFieldTransformer;
  private final MetadataTransformer metadataTransformer;

  public QOFToIMQTransformer() {
    this.logicParser = new LogicExpressionParser();
    this.fieldMapping = new FieldMappingDictionary();
    this.selectionTransformer = new SelectionTransformer(logicParser, fieldMapping);
    this.registerTransformer = new RegisterTransformer(logicParser, fieldMapping);
    this.indicatorTransformer = new IndicatorTransformer(logicParser, fieldMapping);
    this.extractionFieldTransformer = new ExtractionFieldTransformer(fieldMapping, logicParser);
    this.metadataTransformer = new MetadataTransformer();
  }

  /**
   * Transform a QOF document to IMQuery.
   * Returns a map of query names to Query objects.
   */
  public Map<String, Query> transform(QOFDocument qofDocument) throws Exception {
    TransformationContext context = new TransformationContext(qofDocument);
    
    try {
      // Step 1: Pre-conversion validation
      validateQOFDocument(qofDocument, context);
      if (context.hasErrors()) {
        throw new IllegalArgumentException("QOF document validation failed");
      }

      // Step 2: Transform Selections first (they are dependencies)
      if (qofDocument.getSelections() != null && !qofDocument.getSelections().isEmpty()) {
        for (Selection selection : qofDocument.getSelections()) {
          selectionTransformer.transform(selection, context);
        }
      }

      // Step 3: Transform Registers
      if (qofDocument.getRegisters() != null && !qofDocument.getRegisters().isEmpty()) {
        for (Register register : qofDocument.getRegisters()) {
          registerTransformer.transform(register, context);
        }
      }

      // Step 4: Transform Indicators
      if (qofDocument.getIndicators() != null && !qofDocument.getIndicators().isEmpty()) {
        for (Indicator indicator : qofDocument.getIndicators()) {
          indicatorTransformer.transform(indicator, context);
        }
      }

      // Step 5: Transform metadata and apply to all queries
      metadataTransformer.transformMetadata(qofDocument, context);

      // Step 6: Post-conversion validation
      validateGeneratedQueries(context);
      if (context.hasErrors()) {
        // Log errors but don't throw - allow partial results
        logErrors(context);
      }

      // Log warnings if any
      if (context.hasWarnings()) {
        logWarnings(context);
      }

      return context.getGeneratedQueries();

    } catch (Exception e) {
      context.addError("Transformation failed: " + e.getMessage());
      throw e;
    }
  }

  /**
   * Transform from a QOF file path.
   */
  public Map<String, Query> transformFromFile(String filePath) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    QOFDocument qofDocument = mapper.readValue(new java.io.File(filePath), QOFDocument.class);
    return transform(qofDocument);
  }

  /**
   * Validate QOF document structure before transformation.
   */
  private void validateQOFDocument(QOFDocument doc, TransformationContext context) {
    if (doc == null) {
      context.addError("QOF document is null");
      return;
    }

    if (doc.getName() == null || doc.getName().isEmpty()) {
      context.addWarning("QOF document has no name");
    }

    // Validate Selections
    if (doc.getSelections() != null) {
      for (Selection selection : doc.getSelections()) {
        if (selection.getName() == null || selection.getName().isEmpty()) {
          context.addError("Selection has no name");
        }
        if (selection.getRules() == null || selection.getRules().isEmpty()) {
          context.addWarning("Selection '" + selection.getName() + "' has no rules");
        }
      }
    }

    // Validate Registers
    if (doc.getRegisters() != null) {
      for (Register register : doc.getRegisters()) {
        if (register.getName() == null || register.getName().isEmpty()) {
          context.addError("Register has no name");
        }
        if (register.getBase() == null || register.getBase().isEmpty()) {
          context.addWarning("Register '" + register.getName() + "' has no base Selection");
        }
      }
    }

    // Validate Indicators
    if (doc.getIndicators() != null) {
      for (Indicator indicator : doc.getIndicators()) {
        if (indicator.getName() == null || indicator.getName().isEmpty()) {
          context.addError("Indicator has no name");
        }
        if (indicator.getBase() == null || indicator.getBase().isEmpty()) {
          context.addWarning("Indicator '" + indicator.getName() + "' has no base Register");
        }
      }
    }
  }

  /**
   * Validate generated IMQuery objects.
   */
  private void validateGeneratedQueries(TransformationContext context) {
    for (Query query : context.getGeneratedQueries().values()) {
      if (query.getIri() == null || query.getIri().isEmpty()) {
        context.addError("Generated query has no IRI: " + query.getName());
      }
      if (query.getTypeOf() == null) {
        context.addWarning("Generated query '" + query.getName() + "' has no typeOf");
      }
    }
  }

  private void logErrors(TransformationContext context) {
    System.err.println("=== Transformation Errors ===");
    for (String error : context.getErrors()) {
      System.err.println("ERROR: " + error);
    }
  }

  private void logWarnings(TransformationContext context) {
    System.out.println("=== Transformation Warnings ===");
    for (String warning : context.getWarnings()) {
      System.out.println("WARNING: " + warning);
    }
  }

  public FieldMappingDictionary getFieldMapping() {
    return fieldMapping;
  }

  public LogicExpressionParser getLogicParser() {
    return logicParser;
  }
}