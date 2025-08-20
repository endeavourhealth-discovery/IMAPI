package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.endeavourhealth.imapi.json.TTNodeDeserializerV2;
import org.endeavourhealth.imapi.json.TTNodeSerializerV2;
import org.endeavourhealth.imapi.vocabulary.VocabEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@JsonSerialize(using = TTNodeSerializerV2.class)
@JsonDeserialize(using = TTNodeDeserializerV2.class)
public class TTNode implements TTValue, Serializable {
  private Map<TTIriRef, TTArray> predicateValues = new HashMap<>();
  @Getter
  private String iri;

  public TTNode setIri(String iri) {
    if (iri != null && iri.startsWith("null"))
      System.err.println("Its here!!!!");

    this.iri = iri;
    return this;
  }

  @JsonSetter
  public TTNode set(TTIriRef predicate, TTValue value) {
    if (value == null)
      predicateValues.remove(predicate);
    else
      predicateValues.put(predicate, new TTArray().add(value));
    return this;
  }

  @JsonIgnore
  public TTNode set(TTIriRef predicate, String value) {
    if (value.startsWith("http:"))
      this.set(predicate, iri(value));
    else
      this.set(predicate, TTLiteral.literal(value));
    return this;
  }

  @JsonIgnore
  public TTNode set(TTIriRef predicate, Integer value) {
    this.set(predicate, TTLiteral.literal(value));
    return this;
  }

  @JsonIgnore
  public TTNode set(TTIriRef predicate, boolean value) {
    this.set(predicate, TTLiteral.literal(value));
    return this;
  }

  @JsonIgnore
  public TTNode set(TTIriRef predicate, Long value) {
    this.set(predicate, TTLiteral.literal(value));
    return this;
  }

  @JsonSetter
  public TTNode set(TTIriRef predicate, TTArray value) {
    predicateValues.put(predicate, value);
    return this;
  }

  @JsonIgnore
  public TTNode set(String predicate, TTValue value) {
    this.set(iri(predicate), value);
    return this;
  }

  @JsonIgnore
  public TTNode set(VocabEnum predicate, TTValue value) {
    this.set(predicate.asIri(), value);
    return this;
  }


  @JsonIgnore
  public TTNode set(String predicate, boolean value) {
    this.set(iri(predicate), value);
    return this;
  }

  @JsonIgnore
  public TTArray get(String predicate) {
    return predicateValues.get(iri(predicate));
  }

  @JsonIgnore
  public TTArray get(VocabEnum predicate) {
    return predicateValues.get(predicate.asIri());
  }

  @JsonGetter
  public TTArray get(TTIriRef predicate) {
    return predicateValues.get(predicate);
  }

  public boolean has(TTIriRef predicate) {
    return predicateValues.containsKey(predicate);
  }

  public boolean has(VocabEnum predicate) {
    return predicateValues.containsKey(predicate.asIri());
  }

  public Map<TTIriRef, TTArray> getPredicateMap() {
    return this.predicateValues;
  }

  public TTNode setPredicateMap(Map<TTIriRef, TTArray> predicateMap) {
    this.predicateValues = predicateMap;
    return this;
  }

  @Override
  public TTNode asNode() {
    return this;
  }

  @Override
  @JsonIgnore
  public boolean isNode() {
    return true;
  }

  @JsonGetter
  public TTLiteral getAsLiteral(TTIriRef predicate) {
    TTArray vals = get(predicate);
    return (vals == null) ? null : vals.asLiteral();
  }

  @JsonGetter
  public TTIriRef getAsIriRef(TTIriRef predicate) {
    TTArray vals = get(predicate);
    return (vals == null) ? null : vals.asIriRef();
  }

  @JsonGetter
  public TTNode getAsNode(TTIriRef predicate) {
    TTArray vals = get(predicate);
    return (vals == null) ? null : vals.asNode();
  }

  /**
   * Adds an object to a predicate if necessary converting to an array if not already an array
   *
   * @param predicate the predicate to add the object to. This may or may not already exist
   * @return the modified node with a predicate object as an array
   */

  public TTNode addObject(TTIriRef predicate, TTValue object) {
    if (this.get(predicate) == null)
      this.set(predicate, new TTArray().add(object));
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

  public TTNode addObject(TTIriRef predicate, String value) {
    if (value.startsWith("http:"))
      this.addObject(predicate, iri(value));
    else
      this.addObject(predicate, TTLiteral.literal(value));
    return this;
  }

  /**
   * Adds an integer value to a predicate if necessary converting to an array if not already an array
   *
   * @param predicate the predicate to add the object to. This may or may not already exist
   * @return the modified node with a predicate object as an array
   */

  public TTNode addObject(TTIriRef predicate, Integer value) {
    this.addObject(predicate, TTLiteral.literal(value));
    return this;
  }

  /**
   * Adds an integer value to a predicate if necessary converting to an array if not already an array
   *
   * @param predicate the predicate to add the object to. This may or may not already exist
   * @return the modified node with a predicate object as an array
   */

  public TTNode addObject(TTIriRef predicate, boolean value) {
    this.addObject(predicate, TTLiteral.literal(value));
    return this;
  }

  /**
   * Adds an integer value to a predicate if necessary converting to an array if not already an array
   *
   * @param predicate the predicate to add the object to. This may or may not already exist
   * @return the modified node with a predicate object as an array
   */

  public TTNode addObject(TTIriRef predicate, Long value) {
    this.addObject(predicate, TTLiteral.literal(value));
    return this;
  }

  public TTNode removeObject(TTIriRef predicate) {
    if (this.get(predicate) != null) {
      this.predicateValues.remove(predicate);
    }
    return this;
  }

}
