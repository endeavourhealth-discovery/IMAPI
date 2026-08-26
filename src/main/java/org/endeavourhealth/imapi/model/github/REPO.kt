package org.endeavourhealth.imapi.model.github

import com.fasterxml.jackson.annotation.JsonValue

enum class REPO(val value: String) {
  IM_DIRECTORY("IMDirectory"),
  IM_QUERY_RUNNER("IMQueryRunner");

  @JsonValue
  override fun toString(): String {
    return value
  }

  companion object {
    @JvmStatic
    fun fromValue(value: String): REPO =
      entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException("Unknown repo $value")
  }
}
