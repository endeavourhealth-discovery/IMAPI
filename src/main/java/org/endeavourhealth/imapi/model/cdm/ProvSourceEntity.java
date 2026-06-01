package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.interfacemanager.model.IM;

public class ProvSourceEntity extends Entry {

  public ProvSourceEntity() {
    this.addType(new TTIriRef(IM.PROVENANCE_SOURCE_ENTITY));
  }

  public TTIriRef getDerivationType() {
    return
      (TTIriRef) TTUtil.get(this, new TTIriRef(IM.DERIVATION_TYPE), TTIriRef.class);
  }

  @JsonSetter
  public ProvSourceEntity setDerivationType(TTIriRef derivationType) {
    set(new TTIriRef(IM.DERIVATION_TYPE), derivationType);
    return this;
  }

  public TTIriRef getEntityIdentifier() {
    return (TTIriRef)
      TTUtil.get(this, new TTIriRef(IM.ENTITY_IDENTIFIER), TTIriRef.class);
  }

  @JsonSetter
  public ProvSourceEntity setEntityIdentifier(TTIriRef entityIdentifier) {
    set(new TTIriRef(IM.ENTITY_IDENTIFIER), entityIdentifier);
    return this;
  }
}
