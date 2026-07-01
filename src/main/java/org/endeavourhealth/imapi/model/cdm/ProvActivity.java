package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.model.tripletree.TTArrayJava;
import org.endeavourhealth.imapi.model.tripletree.TTLiteralJava;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.model.tripletree.TTValueJava;
import org.endeavourhealth.imapi.utility.EnumUtils;

import java.util.List;

/**
 * Class which sets and gets Provenance activity entry
 */
@Slf4j
public class ProvActivity extends Entry {

  public ProvActivity() {
    this.addType(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROVENANCE_ACTIVITY));
    this.setScheme(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.IM));
  }

  @Override
  public ProvActivity setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public TTIriRef getTargetEntity() {
    return get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROVENANCE_TARGET)) == null ? null :
      get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROVENANCE_TARGET)).asIriRef();
  }

  @JsonSetter
  public ProvActivity setTargetEntity(TTIriRef targetEntity) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROVENANCE_TARGET), targetEntity);
    return this;
  }

  public TTIriRef getActivityType() {
    return get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROVENANCE_ACTIVITY_TYPE)) == null ? null :
      get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROVENANCE_ACTIVITY_TYPE)).asIriRef();
  }

  @JsonSetter
  public ProvActivity setActivityType(TTIriRef activityType) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROVENANCE_ACTIVITY_TYPE), activityType);
    return this;
  }

  public String getEffectiveDate() {
    return get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.EFFECTIVE_DATE)) == null ? null :
      get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.EFFECTIVE_DATE)).asLiteral().getValue();

  }

  public ProvActivity setEffectiveDate(String effectiveDate) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.EFFECTIVE_DATE), TTLiteralJava.literal(effectiveDate));
    return this;
  }

  public String getStartTime() {
    return (String) TTUtil.get(this, TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.START_TIME), String.class);
  }

  public ProvActivity setStartTime(String startTime) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.START_TIME), TTLiteralJava.literal(startTime));
    return this;
  }

  public List<TTIriRef> getAgent() {
    return TTUtil.getIriList(this, EnumUtils.asIri(ImVocab.PROVENANCE_AGENT));
  }

  public ProvActivity setAgent(TTArrayJava agent) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROVENANCE_AGENT), agent);
    return this;
  }

  public ProvActivity addAgent(TTValueJava agent) {
    TTUtil.add(this, TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROVENANCE_AGENT), agent);
    return this;
  }

  public List<TTIriRef> getUsed() {
    return TTUtil.getIriList(this, EnumUtils.asIri(ImVocab.PROVENANCE_USED));
  }

  public ProvActivity setUsed(TTArrayJava used) {
    set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROVENANCE_USED), used);
    return this;
  }

  public ProvActivity addUsed(TTIriRef used) {
    TTUtil.add(this, TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROVENANCE_USED), used);
    return this;
  }
}
