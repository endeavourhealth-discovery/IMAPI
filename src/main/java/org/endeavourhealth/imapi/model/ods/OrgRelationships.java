package org.endeavourhealth.imapi.model.ods;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgRelationships {
  private List<OrgRelationship> rel = new ArrayList<>();

  @JsonProperty("Rel")
  public List<OrgRelationship> getRel() {
    return rel;
  }

  public OrgRelationships setRel(List<OrgRelationship> rel) {
    this.rel = rel;
    return this;
  }

  public OrgRelationships addRel(OrgRelationship orgRel) {
    if (rel == null)
      rel = new ArrayList<>();
    rel.add(orgRel);

    return this;
  }
}
