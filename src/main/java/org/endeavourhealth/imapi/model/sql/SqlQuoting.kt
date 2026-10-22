package org.endeavourhealth.imapi.model.sql

/** Quotes [value] as a MySQL string literal, escaping quotes and backslashes (MySQL's default escape mode). */
fun quoteSqlString(value: String): String = "'${value.replace("\\", "\\\\").replace("'", "''")}'"

/** Quotes [name] as a MySQL identifier, doubling any embedded backticks. */
fun quoteIdentifier(name: String): String = "`${name.replace("`", "``")}`"
