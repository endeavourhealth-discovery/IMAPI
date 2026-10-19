package org.endeavourhealth.imapi.transforms.imqtosql

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.*
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.imapi.model.sql.MappingParser
import org.endeavourhealth.imapi.model.sql.MySQLQuery
import org.endeavourhealth.imapi.model.sql.MySQLSelect
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.endeavourhealth.imapi.model.sql.Table
import org.endeavourhealth.imapi.model.sql.TableMap
import org.endeavourhealth.imapi.model.sql.quoteSqlString
import org.endeavourhealth.imapi.vocabulary.NAMESPACE

class IMQtoSQLConverterKotlin @JvmOverloads constructor(
  val queryRequest: QueryRequest, val mapper: ObjectMapper? = null,
  val denominator: String? = null, val numerator: String? = null, val dataset: String? = null,
  val debugPatientId: String? = null
) {
  private val imToMySqlMap: TableMap get() = tableMap
  var sql: String? = null
  var queryTypeOf: String? = queryRequest.query?.typeOf?.iri
  var mySQLQueries: MutableList<MySQLQuery> = mutableListOf()
  var queryTypeOfTable = Table()
  private val aliases = AliasAllocator()

  private val nodePathContextMap = HashMap<String, NodePathContext>()
  private val pathJoinBuilder = PathJoinBuilder(nodePathContextMap, ::findTableFromTypeAndProperty)
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
      log.error("SQL Conversion Error", e)
      throw e
    } catch (e: JsonProcessingException) {
      log.error("SQL Conversion Error", e)
      throw e
    } catch (e: IllegalArgumentException) {
      log.error("Invalid query for SQL conversion", e)
      throw e
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
  }

  private companion object {
    private val log = LoggerFactory.getLogger(IMQtoSQLConverterKotlin::class.java)

    /**
     * Parsed once and shared by every conversion. It is read-only after parsing: [TableMap.getTableFromDataModel]
     * hands out fresh [Table] instances, so per-query alias/condition changes never touch the shared map.
     */
    private val tableMap: TableMap by lazy { MappingParser().parse("IMQtoMYSQL.json") }
  }

  private fun resetPerQueryState() {
    aliases.reset()
    nodePathContextMap.clear()
    keepAsMap.clear()
  }

  private fun generateSQL(definition: Query): String {
    resetPerQueryState()
    val mySqlQuery = MySQLQuery()
    if (definition.typeOf == null || definition.typeOf.iri == null) {
      throw SQLConversionException("Query typeOf is null")
    }

    if (definition.columnGroup != null) {
      for ((index, columnGroup) in definition.columnGroup.withIndex()) {
        resetPerQueryState()
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
          val lastCTE = newMySqlQuery.withs.lastIncluded()
          val fk = getLastCteEntityKeyField(lastCTE, queryTypeOfTable)
          newMySqlQuery.insert = "dataset.dataset_results"
          newMySqlQuery.selects.add(MySQLSelect(SqlLiteralUtils.toSqlLiteral(definition.iri), "query_result_id"))
          newMySqlQuery.selects.add(MySQLSelect("${lastCTE.alias}.$fk", ENTITY_ID_FIELD))
          newMySqlQuery.selects.add(MySQLSelect(quoteSqlString(columnGroup.name.replace(" ", "")), "column_group"))
          newMySqlQuery.selects.add(MySQLSelect(DatasetJsonBuilder.build(newMySqlQuery), "json"))
        }
        filterInjector.injectOrgReturnAndFilter(newMySqlQuery)
        filterInjector.injectPatientFilter(newMySqlQuery)
      }
      return mySQLQueries.joinToString(separator = "\n----------------------------------------\n") { it.toSql() }
    } else {
      // column groups each compile their own query, so the main tree is only needed when there are none
      matchCompiler.addMatchWithsRecursively(definition, mySqlQuery)
      if (definition.`return` == null) {
        val lastCTE = mySqlQuery.withs.lastIncluded()
        val fk = getLastCteEntityKeyField(lastCTE, queryTypeOfTable)
        mySqlQuery.insert = "dataset.cohort_results"
        mySqlQuery.selects.add(MySQLSelect(SqlLiteralUtils.toSqlLiteral(definition.iri), "query_result_id"))
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
    resetPerQueryState()
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
    return findTableFromTypeAndProperty(typeIri, propertyIri)
      ?: throw SQLConversionException("Type $typeIri not found in table map")
  }

  private fun findTableFromTypeAndProperty(typeIri: String?, propertyIri: String?): Table? =
    imToMySqlMap.findTable(typeIri, propertyIri)
}
