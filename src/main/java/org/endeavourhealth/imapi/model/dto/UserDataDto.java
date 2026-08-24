package org.endeavourhealth.imapi.model.dto;

import java.util.List;

import lombok.Data;
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto;

@Data
public class UserDataDto {

  private String preset;
  private String primaryColor;
  private boolean darkMode;
  private String scale;
  private List<String> organisations;
  private List<String> favourites;
  private List<RecentActivityItemDto> mru;
}
