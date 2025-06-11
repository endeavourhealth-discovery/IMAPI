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
