package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.imq.Path
import org.endeavourhealth.imapi.model.sql.MySQLJoin
import org.endeavourhealth.imapi.model.sql.MySQLPropertyValueWhere
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.endeavourhealth.imapi.model.sql.Table

/**
 * Resolves IMQ match paths to tables, registering node tables and adding the joins along the path.
 */
internal class PathJoinBuilder(
  private val nodePathContextMap: MutableMap<String, NodePathContext>,
  private val findTableFromTypeAndProperty: (String?, String?) -> Table?,
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
      val table = findTableFromTypeAndProperty(path.typeOf.iri, path.iri)
      val join = table?.let { buildJoin(parentTable, it, path, nodeKey) }

      if (table == null || join == null) {
        // an unmapped path degrades to its parent table; selects may then resolve through the field's returnAs
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
        continue
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
    }
  }

  /** The join from [parentTable] to [table], or null when the mapping defines no relationship between them. */
  private fun buildJoin(parentTable: Table, table: Table, path: Path, nodeKey: String?): MySQLJoin? {
    table.alias = nodeKey
    val join = try {
      parentTable.getJoinCondition(
        joinType = if (path.isOptional) "LEFT JOIN" else "JOIN",
        tableTo = table,
        tableToAlias = table.alias,
        tableFromAlias = parentTable.alias,
        viaProperty = path.iri,
      )
    } catch (_: SQLConversionException) {
      return null
    }

    table.condition?.let {
      join.wheres.add(
        MySQLPropertyValueWhere(
          property = it.field,
          operator = "=",
          value = it.value
        )
      )
    }
    return join
  }
}
