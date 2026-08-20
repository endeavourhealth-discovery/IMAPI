package org.endeavourhealth.imapi.model.sql

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.extern.slf4j.Slf4j
import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.*
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.imapi.vocabulary.IM
import org.endeavourhealth.imapi.vocabulary.NAMESPACE
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale.getDefault

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
  private val MAX_ALIAS_LENGTH = 64
  private var longAliasCounter = 1
  private val usedAliases = mutableSetOf<String>()
  private val carryPropertiesStack = ArrayDeque<List<String>>()
  private val DATE_FORMATS = listOf(
    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
    DateTimeFormatter.ofPattern("dd/MM/yyyy"),
    DateTimeFormatter.ofPattern("MM/dd/yyyy"),
    DateTimeFormatter.ofPattern("dd-MM-yyyy"),
    DateTimeFormatter.ofPattern("MM-dd-yyyy"),
    DateTimeFormatter.ofPattern("d/M/yyyy"),
    DateTimeFormatter.ofPattern("yyyy/MM/dd"),
  )
  private val SQL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  private data class NodePathContext(
    val parentTable: Table,
    val pathIri: String,
    val nodeTable: Table
  )

  private val nodePathContextMap = HashMap<String, NodePathContext>()

  init {
    require(queryRequest.query != null) { "Query request must have a query body" }
  }

  init {
    try {
      if (debugPatientId != null) {
        require(queryTypeOf != null) { "Queries need a type" }
        queryTypeOfTable = getTableFromTypeAndProperty(queryTypeOf, null)
        sql = generatePatientTraceSQL(queryRequest.query, debugPatientId)
      } else if (queryRequest.query.queryType == IMQType.INDICATOR) sql = generateSQLforIndicator()
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

  private fun generateSQLforIndicator(): String {
    if (denominator == null || numerator == null || dataset == null) {
      throw SQLConversionException("Missing denominator, numerator or dataset")
    }
    val indSql = """
      SELECT c.entity_id, !ISNULL(n.entity_id) as "Yes/No", d.json
      FROM dataset.cohort_results c
      LEFT JOIN dataset.cohort_results n ON n.entity_id = c.entity_id AND n.query_result_id = $numerator
      LEFT JOIN dataset.dataset_results d ON d.entity_id = c.entity_id AND d.query_result_id = $dataset
      WHERE c.query_result_id = $denominator;
    """.trimIndent()
    return indSql
  }

  private fun generateSQL(definition: Query): String {
    usedAliases.clear()
    nodePathContextMap.clear()
    val mySqlQuery = MySQLQuery()
    if (definition.typeOf == null || definition.typeOf.iri == null) {
      throw SQLConversionException("Query typeOf is null")
    }

    addMatchWithsRecursively(definition, mySqlQuery)

    if (definition.columnGroup != null) {
      for ((index, columnGroup) in definition.columnGroup.withIndex()) {
        usedAliases.clear()
        nodePathContextMap.clear()
        val newMySqlQuery = MySQLQuery()
        if (columnGroup.name == null) columnGroup.name = "ColumnGroup$index"
        mySQLQueries.add(newMySqlQuery)
        if (definition.`is` != null) newMySqlQuery.withs.add(getIsWith(definition, newMySqlQuery))
        addMatchWithsRecursively(columnGroup, newMySqlQuery)
        if (columnGroup.and == null && columnGroup.or == null &&
          columnGroup.`is` == null
        ) {
          val with = getMySQLWithFromMatch(columnGroup, newMySqlQuery)
          if (columnGroup.orderBy == null && newMySqlQuery.withs.isNotEmpty()) {
            with.joins.add(getJoinBetweenWiths(with, newMySqlQuery.withs.last()))
          }
          newMySqlQuery.withs.add(with)
        }
        if (definition.`return` == null) {
          val lastCTE = newMySqlQuery.withs.last { !it.exclude }
          val (fk, _) = if (lastCTE.table.table == queryTypeOfTable.table)
            queryTypeOfTable.primaryKey to queryTypeOfTable.primaryKey
          else lastCTE.table.foreignKeyTo(queryTypeOfTable)
          newMySqlQuery.insert = "dataset.dataset_results"
          newMySqlQuery.selects.add(MySQLSelect(definition.iri, "query_result_id"))
          newMySqlQuery.selects.add(MySQLSelect("${lastCTE.alias}.$fk", "entity_id"))
          newMySqlQuery.selects.add(MySQLSelect("'${columnGroup.name.replace(" ", "")}'", "column_group"))
          newMySqlQuery.selects.add(MySQLSelect(getJSONObject(newMySqlQuery), "json"))
        }
        injectOrgReturnAndFilter(newMySqlQuery)
        injectPatientFilter(newMySqlQuery)
      }
      return mySQLQueries.joinToString(separator = "\n----------------------------------------\n") { it.toSql() }
    } else {
      if (definition.`return` == null) {
        val lastCTE = mySqlQuery.withs.last { !it.exclude }
        val (fk, _) = if (lastCTE.table.table == queryTypeOfTable.table)
          queryTypeOfTable.primaryKey to queryTypeOfTable.primaryKey
        else lastCTE.table.foreignKeyTo(queryTypeOfTable)
        mySqlQuery.insert = "dataset.cohort_results"
        mySqlQuery.selects.add(MySQLSelect(definition.iri, "query_result_id"))
        mySqlQuery.selects.add(MySQLSelect("${lastCTE.alias}.$fk", "entity_id"))
      }
      injectOrgReturnAndFilter(mySqlQuery)
      injectPatientFilter(mySqlQuery)
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
    usedAliases.clear()
    nodePathContextMap.clear()
    val mySqlQuery = MySQLQuery()
    if (definition.typeOf == null || definition.typeOf.iri == null) {
      throw SQLConversionException("Query typeOf is null")
    }

    addMatchWithsRecursively(definition, mySqlQuery)
    if (mySqlQuery.withs.isEmpty()) {
      throw SQLConversionException("Query produced no steps to trace")
    }

    val patientTable = getTableFromTypeAndProperty("${NAMESPACE.IM.asIri().iri}Patient", null)
    val isPatientRooted = queryTypeOfTable.dataModel == patientTable.dataModel
    val queryIriLiteral = toSqlLiteral(definition.iri)
    val patientLiteral = toSqlLiteral(patientId)

    val checks = mySqlQuery.withs.mapIndexed { index, with ->
      val stepLabel = toSqlLiteral(with.alias.replace("`", ""))
      val keyField = with.entityKeyField
      val patientFoundSelect = if (isPatientRooted && keyField != null) {
        "EXISTS(SELECT 1 FROM ${with.alias} WHERE ${with.alias}.$keyField = $patientLiteral)"
      } else "NULL"
      "SELECT $queryIriLiteral AS query_iri, $patientLiteral AS patient_id, " +
        "$index AS step_no, $stepLabel AS cte_name, $patientFoundSelect AS patient_found"
    }

    return buildString {
      append("INSERT INTO dataset.patient_exists (query_iri, patient_id, step_no, cte_name, patient_found)\n")
      append("WITH ")
      append(mySqlQuery.withs.joinToString(",\n") { it.toSql() })
      append("\n")
      append(checks.joinToString("\nUNION ALL\n"))
      append(";")
    }
  }

  private fun getJSONObject(newMySqlQuery: MySQLQuery): String {
    val lastWith = newMySqlQuery.withs.last()

    val selects = if (
      lastWith.selects.size == 1 &&
      lastWith.selects.first().name.contains("*")
    ) {
      if (lastWith.subQuery != null) {
        lastWith.subQuery?.selects?.filterNot { it.alias == null || it.alias == "rn" }
      } else {
        newMySqlQuery.withs
          .dropLast(1)
          .last()
          .selects
          .filterNot { it.alias == "rn" || it.name == "patient.id" }
      }
    } else {
      lastWith.selects.filterNot { it.name == "patient.id" }
    }

    if (selects == null) throw SQLConversionException("No selects found in last with")

    return buildString {
      append("JSON_OBJECT(\n")
      append(
        selects
          .filterNot { it.alias == "id" }
          .joinToString(",\n") { select ->
            val rawAliasOrName = (select.alias ?: select.name).replace("`", "")
            val key = "\"$rawAliasOrName\""
            val value = if (select.alias != null) {
              "`${rawAliasOrName}`"
            } else {
              select.name
            }
            " $key, $value"
          }
      )
      append("\n)")
    }
  }

  private fun getIsWith(match: Query, mySqlQuery: MySQLQuery): MySQLWith {
    val isA = match.`is`
    val isAlias = ensureUniqueAlias(getCteAliasFromTypeAndProperty(isA.iri, null))
    val withJoins = mutableListOf<MySQLJoin>()
    val cohortTable = getTableFromTypeAndProperty("http://endhealth.info/im#Cohort", null)
    cohortTable.table = "dataset.cohort_results"
    if (mySqlQuery.withs.isNotEmpty()) {
      val lastWith = mySqlQuery.withs.last()
      val (fk, _) =
        if (cohortTable.table == lastWith.table.table)
          lastWith.table.primaryKey to cohortTable.primaryKey
        else
          lastWith.table.foreignKeyTo(cohortTable)
      withJoins.add(
        MySQLJoin(
          "JOIN",
          tableFrom = "dataset.cohort_results",
          tableTo = mySqlQuery.withs.last { !it.exclude }.alias,
          fromProperty = "entity_id",
          toProperty = fk,
          wheres = if (isA.isExclude) mutableListOf(
            MySQLPropertyValueWhere("query_result_id", "=", "${isA.iri}", null, null),
            MySQLPropertyValueWhere("entity_id", "IS", "NULL", null, null)
          ) else mutableListOf(
            MySQLPropertyValueWhere("query_result_id", "=", "${isA.iri}", null, null),
          )
        )
      )
    }

    val topWheres = if (withJoins.isEmpty()) mutableListOf<MySQLWhere>(
      MySQLPropertyValueWhere("query_result_id", "=", "${isA.iri}", null, null),
    ) else mutableListOf()

    return MySQLWith(
      table = cohortTable,
      alias = isAlias,
      selects = mutableListOf(MySQLSelect("${cohortTable.table}.entity_id")),
      joins = withJoins.ifEmpty { mutableListOf() },
      exclude = isA.isExclude,
      wheres = topWheres,
      entityKeyField = "entity_id",
      isCohortRef = true
    )
  }

  private fun getCteAliasFromTypeAndProperty(typeIri: String?, propertyIri: String?): String {
    val typeIriSuffix = typeIri?.substringAfter('#')
    if (propertyIri == null) return "${typeIriSuffix}_cte"
    val propertyIriSuffix = propertyIri.substringAfter('#')
    return "${typeIriSuffix}_${propertyIriSuffix}_cte"
  }

  private fun addMatchWithsRecursively(
    currentMatch: Query,
    mySqlQuery: MySQLQuery,
  ) {
    if (currentMatch.and != null) addAnds(currentMatch, mySqlQuery)
    if (currentMatch.or != null) addOrs(currentMatch, mySqlQuery)
    if (currentMatch.`is` != null) mySqlQuery.withs.add(getIsWith(currentMatch, mySqlQuery))
  }


  private fun addAnds(currentMatch: Query, mySqlQuery: MySQLQuery) {
    for (match in currentMatch.and) {
      addMatchWithsRecursively(match, mySqlQuery)
      if (match.and == null && match.or == null && match.`is` == null) {
        val with = getMySQLWithFromMatch(match, mySqlQuery)
        if (match.orderBy == null && mySqlQuery.withs.isNotEmpty()) {
          with.joins.add(getJoinBetweenWiths(with, mySqlQuery.withs.last()))
        }
        mySqlQuery.withs.add(with)
      }
    }
  }

  private fun addOrs(currentMatch: Query, mySqlQuery: MySQLQuery) {
    val orWiths = mutableListOf<MySQLWith>()
    val tempQuery = MySQLQuery()
    tempQuery.nodeToTableMap.putAll(mySqlQuery.nodeToTableMap)
    if (mySqlQuery.withs.isNotEmpty()) tempQuery.withs.add(mySqlQuery.withs.last())

    val carryProperties = getGroupCarryProperties(currentMatch)
    if (carryProperties.isNotEmpty()) carryPropertiesStack.addLast(carryProperties)
    try {
      for (match in currentMatch.or) {
        val branchQuery = MySQLQuery()
        branchQuery.nodeToTableMap.putAll(tempQuery.nodeToTableMap)
        if (tempQuery.withs.isNotEmpty()) branchQuery.withs.add(tempQuery.withs.last())
        addMatchWithsRecursively(match, branchQuery)

        if (match.and == null && match.or == null && match.`is` == null) {
          val with = getMySQLWithFromMatch(match, branchQuery)
          if (match.orderBy == null && branchQuery.withs.isNotEmpty()) {
            with.joins.add(getJoinBetweenWiths(with, branchQuery.withs.last()))
          }
          branchQuery.withs.add(with)
        } else {
          val newWiths = branchQuery.withs.drop(tempQuery.withs.size)
          mySqlQuery.withs.addAll(newWiths)
        }

        orWiths.add(normaliseForUnion(branchQuery.withs.last(), carryProperties))
        mySqlQuery.nodeToTableMap.putAll(branchQuery.nodeToTableMap)
      }
    } finally {
      if (carryProperties.isNotEmpty()) carryPropertiesStack.removeLast()
    }

    if (orWiths.size == 1 && currentMatch.orderBy == null) return

    val unionWith = if (orWiths.size == 1) orWiths.first() else MySQLWith(
      alias = ensureUniqueAlias("union_${mySqlQuery.withs.size}"),
      table = orWiths.first().table,
      unionWiths = orWiths,
      entityKeyField = orWiths.first().entityKeyField
    )

    mySqlQuery.withs.add(
      if (currentMatch.orderBy != null) wrapGroupOrderBy(unionWith, currentMatch) else unionWith
    )
  }

  private fun normaliseForUnion(with: MySQLWith, carryProperties: List<String> = emptyList()): MySQLWith {
    val keyField = with.entityKeyField ?: return with

    if (carryProperties.isEmpty()) {
      val onlySelect = with.selects.singleOrNull()
      if (onlySelect != null && onlySelect.alias == null && !onlySelect.name.contains("*")) return with
      return MySQLWith(
        table = with.table,
        alias = with.alias,
        selects = mutableListOf(MySQLSelect("t.$keyField")),
        subQuery = with,
        fromAlias = "t",
        entityKeyField = keyField
      )
    }

    val selects = mutableListOf(MySQLSelect("t.$keyField"))
    for (propIri in carryProperties) {
      val alias = propIri.substringAfterLast('#')
      selects.add(if (with.isCohortRef) MySQLSelect("NULL", alias) else MySQLSelect("t.$alias", alias))
    }
    return MySQLWith(
      table = with.table,
      alias = with.alias,
      selects = selects,
      subQuery = with,
      fromAlias = "t",
      entityKeyField = keyField
    )
  }

  private fun getGroupCarryProperties(match: Query): List<String> {
    if (match.orderBy == null) return emptyList()
    val props = linkedSetOf<String>()
    match.orderBy.property.forEach { props.add(it.iri) }
    match.then?.where?.let { props.addAll(getPropsUsedInThen(it)) }
    return props.toList()
  }

  private fun wrapGroupOrderBy(with: MySQLWith, match: Query): MySQLWith {
    val keyField = with.entityKeyField ?: throw SQLConversionException("Group order-by requires an entity key")
    val orderBy = match.orderBy ?: throw SQLConversionException("wrapGroupOrderBy called without an orderBy")
    val orderClause = orderBy.property.joinToString(", ") { p ->
      "${p.iri.substringAfterLast('#')} ${if (p.direction == Order.descending) "DESC" else "ASC"}"
    }

    val rnLayer = MySQLWith(
      table = with.table,
      selects = mutableListOf(
        MySQLSelect("u.*"),
        MySQLSelect("ROW_NUMBER() OVER(PARTITION BY u.$keyField ORDER BY $orderClause)", "rn")
      ),
      subQuery = with,
      fromAlias = "u",
      entityKeyField = keyField
    )

    val finalWith = MySQLWith(
      table = with.table,
      alias = with.alias,
      selects = mutableListOf(MySQLSelect("sq.*")),
      subQuery = rnLayer,
      fromAlias = "sq",
      wheres = mutableListOf(MySQLPropertyValueWhere("rn", "<=", orderBy.limit.toString(), table = "sq")),
      entityKeyField = keyField
    )

    match.then?.where?.let { finalWith.wheres.add(buildBarePropertyWhere(it)) }
    return finalWith
  }

  private fun buildBarePropertyWhere(where: Where): MySQLWhere {
    if (where.iri == null || where.and != null || where.or != null || where.`is` != null || where.compare != null) {
      throw SQLConversionException("Unsupported group-level 'then' clause: $where")
    }
    val alias = where.iri.substringAfterLast('#')
    return MySQLPropertyValueWhere(alias, where.operator.value, toSqlLiteral(where.value), not = where.isNot)
  }

  private fun getMySQLWithFromMatch(match: Query, mySQLQuery: MySQLQuery): MySQLWith {
    var with = MySQLWith()

    if (match.typeOf?.iri != null) {
      with.table = getTableFromTypeAndProperty(match.typeOf.iri, null)
    } else {
      with.table = if (match.from != null)
        mySQLQuery.nodeToTableMap[match.from]
          ?: throw SQLConversionException("Table not found: ${match.from}")
      else queryTypeOfTable
    }

    if (match.path != null) addPathTableAndJoins(match.path, mySQLQuery.nodeToTableMap, with, addJoins = true)
    if (match.node != null) mySQLQuery.nodeToTableMap[match.node] = with.table
    with.alias = getWithAlias(match, mySQLQuery)

    if (match.where != null) {
      addWheresRecursively(
        where = match.where,
        with = with,
        variableToTableMap = mySQLQuery.nodeToTableMap,
        parentWhere = null,
        bool = null
      )
    }

    addSelects(match, mySQLQuery, with)

    if (match.orderBy != null) {
      with = getOrderByWith(with, match, mySQLQuery)
    }
    return with
  }

  private fun getJoinBetweenWiths(fromWith: MySQLWith, toWith: MySQLWith): MySQLJoin {
    val (fk, pk) =
      if (fromWith.table.table == toWith.table.table) {
        val (ffk, _) = fromWith.table.foreignKeyTo(queryTypeOfTable)
          .takeIf { it.first != null }
          ?: (fromWith.table.primaryKey to fromWith.table.primaryKey)
        ffk to ffk
      } else {
        fromWith.table.foreignKeyTo(toWith.table)
      }

    if (fk == null || pk == null) {
      throw SQLConversionException(
        "No relationship between ${fromWith.table.table} and ${toWith.table}"
      )
    }

    return if (toWith.exclude) {
      MySQLJoin(
        join = "LEFT JOIN",
        tableFrom = fromWith.table.alias ?: fromWith.table.table,
        tableTo = toWith.table.table,
        tableToAlias = toWith.alias,
        fromProperty = fk,
        toProperty = pk,
        reference = true
      ).apply {
        wheres.add(MySQLPropertyValueWhere("${toWith.alias}.$pk", "IS", "NULL"))
      }
    } else {
      MySQLJoin(
        join = "JOIN",
        tableFrom = fromWith.table.alias ?: fromWith.table.table,
        tableTo = toWith.table.table,
        tableToAlias = toWith.alias,
        fromProperty = fk,
        toProperty = pk,
        reference = true
      )
    }
  }

  private fun injectPatientFilter(mySqlQuery: MySQLQuery) {
    val patientTable = getTableFromTypeAndProperty("${NAMESPACE.IM.asIri().iri}Patient", null)
    val found = mySqlQuery.joins.find { it.tableTo == "patient" }
    if (found != null) {
      getPatientFilterWhere(patientTable)?.let { found.wheres.add(it) }
    } else {
      val lastCTE = mySqlQuery.withs.last { !it.exclude }
      val (fk, _) = if (lastCTE.table.table == patientTable.table)
        patientTable.primaryKey to patientTable.primaryKey
      else lastCTE.table.foreignKeyTo(patientTable)

      val orgJoin = MySQLJoin(
        join = "JOIN",
        tableFrom = lastCTE.alias,
        tableTo = patientTable.table,
        fromProperty = fk,
        toProperty = patientTable.primaryKey,
        reference = true,
      )
      getPatientFilterWhere(patientTable)?.let { orgJoin.wheres.add(it) }
      mySqlQuery.joins.add(orgJoin)
    }
  }

  private fun getPatientFilterWhere(table: Table): MySQLWhere? {
    val patientIds = queryRequest.getArgumentDataList("\$patientId")
    if (!patientIds.isNullOrEmpty()) {
      return MySQLPropertyValueWhere(
        property = table.primaryKey,
        operator = if (patientIds.size == 1) "=" else "IN",
        value = patientIds.joinToString(prefix = "(", separator = ",", postfix = ")"),
        table = table.table,
      )
    }
    return null
  }

  private fun injectOrgReturnAndFilter(mySqlQuery: MySQLQuery) {
    val joinTable = getTableFromTypeAndProperty(queryTypeOfTable.dataModel, null)
    val lastCTE = mySqlQuery.withs.last { !it.exclude }
    val (fk, _) = if (lastCTE.table.table == queryTypeOfTable.table)
      queryTypeOfTable.primaryKey to queryTypeOfTable.primaryKey
    else lastCTE.table.foreignKeyTo(queryTypeOfTable)

    val orgJoin = MySQLJoin(
      join = "JOIN",
      tableFrom = lastCTE.alias,
      tableTo = joinTable.table,
      fromProperty = fk,
      toProperty = joinTable.primaryKey,
      reference = true,
    )
    getOrgFilterWhere()?.let { orgJoin.wheres.add(it) }
    mySqlQuery.joins.add(orgJoin)
    mySqlQuery.selects.add(MySQLSelect("${queryTypeOfTable.table}.organization_id", "entity_org_id"))
  }

  private fun getOrgFilterWhere(): MySQLWhere? {
    val orgIds = queryRequest.getArgumentDataList("\$organisationId")
    if (!orgIds.isNullOrEmpty()) {
      return MySQLPropertyValueWhere(
        property = "organization_id",
        operator = if (orgIds.size == 1) "=" else "IN",
        value = "\$organisationId",
        table = queryTypeOfTable.table
      )
    }
    return null
  }

  private fun getOrderByWith(with: MySQLWith, match: Query, mySQLQuery: MySQLQuery): MySQLWith {
    val (fk, pk) = if (with.table.table == queryTypeOfTable.table) {
      with.table.primaryKey to with.table.primaryKey
    } else {
      with.table.foreignKeyTo(queryTypeOfTable)
    }

    if (fk == null || pk == null) {
      throw SQLConversionException(
        "No relationship between ${with.table.table} and ${mySQLQuery.withs.last().table.table}"
      )
    }

    val partitionByField = "${with.table.alias ?: with.table.table}.$fk"
    with.selects.add(
      MySQLSelect(
        "ROW_NUMBER() OVER(PARTITION BY $partitionByField ${
          getMySQLOrderBy(with.table, match.orderBy, mySQLQuery.nodeToTableMap).toSql()
        })", "rn"
      )
    )

    val select = if (match.notExists()) {
      if (mySQLQuery.withs.last().table.dataModel == "http://endhealth.info/im#Cohort") "${mySQLQuery.withs.last().alias}.*, ${mySQLQuery.withs.last().alias}.entity_id as patient_id"
      else "${mySQLQuery.withs.last().alias}.*"
    } else "sq.*"


    val entityKeyField = if (match.notExists()) {
      if (mySQLQuery.withs.last().table.dataModel == "http://endhealth.info/im#Cohort") "patient_id"
      else mySQLQuery.withs.last().entityKeyField
    } else with.entityKeyField

    val rnWith = MySQLWith(
      table = with.table,
      alias = with.alias,
      selects = mutableListOf(MySQLSelect(select)),
      wheres = mutableListOf(),
      whereBool = Bool.and,
      subQuery = with,
      entityKeyField = entityKeyField
    )

    val (fkLast, pkLast) = if (mySQLQuery.withs.last().table.table == queryTypeOfTable.table) {
      mySQLQuery.withs.last().table.primaryKey to mySQLQuery.withs.last().table.primaryKey
    } else {
      mySQLQuery.withs.last().table.foreignKeyTo(queryTypeOfTable)
    }

    if (fkLast == null || pkLast == null) {
      throw SQLConversionException(
        "No relationship between ${with.table.table} and ${mySQLQuery.withs.last().table.table}"
      )
    }

    val innerQueryJoin = MySQLJoin(
      join = "JOIN",
      tableFrom = with.table.alias ?: with.table.table,
      tableTo = mySQLQuery.withs.last().alias,
      tableToAlias = mySQLQuery.withs.last().alias,
      fromProperty = fk,
      toProperty = fkLast,
      reference = true
    )

    with.joins.add(innerQueryJoin)

    if (match.notExists()) {
      val notExistJoinCondition = MySQLJoin(
        join = "RIGHT JOIN",
        tableFrom = "sq",
        tableTo = mySQLQuery.withs.last().alias,
        tableToAlias = mySQLQuery.withs.last().alias,
        fromProperty = fk,
        toProperty = fkLast,
        reference = true
      )
      rnWith.joins.add(notExistJoinCondition)
      val or = mutableListOf<MySQLWhere>()
      or.add(MySQLPropertyValueWhere("rn", "!=", match.orderBy.limit.toString(), table = "sq"))
      or.add(MySQLPropertyValueWhere(fk, "IS", "NULL", table = "sq"))
      rnWith.wheres.add(MySQLBoolWhere(or = or))
    } else {
      rnWith.wheres.add(MySQLPropertyValueWhere("rn", "<=", match.orderBy.limit.toString(), table = "sq"))
    }

    if (match.then != null) {
      val properties = getPropsUsedInThen(match.then.where)
      for (property in properties) {
        val field = getPropertyNameByTableAndPropertyIri(with.table, property).field
          ?: throw SQLConversionException("No field found for property $property")
        with.selects.add(MySQLSelect("${with.table.alias ?: with.table.table}.$field"))
      }
      val table = rnWith.table.copy(table = "sq")
      addWheresRecursively(match.then.where, rnWith, mySQLQuery.nodeToTableMap, null, null, table)
    }
    return rnWith
  }

  private fun getPropsUsedInThen(then: Where): MutableSet<String> {
    val properties = mutableSetOf<String>()
    fun collect(where: Where?) {
      if (where == null) return
      where.iri?.let { properties.add(it) }
      where.and?.forEach { collect(it) }
      where.or?.forEach { collect(it) }
      where.range?.from?.compare?.left?.iri?.let { properties.add(it) }
      where.range?.from?.compare?.right?.iri?.let { properties.add(it) }
      where.compare?.left?.iri?.let { properties.add(it) }
      where.compare?.right?.iri?.let { properties.add(it) }
    }
    collect(then)
    return properties
  }

  private fun addSelects(match: Query, mySQLQuery: MySQLQuery, with: MySQLWith) {
    with.selects.add(getDefaultSelect(with.table))
    with.entityKeyField = getEntityKeyFieldName(with.table)
    if (match.`return` != null) {
      val (selects, _) =
        getSelects(
          with.table,
          match.`return`,
          mySQLQuery,
          with.alias,
          mySQLQuery.nodeToTableMap,
        )
      with.selects.addAll(selects)
    }
    for (propIri in carryPropertiesStack.lastOrNull().orEmpty()) {
      val alias = propIri.substringAfterLast('#')
      if (with.selects.none { it.alias == alias }) {
        val field = getPropertyNameByTableAndPropertyIri(with.table, propIri).field
        with.selects.add(MySQLSelect("${with.table.alias ?: with.table.table}.$field", alias))
      }
    }
  }

  private fun getDefaultSelect(table: Table): MySQLSelect {
    val field = getEntityKeyFieldName(table)
    if (table.dataModel == queryTypeOfTable.dataModel)
      return MySQLSelect("${table.table}.$field")
    return MySQLSelect("${table.alias ?: table.table}.$field")
  }

  private fun getEntityKeyFieldName(table: Table): String {
    if (table.dataModel == queryTypeOfTable.dataModel) return queryTypeOfTable.primaryKey
    val (fk, _) = table.foreignKeyTo(queryTypeOfTable)
    return fk ?: throw SQLConversionException(
      "No relationship between ${table.table} and ${queryTypeOfTable.table}"
    )
  }

  private fun getWithAlias(match: Query, mySQLQuery: MySQLQuery): String {
    val baseAlias = if (match.name != null) sanitiseAlias(match.name)
    else if (match.node != null) sanitiseAlias(match.node)
    else "cte_${mySQLQuery.withs.size}"

    return ensureUniqueAlias(baseAlias)
  }

  private fun sanitiseAlias(alias: String): String {
    return alias.replace("...", "_").replace(" ", "_").replace(".", "_").replace("-", "_").lowercase(getDefault())
  }

  private fun getSelects(
    table: Table,
    returx: MutableList<Return>,
    mySqlQuery: MySQLQuery,
    currentWithAlias: String,
    nodeToTableMap: HashMap<String, Table>,
  ): Pair<MutableList<MySQLSelect>, MutableList<MySQLJoin>> {
    val selects = mutableListOf<MySQLSelect>()
    val joins = mutableListOf<MySQLJoin>()

    for (ret in returx) {
      when {
        ret.iri != null -> addSelectFromProperty(ret, selects, nodeToTableMap, table)

        ret.function != null -> {
          if (ret.function.iri == IM.COUNT.toString()) {
            selects.add(MySQLSelect("COUNT(*)", ret.`as`))
          } else {
            selects.add(getFunctionSelect(table, ret, nodeToTableMap))
          }
        }

        ret.case != null -> {
          selects.add(getCaseSelect(ret, table, currentWithAlias, nodeToTableMap))
        }

        ret.value != null -> {
          selects.add(MySQLSelect(toSqlLiteral(ret.value), ret.`as`))
        }

        else -> throw SQLConversionException("Unsupported return $ret")
      }
    }
    return Pair(selects, joins)
  }

  private fun getCaseSelect(
    returnProperty: Return,
    table: Table,
    currentWithAlias: String,
    nodeToTableMap: HashMap<String, Table>
  ): MySQLSelect {
    val caseClause = returnProperty.case
      ?: throw SQLConversionException("Return.case is null")

    val whenClauses = caseClause.`when`
      ?: throw SQLConversionException("Case.when is null")

    if (whenClauses.isEmpty()) {
      throw SQLConversionException("Case.when must contain at least one branch")
    }

    if (
      whenClauses.size == 1 &&
      whenClauses.first().isExists &&
      caseClause.`else` != null
    ) {
      return MySQLSelect(
        getExpressionSql(whenClauses.first().then, table, nodeToTableMap),
        returnProperty.`as`
      )
    }

    val sql = buildString {
      append("CASE ")

      for (whenClause in whenClauses) {
        append("WHEN ")
        append(getWhenConditionSql(whenClause, table, currentWithAlias, nodeToTableMap))
        append(" THEN ")
        append(getExpressionSql(whenClause.then, table, nodeToTableMap))
        append(" ")
      }

      caseClause.`else`?.let {
        append("ELSE ")
        append(getExpressionSql(it, table, nodeToTableMap))
        append(" ")
      }

      append("END")
    }

    return MySQLSelect(sql, returnProperty.`as`)
  }

  private fun getWhenConditionSql(
    whenClause: When,
    currentTable: Table,
    currentWithAlias: String,
    nodeToTableMap: HashMap<String, Table>
  ): String {
    if (whenClause.isExists) {
      val existenceField = if (currentTable.dataModel == queryTypeOfTable.dataModel) {
        "$currentWithAlias.${queryTypeOfTable.primaryKey}"
      } else {
        val fk = currentTable.foreignKeyTo(queryTypeOfTable).first
          ?: throw SQLConversionException("No relationship from ${currentTable.table} to ${queryTypeOfTable.table}")
        "$currentWithAlias.$fk"
      }
      return "$existenceField IS NOT NULL"
    }

    if (whenClause.iri != null) {
      val sourceTable = if (whenClause.nodeRef != null) {
        nodeToTableMap[whenClause.nodeRef]
          ?: throw SQLConversionException("No table found for nodeRef ${whenClause.nodeRef}")
      } else {
        currentTable
      }

      val field = if (whenClause.propertyRef != null) {
        whenClause.propertyRef
      } else {
        getPropertyNameByTableAndPropertyIri(sourceTable, whenClause.iri).field
      } ?: throw SQLConversionException("No field found for when clause ${whenClause.iri}")

      val tableAlias = sourceTable.alias ?: currentWithAlias
      val operator = whenClause.operator?.value ?: "="
      val value = whenClause.value ?: throw SQLConversionException("When clause value is null")

      return "$tableAlias.$field $operator ${toSqlLiteral(value)}"
    }

    if (whenClause.compare != null || whenClause.range != null || whenClause.and != null || whenClause.or != null || whenClause.`is` != null || whenClause.isNull) {
      throw SQLConversionException("Not yet implemented")
    }

    throw SQLConversionException("Unsupported CASE WHEN clause")
  }

  private fun getExpressionSql(
    expression: Expression?,
    currentTable: Table,
    nodeToTableMap: HashMap<String, Table>
  ): String {
    if (expression == null) {
      throw SQLConversionException("CASE expression branch is null")
    }

    expression.value?.let {
      return toSqlLiteral(it)
    }

    val sourceTable = if (expression.nodeRef != null) {
      nodeToTableMap[expression.nodeRef]
        ?: throw SQLConversionException("No table found for nodeRef ${expression.nodeRef}")
    } else {
      currentTable
    }

    if (expression.propertyRef != null) {
      val tableAlias = sourceTable.alias ?: sourceTable.table
      return "$tableAlias.${expression.propertyRef}"
    }

    if (expression.iri != null) {
      val field = getPropertyNameByTableAndPropertyIri(sourceTable, expression.iri).field
      val tableAlias = sourceTable.alias ?: sourceTable.table
      return "$tableAlias.$field"
    }

    throw SQLConversionException("Unsupported CASE expression branch")
  }

  private fun tryParseDate(value: String): LocalDate? {
    for (formatter in DATE_FORMATS) {
      try {
        return LocalDate.parse(value, formatter)
      } catch (_: DateTimeParseException) {
        continue
      }
    }
    return null
  }

  private fun toSqlLiteral(value: String): String {
    if (value.toBigDecimalOrNull() != null) return value

    val date = tryParseDate(value)
    if (date != null) return "'${date.format(SQL_DATE_FORMAT)}'"

    return "'${value.replace("'", "''")}'"
  }

  private fun getFunctionSelect(
    table: Table,
    returnProperty: Return,
    nodeToTableMap: HashMap<String, Table>
  ): MySQLSelect {
    if (returnProperty.function.iri != IM.CONCATENATE.toString()) {
      throw SQLConversionException("Unsupported function ${returnProperty.function.iri}")
    }

    val concatenateFields = mutableListOf<String>()
    for (arg in returnProperty.function.argument) {
      if (arg.valuePath == null) {
        throw SQLConversionException("Missing valuePath for concatenate function argument")
      }

      val field = if (arg.valuePath.nodeRef != null) {
        val currentTable = nodeToTableMap[arg.valuePath.nodeRef]
          ?: throw SQLConversionException(
            "Missing nodeRef from valuePath for concatenate function argument: ${arg.valuePath.nodeRef}"
          )
        val resolved = getPropertyNameByTableAndPropertyIri(currentTable, arg.valuePath.iri).field
        "${currentTable.alias}.$resolved"
      } else {
        getPropertyNameByTableAndPropertyIri(table, arg.valuePath.iri).field
      }

      if (field.isEmpty()) {
        throw SQLConversionException("No field found for concatenate function argument ${arg.valuePath}")
      }
      concatenateFields.add(field)
    }

    return MySQLSelect(
      "CONCAT(${concatenateFields.joinToString(", ")})",
      returnProperty.`as`
    )
  }

  private fun getDegradedNodeRefSelect(
    nodeRef: String,
    returnIri: String
  ): String? {
    val nodeContext = nodePathContextMap[nodeRef] ?: return null
    val sourceField = nodeContext.parentTable.fields[nodeContext.pathIri] ?: return null
    val degraded = sourceField.returnAs?.get(returnIri) ?: return null
    val parentTableRef = nodeContext.parentTable.alias ?: nodeContext.parentTable.table
    return degraded.replace("{table}", parentTableRef)
  }

  private fun addSelectFromProperty(
    returnProperty: Return,
    selects: MutableList<MySQLSelect>,
    nodeToTableMap: HashMap<String, Table>,
    currentWithTable: Table
  ) {
    if (returnProperty.nodeRef != null) {
      getDegradedNodeRefSelect(returnProperty.nodeRef, returnProperty.iri)?.let {
        selects.add(MySQLSelect(it, returnProperty.`as`))
        return
      }

      val currentTable = nodeToTableMap[returnProperty.nodeRef]
        ?: throw SQLConversionException("No table exists for ${returnProperty.iri}")

      val property = getPropertyNameByTableAndPropertyIri(currentTable, returnProperty.iri)
      val field = "${currentTable.alias ?: currentTable.table}.${property.field}"
      selects.add(MySQLSelect(field, returnProperty.`as`))
      return
    }

    val property = getPropertyNameByTableAndPropertyIri(currentWithTable, returnProperty.iri)
    val field = "${currentWithTable.alias ?: currentWithTable.table}.${property.field}"
    selects.add(MySQLSelect(field, returnProperty.`as`))
  }

  private fun findToTable(
    paths: MutableList<Return>,
    startIndex: Int,
    tableMap: TableMap,
    currentIri: String = paths[startIndex].iri
  ): Pair<Table?, Int> {
    val toTable = tableMap.getTableFromProperty(listOf(currentIri))
    if (toTable != null) return toTable to startIndex

    val nextIndex = startIndex + 1
    if (nextIndex >= paths.size) return null to startIndex

    return findToTable(
      paths,
      nextIndex,
      tableMap,
      currentIri + paths[nextIndex].iri
    )
  }

  private fun getMySQLOrderBy(
    table: Table, orderBy: OrderLimit, nodeToTableMap: HashMap<String, Table>,
  ): MySQLOrderBy {
    val items = mutableListOf<MySQLOrderByItem>()
    for (p in orderBy.property) {
      val currentTable =
        if (p.nodeRef != null) nodeToTableMap[p.nodeRef] else table

      if (currentTable == null) throw SQLConversionException("No table exists for ${p.iri}")
      val field = getPropertyNameByTableAndPropertyIri(
        currentTable,
        p.iri
      ).field ?: throw SQLConversionException("No field found for property ${p.iri}")
      items.add(MySQLOrderByItem(field, if (p.direction == Order.descending) "DESC" else "ASC", table = currentTable))
    }
    return MySQLOrderBy(items, orderBy.limit)
  }

  private fun walkMySQLWheres(
    where: MySQLWhere,
    visit: (MySQLWhere) -> Unit
  ) {
    visit(where)
    where.and?.forEach { child -> walkMySQLWheres(child, visit) }
    where.or?.forEach { child -> walkMySQLWheres(child, visit) }
  }

  private fun addWheresRecursively(
    where: Where,
    with: MySQLWith,
    variableToTableMap: HashMap<String, Table>,
    parentWhere: MySQLWhere? = null,
    bool: Bool? = null,
    table: Table? = null,
  ) {
    where.and?.let { andList ->
      val boolWhere = MySQLBoolWhere()
      when (bool) {
        Bool.and -> parentWhere
          ?.also { it.and = it.and ?: mutableListOf() }
          ?.and
          ?.add(boolWhere)

        Bool.or -> parentWhere
          ?.also { it.or = it.or ?: mutableListOf() }
          ?.or
          ?.add(boolWhere)

        else -> with.wheres.add(boolWhere)
      }
      andList.forEach {
        addWheresRecursively(it, with, variableToTableMap, boolWhere, Bool.and, table)
      }
      return
    }

    where.or?.let { orList ->
      val boolWhere = MySQLBoolWhere()
      when (bool) {
        Bool.and -> parentWhere
          ?.also { it.and = it.and ?: mutableListOf() }
          ?.and
          ?.add(boolWhere)

        Bool.or -> parentWhere
          ?.also { it.or = it.or ?: mutableListOf() }
          ?.or
          ?.add(boolWhere)

        else -> with.wheres.add(boolWhere)
      }

      orList.forEach {
        addWheresRecursively(it, with, variableToTableMap, boolWhere, Bool.or, table)
      }
      return
    }

    val leaf = getMySQLWhereFromWhere(where, variableToTableMap, with, table)
    when (bool) {
      Bool.and -> parentWhere
        ?.also { it.and = it.and ?: mutableListOf() }
        ?.and
        ?.add(leaf)

      Bool.or -> parentWhere
        ?.also { it.or = it.or ?: mutableListOf() }
        ?.or
        ?.add(leaf)

      else -> with.wheres.add(leaf)
    }
  }

  private fun resolveUnitAndTypeFromWhere(where: Where, rangeEndUnitsIri: String? = null): Pair<String?, String?> =
    when {
      where.compare?.units?.iri != null -> getUnitNameAndType(where.compare.units.iri)
      rangeEndUnitsIri != null -> getUnitNameAndType(rangeEndUnitsIri)
      where.qualifier?.iri != null -> getUnitNameAndType(where.qualifier.iri)
      else -> null to null
    }

  private fun getMySQLWhereFromWhere(
    where: Where,
    variableToTableMap: HashMap<String, Table>,
    with: MySQLWith,
    table: Table? = null,
  ): MySQLWhere {
    var (currentTable, field) = if (table != null && where.iri != null)
      table to getPropertyNameByTableAndPropertyIri(table, where.iri).field
    else
      getTableAndField(with, where, variableToTableMap)

    if (table != null) currentTable = table
    if (where.propertyRef != null) field = where.propertyRef

    val tableRef = table?.alias ?: table?.table ?: currentTable.alias ?: currentTable.table

    val result = if (where.`is` != null) {
      val (conceptJoins, conceptAlias) = addWhereConceptJoin(currentTable, field, with)
      for (join in conceptJoins) {
        if (!with.joins.contains(join)) with.joins.add(join)
      }
      MySQLPropertyIsWhere(
        field,
        where.`is`,
        "=",
        not = where.isNot,
        table = tableRef,
        conceptTable = conceptAlias,
      )
    } else if (where.range != null) {
      val from = where.range.from
      val to = where.range.to
      val isDirectValue = from.compare == null && from.value != null

      val fromWhere: MySQLWhere
      val toWhere: MySQLWhere

      if (isDirectValue) {
        fromWhere = MySQLPropertyValueWhere(
          property = field,
          operator = from.operator.value,
          value = toSqlLiteral("${from.value}"),
          table = tableRef
        )
        toWhere = MySQLPropertyValueWhere(
          property = field,
          operator = to.operator.value,
          value = toSqlLiteral("${to.value}"),
          table = tableRef
        )
      } else {
        val fromRight = from.compare?.right?.parameter ?: from.compare?.right?.propertyRef
        ?: getValueFromRelativeTo(from, variableToTableMap)
        ?: throw SQLConversionException("No value for range.from")
        val toRight = to.compare?.right?.parameter ?: to.compare?.right?.propertyRef
        ?: getValueFromRelativeTo(to, variableToTableMap)
        ?: throw SQLConversionException("No value for range.to")

        val (fromUnit, fromUnitType) = resolveUnitAndTypeFromWhere(where, where.range.from.compare?.units?.iri)
        val (toUnit, toType) = resolveUnitAndTypeFromWhere(where, where.range.to.compare?.units?.iri)

        fromWhere = MySQLCompareWhere(
          property = field,
          operator = from.operator.value,
          right = fromRight,
          value = toSqlLiteral(from.value),
          table = tableRef,
          units = if (fromUnitType == "Unit") fromUnit else null,
          qualifier = if (fromUnitType == "Qualifier") fromUnit else null,
        )

        toWhere = MySQLCompareWhere(
          property = field,
          operator = to.operator.value,
          right = toRight,
          value = toSqlLiteral(to.value),
          table = tableRef,
          units = if (toType == "Unit") toUnit else null,
          qualifier = if (toType == "Qualifier") toUnit else null,
        )
      }

      MySQLBoolWhere(and = mutableListOf(fromWhere, toWhere))

    } else if (where.isNull) {
      MySQLPropertyIsNullWhere(
        field,
        not = where.isNot,
        table = tableRef,
      )
    } else if (where.compare != null) {
      val (name, type) = resolveUnitAndTypeFromWhere(where)
      val compareValue = where.compare.right.parameter ?: where.compare.right.propertyRef
      ?: getValueFromRelativeTo(where, variableToTableMap)
      ?: throw SQLConversionException("No value provided for where $where")

      if (where.value != null)
        MySQLCompareWhere(
          property = field,
          operator = where.operator.value,
          right = compareValue,
          value = where.value,
          units = if (type == "Unit") name else null,
          qualifier = if (type == "Qualifier") name else null,
          not = where.isNot,
          table = currentTable.alias ?: currentTable.table,
        ) else MySQLPropertyValueWhere(
        property = field,
        operator = where.operator.value,
        value = compareValue,
        qualifier = if (type == "Qualifier") name else null,
        not = where.isNot,
        table = currentTable.alias ?: currentTable.table,
      )
    } else {
      MySQLPropertyValueWhere(
        field,
        where.operator.value,
        toSqlLiteral(where.value),
        not = where.isNot,
        table = tableRef,
      )
    }
    return result
  }

  private fun getTableAndField(
    with: MySQLWith,
    where: Where,
    variableToTableMap: HashMap<String, Table>
  ): Pair<Table, String> {
    val nodeRef = where.compare?.left?.nodeRef ?: where.nodeRef
    val whereIri = where.compare?.left?.iri ?: where.iri ?: where.range?.from?.compare?.left?.iri
    if (whereIri == null) throw SQLConversionException("No property found for where $whereIri")
    val currentTable =
      if (nodeRef != null) variableToTableMap[nodeRef] else with.table
    if (currentTable == null) throw SQLConversionException("No table found: $nodeRef")
    var rawField = getPropertyNameByTableAndPropertyIri(
      currentTable,
      whereIri
    ).field
    if (rawField.isEmpty()) {
      rawField = getPropertyNameByTableAndPropertyIri(currentTable, whereIri).field
        ?: throw SQLConversionException("No field found for property $whereIri")
      val (fk, pk) = with.table.foreignKeyTo(queryTypeOfTable)
      with.joins.add(
        MySQLJoin(
          join = "JOIN",
          with.table.table,
          queryTypeOfTable.table,
          fromProperty = fk,
          toProperty = pk
        )
      )
      rawField = queryTypeOfTable.table + "." + rawField
      return queryTypeOfTable to rawField
    }

    val field =
      if (with.fromAlias != null && nodeRef == null && !rawField.contains(".") && !rawField.contains("(")) {
        "${with.fromAlias}.$rawField"
      } else {
        rawField
      }
    return currentTable to field
  }

  private fun getValueFromRelativeTo(where: Assignable, nodeToTableMap: HashMap<String, Table>): String {
    val nodeRef =
      where.compare.right?.nodeRef
        ?: throw SQLConversionException("No property found for relativeTo ${where.compare.right}")
    var property = ""
    val nodeRefTable = nodeToTableMap[nodeRef]
    if (nodeRefTable != null) {
      property = getPropertyNameByTableAndPropertyIri(nodeRefTable, where.compare.right.iri).field
    } else {
      getDataModelFromKeepAs(nodeRef)?.let {
        property =
          getPropertyNameByTableAndPropertyIri(
            getTableFromTypeAndProperty(it, null),
            where.compare.right.iri
          ).field
      }
    }
    if (property.isEmpty()) throw SQLConversionException("No property found for relativeTo ${where.compare.right.nodeRef}")
    if (nodeToTableMap[nodeRef] != null) return "${sanitiseAlias(nodeRef)}.$property"
    return "`${nodeRef}`.${property}"
  }

  private fun addWhereConceptJoin(table: Table, fromField: String?, with: MySQLWith): Pair<MutableList<MySQLJoin>, String> {
    val joins: MutableList<MySQLJoin> = mutableListOf()

    val conceptTCT = getTableFromTypeAndProperty(IM.CONCEPT.toString() + "TCT", null)
    val bareConceptTableRef = conceptTCT.table.substringAfterLast('.')
    val existingJoinForField = with.joins.firstOrNull {
      it.tableTo == conceptTCT.table && it.fromProperty == fromField
    }
    if (existingJoinForField != null) {
      return joins to (existingJoinForField.tableToAlias?.replace("`", "") ?: bareConceptTableRef)
    }

    val needsAlias = with.joins.any { it.tableTo == conceptTCT.table }
    val alias = if (needsAlias) ensureUniqueAlias("${bareConceptTableRef}_$fromField") else null
    joins.add(
      table.getJoinCondition(
        tableFromAlias = table.alias,
        tableTo = conceptTCT,
        tableToAlias = alias,
        fromField = fromField,
        toField = "im1dbid"
      )
    )
    return joins to (alias?.replace("`", "") ?: bareConceptTableRef)
  }

  private fun getUnitNameAndType(iri: String): Pair<String, String> {
    return when (IM.from(iri)) {
      IM.YEARS -> "YEAR" to "Unit"
      IM.YEAR -> "YEAR" to "Qualifier"
      IM.MONTHS -> "MONTH" to "Unit"
      IM.MONTH -> "MONTH" to "Qualifier"
      IM.DAYS -> "DAY" to "Unit"
      IM.DAY -> "DAY" to "Qualifier"
      IM.HOURS -> "HOUR" to "Unit"
      IM.MINUTES -> "MINUTE" to "Unit"
      IM.SECONDS -> "SECOND" to "Unit"
      IM.FISCAL_YEAR -> "FISCAL_YEAR" to "Qualifier"
      IM.QUARTER -> "QUARTER" to "Qualifier"
      else -> throw SQLConversionException("No unit name found for $iri")
    }
  }

  private fun getPropertyNameByTableAndPropertyIri(table: Table, propertyIri: String): Field {
    val field = table.fields[propertyIri] ?: throw SQLConversionException(
      "Property $propertyIri not found in table ${table.table}"
    )
    return field
  }

  private fun getTableFromTypeAndProperty(typeIri: String?, propertyIri: String?): Table {
    return IMtoMySQLMap.getTableFromProperty(listOfNotNull(propertyIri))
      ?: IMtoMySQLMap.getTableFromDataModel(typeIri)
      ?: throw SQLConversionException("Type $typeIri not found in table map")
  }

  private fun ensureUniqueAlias(baseAlias: String): String {
    fun normalize(a: String) =
      a.replace("`", "").lowercase()

    val alias = baseAlias.replace("`", "")

    if (alias.length > MAX_ALIAS_LENGTH) {
      var newAlias: String
      do {
        newAlias = "cte_${longAliasCounter++}"
      } while (usedAliases.contains(normalize(newAlias)))
      usedAliases.add(normalize(newAlias))
      return "`$newAlias`"
    }

    var uniqueAlias = alias
    var index = 2

    while (
      usedAliases.contains(normalize(uniqueAlias)) ||
      uniqueAlias.length > MAX_ALIAS_LENGTH
    ) {
      uniqueAlias = if (uniqueAlias.length > MAX_ALIAS_LENGTH) {
        "cte_${longAliasCounter++}"
      } else {
        "${alias}_$index"
      }
      index++
    }

    usedAliases.add(normalize(uniqueAlias))
    return "`$uniqueAlias`"
  }

  fun getDataModelFromKeepAs(keepAs: String?): String? {
    var match: Query? = findMatchByKeepAs(queryRequest.query, keepAs)
    if (match == null && queryRequest.query.columnGroup != null) {
      for (child in queryRequest.query.columnGroup) {
        val result: Query? = findMatchByKeepAs(child, keepAs)
        if (result != null) match = result
      }
    }
    if (match != null) {
      if (match.typeOf != null) {
        return match.typeOf.iri
      }
      if (match.path != null) {
        return match.path.first().typeOf.iri
      }
    }
    return null
  }

  fun findMatchByKeepAs(match: Query?, keepAs: String?): Query? {
    if (match == null) return null
    if (match.node != null && match.node == keepAs) {
      return match
    }

    if (match.and != null) {
      for (child in match.and) {
        val result = findMatchByKeepAs(child, keepAs)
        if (result != null) return result
      }
    }

    if (match.or != null) {
      for (child in match.or) {
        val result = findMatchByKeepAs(child, keepAs)
        if (result != null) return result
      }
    }

    return null
  }

  private fun addPathTableAndJoins(
    paths: MutableList<Path>,
    tableMap: HashMap<String, Table>,
    with: MySQLWith,
    parentTable: Table = with.table,
    addJoins: Boolean = false
  ) {

    for (path in paths) {
      try {
        val table = getTableFromTypeAndProperty(path.typeOf.iri, path.iri)
        table.alias = path.node

        val join = parentTable.getJoinCondition(
          joinType = if (path.isOptional) "LEFT JOIN" else "JOIN",
          tableTo = table,
          tableToAlias = table.alias,
          tableFromAlias = parentTable.alias,
        )

        if (table.condition != null) {
          join.wheres.add(
            MySQLPropertyValueWhere(
              property = table.condition!!.field,
              operator = "=",
              value = table.condition!!.value
            )
          )
        }

        tableMap[path.node] = table
        nodePathContextMap[path.node] = NodePathContext(
          parentTable = parentTable,
          pathIri = path.iri,
          nodeTable = table
        )

        if (!with.joins.contains(join) && addJoins) {
          with.joins.add(join)
        }

        if (path.path != null) {
          addPathTableAndJoins(path.path, tableMap, with, table, addJoins)
        }

      } catch (exception: SQLConversionException) {
        tableMap[path.node] = parentTable
        nodePathContextMap[path.node] = NodePathContext(
          parentTable = parentTable,
          pathIri = path.iri,
          nodeTable = parentTable
        )
        if (path.path != null) {
          addPathTableAndJoins(path.path, tableMap, with, parentTable, addJoins)
        }
      }
    }
  }
}