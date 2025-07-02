package org.endeavourhealth.imapi.filer;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.Graph;

import java.util.Map;
import java.util.Set;

public interface TTEntityFiler {
  void fileEntity(TTEntity entity, Graph graph) throws TTFilerException;

  void updateIsAs(TTEntity entity, Graph graph) throws TTFilerException;

  Set<String> getIsAs(String iri, Graph graph) throws TTFilerException;

  void fileIsAs(Map<String, Set<String>> isAs, Graph graph);

  Set<TTEntity> getDescendants(Set<String> entities, Graph graph);

  void deleteIsas(Set<String> entities, Graph graph);
}
