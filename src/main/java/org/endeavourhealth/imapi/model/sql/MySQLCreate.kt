package org.endeavourhealth.imapi.model.sql

class MySQLCreate(val tableHash: String) {
  fun toSql(): String {
    return "CREATE TABLE `$tableHash` AS\n"
  }
}