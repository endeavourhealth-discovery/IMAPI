package org.endeavourhealth.imapi.transform.qofimq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.transform.qofimq.ast.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class QofAstParser {

  public AstNode parse(String json) {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      JsonNode node = om.readTree(json);
      return parse(node);
    } catch (JsonProcessingException e) {
      throw new QofParseException("Invalid JSON: " + e.getMessage(), e);
    }
  }

  public AstNode parse(JsonNode node) {
    if (node == null || node.isNull()) {
      throw new QofParseException("Input JSON is null");
    }
    if (node.has("operator") && node.has("operands")) {
      String op = node.get("operator").asText();
      BoolOp bop = BoolOp.valueOf(op);
      List<AstNode> operands = new ArrayList<>();
      for (JsonNode child : node.get("operands")) {
        operands.add(parse(child));
      }
      return new BoolNode(bop, operands);
    }
    if (node.has("field") && node.has("op")) {
      String field = node.get("field").asText();
      PredicateOp op = PredicateOp.valueOf(node.get("op").asText());
      boolean negated = node.has("negated") && node.get("negated").asBoolean(false);
      JsonNode valueNode = node.get("value");
      Object single = valueNode == null || valueNode.isNull() ? null : extractValue(valueNode);
      List<Object> values = null;
      if (node.has("values") && node.get("values").isArray()) {
        values = new ArrayList<>();
        for (JsonNode v : node.get("values")) values.add(extractValue(v));
      }
      if (node.has("min") || node.has("max")) {
        // wrap range object as simple map like {min:..., max:...}
        java.util.Map<String,Object> range = new java.util.LinkedHashMap<>();
        if (node.has("min")) range.put("min", extractValue(node.get("min")));
        if (node.has("max")) range.put("max", extractValue(node.get("max")));
        single = range;
      }
      return new PredicateNode(field, op, single, values, negated);
    }
    throw new QofParseException("JSON does not match expected BoolNode or Predicate shape");
  }

  private Object extractValue(JsonNode v) {
    if (v.isTextual()) return v.asText();
    if (v.isNumber()) return v.numberValue();
    if (v.isBoolean()) return v.asBoolean();
    if (v.isNull()) return null;
    if (v.isArray()) {
      List<Object> list = new ArrayList<>();
      for (JsonNode n : v) list.add(extractValue(n));
      return list;
    }
    if (v.isObject()) {
      // convert to simple map
      java.util.Map<String,Object> m = new java.util.LinkedHashMap<>();
      Iterator<String> it = v.fieldNames();
      while (it.hasNext()) {
        String k = it.next();
        m.put(k, extractValue(v.get(k)));
      }
      return m;
    }
    return v.asText();
  }
}
