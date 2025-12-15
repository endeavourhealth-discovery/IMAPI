package org.endeavourhealth.imapi.model.sql

import org.endeavourhealth.imapi.model.imq.Node

interface MySQLWhere {
  val property: String?
  val sqlTemplate: String
  val args: Map<String, String>?
  var and: MutableList<MySQLWhere>?
  var or: MutableList<MySQLWhere>?
  val not: Boolean?
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
  override val not: Boolean? = false
) : MySQLWhere {
  override val sqlTemplate = ""
}

class MySQLPropertyValueWhere(
  override val property: String,
  val operator: String,
  val value: String,
  override val args: Map<String, String>? = null,
  override var and: MutableList<MySQLWhere>? = null,
  override var or: MutableList<MySQLWhere>? = null,
  override val not: Boolean? = false
) : MySQLWhere {
  override val sqlTemplate: String
    get() {
      val base = "$property $operator $value"
      return if (not == true) "NOT ($base)" else base
    }
}

class MySQLPropertyIsNullWhere(
  override val property: String,
  override val args: Map<String, String>? = null,
  override var and: MutableList<MySQLWhere>? = null,
  override var or: MutableList<MySQLWhere>? = null,
  override val not: Boolean? = false
) : MySQLWhere {
  override val sqlTemplate: String
    get() {
      val base = "$property IS NULL"
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
  override val not: Boolean? = false
) : MySQLWhere {
  override val sqlTemplate: String
    get() {
      val base = "$property $operator $value AND $property $operator2 $value2"
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
  override val not: Boolean? = false
) : MySQLWhere {

  override val sqlTemplate: String
    get() = ""

  override fun baseSql(): String {
    val blocks = values.map { node ->
      val iri = node.iri
      val selfValue = if (node.isDescendantsOf) 0 else 1
      val base = """(
        concept_set_member.set $operator '$iri'
        AND concept_set_member.self = $selfValue
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
