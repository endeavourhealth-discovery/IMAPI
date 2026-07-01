package org.endeavourhealth.imapi.filer;

import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;

import java.util.Map;
import java.util.Set;

public interface TTEntityFiler {
  void fileEntity(TTEntityJava entity) throws TTFilerException;

  void updateIsAs(String iri);

  Set<String> getIsAs(String iri);

  void fileIsAs(Map<String, Set<String>> isAs);


  void deleteIsAs(Set<String> entities);
}
