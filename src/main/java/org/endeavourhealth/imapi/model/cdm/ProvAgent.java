package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.interfacemanager.model.IM;

public class ProvAgent extends Entry {

  public ProvAgent() {
    this.addType(new TTIriRef(IM.PROVENANCE_AGENT));
  }


  public TTIriRef getParticipationType() {

    return (TTIriRef) TTUtil.get(this, new TTIriRef(IM.PARTICIPATION_TYPE), TTIriRef.class);
  }

  @JsonSetter
  public ProvAgent setParticipationType(TTIriRef participationType) {
    set(new TTIriRef(IM.PARTICIPATION_TYPE), participationType);
    return this;
  }

  public TTIriRef getPersonInRole() {
    return
      (TTIriRef) TTUtil.get(this, new TTIriRef(IM.PERSON_IN_ROLE), TTIriRef.class);
  }

  @JsonSetter
  public ProvAgent setPersonInRole(TTIriRef personInRole) {
    set(new TTIriRef(IM.PERSON_IN_ROLE), personInRole);
    return this;
  }
}
