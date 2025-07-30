package org.endeavourhealth.imapi.model.dto;

import lombok.Data;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

@Data
public class FilterOptionsDto {
  private List<TTIriRef> status;
  private List<TTIriRef> schemes;
  private List<TTIriRef> types;
  private List<TTIriRef> sortFields;
  private List<TTIriRef> sortDirections;
}
