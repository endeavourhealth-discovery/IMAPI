package org.endeavourhealth.imapi.transforms

import org.endeavourhealth.imapi.model.sql.MySQLWith

internal object PatientTraceSqlBuilder {
  fun build(withs: List<MySQLWith>, queryIri: String, patientId: String, isPatientRooted: Boolean): String {
    val queryIriLiteral = SqlLiteralUtils.toSqlLiteral(queryIri)
    val patientLiteral = SqlLiteralUtils.toSqlLiteral(patientId)

    val checks = withs.mapIndexed { index, with ->
      val stepLabel = SqlLiteralUtils.toSqlLiteral(with.alias.replace("`", ""))
      val keyField = with.entityKeyField
      val patientFoundSelect = if (isPatientRooted && keyField != null) {
        "EXISTS(SELECT 1 FROM ${with.alias} WHERE ${with.alias}.$keyField = $patientLiteral)"
      } else "NULL"
      "SELECT $queryIriLiteral AS query_iri, $patientLiteral AS patient_id, " +
        "$index AS step_no, $stepLabel AS cte_name, $patientFoundSelect AS patient_found"
    }

    return buildString {
      append("INSERT INTO dataset.patient_exists (query_iri, patient_id, step_no, cte_name, patient_found)\n")
      append("WITH ")
      append(withs.joinToString(",\n") { it.toSql() })
      append("\n")
      append(checks.joinToString("\nUNION ALL\n"))
      append(";")
    }
  }
}
