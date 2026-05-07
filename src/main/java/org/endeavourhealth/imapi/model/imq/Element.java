package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;


@JsonPropertyOrder({"parameter", "iri", "parameter", "name"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public abstract class Element extends IriLD implements Entailment {
  private String parameter;
  private boolean ancestorsOf;
  private boolean descendantsOrSelfOf;
  private boolean descendantsOf;
  private boolean childOrSelfOf;
  private boolean childOf;
  private boolean memberOf;
  @Getter
  private boolean cohort;
  private String nodeRef;
  @Getter
  @Setter
  private boolean invalid;
  private boolean resultSet;

  public boolean getResultSet(){
    return resultSet;
  }

  public Element setIsResultSet(boolean resultSet){
    this.resultSet = resultSet;
    return this;
  }


  public Element setIsCohort(boolean cohort) {
    this.cohort= cohort;
    return this;
  }
  public boolean isMemberOf() {
    return memberOf;
  }

  public Element setMemberOf(boolean memberOf) {
    this.memberOf = memberOf;
    return this;
  }


  public String getNodeRef() {
    return nodeRef;
  }

  public boolean isChildOrSelfOf() {
    return childOrSelfOf;
  }

  public Element setChildOrSelfOf(boolean childOrSelfOf) {
    this.childOrSelfOf = childOrSelfOf;
    return this;
  }

  public boolean isChildOf() {
    return childOf;
  }

  public Element setChildOf(boolean childOf) {
    this.childOf = childOf;
    return this;
  }

  public Element setNodeRef(String nodeRef) {
    this.nodeRef = nodeRef;
    return this;
  }


  public Element() {
  }

  public Element(String iri) {
    setIri(iri);
  }

  public Element(String iri, String name) {
    setIri(iri);
    setName(name);
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


  public boolean isAncestorsOf() {
    return ancestorsOf;
  }


  public Element setAncestorsOf(boolean ancestorsOf) {
    this.ancestorsOf = ancestorsOf;
    return this;
  }


  public boolean isDescendantsOrSelfOf() {
    return descendantsOrSelfOf;
  }


  public Element setDescendantsOrSelfOf(boolean descendantsOrSelfOf) {
    this.descendantsOrSelfOf = descendantsOrSelfOf;
    return this;
  }


  public boolean isDescendantsOf() {
    return descendantsOf;
  }

  public Element setDescendantsOf(boolean descendantsOf) {
    this.descendantsOf = descendantsOf;
    return this;
  }





}
