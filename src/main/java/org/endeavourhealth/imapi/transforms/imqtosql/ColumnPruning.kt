package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.model.sql.MySQLPropertyValueWhere
import org.endeavourhealth.imapi.model.sql.MySQLQuery
import org.endeavourhealth.imapi.model.sql.MySQLSelect
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.endeavourhealth.imapi.model.sql.quoteIdentifier
import java.util.Locale

/**
 * Narrows the `table.*` selects of a compiled query to the columns that later steps actually use.
 *
 * Every match with an `as` selects all columns of its table so that later steps can refer to them. Most never do, so
 * the CTEs carry (and `DISTINCT`-sort, and hold in temp tables) columns nothing reads. This pass works backwards
 * through the CTE chain: the columns a step has to expose are the ones later text refers to as `step.column`, plus
 * whatever steps that select `step.*` themselves need to pass on. A `X.*` is then replaced by that explicit list.
 *
 * It is deliberately conservative and leaves a star alone when it cannot be sure:
 * - the query uses an aggregate or a window limit above 1, where the number of rows a step produces matters;
 * - a step is referred to as `step.*` in a form it does not model (e.g. `step.*, step.entity_id AS patient_id`);
 * - the last step could repeat an entity, because removing columns could then merge rows and change the result.
 *
 * Switch it off with `-Dimapi.sql.pruneColumns=false`.
 */
internal object ColumnPruning {
  const val ENABLE_PROPERTY = "imapi.sql.pruneColumns"

  private const val IDENT = "(?:`[^`]+`|[A-Za-z_][A-Za-z0-9_\$]*)"
  private val TOKEN = Regex("(`[^`]*`)|(\"(?:[^\"\\\\]|\\\\.)*\")|('(?:[^'\\\\]|\\\\.|'')*')|(--[^\\n]*)")
  private val QUALIFIED = Regex("(?<![\\w`.\$])($IDENT)\\.($IDENT|\\*)")
  private val STAR_SELECT = Regex("($IDENT)\\.\\*")
  private val BARE_IDENT = Regex("(?<![\\w`.\$])(?<!AS )`([^`]+)`(?!\\.)")
  private val ROW_LIMIT = Regex("\\brn`?\\s*<=\\s*(\\d+)")
  private val PLAIN_NAME = Regex("[A-Za-z_][A-Za-z0-9_]*")

  /** Columns referred to as `qualifier.column`, and how many times each `qualifier.*` appears. */
  private class Refs {
    val columns = HashMap<String, LinkedHashMap<String, String>>()
    val stars = HashMap<String, Int>()
  }

  private class State(
    val aliasIndex: Map<String, Int>,
    val pending: Array<LinkedHashMap<String, String>>,
    val unprunable: BooleanArray,
    val starsHandled: HashMap<String, Int> = HashMap(),
  )

  fun prune(query: MySQLQuery) {
    if (System.getProperty(ENABLE_PROPERTY, "true").equals("false", ignoreCase = true)) return
    val withs = query.withs
    if (withs.isEmpty()) return

    val aliasIndex = HashMap<String, Int>()
    withs.forEachIndexed { i, w -> aliasIndex[norm(w.alias)] = i }
    if (aliasIndex.size != withs.size) return

    val texts = withs.map { strip(it.toSql()) }
    if (!isSafeToPrune(texts)) return

    val refs = texts.map { collectRefs(it) }
    val finalText = strip(finalStatementText(query))
    val finalRefs = collectRefs(finalText)

    val starsSeen = HashMap<String, Int>()
    (refs + finalRefs).forEach { r -> r.stars.forEach { (q, n) -> starsSeen.merge(q, n, Int::plus) } }

    val state = State(aliasIndex, Array(withs.size) { LinkedHashMap() }, BooleanArray(withs.size))
    val last = withs.size - 1

    for (j in last downTo 0) {
      val w = withs[j]
      val key = norm(w.alias)
      val allStarsModelled = (starsSeen[key] ?: 0) == (state.starsHandled[key] ?: 0)
      val canPrune = !state.unprunable[j] && allStarsModelled && (j != last || isKeyUnique(w))

      val need: Map<String, String>? = if (!canPrune) null else {
        val columns = LinkedHashMap<String, String>()
        w.entityKeyField?.let { columns.putIfAbsent(it.lowercase(Locale.ROOT), it) }
        for (k in j + 1..last) refs[k].columns[key]?.forEach { (c, name) -> columns.putIfAbsent(c, name) }
        finalRefs.columns[key]?.forEach { (c, name) -> columns.putIfAbsent(c, name) }
        state.pending[j].forEach { (c, name) -> columns.putIfAbsent(c, name) }
        if (j == last && query.insert == DATASET_RESULTS_TABLE) {
          // the dataset JSON names the columns it needs unqualified
          BARE_IDENT.findAll(finalText).forEach { m -> columns.putIfAbsent(m.groupValues[1].lowercase(Locale.ROOT), m.groupValues[1]) }
        }
        columns
      }
      pushDown(w, need, j, state)
    }
  }

  /**
   * Applies [need] (the columns this step has to expose; null means all of them) to the star selects of [w] and of the
   * sub-selects it wraps, and records what the steps it reads from must in turn expose.
   */
  private fun pushDown(w: MySQLWith, need: Map<String, String>?, j: Int, state: State) {
    val explicit = w.selects.mapNotNull { it.alias }.map { it.lowercase(Locale.ROOT) }.toSet()
    val wrapperAlias = norm(w.fromAlias ?: SUBQUERY_ALIAS)
    val ownBase = norm(w.table.alias ?: w.table.table)
    var hasWrapperStar = false

    var idx = 0
    while (idx < w.selects.size) {
      val sel = w.selects[idx]
      val m = if (sel.alias == null && !sel.isAggregate) STAR_SELECT.matchEntire(sel.name) else null
      if (m == null) {
        idx++
        continue
      }
      val qualifierText = m.groupValues[1]
      val q = norm(qualifierText)
      val k = state.aliasIndex[q]
      val fromWrapper = w.subQuery != null && q == wrapperAlias

      when {
        fromWrapper -> {
          hasWrapperStar = true
          idx++
        }

        k != null && k < j -> {
          state.starsHandled.merge(q, 1, Int::plus)
          if (need == null) {
            state.unprunable[k] = true
            idx++
          } else {
            val columns = need.filterKeys { it !in explicit }
            state.pending[k].putAll(columns)
            idx += replaceStar(w, idx, qualifierText, columns)
          }
        }

        q == ownBase && w.subQuery == null && need != null -> idx += replaceStar(w, idx, qualifierText, need.filterKeys { it !in explicit })

        else -> idx++
      }
    }

    w.subQuery?.let { sub ->
      val own = ownRefs(w, wrapperAlias)
      val innerNeed: Map<String, String>? = when {
        hasWrapperStar && need == null -> null
        hasWrapperStar -> LinkedHashMap(need!!.filterKeys { it !in explicit }).also { it.putAll(own) }
        else -> own
      }
      pushDown(sub, innerNeed?.let { withKey(it, sub) }, j, state)
    }
    w.unionWiths.forEach { pushDown(it, null, j, state) }
  }

  private fun withKey(need: Map<String, String>, w: MySQLWith): Map<String, String> {
    val key = w.entityKeyField ?: return need
    if (key.lowercase(Locale.ROOT) in need) return need
    return LinkedHashMap<String, String>().also {
      it[key.lowercase(Locale.ROOT)] = key
      it.putAll(need)
    }
  }

  /** Replaces the star select at [idx] with one select per column and returns how many it was replaced by (1 if left). */
  private fun replaceStar(w: MySQLWith, idx: Int, qualifierText: String, columns: Map<String, String>): Int {
    if (columns.isEmpty()) return 1
    w.selects.removeAt(idx)
    w.selects.addAll(idx, columns.values.map { MySQLSelect("$qualifierText.${columnName(it)}") })
    return columns.size
  }

  private fun columnName(name: String) = if (PLAIN_NAME.matches(name)) name else quoteIdentifier(name)

  /** Rows of [w] are one per entity, so dropping columns cannot merge rows. */
  private fun isKeyUnique(w: MySQLWith): Boolean {
    if (w.subQuery != null) {
      return w.wheres.any { it is MySQLPropertyValueWhere && it.property == ROW_NUMBER_ALIAS && it.operator == "<=" && it.value == "1" }
    }
    val key = w.entityKeyField ?: return false
    if (w.unionWiths.isNotEmpty() || key != w.table.primaryKey) return false
    val base = norm(w.table.alias ?: w.table.table)
    return w.selects.any { it.alias == null && STAR_SELECT.matchEntire(it.name)?.let { m -> norm(m.groupValues[1]) == base } == true }
  }

  private fun isSafeToPrune(texts: List<String>): Boolean = texts.none { text ->
    text.contains("GROUP BY") || text.contains("COUNT(") ||
      ROW_LIMIT.findAll(text).any { it.groupValues[1] != "1" }
  }

  private fun finalStatementText(query: MySQLQuery): String =
    query.selects.joinToString(",\n") { it.toSql() } + "\n" + query.joins.joinToString("\n") { it.toSql() }

  /** Columns of [alias] referred to by the parts of [w] that are not its sub-select, wrapper or union branches. */
  private fun ownRefs(w: MySQLWith, alias: String): Map<String, String> {
    val parts = mutableListOf<String>()
    w.selects.forEach { parts.add(it.toSql()) }
    w.wheres.forEach { parts.add(it.toSql()) }
    w.joins.forEach { parts.add(it.toSql()) }
    parts.addAll(w.groupByColumns)
    w.havingClause?.let { parts.add(it) }
    w.orderBy?.let { parts.add(it.toSql()) }
    return collectRefs(strip(parts.joinToString("\n"))).columns[alias] ?: emptyMap()
  }

  private fun collectRefs(text: String): Refs {
    val refs = Refs()
    for (m in QUALIFIED.findAll(text)) {
      val qualifier = norm(m.groupValues[1])
      val column = m.groupValues[2]
      if (column == "*") {
        refs.stars.merge(qualifier, 1, Int::plus)
      } else {
        val name = column.trim('`')
        refs.columns.getOrPut(qualifier) { LinkedHashMap() }.putIfAbsent(name.lowercase(Locale.ROOT), name)
      }
    }
    return refs
  }

  /** Blanks string literals and comments (keeping backtick-quoted names) so their contents are not read as references. */
  private fun strip(text: String): String = TOKEN.replace(text) { m -> if (m.groups[1] != null) m.value else "" }

  private fun norm(name: String) = name.trim('`').lowercase(Locale.ROOT)
}
