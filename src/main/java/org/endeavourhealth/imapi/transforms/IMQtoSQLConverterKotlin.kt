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
  private val carryPropertiesStack = ArrayDeque<List<String>>()
  private val aliases = AliasAllocator()

  private val nodePathContextMap = HashMap<String, NodePathContext>()
  private val selectCompiler = SelectClauseCompiler({ queryTypeOfTable }, nodePathContextMap)

  private val keepAsMap = HashMap<String, MySQLWith>()
  private val filterInjector = FilterInjector(queryRequest, { queryTypeOfTable }, ::getTableFromTypeAndProperty)
  private val whereCompiler = WhereClauseCompiler(
    { queryTypeOfTable }, keepAsMap, aliases, ::getTableFromTypeAndProperty, ::getDataModelFromKeepAs
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

    addMatchWithsRecursively(definition, mySqlQuery)

    if (definition.columnGroup != null) {
      for ((index, columnGroup) in definition.columnGroup.withIndex()) {
        aliases.reset()
        nodePathContextMap.clear()
        keepAsMap.clear()
        val newMySqlQuery = MySQLQuery()
        if (columnGroup.name == null) columnGroup.name = "ColumnGroup$index"
        mySQLQueries.add(newMySqlQuery)
        if (definition.`is` != null) newMySqlQuery.withs.add(getIsWith(definition, newMySqlQuery))
        addMatchWithsRecursively(columnGroup, newMySqlQuery)
        if (columnGroup.and == null && columnGroup.or == null &&
          columnGroup.`is` == null
        ) {
          newMySqlQuery.withs.add(buildChainedWith(columnGroup, newMySqlQuery))
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

    addMatchWithsRecursively(definition, mySqlQuery)
    if (mySqlQuery.withs.isEmpty()) {
      throw SQLConversionException("Query produced no steps to trace")
    }

    val patientTable = getTableFromTypeAndProperty("${NAMESPACE.IM.asIri().iri}Patient", null)
    val isPatientRooted = queryTypeOfTable.dataModel == patientTable.dataModel
    return PatientTraceSqlBuilder.build(mySqlQuery.withs, definition.iri, patientId, isPatientRooted)
  }

  private fun getIsWith(match: Query, mySqlQuery: MySQLQuery): MySQLWith {
    val isA = match.`is`
    val name = ensureAs(match) { isA.name ?: "cte" }
    val isAlias = aliases.nextCteAlias(name)
    val withJoins = mutableListOf<MySQLJoin>()
    val cohortTable = getTableFromTypeAndProperty(COHORT_DATA_MODEL_IRI, null)
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
          fromProperty = ENTITY_ID_FIELD,
          toProperty = fk,
          wheres = if (isA.isExclude) mutableListOf(
            MySQLPropertyValueWhere("query_result_id", "=", "${isA.iri}", null, null),
            MySQLPropertyValueWhere(ENTITY_ID_FIELD, "IS", "NULL", null, null)
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
      selects = mutableListOf(MySQLSelect("${cohortTable.table}.$ENTITY_ID_FIELD")),
      joins = withJoins.ifEmpty { mutableListOf() },
      exclude = isA.isExclude,
      wheres = topWheres,
      entityKeyField = ENTITY_ID_FIELD,
      isCohortRef = true
    )
  }

  private fun addMatchWithsRecursively(
    currentMatch: Query,
    mySqlQuery: MySQLQuery,
  ) {
    if (currentMatch.notExists() && (currentMatch.and != null || currentMatch.or != null || currentMatch.`is` != null)) {
      addNotExistsGroup(currentMatch, mySqlQuery)
      return
    }
    if (currentMatch.and != null) addAnds(currentMatch, mySqlQuery)
    if (currentMatch.or != null) addOrs(currentMatch, mySqlQuery)
    if (currentMatch.`is` != null) mySqlQuery.withs.add(getIsWith(currentMatch, mySqlQuery))
  }

  private fun addNotExistsGroup(group: Query, mySqlQuery: MySQLQuery) {
    val previous = mySqlQuery.withs.lastOrNull()
      ?: throw SQLConversionException("notExists on a group needs a preceding match to exclude from")
    val groupAs = group.`as`
    val withCount = mySqlQuery.withs.size
    group.setNotExists(false)
    try {
      addMatchWithsRecursively(group, mySqlQuery)
    } finally {
      group.setNotExists(true)
    }
    if (mySqlQuery.withs.size == withCount) {
      throw SQLConversionException("notExists group produced no match to exclude")
    }
    mySqlQuery.withs.add(getAntiJoinWith(groupAs, previous, mySqlQuery.withs.last()))
  }

  private fun getAntiJoinWith(groupAs: String?, previous: MySQLWith, excluded: MySQLWith): MySQLWith {
    val previousKey = getLastCteEntityKeyField(previous, queryTypeOfTable)
    val excludedKey = getLastCteEntityKeyField(excluded, queryTypeOfTable)
    if (previousKey == null || excludedKey == null) {
      throw SQLConversionException("No entity key to exclude ${excluded.alias} from ${previous.alias}")
    }
    val name = groupAs ?: "not_exists"
    return MySQLWith(
      table = previous.table,
      alias = aliases.nextCteAlias(name),
      selects = mutableListOf(MySQLSelect("${previous.alias}.*")),
      wheres = mutableListOf(
        MySQLNotExistsWhere(
          outerTable = previous.alias,
          outerKey = previousKey,
          innerTable = excluded.alias,
          innerKey = excludedKey
        )
      ),
      fromAlias = previous.alias,
      entityKeyField = previousKey,
      isCarrierAliased = previous.isCarrierAliased
    )
  }

  private fun addAnds(currentMatch: Query, mySqlQuery: MySQLQuery) {
    for (match in currentMatch.and) {
      addMatchWithsRecursively(match, mySqlQuery)
      if (match.and == null && match.or == null && match.`is` == null) {
        mySqlQuery.withs.add(buildChainedWith(match, mySqlQuery))
      }
    }
  }

  private fun addOrs(currentMatch: Query, mySqlQuery: MySQLQuery) {
    val branchWiths = mutableListOf<MySQLWith>()
    val branchEmitted = mutableListOf<Boolean>()
    val tempQuery = MySQLQuery()
    tempQuery.nodeToTableMap.putAll(mySqlQuery.nodeToTableMap)
    if (mySqlQuery.withs.isNotEmpty()) tempQuery.withs.add(mySqlQuery.withs.last())

    val explicitCarryProperties = getGroupCarryProperties(currentMatch)
    if (explicitCarryProperties.isNotEmpty()) carryPropertiesStack.addLast(explicitCarryProperties)
    try {
      for (match in currentMatch.or) {
        val branchQuery = MySQLQuery()
        branchQuery.nodeToTableMap.putAll(tempQuery.nodeToTableMap)
        if (tempQuery.withs.isNotEmpty()) branchQuery.withs.add(tempQuery.withs.last())
        addMatchWithsRecursively(match, branchQuery)

        var emitted = false
        if (match.and == null && match.or == null && match.`is` == null) {
          branchQuery.withs.add(buildChainedWith(match, branchQuery))
        } else {
          val newWiths = branchQuery.withs.drop(tempQuery.withs.size)
          mySqlQuery.withs.addAll(newWiths)
          emitted = newWiths.isNotEmpty()
        }

        branchWiths.add(branchQuery.withs.last())
        branchEmitted.add(emitted)
        mySqlQuery.nodeToTableMap.putAll(branchQuery.nodeToTableMap)
      }
    } finally {
      if (explicitCarryProperties.isNotEmpty()) carryPropertiesStack.removeLast()
    }

    val carryAliases = linkedSetOf<String>()
    explicitCarryProperties.forEach { carryAliases.add(it.substringAfterLast('#')) }
    branchWiths.forEach { carryAliases.addAll(getAvailableAliases(it)) }

    val orWiths = branchWiths.mapIndexed { i, with ->
      if (branchEmitted[i]) selectFromEmittedWith(with, carryAliases.toList())
      else normaliseForUnion(with, carryAliases.toList())
    }.toMutableList()

    if (orWiths.size == 1 && currentMatch.orderBy == null) {
      currentMatch.`as`?.let { keepAsMap[it] = branchWiths.first() }
      return
    }

    val unionWith = if (orWiths.size == 1) orWiths.first() else MySQLWith(
      alias = aliases.ensureUniqueAlias("union_${mySqlQuery.withs.size}"),
      table = orWiths.first().table,
      unionWiths = orWiths,
      entityKeyField = orWiths.first().entityKeyField
    )

    val finalWith = if (currentMatch.orderBy != null) wrapGroupOrderBy(unionWith, currentMatch) else unionWith
    finalWith.isCarrierAliased = true
    mySqlQuery.withs.add(finalWith)
    currentMatch.`as`?.let { keepAsMap[it] = finalWith }
  }

  private fun selectFromEmittedWith(with: MySQLWith, carryProperties: List<String>): MySQLWith {
    val keyField = with.entityKeyField ?: return with
    val selects = mutableListOf(MySQLSelect("${with.alias}.$keyField"))
    if (carryProperties.isNotEmpty()) {
      val availableAliases = getAvailableAliases(with)
      for (propIri in carryProperties) {
        val alias = propIri.substringAfterLast('#')
        selects.add(
          if (with.isCohortRef || alias !in availableAliases) MySQLSelect("NULL", alias)
          else MySQLSelect("${with.alias}.$alias", alias)
        )
      }
    }
    return MySQLWith(
      table = with.table,
      alias = with.alias,
      selects = selects,
      fromAlias = with.alias,
      entityKeyField = keyField
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

    val availableAliases = getAvailableAliases(with)
    val selects = mutableListOf(MySQLSelect("t.$keyField"))
    for (propIri in carryProperties) {
      val alias = propIri.substringAfterLast('#')
      selects.add(
        if (with.isCohortRef || alias !in availableAliases) MySQLSelect("NULL", alias)
        else MySQLSelect("t.$alias", alias)
      )
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

  private fun getAvailableAliases(with: MySQLWith): Set<String> {
    val effectiveSelects = if (with.selects.size == 1 && with.selects.first().name.contains("*")) {
      with.subQuery?.selects ?: emptyList()
    } else with.selects
    return effectiveSelects.mapNotNull { it.alias }.filterNot { it == ROW_NUMBER_ALIAS }.toSet()
  }

  private fun getGroupCarryProperties(match: Query): List<String> {
    if (match.orderBy == null) return emptyList()
    val props = linkedSetOf<String>()
    match.orderBy.property.forEach { props.add(it.iri) }
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
        MySQLSelect("ROW_NUMBER() OVER(PARTITION BY u.$keyField ORDER BY $orderClause)", ROW_NUMBER_ALIAS)
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
      wheres = mutableListOf(MySQLPropertyValueWhere(ROW_NUMBER_ALIAS, "<=", orderBy.limit.toString(), table = "sq")),
      entityKeyField = keyField
    )


    return finalWith
  }

  private fun getMySQLWithFromMatch(match: Query, mySQLQuery: MySQLQuery): MySQLWith {
    var with = MySQLWith()
    val isReferencedElsewhere = match.`as` != null

    if (match.typeOf?.iri != null) {
      with.table = getTableFromTypeAndProperty(match.typeOf.iri, null)
    } else if (match.from != null) {
      val keptWith = keepAsMap[match.from]
      with.table = mySQLQuery.nodeToTableMap[match.from] ?: run {
        val kw = keptWith ?: throw SQLConversionException("Table not found: ${match.from}")
        kw.table.copy(table = kw.alias.trim('`'))
      }
      with.isCarrierAliased = keptWith?.isCarrierAliased ?: false
    } else {
      with.table = queryTypeOfTable
    }

    if (match.path != null) addPathTableAndJoins(match.path, mySQLQuery.nodeToTableMap, with, addJoins = true)
    if (match.node != null) mySQLQuery.nodeToTableMap[match.node] = with.table
    with.alias = getWithAlias(match, mySQLQuery)

    if (match.where != null) {
      whereCompiler.addWheresRecursively(
        where = match.where,
        with = with,
        variableToTableMap = mySQLQuery.nodeToTableMap,
        parentWhere = null,
        bool = null
      )
    }

    addSelects(match, mySQLQuery, with, isReferencedElsewhere)

    if (match.orderBy != null) {
      with = getOrderByWith(with, match, mySQLQuery)
    }
    match.`as`?.let { keepAsMap[it] = with }
    return with
  }

  private fun getJoinBetweenWiths(fromWith: MySQLWith, toWith: MySQLWith): MySQLJoin {
    val (fk, pk) =
      if (fromWith.table.dataModel == toWith.table.dataModel) {
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

    val actualPk = toWith.entityKeyField ?: pk
    return if (toWith.exclude) {
      MySQLJoin(
        join = "LEFT JOIN",
        tableFrom = fromWith.table.alias ?: fromWith.table.table,
        tableTo = toWith.table.table,
        tableToAlias = toWith.alias,
        fromProperty = fk,
        toProperty = actualPk,
        reference = true
      ).apply {
        wheres.add(MySQLPropertyValueWhere("${toWith.alias}.$actualPk", "IS", "NULL"))
      }
    } else {
      MySQLJoin(
        join = "JOIN",
        tableFrom = fromWith.table.alias ?: fromWith.table.table,
        tableTo = toWith.table.table,
        tableToAlias = toWith.alias,
        fromProperty = fk,
        toProperty = actualPk,
        reference = true
      )
    }
  }

  private fun buildChainedWith(match: Query, mySqlQuery: MySQLQuery): MySQLWith {
    val with = getMySQLWithFromMatch(match, mySqlQuery)
    if (match.orderBy != null || mySqlQuery.withs.isEmpty()) return with
    if (isFromNamedStep(match, mySqlQuery)) return with
    val previous = mySqlQuery.withs.last()
    return if (match.notExists()) {
      wrapNotExistsMatch(with, previous)
    } else {
      with.joins.add(getJoinBetweenWiths(with, previous))
      with
    }
  }

  private fun isFromNamedStep(match: Query, mySqlQuery: MySQLQuery): Boolean {
    val from = match.from ?: return false
    return mySqlQuery.nodeToTableMap[from] == null && keepAsMap.containsKey(from)
  }

  private fun wrapNotExistsMatch(with: MySQLWith, previous: MySQLWith): MySQLWith {
    val (fk, _) = resolveForeignKeyByDataModel(with.table, queryTypeOfTable)
    val (fkLast, pkLast) = resolveForeignKeyByDataModel(previous.table, queryTypeOfTable)

    if (fk == null || fkLast == null || pkLast == null) {
      throw SQLConversionException(
        "No relationship between ${with.table.table} and ${previous.table.table}"
      )
    }

    with.joins.add(
      MySQLJoin(
        join = "JOIN",
        tableFrom = with.table.alias ?: with.table.table,
        tableTo = previous.alias,
        tableToAlias = previous.alias,
        fromProperty = fk,
        toProperty = fkLast,
        reference = true
      )
    )

    val select = if (previous.table.dataModel == COHORT_DATA_MODEL_IRI)
      "${previous.alias}.*, ${previous.alias}.$ENTITY_ID_FIELD as $PATIENT_ID_FIELD"
    else "${previous.alias}.*"

    val entityKeyField = if (previous.table.dataModel == COHORT_DATA_MODEL_IRI)
      PATIENT_ID_FIELD
    else previous.entityKeyField

    val wrapped = MySQLWith(
      table = with.table,
      alias = with.alias,
      selects = mutableListOf(MySQLSelect(select)),
      subQuery = with,
      entityKeyField = entityKeyField
    )

    wrapped.joins.add(
      MySQLJoin(
        join = "RIGHT JOIN",
        tableFrom = "sq",
        tableTo = previous.alias,
        tableToAlias = previous.alias,
        fromProperty = fk,
        toProperty = fkLast,
        reference = true
      )
    )
    wrapped.wheres.add(MySQLPropertyValueWhere(fk, "IS", "NULL", table = "sq"))
    return wrapped
  }

  private fun getOrderByWith(with: MySQLWith, match: Query, mySQLQuery: MySQLQuery): MySQLWith {
    val previousWith = mySQLQuery.withs.lastOrNull()

    val (fk, pk) = resolveForeignKeyByTableName(with.table, queryTypeOfTable)

    if (fk == null || pk == null) {
      throw SQLConversionException(
        "No relationship between ${with.table.table} and ${queryTypeOfTable.table}"
      )
    }

    val partitionByField = "${with.table.alias ?: with.table.table}.$fk"
    with.selects.add(
      MySQLSelect(
        "ROW_NUMBER() OVER(PARTITION BY $partitionByField ${
          getMySQLOrderBy(with.table, match.orderBy, mySQLQuery.nodeToTableMap).toSql()
        })", ROW_NUMBER_ALIAS
      )
    )

    val select = if (match.notExists() && previousWith != null) {
      if (previousWith.table.dataModel == COHORT_DATA_MODEL_IRI) "${previousWith.alias}.*, ${previousWith.alias}.$ENTITY_ID_FIELD as $PATIENT_ID_FIELD"
      else "${previousWith.alias}.*"
    } else "sq.*"


    val entityKeyField = if (match.notExists() && previousWith != null) {
      if (previousWith.table.dataModel == COHORT_DATA_MODEL_IRI) PATIENT_ID_FIELD
      else previousWith.entityKeyField
    } else with.entityKeyField

    val rnWith = MySQLWith(
      table = with.table,
      alias = with.alias,
      selects = mutableListOf(MySQLSelect(select)),
      wheres = mutableListOf(),
      whereBool = Bool.and,
      subQuery = with,
      entityKeyField = entityKeyField,
      isCarrierAliased = with.isCarrierAliased
    )

    if (previousWith != null) {
      val (fkLast, pkLast) = resolveForeignKeyByTableName(previousWith.table, queryTypeOfTable)

      if (fkLast == null || pkLast == null) {
        throw SQLConversionException(
          "No relationship between ${with.table.table} and ${previousWith.table.table}"
        )
      }

      val innerQueryJoin = MySQLJoin(
        join = "JOIN",
        tableFrom = with.table.alias ?: with.table.table,
        tableTo = previousWith.alias,
        tableToAlias = previousWith.alias,
        fromProperty = fk,
        toProperty = fkLast,
        reference = true
      )

      with.joins.add(innerQueryJoin)

      if (match.notExists()) {
        val notExistJoinCondition = MySQLJoin(
          join = "RIGHT JOIN",
          tableFrom = "sq",
          tableTo = previousWith.alias,
          tableToAlias = previousWith.alias,
          fromProperty = fk,
          toProperty = fkLast,
          reference = true
        )
        rnWith.joins.add(notExistJoinCondition)
        val or = mutableListOf<MySQLWhere>()
        or.add(MySQLPropertyValueWhere(ROW_NUMBER_ALIAS, "!=", match.orderBy.limit.toString(), table = "sq"))
        or.add(MySQLPropertyValueWhere(fk, "IS", "NULL", table = "sq"))
        rnWith.wheres.add(MySQLBoolWhere(or = or))
      }
    }

    if (!match.notExists() || previousWith == null) {
      rnWith.wheres.add(MySQLPropertyValueWhere(ROW_NUMBER_ALIAS, "<=", match.orderBy.limit.toString(), table = "sq"))
    }
    return rnWith
  }


  private fun addSelects(match: Query, mySQLQuery: MySQLQuery, with: MySQLWith, isReferencedElsewhere: Boolean) {
    if (isReferencedElsewhere) {
      with.selects.add(MySQLSelect("${with.table.alias ?: with.table.table}.*"))
    } else {
      with.selects.add(getDefaultSelect(with.table))
    }
    with.entityKeyField = getEntityKeyFieldName(with.table)
    if (match.`return` != null) {
      val (selects, _) =
        selectCompiler.getSelects(
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
    val (fk, _) = resolveForeignKeyByDataModel(table, queryTypeOfTable)
    return fk ?: throw SQLConversionException(
      "No relationship between ${table.table} and ${queryTypeOfTable.table}"
    )
  }

  private fun getWithAlias(match: Query, mySQLQuery: MySQLQuery): String {
    val name = ensureAs(match) {
      match.name
        ?: match.node
        ?: match.typeOf?.name
        ?: match.from?.let { "${it}_relative" }
        ?: "match"
    }
    return aliases.nextCteAlias(name)
  }

  private fun ensureAs(match: Query, fallback: () -> String): String {
    if (match.`as` == null) match.setAs(fallback())
    return match.`as`
  }

  private fun getMySQLOrderBy(
    table: Table, orderBy: OrderLimit, nodeToTableMap: HashMap<String, Table>,
  ): MySQLOrderBy {
    val items = mutableListOf<MySQLOrderByItem>()
    for (p in orderBy.property) {
      val currentTable =
        if (p.nodeRef != null) nodeToTableMap[p.nodeRef] else table

      if (currentTable == null) throw SQLConversionException("No table exists for ${p.iri}")
      val propertyIri = p.iri.substringAfterLast(' ')
      val field = getPropertyNameByTableAndPropertyIri(
        currentTable,
        propertyIri
      ).field ?: throw SQLConversionException("No field found for property $propertyIri")
      items.add(MySQLOrderByItem(field, if (p.direction == Order.descending) "DESC" else "ASC", table = currentTable))
    }
    return MySQLOrderBy(items, orderBy.limit)
  }

  private fun getTableFromTypeAndProperty(typeIri: String?, propertyIri: String?): Table {
    return IMtoMySQLMap.getTableFromProperty(listOfNotNull(propertyIri))
      ?: IMtoMySQLMap.getTableFromDataModel(typeIri)
      ?: throw SQLConversionException("Type $typeIri not found in table map")
  }

  private fun getDataModelFromKeepAs(keepAs: String?): String? {
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

  private fun findMatchByKeepAs(match: Query?, keepAs: String?): Query? {
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
      val nodeKey = path.node ?: path.name
      try {
        val table = getTableFromTypeAndProperty(path.typeOf.iri, path.iri)
        table.alias = nodeKey

        val join = parentTable.getJoinCondition(
          joinType = if (path.isOptional) "LEFT JOIN" else "JOIN",
          tableTo = table,
          tableToAlias = table.alias,
          tableFromAlias = parentTable.alias,
          viaProperty = path.iri,
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

        if (nodeKey != null) {
          tableMap[nodeKey] = table
          nodePathContextMap[nodeKey] = NodePathContext(
            parentTable = parentTable,
            pathIri = path.iri,
            nodeTable = table
          )
        }

        if (!with.joins.contains(join) && addJoins) {
          with.joins.add(join)
        }

        if (path.path != null) {
          addPathTableAndJoins(path.path, tableMap, with, table, addJoins)
        }

      } catch (exception: SQLConversionException) {
        if (nodeKey != null) {
          tableMap[nodeKey] = parentTable
          nodePathContextMap[nodeKey] = NodePathContext(
            parentTable = parentTable,
            pathIri = path.iri,
            nodeTable = parentTable
          )
        }
        if (path.path != null) {
          addPathTableAndJoins(path.path, tableMap, with, parentTable, addJoins)
        }
      }
    }
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