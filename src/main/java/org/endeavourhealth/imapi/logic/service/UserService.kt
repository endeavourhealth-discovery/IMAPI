package org.endeavourhealth.imapi.logic.service

import org.endeavourhealth.imapi.dataaccess.UserRepository
import org.endeavourhealth.imapi.model.casdoor.UserSettings
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto
import org.endeavourhealth.imapi.model.primevue.FontSize
import org.endeavourhealth.imapi.model.primevue.PrimeVueColors
import org.endeavourhealth.imapi.model.primevue.PrimeVuePresetThemes
import org.endeavourhealth.imapi.model.tripletree.TTEntity
import org.endeavourhealth.imapi.vocabulary.Graph
import org.endeavourhealth.imapi.vocabulary.IM
import org.endeavourhealth.imapi.vocabulary.USER
import org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet
import org.springframework.stereotype.Component

@Component
class UserService {
  private val userRepository = UserRepository()
  private val entityService = EntityService()

  fun getUserPreset(userId: String): String {
    return userRepository.getByPredicate(userId, USER.USER_PRESET)
  }

  fun updateUserPreset(userId: String, preset: String): Unit {
    require(PrimeVuePresetThemes.isTheme(preset))
    userRepository.updateByPredicate(userId, preset, USER.USER_PRESET)
  }

  fun getUserPrimaryColor(userId: String): String {
    return userRepository.getByPredicate(userId, USER.USER_PRIMARY_COLOR)
  }

  fun updateUserPrimaryColor(userId: String, color: String) {
    require(PrimeVueColors.isColor(color))
    userRepository.updateByPredicate(userId, color, USER.USER_PRIMARY_COLOR)
  }

  fun getUserSurfaceColor(userId: String): String {
    return userRepository.getByPredicate(userId, USER.USER_SURFACE_COLOR)
  }

  fun updateUserSurfaceColor(userId: String, color: String) {
    require(PrimeVueColors.isColor(color))
    userRepository.updateByPredicate(userId, color, USER.USER_SURFACE_COLOR)
  }

  fun getUserDarkMode(userId: String): Boolean {
    return userRepository.getByPredicate(userId, USER.USER_DARK_MODE) == "\"true\""
  }

  fun updateUserDarkMode(userId: String, darkMode: Boolean) {
    userRepository.updateByPredicate(userId, darkMode, USER.USER_DARK_MODE)
  }

  fun getUserFontSize(userId: String): String {
    return userRepository.getByPredicate(userId, USER.USER_FONT_SIZE)
  }

  fun updateUserFontSize(userId: String, fontSize: String) {
    require(FontSize.isFontSize(fontSize))
    userRepository.updateByPredicate(userId, fontSize, USER.USER_FONT_SIZE)
  }

  fun getUserMRUs(userId: String): List<RecentActivityItemDto> {
    val mru = userRepository.getUserMRU(userId)
    val hasNoneExistingIris = mru.any { !entityService.iriExists(it.iri) }
    if (hasNoneExistingIris) {
      val updatedMRUs = mru.filter { entityService.iriExists(it.iri) }
      updateUserMRU(userId, updatedMRUs)
      return userRepository.getUserMRU(userId)
    }
    return mru
  }

  fun updateUserMRU(userId: String, mru: List<RecentActivityItemDto>) {
    userRepository.updateUserMRU(userId, mru)
  }

  fun getUserFavourites(userId: String): List<String> {
    val favourites = userRepository.getUserFavourites(userId)
    val hasNoneExistingIris = favourites.any { !entityService.iriExists(it) }
    if (hasNoneExistingIris) {
      val updatedFavourites = favourites.filter { entityService.iriExists(it) }
      updateUserFavourites(userId, updatedFavourites)
      return userRepository.getUserFavourites(userId)
    }
    return favourites
  }

  fun updateUserFavourites(userId: String, favourites: List<String>) {
    userRepository.updateUserFavourites(userId, favourites)
  }

  fun getUserOrganisations(userId: String): List<String> {
    return userRepository.getUserOrganisations(userId)
  }

  fun updateUserOrganisations(userId: String, organisations: List<String>) {
    userRepository.updateUserOrganisations(userId, organisations)
  }

  fun updateUserGraphs(userId: String, graphs: List<Graph>) {
    userRepository.updateUserGraphs(userId, graphs)
  }

  fun userIdExists(userId: String): Boolean {
    return userRepository.getUserIdExists(userId)
  }

  fun getEditAccess(userId: String, entityIri: String): Boolean {
    val organisations: List<String> = getUserOrganisations(userId)
    val predicates: Set<String> = asHashSet(IM.HAS_SCHEME)
    val entity: TTEntity = entityService.getBundle(entityIri, predicates).entity
    return organisations.contains(entity.scheme?.iri)
  }

  fun getUserSettings(userId: String): UserSettings {
    val userSettings = UserSettings()
    val preset = getUserPreset(userId).replace("\"", "")
    if (preset.isNotEmpty()) userSettings.setPreset(preset)
    val primaryColor = getUserPrimaryColor(userId).replace("\"", "")
    if (primaryColor.isNotEmpty()) userSettings.setPrimaryColor(primaryColor)
    val surfaceColor = getUserSurfaceColor(userId).replace("\"", "")
    if (surfaceColor.isNotEmpty()) userSettings.setSurfaceColor(surfaceColor)
    userSettings.darkMode = getUserDarkMode(userId)
    val fontSize = getUserFontSize(userId).replace("\"", "").replace("\\\\", "")
    if (fontSize.isNotEmpty()) userSettings.setFontSize(fontSize)
    userSettings.organisations = getUserOrganisations(userId).toMutableList()
    userSettings.favourites = getUserFavourites(userId).toMutableList()
    userSettings.mru = getUserMRUs(userId).toMutableList()
    return userSettings
  }

  fun getUserGraphs(userId: String): List<Graph> {
    return try {
      userRepository.getUserGraphs(userId)
    } catch (e: IllegalArgumentException) {
      listOf(Graph.IM)
    }
  }
}