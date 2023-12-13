package org.endeavourhealth.imapi.model.cdm;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Class which sets and gets Provenance activity entry
 */
public class ProvActivity extends Entry {
    private static final Logger LOG = LoggerFactory.getLogger(ProvActivity.class);
	public ProvActivity(){
		this.addType(IM.PROV_ACIVITY.asTTIriRef());
        super.setGraph(IM.GRAPH_PROV.asTTIriRef());
	}

    @Override
    public ProvActivity setGraph(TTIriRef graph) {
        LOG.error("Attempt to set graph on provenance");
        return this;
    }

    @Override
	public ProvActivity setIri(String iri){
		super.setIri(iri);
		return this;
	}
	public TTIriRef getTargetEntity() {
		return get(IM.PROV_TARGET.asTTIriRef())==null ?null :
			get(IM.PROV_TARGET.asTTIriRef()).asIriRef();
	}

	public ProvActivity setTargetEntity(TTIriRef targetEntity) {
		set(IM.PROV_TARGET.asTTIriRef(),targetEntity);
		return this;
	}

	public TTIriRef getActivityType() {
		return get(IM.PROV_ACIVITY_TYPE.asTTIriRef())==null ? null :
			get(IM.PROV_ACIVITY_TYPE.asTTIriRef()).asIriRef();
	}

	public ProvActivity setActivityType(TTIriRef activityType) {
		set(IM.PROV_ACIVITY_TYPE.asTTIriRef(),activityType);
		return this;
	}

	public String getEffectiveDate() {
		return get(IM.EFFECTIVE_DATE.asTTIriRef())==null ? null:
			get(IM.EFFECTIVE_DATE.asTTIriRef()).asLiteral().getValue();

	}

	public ProvActivity setEffectiveDate(String effectiveDate) {
		set(IM.EFFECTIVE_DATE.asTTIriRef(), TTLiteral.literal(effectiveDate));
		return this;
	}

	public String getStartTime() {
		return (String) TTUtil.get(this,IM.START_TIME.asTTIriRef(),String.class);
	}

	public ProvActivity setStartTime(String startTime) {
		set(IM.START_TIME.asTTIriRef(),TTLiteral.literal(startTime));
		return this;
	}

	public List<TTIriRef> getAgent() {
		return TTUtil.getList(this,IM.PROV_AGENT.asTTIriRef(),TTIriRef.class);
	}

	public ProvActivity setAgent(TTArray agent) {
		set(IM.PROV_AGENT.asTTIriRef(),agent);
		return this;
	}

	public ProvActivity addAgent(TTValue agent){
		TTUtil.add(this,IM.PROV_AGENT.asTTIriRef(),agent);
		return this;
	}

	public List<TTIriRef> getUsed() {
		return TTUtil.getList(this,IM.PROV_USED.asTTIriRef(),TTIriRef.class);
	}

	public ProvActivity setUsed(TTArray used) {
		set(IM.PROV_USED.asTTIriRef(),used);
		return this;
	}

	public ProvActivity addUsed(TTIriRef used){
		TTUtil.add(this,IM.PROV_USED.asTTIriRef(),used);
		return this;
	}
}
