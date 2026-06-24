package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.json.TTEntityDeserializer;
import org.endeavourhealth.imapi.json.TTEntitySerializer;
import org.endeavourhealth.imapi.model.extensions.TTIriRefExtensionsKt;
import org.endeavourhealth.interfacemanager.model.ImVocab;
import org.endeavourhealth.interfacemanager.model.RdfVocab;
import org.endeavourhealth.interfacemanager.model.RdfsVocab;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@JsonSerialize(using = TTEntitySerializer.class)
@JsonDeserialize(using = TTEntityDeserializer.class)
public class TTEntity extends TTNode implements Serializable {
  private TTContext context = new TTContext();
  private TTIriRef crud;

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
    TTLiteral literal = getAsLiteral(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL));
    return (literal == null) ? null : literal.getValue();
  }

  // Utility methods for common predicates
  public TTEntity setName(String name) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL), TTLiteral.literal(name));
    return this;
  }

  public String getPreferredName() {
    TTLiteral literal = getAsLiteral(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PREFERRED_NAME));
    return (literal == null) ? null : literal.getValue();
  }

  public String getBestMatch() {
    TTLiteral literal = getAsLiteral(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.BEST_MATCH));
    return (literal == null) ? null : literal.getValue();
  }

  public Integer getUsageTotal() {
    TTLiteral literal = getAsLiteral(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.USAGE_TOTAL));
    return (literal == null) ? null : literal.getValue() == null ? null : literal.intValue();
  }

  public int getVersion() {
    TTLiteral literal = getAsLiteral(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.VERSION));
    return (literal == null) ? 1 : literal.intValue();
  }

  public TTEntity setVersion(int version) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.VERSION), TTLiteral.literal(version));
    return this;
  }

  public String getDescription() {
    TTLiteral literal = getAsLiteral(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.COMMENT));
    return (literal == null) ? null : literal.getValue();
  }

  public TTEntity setDescription(String description) {
    if (description == null)
      getPredicateMap().remove(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.COMMENT));
    else
      set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.COMMENT), TTLiteral.literal(description));
    return this;
  }

  public String getCode() {
    TTLiteral literal = getAsLiteral(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CODE));
    return (literal == null) ? null : literal.getValue();
  }

  public TTEntity setCode(String code) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CODE), TTLiteral.literal(code));
    return this;
  }

  public TTIriRef getScheme() {
    return this.getAsIriRef(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_SCHEME));
  }

  @JsonSetter
  public TTEntity setScheme(TTIriRef scheme) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_SCHEME), scheme);
    return this;
  }

  public TTEntity addType(TTIriRef type) {
    TTArray types;
    if (has(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE))) {
      types = get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE));
    } else {
      types = new TTArray();
      setType(types);
    }
    types.add(type);
    return this;
  }

  public boolean isType(TTIriRef type) {
    if (this.getType() != null) {
      return this.getType().contains(type);
    }
    return false;
  }

  public TTArray getType() {
    if (get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE)) == null)
      return null;
    else
      return get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE));
  }

  public TTEntity setType(TTArray type) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE), type);
    return this;
  }

  public Set<TTIriRef> getTypes() {
    TTArray types = getType();
    if (types == null)
      return null;
    return types.getElements().stream().map(TTValue::asIriRef).collect(java.util.stream.Collectors.toSet());
  }


  public TTIriRef getStatus() {
    return this.getAsIriRef(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_STATUS));
  }

  @JsonSetter
  public TTEntity setStatus(TTIriRef status) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_STATUS), status);
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
  public TTEntity set(TTIriRef predicate, TTValue value) {
    super.set(predicate, value);
    return this;
  }

  public TTEntity set(TTIriRef predicate, TTIriRef value) {
    super.set(predicate, value);
    return this;
  }

  @Override
  public TTEntity set(TTIriRef predicate, TTArray value) {
    super.set(predicate, value);
    return this;
  }

  @Override
  public TTEntity set(TTIriRef predicate, Integer value) {
    super.set(predicate, value);
    return this;
  }

  @Override
  public TTEntity set(TTIriRef predicate, Long value) {
    super.set(predicate, value);
    return this;
  }

  @Override
  public TTEntity set(TTIriRef predicate, boolean value) {
    super.set(predicate, value);
    return this;
  }

  @Override
  public TTEntity addObject(TTIriRef predicate, TTValue object) {
    super.addObject(predicate, object);
    return this;
  }

  @Override
  public TTEntity removeObject(TTIriRef predicate) {
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

  public TTIriRef getCrud() {
    return crud;
  }

  @JsonSetter
  public TTEntity setCrud(TTIriRef crud) {
    this.crud = crud;
    return this;
  }
}
