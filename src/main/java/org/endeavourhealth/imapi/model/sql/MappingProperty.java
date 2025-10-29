package org.endeavourhealth.imapi.model.sql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class MappingProperty {
  private List<String> path;
  private String dataModel;
}
