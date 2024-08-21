package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.model.map.MapProperty;

import java.util.ArrayList;
import java.util.zip.DataFormatException;

public class JsonTranslator implements SyntaxTranslator {

  @Override
  public Object convertToTarget(Object from) {
    return null;
  }

  @Override
  public Object convertFromSource(Object from) throws DataFormatException {
    try {
      if (from instanceof ArrayNode fromArrayNode) {
        ArrayList<Object> target = new ArrayList<>();
        for (JsonNode node : fromArrayNode) {
          target.add(convertNode(node));
        }
        return target;
      } else
        return (convertNode(from));
    } catch (Exception e) {
      throw new DataFormatException("Unknown json source to Triple (LD) object : " + from.getClass().getSimpleName());
    }
  }

  @Override
  public void setPropertyValue(MapProperty rule, Object targetEntity, String path, Object targetValue) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Object createEntity(String type) {
    return null;
  }

  @Override
  public Object getPropertyValue(Object source, String property) throws DataFormatException {
    if (source instanceof ArrayNode sourceArrayNode) {
      if (sourceArrayNode.size() > 1)
        throw new DataFormatException("Looking for value of property : " + property + "  but Source object is list. Either source is wrongly formatted or the map should have a list mode set");
      else
        source = sourceArrayNode.get(0);
    }
    if (((JsonNode) source).has(property)) {
      JsonNode value = ((JsonNode) source).get(property);
      return convertFromSource(value);
    }
    return null;
  }

  @Override
  public boolean isCollection(Object source) {
    return source instanceof ArrayNode;
  }


  private Object convertNode(Object node) throws DataFormatException {
    if (node instanceof JsonNode nodeJsonNode) {
      if (nodeJsonNode.isTextual())
        return nodeJsonNode.asText();
      else if (nodeJsonNode.isLong())
        return nodeJsonNode.asLong();
      else if (nodeJsonNode.isInt())
        return nodeJsonNode.asInt();
      else if (nodeJsonNode.isDouble())
        return nodeJsonNode.asDouble();
      else if (nodeJsonNode.isFloat())
        return nodeJsonNode.asDouble();
      else if (nodeJsonNode.isBoolean())
        return nodeJsonNode.asBoolean();
      else
        return node;
    } else
      throw new DataFormatException("Unexpected json object : " + node.getClass().getSimpleName());
  }


}
