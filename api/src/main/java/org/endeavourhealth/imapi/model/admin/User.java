package org.endeavourhealth.imapi.model.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User {
  private String id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String avatar;
  private List<String> roles;
  private List<String> mfaStatus;

  public User setPassword(String password) {
    this.password = "";
    return this;
  }
}
