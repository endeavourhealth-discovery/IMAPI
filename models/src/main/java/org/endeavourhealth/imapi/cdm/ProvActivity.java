package org.endeavourhealth.imapi.cdm;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.List;

/**
 * Class which sets and gets Provenance activity entry
 */
public class ProvActivity extends Entry {

	public ProvActivity(){
		this.addType(IM.PROV_ACIVITY);
	}

	@Override
	public ProvActivity setIri(String iri){
		super.setIri(iri);
		return this;
	}
	public TTIriRef getTargetEntity() {
		return get(IM.PROV_TARGET)==null ?null :
			get(IM.PROV_TARGET).asIriRef();
	}

	public ProvActivity setTargetEntity(TTIriRef targetEntity) {
		set(IM.PROV_TARGET,targetEntity);
		return this;
	}

	public TTIriRef getActivityType() {
		return get(IM.PROV_ACIVITY_TYPE)==null ? null :
			get(IM.PROV_ACIVITY_TYPE).asIriRef();
	}

	public ProvActivity setActivityType(TTIriRef activityType) {
		set(IM.PROV_ACIVITY_TYPE,activityType);
		return this;
	}

	public String getEffectiveDate() {
		return get(IM.EFFECTIVE_DATE)==null ? null:
			get(IM.EFFECTIVE_DATE).asLiteral().getValue();

	}

	public ProvActivity setEffectiveDate(String effectiveDate) {
		set(IM.EFFECTIVE_DATE,TTLiteral.literal(effectiveDate));
		return this;
	}

	public String getStartTime() {
		return (String) TTUtil.get(this,IM.START_TIME,String.class);
	}

	public ProvActivity setStartTime(String startTime) {
		set(IM.START_TIME,TTLiteral.literal(startTime));
		return this;
	}

	public List<TTIriRef> getAgent() {
		return TTUtil.getList(this,IM.PROV_AGENT,TTIriRef.class);
	}

	public ProvActivity setAgent(TTArray agent) {
		set(IM.PROV_AGENT,agent);
		return this;
	}

	public ProvActivity addAgent(TTValue agent){
		TTUtil.add(this,IM.PROV_AGENT,agent);
		return this;
	}

	public List<TTIriRef> getUsed() {
		return TTUtil.getList(this,IM.PROV_USED,TTIriRef.class);
	}

	public ProvActivity setUsed(TTArray used) {
		set(IM.PROV_USED,used);
		return this;
	}

	public ProvActivity addUsed(TTIriRef used){
		TTUtil.add(this,IM.PROV_USED,used);
		return this;
	}
}
