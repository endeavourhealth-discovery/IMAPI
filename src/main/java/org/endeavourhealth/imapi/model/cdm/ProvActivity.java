package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.List;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

/**
 * Class which sets and gets Provenance activity entry
 */
@Slf4j
public class ProvActivity extends Entry {

  public ProvActivity() {
    this.addType(iri(IM.PROVENANCE_ACTIVITY));
  }

  @Override
  public ProvActivity setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public TTIriRef getTargetEntity() {
    return get(iri(IM.PROVENANCE_TARGET)) == null ? null :
      get(iri(IM.PROVENANCE_TARGET)).asIriRef();
  }

  @JsonSetter
  public ProvActivity setTargetEntity(TTIriRef targetEntity) {
    set(iri(IM.PROVENANCE_TARGET), targetEntity);
    return this;
  }

  public TTIriRef getActivityType() {
    return get(iri(IM.PROVENANCE_ACTIVITY_TYPE)) == null ? null :
      get(iri(IM.PROVENANCE_ACTIVITY_TYPE)).asIriRef();
  }

  @JsonSetter
  public ProvActivity setActivityType(TTIriRef activityType) {
    set(iri(IM.PROVENANCE_ACTIVITY_TYPE), activityType);
    return this;
  }

  public String getEffectiveDate() {
    return get(iri(IM.EFFECTIVE_DATE)) == null ? null :
      get(iri(IM.EFFECTIVE_DATE)).asLiteral().getValue();

  }

  public ProvActivity setEffectiveDate(String effectiveDate) {
    set(iri(IM.EFFECTIVE_DATE), TTLiteral.literal(effectiveDate));
    return this;
  }

  public String getStartTime() {
    return (String) TTUtil.get(this, iri(IM.START_TIME), String.class);
  }

  public ProvActivity setStartTime(String startTime) {
    set(iri(IM.START_TIME), TTLiteral.literal(startTime));
    return this;
  }

  public List<TTIriRef> getAgent() {
    return TTUtil.getList(this, iri(IM.PROVENANCE_AGENT), TTIriRef.class);
  }

  public ProvActivity setAgent(TTArray agent) {
    set(iri(IM.PROVENANCE_AGENT), agent);
    return this;
  }

  public ProvActivity addAgent(TTValue agent) {
    TTUtil.add(this, iri(IM.PROVENANCE_AGENT), agent);
    return this;
  }

  public List<TTIriRef> getUsed() {
    return TTUtil.getList(this, iri(IM.PROVENANCE_USED), TTIriRef.class);
  }

  public ProvActivity setUsed(TTArray used) {
    set(iri(IM.PROVENANCE_USED), used);
    return this;
  }

  public ProvActivity addUsed(TTIriRef used) {
    TTUtil.add(this, iri(IM.PROVENANCE_USED), used);
    return this;
  }
}
