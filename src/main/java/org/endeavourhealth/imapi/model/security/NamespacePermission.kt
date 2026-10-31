package org.endeavourhealth.imapi.model.security

import org.endeavourhealth.imapi.vocabulary.NAMESPACE

class NamespacePermission(
  var iri: NAMESPACE? = null,
  var read: Boolean = false,
  var write: Boolean = false
)
