package org.endeavourhealth.imapi.filer;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.Map;
import java.util.Set;

public interface TTEntityFiler {
  void fileEntity(TTEntity entity, TTIriRef graph) throws TTFilerException;

  void updateIsAs(TTEntity entity) throws TTFilerException;

  Set<String> getIsAs(String iri) throws TTFilerException;

  void fileIsAs(Map<String, Set<String>> isAs);

  Set<TTEntity> getDescendants(Set<String> entities);

  void deleteIsas(Set<String> entities);
}
