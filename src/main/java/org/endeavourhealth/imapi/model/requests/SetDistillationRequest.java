package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class SetDistillationRequest {
  private List<TTIriRefExtended> conceptList;
  private GRAPH graph;

  public SetDistillationRequest setConceptList(List<TTIriRefExtended> conceptList) {
    this.conceptList = conceptList;
    return this;
  }

  public void addToConceptList(TTIriRefExtended concept) {
    if (null == conceptList) {
      conceptList = new ArrayList<>();
    }
    this.conceptList.add(concept);
  }

  public SetDistillationRequest setGraph(GRAPH graph) {
    this.graph = graph;
    return this;
  }
}
