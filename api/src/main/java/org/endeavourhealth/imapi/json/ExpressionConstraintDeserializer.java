package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.model.eclBuilder.BoolGroup;
import org.endeavourhealth.imapi.model.eclBuilder.ConceptReference;
import org.endeavourhealth.imapi.model.eclBuilder.ExpressionConstraint;
import org.endeavourhealth.imapi.model.eclBuilder.Refinement;
import org.endeavourhealth.imapi.model.imq.Bool;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ExpressionConstraintDeserializer extends StdDeserializer<ExpressionConstraint> {
  private final ObjectMapper mapper = new ObjectMapper();

  public ExpressionConstraintDeserializer() {
    this(null);
  }

  public ExpressionConstraintDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public ExpressionConstraint deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    JsonNode node = jp.getCodec().readTree(jp);
    return populateExpressionConstraintFromJson(node);
  }

  public ExpressionConstraint populateExpressionConstraintFromJson(JsonNode node) throws IOException {
    ExpressionConstraint expressionConstraint = new ExpressionConstraint();
    Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
    while (fields.hasNext()) {
      Map.Entry<String, JsonNode> field = fields.next();
      String key = field.getKey();
      switch (key) {
        case "constraintOperator" -> expressionConstraint.setConstraintOperator(field.getValue().textValue());
        case "conjunction" -> {
          switch (field.getValue().textValue()) {
            case "and" -> expressionConstraint.setConjunction(Bool.and);
            case "or" -> expressionConstraint.setConjunction(Bool.or);
            default -> throw new IOException("Failure to set Bool value from input: " + field.getValue());
          }
        }
        case "conceptSingle" ->
          expressionConstraint.setConceptSingle(mapper.readValue(mapper.writeValueAsString(field.getValue()), ConceptReference.class));
        case "conceptBool" ->
          expressionConstraint.setConceptBool(mapper.readValue(mapper.writeValueAsString(field.getValue()), BoolGroup.class));
        case "refinementItems" -> processRefinementItems(field, expressionConstraint);
        case "type" -> expressionConstraint.setType(field.getValue().textValue());
        default -> throw new IOException("Unexpected key encountered while deserializing ExpressionConstraint: " + key);
      }
    }
    return expressionConstraint;
  }

  private void processRefinementItems(Map.Entry<String, JsonNode> field, ExpressionConstraint expressionConstraint) throws IOException {
    ArrayNode arrayNode = (ArrayNode) field.getValue();
    Iterator<JsonNode> items = arrayNode.elements();
    while (items.hasNext()) {
      JsonNode item = items.next();
      if (item.isObject() && item.has("type")) {
        String type = item.get("type").textValue();
        if ("BoolGroup".equals(type)) {
          expressionConstraint.addRefinementItem(mapper.readValue(mapper.writeValueAsString(item), BoolGroup.class));
        } else if ("ExpressionConstraint".equals(type)) {
          expressionConstraint.addRefinementItem(mapper.readValue(mapper.writeValueAsString(item), ExpressionConstraint.class));
        } else if ("Refinement".equals(type)) {
          expressionConstraint.addRefinementItem(mapper.readValue(mapper.writeValueAsString(item), Refinement.class));
        }
      } else throw new IOException("Refinement items must be an object with a type field");
    }
  }
}
