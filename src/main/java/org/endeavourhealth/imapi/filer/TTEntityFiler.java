package org.endeavourhealth.imapi.filer;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;

import java.util.Map;
import java.util.Set;

public interface TTEntityFiler {
  void fileEntity(TTEntity entity, String graph) throws TTFilerException;

  void updateIsAs(TTEntity entity, String graph) throws TTFilerException;

  Set<String> getIsAs(String iri, String graph) throws TTFilerException;

  void fileIsAs(Map<String, Set<String>> isAs, String graph);

  Set<TTEntity> getDescendants(Set<String> entities, String graph);

  void deleteIsas(Set<String> entities, String graph);
}
