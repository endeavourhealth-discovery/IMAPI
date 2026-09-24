package org.endeavourhealth.imapi.transforms

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.extern.slf4j.Slf4j
import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.*
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.imapi.model.sql.MappingParser
import org.endeavourhealth.imapi.model.sql.MySQLBoolWhere
import org.endeavourhealth.imapi.model.sql.MySQLCompareWhere
import org.endeavourhealth.imapi.model.sql.MySQLJoin
import org.endeavourhealth.imapi.model.sql.MySQLOrderBy
import org.endeavourhealth.imapi.model.sql.MySQLOrderByItem
import org.endeavourhealth.imapi.model.sql.MySQLPropertyIsNullWhere
import org.endeavourhealth.imapi.model.sql.MySQLPropertyIsWhere
import org.endeavourhealth.imapi.model.sql.MySQLNotExistsWhere
import org.endeavourhealth.imapi.model.sql.MySQLPropertyValueWhere
import org.endeavourhealth.imapi.model.sql.MySQLQuery
import org.endeavourhealth.imapi.model.sql.MySQLSelect
import org.endeavourhealth.imapi.model.sql.MySQLWhere
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.endeavourhealth.imapi.model.sql.Table
import org.endeavourhealth.imapi.model.sql.TableMap
import org.endeavourhealth.imapi.vocabulary.IM
import org.endeavourhealth.imapi.vocabulary.NAMESPACE

@Slf4j
class IMQtoSQLConverterKotlin @JvmOverloads constructor(
  val queryRequest: QueryRequest, val mapper: ObjectMapper? = ObjectMapper(),
  val denominator: String? = null, val numerator: String? = null, val dataset: String? = null,
  val debugPatientId: String? = null
) {
  private var IMtoMySQLMap: TableMap = MappingParser().parse("IMQtoMYSQL.json")
  var sql: String? = null
  var queryTypeOf: String? = queryRequest.query?.typeOf?.iri
  var mySQLQueries: MutableList<MySQLQuery> = mutableListOf()
  var queryTypeOfTable = Table()
  private val aliases = AliasAllocator()

  private val nodePathContextMap = HashMap<String, NodePathContext>()
  private val pathJoinBuilder = PathJoinBuilder(nodePathContextMap, ::getTableFromTypeAndProperty)
  private val keepAsLookup = KeepAsLookup(queryRequest)
  private val selectCompiler = SelectClauseCompiler({ queryTypeOfTable }, nodePathContextMap)

  private val keepAsMap = HashMap<String, MySQLWith>()
  private val filterInjector = FilterInjector(queryRequest, { queryTypeOfTable }, ::getTableFromTypeAndProperty)
  private val whereCompiler = WhereClauseCompiler(
    { queryTypeOfTable }, keepAsMap, aliases, ::getTableFromTypeAndProperty, keepAsLookup::getDataModelFromKeepAs
  )
  private val matchCompiler = MatchTreeCompiler(
    { queryTypeOfTable }, aliases, keepAsMap, selectCompiler, whereCompiler, pathJoinBuilder,
    ::getTableFromTypeAndProperty
  )

  init {
    require(queryRequest.query != null) { "Query request must have a query body" }
  }

  init {
    try {
      if (debugPatientId != null) {
        require(queryTypeOf != null) { "Queries need a type" }
        queryTypeOfTable = getTableFromTypeAndProperty(queryTypeOf, null)
        sql = generatePatientTraceSQL(queryRequest.query, debugPatientId)
      } else if (queryRequest.query.queryType == IMQType.INDICATOR) sql = IndicatorSqlGenerator.generate(denominator, numerator, dataset)
      else {
        require(queryTypeOf != null) { "Queries need a type" }
        queryTypeOfTable = getTableFromTypeAndProperty(queryTypeOf, null)
        sql = generateSQL(queryRequest.query)
      }
    } catch (e: SQLConversionException) {
      println("SQL Conversion Error: $e")
      throw e
    } catch (e: JsonProcessingException) {
      println("SQL Conversion Error: $e")
      throw e
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
  }

  private fun generateSQL(definition: Query): String {
    aliases.reset()
    nodePathContextMap.clear()
    keepAsMap.clear()
    val mySqlQuery = MySQLQuery()
    if (definition.typeOf == null || definition.typeOf.iri == null) {
      throw SQLConversionException("Query typeOf is null")
    }

    matchCompiler.addMatchWithsRecursively(definition, mySqlQuery)

    if (definition.columnGroup != null) {
      for ((index, columnGroup) in definition.columnGroup.withIndex()) {
        aliases.reset()
        nodePathContextMap.clear()
        keepAsMap.clear()
        val newMySqlQuery = MySQLQuery()
        if (columnGroup.name == null) columnGroup.name = "ColumnGroup$index"
        mySQLQueries.add(newMySqlQuery)
        if (definition.`is` != null) newMySqlQuery.withs.add(matchCompiler.getIsWith(definition, newMySqlQuery))
        matchCompiler.addMatchWithsRecursively(columnGroup, newMySqlQuery)
        if (columnGroup.and == null && columnGroup.or == null &&
          columnGroup.`is` == null
        ) {
          newMySqlQuery.withs.add(matchCompiler.buildChainedWith(columnGroup, newMySqlQuery))
        }
        if (definition.`return` == null) {
          val lastCTE = newMySqlQuery.withs.last { !it.exclude }
          val fk = getLastCteEntityKeyField(lastCTE, queryTypeOfTable)
          newMySqlQuery.insert = "dataset.dataset_results"
          newMySqlQuery.selects.add(MySQLSelect(definition.iri, "query_result_id"))
          newMySqlQuery.selects.add(MySQLSelect("${lastCTE.alias}.$fk", ENTITY_ID_FIELD))
          newMySqlQuery.selects.add(MySQLSelect("'${columnGroup.name.replace(" ", "")}'", "column_group"))
          newMySqlQuery.selects.add(MySQLSelect(DatasetJsonBuilder.build(newMySqlQuery), "json"))
        }
        filterInjector.injectOrgReturnAndFilter(newMySqlQuery)
        filterInjector.injectPatientFilter(newMySqlQuery)
      }
      return mySQLQueries.joinToString(separator = "\n----------------------------------------\n") { it.toSql() }
    } else {
      if (definition.`return` == null) {
        val lastCTE = mySqlQuery.withs.last { !it.exclude }
        val fk = getLastCteEntityKeyField(lastCTE, queryTypeOfTable)
        mySqlQuery.insert = "dataset.cohort_results"
        mySqlQuery.selects.add(MySQLSelect(definition.iri, "query_result_id"))
        mySqlQuery.selects.add(MySQLSelect("${lastCTE.alias}.$fk", ENTITY_ID_FIELD))
      }
      filterInjector.injectOrgReturnAndFilter(mySqlQuery)
      filterInjector.injectPatientFilter(mySqlQuery)
      return mySqlQuery.toSql()
    }
  }

  private fun generatePatientTraceSQL(definition: Query, patientId: String): String {
    if (definition.columnGroup != null) {
      throw SQLConversionException("Patient trace debugging is not supported for column-group (dataset) queries")
    }
    if (definition.iri == null) {
      throw SQLConversionException("Query iri is null")
    }
    aliases.reset()
    nodePathContextMap.clear()
    keepAsMap.clear()
    val mySqlQuery = MySQLQuery()
    if (definition.typeOf == null || definition.typeOf.iri == null) {
      throw SQLConversionException("Query typeOf is null")
    }

    matchCompiler.addMatchWithsRecursively(definition, mySqlQuery)
    if (mySqlQuery.withs.isEmpty()) {
      throw SQLConversionException("Query produced no steps to trace")
    }

    val patientTable = getTableFromTypeAndProperty("${NAMESPACE.IM.asIri().iri}Patient", null)
    val isPatientRooted = queryTypeOfTable.dataModel == patientTable.dataModel
    return PatientTraceSqlBuilder.build(mySqlQuery.withs, definition.iri, patientId, isPatientRooted)
  }

  private fun getTableFromTypeAndProperty(typeIri: String?, propertyIri: String?): Table {
    return IMtoMySQLMap.getTableFromProperty(listOfNotNull(propertyIri))
      ?: IMtoMySQLMap.getTableFromDataModel(typeIri)
      ?: throw SQLConversionException("Type $typeIri not found in table map")
  }

  companion object {
    @JvmStatic
    fun preassignOrderByAs(query: Query?) {
      var unknownCounter = 0
      fun walk(match: Query?) {
        if (match == null) return
        if (match.orderBy != null && match.`as` == null) {
          unknownCounter++
          match.setAs("cte_$unknownCounter")
        }
        match.and?.forEach { walk(it) }
        match.or?.forEach { walk(it) }
        match.rule?.forEach { walk(it) }
        match.columnGroup?.forEach { walk(it) }
      }
      walk(query)
    }
  }
}