package org.endeavourhealth.imapi.queryassistant

import java.io.File

/** Minimal RFC 4180 reader for the two-column (s, o) query corpus export: quoted fields, doubled quotes, newlines in quotes. */
object CorpusCsv {
  fun read(file: File): List<Pair<String, String>> {
    val text = file.readText()
    val rows = mutableListOf<Pair<String, String>>()
    val fields = mutableListOf<String>()
    val field = StringBuilder()
    var i = 0
    var quoted = false
    fun endField() { fields += field.toString(); field.clear() }
    fun endRow() {
      endField()
      if (fields.size >= 2) rows += fields[0] to fields[1]
      fields.clear()
    }
    while (i < text.length) {
      val c = text[i]
      when {
        quoted && c == '"' && i + 1 < text.length && text[i + 1] == '"' -> { field.append('"'); i++ }
        c == '"' -> quoted = !quoted
        quoted -> field.append(c)
        c == ',' -> endField()
        c == '\n' -> endRow()
        c == '\r' -> {}
        else -> field.append(c)
      }
      i++
    }
    if (field.isNotEmpty() || fields.isNotEmpty()) endRow()
    return rows
  }
}
