package org.endeavourhealth.imapi.logic.service;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.casbin.casdoor.config.CasdoorConfiguration;
import org.casbin.casdoor.exception.AuthException;
import org.casbin.casdoor.service.AuthService;
import org.casbin.casdoor.service.UserService;
import org.eclipse.rdf4j.http.protocol.UnauthorizedException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.excel.ExcelReader;
import org.endeavourhealth.imapi.model.casdoor.User;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CasdoorService {
  private CasdoorConfiguration casdoorConfiguration;
  private AuthService casdoorAuthService;
  private UserService casdoorUserService;
  private ExcelReader excelReader = new ExcelReader();

  private String clientId = System.getenv("CASDOOR_CLIENT_ID");
  private String endpoint = System.getenv("CASDOOR_ENDPOINT");
  private String certificate;
  private String applicationName = System.getenv("CASDOOR_APPLICATION_NAME");
  private String clientSecret = System.getenv("CASDOOR_CLIENT_SECRET");
  private String organisationName = System.getenv("CASDOOR_ORGANISATION_NAME");

  public CasdoorService() {
    casdoorConfiguration = new CasdoorConfiguration();
    casdoorConfiguration.setApplicationName(applicationName);
    casdoorConfiguration.setClientId(clientId);
    casdoorConfiguration.setEndpoint(endpoint);
    try {
      ClassLoader classloader = Thread.currentThread().getContextClassLoader();
      InputStream is = classloader.getResourceAsStream("casdoor-cert.txt");
      this.certificate = new String(is.readAllBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
    casdoorConfiguration.setCertificate(certificate);
    casdoorConfiguration.setClientSecret(clientSecret);
    casdoorConfiguration.setOrganizationName(organisationName);
    casdoorAuthService = new AuthService(casdoorConfiguration);
    casdoorUserService = new UserService(casdoorConfiguration);
  }

  public boolean validateToken(String token) {
    try {
      org.casbin.casdoor.entity.User user = casdoorAuthService.parseJwtToken(token);
      return user != null;
    } catch (AuthException e) {
      return false;
    }
  }

  public User getUser(HttpServletRequest request) throws UserNotFoundException {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("access_token")) {
          String token = cookie.getValue();
          org.casbin.casdoor.entity.User user = casdoorAuthService.parseJwtToken(token);
          return parseUser(user);
        }
      }
    }
    throw new UserNotFoundException("User not found");
  }

  public String getUserUrl(HttpServletRequest request) throws UserNotFoundException {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("access_token")) {
          String token = cookie.getValue();
          return casdoorAuthService.getMyProfileUrl(token);
        }
      }
    }
    throw new UserNotFoundException("User token not found");
  }

  private User parseUser(org.casbin.casdoor.entity.User casdoorUser) {
    User user = new User();
    user.setId(casdoorUser.id);
    user.setFirstName(casdoorUser.firstName);
    user.setLastName(casdoorUser.lastName);
    user.setEmail(casdoorUser.email);
    user.setUsername(casdoorUser.name);
    user.setAvatar(casdoorUser.avatar);
    user.setRoles(casdoorUser.roles.stream().map(role -> UserRole.valueOf(role.name)).collect(Collectors.toList()));
    user.setGroups(casdoorUser.groups);
    return user;
  }

  public void loginUser(String code, String state, HttpServletResponse response) {
    String token = casdoorAuthService.getOAuthToken(code, state);
    Cookie cookie = new Cookie("access_token", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    response.addCookie(cookie);
  }

  public void loginWithBearerToken(HttpServletRequest request, HttpServletResponse response) {
    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      throw new UnauthorizedException("Unauthorized");
    }
    String token = header.substring(7);
    Cookie cookie = new Cookie("access_token", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    response.addCookie(cookie);
  }

  public void logout(HttpServletResponse response) {
    Cookie cookie = new Cookie("access_token", "");
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(0);
    response.addCookie(cookie);
    Cookie sessionCookie = new Cookie("casdoor_session_id", "");
    sessionCookie.setPath("/");
    sessionCookie.setHttpOnly(true);
    sessionCookie.setMaxAge(0);
    response.addCookie(sessionCookie);
  }

  public User adminGetUser(String userId) throws UserNotFoundException {
    try {
      org.casbin.casdoor.entity.User casdoorUser = casdoorUserService.getUser(userId);
      return parseUser(casdoorUser);
    } catch (IOException e) {
      throw new UserNotFoundException(userId);
    }
  }

  public List<User> adminGetUsersInGroup(UserRole group) throws UserNotFoundException {
    try {
      List<org.casbin.casdoor.entity.User> casdoorUsers = casdoorUserService.getUsers();
      return casdoorUsers.stream().filter(user -> user.roles.contains(group)).map(user -> parseUser(user)).collect(Collectors.toList());
    } catch (IOException e) {
      throw new UserNotFoundException("Failed to get all users");
    }
  }

  public List<UserRole> adminGetGroups() {
    return Arrays.stream(UserRole.values()).toList();
  }

  public void emailTemporaryPasswords(String path) throws IOException, MessagingException {
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
  }

}
