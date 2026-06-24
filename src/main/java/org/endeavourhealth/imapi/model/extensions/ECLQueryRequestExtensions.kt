package org.endeavourhealth.imapi.model.extensions

import org.endeavourhealth.interfacemanager.model.ECLQueryRequest
import org.endeavourhealth.interfacemanager.model.TTIriRef

fun ECLQueryRequest.getStatusFilterAsSet(): MutableSet<TTIriRef?>? {
  return statusFilter?.toMutableSet()
}

fun ECLQueryRequest.setStatusFilter(statusFilter: MutableSet<TTIriRef?>?): ECLQueryRequest {
  this.statusFilter = statusFilter?.toList()
  return this
}