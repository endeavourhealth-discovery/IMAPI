package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.json.TTLiteralDeserializer;
import org.endeavourhealth.imapi.json.TTLiteralSerializer;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;

import java.io.Serializable;
import java.util.regex.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSerialize(using = TTLiteralSerializer.class)
@JsonDeserialize(using = TTLiteralDeserializer.class)
public class TTLiteralJava implements TTValueJava, Serializable {
  private String value;
  private TTIriRef type;

  // General constructors
  public TTLiteralJava() {
  }

  public TTLiteralJava(String value, TTIriRef type) {
    this.value = value;
    this.type = type;
  }

  public TTLiteralJava(String value, String type) {
    this.value = value;
    this.type = TTIriRefExtensionsKt.iri(new TTIriRef(), type);
  }

  // Type specific constructors
  public TTLiteralJava(String value) {
    this.value = value;
    this.type = null;
  }

  public TTLiteralJava(Boolean value) {
    this.value = value.toString();
    this.type = TTIriRefExtensionsKt.iri(new TTIriRef(), XsdVocab.BOOLEAN);
  }

  public TTLiteralJava(Integer value) {
    this.value = value.toString();
    this.type = TTIriRefExtensionsKt.iri(new TTIriRef(), XsdVocab.INTEGER);
  }

  public TTLiteralJava(Long value) {
    this.value = value.toString();
    this.type = TTIriRefExtensionsKt.iri(new TTIriRef(), XsdVocab.LONG);
  }

  public TTLiteralJava(Pattern value) {
    this.value = value.toString();
    this.type = TTIriRefExtensionsKt.iri(new TTIriRef(), XsdVocab.PATTERN);
  }

  public TTLiteralJava(Object value) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      this.value = om.writeValueAsString(value);
      this.type = TTIriRefExtensionsKt.iri(new TTIriRef(), XsdVocab.STRING);
    }
  }

  // Static helpers
  public static TTLiteralJava literal(String value, TTIriRef type) {
    return new TTLiteralJava(value, type);
  }

  public static TTLiteralJava literal(String value, String type) {
    return new TTLiteralJava(value, type);
  }

  public static TTLiteralJava literal(String value) {
    return new TTLiteralJava(value);
  }

  public static TTLiteralJava literal(Boolean value) {
    return new TTLiteralJava(value);
  }

  public static TTLiteralJava literal(Integer value) {
    return new TTLiteralJava(value);
  }

  public static TTLiteralJava literal(Long value) {
    return new TTLiteralJava(value);
  }

  public static TTLiteralJava literal(Pattern value) {
    return new TTLiteralJava(value);
  }

  public static TTLiteralJava literal(Object value) throws JsonProcessingException {
    return new TTLiteralJava(value);
  }

  public static TTLiteralJava literal(JsonNode node) {
    if (!node.isValueNode())
      throw new IllegalStateException("Only value Json nodes currently handled");

    if (node.isBoolean())
      return literal(node.booleanValue());
    else if (node.isLong())
      return literal(node.longValue());
    else if (node.isInt())
      return literal(node.intValue());
    else
      return literal(node.textValue());
  }

  public String getValue() {
    return value;
  }

  public TTLiteralJava setValue(String value) {
    this.value = value;
    return this;
  }

  // Type specific getters
  public Boolean booleanValue() {
    return Boolean.parseBoolean(this.value);
  }

  public Integer intValue() {
    return Integer.parseInt(this.value);
  }

  public Long longValue() {
    return Long.parseLong(this.value);
  }

  public Pattern patternValue() {
    return Pattern.compile(this.value);
  }

  public <T> T objectValue(Class<T> valueType) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      return om.readValue(this.value, valueType);
    }
  }

  public TTIriRef getType() {
    return type;
  }

  @JsonSetter
  public TTLiteralJava setType(TTIriRef type) {
    this.type = type;
    return this;
  }

  @Override
  public TTLiteralJava asLiteral() {
    return this;
  }

  @Override
  @JsonIgnore
  public boolean isLiteral() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TTLiteralJava v = (TTLiteralJava) o;

    if (value == null && v.value != null) return false;
    if (value != null && !value.equals(v.value)) return false;

    if (type == null && v.type != null) return false;

    return type == null || type.equals(v.type);
  }

  @Override
  public int hashCode() {
    String toHash = "";
    if (value != null)
      toHash += value;
    if (type != null)
      toHash += type.getIri();
    return toHash.hashCode();
  }
}
