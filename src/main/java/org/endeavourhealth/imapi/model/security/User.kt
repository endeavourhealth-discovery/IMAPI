package org.endeavourhealth.imapi.model.security

import com.fasterxml.jackson.annotation.JsonIgnore
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole
import org.endeavourhealth.interfacemanager.model.FontSize
import org.endeavourhealth.interfacemanager.model.PrimeVueColors
import org.endeavourhealth.interfacemanager.model.PrimeVuePresetThemes

class User {
  var id: String = ""
  var username: String = ""
  var email: String = ""
  var displayName: String = ""
  var password: String = ""
    set(value) {
      field = ""
    }
  var avatar: String = ""
  var roles: List<UserRole> = mutableListOf()
  var organisations: List<String> = mutableListOf()
  var theme: PrimeVuePresetThemes = PrimeVuePresetThemes.AURA
  var primaryColor: PrimeVueColors = PrimeVueColors.EMERALD
  var surfaceColor: PrimeVueColors = PrimeVueColors.SLATE
  var darkMode: Boolean = false
  var fontSize: FontSize = FontSize.MEDIUM
  var favourites: List<String> = mutableListOf()
  var recentActivity: List<RecentActivityItemDto> = mutableListOf()
  var namespaces: List<NamespacePermission> = mutableListOf()

  @JsonIgnore
  fun adminSetPassword(password: String) {
    this.password = password
  }
}