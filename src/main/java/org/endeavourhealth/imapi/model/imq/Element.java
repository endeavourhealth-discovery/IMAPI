package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({"parameter", "iri", "parameter", "name"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public abstract class Element extends IriLD implements Entailment {

  private String parameter;
  private Entail entails;

  public Entail getEntails(){
    return entails;
  }
  public Element setEntails(Entail entails){
    this.entails = entails;
    return this;
  }
  @Getter
  private boolean cohort;

  private String nodeRef;

  @Getter
  @Setter
  private boolean invalid;

  private boolean resultSet;

  public Element() {
  }

  public Element(String iri) {
    setIri(iri);
  }

  public Element(String iri, String name) {
    setIri(iri);
    setName(name);
  }

  public boolean getResultSet() {
    return resultSet;
  }

  public Element setIsResultSet(boolean resultSet) {
    this.resultSet = resultSet;
    return this;
  }

  public Element setCohort(boolean cohort) {
    this.cohort = cohort;
    return this;
  }

  public String getNodeRef() {
    return nodeRef;
  }

  public Element setNodeRef(String nodeRef) {
    this.nodeRef = nodeRef;
    return this;
  }



  public Element setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public Element setName(String name) {
    super.setName(name);
    return this;
  }

  public String getParameter() {
    return parameter;
  }

  public Element setParameter(String parameter) {
    this.parameter = parameter;
    return this;
  }



}
