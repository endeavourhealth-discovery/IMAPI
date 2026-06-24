package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.interfacemanager.model.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;

public class ProvSourceEntity extends Entry {

  public ProvSourceEntity() {
    this.addType(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROVENANCE_SOURCE_ENTITY));
  }

  public TTIriRef getDerivationType() {
    return
      (TTIriRef) TTUtil.get(this, TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DERIVATION_TYPE), TTIriRef.class);
  }

  @JsonSetter
  public ProvSourceEntity setDerivationType(TTIriRef derivationType) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DERIVATION_TYPE), derivationType);
    return this;
  }

  public TTIriRef getEntityIdentifier() {
    return (TTIriRef)
      TTUtil.get(this, TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ENTITY_IDENTIFIER), TTIriRef.class);
  }

  @JsonSetter
  public ProvSourceEntity setEntityIdentifier(TTIriRef entityIdentifier) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ENTITY_IDENTIFIER), entityIdentifier);
    return this;
  }
}
