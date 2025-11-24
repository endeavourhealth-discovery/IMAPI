package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;


@JsonPropertyOrder({"parameter", "iri", "variable", "parameter", "name"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Element extends IriLD implements Entailment {
  private String variable;
  private String parameter;
  private boolean ancestorsOf;
  private boolean ancestorsOrSelfOf;
  private boolean descendantsOrSelfOf;
  private boolean descendantsOf;
  private boolean childOrSelfOf;
  private boolean childOf;
  private boolean parentOrSelfOf;
  private boolean parentOf;
  private boolean memberOf;
  @Getter
  private boolean cohort;
  private String nodeRef;
  @Getter
  @Setter
  private boolean invalid;

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

  public boolean isAncestorsOrSelfOf() {
    return ancestorsOrSelfOf;
  }

  public Element setAncestorsOrSelfOf(boolean ancestorsOrSelfOf) {
    this.ancestorsOrSelfOf = ancestorsOrSelfOf;
    return this;
  }

  public boolean isParentOrSelfOf() {
    return parentOrSelfOf;
  }

  public Element setParentOrSelfOf(boolean parentOrSelfOf) {
    this.parentOrSelfOf = parentOrSelfOf;
    return this;
  }

  public boolean isParentOf() {
    return parentOf;
  }

  public Element setParentOf(boolean parentOf) {
    this.parentOf = parentOf;
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

  public static Element iri(String iri) {
    return new Element(iri);
  }

  public static Element iri(String iri, String name) {
    return new Element(iri, name);
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

  public String getVariable() {
    return variable;
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


  public Element setVariable(String variable) {
    this.variable = variable;
    return this;
  }


}
