package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.eclipse.rdf4j.http.protocol.UnauthorizedException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.model.responses.LoginResponse;
import org.endeavourhealth.imapi.model.responses.LoginResponseES;
import org.endeavourhealth.imapi.model.security.NamespacePermission;
import org.endeavourhealth.imapi.model.security.Permission;
import org.endeavourhealth.imapi.model.security.User;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.endeavourhealth.imapi.utility.IpExtractor.getIpAddress;

@Component
@Slf4j
public class SecurityService {
  private EndeavourSecurityService endeavourSecurityService = new EndeavourSecurityService();

  public User getUser(HttpServletRequest request) throws UserNotFoundException, JsonProcessingException {
    String sessionId = getSessionId(request);
    String ipAddress = getIpAddress(request);
    return endeavourSecurityService.getUser(ipAddress, sessionId);
  }

  public String getUserUrl(HttpServletRequest request) {
    String sessionId = getSessionId(request);
    String ipAddress = getIpAddress(request);
    return endeavourSecurityService.getProfileUrl(ipAddress, sessionId);
  }

  public User updateUser(HttpServletRequest request, User user) throws UserNotFoundException, IOException {
    String sessionId = getSessionId(request);
    String ipAddress = getIpAddress(request);
    return endeavourSecurityService.updateUser(ipAddress, sessionId, user);
  }

  public LoginResponse loginUser(String code, String state, HttpServletRequest request, HttpServletResponse response) throws HttpException {
    String ipAddress = getIpAddress(request);
    LoginResponseES loginResponseES = endeavourSecurityService.login(ipAddress, code, state);
    Cookie cookie = new Cookie("session_id", loginResponseES.getSessionId());
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    response.addCookie(cookie);
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setUser(loginResponseES.getUser());
    loginResponse.setState(loginResponseES.getState());
    return loginResponse;
  }

  public String getLoginUrl(String redirectUrl, HttpServletRequest request) throws HttpException {
    String ipAddress = getIpAddress(request);
    return endeavourSecurityService.getLoginUrl(ipAddress, redirectUrl);
  }

  public String getRegisterUrl(HttpServletRequest request, String redirectUrl) {
    String ipAddress = getIpAddress(request);
    return endeavourSecurityService.getRegisterUrl(ipAddress, redirectUrl);
  }

  public void logout(HttpServletRequest request, HttpServletResponse response) throws HttpException {
    String ipAddress = getIpAddress(request);
    String sessionId = getSessionId(request);
    endeavourSecurityService.logout(ipAddress, sessionId);
    Cookie accessCookie = new Cookie("session_id", "");
    accessCookie.setPath("/");
    accessCookie.setHttpOnly(true);
    accessCookie.setMaxAge(0);
    response.addCookie(accessCookie);
  }

  public String getSessionId(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("session_id")) {
          return cookie.getValue();
        }
      }
    }
    throw new UnauthorizedException("No session id found");
  }

  public boolean userExists(String userId, HttpServletRequest request) throws IOException {
    String ipAddress = getIpAddress(request);
    String sessionId = getSessionId(request);
    return endeavourSecurityService.isUser(ipAddress, sessionId, userId);
  }

  public List<User> adminGetUsersInGroup(UserRole role, HttpServletRequest request) throws UserNotFoundException {
    String ipAddress = getIpAddress(request);
    String sessionId = getSessionId(request);
    return endeavourSecurityService.adminGetUsersWithRole(ipAddress, sessionId, role);
  }

  public List<UserRole> adminGetGroups() {
    return Arrays.stream(UserRole.values()).toList();
  }

  public User updateUserNamespaces(String userId, List<NamespacePermission> namespaces, HttpServletRequest request) throws UserNotFoundException {
    String ipAddress = getIpAddress(request);
    String sessionId = getSessionId(request);
    User user = endeavourSecurityService.adminGetUser(ipAddress, sessionId, userId);
    user.setNamespaces(namespaces);
    return endeavourSecurityService.adminUpdateUser(ipAddress, sessionId, user);
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

  public void requiresPermission(Permission permission, HttpServletRequest request) {
    String ipAddress = getIpAddress(request);
    String sessionId = getSessionId(request);
    endeavourSecurityService.requiresPermission(ipAddress, sessionId, permission);
  }
}
