package org.endeavourhealth.imapi.model.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.endeavourhealth.interfacemanager.model.UserRole;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Permission {
  private Resource resource;
  private List<UserRole> allowableRoles;
  private List<NamespacePermission> requiredNamespaces;

  public Permission(Resource resource, List<UserRole> allowableRoles, List<NamespacePermission> requiredNamespaces) {
    this.resource = resource;
    this.allowableRoles = allowableRoles;
    this.requiredNamespaces = requiredNamespaces;
  }
}
