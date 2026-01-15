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
  String access_token;
  String id_token;
  String refresh_token;
  String token_type;
  String scope;
  String expires_in;
}
