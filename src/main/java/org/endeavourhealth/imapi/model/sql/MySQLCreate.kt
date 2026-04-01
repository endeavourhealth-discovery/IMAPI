package org.endeavourhealth.imapi.model.sql

class MySQLCreate(val tableHash: String, val dataset: Boolean? = false) {
  fun toSql(): String {
    if (dataset!!) return "CREATE TABLE `$tableHash` (\n" +
      "  cohort_id BIGINT,\n" +
      "  `group` VARCHAR(50),\n" +
      "  results JSON\n" +
      ")\n"
    return "CREATE TABLE `$tableHash` AS\n"
  }
}