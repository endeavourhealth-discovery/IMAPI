package org.endeavourhealth.imapi.model.dto;

import lombok.Data;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.util.List;
import java.util.Map;

@Data
public class FilterOptionsDto {
  private List<TTIriRefExtended> status;
  private List<TTIriRefExtended> schemes;
  private List<TTIriRefExtended> types;
  private List<TTIriRefExtended> sortFields;
  private List<TTIriRefExtended> sortDirections;
  private Map<String,List<TTIriRefExtended>> typeSchemes;
}
