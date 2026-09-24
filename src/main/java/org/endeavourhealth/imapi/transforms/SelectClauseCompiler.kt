package org.endeavourhealth.imapi.transforms

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.Expression
import org.endeavourhealth.imapi.model.imq.Return
import org.endeavourhealth.imapi.model.imq.When
import org.endeavourhealth.imapi.model.sql.MySQLJoin
import org.endeavourhealth.imapi.model.sql.MySQLQuery
import org.endeavourhealth.imapi.model.sql.MySQLSelect
import org.endeavourhealth.imapi.model.sql.Table
import org.endeavourhealth.imapi.vocabulary.IM

/**
 * Compiles IMQ return clauses (properties, functions, CASE expressions, literals) into SQL select items.
 */
internal class SelectClauseCompiler(
  private val queryTypeOfTableProvider: () -> Table,
  private val nodePathContextMap: Map<String, NodePathContext>,
) {
  private val queryTypeOfTable: Table get() = queryTypeOfTableProvider()

  fun getSelects(
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
          selects.add(MySQLSelect(SqlLiteralUtils.toSqlLiteral(ret.value), ret.`as`))
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
      val fk = resolveForeignKeyByDataModel(currentTable, queryTypeOfTable).first
        ?: throw SQLConversionException("No relationship from ${currentTable.table} to ${queryTypeOfTable.table}")
      return "$currentWithAlias.$fk IS NOT NULL"
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

      return "$tableAlias.$field $operator ${SqlLiteralUtils.toSqlLiteral(value)}"
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
      return SqlLiteralUtils.toSqlLiteral(it)
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
}
