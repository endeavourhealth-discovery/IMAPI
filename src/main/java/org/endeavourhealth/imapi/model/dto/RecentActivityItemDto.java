package org.endeavourhealth.imapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecentActivityItemDto {
  private String iri;
  private Date dateTime;
  private String action;
}
