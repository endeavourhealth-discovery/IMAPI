package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.model.security.Action;
import org.endeavourhealth.imapi.model.security.NamespacePermission;
import org.endeavourhealth.imapi.model.security.Resource;
import org.endeavourhealth.imapi.model.security.User;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.endeavourhealth.imapi.security.CasdoorClient;
import org.endeavourhealth.imapi.security.CasdoorUserMapper;
import org.endeavourhealth.imapi.vocabulary.NAMESPACE;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Identity and authorisation, backed directly by Casdoor. Callers are authenticated by a Casdoor access token (see SecurityConfig); the
 * token only proves who is calling. Roles, namespaces and preferences are read from Casdoor (briefly cached), so changes take effect
 * within a minute whatever the token lifetime.
 */
@Component
@Slf4j
public class SecurityService {
  private static final String APPLICATION_TOKEN_TYPE = "application";

  private final Supplier<CasdoorClient> casdoor;

  public SecurityService() {
    this(CasdoorClient::getInstance);
  }

  public SecurityService(Supplier<CasdoorClient> casdoor) {
    this.casdoor = casdoor;
  }

  /** The signed-in user. Calls made with an application (client credentials) token get a user with no roles or namespaces. */
  public User getUser() throws UserNotFoundException {
    Jwt jwt = currentJwt();
    String name = jwt.getClaimAsString("name");
    if (name == null || name.isBlank()) throw new BadCredentialsException("The token does not identify a user");

    if (APPLICATION_TOKEN_TYPE.equals(jwt.getClaimAsString("type"))) return applicationUser(jwt, name);

    String owner = jwt.getClaimAsString("owner");
    String organisation = casdoor.get().settings().organisation();
    if (!organisation.equals(owner)) throw new AccessDeniedException("The token was issued to a user outside organisation " + organisation);
    return CasdoorUserMapper.fromCasdoor(casdoor.get().findUser(name).orElseThrow(() -> new UserNotFoundException("User not found: " + name)));
  }

  /** Another user of the organisation, by username. */
  public User getUserByUsername(String username) throws UserNotFoundException {
    return CasdoorUserMapper.fromCasdoor(casdoor.get().findUser(username).orElseThrow(() -> new UserNotFoundException("User not found: " + username)));
  }

  public String getUserUrl() {
    return casdoor.get().settings().profileUrl();
  }

  /** Saves the signed-in user's preferences (not their roles or namespaces). */
  public User updateUser(User user) throws UserNotFoundException {
    String name = getUser().getUsername();
    ObjectNode casdoorUser = casdoor.get().findUser(name).orElseThrow(() -> new UserNotFoundException("User not found: " + name));
    CasdoorUserMapper.applyPreferences(casdoorUser, user);
    casdoor.get().updateUser(casdoorUser);
    return getUser();
  }

  public boolean userExists(String userId) {
    return casdoor.get().findUserById(userId).isPresent();
  }

  public List<User> adminGetUsersInGroup(UserRole role) {
    return casdoor.get().getUsers().stream()
      .map(CasdoorUserMapper::fromCasdoor)
      .filter(user -> user.getRoles().contains(role))
      .toList();
  }

  public List<UserRole> adminGetGroups() {
    return Arrays.stream(UserRole.values()).toList();
  }

  /** Administrative change to another user's namespaces. Callers must have checked the caller is allowed to do this. */
  public void updateUserNamespaces(String userId, List<NamespacePermission> namespaces) throws UserNotFoundException {
    ObjectNode casdoorUser = casdoor.get().findUserById(userId).orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    CasdoorUserMapper.applyNamespaces(casdoorUser, namespaces);
    casdoor.get().updateUser(casdoorUser);
  }

  /** Requires that the Casdoor enforcer permits the signed-in user to perform `action` on `resource`. */
  public void requiresPermission(Resource resource, Action action) {
    User user = signedInUser();
    if (!casdoor.get().enforce(CasdoorUserMapper.toEnforceSubject(user), resource.name(), action.name()))
      throw new AccessDeniedException("Insufficient authorisation to " + action + " " + resource);
  }

  /** Requires that the signed-in user holds the given access to a namespace. This depends on the data being touched, so it is not part of the enforcer request. */
  public void requiresNamespace(NAMESPACE namespace, boolean read, boolean write) {
    boolean permitted = signedInUser().getNamespaces().stream()
      .anyMatch(held -> held.getIri() == namespace && (!read || held.isRead()) && (!write || held.isWrite()));
    if (!permitted) throw new AccessDeniedException("Insufficient authorisation for namespace " + namespace);
  }

  /** For authorisation checks, which have no checked exceptions: a token for a user Casdoor does not know is simply not authorised. */
  private User signedInUser() {
    try {
      return getUser();
    } catch (UserNotFoundException e) {
      throw new AccessDeniedException(e.getMessage(), e);
    }
  }

  private static Jwt currentJwt() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof JwtAuthenticationToken token) return token.getToken();
    throw new AuthenticationCredentialsNotFoundException("No authenticated user");
  }

  private static User applicationUser(Jwt jwt, String name) {
    User user = new User();
    user.setId(jwt.getSubject() == null ? "" : jwt.getSubject());
    user.setType(APPLICATION_TOKEN_TYPE);
    user.setUsername(name);
    user.setNamespaces(List.of());
    return user;
  }
}
