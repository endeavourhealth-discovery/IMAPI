package org.endeavourhealth.imapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class CasdoorClientTest {
  private static final String BASIC = "Basic " + java.util.Base64.getEncoder().encodeToString("app-id:app-secret".getBytes());
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private final CasdoorSettings settings = new CasdoorSettings("https://auth.example.org", "https://auth.example.org", "Endeavour", "app-id", "app-secret",
    "Endeavour/TestEnforcer", List.of("client"), "RS256");
  private MockRestServiceServer server;
  private MutableClock clock;
  private CasdoorClient client;

  /** A clock the test can move forward. */
  private static class MutableClock extends Clock {
    private Instant now = Instant.parse("2026-01-01T00:00:00Z");

    void advance(Duration duration) {
      now = now.plus(duration);
    }

    @Override
    public java.time.ZoneId getZone() {
      return ZoneOffset.UTC;
    }

    @Override
    public Clock withZone(java.time.ZoneId zone) {
      return this;
    }

    @Override
    public Instant instant() {
      return now;
    }
  }

  @BeforeEach
  void setUp() {
    RestClient.Builder builder = RestClient.builder();
    server = MockRestServiceServer.bindTo(builder).build();
    clock = new MutableClock();
    client = new CasdoorClient(settings, builder, clock);
  }

  @Test
  void findsAUserByOrganisationAndNameAsTheApplicationAndUnwrapsTheEnvelope() {
    server.expect(requestTo(org.hamcrest.Matchers.startsWith("https://auth.example.org/api/get-user")))
      .andExpect(method(HttpMethod.GET))
      .andExpect(queryParam("id", "Endeavour%2Fjbloggs"))
      .andExpect(header("Authorization", BASIC))
      .andRespond(withSuccess("{\"status\":\"ok\",\"data\":{\"name\":\"jbloggs\",\"owner\":\"Endeavour\"}}", MediaType.APPLICATION_JSON));

    Optional<ObjectNode> user = client.findUser("jbloggs");

    assertEquals("jbloggs", user.orElseThrow().get("name").asText());
    server.verify();
  }

  @Test
  void acceptsABareUserResponseToo() {
    server.expect(requestTo(org.hamcrest.Matchers.startsWith("https://auth.example.org/api/get-user")))
      .andRespond(withSuccess("{\"name\":\"jbloggs\",\"owner\":\"Endeavour\"}", MediaType.APPLICATION_JSON));
    assertEquals("jbloggs", client.findUser("jbloggs").orElseThrow().get("name").asText());
  }

  @Test
  void anUnknownUserIsEmpty() {
    server.expect(requestTo(org.hamcrest.Matchers.startsWith("https://auth.example.org/api/get-user")))
      .andRespond(withSuccess("{\"status\":\"ok\",\"data\":null}", MediaType.APPLICATION_JSON));
    assertTrue(client.findUser("nobody").isEmpty());
  }

  @Test
  void cachesAUserBrieflyThenReadsItAgain() {
    server.expect(requestTo(org.hamcrest.Matchers.startsWith("https://auth.example.org/api/get-user")))
      .andRespond(withSuccess("{\"status\":\"ok\",\"data\":{\"name\":\"jbloggs\"}}", MediaType.APPLICATION_JSON));
    client.findUser("jbloggs");
    client.findUser("jbloggs");
    server.verify();
    server.reset();

    clock.advance(CasdoorClient.USER_CACHE_TTL.plusSeconds(1));
    server.expect(requestTo(org.hamcrest.Matchers.startsWith("https://auth.example.org/api/get-user")))
      .andRespond(withSuccess("{\"status\":\"ok\",\"data\":{\"name\":\"jbloggs\"}}", MediaType.APPLICATION_JSON));
    client.findUser("jbloggs");
    server.verify();
  }

  @Test
  void aCachedUserCannotBeChangedThroughACopyHandedOut() {
    server.expect(requestTo(org.hamcrest.Matchers.startsWith("https://auth.example.org/api/get-user")))
      .andRespond(withSuccess("{\"status\":\"ok\",\"data\":{\"name\":\"jbloggs\"}}", MediaType.APPLICATION_JSON));
    client.findUser("jbloggs").orElseThrow().put("name", "mutated");
    assertEquals("jbloggs", client.findUser("jbloggs").orElseThrow().get("name").asText());
  }

  @Test
  void findsAUserByIdWithTheOwner() {
    server.expect(requestTo(org.hamcrest.Matchers.startsWith("https://auth.example.org/api/get-user")))
      .andExpect(queryParam("userId", "1111"))
      .andExpect(queryParam("owner", "Endeavour"))
      .andRespond(withSuccess("{\"status\":\"ok\",\"data\":{\"name\":\"jbloggs\"}}", MediaType.APPLICATION_JSON));
    assertTrue(client.findUserById("1111").isPresent());
    server.verify();
  }

  @Test
  void updatesAUserByIdWithTheWholeUserAndForgetsTheCachedCopy() throws Exception {
    server.expect(requestTo(org.hamcrest.Matchers.startsWith("https://auth.example.org/api/get-user")))
      .andRespond(withSuccess("{\"status\":\"ok\",\"data\":{\"name\":\"jbloggs\"}}", MediaType.APPLICATION_JSON));
    client.findUser("jbloggs");
    server.verify();
    server.reset();

    server.expect(requestTo(org.hamcrest.Matchers.startsWith("https://auth.example.org/api/update-user")))
      .andExpect(method(HttpMethod.POST))
      .andExpect(queryParam("id", "Endeavour%2Fjbloggs"))
      .andExpect(header("Authorization", BASIC))
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.name").value("jbloggs"))
      .andExpect(jsonPath("$.properties.darkMode").value("true"))
      .andRespond(withSuccess("{\"status\":\"ok\",\"data\":\"Affected\"}", MediaType.APPLICATION_JSON));
    client.updateUser((ObjectNode) MAPPER.readTree("{\"name\":\"jbloggs\",\"properties\":{\"darkMode\":\"true\"}}"));
    server.verify();
    server.reset();

    server.expect(requestTo(org.hamcrest.Matchers.startsWith("https://auth.example.org/api/get-user")))
      .andRespond(withSuccess("{\"status\":\"ok\",\"data\":{\"name\":\"jbloggs\"}}", MediaType.APPLICATION_JSON));
    client.findUser("jbloggs");
    server.verify();
  }

  @Test
  void listsTheOrganisationsUsers() {
    server.expect(requestTo(org.hamcrest.Matchers.startsWith("https://auth.example.org/api/get-users")))
      .andExpect(queryParam("owner", "Endeavour"))
      .andRespond(withSuccess("{\"status\":\"ok\",\"data\":[{\"name\":\"a\"},{\"name\":\"b\"}]}", MediaType.APPLICATION_JSON));
    assertEquals(2, client.getUsers().size());
  }

  @Test
  void enforcesWithTheConfiguredEnforcerAndTheSubObjActRequest() {
    server.expect(requestTo(org.hamcrest.Matchers.startsWith("https://auth.example.org/api/enforce")))
      .andExpect(method(HttpMethod.POST))
      .andExpect(queryParam("enforcerId", "Endeavour%2FTestEnforcer"))
      .andExpect(header("Authorization", BASIC))
      .andExpect(jsonPath("$[0].Username").value("jbloggs"))
      .andExpect(jsonPath("$[1]").value("JOB"))
      .andExpect(jsonPath("$[2]").value("EXECUTE"))
      .andRespond(withSuccess("{\"status\":\"ok\",\"data\":[true],\"data2\":[\"model\"]}", MediaType.APPLICATION_JSON));

    assertTrue(client.enforce(Map.of("Username", "jbloggs"), "JOB", "EXECUTE"));
    server.verify();
  }

  @Test
  void aDeniedOrEmptyEnforceResultIsNotAllowed() {
    server.expect(requestTo(org.hamcrest.Matchers.startsWith("https://auth.example.org/api/enforce")))
      .andRespond(withSuccess("{\"status\":\"ok\",\"data\":[false]}", MediaType.APPLICATION_JSON));
    assertFalse(client.enforce(Map.of(), "JOB", "EXECUTE"));
    server.verify();
    server.reset();

    server.expect(requestTo(org.hamcrest.Matchers.startsWith("https://auth.example.org/api/enforce")))
      .andRespond(withSuccess("{\"status\":\"ok\",\"data\":[]}", MediaType.APPLICATION_JSON));
    assertFalse(client.enforce(Map.of(), "JOB", "EXECUTE"));
  }

  @Test
  void anErrorEnvelopeBecomesAnExceptionNamingTheEndpoint() {
    server.expect(requestTo(org.hamcrest.Matchers.startsWith("https://auth.example.org/api/update-user")))
      .andRespond(withSuccess("{\"status\":\"error\",\"msg\":\"Unauthorized operation\"}", MediaType.APPLICATION_JSON));

    CasdoorException e = assertThrows(CasdoorException.class, () -> client.updateUser(MAPPER.createObjectNode().put("name", "x")));
    assertTrue(e.getMessage().contains("update-user"));
    assertTrue(e.getMessage().contains("Unauthorized operation"));
  }
}
