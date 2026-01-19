package org.endeavourhealth.imapi.model.casdoor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OAuthTokens {
  private String access_token;
  private String id_token;
  private String refresh_token;
  private String token_type;
  private String scope;
  private String expires_in;
}
