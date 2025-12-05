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
    for (isA in match.`is`) {
      val isAlias = getCteAlias(isA.iri, null)
      val isSelects = mutableListOf(MySQLSelect("id"))
      val joins = mutableListOf<MySQLJoin>()
      if (mySQLQuery.withs.isNotEmpty()) {
        joins.add(MySQLJoin(mySQLQuery.withs.last().alias, "id", "id"))
      }
      mySQLQuery.withs.add(
        MySQLWith(
          getTableFromTypeAndProperty(IM.COHORT.toString(), IM.ID.toString()),
          isAlias,
          mutableListOf(MySQLPropertyValueWhere("hashCode", "=", isA.iri, null, null)),
          isSelects,
          joins.ifEmpty { null }
        )
      )
      // TODO: replace isA.iri in new MySQLWhere() with the hashcode of query definition+arguments - create a map of iri to hashCode initially
    }
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
    val isAlias = getCteAlias(typeOf, match.where.iri)
    val isSelects = mutableListOf(MySQLSelect("id"))
    val field = getPropertyNameByTableAndPropertyIri(table, match.where.iri)

    val where = if (match.where.`is` != null) {
      MySQLPropertyIsWhere(
        field.field,
        match.where.`is`,
        "=",
        match.where.isNot,
        getFunctionArgumentMap(table, match.where)
      )
    } else {
      MySQLPropertyValueWhere(
        field.field,
        match.where.operator.value,
        match.where.value,
        match.where.isNot,
        getFunctionArgumentMap(table, match.where)
      )
    }

    val with = MySQLWith(
      table,
      isAlias,
      mutableListOf(
        where
      ),
      isSelects,
      null
    )

    with.joins = getJoins(mySQLQuery, with)
    return with
  }


  private fun getFunctionArgumentMap(table: Table, where: Where): HashMap<String, String> {
    val argMap = HashMap<String, String>()
    if (where.function?.argument != null)
      for (argument in where.function.argument) {
        val valueToReplace = argument.parameter
        var valueToReplaceWith = getArgValue(table, argument)
        if (argument.parameter == "units") valueToReplaceWith = getUnitName(valueToReplaceWith)
        argMap[valueToReplace] = valueToReplaceWith
      }
    return argMap
  }

  private fun getArgValue(table: Table, argument: Argument): String {
    if (argument.valuePath != null) {
      return getPropertyNameByTableAndPropertyIri(table, argument.valuePath.iri).field
    } else if (argument.valueParameter != null) {
      return argument.valueParameter
    } else if (argument.valueIri != null) {
      return argument.valueIri.iri
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
      else -> ""
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
      joins.add((with.table.getJoinCondition(mySQLQuery.withs.last().table, mySQLQuery.withs.last().alias, null)))
    }
    if (queryTypeOf != null && queryTypeOf != with.table.dataModel) {
      val queryTypeOfTable = getTableFromTypeAndProperty(queryTypeOf, null)
      joins.add(with.table.getJoinCondition(queryTypeOfTable, queryTypeOfTable.table, null))
    }
    return joins
  }
}