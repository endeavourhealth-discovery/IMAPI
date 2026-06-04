package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.json.TTEntityDeserializer;
import org.endeavourhealth.imapi.json.TTEntitySerializer;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@JsonSerialize(using = TTEntitySerializer.class)
@JsonDeserialize(using = TTEntityDeserializer.class)
public class TTEntity extends TTNode implements Serializable {
  private TTContext context = new TTContext();
  private TTIriRefExtended crud;

  public TTEntity() {
  }

  public TTEntity(String iri) {

    super.setIri(iri);
  }

  public TTEntity setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public String getName() {
    TTLiteral literal = getAsLiteral(new TTIriRefExtended(RdfsVocab.LABEL));
    return (literal == null) ? null : literal.getValue();
  }

  // Utility methods for common predicates
  public TTEntity setName(String name) {
    set(new TTIriRefExtended(RdfsVocab.LABEL), TTLiteral.literal(name));
    return this;
  }

  public String getPreferredName() {
    TTLiteral literal = getAsLiteral(new TTIriRefExtended(ImVocab. PREFERRED_NAME));
    return (literal == null) ? null : literal.getValue();
  }

  public String getBestMatch() {
    TTLiteral literal = getAsLiteral(new TTIriRefExtended(ImVocab. BEST_MATCH));
    return (literal == null) ? null : literal.getValue();
  }

  public Integer getUsageTotal() {
    TTLiteral literal = getAsLiteral(new TTIriRefExtended(ImVocab. USAGE_TOTAL));
    return (literal == null) ? null : literal.getValue() == null ? null : literal.intValue();
  }

  public int getVersion() {
    TTLiteral literal = getAsLiteral(new TTIriRefExtended(ImVocab. VERSION));
    return (literal == null) ? 1 : literal.intValue();
  }

  public TTEntity setVersion(int version) {
    set(new TTIriRefExtended(ImVocab. VERSION),TTLiteral.literal(version));
    return this;
  }

  public String getDescription() {
    TTLiteral literal = getAsLiteral(new TTIriRefExtended(RdfsVocab.COMMENT));
    return (literal == null) ? null : literal.getValue();
  }

  public TTEntity setDescription(String description) {
    if (description == null)
      getPredicateMap().remove(new TTIriRefExtended(RdfsVocab.COMMENT));
    else
      set(new TTIriRefExtended(RdfsVocab.COMMENT), TTLiteral.literal(description));
    return this;
  }

  public String getCode() {
    TTLiteral literal = getAsLiteral(new TTIriRefExtended(ImVocab. CODE));
    return (literal == null) ? null : literal.getValue();
  }

  public TTEntity setCode(String code) {
    set(new TTIriRefExtended(ImVocab. CODE),TTLiteral.literal(code));
    return this;
  }

  public TTIriRefExtended getScheme() {
    return this.getAsIriRef(new TTIriRefExtended(ImVocab. HAS_SCHEME));
  }

  @JsonSetter
  public TTEntity setScheme(TTIriRefExtended scheme) {
    set(new TTIriRefExtended(ImVocab. HAS_SCHEME),scheme);
    return this;
  }

  public TTEntity addType(TTIriRefExtended type) {
    TTArray types;
    if (has(new TTIriRefExtended(RdfVocab.TYPE))) {
      types = get(new TTIriRefExtended(RdfVocab.TYPE));
    } else {
      types = new TTArray();
      setType(types);
    }
    types.add(type);
    return this;
  }

  public boolean isType(TTIriRefExtended type) {
    if (this.getType() != null) {
      return this.getType().contains(type);
    }
    return false;
  }

  public TTArray getType() {
    if (get(new TTIriRefExtended(RdfVocab.TYPE)) == null)
      return null;
    else
      return get(new TTIriRefExtended(RdfVocab.TYPE));
  }

  public TTEntity setType(TTArray type) {
    set(new TTIriRefExtended(RdfVocab.TYPE), type);
    return this;
  }

  public Set<TTIriRefExtended> getTypes() {
    TTArray types = getType();
    if (types == null)
      return null;
    return types.getElements().stream().map(TTValue::asIriRef).collect(java.util.stream.Collectors.toSet());
  }


  public TTIriRefExtended getStatus() {
    return this.getAsIriRef(new TTIriRefExtended(ImVocab. HAS_STATUS));
  }

  @JsonSetter
  public TTEntity setStatus(TTIriRefExtended status) {
    set(new TTIriRefExtended(ImVocab. HAS_STATUS),status);
    return this;
  }

  public List<TTPrefix> getPrefixes() {
    return context.getPrefixes();
  }

  public TTEntity addPrefix(String iri, String prefix) {
    context.add(iri, prefix);
    return this;
  }

  @Override
  public TTEntity set(TTIriRefExtended predicate, TTValue value) {
    super.set(predicate, value);
    return this;
  }

  @Override
  public TTEntity set(TTIriRefExtended predicate, TTArray value) {
    super.set(predicate, value);
    return this;
  }

  @Override
  public TTEntity set(TTIriRefExtended predicate, Integer value) {
    super.set(predicate, value);
    return this;
  }

  @Override
  public TTEntity set(TTIriRefExtended predicate, Long value) {
    super.set(predicate, value);
    return this;
  }

  @Override
  public TTEntity set(TTIriRefExtended predicate, boolean value) {
    super.set(predicate, value);
    return this;
  }

  @Override
  public TTEntity addObject(TTIriRefExtended predicate, TTValue object) {
    super.addObject(predicate, object);
    return this;
  }

  @Override
  public TTEntity removeObject(TTIriRefExtended predicate) {
    super.removeObject(predicate);
    return this;
  }

  public TTContext getContext() {
    return context;
  }

  public TTEntity setContext(TTContext context) {
    this.context = context;
    return this;
  }

  public TTIriRefExtended getCrud() {
    return crud;
  }

  @JsonSetter
  public TTEntity setCrud(TTIriRefExtended crud) {
    this.crud = crud;
    return this;
  }
}
