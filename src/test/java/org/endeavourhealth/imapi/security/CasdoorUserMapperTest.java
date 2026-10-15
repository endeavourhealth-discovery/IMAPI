package org.endeavourhealth.imapi.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.model.primevue.FontSize;
import org.endeavourhealth.imapi.model.primevue.PrimeVueColors;
import org.endeavourhealth.imapi.model.primevue.PrimeVuePresetThemes;
import org.endeavourhealth.imapi.model.security.NamespacePermission;
import org.endeavourhealth.imapi.model.security.User;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.endeavourhealth.imapi.vocabulary.NAMESPACE;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CasdoorUserMapperTest {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private static JsonNode casdoorUser(String json) throws Exception {
    return MAPPER.readTree(json);
  }

  private static final String BASE = """
    {"owner":"Endeavour","name":"jbloggs","id":"1111","type":"normal-user","displayName":"Joe Bloggs",
     "email":"joe@example.com","avatar":"https://example.com/a.png","roles":[{"name":"EXECUTOR"}]%s}""";

  @Test
  void mapsIdentityAndRoles() throws Exception {
    User user = CasdoorUserMapper.fromCasdoor(casdoorUser(BASE.formatted("")));
    assertEquals("1111", user.getId());
    assertEquals("jbloggs", user.getUsername());
    assertEquals("Joe Bloggs", user.getDisplayName());
    assertEquals("joe@example.com", user.getEmail());
    assertEquals(List.of(UserRole.EXECUTOR), user.getRoles());
  }

  @Test
  void ignoresRolesThisApiDoesNotKnow() throws Exception {
    User user = CasdoorUserMapper.fromCasdoor(casdoorUser("""
      {"name":"x","roles":[{"name":"EDITOR"},{"name":"SOMETHING_ELSE"}]}"""));
    assertEquals(List.of(UserRole.EDITOR), user.getRoles());
  }

  @Test
  void appliesDefaultsWhenThereAreNoProperties() throws Exception {
    User user = CasdoorUserMapper.fromCasdoor(casdoorUser(BASE.formatted("")));
    assertFalse(user.getDarkMode());
    assertEquals(PrimeVuePresetThemes.AURA, user.getTheme());
    assertTrue(user.getFavourites().isEmpty());
    assertEquals(List.of(NAMESPACE.IM.toString()), user.getOrganisations());
    assertEquals(1, user.getNamespaces().size());
    assertSame(NAMESPACE.IM, user.getNamespaces().get(0).getIri());
    assertTrue(user.getNamespaces().get(0).isRead());
    assertFalse(user.getNamespaces().get(0).isWrite());
  }

  @Test
  void decodesStringEncodedProperties() throws Exception {
    String properties = """
      ,"properties":{"darkMode":"true","theme":"Lara","primaryColor":"red","fontSize":"16px",
       "favourites":"[\\"a\\",\\"b\\"]",
       "namespaces":"[{\\"iri\\":\\"http://endhealth.info/im#\\",\\"read\\":true,\\"write\\":true}]"}""";
    User user = CasdoorUserMapper.fromCasdoor(casdoorUser(BASE.formatted(properties)));
    assertTrue(user.getDarkMode());
    assertEquals(PrimeVuePresetThemes.LARA, user.getTheme());
    assertEquals(PrimeVueColors.RED, user.getPrimaryColor());
    assertEquals(FontSize.LARGE, user.getFontSize());
    assertEquals(List.of("a", "b"), user.getFavourites());
    assertTrue(user.getNamespaces().get(0).isWrite());
  }

  @Test
  void fallsBackToDefaultsForMalformedProperties() throws Exception {
    User user = CasdoorUserMapper.fromCasdoor(casdoorUser(BASE.formatted(",\"properties\":{\"favourites\":\"{not json\",\"theme\":\"Unknown\"}")));
    assertTrue(user.getFavourites().isEmpty());
    assertEquals(PrimeVuePresetThemes.AURA, user.getTheme());
  }

  @Test
  void resolvesAvatars() throws Exception {
    assertEquals("https://im.endhealth.co.uk/avatars/colour/002-woman.png",
      CasdoorUserMapper.fromCasdoor(casdoorUser("{\"name\":\"x\",\"avatar\":\"colour/002-woman.png\"}")).getAvatar());
    assertEquals("https://im.endhealth.co.uk/avatars/colour/001-man.png",
      CasdoorUserMapper.fromCasdoor(casdoorUser("{\"name\":\"x\"}")).getAvatar());
  }

  @Test
  void writesPreferencesBackWithoutTouchingOtherPropertiesOrNamespaces() throws Exception {
    ObjectNode casdoor = (ObjectNode) casdoorUser(BASE.formatted("""
      ,"properties":{"somethingElse":"keep","namespaces":"[]","organisations":"[]"}"""));
    User user = CasdoorUserMapper.fromCasdoor(casdoor);
    user.setDarkMode(true);
    user.setFavourites(List.of("fav"));
    user.setNamespaces(List.of(new NamespacePermission(NAMESPACE.IM, true, true)));
    user.setOrganisations(List.of("x"));

    CasdoorUserMapper.applyPreferences(casdoor, user);

    JsonNode properties = casdoor.get("properties");
    assertEquals("true", properties.get("darkMode").asText());
    assertEquals("[\"fav\"]", properties.get("favourites").asText());
    assertEquals("keep", properties.get("somethingElse").asText());
    assertEquals("[]", properties.get("namespaces").asText());
    assertEquals("[]", properties.get("organisations").asText());
    assertEquals("Endeavour", casdoor.get("owner").asText());
  }

  @Test
  void createsPropertiesWhenTheUserHasNone() throws Exception {
    ObjectNode casdoor = (ObjectNode) casdoorUser("{\"name\":\"x\",\"properties\":null}");
    CasdoorUserMapper.applyPreferences(casdoor, CasdoorUserMapper.fromCasdoor(casdoor));
    assertEquals("false", casdoor.get("properties").get("darkMode").asText());
  }

  @Test
  void writesNamespacesOnlyWhenAsked() throws Exception {
    ObjectNode casdoor = (ObjectNode) casdoorUser(BASE.formatted(""));
    CasdoorUserMapper.applyNamespaces(casdoor, List.of(new NamespacePermission(NAMESPACE.IM, true, true)));
    JsonNode written = MAPPER.readTree(casdoor.get("properties").get("namespaces").asText());
    assertEquals("http://endhealth.info/im#", written.get(0).get("iri").asText());
    assertTrue(written.get(0).get("write").asBoolean());
  }

  @Test
  void enforceSubjectHasCapitalisedKeysAndNoPassword() throws Exception {
    User user = CasdoorUserMapper.fromCasdoor(casdoorUser(BASE.formatted("")));
    Map<String, Object> subject = CasdoorUserMapper.toEnforceSubject(user);

    assertEquals("jbloggs", subject.get("Username"));
    assertEquals(List.of("EXECUTOR"), subject.get("Roles"));
    assertFalse(subject.containsKey("Password"));
    assertFalse(subject.containsKey("password"));
    assertTrue(subject.keySet().stream().allMatch(key -> Character.isUpperCase(key.charAt(0))), "all top-level keys must be exported Go field names");
  }
}
