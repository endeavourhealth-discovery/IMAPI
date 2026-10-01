package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.sql.MySQLQuery
import org.endeavourhealth.imapi.model.sql.MySQLSelect

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
          .filterNot { it.alias == ROW_NUMBER_ALIAS || isEntityKeySelect(it) }
      }
    } else {
      lastWith.selects.filterNot { isEntityKeySelect(it) || it.name.contains("*") }
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

  // the bare key select identifies the row; an aliased one is a requested column
  private fun isEntityKeySelect(select: MySQLSelect) = select.name == "patient.id" && select.alias == null
}
