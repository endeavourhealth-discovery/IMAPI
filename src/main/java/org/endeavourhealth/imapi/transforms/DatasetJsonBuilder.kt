package org.endeavourhealth.imapi.transforms

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.sql.MySQLQuery

internal object DatasetJsonBuilder {
  fun build(newMySqlQuery: MySQLQuery): String {
    val lastWith = newMySqlQuery.withs.last()

    val selects = if (
      lastWith.selects.size == 1 &&
      lastWith.selects.first().name.contains("*")
    ) {
      if (lastWith.subQuery != null) {
        lastWith.subQuery?.selects?.filterNot { it.alias == null || it.alias == ROW_NUMBER_ALIAS }
      } else {
        newMySqlQuery.withs
          .dropLast(1)
          .last()
          .selects
          .filterNot { it.alias == ROW_NUMBER_ALIAS || it.name == "patient.id" }
      }
    } else {
      lastWith.selects.filterNot { it.name == "patient.id" }
    }

    if (selects == null) throw SQLConversionException("No selects found in last with")

    return buildString {
      append("JSON_OBJECT(\n")
      append(
        selects
          .filterNot { it.alias == "id" }
          .joinToString(",\n") { select ->
            val rawAliasOrName = (select.alias ?: select.name).replace("`", "")
            val key = "\"$rawAliasOrName\""
            val value = if (select.alias != null) {
              "`${rawAliasOrName}`"
            } else {
              select.name
            }
            " $key, $value"
          }
      )
      append("\n)")
    }
  }
}
