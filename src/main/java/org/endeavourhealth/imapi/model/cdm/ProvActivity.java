package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.utility.EnumUtils;

import java.util.List;

/**
 * Class which sets and gets Provenance activity entry
 */
@Slf4j
public class ProvActivity extends Entry {

  public ProvActivity() {
    this.addType(new TTIriRefExtended(ImVocab.PROVENANCE_ACTIVITY));
    this.setScheme(new TTIriRefExtended(NamespaceVocab. IM));
  }

  @Override
  public ProvActivity setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public TTIriRefExtended getTargetEntity() {
    return get(new TTIriRefExtended(ImVocab.PROVENANCE_TARGET)) == null ? null :
      get(new TTIriRefExtended(ImVocab.PROVENANCE_TARGET)).asIriRef();
  }

  @JsonSetter
  public ProvActivity setTargetEntity(TTIriRefExtended targetEntity) {
    set(new TTIriRefExtended(ImVocab.PROVENANCE_TARGET), targetEntity);
    return this;
  }

  public TTIriRefExtended getActivityType() {
    return get(new TTIriRefExtended(ImVocab.PROVENANCE_ACTIVITY_TYPE)) == null ? null :
      get(new TTIriRefExtended(ImVocab.PROVENANCE_ACTIVITY_TYPE)).asIriRef();
  }

  @JsonSetter
  public ProvActivity setActivityType(TTIriRefExtended activityType) {
    set(new TTIriRefExtended(ImVocab.PROVENANCE_ACTIVITY_TYPE), activityType);
    return this;
  }

  public String getEffectiveDate() {
    return get(new TTIriRefExtended(ImVocab.EFFECTIVE_DATE)) == null ? null :
      get(new TTIriRefExtended(ImVocab.EFFECTIVE_DATE)).asLiteral().getValue();

  }

  public ProvActivity setEffectiveDate(String effectiveDate) {
    set(new TTIriRefExtended(ImVocab.EFFECTIVE_DATE), TTLiteral.literal(effectiveDate));
    return this;
  }

  public String getStartTime() {
    return (String) TTUtil.get(this, new TTIriRefExtended(ImVocab.START_TIME), String.class);
  }

  public ProvActivity setStartTime(String startTime) {
    set(new TTIriRefExtended(ImVocab.START_TIME), TTLiteral.literal(startTime));
    return this;
  }

  public List<TTIriRefExtended> getAgent() {
    return TTUtil.getIriList(this, EnumUtils.asIri(ImVocab.PROVENANCE_AGENT));
  }

  public ProvActivity setAgent(TTArray agent) {
    set(new TTIriRefExtended(ImVocab.PROVENANCE_AGENT), agent);
    return this;
  }

  public ProvActivity addAgent(TTValue agent) {
    TTUtil.add(this, new TTIriRefExtended(ImVocab.PROVENANCE_AGENT), agent);
    return this;
  }

  public List<TTIriRefExtended> getUsed() {
    return TTUtil.getIriList(this, EnumUtils.asIri(ImVocab.PROVENANCE_USED));
  }

  public ProvActivity setUsed(TTArray used) {
    set(new TTIriRefExtended(ImVocab.PROVENANCE_USED), used);
    return this;
  }

  public ProvActivity addUsed(TTIriRefExtended used) {
    TTUtil.add(this, new TTIriRefExtended(ImVocab.PROVENANCE_USED), used);
    return this;
  }
}
