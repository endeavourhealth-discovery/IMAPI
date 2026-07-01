package org.endeavourhealth.imapi.model.requests;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuperiorPropertiesBoolFocusPagedRequest {

  private String ecl;
  private int page;
  private int size;
  private List<String> schemeFilters;
  private boolean inactive;

  public SuperiorPropertiesBoolFocusPagedRequest(String ecl, int page, int size, List<String> schemeFilters, boolean inactive) {
    this.ecl = ecl;
    this.page = page;
    this.size = size;
    this.schemeFilters = schemeFilters;
    this.inactive = inactive;
  }

  public SuperiorPropertiesBoolFocusPagedRequest() {}
}
