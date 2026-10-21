package org.endeavourhealth.imapi.dataaccess.databases

import org.eclipse.rdf4j.model.Model
import org.eclipse.rdf4j.query.BooleanQuery
import org.eclipse.rdf4j.query.GraphQuery
import org.eclipse.rdf4j.query.TupleQuery
import org.eclipse.rdf4j.query.Update
import org.eclipse.rdf4j.query.impl.SimpleDataset
import org.eclipse.rdf4j.repository.Repository
import org.eclipse.rdf4j.repository.RepositoryConnection
import org.eclipse.rdf4j.repository.http.HTTPRepository
import org.eclipse.rdf4j.repository.sail.SailRepository
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository
import org.eclipse.rdf4j.sail.nativerdf.NativeStore
import org.endeavourhealth.imapi.dataaccess.helpers.DALException
import org.endeavourhealth.imapi.utility.QueryCounter
import org.endeavourhealth.imapi.vocabulary.GRAPH
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.AutoCloseable
import java.util.*

abstract class BaseDB protected constructor(vararg graphs: GRAPH?) : AutoCloseable {
  private val dataset = SimpleDataset()

  protected var conn: RepositoryConnection? = null

  init {
    QueryCounter.connectionOpened()
    for (graph in graphs) {
      if (graph == null) dataset.addDefaultGraph(null)
      else dataset.addDefaultGraph(graph.asDbIri())
    }
  }

  @Synchronized
  protected fun getRepository(repoId: String?): Repository {
    log.debug("Connecting to repository [{}]", repoId)
    var type = System.getenv("GRAPH_TYPE")
    if (type.isNullOrEmpty()) type = "http"

    return when (type.lowercase(Locale.getDefault())) {
      "http" -> getHttpRepo(repoId)
      "file" -> getFileRepo(repoId)
      "sparql" -> getSparqlRepo(repoId)
      else -> throw DALException("Invalid graph connection parameters")
    }
  }

  fun prepareTupleSparql(sparql: String): TupleQuery {
    QueryCounter.queryPrepared(sparql)
    if (sparql.uppercase(Locale.getDefault())
        .startsWith("INSERT")
    ) throw DALException("This appears to be an INSERT statement, use `prepareInsertSparql` instead")

    if (sparql.uppercase(Locale.getDefault())
        .startsWith("DELETE")
    ) throw DALException("This appears to be an DELETE statement, use `prepareDeleteSparql` instead")

    try {
      val sj = listOf(
        DEFAULT_PREFIXES,
        sparql
      ).joinToString(separator = System.lineSeparator())
      val tq = conn!!.prepareTupleQuery(sj)
      tq.dataset = dataset
      return tq
    } catch (e: Exception) {
      throw DALException("Failed to prepare SPARQL query", e)
    }
  }

  fun prepareDeleteSparql(sparql: String): Update {
    var sparql = sparql
    sparql = sparql.trim { it <= ' ' }
    if (!sparql.uppercase(Locale.getDefault())
        .startsWith("DELETE")
    ) throw DALException("This doesnt appear to be an DELETE statement")

    return prepareSparql(sparql)
  }

  fun prepareInsertSparql(sparql: String, graph: GRAPH): Update {
    var sparql = sparql
    sparql = sparql.trim { it <= ' ' }
    if (!sparql.uppercase(Locale.getDefault())
        .startsWith("INSERT")
    ) throw DALException("This doesnt appear to be an INSERT statement")

    if (graph == null) throw DALException("A graph MUST be specified for an insert statement")

    val insert = prepareSparql(sparql)
    dataset.setDefaultInsertGraph(graph.asDbIri())

    return insert
  }

  fun prepareUpdateSparql(sparql: String, graph: GRAPH): Update {
    var sparql = sparql
    sparql = sparql.trim { it <= ' ' }
    if (!sparql.uppercase(Locale.getDefault()).startsWith("DELETE") || !sparql.uppercase(Locale.getDefault())
        .contains("INSERT")
    ) throw DALException("This doesnt appear to be an UPDATE statement")

    if (graph == null) throw DALException("A graph MUST be specified for an update statement")

    val update = prepareSparql(sparql)
    dataset.setDefaultInsertGraph(graph.asDbIri())

    return update
  }

  private fun prepareSparql(sparql: String?): Update {
    QueryCounter.queryPrepared(sparql)
    try {
      val sj = StringJoiner(System.lineSeparator())
      sj.add(DEFAULT_PREFIXES)
      sj.add(sparql)
      val uq = conn!!.prepareUpdate(sj.toString())
      uq.setDataset(dataset)
      return uq
    } catch (e: Exception) {
      throw DALException("Failed to prepare SPARQL query", e)
    }
  }

  fun prepareGraphSparql(sparql: String?): GraphQuery {
    QueryCounter.queryPrepared(sparql)

    try {
      val sj = StringJoiner(System.lineSeparator())
      sj.add(DEFAULT_PREFIXES)
      sj.add(sparql)
      val gq = conn!!.prepareGraphQuery(sj.toString())
      gq.setDataset(dataset)
      return gq
    } catch (e: Exception) {
      throw DALException("Failed to prepare SPARQL query", e)
    }
  }

  fun prepareBooleanSparql(sparql: String): BooleanQuery {
    var sparql = sparql
    QueryCounter.queryPrepared(sparql)
    sparql = sparql.trim { it <= ' ' }
    if (!sparql.uppercase(Locale.getDefault())
        .startsWith("ASK")
    ) throw DALException("This doesnt appear to be an ASK statement")

    try {
      val sj = StringJoiner(System.lineSeparator())
      sj.add(DEFAULT_PREFIXES)
      sj.add(sparql)
      val bq = conn!!.prepareBooleanQuery(sj.toString())
      bq.setDataset(dataset)
      return bq
    } catch (e: Exception) {
      throw DALException("Failed to prepare SPARQL query", e)
    }
  }

  override fun close() {
    if (conn != null) {
      conn!!.close()
      conn = null
    }
  }

  fun add(build: Model?) {
    conn!!.add(build)
  }

  fun begin() {
    conn!!.begin()
  }

  fun commit() {
    conn!!.commit()
  }

  fun rollback() {
    conn!!.rollback()
  }

  companion object {
    private val log = LoggerFactory.getLogger(javaClass)

    private val DEFAULT_PREFIXES = """
    PREFIX bc: <http://endhealth.info/bc#>
    PREFIX owl: <http://www.w3.org/2002/07/owl#>
    PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
    PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
    PREFIX sh: <http://www.w3.org/ns/shacl#>
    PREFIX xml: <http://www.w3.org/XML/1998/namespace#>
    PREFIX sn: <http://snomed.info/sct#>
    PREFIX im: <http://endhealth.info/im#>
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    
    """.trimIndent()

    private fun getHttpRepo(repoId: String?): HTTPRepository {
      var server = System.getenv("GRAPH_SERVER")
      if (server.isNullOrEmpty()) {
        server = "http://localhost:7200/"
        log.warn("GRAPH_SERVER not set, defaulting to '$server'")
      }

      val repo = HTTPRepository(server, repoId)

      val user = System.getenv("GRAPH_USER")
      val pass = System.getenv("GRAPH_PASS")

      if (user != null && pass != null) repo.setUsernameAndPassword(user, pass)

      return repo
    }

    private fun getFileRepo(repoId: String?): SailRepository {
      var server = System.getenv("GRAPH_SERVER")
      if (server.isNullOrEmpty()) {
        server = "C:\\rdf4j\\"
        log.warn("GRAPH_FILENAME not set, defaulting to '$server'")
      }

      var indexes = System.getenv("GRAPH_INDEXES")
      if (indexes.isNullOrEmpty()) {
        indexes = "spoc,posc,opsc"
        log.warn("GRAPH_INDEXES not set, defaulting to '$indexes'")
      }

      return SailRepository(NativeStore(File(server + repoId), indexes))
    }

    private fun getSparqlRepo(repoId: String?): SPARQLRepository {
      var query = System.getenv("GRAPH_QUERY")
      if (query.isNullOrEmpty()) {
        query = "cluster-cwwwbkhdusnw.eu-west-2.neptune.amazonaws.com"
        log.warn("GRAPH_QUERY not set, defaulting to '$query'")
      }

      var update = System.getenv("GRAPH_UPDATE")
      if (update.isNullOrEmpty()) {
        update = "cluster-ro-cwwwbkhdusnw.eu-west-2.neptune.amazonaws.com"
        log.warn("GRAPH_UPDATE not set, defaulting to '$update'")
      }

      return SPARQLRepository("$repoId.$query", "$repoId.$update")
    }
  }
}

