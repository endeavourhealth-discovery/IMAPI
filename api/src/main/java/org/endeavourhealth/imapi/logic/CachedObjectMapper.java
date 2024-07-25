package org.endeavourhealth.imapi.logic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class CachedObjectMapper implements AutoCloseable {
  private static final Stack<ObjectMapper> pool = new Stack<>();

  private ObjectMapper objectMapper;

  public CachedObjectMapper() {
    objectMapper = pop();
  }

  @Override
  public void close() {
    objectMapper.setSerializationInclusion(JsonInclude.Include.USE_DEFAULTS);
    push(objectMapper);
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


  private void push(ObjectMapper objectMapper) {
    synchronized (pool) {
      pool.push(objectMapper);
    }
  }

  private ObjectMapper pop() {
    synchronized (pool) {
      if (pool.size() > 0)
        return pool.pop();
      else
        return new ObjectMapper();
    }
  }

  public void setSerializationInclusion(JsonInclude.Include incl) {
    objectMapper.setSerializationInclusion(incl);
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
