package org.endeavourhealth.imapi.transforms

import java.util.Locale.getDefault

/**
 * Allocates unique, length-limited SQL aliases for CTEs and joined tables.
 */
class AliasAllocator {
  private val maxAliasLength = 64
  private var longAliasCounter = 1
  private val usedAliases = mutableSetOf<String>()
  private var cteCounter = 0

  /** Clears per-query alias state. The long-alias counter is deliberately kept across resets. */
  fun reset() {
    usedAliases.clear()
    cteCounter = 0
  }

  fun sanitiseAlias(alias: String): String {
    return alias.replace("...", "_").replace(" ", "_").replace(".", "_").replace("-", "_").lowercase(getDefault())
  }

  fun nextCteAlias(baseName: String): String {
    cteCounter++
    return ensureUniqueAlias("${sanitiseAlias(baseName)}_$cteCounter")
  }

  fun cteNormalise(value: String): String {
    return value.lowercase(getDefault()).replace(Regex("[^a-z0-9_]"), "_")
  }

  fun ensureUniqueAlias(baseAlias: String): String {
    fun normalize(a: String) =
      a.replace("`", "").lowercase()

    val alias = baseAlias.replace("`", "")

    if (alias.length > maxAliasLength) {
      var newAlias: String
      do {
        newAlias = "cte_${longAliasCounter++}"
      } while (usedAliases.contains(normalize(newAlias)))
      usedAliases.add(normalize(newAlias))
      return "`$newAlias`"
    }

    var uniqueAlias = alias
    var index = 2

    while (
      usedAliases.contains(normalize(uniqueAlias)) ||
      uniqueAlias.length > maxAliasLength
    ) {
      uniqueAlias = if (uniqueAlias.length > maxAliasLength) {
        "cte_${longAliasCounter++}"
      } else {
        "${alias}_$index"
      }
      index++
    }

    usedAliases.add(normalize(uniqueAlias))
    return "`$uniqueAlias`"
  }
}
