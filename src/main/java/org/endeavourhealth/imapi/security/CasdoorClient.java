package org.endeavourhealth.imapi.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thin client for the parts of the Casdoor API IMAPI uses, authenticating as the configured Casdoor application (Basic auth).
 * Casdoor must be addressed over https: an http -> https redirect strips the Authorization header and turns POSTs into GETs.
 */
@Slf4j
public class CasdoorClient {
  static final Duration USER_CACHE_TTL = Duration.ofSeconds(60);
  private static volatile CasdoorClient instance;

  private final CasdoorSettings settings;
  private final RestClient http;
  private final Clock clock;
  private final Map<String, CachedUser> userCache = new ConcurrentHashMap<>();

  private record CachedUser(ObjectNode user, long expiresAtMillis) {
  }

  public CasdoorClient(CasdoorSettings settings, RestClient.Builder builder, Clock clock) {
    this.settings = settings;
    this.clock = clock;
    this.http = builder
      .baseUrl(settings.url())
      .defaultHeaders(headers -> headers.setBasicAuth(settings.clientId(), settings.clientSecret()))
      .build();
  }

  /** Shared instance configured from the environment, created on first use so the app can start without Casdoor settings. */
  public static CasdoorClient getInstance() {
    if (instance == null) {
      synchronized (CasdoorClient.class) {
        if (instance == null)
          instance = new CasdoorClient(CasdoorSettings.fromEnvironment().requireConfigured(), RestClient.builder(), Clock.systemUTC());
      }
    }
    return instance;
  }

  public CasdoorSettings settings() {
    return settings;
  }

  /** A user of the configured organisation, cached briefly. The returned node is a copy and safe to modify. */
  public Optional<ObjectNode> findUser(String name) {
    CachedUser cached = userCache.get(name);
    if (cached != null && cached.expiresAtMillis() > clock.millis()) return Optional.of(cached.user().deepCopy());

    JsonNode user = unwrap("get-user", http.get()
      .uri("/api/get-user?id={id}", settings.organisation() + "/" + name)
      .retrieve().body(JsonNode.class));
    if (user == null || !user.isObject()) return Optional.empty();

    userCache.put(name, new CachedUser(((ObjectNode) user).deepCopy(), clock.millis() + USER_CACHE_TTL.toMillis()));
    return Optional.of((ObjectNode) user);
  }

  /** Looks a user up by their Casdoor id (uuid). Not cached. */
  public Optional<ObjectNode> findUserById(String userId) {
    JsonNode user = unwrap("get-user", http.get()
      .uri("/api/get-user?userId={userId}&owner={owner}", Map.of("userId", userId, "owner", settings.organisation()))
      .retrieve().body(JsonNode.class));
    return user != null && user.isObject() ? Optional.of((ObjectNode) user) : Optional.empty();
  }

  public List<ObjectNode> getUsers() {
    JsonNode users = unwrap("get-users", http.get()
      .uri("/api/get-users?owner={owner}", settings.organisation())
      .retrieve().body(JsonNode.class));
    List<ObjectNode> result = new ArrayList<>();
    if (users != null && users.isArray()) users.forEach(u -> result.add((ObjectNode) u));
    return result;
  }

  /** Takes the complete user as returned by get-user. Without `id` Casdoor would fall back to the caller's own (application) identity. */
  public void updateUser(ObjectNode user) {
    String name = user.path("name").asText();
    unwrap("update-user", http.post()
      .uri("/api/update-user?id={id}", settings.organisation() + "/" + name)
      .contentType(MediaType.APPLICATION_JSON)
      .body(user)
      .retrieve().body(JsonNode.class));
    userCache.remove(name);
  }

  /** Asks the configured Casdoor (casbin) enforcer whether `sub` may perform `act` on `obj`. */
  public boolean enforce(Object sub, String obj, String act) {
    JsonNode results = unwrap("enforce", http.post()
      .uri("/api/enforce?enforcerId={enforcerId}", settings.enforcerId())
      .contentType(MediaType.APPLICATION_JSON)
      .body(List.of(sub, obj, act))
      .retrieve().body(JsonNode.class));
    if (results == null || !results.isArray() || results.isEmpty()) return false;
    for (JsonNode result : results) if (!result.asBoolean(false)) return false;
    return true;
  }

  /** Casdoor wraps most responses as { status, msg, data }. */
  static JsonNode unwrap(String endpoint, JsonNode response) {
    if (response == null || !response.isObject() || !response.path("status").isTextual()) return response;
    String status = response.path("status").asText();
    if ("error".equals(status)) throw new CasdoorException("Casdoor " + endpoint + " failed: " + response.path("msg").asText());
    if ("ok".equals(status)) return response.path("data");
    return response;
  }
}
