package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CognitoGroupRequest {
  private String username;
  private String groupName;
}
