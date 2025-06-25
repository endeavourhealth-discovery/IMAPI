package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class SetDistillationRequest {
  private List<TTIriRef> conceptList;
  private String graph;

  public SetDistillationRequest setConceptList(List<TTIriRef> conceptList) {
    this.conceptList = conceptList;
    return this;
  }

  public void addToConceptList(TTIriRef concept) {
    if (null == conceptList) {
      conceptList = new ArrayList<>();
    }
    this.conceptList.add(concept);
  }

  public SetDistillationRequest setGraph(String graph) {
    this.graph = graph;
    return this;
  }
}
