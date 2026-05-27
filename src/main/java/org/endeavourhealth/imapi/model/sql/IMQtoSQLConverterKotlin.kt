package org.endeavourhealth.imapi.model.sql

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.extern.slf4j.Slf4j
import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.*
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.interfacemanager.model.Bool
import org.endeavourhealth.interfacemanager.model.IM
import org.endeavourhealth.interfacemanager.model.IMQType
import org.endeavourhealth.interfacemanager.model.Order
import java.util.Locale.getDefault

@Slf4j
class IMQtoSQLConverterKotlin @JvmOverloads constructor(
  val queryRequest: QueryRequest, val mapper: ObjectMapper? = ObjectMapper(),
  val denominator: String? = null, val numerator: String? = null, val dataset: String? = null
) {
  private var IMtoMySQLMap: TableMap = MappingParser().parse("IMQtoMYSQL.json")
  var sql: String? = null
  var queryTypeOf: String? = queryRequest.query?.typeOf?.iri
  var mySQLQueries: MutableList<MySQLQuery> = mutableListOf()
  var queryTypeOfTable = Table()
  private val MAX_ALIAS_LENGTH = 64  // DB limit for MySQL
  private var longAliasCounter = 1


  init {
    require(queryRequest.query != null) { "Query request must have a query body" }
  }

  init {
    try {
      if (queryRequest.query.queryType == IMQType.INDICATOR) sql = generateSQLforIndicator()
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
    if (denominator == null || numerator == null || dataset == null) throw SQLConversionException("Missing denominator, numerator or dataset")
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
    val mySqlQuery = MySQLQuery()
    if (definition.typeOf == null || definition.typeOf.iri == null
    ) throw SQLConversionException("Query typeOf is null")

    if (definition.`is` != null) {
      mySqlQuery.withs.addAll(getIsWiths(definition, mySqlQuery))
    }

    if (definition.and != null) {
      addMatchWiths(definition.and, definition, mySqlQuery, Bool.and)
    }

    if (definition.or != null) {
      addMatchWiths(definition.or, definition, mySqlQuery, Bool.or)
    }


    if (definition.columnGroup != null) {
      for (columnGroup in definition.columnGroup) {
        val newMySqlQuery = MySQLQuery()
        mySQLQueries.add(newMySqlQuery)
        if (definition.`is` != null) newMySqlQuery.withs.addAll(getIsWiths(definition, newMySqlQuery))
        addMatchWiths(listOf(columnGroup), definition, newMySqlQuery, Bool.and)
        if (definition.`return` == null) {
          val lastCTE = newMySqlQuery.withs.last { !it.exclude }
          val (fk, pk) = if (lastCTE.table.table == queryTypeOfTable.table)
            queryTypeOfTable.primaryKey to queryTypeOfTable.primaryKey
          else lastCTE.table.foreignKeyTo(queryTypeOfTable)
          newMySqlQuery.insert = "dataset.dataset_results"
          newMySqlQuery.selects.add(MySQLSelect(definition.iri, "query_result_id"))
          newMySqlQuery.selects.add(MySQLSelect("${lastCTE.alias}.$fk", "entity_id"))
          newMySqlQuery.selects.add(MySQLSelect("'${columnGroup.name.replace(" ", "")}'", "column_group"))
          newMySqlQuery.selects.add(MySQLSelect(getJSONObject(newMySqlQuery), "json"))
        }
      }
      return mySQLQueries.joinToString(separator = "\n----------------------------------------\n") { it.toSql() }
    } else {
      if (definition.`return` == null) {
        val lastCTE = mySqlQuery.withs.last { !it.exclude }
        val (fk, pk) = if (lastCTE.table.table == queryTypeOfTable.table)
          queryTypeOfTable.primaryKey to queryTypeOfTable.primaryKey
        else lastCTE.table.foreignKeyTo(queryTypeOfTable)
        mySqlQuery.insert = "dataset.cohort_results"
        mySqlQuery.selects.add(MySQLSelect(definition.iri, "query_result_id"))
        mySqlQuery.selects.add(MySQLSelect("${lastCTE.alias}.$fk", "entity_id"))
      }
      return mySqlQuery.toSql()
    }
  }

  private fun getJSONObject(newMySqlQuery: MySQLQuery): String {
    val lastWith = newMySqlQuery.withs.last()

    val selects = if (
      lastWith.selects.size == 1 &&
      lastWith.selects.first().name.contains("*")
    ) {
      if (lastWith.subQuery != null) {
        lastWith.subQuery?.selects?.filterNot { it.alias == null || it.alias == ("rn") }
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

    val jsonObject = buildString {
      append("JSON_OBJECT(\n")
      append(
        selects
          .filterNot { it.alias == "id" }
          .joinToString(",\n") { select ->
            val key = (select.alias ?: select.name).replace("`", "\"")
            val value = select.alias ?: select.name
            "  $key, $value"
          }
      )
      append("\n)")
    }
    return jsonObject
  }

  private fun getIsWiths(match: Match, mySqlQuery: MySQLQuery): MutableList<MySQLWith> {
    val isAWiths = mutableListOf<MySQLWith>()
    for (isA in match.`is`) {
      val isAlias = "`${getCteAliasFromTypeAndProperty(isA.iri, null)}`"
      val withJoins = mutableListOf<MySQLJoin>()
      if (mySqlQuery.withs.isNotEmpty()) {
        withJoins.add(
          MySQLJoin(
            "JOIN",
            tableFrom = "dataset.cohort_results",
            tableTo = mySqlQuery.withs.last { !it.exclude }.alias,
            fromProperty = "entity_id",
            toProperty = mySqlQuery.withs.last().selects.first().name.split(".").last(),
            wheres = if (isA.isExclude) mutableListOf(
              MySQLPropertyValueWhere("query_result_id", "=", "${isA.iri}", null, null),
              MySQLPropertyValueWhere("entity_id", "IS", "NULL", null, null)
            ) else mutableListOf(
              MySQLPropertyValueWhere("query_result_id", "=", "${isA.iri}", null, null),
            )
          )
        )
      }
      val cohortTable = getTableFromTypeAndProperty("http://endhealth.info/im#Cohort", null)
      cohortTable.table = "dataset.cohort_results"
      val topWheres = if (withJoins.isEmpty()) mutableListOf<MySQLWhere>(
        MySQLPropertyValueWhere("query_result_id", "=", "${isA.iri}", null, null),
      ) else mutableListOf()
      val isAWith = MySQLWith(
        table = cohortTable,
        alias = isAlias,
        selects = mutableListOf(MySQLSelect("${cohortTable.table}.entity_id")),
        joins = withJoins.ifEmpty { mutableListOf() },
        exclude = isA.isExclude,
        wheres = topWheres
      )
      isAWiths.add(isAWith)
    }
    return isAWiths
  }

  private fun getCteAliasFromTypeAndProperty(typeIri: String?, propertyIri: String?): String {
    val typeIriSuffix = typeIri?.substringAfter('#')
    if (propertyIri == null)
      return "${typeIriSuffix}_cte"
    val propertyIriSuffix = propertyIri.substringAfter('#')
    return "${typeIriSuffix}_${propertyIriSuffix}_cte"
  }

  private fun addMatchWiths(
    match: List<Match>,
    definition: Query,
    mySqlQuery: MySQLQuery,
    bool: Bool,
  ) {
    for (m in match) {
      addMatchWithsRecursively(m, definition, mySqlQuery, bool)
    }
  }

  private fun addMatchWithsRecursively(
    currentMatch: Match,
    parentMatch: Match,
    mySqlQuery: MySQLQuery,
    bool: Bool,
  ) {
//    if (currentMatch.having != null && (currentMatch.and != null || currentMatch.or != null)) {
//      addScoredMatchWiths(currentMatch, mySqlQuery)
//      return
//    }

    if (currentMatch.and != null) {
      for (m in currentMatch.and) {
        addMatchWithsRecursively(m, currentMatch, mySqlQuery, Bool.and)
      }
    }
    if (currentMatch.or != null) {
      for (m in currentMatch.or) {
        addMatchWithsRecursively(m, currentMatch, mySqlQuery, Bool.or)
      }
    }


    if (currentMatch.and == null && currentMatch.or == null) {
      if (currentMatch.`is` != null) mySqlQuery.withs.addAll(getIsWiths(currentMatch, mySqlQuery))
      else mySqlQuery.withs.add(getMySQLWithFromMatch(currentMatch, mySqlQuery))
    }
  }

  private fun addScoredMatchWiths(currentMatch: Match, mySqlQuery: MySQLQuery) {
//    val having = currentMatch.having
//      ?: throw SQLConversionException("Having clause is required for scored match")
//
//    val scoredWiths = mutableListOf<MySQLWith>()
//    val priorWiths = mySqlQuery.withs.toList()
//
//    val childMatches = currentMatch.and ?: currentMatch.or
//      ?: throw SQLConversionException("Scored match must have 'and' or 'or' children")
//
//    // Build each child match as an independent CTE with a score select
//    for (childMatch in childMatches) {
//      val scoreValue = childMatch.score ?: "1"
//      // Reset withs to prior state so each child CTE doesn't join to sibling CTEs
//      mySqlQuery.withs.clear()
//      mySqlQuery.withs.addAll(priorWiths)
//      if (childMatch.`is` != null) {
//        val isWiths = getIsWiths(childMatch, mySqlQuery)
//        for (isWith in isWiths) {
//          isWith.selects.add(MySQLSelect(scoreValue, "score"))
//          scoredWiths.add(isWith)
//        }
//      } else {
//        val childWith = getMySQLWithFromMatch(childMatch, mySqlQuery)
//        childWith.selects.add(MySQLSelect(scoreValue, "score"))
//        scoredWiths.add(childWith)
//      }
//    }
//
//    // Restore prior withs and add all scored CTEs
//    mySqlQuery.withs.clear()
//    mySqlQuery.withs.addAll(priorWiths)
//    for (w in scoredWiths) {
//      mySqlQuery.withs.add(w)
//    }
//
//    // Build the "combined" CTE using UNION ALL of all scored CTEs
//    val primaryKeyCol = queryTypeOfTable.primaryKey
//    val unionWiths = scoredWiths.map { scoredWith ->
//      MySQLWith(
//        table = Table().apply { table = scoredWith.alias },
//        fromAlias = scoredWith.alias,
//        selects = mutableListOf(
//          MySQLSelect(primaryKeyCol),
//          MySQLSelect("score")
//        )
//      )
//    }.toMutableList()
//
//    val combinedWith = MySQLWith(
//      alias = "combined",
//      unionWiths = unionWiths,
//      unionAll = true
//    )
//    mySqlQuery.withs.add(combinedWith)
//
//    // Build the "aggregated" CTE with GROUP BY and aggregate HAVING
//    val aggregateFn = having.aggregate?.name ?: "SUM"
//    val operatorValue = having.operator?.value ?: ">="
//    val thresholdValue = having.value ?: "0"
//
//    val aggregatedWith = MySQLWith(
//      table = queryTypeOfTable.copy(),
//      fromAlias = "combined",
//      alias = "aggregated",
//      selects = mutableListOf(
//        MySQLSelect(primaryKeyCol),
//        MySQLSelect("$aggregateFn(score)", "total_score")
//      ),
//      groupByColumns = mutableListOf(primaryKeyCol),
//      havingClause = "$aggregateFn(score) $operatorValue $thresholdValue"
//    )
//    mySqlQuery.withs.add(aggregatedWith)
  }

  private fun getMySQLWithFromMatch(match: Match, mySQLQuery: MySQLQuery): MySQLWith {
    var with = MySQLWith()

    if (match.typeOf?.iri != null) {
      with.table = getTableFromTypeAndProperty(match.typeOf.iri, null)
    } else {
      with.table = if (match.nodeRef != null)
        mySQLQuery.nodeToTableMap[match.nodeRef]
          ?: throw SQLConversionException("Table not found: ${match.nodeRef}") else queryTypeOfTable
    }

    if (match.path != null)
      addPathTableAndJoins(match.path, mySQLQuery.nodeToTableMap, with, addJoins = true)
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


    if (match.orderBy == null && mySQLQuery.withs.isNotEmpty()) {
      with.joins.add(getJoinBetweenWiths(with, mySQLQuery.withs.last()))
    }


    return with;
  }

  private fun getJoinBetweenWiths(fromWith: MySQLWith, toWith: MySQLWith): MySQLJoin {
    val (fk, pk) =
      if (fromWith.table.table == toWith.table.table)
        fromWith.table.primaryKey to toWith.table.primaryKey
      else
        fromWith.table.foreignKeyTo(toWith.table)

    if (fk == null || pk == null) {
      throw SQLConversionException(
        "No relationship between ${fromWith.table.table} and ${toWith.table}"
      )
    }

    val join =
      if (toWith.exclude) {
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

    return join
  }

  private fun getOrderByWith(with: MySQLWith, match: Match, mySQLQuery: MySQLQuery): MySQLWith {
    val (fk, pk) = if (with.table.table == queryTypeOfTable.table) with.table.primaryKey to with.table.primaryKey else with.table.foreignKeyTo(
      queryTypeOfTable
    )
    if (fk == null || pk == null) {
      throw SQLConversionException(
        "No relationship between ${with.table.table} and ${mySQLQuery.withs.last().table.table}"
      )
    }
    val partitionByField = "${with.table.alias ?: with.table.table}.$fk"
    with.selects.add(
      MySQLSelect(
        "ROW_NUMBER() OVER(PARTITION BY $partitionByField ${
          getMySQLOrderBy(
            with.table,
            match.orderBy,
            mySQLQuery.nodeToTableMap
          ).toSql()
        })", "rn"
      )
    )

    val rnWith = MySQLWith(
      table = with.table,
      alias = with.alias,
      selects = mutableListOf(MySQLSelect(if (match.notExists()) "${mySQLQuery.withs.last().alias}.*" else "sq.*")),
      wheres = mutableListOf(),
      whereBool = Bool.and,
      subQuery = with
    )

    val (fkLast, pkLast) = if (mySQLQuery.withs.last().table.table == queryTypeOfTable.table) mySQLQuery.withs.last().table.primaryKey to mySQLQuery.withs.last().table.primaryKey else mySQLQuery.withs.last().table.foreignKeyTo(
      queryTypeOfTable
    )
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
      rnWith.wheres?.add(MySQLBoolWhere(or = or))
    } else {
      rnWith.wheres?.add(MySQLPropertyValueWhere("rn", "<=", match.orderBy.limit.toString(), table = "sq"))
    }

    if (match.then != null) {
      val properties = getPropsUsedInThen(match.then)
      for (property in properties) {
        val field = getPropertyNameByTableAndPropertyIri(with.table, property).field
          ?: throw SQLConversionException("No field found for property $property")
        with.selects.add(MySQLSelect("${with.table.alias ?: with.table.table}.$field"))
      }
      val table = rnWith.table.copy(table = "sq")
      addWheresRecursively(match.then, rnWith, mySQLQuery.nodeToTableMap, null, null, table)
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

  private fun addSelects(match: Match, mySQLQuery: MySQLQuery, with: MySQLWith) {
    with.selects.add(getDefaultSelect(with.table))
    if (match.`return` != null) {
      val (selects, selectJoins) =
        getSelects(
          with.table,
          match.`return`,
          mySQLQuery,
          with.alias,
          mySQLQuery.nodeToTableMap,
        )
      with.selects.addAll(selects)
    }
  }

  private fun getDefaultSelect(table: Table): MySQLSelect {
    if (table.dataModel == queryTypeOfTable.dataModel)
      return MySQLSelect("${table.table}.${queryTypeOfTable.primaryKey}")
    val (fk, pk) = table.foreignKeyTo(queryTypeOfTable)
    return MySQLSelect("${table.alias ?: table.table}.$fk")
  }

  private fun getWithAlias(match: Match, mySQLQuery: MySQLQuery): String {
    val alias = if (match.name != null) sanitiseAlias(match.name)
    else if (match.node != null) sanitiseAlias(match.node)
    else if (mySQLQuery.nodeToTableMap.keys.isNotEmpty()) "cte_${mySQLQuery.nodeToTableMap.keys.size}"
    else "cte_${mySQLQuery.withs.size}"

    val existing = mySQLQuery.withs
      .map { it.alias }
      .toHashSet()

    if (alias.length > MAX_ALIAS_LENGTH) {
      var alias: String
      do {
        alias = "cte_${longAliasCounter++}"
      } while (existing.contains(alias))
      return alias
    }

    return alias
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
      if (ret.iri != null)
        addSelectFromProperty(ret, selects, nodeToTableMap, table)
      else if (ret.function != null) {
        if (ret.function.iri == IM.COUNT.toString()) selects.add(
          MySQLSelect(
            "COUNT(*)",
            if (ret.`as` != null) "`${ret.`as`}`" else null
          )
        )
        else if (ret.function != null) selects.add(getFunctionSelect(table, ret, nodeToTableMap))
      } else throw SQLConversionException("Unsupported return $returx")
    }
    return Pair(selects, joins)
  }

  private fun getFunctionSelect(
    table: Table,
    returnProperty: Return,
    nodeToTableMap: HashMap<String, Table>
  ): MySQLSelect {
    if (returnProperty.function.iri != IM.CONCATENATE.toString()) throw SQLConversionException("Unsupported function ${returnProperty.function.iri}")
    val concatenateFields = mutableListOf<String>()
    for (arg in returnProperty.function.argument) {
      if (arg.valuePath == null) throw SQLConversionException("Missing valuePath for concatenate function argument")
      var field = ""
      if (arg.valuePath.nodeRef != null) {
        val currentTable = nodeToTableMap[arg.valuePath.nodeRef]
          ?: throw SQLConversionException("Missing nodeRef from valuePath for concatenate function argument: ${arg.valuePath.nodeRef}")
        field = getPropertyNameByTableAndPropertyIri(
          currentTable,
          arg.valuePath.iri
        ).field
        field = "${currentTable.alias}.$field"
      } else field = getPropertyNameByTableAndPropertyIri(table, arg.valuePath.iri).field
      if (field.isEmpty()) throw SQLConversionException("No field found for concatenate function argument ${arg.valuePath}")
      concatenateFields.add(field)
    }
    return MySQLSelect(
      "CONCAT(${concatenateFields.joinToString(", ")})",
      if (returnProperty.`as` != null) "`${returnProperty.`as`}`" else null
    )
  }

  private fun addSelectFromProperty(
    returnProperty: Return,
    selects: MutableList<MySQLSelect>,
    nodeToTableMap: HashMap<String, Table>,
    currentWithTable: Table
  ) {
    val currentTable =
      if (returnProperty.nodeRef != null) nodeToTableMap[returnProperty.nodeRef] else currentWithTable
    if (currentTable == null) throw SQLConversionException("No table exists for ${returnProperty.iri}")
    val property = getPropertyNameByTableAndPropertyIri(
      currentTable,
      returnProperty.iri
    )
    val field = "${currentTable.alias ?: currentTable.table}.${property.field}"
    if (returnProperty.nodeRef != null) {
      currentWithTable.fields[returnProperty.iri] = property
    }
    selects.add(MySQLSelect(field, if (returnProperty.`as` != null) "`${returnProperty.`as`}`" else null))
  }

  private fun findToTable(
    paths: MutableList<Return>,
    startIndex: Int,
    tableMap: TableMap,
    currentIri: String = paths[startIndex].iri
  ): Pair<Table?, Int> {
    val toTable = tableMap.getTableFromProperty(listOf(currentIri))
    if (toTable != null) {
      return toTable to startIndex
    }

    val nextIndex = startIndex + 1
    if (nextIndex >= paths.size) {
      return null to startIndex
    }

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

    where.and?.forEach { child ->
      walkMySQLWheres(child, visit)
    }

    where.or?.forEach { child ->
      walkMySQLWheres(child, visit)
    }
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

        else -> with.wheres?.add(boolWhere)
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

        else -> with.wheres?.add(boolWhere)
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

      else -> with.wheres?.add(leaf)
    }
  }


  private fun getMySQLWhereFromWhere(
    where: Where,
    variableToTableMap: HashMap<String, Table>,
    with: MySQLWith,
    table: Table? = null,
  ): MySQLWhere {
    var (currentTable, field) = if (table != null && where.iri != null) table to getPropertyNameByTableAndPropertyIri(
      table,
      where.iri
    ).field
    else
      getTableAndField(
        with,
        where,
        variableToTableMap
      )
    if (table != null) currentTable = table
    if (where.propertyRef != null) field = where.propertyRef
    val where = if (where.`is` != null) {
      for (join in addWhereConceptJoin(currentTable, field)) {
        if (!with.joins.contains(join))
          with.joins.add(join)
      }
      MySQLPropertyIsWhere(
        field,
        where.`is`,
        "=",
        not = where.isNot,
        table = currentTable.alias ?: currentTable.table,
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
          value = "'${from.value}'",
          table = table?.alias ?: table?.table ?: currentTable.alias ?: currentTable.table
        )
        toWhere = MySQLPropertyValueWhere(
          property = field,
          operator = to.operator.value,
          value = "'${to.value}'",
          table = table?.alias ?: table?.table ?: currentTable.alias ?: currentTable.table
        )
      } else {
        val fromRight = from.compare?.right?.parameter ?: from.compare?.right?.propertyRef
        ?: getValueFromRelativeTo(from, variableToTableMap)
        ?: throw SQLConversionException("No value for range.from")

        val toRight = to.compare?.right?.parameter ?: to.compare?.right?.propertyRef
        ?: getValueFromRelativeTo(to, variableToTableMap)
        ?: throw SQLConversionException("No value for range.to")

        val (fromUnit, fromUnitType) =
          if (where.compare?.units?.iri != null) getUnitNameAndType(where.compare.units.iri)
          else if (where.range?.from?.compare?.units?.iri != null) getUnitNameAndType(where.range.from.compare.units.iri)
          else if (where.qualifier?.iri != null) getUnitNameAndType(where.qualifier.iri)
          else null to null

        val (toUnit, toType) =
          if (where.compare?.units?.iri != null) getUnitNameAndType(where.compare.units.iri)
          else if (where.range?.to?.compare?.units?.iri != null) getUnitNameAndType(where.range.to.compare.units.iri)
          else if (where.qualifier?.iri != null) getUnitNameAndType(where.qualifier.iri)
          else null to null

        fromWhere = MySQLCompareWhere(
          property = field,
          operator = from.operator.value,
          right = fromRight,
          value = from.value,
          table = table?.alias ?: table?.table ?: currentTable.alias ?: currentTable.table,
          units = if (fromUnitType == "Unit") fromUnit else null,
          qualifier = if (fromUnitType == "Qualifier") fromUnit else null,
        )

        toWhere = MySQLCompareWhere(
          property = field,
          operator = to.operator.value,
          right = toRight,
          value = to.value,
          table = table?.alias ?: table?.table ?: currentTable.alias ?: currentTable.table,
          units = if (toType == "Unit") toUnit else null,
          qualifier = if (toType == "Qualifier") toUnit else null,
        )
      }

      MySQLBoolWhere(
        and = mutableListOf(fromWhere, toWhere)
      )
    } else if (where.isNull) {
      MySQLPropertyIsNullWhere(
        field,
        not = where.isNot,
        table = currentTable.alias ?: currentTable.table,
      )
    } else if (where.compare != null) {
      val (name, type) =
        if (where.compare.units?.iri != null) getUnitNameAndType(where.compare.units.iri)
        else if (where.qualifier?.iri != null) getUnitNameAndType(where.qualifier.iri)
        else null to null
      val compareValue = where.compare.right.parameter ?: where.compare.right.propertyRef ?: getValueFromRelativeTo(
        where,
        variableToTableMap
      )
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
        where.value,
        not = where.isNot,
        table = currentTable.alias ?: currentTable.table,
      )
    }
    return where
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

  private fun addWhereConceptJoin(table: Table, fromField: String?): MutableList<MySQLJoin> {
    val joins: MutableList<MySQLJoin> = mutableListOf()
    val conceptTable = getTableFromTypeAndProperty(IM.CONCEPT.toString(), null)
    joins.add(
      table.getJoinCondition(
        tableFromAlias = table.alias,
        tableTo = conceptTable,
        tableToAlias = "concept_property",
        fromField = fromField,
        toField = "dbid"
      )
    )

    val conceptTCT = getTableFromTypeAndProperty(IM.CONCEPT.toString() + "TCT", null)
    joins.add(
      conceptTable.getJoinCondition(
        tableFrom = conceptTable,
        tableFromAlias = "concept_property",
        tableTo = conceptTCT,
      )
    )
    return joins
  }

  private fun getUnitNameAndType(iri: String): Pair<String, String> {
    return when (IM.decode(iri)) {
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
    val table =
      IMtoMySQLMap.getTableFromProperty(listOf(propertyIri))
        ?: IMtoMySQLMap.getTableFromDataModel(
          typeIri
        ) ?: throw SQLConversionException(
          "Type $typeIri not found in table map"
        )
    return table
  }

  private fun ensureUniqueAlias(baseAlias: String, mySqlQuery: MySQLQuery): String {
    fun normalize(a: String) =
      a.replace("`", "").lowercase()

    val existing = mySqlQuery.withs
      .map { normalize(it.alias) }
      .toHashSet()

    if (baseAlias.length > MAX_ALIAS_LENGTH) {
      var alias: String
      do {
        alias = "cte_${longAliasCounter++}"
      } while (existing.contains(normalize(alias)))
      return "`$alias`"
    }

    var alias = baseAlias
    var index = 2

    while (
      existing.contains(normalize(alias)) ||
      alias.length > MAX_ALIAS_LENGTH
    ) {
      alias = "${baseAlias}_$index"
      index++
    }

    alias = alias.replace("`", "")
    return "`$alias`"
  }

  fun getDataModelFromKeepAs(keepAs: String?): String? {
    var match: Match? = findMatchByKeepAs(queryRequest.query, keepAs)
    if (match == null && queryRequest.query.columnGroup != null) {
      for (child in queryRequest.query.columnGroup) {
        val result: Match? = findMatchByKeepAs(child, keepAs)
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

  fun findMatchByKeepAs(match: Match?, keepAs: String?): Match? {
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

        if (!with.joins.contains(join) && addJoins) {
          with.joins.add(join)
        }

        if (path.path != null) {
          addPathTableAndJoins(path.path, tableMap, with, table, addJoins)
        }

      } catch (exception: SQLConversionException) {
        tableMap[path.node] = parentTable
        if (path.path != null) {
          addPathTableAndJoins(path.path, tableMap, with, parentTable, addJoins)
        }
      }
    }
  }
}
