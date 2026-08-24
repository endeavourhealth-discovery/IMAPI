package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Update extends TTIriRef {

  private String description;
  private List<Query> queries;
  private List<Delete> delete;

  public Update setName(String name) {
    super.setName(name);
    return this;
  }

  public Update setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public List<Query> getMatch() {
    return queries;
  }

  public Update setMatch(List<Query> queries) {
    this.queries = queries;
    return this;
  }

  public Update addMatch(Query query) {
    if (this.queries == null) this.queries = new ArrayList<>();
    this.queries.add(query);
    return this;
  }

  public Update match(Consumer<Query> builder) {
    Query query = new Query();
    addMatch(query);
    builder.accept(query);
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Update setDescription(String description) {
    this.description = description;
    return this;
  }

  public List<Delete> getDelete() {
    return delete;
  }

  public Update setDelete(List<Delete> delete) {
    this.delete = delete;
    return this;
  }

  public Update addDelete(Delete delete) {
    if (this.delete == null) this.delete = new ArrayList<>();
    this.delete.add(delete);
    return this;
  }

  public Update delete(Consumer<Delete> builder) {
    Delete delete = new Delete();
    addDelete(delete);
    builder.accept(delete);
    return this;
  }
}
