package org.endeavourhealth.imapi.model.sql

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.extern.slf4j.Slf4j
import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.DatabaseOption
import org.endeavourhealth.imapi.model.imq.Match
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.requests.QueryRequest

@Slf4j
class IMQtoSQLConverterKotlin(
  private val queryRequest: QueryRequest, private val mapper: ObjectMapper = ObjectMapper()
) {

  init {
    require(queryRequest.query != null) { "Query request must have a query body" }
  }

  private var tableMap: TableMap? = null
  private var sql: String? = null


  fun init() {
    if (queryRequest.language == null) queryRequest.setLanguage(DatabaseOption.MYSQL)

    try {
      val resourcePath = "IMQtoMYSQL.json"
      tableMap = MappingParser().parse(resourcePath)
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
    sql = IMQtoSQL()
  }

  @Throws(SQLConversionException::class, JsonProcessingException::class)
  fun IMQtoSQL(): String {
    try {
      return processQueryDefinition(queryRequest.query)
    } catch (e: SQLConversionException) {
      println("SQL Conversion Error: $e.message")
      throw e
    } catch (e: JsonProcessingException) {
      println("SQL Conversion Error: $e.message")
      throw e
    }
  }

  private fun processQueryDefinition(definition: Query): String {
    if (definition.getTypeOf() == null || definition.typeOf.iri == null) throw SQLConversionException("Query typeOf is null")
    val mySQLQuery = MySQLQuery()

    if (definition.getIs() != null) {
      mySQLQuery.ctes = getIsCTEs(definition)
    }
//    if(definition.`return` != null) {
//
//    }
    mySQLQuery.selects.add(Select("id"))
    return mySQLQuery.toSql()
  }

  private fun getIsCTEs(match: Match, typeOf: String? = null): MutableList<CTE> {
    val ctes = ArrayList<CTE>()
    val table = getTableFromTypeAndProperty(match.typeOf.iri ?: typeOf)
    for (isA in match.getIs()) {
      val isAlias = getCteAlias(isA.iri)
      val isSelects = mutableListOf(Select("id"))
      ctes.add(CTE(table, isAlias, isSelects))
    }
    return ctes
  }

  private fun getTableFromTypeAndProperty(typeIri: String?): String {
    val table = tableMap?.getTableFromDataModel(typeIri);
    return table?.table ?: "";
  }

  private fun getCteAlias(iri: String?): String {
    val iriSuffix = iri?.substringAfter('#')
    return "${iriSuffix}_cte"
  }
}