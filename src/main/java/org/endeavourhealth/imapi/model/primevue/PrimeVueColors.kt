package org.endeavourhealth.imapi.model.primevue

import com.fasterxml.jackson.annotation.JsonValue

enum class PrimeVueColors(@get:JsonValue val value: String) {
  EMERALD("emerald"),
  GREEN("green"),
  LIME("lime"),
  RED("red"),
  ORANGE("orange"),
  AMBER("amber"),
  YELLOW("yellow"),
  TEAL("teal"),
  CYAN("cyan"),
  SKY("sky"),
  BLUE("blue"),
  INDIGO("indigo"),
  VIOLET("violet"),
  PURPLE("purple"),
  FUCHSIA("fuchsia"),
  PINK("pink"),
  ROSE("rose"),
  SLATE("slate"),
  GRAY("gray"),
  ZINC("zinc"),
  NEUTRAL("neutral"),
  STONE("stone");

  companion object {
    fun fromValue(value: String): PrimeVueColors? = entries.find { it.value.equals(value, ignoreCase = false) }

    fun isColor(value: String): Boolean = entries.any { it.value.equals(value, ignoreCase = false) }
  }
}