package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.model.security.Action;
import org.endeavourhealth.imapi.model.security.NamespacePermission;
import org.endeavourhealth.imapi.model.security.Resource;
import org.endeavourhealth.imapi.model.security.User;
import org.endeavourhealth.imapi.security.CasdoorClient;
import org.endeavourhealth.imapi.security.CasdoorSettings;
import org.endeavourhealth.imapi.vocabulary.NAMESPACE;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SecurityServiceTest {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private final CasdoorClient casdoor = mock(CasdoorClient.class);
  private final SecurityService service = new SecurityService(() -> casdoor);

  @BeforeEach
  void setUp() {
    when(casdoor.settings()).thenReturn(new CasdoorSettings("https://auth", "https://auth", "Endeavour", "id", "secret", "Endeavour/TestEnforcer", List.of("client"), "RS256"));
  }

  @AfterEach
  void clearContext() {
    SecurityContextHolder.clearContext();
  }

  private static void signInAs(Map<String, Object> claims) {
    Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "RS256"), claims);
    SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));
  }

  private void signInAsUser(String casdoorUserJson) throws Exception {
    signInAs(Map.of("name", "jbloggs", "owner", "Endeavour", "sub", "1111"));
    when(casdoor.findUser("jbloggs")).thenAnswer(invocation -> Optional.of((ObjectNode) MAPPER.readTree(casdoorUserJson)));
  }

  private static final String EXECUTOR = """
    {"owner":"Endeavour","name":"jbloggs","id":"1111","roles":[{"name":"EXECUTOR"}],
     "properties":{"namespaces":"[{\\"iri\\":\\"http://endhealth.info/im#\\",\\"read\\":true,\\"write\\":false}]"}}""";

  @Test
  void theSignedInUserIsReadFromCasdoorNotTheToken() throws Exception {
    signInAsUser(EXECUTOR);
    User user = service.getUser();
    assertEquals("jbloggs", user.getUsername());
    assertEquals(1, user.getRoles().size());
  }

  @Test
  void noAuthenticationIsRejected() {
    assertThrows(AuthenticationCredentialsNotFoundException.class, service::getUser);
  }

  @Test
  void aTokenForAUserOutsideTheOrganisationIsRejected() {
    signInAs(Map.of("name", "jbloggs", "owner", "SomeoneElse"));
    assertThrows(AccessDeniedException.class, service::getUser);
  }

  @Test
  void aUserCasdoorDoesNotKnowIsNotFound() {
    signInAs(Map.of("name", "ghost", "owner", "Endeavour"));
    when(casdoor.findUser("ghost")).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, service::getUser);
  }

  @Test
  void anApplicationTokenGetsAUserWithNoRolesOrNamespacesAndNeverTouchesCasdoorUsers() throws Exception {
    signInAs(Map.of("name", "IMQueryRunner", "owner", "admin", "type", "application", "sub", "app-id"));
    User user = service.getUser();
    assertEquals("IMQueryRunner", user.getUsername());
    assertTrue(user.getRoles().isEmpty());
    assertTrue(user.getNamespaces().isEmpty());
    verify(casdoor, never()).findUser(any());
  }

  @Test
  void permissionIsDecidedByTheEnforcerFromTheUserTheResourceAndTheAction() throws Exception {
    signInAsUser(EXECUTOR);
    when(casdoor.enforce(any(), eq("SET"), eq("PUBLISH"))).thenReturn(true);

    assertDoesNotThrow(() -> service.requiresPermission(Resource.SET, Action.PUBLISH));

    ArgumentCaptor<Object> subject = ArgumentCaptor.forClass(Object.class);
    verify(casdoor).enforce(subject.capture(), eq("SET"), eq("PUBLISH"));
    @SuppressWarnings("unchecked") Map<String, Object> sub = (Map<String, Object>) subject.getValue();
    assertEquals("jbloggs", sub.get("Username"));
    assertEquals(List.of("EXECUTOR"), sub.get("Roles"));
    assertFalse(sub.containsKey("Password"));
  }

  @Test
  void aDeniedPermissionIsAccessDenied() throws Exception {
    signInAsUser(EXECUTOR);
    when(casdoor.enforce(any(), any(), any())).thenReturn(false);
    assertThrows(AccessDeniedException.class, () -> service.requiresPermission(Resource.SET, Action.PUBLISH));
  }

  @Test
  void aTokenForAnUnknownUserIsDeniedRatherThanBlowingUp() {
    signInAs(Map.of("name", "ghost", "owner", "Endeavour"));
    when(casdoor.findUser("ghost")).thenReturn(Optional.empty());
    assertThrows(AccessDeniedException.class, () -> service.requiresPermission(Resource.SET, Action.PUBLISH));
    verify(casdoor, never()).enforce(any(), any(), any());
  }

  @Test
  void namespaceAccessRequiresTheMatchingReadAndWriteFlags() throws Exception {
    signInAsUser(EXECUTOR);
    assertDoesNotThrow(() -> service.requiresNamespace(NAMESPACE.IM, true, false));
    assertThrows(AccessDeniedException.class, () -> service.requiresNamespace(NAMESPACE.IM, true, true));
    assertThrows(AccessDeniedException.class, () -> service.requiresNamespace(NAMESPACE.QOF, true, false));
  }

  @Test
  void updatingAUserSavesPreferencesButNotNamespaces() throws Exception {
    signInAsUser(EXECUTOR);
    User user = service.getUser();
    user.setDarkMode(true);
    user.setNamespaces(List.of(new NamespacePermission(NAMESPACE.IM, true, true)));

    service.updateUser(user);

    ArgumentCaptor<ObjectNode> saved = ArgumentCaptor.forClass(ObjectNode.class);
    verify(casdoor).updateUser(saved.capture());
    assertEquals("true", saved.getValue().get("properties").get("darkMode").asText());
    assertTrue(saved.getValue().get("properties").get("namespaces").asText().contains("\"write\":false"), "namespaces are not user-writable");
  }

  @Test
  void anAdministratorCanSetAnotherUsersNamespaces() throws Exception {
    when(casdoor.findUserById("2222")).thenReturn(Optional.of((ObjectNode) MAPPER.readTree("{\"owner\":\"Endeavour\",\"name\":\"other\",\"id\":\"2222\"}")));

    service.updateUserNamespaces("2222", List.of(new NamespacePermission(NAMESPACE.IM, true, true)));

    ArgumentCaptor<ObjectNode> saved = ArgumentCaptor.forClass(ObjectNode.class);
    verify(casdoor).updateUser(saved.capture());
    assertTrue(saved.getValue().get("properties").get("namespaces").asText().contains("\"write\":true"));
  }

  @Test
  void usersInAGroupAreThoseWithTheRole() throws Exception {
    when(casdoor.getUsers()).thenReturn(List.of(
      (ObjectNode) MAPPER.readTree("{\"name\":\"a\",\"roles\":[{\"name\":\"EDITOR\"}]}"),
      (ObjectNode) MAPPER.readTree("{\"name\":\"b\",\"roles\":[{\"name\":\"CREATOR\"}]}")));

    List<User> editors = service.adminGetUsersInGroup(org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole.EDITOR);

    assertEquals(List.of("a"), editors.stream().map(User::getUsername).toList());
  }
}
