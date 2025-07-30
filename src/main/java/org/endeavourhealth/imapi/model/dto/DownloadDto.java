package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.set.ExportSet;

import java.io.Serializable;
import java.util.List;

public class DownloadDto implements Serializable {

  private TTEntity summary;
  private List<EntityReferenceNode> hasSubTypes;
  private TTNode inferred;
  private TTNode axioms;
  private ExportSet members;
  private List<DataModelProperty> dataModelProperties;
  private List<SearchTermCode> terms;
  private TTArray isChildOf;
  private TTArray hasChildren;

  public List<EntityReferenceNode> getHasSubTypes() {
    return hasSubTypes;
  }

  public void setHasSubTypes(List<EntityReferenceNode> hasSubTypes) {
    this.hasSubTypes = hasSubTypes;
  }

  public TTNode getInferred() {
    return inferred;
  }

  public DownloadDto setInferred(TTNode inferred) {
    this.inferred = inferred;
    return this;
  }

  public TTNode getAxioms() {
    return axioms;
  }

  public DownloadDto setAxioms(TTNode axioms) {
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

  public TTEntity getSummary() {
    return summary;
  }

  public void setSummary(TTEntity summary) {
    this.summary = summary;
  }

  public List<SearchTermCode> getTerms() {
    return terms;
  }

  public void setTerms(List<SearchTermCode> terms) {
    this.terms = terms;
  }

  public TTArray getIsChildOf() {
    return isChildOf;
  }

  public void setIsChildOf(TTArray isChildOf) {
    this.isChildOf = isChildOf;
  }

  public TTArray getHasChildren() {
    return hasChildren;
  }

  public void setHasChildren(TTArray hasChildren) {
    this.hasChildren = hasChildren;
  }
}
