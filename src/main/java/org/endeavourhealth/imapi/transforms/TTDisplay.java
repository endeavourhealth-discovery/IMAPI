package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.interfacemanager.model.ImVocab;
import org.endeavourhealth.interfacemanager.model.RdfVocab;
import org.endeavourhealth.interfacemanager.model.RdfsVocab;

/**
 * static utilities to handle templated display orders of RDF nodes
 */
public class TTDisplay {
  private static final TTIriRefExtended[] entity = {new TTIriRefExtended(RdfVocab.TYPE), new TTIriRefExtended(RdfsVocab.LABEL), new TTIriRefExtended(ImVocab.DEFINITION)};

  private TTDisplay() {
    throw new IllegalStateException("Utility class");
  }

  public static TTIriRefExtended[] getTemplate(TTNode node) {
    return entity;
  }

}
