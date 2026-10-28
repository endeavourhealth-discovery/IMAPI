package org.endeavourhealth.imapi.logic.service

import com.fasterxml.jackson.databind.node.ObjectNode
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException
import org.endeavourhealth.imapi.model.security.Action
import org.endeavourhealth.imapi.model.security.NamespacePermission
import org.endeavourhealth.imapi.model.security.Resource
import org.endeavourhealth.imapi.model.security.User
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole
import org.endeavourhealth.imapi.security.CasdoorClient
import org.endeavourhealth.imapi.security.CasdoorUserMapper
import org.endeavourhealth.imapi.vocabulary.NAMESPACE
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * Identity and authorisation, backed directly by Casdoor. Callers are authenticated by a Casdoor access token (see SecurityConfig); the
 * token only proves who is calling. Roles, namespaces and preferences are read from Casdoor (briefly cached), so changes take effect
 * within a minute whatever the token lifetime.
 */
@Component
class SecurityService(private val casdoor: Supplier<CasdoorClient> = Supplier { CasdoorClient.getInstance() }) {
  val user: User
    /** The signed-in user. Calls made with an application (client credentials) token get a user with no roles or namespaces.  */
    @Throws(UserNotFoundException::class)
    get() {
      val jwt: Jwt = currentJwt()
      val name = jwt.getClaimAsString("name")
      if (name == null || name.isBlank()) throw BadCredentialsException("The token does not identify a user")

      if (APPLICATION_TOKEN_TYPE == jwt.getClaimAsString("type")) return applicationUser(jwt, name)

      val owner = jwt.getClaimAsString("owner")
      val organisation = casdoor.get().settings().organisation
      if (organisation != owner) {
        SecurityContextHolder.clearContext()
        throw AccessDeniedException("The token was issued to a user outside organisation $organisation")
      }
      return CasdoorUserMapper.fromCasdoor(
        casdoor.get().findUser(name).orElseThrow(Supplier { UserNotFoundException("User not found: $name") })
      )
    }

  /** Another user of the organisation, by username.  */
  @Throws(UserNotFoundException::class)
  fun getUserByUsername(username: String): User {
    return CasdoorUserMapper.fromCasdoor(
      casdoor.get().findUser(username).orElseThrow(Supplier { UserNotFoundException("User not found: $username") })
    )
  }

  val userUrl: String
    get() = casdoor.get().settings().profileUrl()

  /** Saves the signed-in user's preferences (not their roles or namespaces).  */
  @Throws(UserNotFoundException::class)
  fun updateUser(user: User): User {
    val name = this.user.username
    val casdoorUser =
      casdoor.get().findUser(name).orElseThrow(Supplier { UserNotFoundException("User not found: $name") })
    CasdoorUserMapper.applyPreferences(casdoorUser, user)
    casdoor.get().updateUser(casdoorUser)
    return this.user
  }

  fun userExists(userId: String): Boolean {
    return casdoor.get().findUserById(userId).isPresent
  }

  fun adminGetUsersInGroup(role: UserRole): MutableList<User> {
    return casdoor.get().getUsers()
      .map { casdoorUser: ObjectNode -> CasdoorUserMapper.fromCasdoor(casdoorUser) }
      .filter { user: User -> user.roles.contains(role) }
      .toMutableList()
  }

  fun adminGetGroups(): MutableList<UserRole> {
    return UserRole.entries.toMutableList()
  }

  /** Administrative change to another user's namespaces. Callers must have checked the caller is allowed to do this.  */
  @Throws(UserNotFoundException::class)
  fun updateUserNamespaces(userId: String, namespaces: MutableList<NamespacePermission>?) {
    val casdoorUser =
      casdoor.get().findUserById(userId).orElseThrow(Supplier { UserNotFoundException("User not found: $userId") })
    CasdoorUserMapper.applyNamespaces(casdoorUser, namespaces)
    casdoor.get().updateUser(casdoorUser)
  }

  /** Requires that the Casdoor enforcer permits the signed-in user to perform `action` on `resource`.  */
  fun requiresPermission(resource: Resource, action: Action) {
    val user = signedInUser()
    if (!casdoor.get()
        .enforce(CasdoorUserMapper.toEnforceSubject(user), resource.name, action.name)
    ) throw AccessDeniedException("Insufficient authorisation to $action $resource")
  }

  /** Requires that the signed-in user holds the given access to a namespace. This depends on the data being touched, so it is not part of the enforcer request.  */
  fun requiresNamespace(namespace: NAMESPACE, read: Boolean, write: Boolean) {
    val permitted =
      signedInUser().namespaces.any { held: NamespacePermission -> held.iri == namespace && (!read || held.read) && (!write || held.write) }
    if (!permitted) throw AccessDeniedException("Insufficient authorisation for namespace $namespace")
  }

  /** For authorisation checks, which have no checked exceptions: a token for a user Casdoor does not know is simply not authorised.  */
  private fun signedInUser(): User {
    try {
      return this.user
    } catch (e: UserNotFoundException) {
      throw AccessDeniedException(e.message, e)
    }
  }

  companion object {
    private const val APPLICATION_TOKEN_TYPE = "application"

    private fun currentJwt(): Jwt {
      val authentication = SecurityContextHolder.getContext().authentication
      if (authentication is JwtAuthenticationToken) return authentication.token
      throw AuthenticationCredentialsNotFoundException("No authenticated user")
    }

    private fun applicationUser(jwt: Jwt, name: String): User {
      val user = User()
      user.id = jwt.subject ?: ""
      user.type = APPLICATION_TOKEN_TYPE
      user.username = name
      user.namespaces = mutableListOf()
      return user
    }
  }
}
