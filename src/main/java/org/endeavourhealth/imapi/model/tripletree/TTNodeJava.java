package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.endeavourhealth.imapi.json.TTNodeDeserializerV2;
import org.endeavourhealth.imapi.json.TTNodeSerializerV2;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonSerialize(using = TTNodeSerializerV2.class)
@JsonDeserialize(using = TTNodeDeserializerV2.class)
public class TTNodeJava implements TTValueJava, Serializable {
  private Map<TTIriRef, TTArrayJava> predicateValues = new HashMap<>();
  @Getter
  private String iri;

  public TTNodeJava setIri(String iri) {
    if (iri != null && iri.startsWith("null"))
      System.err.println("Its here!!!!");

    this.iri = iri;
    return this;
  }

  @JsonSetter
  public TTNodeJava set(TTIriRef predicate, TTValueJava value) {
    if (value == null)
      predicateValues.remove(predicate);
    else
      predicateValues.put(predicate, new TTArrayJava().add(value));
    return this;
  }

  @JsonIgnore
  public TTNodeJava set(TTIriRef predicate, String value) {
    if (value.startsWith("http:"))
      this.set(predicate, TTIriRefExtensionsKt.iri(new TTIriRef(), value));
    else
      this.set(predicate, TTLiteralJava.literal(value));
    return this;
  }

  @JsonIgnore
  public TTNodeJava set(TTIriRef predicate, Integer value) {
    this.set(predicate, TTLiteralJava.literal(value));
    return this;
  }

  @JsonIgnore
  public TTNodeJava set(TTIriRef predicate, boolean value) {
    this.set(predicate, TTLiteralJava.literal(value));
    return this;
  }

  @JsonIgnore
  public TTNodeJava set(TTIriRef predicate, Long value) {
    this.set(predicate, TTLiteralJava.literal(value));
    return this;
  }

  @JsonSetter
  public TTNodeJava set(TTIriRef predicate, TTArrayJava value) {
    predicateValues.put(predicate, value);
    return this;
  }

  @JsonIgnore
  public TTNodeJava set(String predicate, TTValueJava value) {
    this.set(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate), value);
    return this;
  }

  @JsonIgnore
  public TTNodeJava set(Enum<?> predicate, TTValueJava value) {
    this.set(EnumUtils.asIri(predicate), value);
    return this;
  }


  @JsonIgnore
  public TTNodeJava set(String predicate, boolean value) {
    this.set(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate), value);
    return this;
  }

  @JsonIgnore
  public TTArrayJava get(String predicate) {
    return predicateValues.get(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate));
  }

  @JsonIgnore
  public TTArrayJava get(Enum<?> predicate) {
    return predicateValues.get(EnumUtils.asIri(predicate));
  }

  @JsonGetter
  public TTArrayJava get(TTIriRef predicate) {
    return predicateValues.get(predicate);
  }

  public boolean has(TTIriRef predicate) {
    return predicateValues.containsKey(predicate);
  }

  public boolean has(Enum<?> predicate) {
    return predicateValues.containsKey(EnumUtils.asIri(predicate));
  }

  public Map<TTIriRef, TTArrayJava> getPredicateMap() {
    return this.predicateValues;
  }

  public TTNodeJava setPredicateMap(Map<TTIriRef, TTArrayJava> predicateMap) {
    this.predicateValues = predicateMap;
    return this;
  }

  @Override
  public TTNodeJava asNode() {
    return this;
  }

  @Override
  @JsonIgnore
  public boolean isNode() {
    return true;
  }

  @JsonGetter
  public TTLiteralJava getAsLiteral(TTIriRef predicate) {
    TTArrayJava vals = get(predicate);
    return (vals == null) ? null : vals.asLiteral();
  }

  @JsonGetter
  public TTIriRef getAsIriRef(TTIriRef predicate) {
    TTArrayJava vals = get(predicate);
    return (vals == null) ? null : vals.asIriRef();
  }

  @JsonGetter
  public TTNodeJava getAsNode(TTIriRef predicate) {
    TTArrayJava vals = get(predicate);
    return (vals == null) ? null : vals.asNode();
  }

  /**
   * Adds an object to a predicate if necessary converting to an array if not already an array
   *
   * @param predicate the predicate to add the object to. This may or may not already exist
   * @return the modified node with a predicate object as an array
   */

  public TTNodeJava addObject(TTIriRef predicate, TTValueJava object) {
    if (this.get(predicate) == null)
      this.set(predicate, new TTArrayJava().add(object));
    else
      this.get(predicate).add(object);
    return this;
  }

  /**
   * Adds a String or string iri to a predicate if necessary converting to an array if not already an array
   *
   * @param predicate the predicate to add the object to. This may or may not already exist
   * @return the modified node with a predicate object as an array
   */

  public TTNodeJava addObject(TTIriRef predicate, String value) {
    if (value.startsWith("http:"))
      this.addObject(predicate, TTIriRefExtensionsKt.iri(new TTIriRef(), value));
    else
      this.addObject(predicate, TTLiteralJava.literal(value));
    return this;
  }

  /**
   * Adds an integer value to a predicate if necessary converting to an array if not already an array
   *
   * @param predicate the predicate to add the object to. This may or may not already exist
   * @return the modified node with a predicate object as an array
   */

  public TTNodeJava addObject(TTIriRef predicate, Integer value) {
    this.addObject(predicate, TTLiteralJava.literal(value));
    return this;
  }

  /**
   * Adds an integer value to a predicate if necessary converting to an array if not already an array
   *
   * @param predicate the predicate to add the object to. This may or may not already exist
   * @return the modified node with a predicate object as an array
   */

  public TTNodeJava addObject(TTIriRef predicate, boolean value) {
    this.addObject(predicate, TTLiteralJava.literal(value));
    return this;
  }

  /**
   * Adds an integer value to a predicate if necessary converting to an array if not already an array
   *
   * @param predicate the predicate to add the object to. This may or may not already exist
   * @return the modified node with a predicate object as an array
   */

  public TTNodeJava addObject(TTIriRef predicate, Long value) {
    this.addObject(predicate, TTLiteralJava.literal(value));
    return this;
  }

  public TTNodeJava removeObject(TTIriRef predicate) {
    if (this.get(predicate) != null) {
      this.predicateValues.remove(predicate);
    }
    return this;
  }

}
