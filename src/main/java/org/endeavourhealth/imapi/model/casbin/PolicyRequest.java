package org.endeavourhealth.imapi.model.casbin;

import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;

@Getter
@Setter
public class PolicyRequest {
  private UserRole userRole;
  private Resource resource;
  private Action action;
}
