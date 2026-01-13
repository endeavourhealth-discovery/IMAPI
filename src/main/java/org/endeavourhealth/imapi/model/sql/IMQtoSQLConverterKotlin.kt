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

    if (definition.not != null) {
      addMatchWiths(definition.not, definition, mySqlQuery, Bool.not)
    }

    if (definition.columnGroup != null) {
      for (columnGroup in definition.columnGroup) {
        val newMySqlQuery = MySQLQuery()
        mySQLQueries.add(newMySqlQuery)
        if (definition.`is` != null) addIsWiths(definition, newMySqlQuery)
        addMatchWiths(listOf(columnGroup), definition, newMySqlQuery, Bool.and)
        if (definition.`return` == null) {
          newMySqlQuery.selects.add(MySQLSelect($$"$hashcode", "hashcode"))
          newMySqlQuery.selects.add(MySQLSelect("${queryTypeOfTable.table}_id", "id"))
          newMySqlQuery.insert = MySQLInsert("dataset")
          val jsonObject = buildString {
            append("JSON_OBJECT(\n")
            append(
              newMySqlQuery.withs.last().selects
                .filterNot { it.name.contains("DISTINCT") }
                .joinToString(",\n") { select ->
                  val key = (select.alias ?: select.name).replace("`", "'")
                  val value = select.alias ?: select.name
                  "  $key, $value"
                }
            )
            append("\n)")
          }
          newMySqlQuery.selects.add(MySQLSelect(jsonObject, "results"))
        }
      }
      return mySQLQueries.joinToString(separator = "\n\n") { it.toSql() }
    } else {
      if (definition.`return` == null) {
        mySqlQuery.selects.add(MySQLSelect($$"$hashcode", "hashcode"))
        mySqlQuery.selects.add(MySQLSelect("${queryTypeOfTable.table}_id", "id"))
        mySqlQuery.insert = MySQLInsert("cohort")
      }
      return mySqlQuery.toSql()
    }
  }

  private fun addIsWiths(match: Match, mySQLQuery: MySQLQuery, not: Boolean? = null) {
    val mySQLQueryJoins = mutableListOf<MySQLJoin>()
    for (isA in match.`is`) {
      val isAlias = match.node ?: getCteAliasFromTypeAndProperty(isA.iri, null)
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
          fromProperty = "id",
          toProperty = "id",
          wheres = mutableListOf(MySQLPropertyValueWhere("hashCode", "=", isA.iri, null, null))
        )
      )
    }
    return MySQLWith(
      getTableFromTypeAndProperty(IM.COHORT.toString(), IM.ID.toString()),
      isAlias,
      if (withJoins.isEmpty()) mutableListOf(MySQLPropertyValueWhere("hashCode", "=", isA.iri, null, null)) else null,
      mutableListOf(MySQLSelect("id"), MySQLSelect("hashCode")),
      withJoins.ifEmpty { null },
      Bool.and,
    )
  }

  private fun getIsExcludeWith(isA: Node, isAlias: String, mySqlQuery: MySQLQuery): Pair<MySQLWith, MySQLJoin> {
    val with = MySQLWith(
      getTableFromTypeAndProperty(IM.COHORT.toString(), IM.ID.toString()),
      isAlias,
      mutableListOf(MySQLPropertyValueWhere("hashCode", "=", isA.iri, null, null)),
      mutableListOf(MySQLSelect("id"), MySQLSelect("hashCode")),
      null,
      Bool.and,
      exclude = true
    )

    val join = MySQLJoin(
      "LEFT JOIN",
      tableFrom = mySqlQuery.withs.last().alias,
      tableTo = with.alias,
      fromProperty = "id",
      toProperty = "id",
      wheres = mutableListOf(
        MySQLPropertyValueWhere("${with.alias}.id", "IS", "NULL", null, null)
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
      addMatchWithsRecursively(m, definition, mySqlQuery, bool)
    }
  }

  private fun addMatchWithsRecursively(
    currentMatch: Match,
    parentMatch: Match,
    mySqlQuery: MySQLQuery,
    bool: Bool,
  ) {
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
    if (currentMatch.not != null) {
      for (m in currentMatch.not) {
        addMatchWithsRecursively(m, currentMatch, mySqlQuery, Bool.not)
      }
    }

    if (currentMatch.and == null && currentMatch.or == null && currentMatch.not == null) {
      if (currentMatch.`is` != null) addIsWiths(currentMatch, mySqlQuery, if (bool == Bool.not) true else null)
      else mySqlQuery.withs.addAll(getMySQLWithFromMatch(currentMatch, mySqlQuery))
    }
  }

  private fun getMySQLWithFromMatch(
    match: Match,
    mySQLQuery: MySQLQuery,
  ): MutableList<MySQLWith> {
    val joins = mutableListOf<MySQLJoin>()
    var currentTable = queryTypeOfTable
    if (match.path != null)
      currentTable = addPathTablesAndJoins(match.path, queryTypeOfTable, mySQLQuery.nodeToTableMap, joins)
    if (match.nodeRef != null) currentTable =
      mySQLQuery.nodeToTableMap[match.nodeRef] ?: throw SQLConversionException("Table not found: ${match.nodeRef}")
    if (match.node != null) {
      mySQLQuery.nodeToTableMap[match.node] = currentTable
    }
    val isAlias = match.name?.replace(" ", "")
      ?: match.node
      ?: if (mySQLQuery.nodeToTableMap.keys.isNotEmpty()) mySQLQuery.nodeToTableMap.keys.joinToString("_") else
        getCteAliasFromTypeAndProperty(
          queryTypeOf,
          match.where?.iri
            ?: match.where?.and?.firstOrNull()?.iri
            ?: match.where?.or?.firstOrNull()?.iri
        )
    val with = MySQLWith(
      table = currentTable,
      alias = isAlias,
      selects = mutableListOf(),
      joins = joins,
      wheres = mutableListOf(),
      whereBool = Bool.and
    )

    if (match.where != null)
      addWheresRecursively(
        where = match.where,
        with = with,
        variableToTableMap = mySQLQuery.nodeToTableMap,
        parentWhere = null,
        bool = null
      )

    if (mySQLQuery.withs.isNotEmpty()) {
      joins.add(
        (with.table.getJoinCondition(
          tableTo = mySQLQuery.withs.last().table,
          tableToAlias = mySQLQuery.withs.last().alias,
          reference = true
        ))
      )
    }

    if (match.orderBy != null) {
      with.orderBy = getMySQLOrderBy(with.table, match.orderBy, mySQLQuery.nodeToTableMap)
    }

    val defaultSelect =
      MySQLSelect("DISTINCT ${with.table.table}.${with.table.primaryKey}", "${with.table.table}_id")
    val (selects, selectJoins, ynWith) =
      if (match.`return` != null) getSelects(
        with.table,
        match.`return`,
        mySQLQuery,
        isAlias,
        with.table,
        mySQLQuery.nodeToTableMap
      )
      else Triple(mutableListOf(defaultSelect), mutableListOf(), null)
    if (selects.isNotEmpty()) with.selects.addAll(selects)
    if (selectJoins.isNotEmpty()) with.joins?.addAll(selectJoins)
    return if (ynWith != null) {
      ynWith.selects.addAll(0, with.selects.mapNotNull { it.alias?.let { alias -> MySQLSelect(alias) } })
      mutableListOf(with, ynWith)
    } else mutableListOf(with)
  }

  private fun getSelects(
    table: Table,
    returx: Return,
    mySqlQuery: MySQLQuery,
    currentWithAlias: String,
    currentWithTable: Table,
    nodeToTableMap: HashMap<String, Table>,
  ): Triple<MutableList<MySQLSelect>, MutableList<MySQLJoin>, MySQLWith?> {
    val defaultSelect =
      MySQLSelect("DISTINCT ${queryTypeOfTable.table}.${queryTypeOfTable.primaryKey}", "${queryTypeOfTable.table}_id")
    val selects = mutableListOf(defaultSelect)
    val joins = mutableListOf<MySQLJoin>()
    var ynWith: MySQLWith? = null
    if (returx.property != null)
      for (p in returx.property) {
        if (p.case != null) ynWith = getYNCaseSelect(p, mySqlQuery, currentWithAlias, currentWithTable)
        else if (p.function != null) selects.add(getFunctionSelect(table, p))
        else addSelectFromProperty(p, selects, nodeToTableMap, currentWithTable)
      }
    else if (returx.function != null) {
      if (returx.function.iri == IM.COUNT.toString()) selects.add(
        MySQLSelect(
          "COUNT(*)",
          if (returx.`as` != null) "`${returx.`as`}`" else null
        )
      )
    } else throw SQLConversionException("Unsupported return $returx")
    return Triple(selects, joins, ynWith)
  }

  private fun getFunctionSelect(table: Table, returnProperty: ReturnProperty): MySQLSelect {
    if (returnProperty.function.iri != IM.CONCATENATE.toString()) throw SQLConversionException("Unsupported function ${returnProperty.function.iri}")
    val concatenateFields = mutableListOf<String>()
    for (arg in returnProperty.function.argument) {
      if (arg.parameter != "text") throw SQLConversionException("Unsupported function argument ${arg.parameter}")
      if (arg.valuePath == null) throw SQLConversionException("Missing valuePath for concatenate function argument")
      var field = ""
      if (arg.valuePath.path != null) {
        try {
          val typeOfTable = getTableFromTypeAndProperty(arg.valuePath.typeOf.iri, null)
          field = "${typeOfTable.table}.${
            getPropertyNameByTableAndPropertyIri(
              typeOfTable,
              arg.valuePath.path.first().iri
            ).field
          }"
        } catch (e: SQLConversionException) {
          field =
            getPropertyNameByTableAndPropertyIri(
              table,
              arg.valuePath.path.first().iri
            ).field
        }
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
    returnProperty: ReturnProperty,
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
    returnProperty: ReturnProperty,
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
      field = "${currentTable.table}.${property.field}"
    }
    selects.add(MySQLSelect(field, if (returnProperty.`as` != null) "`${returnProperty.`as`}`" else null))
  }

  private fun findToTable(
    paths: MutableList<ReturnProperty>,
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


  private fun walkPropertyPath(property: ReturnProperty, propertyPath: MutableList<ReturnProperty>) {
    propertyPath.add(property)
    if (null != property.getReturn() && null != property.getReturn().property && !property.getReturn()
        .property.isEmpty()
    ) walkPropertyPath(property.getReturn().property.first(), propertyPath)
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
      items.add(MySQLOrderByItem(field, if (p.direction == Order.descending) "DESC" else "ASC"))
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
    val field = IMtoMySQLMap.functions[where.function?.iri] ?: getPropertyNameByTableAndPropertyIri(
      currentTable,
      where.iri
    ).field ?: throw SQLConversionException("No field found for property ${where.iri}")
    val args = getFunctionArgumentMap(currentTable, where)
    val where = if (where.`is` != null) {
      with.joins?.addAll(addWhereConceptJoin(currentTable))
      MySQLPropertyIsWhere(
        field,
        where.`is`,
        "=",
        not = where.isNot,
        args = args
      )
    } else if (where.range != null) {
      MySQLPropertyRangeWhere(
        field,
        where.range.from.operator.value,
        where.range.from.value,
        where.range.to.value,
        where.range.to.operator.value,
        not = where.isNot,
        args = args
      )
    } else if (where.getIsNull()) {
      MySQLPropertyIsNullWhere(
        field,
        args = args,
        not = where.isNot,
      )
    } else {
      MySQLPropertyValueWhere(
        field,
        where.operator.value,
        where.value ?: where.relativeTo.parameter ?: getValueFromRelativeTo(where)
        ?: throw SQLConversionException("No value provided for where $where"),
        not = where.isNot,
        args = args
      )
    }
    return where
  }

  private fun getValueFromRelativeTo(where: Where): String? {
    val nodeRef =
      where.relativeTo?.nodeRef ?: throw SQLConversionException("No property found for relativeTo ${where.relativeTo}")
    var property = ""
    val with = mySQLQueries.last().withs.find { it.alias == nodeRef }
    if (with != null) {
      property = getPropertyNameByTableAndPropertyIri(with.table, where.relativeTo.iri).field
    } else {
      getDataModelFromKeepAs(nodeRef)?.let {
        property =
          getPropertyNameByTableAndPropertyIri(getTableFromTypeAndProperty(it, null), where.relativeTo.iri).field
      }
    }
    if (property.isEmpty()) throw SQLConversionException("No property found for relativeTo ${where.relativeTo.nodeRef}")
    return "${nodeRef}.${property}"
  }

  private fun addWhereConceptJoin(table: Table): MutableList<MySQLJoin> {
    val joins: MutableList<MySQLJoin> = mutableListOf()
    val conceptTable = getTableFromTypeAndProperty(IM.CONCEPT.toString(), null)
    joins.add(table.getJoinCondition(tableTo = conceptTable))

    val conceptMemberTable = getTableFromTypeAndProperty(IM.CONCEPT.toString() + "Member", null)
    joins.add(
      conceptTable.getJoinCondition(
        tableFrom = conceptTable,
        tableTo = conceptMemberTable,
      )
    )
    return joins
  }

  private fun getFunctionArgumentMap(table: Table, where: Where): HashMap<String, String> {
    val argMap = HashMap<String, String>()
    if (where.function?.argument != null) {
      for (argument in where.function.argument) {
        val valueToReplace = argument.parameter
        var valueToReplaceWith = getArgValue(table, argument)
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

  private fun getTableFromTypeAndProperty(typeIri: String?, propertyIri: String?): Table {
    val table = IMtoMySQLMap.getTableFromDataModel(typeIri) ?: throw SQLConversionException(
      "Type $typeIri not found in table map"
    )
    return table
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

    if (match.not != null) {
      for (child in match.not) {
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
    var lastTable = parentTable

    for (path in paths) {
      val table = getTableFromTypeAndProperty(path.typeOf.iri, null)
      val join = parentTable.getJoinCondition(
        joinType = if (path.isOptional) "LEFT JOIN" else "JOIN",
        tableTo = table,
        tableFromAlias = parentTable.table,
      )

      tableMap[path.node] = table
      joins.add(join)

      lastTable =
        if (path.path != null) {
          addPathTablesAndJoins(path.path, table, tableMap, joins)
        } else {
          table
        }
    }
    return lastTable
  }

}