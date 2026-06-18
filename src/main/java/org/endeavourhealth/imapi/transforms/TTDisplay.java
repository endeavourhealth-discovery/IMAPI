package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.library.model.tripletree.TTIriRef;
import org.endeavourhealth.library.model.tripletree.TTNode;
import org.endeavourhealth.library.vocabulary.IM;
import org.endeavourhealth.library.vocabulary.RDF;
import org.endeavourhealth.library.vocabulary.RDFS;

import static org.endeavourhealth.library.model.tripletree.TTIriRef.iri;

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
