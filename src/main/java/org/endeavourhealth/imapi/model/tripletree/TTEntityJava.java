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
public class TTEntityJava extends TTNodeJava implements Serializable {
  private TTContextJava context = new TTContextJava();
  private TTIriRef crud;

  public TTEntityJava() {
  }

  public TTEntityJava(String iri) {

    super.setIri(iri);
  }

  public TTEntityJava setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public String getName() {
    TTLiteralJava literal = getAsLiteral(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL));
    return (literal == null) ? null : literal.getValue();
  }

  // Utility methods for common predicates
  public TTEntityJava setName(String name) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL), TTLiteralJava.literal(name));
    return this;
  }

  public String getPreferredName() {
    TTLiteralJava literal = getAsLiteral(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PREFERRED_NAME));
    return (literal == null) ? null : literal.getValue();
  }

  public String getBestMatch() {
    TTLiteralJava literal = getAsLiteral(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.BEST_MATCH));
    return (literal == null) ? null : literal.getValue();
  }

  public Integer getUsageTotal() {
    TTLiteralJava literal = getAsLiteral(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.USAGE_TOTAL));
    return (literal == null) ? null : literal.getValue() == null ? null : literal.intValue();
  }

  public int getVersion() {
    TTLiteralJava literal = getAsLiteral(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.VERSION));
    return (literal == null) ? 1 : literal.intValue();
  }

  public TTEntityJava setVersion(int version) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.VERSION), TTLiteralJava.literal(version));
    return this;
  }

  public String getDescription() {
    TTLiteralJava literal = getAsLiteral(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.COMMENT));
    return (literal == null) ? null : literal.getValue();
  }

  public TTEntityJava setDescription(String description) {
    if (description == null)
      getPredicateMap().remove(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.COMMENT));
    else
      set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.COMMENT), TTLiteralJava.literal(description));
    return this;
  }

  public String getCode() {
    TTLiteralJava literal = getAsLiteral(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CODE));
    return (literal == null) ? null : literal.getValue();
  }

  public TTEntityJava setCode(String code) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CODE), TTLiteralJava.literal(code));
    return this;
  }

  public TTIriRef getScheme() {
    return this.getAsIriRef(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_SCHEME));
  }

  @JsonSetter
  public TTEntityJava setScheme(TTIriRef scheme) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_SCHEME), scheme);
    return this;
  }

  public TTEntityJava addType(TTIriRef type) {
    TTArrayJava types;
    if (has(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE))) {
      types = get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE));
    } else {
      types = new TTArrayJava();
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

  public TTArrayJava getType() {
    if (get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE)) == null)
      return null;
    else
      return get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE));
  }

  public TTEntityJava setType(TTArrayJava type) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE), type);
    return this;
  }

  public Set<TTIriRef> getTypes() {
    TTArrayJava types = getType();
    if (types == null)
      return null;
    return types.getElements().stream().map(TTValueJava::asIriRef).collect(java.util.stream.Collectors.toSet());
  }


  public TTIriRef getStatus() {
    return this.getAsIriRef(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_STATUS));
  }

  @JsonSetter
  public TTEntityJava setStatus(TTIriRef status) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_STATUS), status);
    return this;
  }

  public List<TTPrefix> getPrefixes() {
    return context.getPrefixes();
  }

  public TTEntityJava addPrefix(String iri, String prefix) {
    context.add(iri, prefix);
    return this;
  }

  @Override
  public TTEntityJava set(TTIriRef predicate, TTValueJava value) {
    super.set(predicate, value);
    return this;
  }

  public TTEntityJava set(TTIriRef predicate, TTIriRef value) {
    super.set(predicate, value);
    return this;
  }

  @Override
  public TTEntityJava set(TTIriRef predicate, TTArrayJava value) {
    super.set(predicate, value);
    return this;
  }

  @Override
  public TTEntityJava set(TTIriRef predicate, Integer value) {
    super.set(predicate, value);
    return this;
  }

  @Override
  public TTEntityJava set(TTIriRef predicate, Long value) {
    super.set(predicate, value);
    return this;
  }

  @Override
  public TTEntityJava set(TTIriRef predicate, boolean value) {
    super.set(predicate, value);
    return this;
  }

  @Override
  public TTEntityJava addObject(TTIriRef predicate, TTValueJava object) {
    super.addObject(predicate, object);
    return this;
  }

  @Override
  public TTEntityJava removeObject(TTIriRef predicate) {
    super.removeObject(predicate);
    return this;
  }

  public TTContextJava getContext() {
    return context;
  }

  public TTEntityJava setContext(TTContextJava context) {
    this.context = context;
    return this;
  }

  public TTIriRef getCrud() {
    return crud;
  }

  @JsonSetter
  public TTEntityJava setCrud(TTIriRef crud) {
    this.crud = crud;
    return this;
  }
}
