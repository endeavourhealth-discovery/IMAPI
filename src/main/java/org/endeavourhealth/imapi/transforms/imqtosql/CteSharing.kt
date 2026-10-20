package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.model.sql.MySQLQuery
import org.endeavourhealth.imapi.model.sql.MySQLSelect
import org.endeavourhealth.imapi.model.sql.MySQLWith
import java.util.Locale

/**
 * Computes each distinct step of a statement once.
 *
 * Queries that repeat the same sub-condition (for example the same "latest value" off the same cohort in several branches) compile to
 * several CTEs with the same body under different names, and MySQL materialises each one. A step counts as a duplicate of an earlier
 * one only when its text is identical after every reference to an earlier duplicate has been replaced by that duplicate's original, so
 * it also joins the same upstream steps all the way down.
 *
 * A duplicate is not removed or renamed. Its body becomes `SELECT original.* FROM original` with no DISTINCT, which MySQL merges into
 * whoever reads it for free. Every existing reference to the duplicate's name therefore stays valid, and the original is materialised
 * once however many steps read it. The last step is never replaced because the final select reads it.
 *
 * Run it before [ColumnPruning], which then treats the replacement like any other step that passes its source through.
 * Switch it off with `-Dimapi.sql.shareCtes=false`.
 */
internal object CteSharing {
  const val ENABLE_PROPERTY = "imapi.sql.shareCtes"

  /** Returns how many steps were replaced by a reference to an identical earlier step. */
  fun share(query: MySQLQuery): Int {
    if (System.getProperty(ENABLE_PROPERTY, "true").equals("false", ignoreCase = true)) return 0
    val withs = query.withs
    if (withs.size < 2) return 0

    val originals = HashMap<String, MySQLWith>()   // signature -> first step with that text
    val canonical = HashMap<String, String>()      // every earlier step's alias (normalised) -> its original's alias (normalised)
    var replaced = 0

    for (i in 0 until withs.size - 1) {
      val w = withs[i]
      val key = norm(w.alias)
      if (key.isEmpty() || w.unionWiths.isEmpty() && w.selects.isEmpty()) continue

      val signature = signature(w, canonical)
      val original = originals[signature]
      if (original == null) {
        originals[signature] = w
        canonical[key] = key
        continue
      }
      canonical[key] = norm(original.alias)
      withs[i] = passThrough(w, original)
      replaced++
    }
    return replaced
  }

  /** A step that reads everything from [original]; reads of [duplicate]'s name keep working. */
  private fun passThrough(duplicate: MySQLWith, original: MySQLWith) = MySQLWith(
    table = duplicate.table.copy(table = original.alias),
    alias = duplicate.alias,
    selects = mutableListOf(MySQLSelect("${original.alias}.*")),
    exclude = duplicate.exclude,
    entityKeyField = duplicate.entityKeyField,
    isCohortRef = duplicate.isCohortRef,
    isCarrierAliased = duplicate.isCarrierAliased,
    distinct = false,
  )

  /**
   * The step's text without its own name or row-count comment. Every reference to an earlier step is rewritten to that step's original
   * in one normalised form, so steps that read different copies of the same thing compare equal.
   */
  private fun signature(w: MySQLWith, canonical: Map<String, String>): String {
    val body = w.toSql().lines().drop(1).filterNot { it.startsWith("-- row count") }.joinToString("\n")
    if (canonical.isEmpty()) return body
    val earlier = canonical.keys.joinToString("|") { Regex.escape(it) }
    val reference = Regex("(?<![\\w$])`?($earlier)`?(?![\\w$])", RegexOption.IGNORE_CASE)
    return reference.replace(body) { m -> canonical.getValue(m.groupValues[1].lowercase(Locale.ROOT)) }
  }

  private fun norm(alias: String) = alias.trim('`').lowercase(Locale.ROOT)
}
