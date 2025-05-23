package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.endeavourhealth.imapi.json.TTDocumentDeserializer;
import org.endeavourhealth.imapi.json.TTDocumentSerializer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonSerialize(using = TTDocumentSerializer.class)
@JsonDeserialize(using = TTDocumentDeserializer.class)
public class TTDocument extends TTNode {
  private TTIriRef graph;
  private TTContext context = new TTContext();
  private List<TTEntity> entities;
  private TTIriRef crud;
  private Map<String, String> predicates = new HashMap<>();

  public Map<String, String> getPredicates() {
    return predicates;
  }

  public TTDocument setPredicates(Map<String, String> predicates) {
    this.predicates = predicates;
    return this;
  }

  public TTIriRef getGraph() {
    return graph;
  }

  public TTDocument(TTIriRef defaultGraph) {
    this.graph = defaultGraph;
  }

  public TTDocument() {
  }

  public TTDocument setContext(TTContext context) {
    this.context = context;
    return this;
  }

  @JsonSetter
  public TTDocument setGraph(TTIriRef graph) {
    this.graph = graph;
    return this;
  }

  public List<TTPrefix> getPrefixes() {
    return context.getPrefixes();
  }

  public TTDocument addPrefix(TTPrefix directive) {
    addPrefix(directive.getIri(), directive.getPrefix());
    return this;
  }

  public TTDocument addPrefix(String iri, String prefix) {
    context.add(iri, prefix);
    return this;
  }

  @Override
  public TTDocument set(TTIriRef predicate, TTValue value) {
    super.set(predicate, value);
    return this;
  }

  public List<TTEntity> getEntities() {
    return entities;
  }

  public TTDocument setEntities(List<TTEntity> entities) {
    this.entities = entities;
    return this;
  }

  public TTDocument addEntity(TTEntity entity) {
    if (this.entities == null)
      this.entities = new ArrayList<>();
    entity.setContext(this.context);
    this.entities.add(entity);
    return this;
  }


  public TTContext getContext() {
    return this.context;
  }

  public TTIriRef getCrud() {
    return crud;
  }

  @JsonSetter
  public TTDocument setCrud(TTIriRef crud) {
    this.crud = crud;
    return this;
  }
}
