package org.endeavourhealth.imapi.model.dto;

import java.util.List;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class ParentDto extends TTIriRef {

  private List<ParentDto> parents;

  public ParentDto() {
  }

  public ParentDto(String iri, String name, List<ParentDto> parents) {
    super(iri, name);
    this.parents = parents;
  }

  public List<ParentDto> getParents() {
    return parents;
  }

  public ParentDto setParents(List<ParentDto> parents) {
    this.parents = parents;
    return this;
  }

  public boolean hasMultipleParents() {
    if (parents == null) return false;
    return parents.size() > 1;
  }

  public boolean hasSingleParent() {
    if (parents == null) return false;
    return parents.size() == 1;
  }
}
