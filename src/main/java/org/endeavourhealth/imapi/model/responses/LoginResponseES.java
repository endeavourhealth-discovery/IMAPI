package org.endeavourhealth.imapi.model.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.endeavourhealth.imapi.model.security.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseES {
  private String sessionId;
  private User user;
  private String state;
}
