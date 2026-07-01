package org.endeavourhealth.imapi.model.primevue

import com.fasterxml.jackson.annotation.JsonValue

enum class PrimeVuePresetThemes(@get:JsonValue val value: String) {
  AURA("Aura"),
  LARA("Lara"),
  NORA("Nora"),
  MATERIAL("Material");

  companion object {
    fun fromValue(value: String): PrimeVuePresetThemes? = entries.find { it.value.equals(value, ignoreCase = false) }

    fun isTheme(value: String): Boolean = entries.any { it.value.equals(value, ignoreCase = false) }
  }
}