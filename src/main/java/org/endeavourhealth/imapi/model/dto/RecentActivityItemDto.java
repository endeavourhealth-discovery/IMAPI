package org.endeavourhealth.imapi.model.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecentActivityItemDto {

  private String iri;
  private Date dateTime;
  private String action;
}
