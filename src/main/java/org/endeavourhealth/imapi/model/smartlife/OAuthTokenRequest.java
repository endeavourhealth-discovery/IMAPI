package org.endeavourhealth.imapi.model.smartlife;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.annotation.ModelAttribute;

@Getter
public class OAuthTokenRequest {

  private String grant_type;
  private String client_id;
  private String client_secret;
  @JsonIgnore
  private String token;

}
