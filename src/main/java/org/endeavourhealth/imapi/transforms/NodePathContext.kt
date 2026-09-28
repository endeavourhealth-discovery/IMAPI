package org.endeavourhealth.imapi.transforms

import org.endeavourhealth.imapi.model.sql.Table

internal data class NodePathContext(
  val parentTable: Table,
  val pathIri: String,
  val nodeTable: Table
)
