package org.endeavourhealth.imapi.model.security

import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole

class Permission(
  var resource: Resource? = null,
  var allowableRoles: List<UserRole>? = null,
  var requiredNamespaces: List<NamespacePermission>? = null,
)
