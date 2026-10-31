package org.endeavourhealth.imapi.dataaccess.databases

import org.eclipse.rdf4j.repository.Repository
import org.endeavourhealth.imapi.vocabulary.GRAPH

class IMDB private constructor() : BaseDB(GRAPH.IM) {
  private val repository: Repository = getRepository("im")

  init {
    conn = repository.connection
  }

  companion object {
    @JvmStatic
    fun getConnection(): IMDB {
      return IMDB()
    }
  }
}
