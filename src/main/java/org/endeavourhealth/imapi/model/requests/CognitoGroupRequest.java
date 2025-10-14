package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;

@Getter
@Setter
public class CognitoGroupRequest {
  private String username;
  private UserRole groupName;
}
