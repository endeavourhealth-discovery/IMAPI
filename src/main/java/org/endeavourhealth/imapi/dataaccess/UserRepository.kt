package org.endeavourhealth.imapi.dataaccess

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import org.eclipse.rdf4j.model.util.Values.iri
import org.eclipse.rdf4j.model.util.Values.literal
import org.eclipse.rdf4j.query.BooleanQuery
import org.eclipse.rdf4j.query.TupleQuery
import org.eclipse.rdf4j.query.Update
import org.endeavourhealth.imapi.dataaccess.databases.UserDB
import org.endeavourhealth.imapi.logic.CachedObjectMapper
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto
import org.endeavourhealth.imapi.vocabulary.Graph
import org.endeavourhealth.imapi.vocabulary.Namespace
import org.endeavourhealth.imapi.vocabulary.USER
import org.endeavourhealth.imapi.vocabulary.VocabEnum

class UserRepository {

  fun getSparqlSelect(): String {
    return """
      SELECT ?o WHERE {
        ?s ?p ?o
      }
    """
  }

  fun getSparqlDelete(): String {
    return """
      DELETE WHERE {
        ?s ?p ?o
      }
    """
  }

  fun getSparqlInsert(): String {
    return """
      INSERT {
        ?s ?p ?o
      }
      WHERE {
        SELECT ?s ?p ?o {}
      }
    """
  }

  fun getByPredicate(user: String, predicate: VocabEnum): String = getByPredicate(user, predicate.toString())

  fun getByPredicate(user: String, predicate: String): String {
    var result = ""
    val sparql = getSparqlSelect()
    UserDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sparql)
      qry.setBinding("s", iri("${Namespace.USER}$user"))
      qry.setBinding("p", iri(predicate))
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          result = bs.getValue("o").stringValue()
        }
      }
    }
    return result
  }

  @Throws(JsonProcessingException::class)
  fun getUserMRU(user: String): List<RecentActivityItemDto> {
    val sparql = getSparqlSelect()
    UserDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sparql)
      qry.setBinding("s", iri("${Namespace.USER}$user"))
      qry.setBinding("p", USER.USER_MRU.asDbIri())
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          CachedObjectMapper().use { om ->
            return om.readValue(
              bs.getValue("o").stringValue(),
              object : TypeReference<List<RecentActivityItemDto>>() {})
          }
        }
      }
    }
    return listOf()
  }

  @Throws(JsonProcessingException::class)
  fun getUserFavourites(user: String): List<String> {
    val sparql = getSparqlSelect()
    UserDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sparql)
      qry.setBinding("s", iri("${Namespace.USER}$user"))
      qry.setBinding("p", USER.USER_FAVOURITES.asDbIri())
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          CachedObjectMapper().use { om ->
            return om.readValue(bs.getValue("o").stringValue(), object : TypeReference<List<String>>() {})
          }
        }
      }
    }
    return mutableListOf()
  }

  fun delete(user: String, predicate: VocabEnum) {
    val sparql = getSparqlDelete()
    UserDB.getConnection().use { conn ->
      val qry: Update = conn.prepareDeleteSparql(sparql)
      qry.setBinding("s", iri("${Namespace.USER}$user"))
      qry.setBinding("p", predicate.asDbIri())
      qry.execute()
    }
  }

  @Throws(JsonProcessingException::class)
  fun insert(user: String, predicate: VocabEnum, obj: Any) {
    CachedObjectMapper().use { om ->
      val sparql = getSparqlInsert()
      UserDB.getConnection().use { conn ->
        val qry: Update = conn.prepareInsertSparql(sparql)
        qry.setBinding("s", iri("${Namespace.USER}$user"))
        qry.setBinding("p", predicate.asDbIri())
        qry.setBinding("o", literal(om.writeValueAsString(obj)))
        qry.execute()
      }
    }
  }

  @Throws(JsonProcessingException::class)
  fun updateUserMRU(user: String, mru: List<RecentActivityItemDto>) {
    if (mru.isEmpty()) {
      delete(user, USER.USER_MRU)
      return
    }
    if (mru.all { isValidRecentActivityItem(it) }) {
      delete(user, USER.USER_MRU)
      insert(user, USER.USER_MRU, mru)
    } else throw IllegalArgumentException("One or more activity items are invalid")
  }

  private fun isValidRecentActivityItem(item: RecentActivityItemDto): Boolean {
    return item.iri.isNotEmpty() && item.action.isNotEmpty() && item.dateTime != null
  }

  @Throws(JsonProcessingException::class)
  fun updateUserFavourites(user: String, favourites: List<String>) {
    delete(user, USER.USER_FAVOURITES)
    insert(user, USER.USER_FAVOURITES, favourites)
  }

  @Throws(JsonProcessingException::class)
  fun updateByPredicate(user: String, data: String, predicate: VocabEnum) {
    delete(user, predicate)
    insert(user, predicate, data)
  }

  @Throws(JsonProcessingException::class)
  fun updateByPredicate(user: String, data: Boolean, predicate: VocabEnum) {
    updateByPredicate(user, data.toString(), predicate)
  }

  @Throws(JsonProcessingException::class)
  fun getUserOrganisations(user: String): List<String> {
    val sparql = getSparqlSelect()
    UserDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sparql)
      qry.setBinding("s", iri("${Namespace.USER}$user"))
      qry.setBinding("p", USER.ORGANISATIONS.asDbIri())
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          CachedObjectMapper().use { om ->
            val organisations: List<String> =
              om.readValue(bs.getValue("o").stringValue(), object : TypeReference<List<String>>() {})
            if (organisations.isNotEmpty()) return organisations
          }
        }
      }
    }
    return mutableListOf()
  }

  @Throws(JsonProcessingException::class)
  fun updateUserOrganisations(user: String, organisations: List<String>) {
    delete(user, USER.ORGANISATIONS)
    insert(user, USER.ORGANISATIONS, organisations)
  }

  @Throws(JsonProcessingException::class)
  fun getUserGraphs(user: String): List<Graph> {
    val result: MutableList<Graph> = mutableListOf(Graph.IM)
    val sparql = getSparqlSelect()
    UserDB.getConnection().use { conn ->
      val qry: TupleQuery = conn.prepareTupleSparql(sparql)
      qry.setBinding("s", iri("${Namespace.USER}$user"))
      qry.setBinding("p", USER.GRAPHS.asDbIri())
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          CachedObjectMapper().use { om ->
            val graphs: List<Graph> =
              om.readValue(bs.getValue("o").stringValue(), object : TypeReference<List<Graph>>() {})
            if (graphs.isNotEmpty()) result.addAll(graphs)
          }
        }
      }
    }
    return result
  }

  @Throws(JsonProcessingException::class)
  fun updateUserGraphs(user: String, graphs: List<Graph>) {
    delete(user, USER.GRAPHS)
    insert(user, USER.GRAPHS, graphs)
  }

  fun getUserIdExists(userId: String): Boolean {
    UserDB.getConnection().use { conn ->
      val sparql = "ASK { ?s ?p ?o. }"
      val qry: BooleanQuery = conn.prepareBooleanSparql(sparql)
      qry.setBinding("s", iri("${Namespace.USER}$userId"))
      return qry.evaluate()
    }
  }
}
