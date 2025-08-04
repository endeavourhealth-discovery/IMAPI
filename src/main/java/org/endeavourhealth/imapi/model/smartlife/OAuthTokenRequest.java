package org.endeavourhealth.imapi.model.smartlife;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class OAuthTokenRequest {

  private String grant_type;
  private String client_id;
  private String client_secret;
  private String token;

}
