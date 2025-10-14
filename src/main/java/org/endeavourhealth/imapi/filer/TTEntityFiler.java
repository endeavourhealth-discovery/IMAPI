package org.endeavourhealth.imapi.filer;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;

import java.util.Map;
import java.util.Set;

public interface TTEntityFiler {
  void fileEntity(TTEntity entity) throws TTFilerException;

  void updateIsAs(TTEntity entity);

  Set<String> getIsAs(String iri);

  void fileIsAs(Map<String, Set<String>> isAs);

  Set<TTEntity> getDescendants(Set<String> entities);

  void deleteIsAs(Set<String> entities);
}
