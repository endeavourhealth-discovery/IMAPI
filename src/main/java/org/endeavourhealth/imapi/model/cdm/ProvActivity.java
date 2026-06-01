package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.IM;
import org.endeavourhealth.interfacemanager.model.NAMESPACE;

import java.util.List;

/**
 * Class which sets and gets Provenance activity entry
 */
@Slf4j
public class ProvActivity extends Entry {

  public ProvActivity() {
    this.addType(new TTIriRef(IM.PROVENANCE_ACTIVITY));
    this.setScheme(new TTIriRef(NAMESPACE.IM));
  }

  @Override
  public ProvActivity setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public TTIriRef getTargetEntity() {
    return get(new TTIriRef(IM.PROVENANCE_TARGET)) == null ? null :
      get(new TTIriRef(IM.PROVENANCE_TARGET)).asIriRef();
  }

  @JsonSetter
  public ProvActivity setTargetEntity(TTIriRef targetEntity) {
    set(new TTIriRef(IM.PROVENANCE_TARGET), targetEntity);
    return this;
  }

  public TTIriRef getActivityType() {
    return get(new TTIriRef(IM.PROVENANCE_ACTIVITY_TYPE)) == null ? null :
      get(new TTIriRef(IM.PROVENANCE_ACTIVITY_TYPE)).asIriRef();
  }

  @JsonSetter
  public ProvActivity setActivityType(TTIriRef activityType) {
    set(new TTIriRef(IM.PROVENANCE_ACTIVITY_TYPE), activityType);
    return this;
  }

  public String getEffectiveDate() {
    return get(new TTIriRef(IM.EFFECTIVE_DATE)) == null ? null :
      get(new TTIriRef(IM.EFFECTIVE_DATE)).asLiteral().getValue();

  }

  public ProvActivity setEffectiveDate(String effectiveDate) {
    set(new TTIriRef(IM.EFFECTIVE_DATE), TTLiteral.literal(effectiveDate));
    return this;
  }

  public String getStartTime() {
    return (String) TTUtil.get(this, new TTIriRef(IM.START_TIME), String.class);
  }

  public ProvActivity setStartTime(String startTime) {
    set(new TTIriRef(IM.START_TIME), TTLiteral.literal(startTime));
    return this;
  }

  public List<TTIriRef> getAgent() {
    return TTUtil.getIriList(this, EnumUtils.asIri(IM.PROVENANCE_AGENT));
  }

  public ProvActivity setAgent(TTArray agent) {
    set(new TTIriRef(IM.PROVENANCE_AGENT), agent);
    return this;
  }

  public ProvActivity addAgent(TTValue agent) {
    TTUtil.add(this, new TTIriRef(IM.PROVENANCE_AGENT), agent);
    return this;
  }

  public List<TTIriRef> getUsed() {
    return TTUtil.getIriList(this, EnumUtils.asIri(IM.PROVENANCE_USED));
  }

  public ProvActivity setUsed(TTArray used) {
    set(new TTIriRef(IM.PROVENANCE_USED), used);
    return this;
  }

  public ProvActivity addUsed(TTIriRef used) {
    TTUtil.add(this, new TTIriRef(IM.PROVENANCE_USED), used);
    return this;
  }
}
