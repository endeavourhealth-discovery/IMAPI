package org.endeavourhealth.imapi.model.casdoor

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSetter
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto
import org.endeavourhealth.imapi.model.primevue.FontSize
import org.endeavourhealth.imapi.model.primevue.PrimeVueColors
import org.endeavourhealth.imapi.model.primevue.PrimeVuePresetThemes

class UserSettings {
  @JsonIgnore
  var preset: PrimeVuePresetThemes = PrimeVuePresetThemes.AURA

  @JsonSetter("preset")
  fun setPreset(preset: String) {
    this.preset = PrimeVuePresetThemes.valueOf(preset)
  }

  @JsonGetter("preset")
  fun getPreset(): String = preset.value

  @JsonIgnore
  var primaryColor: PrimeVueColors = PrimeVueColors.EMERALD

  @JsonSetter("primaryColor")
  fun setPrimaryColor(primaryColor: String) {
    this.primaryColor =
      PrimeVueColors.fromValue(primaryColor) ?: throw IllegalArgumentException("Invalid color: $primaryColor")
  }

  @JsonGetter("primaryColor")
  fun getPrimaryColor(): String = primaryColor.value

  @JsonIgnore
  var surfaceColor: PrimeVueColors = PrimeVueColors.SLATE

  @JsonSetter("surfaceColor")
  fun setSurfaceColor(surfaceColor: String) {
    this.surfaceColor =
      PrimeVueColors.fromValue(surfaceColor) ?: throw IllegalArgumentException("Invalid color: $surfaceColor")
  }

  @JsonGetter("surfaceColor")
  fun getSurfaceColor(): String = surfaceColor.value

  var darkMode: Boolean = false

  @JsonIgnore
  var scale: FontSize = FontSize.LARGE

  @JsonSetter("scale")
  fun setScale(scale: String) {
    this.scale = FontSize.fromValue(scale) ?: throw IllegalArgumentException("Invalid scale: $scale")
  }

  @JsonGetter("scale")
  fun getScale(): String = scale.value

  var organisations: MutableList<String> = mutableListOf()
  var favourites: MutableList<String> = mutableListOf()
  var mru: MutableList<RecentActivityItemDto> = mutableListOf()
}