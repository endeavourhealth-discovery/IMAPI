package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;

public class ProvSourceEntity extends Entry {

  public ProvSourceEntity() {
    this.addType(new TTIriRefExtended(ImVocab. PROVENANCE_SOURCE_ENTITY));
  }

  public TTIriRefExtended getDerivationType() {
    return
      (TTIriRefExtended) TTUtil.get(this, new TTIriRefExtended(ImVocab. DERIVATION_TYPE), TTIriRefExtended.class);
  }

  @JsonSetter
  public ProvSourceEntity setDerivationType(TTIriRefExtended derivationType) {
    set(new TTIriRefExtended(ImVocab. DERIVATION_TYPE),derivationType);
    return this;
  }

  public TTIriRefExtended getEntityIdentifier() {
    return (TTIriRefExtended)
      TTUtil.get(this, new TTIriRefExtended(ImVocab. ENTITY_IDENTIFIER), TTIriRefExtended.class);
  }

  @JsonSetter
  public ProvSourceEntity setEntityIdentifier(TTIriRefExtended entityIdentifier) {
    set(new TTIriRefExtended(ImVocab. ENTITY_IDENTIFIER),entityIdentifier);
    return this;
  }
}
