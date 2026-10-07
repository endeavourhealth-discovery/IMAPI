package org.endeavourhealth.imapi.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CasdoorTokenValidatorTest {
  private final CasdoorTokenValidator validator = new CasdoorTokenValidator(List.of("query-runner-id", "directory-id"));

  private static Jwt jwt(Map<String, Object> claims) {
    return new Jwt("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "RS256"), claims);
  }

  @Test
  void acceptsAnAccessTokenIssuedToAnAcceptedApplication() {
    assertFalse(validator.validate(jwt(Map.of("aud", List.of("directory-id"), "tokenType", "access-token"))).hasErrors());
  }

  @Test
  void acceptsTokensWithNoTokenTypeClaim() {
    assertFalse(validator.validate(jwt(Map.of("aud", List.of("query-runner-id")))).hasErrors());
  }

  @Test
  void rejectsATokenIssuedToAnotherApplication() {
    assertTrue(validator.validate(jwt(Map.of("aud", List.of("someone-else")))).hasErrors());
  }

  @Test
  void rejectsATokenWithNoAudience() {
    assertTrue(validator.validate(jwt(Map.of("sub", "x"))).hasErrors());
  }

  @Test
  void rejectsARefreshToken() {
    assertTrue(validator.validate(jwt(Map.of("aud", List.of("directory-id"), "tokenType", "refresh-token"))).hasErrors());
  }
}
