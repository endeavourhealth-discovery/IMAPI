package org.endeavourhealth.imapi.model.sql

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.extern.slf4j.Slf4j
import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.Bool
import org.endeavourhealth.imapi.model.imq.FunctionClause
import org.endeavourhealth.imapi.model.imq.Match
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.imq.Where
import org.endeavourhealth.imapi.model.requests.QueryRequest

@Slf4j
class IMQtoSQLConverterKotlin @JvmOverloads constructor(
  val queryRequest: QueryRequest, val mapper: ObjectMapper? = ObjectMapper()
) {

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
    } catch (e: Exception) {
      throw RuntimeException(e)
    } catch (e: SQLConversionException) {
      println("SQL Conversion Error: $e.message")
      throw e
    } catch (e: JsonProcessingException) {
      println("SQL Conversion Error: $e.message")
      throw e
    }
  }

  private fun generateSQL(definition: Query): String {
    if (definition.getTypeOf() == null || definition.getTypeOf()
        .getIri() == null
    ) throw SQLConversionException("Query typeOf is null")
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


//    if(definition.`return` != null) {
//
//    }
    mySQLQuery.selects.add(MySQLSelect("id"))
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
          "cohort",
          isAlias,
          mutableListOf(MySQLWhere("hashCode", isA.iri, "=", null)),
          isSelects,
          joins.ifEmpty { null }
        )
      )
      // TODO: replace isA.iri in new MySQLWhere() with the hashcode of query definition+arguments - create a map of iri to hashCode initially
    }
  }

  private fun getFunctionArgumentMap(where: Where): HashMap<String, String> {
    val argMap = HashMap<String, String>()
    if (where.function.argument != null)
      for (argument in where.function.argument) {
        val valueToReplace = argument.parameter
        val valueToReplaceWith =
          argument.valuePath?.iri
            ?: argument.valueParameter
            ?: argument.valueIri?.iri
            ?: throw SQLConversionException("No value provided for argument $argument")
        argMap[valueToReplace] = valueToReplaceWith
      }
    return argMap
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
    val table = getTableFromTypeAndProperty(match.typeOf.iri, match.where.iri)
    val isAlias = getCteAlias(match.typeOf.iri, match.where.iri)
    val isSelects = mutableListOf(MySQLSelect("id"))
    val field = getPropertyNameByTableAndPropertyIri(table, match.where.iri)
    val joins = mutableListOf<MySQLJoin>()
    if (mySQLQuery.withs.isNotEmpty()) {
      joins.add(MySQLJoin(mySQLQuery.withs.last().alias, "id", "id"))
    }
    return MySQLWith(
      if (match.typeOf.iri == "cohort") "cohort" else table.table,
      isAlias,
      mutableListOf(MySQLWhere(field.field, match.where.value, match.where.operator.value, getFunctionArgumentMap(match.where))),
      isSelects,
      joins.ifEmpty { null }
    )
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

  private fun getCteAlias(typeIri: String?, propertyIri: String?): String {
    val typeIriSuffix = typeIri?.substringAfter('#')
    if (propertyIri == null)
      return "${typeIriSuffix}_cte"
    val propertyIriSuffix = propertyIri.substringAfter('#')
    return "${typeIriSuffix}_${propertyIriSuffix}_cte"
  }
}