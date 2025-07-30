package org.endeavourhealth.imapi.model.smartlife;

public class OAuthTokenResponse {

  private String access_token;
  private String token_type;
  private String expires_in;

  public OAuthTokenResponse setAccess_token(String access_token) {
    this.access_token = access_token;
    return this;
  }

  public OAuthTokenResponse setToken_type(String token_type) {
    this.token_type = token_type;
    return this;
  }

  public OAuthTokenResponse setExpires_in(String expires_in) {
    this.expires_in = expires_in;
    return this;
  }
}
