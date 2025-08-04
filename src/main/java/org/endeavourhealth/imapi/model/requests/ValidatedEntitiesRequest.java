package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.endeavourhealth.imapi.vocabulary.types.Graph;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ValidatedEntitiesRequest {
  private List<String> snomedCodes;
  private Graph graph;

  public ValidatedEntitiesRequest setSnomedCodes(List<String> snomedCodes) {
    this.snomedCodes = snomedCodes;
    return this;
  }

  public void addToSnomedCodes(String code) {
    if (null == snomedCodes) {
      snomedCodes = new ArrayList<>();
    }
    this.snomedCodes.add(code);
  }

  public ValidatedEntitiesRequest setGraph(Graph graph) {
    this.graph = graph;
    return this;
  }
}
