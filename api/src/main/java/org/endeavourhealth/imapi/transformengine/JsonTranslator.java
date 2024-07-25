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
      if (from instanceof ArrayNode) {
        ArrayList<Object> target = new ArrayList<>();
        for (JsonNode node : ((ArrayNode) from)) {
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

  }

  @Override
  public Object createEntity(String type) {
    return null;
  }

  @Override
  public Object getPropertyValue(Object source, String property) throws DataFormatException {
    if (source instanceof ArrayNode) {
      if (((ArrayNode) source).size() > 1)
        throw new DataFormatException("Looking for value of property : " + property + "  but Source object is list. Either source is wrongly formatted or the map should have a list mode set");
      else
        source = ((ArrayNode) source).get(0);
    }
    if (((JsonNode) source).has(property)) {
      JsonNode value = ((JsonNode) source).get(property);
      return convertFromSource(value);
    }
    return null;
  }

  @Override
  public boolean isCollection(Object source) {
    if (source instanceof ArrayNode)
      return true;
    else
      return false;
  }


  private Object convertNode(Object node) throws DataFormatException {
    if (node instanceof JsonNode) {
      JsonNode jsonNode = (JsonNode) node;
      if (jsonNode.isTextual())
        return jsonNode.asText();
      else if (jsonNode.isLong())
        return jsonNode.asLong();
      else if (jsonNode.isInt())
        return jsonNode.asInt();
      else if (jsonNode.isDouble())
        return jsonNode.asDouble();
      else if (jsonNode.isFloat())
        return jsonNode.asDouble();
      else if (jsonNode.isBoolean())
        return jsonNode.asBoolean();
      else
        return node;
    } else
      throw new DataFormatException("Unexpected json object : " + node.getClass().getSimpleName());
  }


}
