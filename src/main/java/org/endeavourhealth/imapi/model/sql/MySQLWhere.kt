package org.endeavourhealth.imapi.model.sql

import org.endeavourhealth.imapi.model.imq.Node

interface MySQLWhere {
  val property: String
  val sqlTemplate: String
  val args: Map<String, String>?
  fun toSql(): String {
    if (args == null) return sqlTemplate
    var resolved = sqlTemplate
    for ((key, value) in args) {
      resolved = resolved.replace("{$key}", value)
    }
    return resolved
  }
}

class MySQLPropertyValueWhere(
  override val property: String,
  val operator: String,
  val value: String,
  val isNot: Boolean? = false,
  override val args: Map<String, String>? = null
) : MySQLWhere {
  override val sqlTemplate = if (isNot == true) "NOT $property $operator $value" else "$property $operator $value"
}


class MySQLPropertyRangeWhere(
  override val property: String,
  val operator: String,
  val value: String,
  val value2: String,
  val operator2: String,
  val isNot: Boolean? = false,
  override val args: Map<String, String>? = null,
) : MySQLWhere {
  override val sqlTemplate =
    if (isNot == true) "NOT ($property $operator $value AND $property $operator2 $value2)" else "$property $operator $value AND $property $operator2 $value2"
}

class MySQLPropertyIsWhere(
  override val property: String,
  val values: List<Node>,
  val operator: String = "=",
  val isNot: Boolean? = false,
  override val args: Map<String, String>? = null,
) : MySQLWhere {

  override val sqlTemplate: String
    get() = ""

  override fun toSql(): String {
    val blocks = values.map { node ->
      val iri = node.iri
      val selfValue = if (node.isDescendantsOf) 0 else 1
      if (isNot == true)
        """
            NOT (
                concept_set_member.set $operator '$iri'
                AND concept_set_member.self = $selfValue
            )
            """.trimIndent()
      else """
            (
                concept_set_member.set $operator '$iri'
                AND concept_set_member.self = $selfValue
            )
            """.trimIndent()
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
