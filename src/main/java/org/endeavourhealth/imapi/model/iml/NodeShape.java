package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"iri", "name", "property"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class NodeShape extends TTIriRef {
  private List<TTIriRef> subType;
  private List<PropertyShape> property;
  private TTIriRef definingProperty;
  private TTIriRef inverseProperty;
  private List<NodeShape> folder;
  private List<NodeShape> type;

  public List<NodeShape> getType() {
    return type;
  }
  public NodeShape setType(List<NodeShape> type) {
    this.type = type;
    return this;
  }
  public NodeShape addType (NodeShape type){
      if (this.type == null) {
        this.type = new ArrayList<>();
      }
      this.type.add(type);
      return this;
  }
  public NodeShape type (Consumer < NodeShape > builder) {
      NodeShape type = new NodeShape();
      addType(type);
      builder.accept(type);
      return this;
  }

  public List<NodeShape> getFolder() {
    return folder;
  }
  public NodeShape setFolder(List<NodeShape> folder) {
    this.folder = folder;
    return this;
  }
  public NodeShape addFolder (NodeShape folder){
      if (this.folder == null) {
        this.folder = new ArrayList<>();
      }
      this.folder.add(folder);
      return this;
  }
  public NodeShape folder (Consumer < NodeShape > builder) {
      NodeShape folder = new NodeShape();
      addFolder(folder);
      builder.accept(folder);
      return this;
  }

  public TTIriRef getInverseProperty() {
    return inverseProperty;
  }
  public NodeShape setInverseProperty(TTIriRef inverseProperty) {
    this.inverseProperty = inverseProperty;
    return this;
  }


  public TTIriRef getDefiningProperty() {
    return definingProperty;
  }
  public NodeShape setDefiningProperty(TTIriRef definingProperty) {
    this.definingProperty = definingProperty;
    return this;
  }


  public List<TTIriRef> getSubType() {
    return subType;
  }

  public NodeShape setSubType(List<TTIriRef> subType) {
    this.subType = subType;
    return this;
  }
  public NodeShape addSubType (TTIriRef subType){
      if (this.subType == null) {
        this.subType = new ArrayList<>();
      }
      this.subType.add(subType);
      return this;
    }
   public NodeShape subType (Consumer < TTIriRef > builder) {
      TTIriRef subType = new TTIriRef();
      addSubType(subType);
      builder.accept(subType);
      return this;
    }


  public List<PropertyShape> getProperty() {
    return property;
  }

  public NodeShape setProperty(List<PropertyShape> property) {
    this.property = property;
    return this;
  }
  public NodeShape addProperty (PropertyShape property){
      if (this.property == null) {
        this.property = new ArrayList<>();
      }
      this.property.add(property);
      return this;
    }

   public NodeShape property (Consumer< PropertyShape > builder) {
      PropertyShape property = new PropertyShape();
      addProperty(property);
      builder.accept(property);
      return this;
    }


}
