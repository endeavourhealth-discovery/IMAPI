package org.endeavourhealth.imapi.model.admin;

import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class User implements Serializable {
  private String id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private transient String password;
  private String avatar;
  private List<UserRole> roles;

  public User setPassword(String password) {
    this.password = "";
    return this;
  }
}
