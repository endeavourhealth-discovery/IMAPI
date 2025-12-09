package org.endeavourhealth.imapi.model.sql

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.extern.slf4j.Slf4j
import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.*
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.imapi.vocabulary.IM
import kotlin.collections.ifEmpty

@Slf4j
class IMQtoSQLConverterKotlin @JvmOverloads constructor(
  val queryRequest: QueryRequest, val mapper: ObjectMapper? = ObjectMapper()
) {
  val queryTypeOf = queryRequest.query?.typeOf?.iri

  init {
    require(queryRequest.query != null) { "Query request must have a query body" }
  }

  private var tableMap: TableMap? = null
  var sql: String? = null

  init {
    try {
      val resourcePath = "IMQtoMYSQL.json"
      tableMap = MappingParser().parse(resourcePath)
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
    if (definition.typeOf == null || definition.typeOf.iri == null
    ) throw SQLConversionException("Query typeOf +is null")
    val mySQLQuery = MySQLQuery()

    if (definition.`is` != null) {
      (addIsWiths(definition, mySQLQuery))
    }

    if (definition.and != null) {
      addMatchWiths(definition.and, definition, Bool.and, mySQLQuery)
    }

    if (definition.or != null) {
      addMatchWiths(definition.or, definition, Bool.or, mySQLQuery)
    }

    if (definition.not != null) {
      addMatchWiths(definition.not, definition, Bool.not, mySQLQuery)
    }

    if (definition.`return` == null) {
      mySQLQuery.selects.add(MySQLSelect("id"))
    }

    return mySQLQuery.toSql()
  }

  private fun addIsWiths(match: Match, mySQLQuery: MySQLQuery) {
    val mySQLQueryJoins = mutableListOf<MySQLJoin>()
    for (isA in match.`is`) {
      val isAlias = getCteAlias(isA.iri, null)
      val with = getIsWith(isA, isAlias, mySQLQuery)
      if (isA.isExclude) {
        val (with, join) = getIsExcludeWith(isA, isAlias, mySQLQuery)
        mySQLQuery.withs.add(with)
        mySQLQueryJoins.add(join)
      } else mySQLQuery.withs.add(with)
      // TODO: replace isA.iri in new MySQLWhere() with the hashcode of query definition+arguments - create a map of iri to hashCode initially
    }
    for (join in mySQLQueryJoins) {
      join.tableFrom = mySQLQuery.withs.last().alias
      mySQLQuery.joins.add(join)
    }
  }

  private fun getIsWith(isA: Node, isAlias: String, mySQLQuery: MySQLQuery): MySQLWith {
    val withJoins = mutableListOf<MySQLJoin>()
    if (mySQLQuery.withs.isNotEmpty()) {
      withJoins.add(
        MySQLJoin(
          "JOIN",
          tableFrom = "cohort",
          tableTo = mySQLQuery.withs.last { !it.exclude }.alias,
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

  private fun getIsExcludeWith(isA: Node, isAlias: String, mySQLQuery: MySQLQuery): Pair<MySQLWith, MySQLJoin> {
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
      tableFrom = mySQLQuery.withs.last().alias,
      tableTo = with.alias,
      fromProperty = "id",
      toProperty = "id",
      wheres = mutableListOf(
        MySQLPropertyValueWhere("hashCode", "=", isA.iri, null, null),
        MySQLPropertyValueWhere("${with.alias}.id", "IS", "NULL", null, null)
      )
    )
    return Pair(with, join)
  }

  private fun getCteAlias(typeIri: String?, propertyIri: String?): String {
    val typeIriSuffix = typeIri?.substringAfter('#')
    if (propertyIri == null)
      return "${typeIriSuffix}_cte"
    val propertyIriSuffix = propertyIri.substringAfter('#')
    return "${typeIriSuffix}_${propertyIriSuffix}_cte"
  }

  private fun addMatchWiths(match: List<Match>, definition: Query, bool: Bool, mySQLQuery: MySQLQuery) {
    for (m in match) {
      addMatchWithsRecursively(m, definition, Bool.and, mySQLQuery)
    }
  }

  private fun addMatchWithsRecursively(
    currentMatch: Match,
    parentMatch: Match,
    bool: Bool,
    mySQLQuery: MySQLQuery
  ) {
    if (currentMatch.and != null) {
      for (m in currentMatch.and) {
        if (m.typeOf == null) m.typeOf = parentMatch.typeOf
        addMatchWithsRecursively(m, currentMatch, Bool.and, mySQLQuery)
      }
    }
    if (currentMatch.or != null) {
      for (m in currentMatch.or) {
        if (m.typeOf == null) m.typeOf = parentMatch.typeOf
        addMatchWithsRecursively(m, currentMatch, Bool.or, mySQLQuery)
      }
    }
    if (currentMatch.not != null) {
      for (m in currentMatch.not) {
        if (m.typeOf == null) m.typeOf = parentMatch.typeOf
        addMatchWithsRecursively(m, currentMatch, Bool.not, mySQLQuery)
      }
    }

    if (currentMatch.and == null && currentMatch.or == null && currentMatch.not == null) {
      if (currentMatch.typeOf == null) currentMatch.typeOf = parentMatch.typeOf
      mySQLQuery.withs.add(getMySQLWithFromMatch(currentMatch, mySQLQuery))
    }
  }

  private fun getMySQLWithFromMatch(match: Match, mySQLQuery: MySQLQuery): MySQLWith {
    val typeOf = match.path?.firstOrNull()?.typeOf?.iri ?: match.typeOf.iri
    val table = getTableFromTypeAndProperty(typeOf, match.where.iri)
    val queryTypeOfTable = getTableFromTypeAndProperty(queryTypeOf, null)

    val isSelects = mutableListOf(
      MySQLSelect(
        "DISTINCT ${queryTypeOfTable.table}.${queryTypeOfTable.primaryKey}"
      )
    )
    val wheres = mutableListOf<MySQLWhere>()
    var whereBool = Bool.and
    if (match.where.and != null) {
      for (where in match.where.and) {
        wheres.add(getMySQLWhereFromWhere(where, table))
      }
    } else if (match.where.or != null) {
      for (where in match.where.or) {
        wheres.add(getMySQLWhereFromWhere(where, table))
      }
      whereBool = Bool.or
    } else {
      wheres.add(getMySQLWhereFromWhere(match.where, table))
    }
    val isAlias =
      getCteAlias(typeOf, match.where?.iri ?: match.where.and?.firstOrNull()?.iri ?: match.where.or?.firstOrNull()?.iri)

    val with = MySQLWith(
      table,
      isAlias,
      wheres,
      isSelects,
      null,
      whereBool
    )

    with.joins = getJoins(mySQLQuery, with)
    for (where in wheres) {
      if (where is MySQLPropertyIsWhere) {
        val newJoins = addWhereConceptJoin(with)
        newJoins.forEach { join ->
          if (join !in with.joins!!) {
            with.joins!!.add(join)
          }
        }
      }
    }

    return with
  }

  private fun getMySQLWhereFromWhere(where: Where, table: Table): MySQLWhere {
    val field = tableMap?.functions[where.function?.iri] ?: getPropertyNameByTableAndPropertyIri(
      table,
      where.iri
    ).field ?: throw SQLConversionException("No field found for property ${where.iri}")
    val args = getFunctionArgumentMap(table, where)

    val where = if (where.`is` != null) {
      MySQLPropertyIsWhere(
        field,
        where.`is`,
        "=",
        where.isNot,
        args
      )
    } else if (where.range != null) {
      MySQLPropertyRangeWhere(
        field,
        where.range.from.operator.value,
        where.range.from.value,
        where.range.to.value,
        where.range.to.operator.value,
        where.isNot,
        args
      )
    } else {
      MySQLPropertyValueWhere(
        field,
        where.operator.value,
        where.value ?: where.relativeTo.parameter,
        where.isNot,
        args
      )
    }

    return where
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

  private fun getTableFromTypeAndProperty(typeIri: String?, propertyIri: String?): Table {
    val table = tableMap?.getTableFromDataModel(typeIri) ?: throw SQLConversionException(
      "Type $typeIri not found in table map"
    )
    return table
  }

  private fun getPropertyNameByTableAndPropertyIri(table: Table, propertyIri: String): Field {
    val field = table.fields.get(propertyIri) ?: throw SQLConversionException(
      "Property $propertyIri not found in table ${table.table}"
    )
    return field
  }

  private fun getJoins(mySQLQuery: MySQLQuery, with: MySQLWith): MutableList<MySQLJoin> {
    val joins: MutableList<MySQLJoin> = mutableListOf()
    if (mySQLQuery.withs.isNotEmpty()) {
      joins.add(
        (with.table.getJoinCondition(
          tableTo = mySQLQuery.withs.last().table,
          tableToAlias = mySQLQuery.withs.last().alias
        ))
      )
    }
    if (queryTypeOf != null && queryTypeOf != with.table.dataModel) {
      val queryTypeOfTable = getTableFromTypeAndProperty(queryTypeOf, null)
      joins.add(with.table.getJoinCondition(tableTo = queryTypeOfTable, tableToAlias = queryTypeOfTable.table))
    }
    return joins
  }

  private fun addWhereConceptJoin(with: MySQLWith): MutableList<MySQLJoin> {
    val joins: MutableList<MySQLJoin> = mutableListOf()
    val conceptTable = getTableFromTypeAndProperty(IM.CONCEPT.toString(), null)
    joins.add(with.table.getJoinCondition(tableTo = conceptTable))

    val conceptMemberTable = getTableFromTypeAndProperty(IM.CONCEPT.toString() + "Member", null)
    joins.add(
      conceptTable.getJoinCondition(
        tableFrom = conceptTable,
        tableTo = conceptMemberTable,
      )
    )
    return joins
  }
}