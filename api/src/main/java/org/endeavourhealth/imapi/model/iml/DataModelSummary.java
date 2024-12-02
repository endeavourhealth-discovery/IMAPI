package org.endeavourhealth.imapi.model.iml;


import com.fasterxml.jackson.annotation.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DataModelSummary {
  @JsonProperty(defaultValue = "")
  private String name;
  @JsonProperty(value = "iri", required = true)
  @JsonAlias({"@id"})
  private String iri;
  @JsonProperty(defaultValue = "")
  private String description;
  @JsonProperty(required = true)
  private TTIriRef status;
  @JsonProperty(required = true)
  private TTIriRef scheme;
  @JsonProperty(required = true)
  private Set<TTIriRef> type = new HashSet<>();
  Set<TTIriRef> intervalUnit;
  List<TTIriRef> qualifier;



  public Set<TTIriRef> getIntervalUnit() {
    return intervalUnit;
  }

  public DataModelSummary setIntervalUnit(Set<TTIriRef> intervalUnit) {
    this.intervalUnit = intervalUnit;
    return this;
  }
  public DataModelSummary addIntervalUnit (TTIriRef intervalUnit){
      if (this.intervalUnit == null) {
        this.intervalUnit = new HashSet<>();
      }
       this.intervalUnit.add(intervalUnit);
      return this;
    }
  public DataModelSummary intervalUnit (Consumer< TTIriRef > builder) {
      TTIriRef intervalUnit = new TTIriRef();
      addIntervalUnit(intervalUnit);
      builder.accept(intervalUnit);
      return this;
    }


  public List<TTIriRef> getQualifier() {
    return qualifier;
  }

  public DataModelSummary setQualifier(List<TTIriRef> qualifier) {
    this.qualifier = qualifier;
    return this;
  }
  public DataModelSummary addQualifier (TTIriRef qualifier){
      if (this.qualifier == null) {
        this.qualifier = new ArrayList<>();
      }
      this.qualifier.add(qualifier);
      return this;
    }
  public DataModelSummary qualifier (Consumer < TTIriRef > builder) {
      TTIriRef qualifier = new TTIriRef();
      addQualifier(qualifier);
      builder.accept(qualifier);
      return this;
    }


  public String getName() {
    return name;
  }

  public DataModelSummary setName(String name) {
    this.name = name;
    return this;
  }

  public String getIri() {
    return iri;
  }

  public DataModelSummary setIri(String iri) {
    this.iri = iri;
    return this;
  }



  public String getDescription() {
    return description;
  }

  public DataModelSummary setDescription(String description) {
    this.description = description;
    return this;
  }

  public TTIriRef getStatus() {
    return status;
  }

  @JsonSetter
  public DataModelSummary setStatus(TTIriRef status) {
    this.status = status;
    return this;
  }

  public TTIriRef getScheme() {
    return scheme;
  }

  @JsonSetter
  public DataModelSummary setScheme(TTIriRef scheme) {
    this.scheme = scheme;
    return this;
  }

  public Set<TTIriRef> getType() {
    return type;
  }

  public DataModelSummary setType(Set<TTIriRef> type) {
    this.type = type;
    return this;
  }
  public DataModelSummary addType (TTIriRef type){
    if (this.type == null) {
      this.type = new HashSet<>();
    }
    this.type.add(type);
    return this;
  }

  public DataModelSummary addEntityType(TTIriRef entityType) {
    if (this.type == null)
      this.type = new HashSet<>();
    this.type.add(entityType);
    return this;
  }


}

