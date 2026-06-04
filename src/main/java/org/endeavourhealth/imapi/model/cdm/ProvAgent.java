package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;

public class ProvAgent extends Entry {

  public ProvAgent() {
    this.addType(new TTIriRefExtended(ImVocab. PROVENANCE_AGENT));
  }


  public TTIriRefExtended getParticipationType() {

    return (TTIriRefExtended) TTUtil.get(this, new TTIriRefExtended(ImVocab. PARTICIPATION_TYPE), TTIriRefExtended.class);
  }

  @JsonSetter
  public ProvAgent setParticipationType(TTIriRefExtended participationType) {
    set(new TTIriRefExtended(ImVocab. PARTICIPATION_TYPE),participationType);
    return this;
  }

  public TTIriRefExtended getPersonInRole() {
    return
      (TTIriRefExtended) TTUtil.get(this, new TTIriRefExtended(ImVocab. PERSON_IN_ROLE), TTIriRefExtended.class);
  }

  @JsonSetter
  public ProvAgent setPersonInRole(TTIriRefExtended personInRole) {
    set(new TTIriRefExtended(ImVocab. PERSON_IN_ROLE),personInRole);
    return this;
  }
}
