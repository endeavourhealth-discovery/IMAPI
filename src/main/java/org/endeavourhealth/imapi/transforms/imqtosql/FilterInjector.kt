package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.imapi.model.sql.MySQLJoin
import org.endeavourhealth.imapi.model.sql.MySQLPropertyValueWhere
import org.endeavourhealth.imapi.model.sql.MySQLQuery
import org.endeavourhealth.imapi.model.sql.MySQLSelect
import org.endeavourhealth.imapi.model.sql.MySQLWhere
import org.endeavourhealth.imapi.model.sql.Table
import org.endeavourhealth.imapi.vocabulary.NAMESPACE

/**
 * Adds the patient and organisation joins/filters (driven by query request arguments) to a compiled query.
 */
internal class FilterInjector(
  private val queryRequest: QueryRequest,
  private val ctx: ConversionContext,
) {
  private val queryTypeOfTable: Table get() = ctx.queryTypeOfTable

  private fun getTableFromTypeAndProperty(typeIri: String?, propertyIri: String?): Table =
    ctx.getTable(typeIri, propertyIri)

  fun injectPatientFilter(mySqlQuery: MySQLQuery) {
    val patientTable = getTableFromTypeAndProperty("${NAMESPACE.IM.asIri().iri}Patient", null)
    val found = mySqlQuery.joins.find { it.tableTo == patientTable.table }
    if (found != null) {
      getPatientFilterWhere(patientTable)?.let { found.wheres.add(it) }
    } else {
      val lastCTE = mySqlQuery.withs.lastIncluded()
      val (fk, _) = resolveForeignKeyByTableName(lastCTE.table, patientTable)
      if (fk == null) {
        throw SQLConversionException("No relationship between ${lastCTE.table.table} and ${patientTable.table}")
      }

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
        value = patientIds.map { SqlLiteralUtils.toSqlLiteral(it) }
          .let { if (it.size == 1) it.first() else it.joinToString(prefix = "(", separator = ",", postfix = ")") },
        table = table.table,
      )
    }
    return null
  }

  fun injectOrgReturnAndFilter(mySqlQuery: MySQLQuery) {
    val joinTable = getTableFromTypeAndProperty(queryTypeOfTable.dataModel, null)
    val lastCTE = mySqlQuery.withs.lastIncluded()
    val fk = getLastCteEntityKeyField(lastCTE, queryTypeOfTable)
      ?: throw SQLConversionException("No entity key on ${lastCTE.alias} to join ${joinTable.table}")

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
}
