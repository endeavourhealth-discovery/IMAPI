package org.endeavourhealth.imapi.transformation.engine;

import org.endeavourhealth.imapi.model.qof.ExtractionField;
import org.endeavourhealth.imapi.model.imq.ReturnProperty;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.transformation.core.TransformationContext;
import org.endeavourhealth.imapi.transformation.util.FieldMappingDictionary;
import org.endeavourhealth.imapi.transformation.util.LogicExpressionParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Processes QOF extraction fields for return specifications.
 * Maps extraction fields to IMQuery return properties.
 * Transforms extraction field logic into WHERE conditions.
 */
public class ExtractionFieldTransformer {
  private final FieldMappingDictionary fieldMapping;
  private final LogicExpressionParser logicParser;

  public ExtractionFieldTransformer(FieldMappingDictionary fieldMapping) {
    this.fieldMapping = fieldMapping;
    this.logicParser = new LogicExpressionParser();
  }
  
  public ExtractionFieldTransformer(FieldMappingDictionary fieldMapping, LogicExpressionParser logicParser) {
    this.fieldMapping = fieldMapping;
    this.logicParser = logicParser;
  }

  /**
   * Transform extraction fields to return properties.
   */
  public List<ReturnProperty> transformExtractionFields(List<ExtractionField> extractionFields, 
                                                         TransformationContext context) {
    List<ReturnProperty> returnProperties = new ArrayList<>();

    if (extractionFields == null || extractionFields.isEmpty()) {
      return returnProperties;
    }

    for (ExtractionField field : extractionFields) {
      try {
        ReturnProperty prop = transformField(field, context);
        if (prop != null) {
          returnProperties.add(prop);
        }
      } catch (Exception e) {
        context.addWarning("Error transforming extraction field '" + field.getName() + "': " + e.getMessage());
      }
    }

    return returnProperties;
  }

  private ReturnProperty transformField(ExtractionField field, TransformationContext context) {
    ReturnProperty property = new ReturnProperty();
    
    // Use field name as property name
    property.setName(field.getName());
    
    // Get IRI for this field
    String iri = fieldMapping.getIri(field.getName());
    property.setIri(iri);
    
    // Set description
    if (field.getDescription() != null) {
      property.setDescription(field.getDescription());
    }
    
    // Store cluster information if present
    if (field.getCluster() != null && !field.getCluster().isEmpty() && !"n/a".equals(field.getCluster())) {
      // Cluster info could be used for grouping or filtering
      property.setAs(field.getCluster());
    }
    
    // Parse logic expression if present and not "Unconditional"
    if (field.getLogic() != null && !field.getLogic().isEmpty() && !"Unconditional".equalsIgnoreCase(field.getLogic())) {
      try {
        Match logicMatch = logicParser.parseLogic(field.getLogic(), fieldMapping);
        if (logicMatch != null) {
          property.addMatch(logicMatch);
        }
      } catch (Exception e) {
        context.addWarning("Error parsing logic for extraction field '" + field.getName() + "': " + e.getMessage());
      }
    }
    
    return property;
  }
}