package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.endeavourhealth.imapi.model.sql.Table
import org.endeavourhealth.imapi.model.sql.TableMap

/**
 * State shared by the compilers during one conversion: the table mapping, the type the query is rooted at, and the
 * per-query alias, named-step and node-path registries.
 */
internal class ConversionContext(private val tableMap: TableMap) {
  /** The table of the query's `typeOf`; set before any compiler runs. */
  lateinit var queryTypeOfTable: Table

  val aliases = AliasAllocator()
  val keepAsMap = HashMap<String, MySQLWith>()
  val nodePathContextMap = HashMap<String, NodePathContext>()

  /** The table for a property if it has its own mapping, else for the data model; null when neither is mapped. */
  fun findTable(typeIri: String?, propertyIri: String?): Table? = tableMap.findTable(typeIri, propertyIri)

  fun getTable(typeIri: String?, propertyIri: String?): Table =
    findTable(typeIri, propertyIri) ?: throw SQLConversionException("Type $typeIri not found in table map")

  fun resetPerQueryState() {
    aliases.reset()
    nodePathContextMap.clear()
    keepAsMap.clear()
  }
}
