package org.endeavourhealth.imapi.transforms

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.sql.MySQLJoin
import org.endeavourhealth.imapi.model.sql.MySQLNotExistsWhere
import org.endeavourhealth.imapi.model.sql.MySQLPropertyValueWhere
import org.endeavourhealth.imapi.model.sql.MySQLQuery
import org.endeavourhealth.imapi.model.sql.MySQLSelect
import org.endeavourhealth.imapi.model.sql.MySQLWhere
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.endeavourhealth.imapi.model.sql.Table

/**
 * Compiles the IMQ match tree (and/or/is/notExists groups and individual matches) into an ordered list of CTEs.
 */
internal class MatchTreeCompiler(
  private val queryTypeOfTableProvider: () -> Table,
  private val aliases: AliasAllocator,
  private val keepAsMap: MutableMap<String, MySQLWith>,
  private val selectCompiler: SelectClauseCompiler,
  private val whereCompiler: WhereClauseCompiler,
  private val pathJoinBuilder: PathJoinBuilder,
  private val getTableFromTypeAndProperty: (String?, String?) -> Table,
) {
  private val queryTypeOfTable: Table get() = queryTypeOfTableProvider()
  private val carryPropertiesStack = ArrayDeque<List<String>>()

  fun getIsWith(match: Query, mySqlQuery: MySQLQuery): MySQLWith {
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

  fun addMatchWithsRecursively(
    currentMatch: Query,
    mySqlQuery: MySQLQuery,
  ) {
    if (currentMatch.notExists() && (currentMatch.and != null || currentMatch.or != null || currentMatch.`is` != null)) {
      addNotExistsGroup(currentMatch, mySqlQuery)
      return
    }
    addGroupBody(currentMatch, mySqlQuery)
  }

  private fun addGroupBody(currentMatch: Query, mySqlQuery: MySQLQuery) {
    if (currentMatch.and != null) addAnds(currentMatch, mySqlQuery)
    if (currentMatch.or != null) addOrs(currentMatch, mySqlQuery)
    if (currentMatch.`is` != null) mySqlQuery.withs.add(getIsWith(currentMatch, mySqlQuery))
  }

  private fun addNotExistsGroup(group: Query, mySqlQuery: MySQLQuery) {
    val previous = mySqlQuery.withs.lastOrNull()
      ?: throw SQLConversionException("notExists on a group needs a preceding match to exclude from")
    val groupAs = group.`as`
    val withCount = mySqlQuery.withs.size
    addGroupBody(group, mySqlQuery)
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

    if (match.path != null) pathJoinBuilder.addPathTableAndJoins(match.path, mySQLQuery.nodeToTableMap, with, addJoins = true)
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
      with = getOrderByWith(with, match, mySQLQuery, queryTypeOfTable)
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

  fun buildChainedWith(match: Query, mySqlQuery: MySQLQuery): MySQLWith {
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

  private fun addSelects(match: Query, mySQLQuery: MySQLQuery, with: MySQLWith, isReferencedElsewhere: Boolean) {
    if (isReferencedElsewhere) {
      with.selects.add(MySQLSelect("${with.table.alias ?: with.table.table}.*"))
    } else {
      with.selects.add(getDefaultSelect(with.table))
    }
    with.entityKeyField = getEntityKeyFieldName(with.table)
    if (match.`return` != null) {
      val selects =
        selectCompiler.getSelects(
          with.table,
          match.`return`,
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
}
