package org.endeavourhealth.imapi.model.casbin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicyRequest {
  private String userId;
  private String dataSource;
  private AccessRequest accessRequest;
}
