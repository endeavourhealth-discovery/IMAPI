package org.endeavourhealth.imapi.dataaccess.databases

import org.eclipse.rdf4j.query.Update
import org.eclipse.rdf4j.repository.Repository
import org.endeavourhealth.imapi.vocabulary.GRAPH

class WorkflowDB private constructor() : BaseDB(GRAPH.WORKFLOW) {
  private val repository: Repository = getRepository("workflow")

  init {
    conn = repository.connection
  }

  fun prepareInsertSparql(sparql: String): Update {
    return prepareInsertSparql(sparql, GRAPH.WORKFLOW)
  }

  companion object {
    fun getConnection(): WorkflowDB {
      return WorkflowDB()
    }
  }
}