package org.endeavourhealth.imapi.model.sql

class MySQLJoin(val table: String, val innerProperty: String, val outerProperty: String, val inner: Boolean? = false, val exclude: Boolean? = false) {
}