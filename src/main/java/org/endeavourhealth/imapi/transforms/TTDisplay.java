package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

/**
 * static utilities to handle templated display orders of RDF nodes
 */
public class TTDisplay {
  private TTDisplay() {
    throw new IllegalStateException("Utility class");
  }

  private static TTIriRef[] entity = {iri(RDF.TYPE), iri(RDFS.LABEL), iri(IM.DEFINITION)};

  public static TTIriRef[] getTemplate(TTNode node) {
    return entity;
  }

}
