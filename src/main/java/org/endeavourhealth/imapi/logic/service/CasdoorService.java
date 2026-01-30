package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.casbin.casdoor.config.CasdoorConfiguration;
import org.casbin.casdoor.exception.AuthException;
import org.casbin.casdoor.service.AuthService;
import org.casbin.casdoor.service.TokenService;
import org.casbin.casdoor.service.UserService;
import org.eclipse.rdf4j.http.protocol.UnauthorizedException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.excel.ExcelReader;
import org.endeavourhealth.imapi.model.casdoor.OAuthTokens;
import org.endeavourhealth.imapi.model.casdoor.Session;
import org.endeavourhealth.imapi.model.casdoor.TokenStatus;
import org.endeavourhealth.imapi.model.casdoor.User;
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto;
import org.endeavourhealth.imapi.model.primevue.FontSize;
import org.endeavourhealth.imapi.model.primevue.PrimeVueColors;
import org.endeavourhealth.imapi.model.primevue.PrimeVuePresetThemes;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.endeavourhealth.imapi.utility.HttpRequestService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.utility.IpExtractor.getIpAddress;

@Component
@Slf4j
public class CasdoorService {
  private static Set<Session> activeSessions = new HashSet<>();
  private CasdoorConfiguration casdoorConfiguration;
  private AuthService casdoorAuthService;
  private UserService casdoorUserService;
  private TokenService casdoorTokenService;
  private ExcelReader excelReader = new ExcelReader();
  private ObjectMapper om = new ObjectMapper();
  private EntityService entityService = new EntityService();
  private HttpRequestService httpRequestService = new HttpRequestService();

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
    casdoorTokenService = new TokenService(casdoorConfiguration);
  }

  public boolean validateToken(String token) {
    try {
      Map<String, Object> body = new HashMap<>();
      body.put("token", token);
      body.put("token_type_hint", "access_token");
      body.put("client_id", clientId);
      body.put("client_secret", clientSecret);
      TokenStatus tokenStatus = httpRequestService.postForm(System.getenv("CASDOOR_ENDPOINT") + "/api/login/oauth/introspect", body, TokenStatus.class);
      return tokenStatus.isActive();
    } catch (AuthException | HttpException e) {
      return false;
    }
  }

  public User getUser(HttpServletRequest request) throws UserNotFoundException, JsonProcessingException {
    Session session = getSession(request);
    org.casbin.casdoor.entity.User user = casdoorAuthService.parseJwtToken(session.getAccess_token());
    try {
      org.casbin.casdoor.entity.User refreshUser = casdoorUserService.getUser(user.name);
      return casdoorUserToIMUser(refreshUser);
    } catch (IOException e) {
      throw new UserNotFoundException(e.getMessage());
    }
  }

  public org.casbin.casdoor.entity.User getCasdoorUser(HttpServletRequest request) throws UserNotFoundException, JsonProcessingException {
    Session session = getSession(request);
    org.casbin.casdoor.entity.User user = casdoorAuthService.parseJwtToken(session.getAccess_token());
    try {
      return casdoorUserService.getUser(user.name);
    } catch (IOException e) {
      throw new UserNotFoundException(e.getMessage());
    }
  }

  public String getUserUrl(HttpServletRequest request) throws UserNotFoundException {
    Session session = getSession(request);
    return casdoorAuthService.getMyProfileUrl(session.getAccess_token());
  }

  public void updateUser(org.casbin.casdoor.entity.User user) throws UserNotFoundException, IOException {
    casdoorUserService.updateUser(user);
  }

  private User casdoorUserToIMUser(org.casbin.casdoor.entity.User casdoorUser) throws JsonProcessingException {
    User user = new User();
    user.setId(casdoorUser.id);
    user.setFirstName(casdoorUser.firstName);
    user.setLastName(casdoorUser.lastName);
    user.setEmail(casdoorUser.email);
    user.setUsername(casdoorUser.name);
    user.setAvatar(casdoorUser.avatar);
    user.setRoles(casdoorUser.roles.stream().map(role -> UserRole.valueOf(role.name)).collect(Collectors.toList()));
    user.setGroups(casdoorUser.groups);
    if (casdoorUser.properties.containsKey("theme"))
      user.setTheme(PrimeVuePresetThemes.valueOf(casdoorUser.properties.get("theme")));
    if (casdoorUser.properties.containsKey("primaryColor"))
      user.setPrimaryColor(Objects.requireNonNull(PrimeVueColors.Companion.fromValue(casdoorUser.properties.get("primaryColor"))));
    if (casdoorUser.properties.containsKey("secondaryColor"))
      user.setSecondaryColor(Objects.requireNonNull(PrimeVueColors.Companion.fromValue(casdoorUser.properties.get("secondaryColor"))));
    if (casdoorUser.properties.containsKey("darkMode"))
      user.setDarkMode(casdoorUser.properties.get("darkMode").equals("true"));
    if (casdoorUser.properties.containsKey("fontSize"))
      user.setFontSize(Objects.requireNonNull(FontSize.Companion.fromValue(casdoorUser.properties.get("fontSize"))));
    if (casdoorUser.properties.containsKey("favourites"))
      user.setFavourites(om.readValue(casdoorUser.properties.get("favourites"), new TypeReference<List<String>>() {
      }));
    if (casdoorUser.properties.containsKey("recentActivity")) {
      List<RecentActivityItemDto> recentActivity = om.readValue(casdoorUser.properties.get("recentActivity"), new TypeReference<List<RecentActivityItemDto>>() {
      });
      boolean hasNoneExistingIris = recentActivity.stream().anyMatch(ra -> !entityService.iriExists(ra.getIri()));
      if (hasNoneExistingIris) {
        List<RecentActivityItemDto> updatedRecentActivity = recentActivity.stream().filter(ra -> entityService.iriExists(ra.getIri())).collect(Collectors.toList());
        user.setRecentActivity(updatedRecentActivity);
      } else {
        user.setRecentActivity(recentActivity);
      }
    }
    if (casdoorUser.properties.containsKey("organisations"))
      user.setOrganisations(om.readValue(casdoorUser.properties.get("organisations"), new TypeReference<List<String>>() {
      }));
    return user;
  }

  public void loginUser(String code, String state, HttpServletRequest request, HttpServletResponse response) throws HttpException {
    Map<String, Object> params = new HashMap<>();
    params.put("code", code);
    params.put("grant_type", "authorization_code");
    params.put("client_id", clientId);
    params.put("client_secret", clientSecret);
    OAuthTokens oAuthTokens = httpRequestService.post(endpoint + "/api/login/oauth/access_token", params, OAuthTokens.class);
    Session session = new Session(oAuthTokens, getIpAddress(request));
    activeSessions.add(session);
    Cookie cookie = new Cookie("session_id", session.getId());
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    response.addCookie(cookie);
  }

  public void logout(HttpServletRequest request, HttpServletResponse response) throws HttpException {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("session_id")) {
          String sessionId = cookie.getValue();
          Map<String, Object> idBody = new HashMap<>();
          Session session = activeSessions.stream().filter(s -> s.getId().equals(sessionId)).findFirst().orElse(null);
          if (session != null) {
            idBody.put("id_token_hint", session.getId_token());
            httpRequestService.post(System.getenv("CASDOOR_ENDPOINT") + "/api/logout", idBody, Void.class);
            Map<String, Object> accessBody = new HashMap<>();
            accessBody.put("accessToken", session.getAccess_token());
            httpRequestService.post(System.getenv("CASDOOR_ENDPOINT") + "/api/delete-token", accessBody, Void.class);
            activeSessions.remove(session);
          }
        }
      }
    }
    Cookie accessCookie = new Cookie("session_id", "");
    accessCookie.setPath("/");
    accessCookie.setHttpOnly(true);
    accessCookie.setMaxAge(0);
    response.addCookie(accessCookie);
  }

  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws HttpException {
    Session session = getSession(request);
    Map<String, Object> body = new HashMap<>();
    body.put("refresh_token", session.getRefresh_token());
    body.put("grant_type", "refresh_token");
    body.put("client_id", clientId);
    body.put("client_secret", clientSecret);
    body.put("scope", session.getScope());
    OAuthTokens oAuthTokens = httpRequestService.post(System.getenv("CASDOOR_ENDPOINT") + "/api/login/oauth/refresh_token", body, OAuthTokens.class);
    Session refreshedSession = new Session(oAuthTokens, getIpAddress(request));
    activeSessions.remove(session);
    activeSessions.add(refreshedSession);
    Cookie accessCookie = new Cookie("session_id", refreshedSession.getId());
    accessCookie.setPath("/");
    accessCookie.setHttpOnly(true);
    response.addCookie(accessCookie);
  }

  public Session getSession(String sessionId, String ipAddress) {
    return activeSessions.stream().filter(s -> s.getId().equals(sessionId) && s.getIpAddress().equals(ipAddress)).findFirst().orElseThrow();
  }

  public Session getSession(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("session_id")) {
          String sessionId = cookie.getValue();
          return getSession(sessionId, getIpAddress(request));
        }
      }
    }
    throw new UnauthorizedException("No active session found");
  }

  public boolean userExists(String userId) throws IOException {
    return casdoorUserService.getUsers().stream().anyMatch(user -> user.id.equals(userId));
  }

  public User adminGetUser(String userId) throws UserNotFoundException {
    try {
      org.casbin.casdoor.entity.User casdoorUser = casdoorUserService.getUser(userId);
      return casdoorUserToIMUser(casdoorUser);
    } catch (IOException e) {
      throw new UserNotFoundException(userId);
    }
  }

  public org.casbin.casdoor.entity.User adminGetCasdoorUser(String userId) throws UserNotFoundException {
    try {
      return casdoorUserService.getUser(userId);
    } catch (IOException e) {
      throw new UserNotFoundException(userId);
    }
  }

  public List<User> adminGetUsersInGroup(UserRole group) throws UserNotFoundException {
    try {
      List<org.casbin.casdoor.entity.User> casdoorUsers = casdoorUserService.getUsers();
      return casdoorUsers.stream().filter(user -> user.roles.contains(group)).map(user -> {
        try {
          return casdoorUserToIMUser(user);
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
      }).collect(Collectors.toList());
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

  @Scheduled(cron = "0 0 0 * * *")
  public void tidySessions() {
    activeSessions.removeIf(Session::isExpired);
  }

}
