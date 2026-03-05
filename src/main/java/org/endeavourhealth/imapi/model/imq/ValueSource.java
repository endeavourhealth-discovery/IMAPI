package org.endeavourhealth.imapi.model.imq;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;


public class ValueSource {
  private String parameter;
  private String name;
  private Path path;
  private String nodeRef;


  public String getName() {
    return name;
  }

  public ValueSource setName(String name) {
    this.name = name;
    return this;
  }



  public String getParameter() {
    return parameter;
  }

  public ValueSource setParameter(String parameter) {
    this.parameter = parameter;
    return this;
  }


  public Path getPath() {
    return path;
  }

  public ValueSource setPath(Path path) {
    this.path = path;
    return this;
  }

  public String getNodeRef() {
    return nodeRef;
  }

  public ValueSource setNodeRef(String nodeRef) {
    this.nodeRef = nodeRef;
    return this;
  }
}
