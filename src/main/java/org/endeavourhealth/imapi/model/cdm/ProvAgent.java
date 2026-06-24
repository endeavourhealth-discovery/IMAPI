package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.interfacemanager.model.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;

public class ProvAgent extends Entry {

  public ProvAgent() {
    this.addType(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROVENANCE_AGENT));
  }


  public TTIriRef getParticipationType() {

    return (TTIriRef) TTUtil.get(this, TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PARTICIPATION_TYPE), TTIriRef.class);
  }

  @JsonSetter
  public ProvAgent setParticipationType(TTIriRef participationType) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PARTICIPATION_TYPE), participationType);
    return this;
  }

  public TTIriRef getPersonInRole() {
    return
      (TTIriRef) TTUtil.get(this, TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PERSON_IN_ROLE), TTIriRef.class);
  }

  @JsonSetter
  public ProvAgent setPersonInRole(TTIriRef personInRole) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PERSON_IN_ROLE), personInRole);
    return this;
  }
}
