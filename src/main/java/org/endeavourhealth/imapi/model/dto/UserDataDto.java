package org.endeavourhealth.imapi.model.dto;

import lombok.Data;

import java.util.List;

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
