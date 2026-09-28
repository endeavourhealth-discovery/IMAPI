package org.endeavourhealth.imapi.logic.service

import com.fasterxml.jackson.core.JsonProcessingException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.http.HttpException
import org.eclipse.rdf4j.http.protocol.UnauthorizedException
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException
import org.endeavourhealth.imapi.model.responses.LoginResponse
import org.endeavourhealth.imapi.model.security.NamespacePermission
import org.endeavourhealth.imapi.model.security.Permission
import org.endeavourhealth.imapi.model.security.User
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole
import org.endeavourhealth.imapi.utility.IpExtractor
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class SecurityService {
  private val endeavourSecurityService = EndeavourSecurityService()

  @Throws(UserNotFoundException::class, JsonProcessingException::class)
  fun getUser(request: HttpServletRequest): User {
    val sessionId = getSessionId(request)
    val ipAddress = IpExtractor.getIpAddress(request)
    return endeavourSecurityService.getUser(ipAddress, sessionId)
  }

  fun getUserUrl(request: HttpServletRequest): String {
    val sessionId = getSessionId(request)
    val ipAddress = IpExtractor.getIpAddress(request)
    return endeavourSecurityService.getProfileUrl(ipAddress, sessionId)
  }

  @Throws(UserNotFoundException::class, IOException::class)
  fun updateUser(request: HttpServletRequest, user: User): User {
    val sessionId = getSessionId(request)
    val ipAddress = IpExtractor.getIpAddress(request)
    return endeavourSecurityService.updateUser(ipAddress, sessionId, user)
  }

  @Throws(UserAuthorisationException::class)
  fun loginUser(
    code: String,
    state: String,
    request: HttpServletRequest,
    response: HttpServletResponse
  ): LoginResponse {
    val ipAddress = IpExtractor.getIpAddress(request)
    try {
      val loginResponseES = endeavourSecurityService.login(ipAddress, code, state)
      val cookie = Cookie("session_id", loginResponseES.sessionId)
      cookie.path = "/"
      cookie.isHttpOnly = true
      response.addCookie(cookie)
      val loginResponse = LoginResponse()
      loginResponse.user = loginResponseES.user
      loginResponse.state = loginResponseES.state
      return loginResponse
    } catch (e: UserAuthorisationException) {
      val cookie = Cookie("session_id", null)
      cookie.path = "/"
      cookie.isHttpOnly = true
      cookie.maxAge = 0
      response.addCookie(cookie)
      throw e
    }
  }

  @Throws(HttpException::class)
  fun getLoginUrl(redirectUrl: String, request: HttpServletRequest): String {
    val ipAddress = IpExtractor.getIpAddress(request)
    return endeavourSecurityService.getLoginUrl(ipAddress, redirectUrl)
  }

  fun getRegisterUrl(request: HttpServletRequest, redirectUrl: String): String {
    val ipAddress = IpExtractor.getIpAddress(request)
    return endeavourSecurityService.getRegisterUrl(ipAddress, redirectUrl)
  }

  @Throws(HttpException::class)
  fun logout(request: HttpServletRequest, response: HttpServletResponse) {
    val ipAddress = IpExtractor.getIpAddress(request)
    val sessionId = getSessionId(request)
    endeavourSecurityService.logout(ipAddress, sessionId)
    val accessCookie = Cookie("session_id", "")
    accessCookie.path = "/"
    accessCookie.isHttpOnly = true
    accessCookie.maxAge = 0
    response.addCookie(accessCookie)
  }

  fun getSessionId(request: HttpServletRequest): String {
    val cookies = request.cookies
    if (cookies != null) {
      for (cookie in cookies) {
        if (cookie.name == "session_id") {
          return cookie.value
        }
      }
    }
    throw UnauthorizedException("No session id found")
  }

  @Throws(IOException::class)
  fun userExists(userId: String, request: HttpServletRequest): Boolean {
    val ipAddress = IpExtractor.getIpAddress(request)
    val sessionId = getSessionId(request)
    return endeavourSecurityService.isUser(ipAddress, sessionId, userId)
  }

  @Throws(UserNotFoundException::class)
  fun adminGetUsersInGroup(role: UserRole, request: HttpServletRequest): List<User> {
    val ipAddress = IpExtractor.getIpAddress(request)
    val sessionId = getSessionId(request)
    return endeavourSecurityService.adminGetUsersWithRole(ipAddress, sessionId, role)
  }

  fun adminGetGroups(): List<UserRole> {
    return UserRole.entries.toList()
  }

  @Throws(UserNotFoundException::class)
  fun updateUserNamespaces(
    userId: String,
    namespaces: MutableList<NamespacePermission>,
    request: HttpServletRequest
  ): User {
    val ipAddress = IpExtractor.getIpAddress(request)
    val sessionId = getSessionId(request)
    val user = endeavourSecurityService.adminGetUser(ipAddress, sessionId, userId)
    user.namespaces = namespaces
    return endeavourSecurityService.adminUpdateUser(ipAddress, sessionId, user)
  }

  /*  public void emailTemporaryPasswords(String path) throws IOException, MessagingException {
  List<User> users = excelReader.readUserImportFile(path);
  EmailService emailService = new EmailService(
    System.getenv("EMAILER_NOREPLY_HOST"),
    Integer.parseInt(System.getenv("EMAILER_NOREPLY_PORT")),
    System.getenv("EMAILER_NOREPLY_USERNAME"),
    System.getenv("EMAILER_NOREPLY_PASSWORD")
  );
  for (User user : users) {
    String emailSubject = "Temporary password";
    String contentTemplate = """
      <!DOCTYPE html>
        <html>
          <head>
            <meta charset='UTF-8'>
            <style>
              body { font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 20px; }
              .container { max-width: 600px; margin: auto; background: #ffffff; padding: 20px;
              border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
              .title { font-size: 20px; font-weight: bold; color: #333333; }
              .content { margin-top: 15px; font-size: 15px; color: #555555; }
              .password-box { margin-top: 20px; padding: 12px; background: #f0f4ff; border-left: 4px solid #4a74f5;
              font-size: 16px; font-weight: bold; color: #2a2a2a; }
              .footer { margin-top: 30px; font-size: 13px; color: #888888; }
            </style>
          </head>
          <body>
            <div class='container'>
              <div class='title'>Temporary Password Request</div>
              <div class='content'>
                Hello <b>%s</b>,<br><br>
                A temporary password has been generated for your account. Use the credentials below to log in and be sure to change your password after signing in.
              </div>
              <div class='password-box'>
                Temporary Password: %s
              </div>
              <div class='footer'>
                If you did not request this, please contact support immediately.
              </div>
            </div>
          </body>
        </html>
      """.formatted(user.getUsername(), user.getPassword());
    emailService.sendMail(emailSubject, contentTemplate, user.getEmail());
  }
}*/
  fun requiresPermission(permission: Permission, request: HttpServletRequest) {
    val ipAddress = IpExtractor.getIpAddress(request)
    val sessionId = getSessionId(request)
    endeavourSecurityService.requiresPermission(ipAddress, sessionId, permission)
  }
}
