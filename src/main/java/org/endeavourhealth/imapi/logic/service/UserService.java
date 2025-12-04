package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.UserRepository;
import org.endeavourhealth.imapi.model.casdoor.UserSettings;
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.USER;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

@Component
public class UserService {
  private final UserRepository userRepository = new UserRepository();
  private final EntityService entityService = new EntityService();

  public String getUserPreset(String userId) {
    return userRepository.getByPredicate(userId, USER.USER_PRESET);
  }

  public void updateUserPreset(String userId, String preset) throws JsonProcessingException {
    userRepository.updateByPredicate(userId, preset, USER.USER_PRESET);
  }

  public String getUserPrimaryColor(String userId) {
    return userRepository.getByPredicate(userId, USER.USER_PRIMARY_COLOR);
  }

  public void updateUserPrimaryColor(String userId, String color) throws JsonProcessingException {
    userRepository.updateByPredicate(userId, color, USER.USER_PRIMARY_COLOR);
  }

  public String getUserSurfaceColor(String userId) {
    return userRepository.getByPredicate(userId, USER.USER_SURFACE_COLOR);
  }

  public void updateUserSurfaceColor(String userId, String color) throws JsonProcessingException {
    userRepository.updateByPredicate(userId, color, USER.USER_SURFACE_COLOR);
  }

  public Boolean getUserDarkMode(String userId) {
    return userRepository.getByPredicate(userId, USER.USER_DARK_MODE).equals("\"true\"");
  }

  public void updateUserDarkMode(String userId, Boolean darkMode) throws JsonProcessingException {
    userRepository.updateByPredicate(userId, darkMode, USER.USER_DARK_MODE);
  }

  public String getUserScale(String userId) {
    return userRepository.getByPredicate(userId, USER.USER_SCALE);
  }

  public void updateUserScale(String userId, String scale) throws JsonProcessingException {
    userRepository.updateByPredicate(userId, scale, USER.USER_SCALE);
  }

  public List<RecentActivityItemDto> getUserMRU(String userId) throws JsonProcessingException {
    List<RecentActivityItemDto> mru = userRepository.getUserMRU(userId);
    boolean hasNoneExistingIris = mru.stream()
      .anyMatch(mruDto -> !entityService.iriExists(mruDto.getIri()));
    if (hasNoneExistingIris) {
      List<RecentActivityItemDto> updatedMRUs = mru.stream()
        .filter(mruDto -> entityService.iriExists(mruDto.getIri())).toList();
      updateUserMRU(userId, updatedMRUs);
      return userRepository.getUserMRU(userId);
    }
    return mru;
  }

  public void updateUserMRU(String userId, List<RecentActivityItemDto> mru) throws JsonProcessingException {
    userRepository.updateUserMRU(userId, mru);
  }

  public List<String> getUserFavourites(String userId) throws JsonProcessingException {
    List<String> favourites = userRepository.getUserFavourites(userId);
    boolean hasNoneExistingIris = favourites.stream()
      .anyMatch(favouriteIri -> !entityService.iriExists(favouriteIri));
    if (hasNoneExistingIris) {
      List<String> updatedFavourites = favourites.stream()
        .filter(entityService::iriExists).toList();
      updateUserFavourites(userId, updatedFavourites);
      return userRepository.getUserFavourites(userId);
    }
    return favourites;
  }

  public void updateUserFavourites(String userId, List<String> favourites) throws JsonProcessingException {
    userRepository.updateUserFavourites(userId, favourites);
  }

  public List<String> getUserOrganisations(String userId) throws JsonProcessingException {
    return userRepository.getUserOrganisations(userId);
  }

  public void updateUserOrganisations(String userId, List<String> organisations) throws JsonProcessingException {
    userRepository.updateUserOrganisations(userId, organisations);
  }

  public void updateUserGraphs(String userId, List<Graph> userGraphs) throws JsonProcessingException {
    userRepository.updateUserGraphs(userId, userGraphs);
  }

  public boolean userIdExists(String userId) {
    return userRepository.getUserIdExists(userId);
  }

  public boolean getEditAccess(String userId, String entityIri) throws JsonProcessingException {
    List<String> organisations = this.getUserOrganisations(userId);
    Set<String> predicates = asHashSet(IM.HAS_SCHEME);
    TTEntity entity = entityService.getBundle(entityIri, predicates).getEntity();
    if (null == entity.getScheme()) return false;
    return organisations.contains(entity.getScheme().getIri());
  }

  public UserSettings getUserSettings(String userId) throws JsonProcessingException {
    UserSettings userData = new UserSettings();
    String preset = getUserPreset(userId).replace("\"", "");
    if (!preset.isEmpty()) userData.setPreset(preset);
    String primaryColor = getUserPrimaryColor(userId).replace("\"", "");
    if (!primaryColor.isEmpty()) userData.setPrimaryColor(primaryColor);
    String surfaceColor = getUserSurfaceColor(userId).replace("\"", "");
    if (!surfaceColor.isEmpty()) userData.setSurfaceColor(surfaceColor);
    userData.setDarkMode(getUserDarkMode(userId));
    String fontSize = getUserScale(userId).replace("\"", "").replace("\\\\", "");
    if (!fontSize.isEmpty()) userData.setScale(fontSize);
    userData.setOrganisations(getUserOrganisations(userId));
    userData.setFavourites(getUserFavourites(userId));
    userData.setMru(getUserMRU(userId));
    return userData;
  }

  public List<Graph> getUserGraphs(String userId) throws JsonProcessingException {
    try {
      return userRepository.getUserGraphs(userId);
    } catch (IllegalArgumentException e) {
      return List.of(Graph.IM);
    }
  }
}
