package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.interfacemanager.model.IM;
import org.endeavourhealth.interfacemanager.model.RDF;
import org.endeavourhealth.interfacemanager.model.RDFS;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

/**
 * static utilities to handle templated display orders of RDF nodes
 */
public class TTDisplay {
  private static final TTIriRef[] entity = {iri(RDF.TYPE), iri(RDFS.LABEL), iri(IM.DEFINITION)};

  private TTDisplay() {
    throw new IllegalStateException("Utility class");
  }

  public static TTIriRef[] getTemplate(TTNode node) {
    return entity;
  }

}
