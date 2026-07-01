package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.json.TTDocumentDeserializer;
import org.endeavourhealth.imapi.json.TTDocumentSerializer;
import org.endeavourhealth.interfacemanager.model.TTIriRef;
import org.endeavourhealth.interfacemanager.model.TTPrefix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonSerialize(using = TTDocumentSerializer.class)
@JsonDeserialize(using = TTDocumentDeserializer.class)
public class TTDocumentJava extends TTNodeJava {
  private TTContextJava context = new TTContextJava();
  private List<TTEntityJava> entities;
  private TTIriRef crud;
  private Map<String, String> predicates = new HashMap<>();

  public Map<String, String> getPredicates() {
    return predicates;
  }

  public TTDocumentJava setPredicates(Map<String, String> predicates) {
    this.predicates = predicates;
    return this;
  }

  public List<TTPrefix> getPrefixes() {
    return context.getPrefixes();
  }

  public TTDocumentJava addPrefix(TTPrefix directive) {
    addPrefix(directive.getIri(), directive.getPrefix());
    return this;
  }

  public TTDocumentJava addPrefix(String iri, String prefix) {
    context.add(iri, prefix);
    return this;
  }

  @Override
  public TTDocumentJava set(TTIriRef predicate, TTValueJava value) {
    super.set(predicate, value);
    return this;
  }

  public List<TTEntityJava> getEntities() {
    return entities;
  }

  public TTDocumentJava setEntities(List<TTEntityJava> entities) {
    this.entities = entities;
    return this;
  }

  public TTDocumentJava addEntity(TTEntityJava entity) {
    if (this.entities == null)
      this.entities = new ArrayList<>();
    entity.setContext(this.context);
    this.entities.add(entity);
    return this;
  }

  public TTContextJava getContext() {
    return this.context;
  }

  public TTDocumentJava setContext(TTContextJava context) {
    this.context = context;
    return this;
  }

  public TTIriRef getCrud() {
    return crud;
  }

  @JsonSetter
  public TTDocumentJava setCrud(TTIriRef crud) {
    this.crud = crud;
    return this;
  }
}
