package org.endeavourhealth.imapi.queryassistant

import org.endeavourhealth.imapi.vocabulary.NAMESPACE

/**
 * Prefixed IRIs ("im:effectiveDate") used in QuerySpec to keep specs short and easy for an LLM to copy.
 * The compiler accepts prefixed or full IRIs; the decompiler always emits prefixed ones where known.
 */
object IriPrefixes {
  val prefixes: Map<String, String> = linkedMapOf(
    "im" to NAMESPACE.IM.toString(),
    "sct" to NAMESPACE.SNOMED.toString(),
    "qof" to NAMESPACE.QOF.toString(),
    "smh" to NAMESPACE.SMARTLIFE.toString(),
    "bnf" to NAMESPACE.BNF.toString(),
    "emis" to NAMESPACE.EMIS.toString(),
    "rdfs" to NAMESPACE.RDFS.toString(),
  )

  fun expand(iri: String): String {
    val colon = iri.indexOf(':')
    if (colon <= 0) return iri
    val namespace = prefixes[iri.substring(0, colon)] ?: return iri
    return namespace + iri.substring(colon + 1)
  }

  fun compact(iri: String): String {
    for ((prefix, namespace) in prefixes) {
      if (iri.startsWith(namespace) && iri.length > namespace.length) return "$prefix:${iri.substring(namespace.length)}"
    }
    return iri
  }
}
