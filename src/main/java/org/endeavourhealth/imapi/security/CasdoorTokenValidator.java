package org.endeavourhealth.imapi.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

/** Casdoor-specific token checks on top of signature, issuer and expiry: the token must be an access token issued to one of our applications. */
public class CasdoorTokenValidator implements OAuth2TokenValidator<Jwt> {
  private static final String REFRESH_TOKEN_TYPE = "refresh-token";

  private final List<String> allowedClientIds;

  public CasdoorTokenValidator(List<String> allowedClientIds) {
    this.allowedClientIds = allowedClientIds;
  }

  @Override
  public OAuth2TokenValidatorResult validate(Jwt jwt) {
    if (REFRESH_TOKEN_TYPE.equals(jwt.getClaimAsString("tokenType")))
      return failure("A refresh token cannot be used to access the API");

    List<String> audience = jwt.getAudience();
    if (audience == null || audience.stream().noneMatch(allowedClientIds::contains))
      return failure("The token was not issued to an application accepted by this API");

    return OAuth2TokenValidatorResult.success();
  }

  private static OAuth2TokenValidatorResult failure(String description) {
    return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", description, null));
  }
}
