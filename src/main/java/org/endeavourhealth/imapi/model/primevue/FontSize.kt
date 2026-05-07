package org.endeavourhealth.imapi.model.primevue

import com.fasterxml.jackson.annotation.JsonValue

enum class FontSize(@get:JsonValue val value: String) {
  SMALL("12px"),
  MEDIUM("14px"),
  LARGE("16px"),
  XL("18px");

  companion object {
    fun fromValue(value: String): FontSize? = entries.find { it.value.equals(value, ignoreCase = false) }

    fun isFontSize(value: String): Boolean = entries.any { it.value.equals(value, ignoreCase = false) }
  }
}