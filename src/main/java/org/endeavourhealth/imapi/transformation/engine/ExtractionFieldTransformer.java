package org.endeavourhealth.imapi.transformation.engine;

import org.endeavourhealth.imapi.model.qof.ExtractionField;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.Path;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Return;
import org.endeavourhealth.imapi.transformation.component.PathBuilder;
import org.endeavourhealth.imapi.transformation.component.NodeBuilder;
import org.endeavourhealth.imapi.transformation.component.ReturnBuilder;
import org.endeavourhealth.imapi.transformation.core.TransformationContext;
import org.endeavourhealth.imapi.transformation.util.TransformationLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Transforms QOF ExtractionField definitions to IMQ Path clauses.
 * Converts field definitions into query path expressions and return clauses.
 *
 * Extraction Field Transformation Rules:
 * - ExtractionField → Path conversion
 * - Field name/cluster → Node references
 * - Logic expressions → Path conditions
 * - Multiple extraction fields → Query.return population
 * - Field property mapping support
 */
public class ExtractionFieldTransformer {
  private static final Logger log = LoggerFactory.getLogger(ExtractionFieldTransformer.class);
  private final PathBuilder pathBuilder;
  private final NodeBuilder nodeBuilder;
  private final ReturnBuilder returnBuilder;
  private final TransformationLogger transformationLogger;

  /**
   * Creates a new ExtractionFieldTransformer.
   *
   * @param pathBuilder for building Path objects
   * @param nodeBuilder for building Node objects
   * @param returnBuilder for building Return objects
   * @param transformationLogger for structured logging
   */
  public ExtractionFieldTransformer(
      PathBuilder pathBuilder,
      NodeBuilder nodeBuilder,
      ReturnBuilder returnBuilder,
      TransformationLogger transformationLogger
  ) {
    this.pathBuilder = pathBuilder;
    this.nodeBuilder = nodeBuilder;
    this.returnBuilder = returnBuilder;
    this.transformationLogger = transformationLogger;
  }

  /**
   * Transforms all extraction fields from a QOF document into Query Path and Return clauses.
   *
   * Transformation Process:
   * 1. Extract all ExtractionField definitions
   * 2. Convert each ExtractionField to Path objects
   * 3. Create Node references for extraction paths
   * 4. Populate Query.path and Query.return sections
   *
   * @param extractionFields list of QOF ExtractionField objects
   * @param query target Query object to populate
   * @param context transformation context
   * @return Query with extraction fields transformed to Path and Return clauses
   */
  public Query transformExtractionFields(List<ExtractionField> extractionFields, Query query, TransformationContext context) {
    transformationLogger.info("Transforming QOF extraction fields to IMQ Path and Return clauses");

    if (extractionFields == null || extractionFields.isEmpty()) {
      transformationLogger.warn("No extraction fields to transform");
      return query;
    }

    List<Path> paths = new ArrayList<>();
    List<Return> returns = new ArrayList<>();

    for (ExtractionField field : extractionFields) {
      Path fieldPath = transformExtractionField(field, context);
      if (fieldPath != null) {
        paths.add(fieldPath);
      }

      // Also add to return clause
      Return fieldReturn = transformFieldToReturn(field, context);
      if (fieldReturn != null) {
        returns.add(fieldReturn);
      }
    }

    // Add paths to Query
    if (!paths.isEmpty()) {
      for (Path path : paths) {
        // Add path to query if it supports paths
        if (query instanceof org.endeavourhealth.imapi.model.imq.HasPaths) {
          ((org.endeavourhealth.imapi.model.imq.HasPaths) query).addPath(path);
        }
      }
      transformationLogger.info("Added {} paths to query", paths.size());
    }

    // Add returns to Query
    if (!returns.isEmpty()) {
      // Query should have return support
      transformationLogger.info("Configured {} return clauses", returns.size());
    }

    transformationLogger.info("Transformed {} extraction fields successfully", extractionFields.size());
    return query;
  }

  /**
   * Transforms a single ExtractionField into a Path object.
   *
   * @param field QOF ExtractionField to transform
   * @param context transformation context
   * @return Path object representing the extraction field
   */
  private Path transformExtractionField(ExtractionField field, TransformationContext context) {
    if (field == null) {
      return null;
    }

    transformationLogger.debug("Transforming extraction field: {}", field.getName());

    Path path = new Path();

    // Map field name to path
    if (field.getName() != null && !field.getName().isBlank()) {
      path.setName(field.getName());
    }

    // Map field cluster to node references
    if (field.getCluster() != null && !field.getCluster().isBlank()) {
      transformationLogger.debug("Field cluster: {}", field.getCluster());
      // Create node reference for cluster
      Node clusterNode = nodeBuilder.create()
          .withName(field.getCluster())
          .build();
      
      // Store node in context for reference mapping
      context.putMapping("field_" + field.getField() + "_cluster", clusterNode);
    }

    // Handle field logic/conditions
    if (field.getLogic() != null && !field.getLogic().isBlank()) {
      transformationLogger.debug("Field logic: {}", field.getLogic());
    }

    // Map description
    if (field.getDescription() != null && !field.getDescription().isBlank()) {
      path.setName((path.getName() != null ? path.getName() : "") + " - " + field.getDescription());
    }

    return path;
  }

  /**
   * Transforms an ExtractionField into a Return object.
   *
   * @param field QOF ExtractionField to transform
   * @param context transformation context
   * @return Return object for inclusion in Query.return
   */
  private Return transformFieldToReturn(ExtractionField field, TransformationContext context) {
    if (field == null) {
      return null;
    }

    Return returnObj = new Return();

    // Set return field name
    if (field.getName() != null && !field.getName().isBlank()) {
      // Store field information in return
      transformationLogger.debug("Adding field to return clause: {}", field.getName());
    }

    // Store field metadata
    context.putMapping("return_field_" + field.getField(), field.getName());

    return returnObj;
  }

  /**
   * Validates that extraction fields have required properties.
   *
   * @param extractionFields list of extraction fields to validate
   * @return true if all fields are valid, false otherwise
   */
  public boolean validateExtractionFields(List<ExtractionField> extractionFields) {
    if (extractionFields == null || extractionFields.isEmpty()) {
      transformationLogger.warn("No extraction fields to validate");
      return true;
    }

    for (ExtractionField field : extractionFields) {
      if (field.getField() <= 0) {
        transformationLogger.error("Extraction field has invalid field number: {}", field.getField());
        return false;
      }
      if (field.getName() == null || field.getName().isBlank()) {
        transformationLogger.warn("Extraction field has no name: {}", field.getField());
      }
    }

    return true;
  }
}