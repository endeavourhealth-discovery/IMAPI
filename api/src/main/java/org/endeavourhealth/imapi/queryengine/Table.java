package org.endeavourhealth.imapi.queryengine;

public class Table {
  private String iri;
  private String name;
  private String alias;
  private String join;
  private String pk;

  public String getIri() {
    return iri;
  }

  public Table setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public String getName() {
    return name;
  }

  public Table setName(String name) {
    this.name = name;
    return this;
  }

  public String getAlias() {
    return alias;
  }

  public Table setAlias(String alias) {
    this.alias = alias;
    return this;
  }

  public String getJoin() {
    return join;
  }

  public Table setJoin(String join) {
    this.join = join;
    return this;
  }

  public String getPk() {
    return pk;
  }

  public Table setPk(String pk) {
    this.pk = pk;
    return this;
  }
}
