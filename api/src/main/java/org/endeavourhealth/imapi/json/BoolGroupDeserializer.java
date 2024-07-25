package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.model.eclBuilder.BoolGroup;
import org.endeavourhealth.imapi.model.eclBuilder.ExpressionConstraint;
import org.endeavourhealth.imapi.model.eclBuilder.Refinement;
import org.endeavourhealth.imapi.model.imq.Bool;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class BoolGroupDeserializer extends StdDeserializer<BoolGroup> {
  private ObjectMapper mapper = new ObjectMapper();

  public BoolGroupDeserializer() {
    this(null);
  }

  public BoolGroupDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public BoolGroup deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    JsonNode node = jp.getCodec().readTree(jp);
    return populateBoolGroupFromJson(node);
  }

  public BoolGroup populateBoolGroupFromJson(JsonNode node) throws IOException {
    BoolGroup boolGroup = new BoolGroup();
    Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
    while (fields.hasNext()) {
      Map.Entry<String, JsonNode> field = fields.next();
      String key = field.getKey();
      switch (key) {
        case "conjunction": {
          switch (field.getValue().textValue()) {
            case "and":
              boolGroup.setConjunction(Bool.and);
              break;
            case "or":
              boolGroup.setConjunction(Bool.or);
              break;
            default:
              throw new IOException("Failure to set Bool value from input: " + field.getValue());
          }
          break;
        }
        case "attributeGroup": {
          boolGroup.setAttributeGroup(field.getValue().booleanValue());
          break;
        }
        case "exclude": {
          boolGroup.setExclude(field.getValue().booleanValue());
          break;
        }
        case "items": {
          ArrayNode arrayNode = (ArrayNode) field.getValue();
          Iterator<JsonNode> items = arrayNode.elements();
          while (items.hasNext()) {
            JsonNode item = items.next();
            if (item.isObject() && item.has("type")) {
              String type = item.get("type").textValue();
              if ("BoolGroup".equals(type)) {
                boolGroup.addItem(populateBoolGroupFromJson(item));
              } else if ("ExpressionConstraint".equals(type)) {
                boolGroup.addItem(mapper.readValue(mapper.writeValueAsString(item), ExpressionConstraint.class));
              } else if ("Refinement".equals(type)) {
                boolGroup.addItem(mapper.readValue(mapper.writeValueAsString(item), Refinement.class));
              }
            } else throw new IOException("Items must be an object with a type field");
          }
        }
      }
    }
    return boolGroup;
  }
}
