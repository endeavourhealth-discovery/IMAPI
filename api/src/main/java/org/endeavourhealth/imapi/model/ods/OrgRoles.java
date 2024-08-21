package org.endeavourhealth.imapi.model.ods;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgRoles {
  private List<OrgRole> role = new ArrayList<>();

  @JsonProperty("Role")
  public List<OrgRole> getRole() {
    return role;
  }

  public OrgRoles setRole(List<OrgRole> role) {
    this.role = role;
    return this;
  }

  public OrgRoles addRole(OrgRole orgRole) {
    if (role == null)
      role = new ArrayList<>();

    role.add(orgRole);

    return this;
  }
}
