package org.endeavourhealth.imapi.model;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntityReferenceNode extends TTIriRef implements Serializable {
  private List<EntityReferenceNode> parents = new ArrayList<>();
  private List<EntityReferenceNode> children;
  private String moduleId;
  private boolean hasChildren;
  private boolean hasGrandChildren;
  private TTArray type;
  private int orderNumber;

  public EntityReferenceNode() {
  }

  public EntityReferenceNode addType(TTIriRef type){
    if (this.type == null)
      this.type = new TTArray();
    this.type.add(type);
    return this;
  }

  public EntityReferenceNode(String iri) {
    super(iri);
  }

  public EntityReferenceNode(String iri, String name, TTArray types) {
    super(iri, name);
    setType(types);
  }

  public EntityReferenceNode(String iri, String name) {
    super(iri, name);
  }

  public List<EntityReferenceNode> getParents() {
    return parents;
  }

  public EntityReferenceNode setParents(List<EntityReferenceNode> parents) {
    this.parents = parents;
    return this;
  }

  public EntityReferenceNode addParent(EntityReferenceNode parent) {
    if (this.parents == null)
      this.parents = new ArrayList<>();

    this.parents.add(parent);

    return this;
  }

  public int getOrderNumber() {
    return orderNumber;
  }

  public EntityReferenceNode setOrderNumber(int order) {
    this.orderNumber = order;
    return this;
  }

  public List<EntityReferenceNode> getChildren() {
    return children;
  }

  public EntityReferenceNode setChildren(List<EntityReferenceNode> children) {
    this.children = children;
    return this;
  }

  public EntityReferenceNode addChild(EntityReferenceNode parent) {
    if (this.children == null)
      this.children = new ArrayList<>();

    this.children.add(parent);

    return this;
  }

  public String getModuleId() {
    return moduleId;
  }

  public EntityReferenceNode setModuleId(String moduleId) {
    this.moduleId = moduleId;
    return this;
  }

  public EntityReferenceNode setHasGrandChildren(boolean hasGrandChildren) {
    this.hasGrandChildren = hasGrandChildren;
    return this;
  }

  public boolean isHasGrandChildren() {
    return hasGrandChildren;
  }

  public EntityReferenceNode setHasChildren(boolean hasChildren) {
    this.hasChildren = hasChildren;
    return this;
  }

  public boolean isHasChildren() {
    return hasChildren;
  }

  public TTArray getType() {
    return type;
  }

  public EntityReferenceNode setType(TTArray type) {
    this.type = type;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    EntityReferenceNode that = (EntityReferenceNode) o;
    return hasChildren == that.hasChildren
      && Objects.equals(parents, that.parents)
      && Objects.equals(children, that.children)
      && Objects.equals(moduleId, that.moduleId)
      && Objects.equals(type, that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), parents, children, moduleId, hasChildren, type);
  }
}
