package org.endeavourhealth.imapi.model.casdoor;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

public class Session extends OAuthTokens {
  @Getter
  private String id;
  @Getter
  private Instant createdAt;

  public Session(OAuthTokens oAuthTokens) {
    super(oAuthTokens.getAccess_token(), oAuthTokens.getId_token(), oAuthTokens.getRefresh_token(), oAuthTokens.getToken_type(), oAuthTokens.getScope(), oAuthTokens.getExpires_in());
    this.id = UUID.randomUUID().toString();
    this.createdAt = Instant.now();
  }

  public Session(String access_token, String id_token, String refresh_token, String token_type, String scope, int expires_in) {
    super(access_token, id_token, refresh_token, token_type, scope, expires_in);
    this.id = UUID.randomUUID().toString();
    this.createdAt = Instant.now();
  }

  public Session() {
  }

  public boolean isExpired() {
    Instant expiresAt = createdAt.plusSeconds(getExpires_in());
    return Instant.now().isAfter(expiresAt);
  }
}
