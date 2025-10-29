package org.endeavourhealth.imapi.transform.qofimq;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Phase 1: JSON validation and structural checks for QOF boolean-query inputs.
 * This performs schema-like validation without external dependencies and emits precise errors
 * with JSON pointer locations.
 */
@Component
public class QofImqValidator {
  private static final Logger LOG = LoggerFactory.getLogger(QofImqValidator.class);

  private static final Set<String> BOOL_OPS = Set.of("AND", "OR", "NOT");
  private static final Set<String> CMP_OPS = Set.of("EQUALS", "IN", "RANGE", "EXISTS", "NOT_EQUALS", "NOT_IN");

  public List<ValidationError> validateFile(Path file) {
    List<ValidationError> errors = new ArrayList<>();
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      JsonNode root = om.readValue(file.toFile(), JsonNode.class);
      validateNode(root, "", file, errors);
    } catch (IOException e) {
      errors.add(new ValidationError(file.toString(), "", "IO error reading JSON: " + e.getMessage()));
    } catch (Exception e) {
      errors.add(new ValidationError(file.toString(), "", "Unexpected error: " + e.getMessage()));
    }
    return errors;
  }

  private void validateNode(JsonNode node, String ptr, Path file, List<ValidationError> errors) {
    if (node == null || node.isNull()) {
      errors.add(new ValidationError(file.toString(), ptr, "Node is null"));
      return;
    }
    if (node.isObject()) {
      ObjectNode obj = (ObjectNode) node;
      // Decide whether this is a BoolNode or Predicate based on presence of keys
      boolean hasOperator = obj.has("operator");
      boolean hasField = obj.has("field") || obj.has("op");
      if (hasOperator) {
        validateBoolNode(obj, ptr, file, errors);
      } else if (hasField) {
        validatePredicate(obj, ptr, file, errors);
      } else {
        errors.add(new ValidationError(file.toString(), ptr, "Object must be a boolean node (operator/operands) or a predicate (field/op)"));
      }
    } else {
      errors.add(new ValidationError(file.toString(), ptr, "Root must be an object"));
    }
  }

  private void validateBoolNode(ObjectNode obj, String ptr, Path file, List<ValidationError> errors) {
    // operator
    JsonNode opNode = obj.get("operator");
    if (opNode == null || !opNode.isTextual()) {
      errors.add(new ValidationError(file.toString(), ptr + "/operator", "operator is required and must be a string"));
    } else {
      String op = opNode.asText();
      if (!BOOL_OPS.contains(op)) {
        errors.add(new ValidationError(file.toString(), ptr + "/operator", "Unsupported boolean operator: " + op));
      }
    }
    // operands
    JsonNode operandsNode = obj.get("operands");
    if (operandsNode == null || !operandsNode.isArray()) {
      errors.add(new ValidationError(file.toString(), ptr + "/operands", "operands is required and must be an array"));
    } else {
      ArrayNode arr = (ArrayNode) operandsNode;
      String op = obj.has("operator") && obj.get("operator").isTextual() ? obj.get("operator").asText() : null;
      int size = arr.size();
      if ("NOT".equals(op)) {
        if (size != 1) {
          errors.add(new ValidationError(file.toString(), ptr + "/operands", "NOT requires exactly 1 operand"));
        }
      } else if ("AND".equals(op) || "OR".equals(op)) {
        if (size < 2) {
          errors.add(new ValidationError(file.toString(), ptr + "/operands", op + " requires at least 2 operands"));
        }
      }
      for (int i = 0; i < size; i++) {
        JsonNode child = arr.get(i);
        validateNode(child, ptr + "/operands/" + i, file, errors);
      }
    }
    // additionalProperties check: warn about unknowns
    Set<String> allowed = Set.of("operator", "operands");
    obj.fieldNames().forEachRemaining(f -> {
      if (!allowed.contains(f)) {
        errors.add(new ValidationError(file.toString(), ptr + "/" + f, "Unknown property on boolean node: " + f));
      }
    });
  }

  private void validatePredicate(ObjectNode obj, String ptr, Path file, List<ValidationError> errors) {
    // field
    if (!obj.has("field") || !obj.get("field").isTextual() || obj.get("field").asText().isBlank()) {
      errors.add(new ValidationError(file.toString(), ptr + "/field", "field is required and must be a non-empty string"));
    }
    // op
    if (!obj.has("op") || !obj.get("op").isTextual()) {
      errors.add(new ValidationError(file.toString(), ptr + "/op", "op is required and must be a string"));
    }
    String op = obj.has("op") && obj.get("op").isTextual() ? obj.get("op").asText() : null;
    if (op != null && !CMP_OPS.contains(op)) {
      errors.add(new ValidationError(file.toString(), ptr + "/op", "Unsupported comparison operator: " + op));
    }

    // Op-specific expectations
    if ("EQUALS".equals(op) || "NOT_EQUALS".equals(op)) {
      if (!obj.has("value") || obj.get("value").isNull()) {
        errors.add(new ValidationError(file.toString(), ptr + "/value", op + " requires 'value'"));
      }
    } else if ("IN".equals(op) || "NOT_IN".equals(op)) {
      if (!obj.has("values") || !obj.get("values").isArray() || obj.get("values").size() == 0) {
        errors.add(new ValidationError(file.toString(), ptr + "/values", op + " requires non-empty 'values' array"));
      }
    } else if ("RANGE".equals(op)) {
      boolean hasMin = obj.has("min") && !obj.get("min").isNull();
      boolean hasMax = obj.has("max") && !obj.get("max").isNull();
      if (!hasMin && !hasMax) {
        errors.add(new ValidationError(file.toString(), ptr + "/min", "RANGE requires 'min' and/or 'max'"));
      }
    } else if ("EXISTS".equals(op)) {
      // EXISTS should not carry value(s)
      if (obj.has("value") || obj.has("values") || obj.has("min") || obj.has("max")) {
        errors.add(new ValidationError(file.toString(), ptr, "EXISTS must not include value/values/min/max"));
      }
    }

    // additionalProperties check: allow known predicate fields
    Set<String> allowed = new HashSet<>(Set.of("field", "op", "value", "values", "min", "max", "negated"));
    obj.fieldNames().forEachRemaining(f -> {
      if (!allowed.contains(f)) {
        errors.add(new ValidationError(file.toString(), ptr + "/" + f, "Unknown property on predicate: " + f));
      }
    });
  }
}
