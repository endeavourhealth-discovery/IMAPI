package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


@JsonPropertyOrder({"parameter", "iri", "type", "set", "variable", "qualifier","name"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Node extends Element implements GraphNode{
  private boolean exclude;
  private String code;
  private String type;
  private Path path;
  private boolean inverse;

  public boolean isInverse() {
    return inverse;
  }

  public Node setInverse(boolean inverse) {
    this.inverse = inverse;
    return this;
  }

  public Path getPath() {
    return path;
  }

  public Node setPath(Path path) {
    this.path = path;
    return this;
  }

  public Node path (Consumer< Path > builder) {
    this.path= new Path();
    builder.accept(this.path);
    return this;
  }


  public String getType() {
    return type;
  }

  public Node setType(String type) {
    this.type = type;
    return this;
  }

  public String getCode() {
    return code;
  }

  public Node setCode(String code) {
    this.code = code;
    return this;
  }

  public Node setQualifier(String qualifier) {
    super.setQualifier(qualifier);
    return this;
  }

  public boolean isExclude() {
    return exclude;
  }

  public Node setExclude(boolean exclude) {
    this.exclude = exclude;
    return this;
  }

  public Node setNodeRef(String nodeRef) {
    super.setNodeRef(nodeRef);
    return this;
  }

  public Node setParameter(String parameter) {
    super.setParameter(parameter);
    return this;
  }

  public static Node iri(String iri) {
    return new Node(iri);
  }

  public Node() {
  }

  public Node(String iri) {
    super.setIri(iri);
  }


  public Node setAncestorsOf(boolean ancestorsOf) {
    super.setAncestorsOf(ancestorsOf);
    return this;
  }

  public Node setDescendantsOrSelfOf(boolean descendantsOrSelfOf) {
    super.setDescendantsOrSelfOf(descendantsOrSelfOf);
    return this;
  }

  public Node setDescendantsOf(boolean descendantsOf) {
    super.setDescendantsOf(descendantsOf);
    return this;
  }

  public Node setMemberOf(boolean memberOf) {
    super.setMemberOf(memberOf);
    return this;
  }


  @JsonSetter
  public Node setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public Node setName(String name) {
    super.setName(name);
    return this;
  }

  public Node setVariable(String variable) {
    super.setVariable(variable);
    return this;
  }


}

