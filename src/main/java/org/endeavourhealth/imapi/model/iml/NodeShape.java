package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"iri", "name", "property"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class NodeShape extends TTIriRefExtended {
  private List<TTIriRefExtended> subType;
  private List<PropertyShape> property;
  private TTIriRefExtended definingProperty;
  private TTIriRefExtended inverseProperty;

  public TTIriRefExtended getInverseProperty() {
    return inverseProperty;
  }
  public NodeShape setInverseProperty(TTIriRefExtended inverseProperty) {
    this.inverseProperty = inverseProperty;
    return this;
  }


  public TTIriRefExtended getDefiningProperty() {
    return definingProperty;
  }
  public NodeShape setDefiningProperty(TTIriRefExtended definingProperty) {
    this.definingProperty = definingProperty;
    return this;
  }


  public List<TTIriRefExtended> getSubType() {
    return subType;
  }

  public NodeShape setSubType(List<TTIriRefExtended> subType) {
    this.subType = subType;
    return this;
  }
  public NodeShape addSubType (TTIriRefExtended subType){
      if (this.subType == null) {
        this.subType = new ArrayList<>();
      }
      this.subType.add(subType);
      return this;
    }
   public NodeShape subType (Consumer <TTIriRefExtended> builder) {
      TTIriRefExtended subType = new TTIriRefExtended();
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
