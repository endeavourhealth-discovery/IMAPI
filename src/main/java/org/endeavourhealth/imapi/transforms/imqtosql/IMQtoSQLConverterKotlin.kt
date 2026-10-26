package org.endeavourhealth.imapi.transforms.imqtosql

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.IMQType
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.imapi.model.sql.*
import org.endeavourhealth.imapi.vocabulary.NAMESPACE
import org.slf4j.LoggerFactory

class IMQtoSQLConverterKotlin @JvmOverloads constructor(
  val queryRequest: QueryRequest, val mapper: ObjectMapper? = null,
  val denominator: String? = null, val numerator: String? = null, val dataset: String? = null,
  val debugPatientId: String? = null
) {
  var sql: String? = null
  var queryTypeOf: String? = queryRequest.query?.typeOf?.iri
  val mySQLQueries: MutableList<MySQLQuery> = mutableListOf()

  private val ctx = ConversionContext(tableMap)
  val queryTypeOfTable: Table get() = ctx.queryTypeOfTable

  private val keepAsLookup = KeepAsLookup(queryRequest)
  private val filterInjector = FilterInjector(queryRequest, ctx)
  private val matchCompiler = MatchTreeCompiler(
    ctx,
    SelectClauseCompiler(ctx),
    WhereClauseCompiler(ctx, keepAsLookup::getDataModelFromKeepAs),
    PathJoinBuilder(ctx),
  )

  init {
    require(queryRequest.query != null) { "Query request must have a query body" }
  }

  init {
    try {
      if (debugPatientId != null) {
        ctx.queryTypeOfTable = requireQueryTypeTable()
        sql = generatePatientTraceSQL(queryRequest.query, debugPatientId)
      } else if (queryRequest.query.queryType == IMQType.INDICATOR) sql = IndicatorSqlGenerator.generate(denominator, numerator, dataset)
      else {
        ctx.queryTypeOfTable = requireQueryTypeTable()
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

  private fun requireQueryTypeTable(): Table {
    require(queryTypeOf != null) { "Queries need a type" }
    return ctx.getTable(queryTypeOf, null)
  }

  private fun generateSQL(definition: Query): String {
    ctx.resetPerQueryState()
    requireTypeOf(definition)

    if (definition.columnGroup != null) {
      // each column group compiles its own query
      for ((index, columnGroup) in definition.columnGroup.withIndex()) {
        ctx.resetPerQueryState()
        val groupQuery = MySQLQuery()
        val groupName = columnGroup.name ?: "ColumnGroup$index"
        mySQLQueries.add(groupQuery)
        if (definition.`is` != null) groupQuery.withs.add(matchCompiler.getIsWith(definition, groupQuery))
        matchCompiler.addMatchWithsRecursively(columnGroup, groupQuery)
        if (columnGroup.and == null && columnGroup.or == null && columnGroup.`is` == null) {
          groupQuery.withs.add(matchCompiler.buildChainedWith(columnGroup, groupQuery))
        }
        if (definition.`return` == null) {
          addResultSelects(groupQuery, DATASET_RESULTS_TABLE, definition)
          groupQuery.selects.add(MySQLSelect(quoteSqlString(groupName), "column_group"))
          groupQuery.selects.add(MySQLSelect(DatasetJsonBuilder.build(groupQuery), "json"))
        }
        addFilters(groupQuery)
      }
      return mySQLQueries.joinToString(separator = "\n----------------------------------------\n") { it.toSql() }
    }

    val mySqlQuery = MySQLQuery()
    matchCompiler.addMatchWithsRecursively(definition, mySqlQuery)
    if (definition.`return` == null) addResultSelects(mySqlQuery, COHORT_RESULTS_TABLE, definition)
    addFilters(mySqlQuery)
    return mySqlQuery.toSql()
  }

  /** Makes the query insert its rows, tagged with the query's iri, into [insertTable]. */
  private fun addResultSelects(query: MySQLQuery, insertTable: String, definition: Query) {
    val queryIri = definition.iri ?: throw SQLConversionException("Query iri is null")
    val lastCTE = query.withs.lastIncluded()
    val fk = getLastCteEntityKeyField(lastCTE, ctx.queryTypeOfTable)
      ?: throw SQLConversionException("No entity key on ${lastCTE.alias}")
    query.insert = insertTable
    query.selects.add(MySQLSelect(SqlLiteralUtils.queryResultIdToken(queryIri), QUERY_RESULT_ID_FIELD))
    query.selects.add(MySQLSelect("${lastCTE.alias}.$fk", ENTITY_ID_FIELD))
  }

  private fun addFilters(query: MySQLQuery) {
    filterInjector.injectOrgReturnAndFilter(query)
    filterInjector.injectPatientFilter(query)
  }

  private fun requireTypeOf(definition: Query) {
    if (definition.typeOf == null || definition.typeOf.iri == null) {
      throw SQLConversionException("Query typeOf is null")
    }
  }

  private fun generatePatientTraceSQL(definition: Query, patientId: String): String {
    if (definition.columnGroup != null) {
      throw SQLConversionException("Patient trace debugging is not supported for column-group (dataset) queries")
    }
    if (definition.iri == null) {
      throw SQLConversionException("Query iri is null")
    }
    ctx.resetPerQueryState()
    requireTypeOf(definition)

    val mySqlQuery = MySQLQuery()
    matchCompiler.addMatchWithsRecursively(definition, mySqlQuery)
    if (mySqlQuery.withs.isEmpty()) {
      throw SQLConversionException("Query produced no steps to trace")
    }

    val patientTable = ctx.getTable("${NAMESPACE.IM.asIri().iri}Patient", null)
    val isPatientRooted = ctx.queryTypeOfTable.dataModel == patientTable.dataModel
    return PatientTraceSqlBuilder.build(mySqlQuery.withs, definition.iri, patientId, isPatientRooted)
  }
}
