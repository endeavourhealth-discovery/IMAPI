package org.endeavourhealth.imapi.dataaccess.databases

import org.eclipse.rdf4j.repository.Repository
import org.endeavourhealth.imapi.vocabulary.GRAPH

class ProvDB private constructor() : BaseDB(GRAPH.PROV) {
  private val repository: Repository = getRepository("im")

  init {
    conn = repository.connection
  }

  companion object {
    fun getConnection(): ProvDB {
      return ProvDB()
    }
  }
}
