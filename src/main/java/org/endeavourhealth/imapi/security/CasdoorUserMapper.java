package org.endeavourhealth.imapi.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto;
import org.endeavourhealth.imapi.model.primevue.FontSize;
import org.endeavourhealth.imapi.model.primevue.PrimeVueColors;
import org.endeavourhealth.imapi.model.primevue.PrimeVuePresetThemes;
import org.endeavourhealth.imapi.model.security.NamespacePermission;
import org.endeavourhealth.imapi.model.security.User;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.endeavourhealth.imapi.vocabulary.NAMESPACE;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Maps between a Casdoor user (identity and roles, plus preferences held in the string-valued `properties` map) and IMAPI's {@link User}.
 */
@Slf4j
public final class CasdoorUserMapper {
  private static final String AVATAR_BASE_URL = "https://im.endhealth.co.uk/avatars";
  private static final ObjectMapper MAPPER = new ObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  private CasdoorUserMapper() {
  }

  public static User fromCasdoor(JsonNode casdoorUser) {
    JsonNode properties = casdoorUser.path("properties");
    User user = new User();
    user.setId(text(casdoorUser, "id"));
    user.setType(text(casdoorUser, "type"));
    user.setUsername(text(casdoorUser, "name"));
    user.setEmail(text(casdoorUser, "email"));
    user.setDisplayName(text(casdoorUser, "displayName"));
    user.setAvatar(avatarUrl(text(casdoorUser, "avatar")));
    user.setRoles(roles(casdoorUser.path("roles")));

    user.setTheme(enumProperty(properties, "theme", value -> PrimeVuePresetThemes.Companion.fromValue(value), user.getTheme()));
    user.setPrimaryColor(enumProperty(properties, "primaryColor", value -> PrimeVueColors.Companion.fromValue(value), user.getPrimaryColor()));
    user.setSurfaceColor(enumProperty(properties, "surfaceColor", value -> PrimeVueColors.Companion.fromValue(value), user.getSurfaceColor()));
    user.setFontSize(enumProperty(properties, "fontSize", value -> FontSize.Companion.fromValue(value), user.getFontSize()));
    user.setDarkMode("true".equals(text(properties, "darkMode")));
    user.setFavourites(jsonProperty(properties, "favourites", new TypeReference<List<String>>() {
    }, new ArrayList<>()));
    user.setRecentActivity(jsonProperty(properties, "recentActivity", new TypeReference<List<RecentActivityItemDto>>() {
    }, new ArrayList<>()));
    user.setOrganisations(jsonProperty(properties, "organisations", new TypeReference<List<String>>() {
    }, new ArrayList<>(List.of(NAMESPACE.IM.toString()))));
    user.setNamespaces(jsonProperty(properties, "namespaces", new TypeReference<List<NamespacePermission>>() {
    }, new ArrayList<>(List.of(new NamespacePermission(NAMESPACE.IM, true, false)))));
    return user;
  }

  /**
   * Writes the user's own preferences into the Casdoor user's `properties`, leaving every other property (and field) untouched.
   * Namespaces and organisations are deliberately not written: namespaces drive authorisation, so users must not be able to set their own.
   */
  public static void applyPreferences(ObjectNode casdoorUser, User user) {
    ObjectNode properties = casdoorUser.withObject("/properties");
    properties.put("theme", user.getTheme().getValue());
    properties.put("primaryColor", user.getPrimaryColor().getValue());
    properties.put("surfaceColor", user.getSurfaceColor().getValue());
    properties.put("darkMode", String.valueOf(user.getDarkMode()));
    properties.put("fontSize", user.getFontSize().getValue());
    properties.put("favourites", toJson(user.getFavourites()));
    properties.put("recentActivity", toJson(user.getRecentActivity()));
  }

  /** For administrative changes to another user's namespaces (e.g. approving a namespace request). */
  public static void applyNamespaces(ObjectNode casdoorUser, List<NamespacePermission> namespaces) {
    casdoorUser.withObject("/properties").put("namespaces", toJson(namespaces));
  }

  /**
   * The `sub` of an enforce request: the user with top-level keys capitalised (Casdoor builds a Go struct from it, which only allows
   * exported fields, so a matcher reads `r.sub.Roles`) and without the password.
   */
  public static Map<String, Object> toEnforceSubject(User user) {
    Map<String, Object> asMap = MAPPER.convertValue(user, new TypeReference<LinkedHashMap<String, Object>>() {
    });
    Map<String, Object> subject = new LinkedHashMap<>();
    asMap.forEach((key, value) -> {
      if (!"password".equals(key)) subject.put(Character.toUpperCase(key.charAt(0)) + key.substring(1), value);
    });
    return subject;
  }

  private static List<UserRole> roles(JsonNode rolesNode) {
    List<UserRole> roles = new ArrayList<>();
    for (JsonNode role : rolesNode) {
      String name = text(role, "name");
      try {
        roles.add(UserRole.valueOf(name));
      } catch (IllegalArgumentException e) {
        log.warn("Ignoring unknown Casdoor role [{}]", name);
      }
    }
    return roles;
  }

  private static String avatarUrl(String avatar) {
    if (avatar.isEmpty()) return AVATAR_BASE_URL + "/colour/001-man.png";
    return avatar.startsWith("http") ? avatar : AVATAR_BASE_URL + "/" + avatar;
  }

  private static <T> T enumProperty(JsonNode properties, String key, Function<String, T> fromValue, T defaultValue) {
    String value = text(properties, key);
    if (value.isEmpty()) return defaultValue;
    T parsed = fromValue.apply(value);
    return parsed != null ? parsed : defaultValue;
  }

  private static <T> T jsonProperty(JsonNode properties, String key, TypeReference<T> type, T defaultValue) {
    String value = text(properties, key);
    if (value.isEmpty()) return defaultValue;
    try {
      return MAPPER.readValue(value, type);
    } catch (JsonProcessingException e) {
      log.warn("Ignoring malformed Casdoor user property [{}]", key);
      return defaultValue;
    }
  }

  private static String toJson(Object value) {
    try {
      return MAPPER.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Failed to serialise user property", e);
    }
  }

  private static String text(JsonNode node, String field) {
    JsonNode value = node.path(field);
    return value.isMissingNode() || value.isNull() ? "" : value.asText();
  }
}
