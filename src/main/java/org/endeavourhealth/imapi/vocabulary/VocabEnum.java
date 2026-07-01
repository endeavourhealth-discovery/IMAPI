package org.endeavourhealth.imapi.vocabulary;

import org.eclipse.rdf4j.model.IRI;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public interface VocabEnum {
  TTIriRef asIri();

  IRI asDbIri();
}
