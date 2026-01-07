package org.endeavourhealth.imapi.logic.service

import org.casbin.casdoor.entity.User
import org.endeavourhealth.imapi.dataaccess.UserRepository
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto
import org.endeavourhealth.imapi.model.primevue.FontSize
import org.endeavourhealth.imapi.model.primevue.PrimeVueColors
import org.endeavourhealth.imapi.model.primevue.PrimeVuePresetThemes
import org.endeavourhealth.imapi.model.tripletree.TTEntity
import org.endeavourhealth.imapi.vocabulary.Graph
import org.endeavourhealth.imapi.vocabulary.IM
import org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet
import org.springframework.stereotype.Component

@Component
class UserService {
  private val userRepository = UserRepository()
  private val entityService = EntityService()
  private val casdoorService = CasdoorService()

  fun updateUserTheme(user: User, theme: String): Unit {
    require(PrimeVuePresetThemes.isTheme(theme))
    user.properties["theme"] = theme
    casdoorService.updateUser(user)
  }

  fun updateUserPrimaryColor(user: User, color: String) {
    require(PrimeVueColors.isColor(color))
    user.properties["primaryColor"] = color
    casdoorService.updateUser(user)
  }

  fun updateUserSurfaceColor(user: User, color: String) {
    require(PrimeVueColors.isColor(color))
    user.properties["surfaceColor"] = color
    casdoorService.updateUser(user)
  }

  fun updateUserDarkMode(user: User, darkMode: Boolean) {
    user.properties["darkMode"] = darkMode.toString()
    casdoorService.updateUser(user)
  }

  fun updateUserFontSize(user: User, fontSize: String) {
    require(FontSize.isFontSize(fontSize))
    user.properties["fontSize"] = fontSize
    casdoorService.updateUser(user)
  }

  fun updateUserRecentActivity(user: User, recentActivity: List<RecentActivityItemDto>) {
    if (recentActivity.isEmpty()) {
      user.properties["recentActivity"] = listOf<RecentActivityItemDto>().joinToString()
      casdoorService.updateUser(user)
      return
    }
    if (recentActivity.all { isValidRecentActivityItem(it) }) {
      user.properties["recentActivity"] = recentActivity.joinToString(",")
      casdoorService.updateUser(user)
    } else {
      throw IllegalArgumentException("One or more activity items are invalid")
    }
  }

  private fun isValidRecentActivityItem(item: RecentActivityItemDto): Boolean {
    return item.iri.isNotEmpty() && item.action.isNotEmpty() && item.dateTime != null
  }

  fun updateUserFavourites(user: User, favourites: List<String>) {
    user.properties["favourites"] = favourites.joinToString(",")
    casdoorService.updateUser(user)
  }

  fun updateUserOrganisations(userId: String, organisations: List<String>) {
    val user = casdoorService.adminGetCasdoorUser(userId)
    user.properties["organisations"] = organisations.joinToString(",")
    casdoorService.updateUser(user)
  }

  fun userIdExists(userId: String): Boolean {
    return userRepository.getUserIdExists(userId)
  }

  fun getEditAccess(user: org.endeavourhealth.imapi.model.casdoor.User, entityIri: String): Boolean {
    val predicates: Set<String> = asHashSet(IM.HAS_SCHEME)
    val entity: TTEntity = entityService.getBundle(entityIri, predicates).entity
    return user.organisations.contains(entity.scheme?.iri)
  }

  fun getUserGraphs(userId: String): List<Graph> {
    return try {
      userRepository.getUserGraphs(userId)
    } catch (e: IllegalArgumentException) {
      listOf(Graph.IM)
    }
  }
}