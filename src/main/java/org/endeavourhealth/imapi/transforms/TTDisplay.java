package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.interfacemanager.model.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNodeJava;
import org.endeavourhealth.interfacemanager.model.ImVocab;
import org.endeavourhealth.interfacemanager.model.RdfVocab;
import org.endeavourhealth.interfacemanager.model.RdfsVocab;

/**
 * static utilities to handle templated display orders of RDF nodes
 */
public class TTDisplay {
  private static final TTIriRef[] entity = {TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE), TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DEFINITION)};

  private TTDisplay() {
    throw new IllegalStateException("Utility class");
  }

  public static TTIriRef[] getTemplate(TTNodeJava node) {
    return entity;
  }

}
