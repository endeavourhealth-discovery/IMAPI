package org.endeavourhealth.imapi.model.extensions

import org.endeavourhealth.interfacemanager.model.IMLLanguage

fun IMLLanguage.initialize() = apply {
  keywords?.addAll(
    mutableSetOf<String?>(
      "define",
      "assign",
      "match",
      "as",
      "from",
      "where",
      "if",
      "prefix",
      "info",
      "default"
    )
  )
  booleans?.addAll(mutableSetOf<String?>("either", "and", "or"))
  alerts?.addAll(mutableSetOf<String?>("exclude", "warning"))
}