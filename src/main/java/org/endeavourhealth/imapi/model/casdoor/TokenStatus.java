package org.endeavourhealth.imapi.model.casdoor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenStatus {
  private boolean active;
  private String scope;
  private String client_id;
  private String username;
  private String token_type;
  private int exp;
  private int iat;
  private int nbf;
  private String sub;
  private List<String> aud;
  private String iss;
  private String jti;
}
