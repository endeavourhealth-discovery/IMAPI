package org.endeavourhealth.imapi.dataaccess.databases

import org.eclipse.rdf4j.query.Update
import org.endeavourhealth.imapi.vocabulary.GRAPH

class ConfigDB private constructor() : BaseDB(GRAPH.CONFIG) {
  private val repository = getRepository("config")

  init {
    conn = repository.connection
  }

  fun prepareInsertSparql(sparql: String): Update {
    return super.prepareInsertSparql(sparql, GRAPH.CONFIG)
  }

  companion object {
    fun getConnection(): ConfigDB {
      return ConfigDB()
    }
  }
}
