package org.endeavourhealth.imapi.model.config;

import lombok.Getter;

@Getter
public class Config {

  private Integer dbid;
  private String name;
  private String comment;
  private String data;

  public Config setDbid(Integer dbid) {
    this.dbid = dbid;
    return this;
  }

  public Config setName(String name) {
    this.name = name;
    return this;
  }

  public Config setData(String data) {
    this.data = data;
    return this;
  }

  public Config setComment(String comment) {
    this.comment = comment;
    return this;
  }
}
