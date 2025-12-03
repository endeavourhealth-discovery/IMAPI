package org.endeavourhealth.imapi.model.sql

import org.endeavourhealth.imapi.model.imq.Argument

class MySQLWhere(
  val property: String,
  val value: String,
  val operator: String,
  val arguments: HashMap<String, String>?
) {
  fun toSql(): String {
    var where = "$property $operator $value"
    if (arguments?.isNotEmpty() == true) {
      for ((key, value) in arguments) {
        where = where.replace(key, value)
      }
    }
    return where
  }
}

