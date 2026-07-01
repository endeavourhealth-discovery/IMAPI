package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.set.ExportSet;
import org.endeavourhealth.imapi.model.tripletree.TTArrayJava;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.model.tripletree.TTNodeJava;
import org.endeavourhealth.interfacemanager.model.DataModelProperty;
import org.endeavourhealth.interfacemanager.model.EntityReferenceNode;
import org.endeavourhealth.interfacemanager.model.SearchTermCode;

import java.io.Serializable;
import java.util.List;

public class DownloadDto implements Serializable {

  private TTEntityJava summary;
  private List<EntityReferenceNode> hasSubTypes;
  private TTNodeJava inferred;
  private TTNodeJava axioms;
  private ExportSet members;
  private List<DataModelProperty> dataModelProperties;
  private List<SearchTermCode> terms;
  private TTArrayJava isChildOf;
  private TTArrayJava hasChildren;

  public List<EntityReferenceNode> getHasSubTypes() {
    return hasSubTypes;
  }

  public void setHasSubTypes(List<EntityReferenceNode> hasSubTypes) {
    this.hasSubTypes = hasSubTypes;
  }

  public TTNodeJava getInferred() {
    return inferred;
  }

  public DownloadDto setInferred(TTNodeJava inferred) {
    this.inferred = inferred;
    return this;
  }

  public TTNodeJava getAxioms() {
    return axioms;
  }

  public DownloadDto setAxioms(TTNodeJava axioms) {
    this.axioms = axioms;
    return this;
  }

  public ExportSet getMembers() {
    return members;
  }

  public void setMembers(ExportSet members) {
    this.members = members;
  }

  public List<DataModelProperty> getDataModelProperties() {
    return dataModelProperties;
  }

  public void setDataModelProperties(List<DataModelProperty> dataModelProperties) {
    this.dataModelProperties = dataModelProperties;
  }

  public TTEntityJava getSummary() {
    return summary;
  }

  public void setSummary(TTEntityJava summary) {
    this.summary = summary;
  }

  public List<SearchTermCode> getTerms() {
    return terms;
  }

  public void setTerms(List<SearchTermCode> terms) {
    this.terms = terms;
  }

  public TTArrayJava getIsChildOf() {
    return isChildOf;
  }

  public void setIsChildOf(TTArrayJava isChildOf) {
    this.isChildOf = isChildOf;
  }

  public TTArrayJava getHasChildren() {
    return hasChildren;
  }

  public void setHasChildren(TTArrayJava hasChildren) {
    this.hasChildren = hasChildren;
  }
}
