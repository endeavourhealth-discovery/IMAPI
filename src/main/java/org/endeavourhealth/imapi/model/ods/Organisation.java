package org.endeavourhealth.imapi.model.ods;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"Name", "OrgId", "Status", "orgRecordClass", "GeoLoc", "Roles", "Rels"})
public class Organisation {
  private String name;
  private OrgId orgId;
  private String status;
  private String orgRecordClass;
  private OrgGeoLocation geoLoc;
  private OrgRelationships rels;
  private OrgRoles roles;

  @JsonProperty("Name")
  public String getName() {
    return name;
  }

  public Organisation setName(String name) {
    this.name = name;
    return this;
  }

  @JsonProperty("OrgId")
  public OrgId getOrgId() {
    return orgId;
  }

  public Organisation setOrgId(OrgId orgId) {
    this.orgId = orgId;
    return this;
  }

  @JsonProperty("Status")
  public String getStatus() {
    return status;
  }

  public Organisation setStatus(String status) {
    this.status = status;
    return this;
  }

  public String getOrgRecordClass() {
    return orgRecordClass;
  }

  public Organisation setOrgRecordClass(String orgRecordClass) {
    this.orgRecordClass = orgRecordClass;
    return this;
  }

  @JsonProperty("GeoLoc")
  public OrgGeoLocation getGeoLoc() {
    return geoLoc;
  }

  public Organisation setGeoLoc(OrgGeoLocation geoLoc) {
    this.geoLoc = geoLoc;
    return this;
  }

  @JsonProperty("Roles")
  public OrgRoles getRoles() {
    return roles;
  }

  public Organisation setRoles(OrgRoles roles) {
    this.roles = roles;
    return this;
  }

  @JsonProperty("Rels")
  public OrgRelationships getRels() {
    return rels;
  }

  public Organisation setRels(OrgRelationships rels) {
    this.rels = rels;
    return this;
  }
}
