package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.interfacemanager.model.IM;
import org.endeavourhealth.interfacemanager.model.RDF;
import org.endeavourhealth.interfacemanager.model.RDFS;

/**
 * static utilities to handle templated display orders of RDF nodes
 */
public class TTDisplay {
  private static final TTIriRef[] entity = {new TTIriRef(RDF.TYPE), new TTIriRef(RDFS.LABEL), new TTIriRef(IM.DEFINITION)};

  private TTDisplay() {
    throw new IllegalStateException("Utility class");
  }

  public static TTIriRef[] getTemplate(TTNode node) {
    return entity;
  }

}
