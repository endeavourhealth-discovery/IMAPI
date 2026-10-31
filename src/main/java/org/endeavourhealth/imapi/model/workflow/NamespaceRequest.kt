package org.endeavourhealth.imapi.model.workflow

import org.endeavourhealth.imapi.model.security.NamespacePermission

class NamespaceRequest(
  var namespacePermission: NamespacePermission? = null
) : Task()
