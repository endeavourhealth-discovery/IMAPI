package org.endeavourhealth.imapi.transforms

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.Path
import org.endeavourhealth.imapi.model.sql.MySQLPropertyValueWhere
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.endeavourhealth.imapi.model.sql.Table

/**
 * Resolves IMQ match paths to tables, registering node tables and adding the joins along the path.
 */
internal class PathJoinBuilder(
  private val nodePathContextMap: MutableMap<String, NodePathContext>,
  private val getTableFromTypeAndProperty: (String?, String?) -> Table,
) {
  fun addPathTableAndJoins(
    paths: MutableList<Path>,
    tableMap: HashMap<String, Table>,
    with: MySQLWith,
    parentTable: Table = with.table,
    addJoins: Boolean = false
  ) {

    for (path in paths) {
      val nodeKey = path.node ?: path.name
      try {
        val table = getTableFromTypeAndProperty(path.typeOf.iri, path.iri)
        table.alias = nodeKey

        val join = parentTable.getJoinCondition(
          joinType = if (path.isOptional) "LEFT JOIN" else "JOIN",
          tableTo = table,
          tableToAlias = table.alias,
          tableFromAlias = parentTable.alias,
          viaProperty = path.iri,
        )

        if (table.condition != null) {
          join.wheres.add(
            MySQLPropertyValueWhere(
              property = table.condition!!.field,
              operator = "=",
              value = table.condition!!.value
            )
          )
        }

        if (nodeKey != null) {
          tableMap[nodeKey] = table
          nodePathContextMap[nodeKey] = NodePathContext(
            parentTable = parentTable,
            pathIri = path.iri,
            nodeTable = table
          )
        }

        if (!with.joins.contains(join) && addJoins) {
          with.joins.add(join)
        }

        if (path.path != null) {
          addPathTableAndJoins(path.path, tableMap, with, table, addJoins)
        }

      } catch (exception: SQLConversionException) {
        if (nodeKey != null) {
          tableMap[nodeKey] = parentTable
          nodePathContextMap[nodeKey] = NodePathContext(
            parentTable = parentTable,
            pathIri = path.iri,
            nodeTable = parentTable
          )
        }
        if (path.path != null) {
          addPathTableAndJoins(path.path, tableMap, with, parentTable, addJoins)
        }
      }
    }
  }
}
