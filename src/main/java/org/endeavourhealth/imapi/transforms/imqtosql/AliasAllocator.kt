package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.sql.quoteIdentifier
import java.util.Locale

/**
 * Allocates unique, length-limited SQL aliases for CTEs and joined tables.
 */
class AliasAllocator {
  private companion object {
    const val MAX_ALIAS_LENGTH = 64
    val NON_ALIAS_CHARS = Regex("[^a-z0-9_]")
  }

  private var longAliasCounter = 1
  private val usedAliases = mutableSetOf<String>()
  private var cteCounter = 0

  /** Clears per-query alias state. The long-alias counter is deliberately kept across resets. */
  fun reset() {
    usedAliases.clear()
    cteCounter = 0
  }

  fun sanitiseAlias(alias: String): String {
    return alias.replace("...", "_").replace(" ", "_").replace(".", "_").replace("-", "_").lowercase(Locale.ROOT)
  }

  fun nextCteAlias(baseName: String): String {
    cteCounter++
    return ensureUniqueAlias("${sanitiseAlias(baseName)}_$cteCounter")
  }

  /** Registers a match's `as` verbatim as its CTE alias. The logic optimiser guarantees uniqueness, so a clash is an error. */
  fun useAs(matchAs: String): String {
    if (!usedAliases.add(normalize(matchAs))) {
      throw SQLConversionException("Duplicate 'as' in query: $matchAs")
    }
    return quoteIdentifier(matchAs)
  }

  fun cteNormalise(value: String): String {
    return value.lowercase(Locale.ROOT).replace(NON_ALIAS_CHARS, "_")
  }

  fun ensureUniqueAlias(baseAlias: String): String {
    val alias = baseAlias.replace("`", "")
    var candidate = if (alias.length > MAX_ALIAS_LENGTH) nextLongAlias() else alias
    var index = 2
    while (normalize(candidate) in usedAliases) {
      candidate = "${alias}_${index++}".let { if (it.length > MAX_ALIAS_LENGTH) nextLongAlias() else it }
    }
    usedAliases.add(normalize(candidate))
    return quoteIdentifier(candidate)
  }

  private fun nextLongAlias(): String {
    var candidate: String
    do {
      candidate = "cte_${longAliasCounter++}"
    } while (normalize(candidate) in usedAliases)
    return candidate
  }

  private fun normalize(alias: String) = alias.replace("`", "").lowercase(Locale.ROOT)
}
