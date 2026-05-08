package org.endeavourhealth.imapi.model.sql

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.Node

interface MySQLWhere {
  val property: String?
  val sqlTemplate: String
  val args: Map<String, String>?
  var and: MutableList<MySQLWhere>?
  var or: MutableList<MySQLWhere>?
  val not: Boolean?
  val table: String?
  fun baseSql(): String {
    if (args == null) return sqlTemplate
    var resolved = sqlTemplate
    for ((key, value) in args) {
      resolved = resolved.replace("{$key}", value)
    }
    return resolved
  }

  fun toSql(): String {
    val parts = mutableListOf<String>()
    val base = baseSql()
    if (base.isNotBlank()) {
      parts.add(base)
    }

    and?.takeIf { it.isNotEmpty() }?.let {
      parts.add(
        it.joinToString(
          separator = " AND ",
          prefix = "(",
          postfix = ")"
        ) { child -> child.toSql() }
      )
    }

    or?.takeIf { it.isNotEmpty() }?.let {
      parts.add(
        it.joinToString(
          separator = " OR ",
          prefix = "(",
          postfix = ")"
        ) { child -> child.toSql() }
      )
    }

    return when (parts.size) {
      0 -> ""
      1 -> parts.first()
      else -> parts.joinToString(
        separator = " AND ",
        prefix = "(",
        postfix = ")"
      )
    }
  }
}

class MySQLBoolWhere(
  override var and: MutableList<MySQLWhere>? = null,
  override var or: MutableList<MySQLWhere>? = null,
  override val property: String? = null,
  override val args: Map<String, String>? = null,
  override val not: Boolean? = false,
  override val table: String? = null,
) : MySQLWhere {
  override val sqlTemplate = ""
}

class MySQLCompareWhere(
  override val property: String,
  val operator: String,
  val right: String,
  val value: String,
  val units: String? = null,
  val qualifier: String? = null,
  override val args: Map<String, String>? = null,
  override var and: MutableList<MySQLWhere>? = null,
  override var or: MutableList<MySQLWhere>? = null,
  override val not: Boolean? = false,
  override val table: String? = null,
) : MySQLWhere {
  override val sqlTemplate: String
    get() {
//      TODO: instead of property.startsWith("TIMESTAMPDIFF") check if function
      val prop = if (table != null && !property.startsWith("TIMESTAMPDIFF")) "`${table}`.$property" else property
      val base =
        if (units != null) {
          when (units) {
            "DAY", "MONTH", "YEAR" -> "TIMESTAMPDIFF($units, $prop, $right) $operator $value"
            else -> throw SQLConversionException("Unsupported unit $units")
          }
        } else if (qualifier != null) {
          when (qualifier) {
            "QUARTER" -> "((YEAR($prop) - YEAR($right)) * 4 + (QUARTER($prop) - QUARTER($right))) $operator $value"
            "FISCAL_YEAR" -> "(YEAR(DATE_SUB($prop, INTERVAL 3 MONTH)) + 1) - (YEAR(DATE_SUB($right, INTERVAL 3 MONTH)) + 1) $operator $value"
            "DAYS", "MONTHS", "YEARS" -> "$qualifier($prop) - $qualifier($right) $operator $value"
            else -> "$prop - $right $operator $value"
          }
        } else throw SQLConversionException("No units or qualifier provided")
      return if (not == true) "NOT ($base)" else base
    }
}

class MySQLPropertyValueWhere(
  override val property: String,
  val operator: String,
  val value: String,
  val qualifier: String? = null,
  override val args: Map<String, String>? = null,
  override var and: MutableList<MySQLWhere>? = null,
  override var or: MutableList<MySQLWhere>? = null,
  override val not: Boolean? = false,
  override val table: String? = null,
) : MySQLWhere {
  override val sqlTemplate: String
    get() {
      val prop = if (table != null && !property.startsWith("TIMESTAMPDIFF")) "`${table}`.$property" else property
      val base = if (qualifier != null) {
        when (qualifier) {
          "QUARTER" -> "(YEAR($prop) $operator YEAR($value) AND (QUARTER($prop) $operator QUARTER($value))"
          "FISCAL_YEAR" -> "(YEAR(DATE_SUB($prop, INTERVAL 3 MONTH)) + 1) $operator (YEAR(DATE_SUB($value, INTERVAL 3 MONTH)) + 1)"
          else -> "$qualifier($prop) $operator $qualifier($value)"
        }
      } else return "$prop $operator $value"
      return if (not == true) "NOT ($base)" else base
    }
}

class MySQLPropertyIsNullWhere(
  override val property: String,
  override val args: Map<String, String>? = null,
  override var and: MutableList<MySQLWhere>? = null,
  override var or: MutableList<MySQLWhere>? = null,
  override val not: Boolean? = false,
  override val table: String? = null,
) : MySQLWhere {
  override val sqlTemplate: String
    get() {
      val prop = if (table != null && !property.startsWith("TIMESTAMPDIFF")) "`${table}`.$property" else property
      val base = "$prop IS NULL"
      return if (not == true) "$property IS NOT NULL" else base
    }
}


class MySQLPropertyRangeWhere(
  override val property: String,
  val operator: String,
  val value: String,
  val value2: String,
  val operator2: String,
  override val args: Map<String, String>? = null,
  override var and: MutableList<MySQLWhere>? = null,
  override var or: MutableList<MySQLWhere>? = null,
  override val not: Boolean? = false,
  override val table: String? = null,
) : MySQLWhere {
  override val sqlTemplate: String
    get() {
      val prop = if (table != null && !property.startsWith("TIMESTAMPDIFF")) "`${table}`.$property" else property
      val base = "$prop $operator $value AND $prop $operator2 $value2"
      return if (not == true) "NOT ($base)" else base
    }
}

class MySQLPropertyIsWhere(
  override val property: String,
  val values: List<Node>,
  val operator: String = "=",
  override val args: Map<String, String>? = null,
  override var and: MutableList<MySQLWhere>? = null,
  override var or: MutableList<MySQLWhere>? = null,
  override val not: Boolean? = false,
  override val table: String? = null,
) : MySQLWhere {

  override val sqlTemplate: String
    get() = ""

  override fun baseSql(): String {
    val blocks = values.map { node ->
      val iri = node.iri
      val selfValue =
        if (!node.isDescendantsOf && !node.isAncestorsOf && !node.isDescendantsOrSelfOf && !node.isMemberOf) 1 else 0
      val base = if (selfValue == 0) "concept_tct.parent $operator '$iri'"
      else """(
        concept_tct.parent $operator '$iri'
        AND concept_tct.self = $selfValue
      )
      """.trimIndent()
      if (not == true) "NOT ($base)" else base
    }
    val sql = blocks.joinToString(" OR ", prefix = "(", postfix = ")")
    if (args == null) return sql
    var resolved = sql
    for ((key, value) in args) {
      resolved = resolved.replace("{$key}", value)
    }
    return resolved
  }
}
