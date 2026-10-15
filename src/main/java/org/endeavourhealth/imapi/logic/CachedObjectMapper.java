package org.endeavourhealth.imapi.logic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

/**
 * Facade over shared, never-reconfigured ObjectMappers (one per inclusion setting; default NON_EMPTY).
 * ObjectMapper is thread-safe once configured, so instances are no longer pooled or borrowed. Calling
 * setSerializationInclusion switches this instance to the shared mapper for that inclusion rather than
 * mutating a mapper that other callers use. close() is kept so existing try-with-resources callers still compile.
 */
public class CachedObjectMapper implements AutoCloseable {

  private static final Map<JsonInclude.Include, ObjectMapper> MAPPERS = new ConcurrentHashMap<>();

  private ObjectMapper objectMapper = forInclusion(JsonInclude.Include.NON_EMPTY);

  private static ObjectMapper forInclusion(JsonInclude.Include incl) {
    return MAPPERS.computeIfAbsent(incl, i -> {
      ObjectMapper om = new ObjectMapper();
      om.setDefaultPropertyInclusion(i);
      return om;
    });
  }

  @Override
  public void close() {
    // nothing to release: the underlying mappers are shared
  }

  public <T> T readValue(String content, Class<T> valueType) throws JsonProcessingException {
    return objectMapper.readValue(content, valueType);
  }

  public <T> T readValue(String content, TypeReference<T> typeReference) throws JsonProcessingException {
    return objectMapper.readValue(content, typeReference);
  }

  public JsonNode readTree(String content) throws JsonProcessingException {
    return objectMapper.readTree(content);
  }

  public String writeValueAsString(Object value) throws JsonProcessingException {
    return objectMapper.writeValueAsString(value);
  }

  public void setSerializationInclusion(JsonInclude.Include incl) {
    objectMapper = forInclusion(incl);
  }

  public ObjectWriter writerWithDefaultPrettyPrinter() {
    return objectMapper.writerWithDefaultPrettyPrinter();
  }

  public ObjectNode createObjectNode() {
    return objectMapper.createObjectNode();
  }

  public ArrayNode createArrayNode() {
    return objectMapper.createArrayNode();
  }

  public JsonNode valueToTree(List<TTIriRef> fromValue) {
    return objectMapper.valueToTree(fromValue);
  }

  public JsonNode stringArrayToTree(List<String> fromValue) {
    return objectMapper.valueToTree(fromValue);
  }

  public <T> T treeToValue(JsonNode source, Class<T> valueType) throws JsonProcessingException {
    return objectMapper.treeToValue(source, valueType);
  }

  public <T> T readValue(File inputFile, Class<T> valueType) throws IOException {
    return objectMapper.readValue(inputFile, valueType);
  }
}
