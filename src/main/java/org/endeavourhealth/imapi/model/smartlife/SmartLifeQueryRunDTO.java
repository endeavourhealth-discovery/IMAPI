package org.endeavourhealth.imapi.model.smartlife;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmartLifeQueryRunDTO {

  private String query_id;
  private String reference_date;
  private String date2;
  private String date3;

}
