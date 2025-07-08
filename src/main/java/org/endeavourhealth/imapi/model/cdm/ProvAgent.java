package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class ProvAgent extends Entry {

  public ProvAgent() {
    this.addType(iri(IM.PROVENANCE_AGENT));
  }


  public TTIriRef getParticipationType() {

    return (TTIriRef) TTUtil.get(this, iri(IM.PARTICIPATION_TYPE), TTIriRef.class);
  }

  @JsonSetter
  public ProvAgent setParticipationType(TTIriRef participationType) {
    set(iri(IM.PARTICIPATION_TYPE), participationType);
    return this;
  }

  public TTIriRef getPersonInRole() {
    return
      (TTIriRef) TTUtil.get(this, iri(IM.PERSON_IN_ROLE), TTIriRef.class);
  }

  @JsonSetter
  public ProvAgent setPersonInRole(TTIriRef personInRole) {
    set(iri(IM.PERSON_IN_ROLE), personInRole);
    return this;
  }
}
