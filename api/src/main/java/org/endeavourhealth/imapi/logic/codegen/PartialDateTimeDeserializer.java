package org.endeavourhealth.imapi.logic.codegen;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.SneakyThrows;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class PartialDateTimeDeserializer extends StdDeserializer<PartialDateTime> {

  protected PartialDateTimeDeserializer() {
    this(null);
  }

  protected PartialDateTimeDeserializer(Class<?> vc) {
    super(vc);
  }

  @SneakyThrows
  @Override
  public PartialDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

    JsonNode node = p.getCodec().readTree(p);
    String isoDateTime = node.get("dateTime").textValue();
    String precision = node.get("precision").textValue();

    return new PartialDateTime(OffsetDateTime.parse(isoDateTime), Precision.valueOf(precision));
  }

  private Object getNodeValue(JsonNode value, JsonParser p, DeserializationContext ctxt) throws IOException {

    switch (value.getNodeType()) {
      case ARRAY:
        return getArrayNode((ArrayNode) value, p, ctxt);
      case BINARY:
        return value.binaryValue();
      case BOOLEAN:
        return value.booleanValue();
      case MISSING:
        throw new UnsupportedOperationException("Missing nodes unsupported");
      case NULL:
        return null;
      case NUMBER:
        return getNumberNode(value);
      case OBJECT:
        return ctxt.readValue(value.traverse(p.getCodec()), IMDMBase.class);
      case POJO:
        throw new UnsupportedOperationException("POJO nodes unsupported");
      case STRING:
        return value.textValue();
    }
    return null;
  }

  private List<Object> getArrayNode(ArrayNode value, JsonParser p, DeserializationContext ctxt) throws IOException {
    List<Object> result = new ArrayList<>();

    for (JsonNode node : value) {
      result.add(getNodeValue(node, p, ctxt));
    }

    return result;
  }

  private Object getNumberNode(JsonNode value) {
    if (value.isInt()) {
      return value.intValue();
    } else if (value.isLong()) {
      return value.longValue();
    } else if (value.isDouble()) {
      return value.doubleValue();
    } else {
      throw new UnsupportedOperationException("Unsupported number type");
    }
  }
}
