package org.endeavourhealth.imapi.model.sql

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.extern.slf4j.Slf4j
import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.*
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.imapi.vocabulary.IM

@Slf4j
class IMQtoSQLConverterKotlin @JvmOverloads constructor(
  val queryRequest: QueryRequest, val mapper: ObjectMapper? = ObjectMapper()
) {
  private var IMtoMySQLMap: TableMap = MappingParser().parse("IMQtoMYSQL.json")
  var sql: String? = null
  var queryTypeOf: String? = queryRequest.query?.typeOf?.iri
  var mySQLQueries: MutableList<MySQLQuery> = mutableListOf()
  var queryTypeOfTable = getTableFromTypeAndProperty(queryTypeOf, null)


  init {
    require(queryRequest.query != null) { "Query request must have a query body" }
    require(queryTypeOf != null) { "Queries need a type" }
  }

  init {
    try {
      sql = generateSQL(queryRequest.query)
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
    val mySqlQuery = MySQLQuery()
    if (definition.typeOf == null || definition.typeOf.iri == null
    ) throw SQLConversionException("Query typeOf is null")

    if (definition.`is` != null) {
      (addIsWiths(definition, mySqlQuery))
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
        if (definition.`is` != null) addIsWiths(definition, newMySqlQuery)
        addMatchWiths(listOf(columnGroup), definition, newMySqlQuery, Bool.and)
        if (definition.`return` == null) {
          newMySqlQuery.selects.add(MySQLSelect(definition.iri ?: $$"$hash", "hash"))
          newMySqlQuery.selects.add(MySQLSelect(queryTypeOfTable.primaryKey, "cohort_id"))
          newMySqlQuery.selects.add(MySQLSelect("'${columnGroup.name.replace(" ", "")}'", "group"))
          newMySqlQuery.insert = MySQLInsert("dataset")
          val jsonObject = getJSONObject(newMySqlQuery)
          newMySqlQuery.selects.add(MySQLSelect(jsonObject, "results"))
          newMySqlQuery.update =
            """ON DUPLICATE KEY UPDATE hash = VALUES(hash), cohort_id = VALUES(cohort_id), `group` = VALUES(`group`), results = VALUES(results);"""
        }
      }
      return mySQLQueries.joinToString(separator = "\n----------------------------------------\n") { it.toSql() }
    } else {
      if (definition.`return` == null) {
        mySqlQuery.selects.add(MySQLSelect(definition.iri ?: $$"$hash", "hash"))
        mySqlQuery.selects.add(
          MySQLSelect(
            "${mySqlQuery.withs.last { !it.exclude }.alias}.${
              mySqlQuery.withs.last { !it.exclude }.selects.first().name.split(
                "."
              ).last()
            }", "cohort_id"
          )
        )
        mySqlQuery.insert = MySQLInsert("cohort")
        mySqlQuery.update = """ON DUPLICATE KEY UPDATE hash = VALUES(hash), cohort_id = VALUES(cohort_id);"""
      }
      return mySqlQuery.toSql()
    }
  }


  private fun getJSONObject(newMySqlQuery: MySQLQuery): String {
    val lastWith = newMySqlQuery.withs.last()

    val selects = if (
      lastWith.selects.size == 1 &&
      lastWith.selects.first().name == "*"
    ) {
      // Fallback to previous WITH and exclude rn
      newMySqlQuery.withs
        .dropLast(1)
        .last()
        .selects
        .filterNot { it.alias == "rn" }
    } else {
      lastWith.selects
    }

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

  private fun addIsWiths(match: Match, mySQLQuery: MySQLQuery, not: Boolean? = null) {
    val mySQLQueryJoins = mutableListOf<MySQLJoin>()
    for (isA in match.`is`) {
      val isAlias =
        if (match.node != null) "`${match.node}_cte`" else "`${getCteAliasFromTypeAndProperty(isA.iri, null)}`"
      val with = getIsWith(isA, isAlias, mySQLQuery)
      if (isA.isExclude || not == true) {
        val (with, join) = getIsExcludeWith(isA, isAlias, mySQLQuery)
        mySQLQuery.withs.add(with)
        mySQLQueryJoins.add(join)
      } else mySQLQuery.withs.add(with)
    }
    for (join in mySQLQueryJoins) {
      join.tableFrom = mySQLQuery.withs.last().alias
      mySQLQuery.joins.add(join)
    }
  }

  private fun getIsWith(isA: Node, isAlias: String, mySqlQuery: MySQLQuery): MySQLWith {
    val withJoins = mutableListOf<MySQLJoin>()
    if (mySqlQuery.withs.isNotEmpty()) {
      withJoins.add(
        MySQLJoin(
          "JOIN",
          tableFrom = "cohort",
          tableTo = mySqlQuery.withs.last { !it.exclude }.alias,
          fromProperty = "cohort_id",
          toProperty = mySqlQuery.withs.last().selects.first().name.split(".").last(),
          wheres = mutableListOf(MySQLPropertyValueWhere("cohort.hash", "=", isA.iri, null, null))
        )
      )
    }
    return MySQLWith(
      getTableFromTypeAndProperty(IM.COHORT.toString(), IM.ID.toString()),
      isAlias,
      if (withJoins.isEmpty()) mutableListOf(MySQLPropertyValueWhere("hash", "=", isA.iri, null, null)) else null,
      mutableListOf(MySQLSelect("cohort.cohort_id"), MySQLSelect("cohort.hash")),
      withJoins.ifEmpty { null },
      Bool.and,
    )
  }

  private fun getIsExcludeWith(isA: Node, isAlias: String, mySqlQuery: MySQLQuery): Pair<MySQLWith, MySQLJoin> {

    val with = MySQLWith(
      getTableFromTypeAndProperty(IM.COHORT.toString(), IM.ID.toString()),
      isAlias,
      mutableListOf(MySQLPropertyValueWhere("hash", "=", isA.iri, null, null)),
      mutableListOf(MySQLSelect("id"), MySQLSelect("hash")),
      null,
      Bool.and,
      exclude = true
    )

    val join = MySQLJoin(
      "LEFT JOIN",
      tableFrom = mySqlQuery.withs.last().alias,
      tableTo = with.alias,
      fromProperty = "cohort_id",
      toProperty = "cohort_id",
      wheres = mutableListOf(
        MySQLPropertyValueWhere("${with.alias}.cohort_id", "IS", "NULL", null, null)
      )
    )
    return Pair(with, join)
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
      addMatchWithsRecursively(m, definition, mySqlQuery, bool, false, definition.step)
    }
  }

  private fun addMatchWithsRecursively(
    currentMatch: Match,
    parentMatch: Match,
    mySqlQuery: MySQLQuery,
    bool: Bool,
    isStep: Boolean = false,
    steps: MutableList<Match>? = null
  ) {
    if (currentMatch.and != null) {
      for (m in currentMatch.and) {
        addMatchWithsRecursively(m, currentMatch, mySqlQuery, Bool.and, isStep, currentMatch.step)
      }
    }
    if (currentMatch.or != null) {
      for (m in currentMatch.or) {
        addMatchWithsRecursively(m, currentMatch, mySqlQuery, Bool.or, isStep, currentMatch.step)
      }
    }

    if (currentMatch.step != null) {
      for (m in currentMatch.step) {
        addMatchWithsRecursively(m, currentMatch, mySqlQuery, Bool.and, isStep, currentMatch.step)
      }
    }

    if (currentMatch.union != null) {
      addUnionWiths(currentMatch, mySqlQuery)
      return
    }

    if (currentMatch.and == null && currentMatch.or == null) {
      if (currentMatch.`is` != null) addIsWiths(currentMatch, mySqlQuery, currentMatch.notExists())
      else mySqlQuery.withs.addAll(getMySQLWithFromMatch(currentMatch, mySqlQuery, isStep, currentMatch.step))
    }
  }

  private fun addUnionWiths(
    currentMatch: Match,
    mySqlQuery: MySQLQuery
  ) {
    val unionMatches = currentMatch.union ?: return
    if (unionMatches.isEmpty()) return

    val baseWith = mySqlQuery.withs.lastOrNull()
    val unionWiths = mutableListOf<MySQLWith>()

    for (unionMatch in unionMatches) {
      val branchQuery = MySQLQuery(
        withs = if (baseWith != null) mutableListOf(baseWith) else mutableListOf(),
        selects = mutableListOf(),
        joins = mutableListOf()
      )
      branchQuery.nodeToTableMap.putAll(mySqlQuery.nodeToTableMap)

      addMatchWithsRecursively(unionMatch, currentMatch, branchQuery, Bool.and, false)

      val newWiths = if (baseWith != null) branchQuery.withs.drop(1) else branchQuery.withs
      if (newWiths.isEmpty()) continue

      mySqlQuery.withs.addAll(newWiths)
      unionWiths.add(newWiths.last())
      mySqlQuery.nodeToTableMap.putAll(branchQuery.nodeToTableMap)
    }

    if (unionWiths.isEmpty()) return

    val baseAlias = currentMatch.name?.replace(" ", "")?.replace(".", "")
      ?: currentMatch.node
      ?: getCteAliasFromTypeAndProperty(
        queryTypeOf,
        currentMatch.where?.iri
          ?: currentMatch.where?.and?.firstOrNull()?.iri
          ?: currentMatch.where?.or?.firstOrNull()?.iri
      )
    val alias = if (currentMatch.node == null) ensureUniqueAlias(baseAlias, mySqlQuery) else baseAlias
    val orderByTable = unionWiths.first().table
    val unionWith = MySQLWith(
      table = orderByTable,
      alias = alias,
      selects = mutableListOf(),
      joins = mutableListOf(),
      wheres = mutableListOf(),
      whereBool = Bool.and,
      unionWiths = unionWiths
    )
    if (currentMatch.orderBy != null) {
      unionWith.orderBy = getMySQLOrderBy(orderByTable, currentMatch.orderBy, mySqlQuery.nodeToTableMap)
    }
    mySqlQuery.withs.add(unionWith)
    if (currentMatch.node != null) {
      mySqlQuery.nodeToTableMap[currentMatch.node] = orderByTable
    }
  }

  private fun getMySQLWithFromMatch(
    match: Match,
    mySQLQuery: MySQLQuery,
    isStep: Boolean = false,
    steps: MutableList<Match>?
  ): MutableList<MySQLWith> {
    val joins = mutableListOf<MySQLJoin>()
    var currentTable = queryTypeOfTable
    var referenceTable = queryTypeOfTable
    if (match.path != null)
      referenceTable = addPathTablesAndJoins(match.path, queryTypeOfTable, mySQLQuery.nodeToTableMap, joins)
    val isStepWrapper = match.step != null && match.path == null && match.nodeRef == null
    val fromWith = if (isStepWrapper) mySQLQuery.withs.lastOrNull() else null
    if (isStepWrapper) {
      val stepTable = getStepResultTable(match, mySQLQuery.nodeToTableMap) ?: fromWith?.table
      if (stepTable != null) {
        currentTable = stepTable
        referenceTable = stepTable
      }
    }
    if (match.nodeRef != null) currentTable =
      mySQLQuery.nodeToTableMap[match.nodeRef] ?: throw SQLConversionException("Table not found: ${match.nodeRef}")
    if (match.node != null) {
      if (match.nodeRef != null) {
        val nodeRefTable = mySQLQuery.nodeToTableMap[match.nodeRef]
        mySQLQuery.nodeToTableMap[match.node] =
          nodeRefTable ?: throw SQLConversionException("Table not found: ${match.nodeRef}")
      } else mySQLQuery.nodeToTableMap[match.node] = referenceTable ?: currentTable
    }
    val baseAlias = match.name?.replace(" ", "")?.replace(".", "")
      ?: if (match.node != null) "`${match.node}_cte`" else
        if (mySQLQuery.nodeToTableMap.keys.isNotEmpty()) "`${mySQLQuery.nodeToTableMap.keys.joinToString("_")}_cte`" else
          getCteAliasFromTypeAndProperty(
            queryTypeOf,
            match.where?.iri
              ?: match.where?.and?.firstOrNull()?.iri
              ?: match.where?.or?.firstOrNull()?.iri
          )
    val isAlias = if (match.node == null) ensureUniqueAlias(baseAlias, mySQLQuery) else baseAlias
    val with = MySQLWith(
      table = currentTable,
      alias = if (match.orderBy != null) "`${(isAlias + "_partition").replace("`", "")}`" else isAlias,
      selects = mutableListOf(),
      joins = joins,
      wheres = mutableListOf(),
      whereBool = Bool.and,
      fromAlias = fromWith?.alias
    )

    if (match.where != null) {
      addWheresRecursively(
        where = match.where,
        with = with,
        variableToTableMap = mySQLQuery.nodeToTableMap,
        parentWhere = null,
        bool = null
      )
    }

    if (mySQLQuery.withs.isNotEmpty() && fromWith == null) {
      if (match.`return` == null)
        joins.add(
          (with.table.getJoinCondition(
            tableTo = mySQLQuery.withs.last().table,
            tableToAlias = mySQLQuery.withs.last().alias,
            reference = true
          ))
        )
      else joins.add(
        (queryTypeOfTable.getJoinCondition(
          tableTo = mySQLQuery.withs.last().table,
          tableToAlias = mySQLQuery.withs.last().alias,
          reference = true
        ))
      )
    }

    val defaultSelect = MySQLSelect("${queryTypeOfTable.table}.${queryTypeOfTable.primaryKey}", "id")
    val (selects, selectJoins, ynWith) =
      if (match.`return` != null) getSelects(
        with.table,
        match.`return`,
        mySQLQuery,
        if (match.orderBy != null) "`${(isAlias + "_partition").replace("`", "")}`" else isAlias,
        mySQLQuery.nodeToTableMap,
      )
      else Triple(mutableListOf(defaultSelect), mutableListOf(), null)

    if (match.orderBy != null) {
      selects.add(
        MySQLSelect(
          "ROW_NUMBER() OVER(PARTITION BY ${queryTypeOfTable.table}.${queryTypeOfTable.primaryKey} ${
            getMySQLOrderBy(
              currentTable,
              match.orderBy,
              mySQLQuery.nodeToTableMap
            ).toSql()
          })", "rn"
        )
      )
    }

    if (fromWith != null) {
      val fromAlias = fromWith.alias
      for (i in selects.indices) {
        val select = selects[i]
        val name = select.name
          .replace("${queryTypeOfTable.table}.", "$fromAlias.")
          .replace("${with.table.alias ?: with.table.table}.", "$fromAlias.")
        selects[i] = MySQLSelect(name, select.alias)
      }
    }
    if (selects.isNotEmpty()) with.selects.addAll(selects)

    if (selectJoins.isNotEmpty()) with.joins?.addAll(selectJoins)
    var rnWith: MySQLWith? = null
    if (match.orderBy != null) {
      rnWith = MySQLWith(
        table = currentTable,
        alias = isAlias,
        selects = mutableListOf(MySQLSelect(if (match.notExists()) "${mySQLQuery.withs.last().alias}.*" else "*")),
        wheres = mutableListOf(),
        whereBool = Bool.and,
        fromAlias = "`${(isAlias + "_partition").replace("`", "")}`"
      )

      if (match.notExists()) {
        val notExistJoinCondition = currentTable.getJoinCondition(
          joinType = "RIGHT JOIN",
          tableFrom = with.table,
          tableFromAlias = with.alias,
          tableTo = mySQLQuery.withs.last().table,
          tableToAlias = mySQLQuery.withs.last().alias,
          reference = true
        )
        rnWith.joins?.add(notExistJoinCondition)
        val or = mutableListOf<MySQLWhere>()
        or.add(MySQLPropertyValueWhere("${with.alias}.rn", "!=", match.orderBy.limit.toString()))
        or.add(MySQLPropertyValueWhere("${with.alias}.id", "IS", "NULL"))
        rnWith.wheres?.add(MySQLBoolWhere(or = or))
      } else {
        rnWith.wheres?.add(MySQLPropertyValueWhere("${with.alias}.rn", "=", match.orderBy.limit.toString()))
      }
    }

    return if (ynWith != null) {
      ynWith.selects.addAll(0, with.selects.mapNotNull { it.alias?.let { alias -> MySQLSelect(alias) } })
      mutableListOf(with, ynWith)
    } else if (rnWith != null) mutableListOf(with, rnWith) else mutableListOf(with)
  }

  private fun getSelects(
    table: Table,
    returx: MutableList<Return>,
    mySqlQuery: MySQLQuery,
    currentWithAlias: String,
    nodeToTableMap: HashMap<String, Table>,
  ): Triple<MutableList<MySQLSelect>, MutableList<MySQLJoin>, MySQLWith?> {
    val defaultSelect =
      MySQLSelect("${queryTypeOfTable.table}.${queryTypeOfTable.primaryKey}", queryTypeOfTable.primaryKey)
    val selects = mutableListOf(defaultSelect)
    val joins = mutableListOf<MySQLJoin>()
    var ynWith: MySQLWith? = null
    for (ret in returx) {
      if (ret.iri != null)
        if (ret.case != null) ynWith = getYNCaseSelect(ret, mySqlQuery, currentWithAlias, table)
        else addSelectFromProperty(ret, selects, nodeToTableMap, table)
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
    return Triple(selects, joins, ynWith)
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

  private fun getYNCaseSelect(
    returnProperty: Return,
    mySqlQuery: MySQLQuery,
    currentWithAlias: String,
    currentWithTable: Table
  ): MySQLWith {
    //    TODO: handle multiple when cases?
    //    TODO: handle when where cases
    //    TODO: handle nested cases?
    if (returnProperty.case.`when`.size != 1) throw SQLConversionException("Unsupported case size ${returnProperty.case.`when`.size}")
    if (!returnProperty.case.`when`.first().isExists) throw SQLConversionException("Unsupported case ${returnProperty.case}")

    val ynSelect = MySQLSelect(
      "IFNULL($currentWithAlias.${currentWithTable.primaryKey}, '${returnProperty.case.`else`}', '${returnProperty.case.`when`.first().then}')",
      if (returnProperty.`as` != null) "`${returnProperty.`as`}`" else null
    )
    val tableYNAlias = "${returnProperty.`as` ?: currentWithAlias}_YN"
    val join = currentWithTable.getJoinCondition(
      joinType = "LEFT JOIN",
      tableTo = mySqlQuery.withs.last().table,
      tableToAlias = mySqlQuery.withs.last().alias,
      tableFromAlias = currentWithAlias,
    )
    val ynWith = MySQLWith(
      table = currentWithTable,
      fromAlias = currentWithAlias,
      alias = tableYNAlias,
      selects = mutableListOf(ynSelect),
      joins = mutableListOf(join),
      wheres = mutableListOf(),
      whereBool = Bool.and
    )
    return ynWith
  }

  private fun addSelectFromProperty(
    returnProperty: Return,
    selects: MutableList<MySQLSelect>,
    nodeToTableMap: HashMap<String, Table>,
    currentWithTable: Table
  ) {
    var field =
      if (returnProperty.iri == "http://endhealth.info/im#age") IMtoMySQLMap.functions[returnProperty.iri]?.replace(
        "{units}",
        "YEAR"
      )?.replace("{relativeTo}", $$"$searchDate") else IMtoMySQLMap.functions[returnProperty.iri]
    if (field == null) {
      val currentTable =
        if (returnProperty.nodeRef != null) nodeToTableMap[returnProperty.nodeRef] else currentWithTable
      if (currentTable == null) throw SQLConversionException("No table exists for ${returnProperty.iri}")
      val property = getPropertyNameByTableAndPropertyIri(
        currentTable,
        returnProperty.iri
      )
      field = "${currentTable.alias ?: currentTable.table}.${property.field}"
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


//  private fun walkPropertyPath(property: Return, propertyPath: MutableList<Return>) {
//    propertyPath.add(property)
//    if (null != property.getReturn() && null != property.getReturn().property && !property.getReturn()
//        .property.isEmpty()
//    ) walkPropertyPath(property.getReturn().property.first(), propertyPath)
//  }

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
    bool: Bool? = null
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
        addWheresRecursively(it, with, variableToTableMap, boolWhere, Bool.and)
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
        addWheresRecursively(it, with, variableToTableMap, boolWhere, Bool.or)
      }
      return
    }

    val leaf = getMySQLWhereFromWhere(where, variableToTableMap, with)
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
    with: MySQLWith
  ): MySQLWhere {
    val currentTable =
      if (where.nodeRef != null) variableToTableMap[where.nodeRef] else with.table
    if (currentTable == null) throw SQLConversionException("No table found: ${where.nodeRef}")
    val rawField = IMtoMySQLMap.functions[where.function?.iri] ?: getPropertyNameByTableAndPropertyIri(
      currentTable,
      where.iri
    ).field ?: throw SQLConversionException("No field found for property ${where.iri}")
    val field =
      if (with.fromAlias != null && where.nodeRef == null && !rawField.contains(".") && !rawField.contains("(")) {
        "${with.fromAlias}.$rawField"
      } else {
        rawField
      }
    val args = getFunctionArgumentMap(currentTable, where)
    val where = if (where.`is` != null) {
      for (join in addWhereConceptJoin(currentTable, field)) {
        if (with.joins?.contains(join) == false)
          with.joins?.add(join)
      }
      MySQLPropertyIsWhere(
        field,
        where.`is`,
        "=",
        not = where.isNot,
        args = args,
        table = currentTable
      )
    } else if (where.range != null) {
      MySQLPropertyRangeWhere(
        field,
        where.range.from.operator.value,
        where.range.from.value,
        where.range.to.value,
        where.range.to.operator.value,
        not = where.isNot,
        args = args,
        table = currentTable
      )
    } else if (where.isNull) {
      MySQLPropertyIsNullWhere(
        field,
        args = args,
        not = where.isNot,
        table = currentTable
      )
    } else {
      MySQLPropertyValueWhere(
        field,
        where.operator.value,
        where.value ?: where.relativeTo.parameter ?: getValueFromRelativeTo(where, variableToTableMap)
        ?: throw SQLConversionException("No value provided for where $where"),
        not = where.isNot,
        args = args,
        table = currentTable
      )
    }
    return where
  }

  private fun getValueFromRelativeTo(where: Where, nodeToTableMap: HashMap<String, Table>): String? {
    val nodeRef =
      where.relativeTo?.nodeRef ?: throw SQLConversionException("No property found for relativeTo ${where.relativeTo}")
    var property = ""
    val nodeRefTable = nodeToTableMap[nodeRef]
    if (nodeRefTable != null) {
      property = getPropertyNameByTableAndPropertyIri(nodeRefTable, where.relativeTo.iri).field
    } else {
      getDataModelFromKeepAs(nodeRef)?.let {
        property =
          getPropertyNameByTableAndPropertyIri(getTableFromTypeAndProperty(it, null), where.relativeTo.iri).field
      }
    }
    if (property.isEmpty()) throw SQLConversionException("No property found for relativeTo ${where.relativeTo.nodeRef}")
    if (nodeToTableMap[nodeRef] != null) return "${nodeRef}_cte.$property"
    return "${nodeRef}.${property}"
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

  private fun getFunctionArgumentMap(table: Table, where: Where): HashMap<String, String> {
    val argMap = HashMap<String, String>()
    if (where.function?.argument != null) {
      for (argument in where.function.argument) {
        val valueToReplace = argument.parameter
        var valueToReplaceWith =
          if (table.alias != null && !getArgValue(
              table,
              argument
            ).contains("$") && argument.parameter != "units"
          ) "${table.alias}.${getArgValue(table, argument)}" else getArgValue(table, argument)
        if (argument.parameter == "units") valueToReplaceWith = getUnitName(valueToReplaceWith)
        argMap[valueToReplace] = valueToReplaceWith
      }
      if (!argMap.containsKey("relativeTo")) {
        argMap["relativeTo"] = $$"$searchDate"
      }
    }
    return argMap
  }

  private fun getArgValue(table: Table, argument: Argument): String {
    return if (argument.valuePath != null) {
      getPropertyNameByTableAndPropertyIri(table, argument.valuePath.iri).field
    } else if (argument.valueParameter != null) {
      argument.valueParameter
    } else if (argument.valueIri != null) {
      argument.valueIri.iri
    } else {
      throw SQLConversionException("No value provided for argument $argument")
    }
  }

  private fun getUnitName(iri: String): String {
    return when (IM.from(iri)) {
      IM.YEARS -> "YEAR"
      IM.MONTHS, IM.MONTH -> "MONTH"
      IM.DAYS -> "DAY"
      IM.HOURS -> "HOUR"
      IM.MINUTES -> "MINUTE"
      IM.SECONDS -> "SECOND"
      IM.FISCAL_YEAR -> "FISCAL_YEAR"
      else -> throw SQLConversionException("No unit name found for $iri")
    }
  }

  private fun getPropertyNameByTableAndPropertyIri(table: Table, propertyIri: String): Field {
    val field = table.fields[propertyIri] ?: throw SQLConversionException(
      "Property $propertyIri not found in table ${table.table}"
    )
    return field
  }

  private fun getStepResultTable(
    match: Match,
    nodeToTableMap: HashMap<String, Table>
  ): Table? {
    val steps = match.step ?: return null
    for (i in steps.indices.reversed()) {
      val step = steps[i]
      if (step.nodeRef != null) {
        nodeToTableMap[step.nodeRef]?.let { return it }
      }
      if (step.path != null && step.path.isNotEmpty()) {
        val lastPath = step.path.last()
        val node = lastPath.node
        if (node != null) {
          nodeToTableMap[node]?.let { return it }
        }
        return try {
          getTableFromTypeAndProperty(lastPath.typeOf.iri, null)
        } catch (e: SQLConversionException) {
          null
        }
      }
      if (step.step != null) {
        val nested = getStepResultTable(step, nodeToTableMap)
        if (nested != null) return nested
      }
    }
    return null
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
    var alias = baseAlias
    var index = 2
    val existing = mySqlQuery.withs.map { it.alias }.toHashSet()
    while (existing.contains(alias)) {
      alias = "${baseAlias}_$index"
      alias = alias.replace("`", "")
      alias = "`$alias`"
      index++
    }
    return alias
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

    if (match.step != null) {
      for (child in match.step) {
        val result = findMatchByKeepAs(child, keepAs)
        if (result != null) return result
      }
    }
    if (match.union != null) {
      for (child in match.union) {
        val result = findMatchByKeepAs(child, keepAs)
        if (result != null) return result
      }
    }

    return null
  }

  private fun addPathTablesAndJoins(
    paths: MutableList<Path>,
    parentTable: Table,
    tableMap: HashMap<String, Table>,
    joins: MutableList<MySQLJoin>,
  ): Table {
    var firstTable: Table? = null

    for (path in paths) {
      try {
        val table = getTableFromTypeAndProperty(path.typeOf.iri, path.iri)
        table.alias = path.node

        if (firstTable == null) {
          firstTable = table
        }

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
        if (!joins.contains(join)) {
          joins.add(join)
        }
        if (path.path != null) {
          addPathTablesAndJoins(path.path, table, tableMap, joins)
        }


      } catch (exception: SQLConversionException) {
        tableMap[path.node] = parentTable
        if (path.path != null) {
          addPathTablesAndJoins(path.path, parentTable, tableMap, joins)
        }
      }
    }

    return firstTable ?: parentTable
  }
}
