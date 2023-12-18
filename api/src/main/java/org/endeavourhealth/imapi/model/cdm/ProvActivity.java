package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;
import org.endeavourhealth.imapi.vocabulary.im.GRAPH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Class which sets and gets Provenance activity entry
 */
public class ProvActivity extends Entry {
    private static final Logger LOG = LoggerFactory.getLogger(ProvActivity.class);
	public ProvActivity(){
		this.addType(IM.PROVENANCE_ACTIVITY.asTTIriRef());
        super.setGraph(GRAPH.PROV.asTTIriRef());
	}

    @Override
	@JsonSetter
    public ProvActivity setGraph(TTIriRef graph) {
        LOG.error("Attempt to set graph on provenance");
        return this;
    }
	@Override
	public ProvActivity setGraph(Vocabulary graph) {
		LOG.error("Attempt to set graph on provenance");
		return this;
	}

    @Override
	public ProvActivity setIri(String iri){
		super.setIri(iri);
		return this;
	}
	public TTIriRef getTargetEntity() {
		return get(IM.PROVENANCE_TARGET.asTTIriRef())==null ?null :
			get(IM.PROVENANCE_TARGET.asTTIriRef()).asIriRef();
	}

	@JsonSetter
	public ProvActivity setTargetEntity(TTIriRef targetEntity) {
		set(IM.PROVENANCE_TARGET.asTTIriRef(),targetEntity);
		return this;
	}
	public ProvActivity setTargetEntity(Vocabulary targetEntity) {
		return setTargetEntity(targetEntity.asTTIriRef());
	}

	public TTIriRef getActivityType() {
		return get(IM.PROVENANCE_ACTIVITY_TYPE.asTTIriRef())==null ? null :
			get(IM.PROVENANCE_ACTIVITY_TYPE.asTTIriRef()).asIriRef();
	}

	@JsonSetter
	public ProvActivity setActivityType(TTIriRef activityType) {
		set(IM.PROVENANCE_ACTIVITY_TYPE.asTTIriRef(),activityType);
		return this;
	}
	public ProvActivity setActivityType(Vocabulary activityType) {
		return setActivityType(activityType.asTTIriRef());
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
		return TTUtil.getList(this,IM.PROVENANCE_AGENT.asTTIriRef(),TTIriRef.class);
	}

	public ProvActivity setAgent(TTArray agent) {
		set(IM.PROVENANCE_AGENT.asTTIriRef(),agent);
		return this;
	}

	public ProvActivity addAgent(TTValue agent){
		TTUtil.add(this,IM.PROVENANCE_AGENT.asTTIriRef(),agent);
		return this;
	}

	public List<TTIriRef> getUsed() {
		return TTUtil.getList(this,IM.PROVENANCE_USED.asTTIriRef(),TTIriRef.class);
	}

	public ProvActivity setUsed(TTArray used) {
		set(IM.PROVENANCE_USED.asTTIriRef(),used);
		return this;
	}

	public ProvActivity addUsed(TTIriRef used){
		TTUtil.add(this,IM.PROVENANCE_USED.asTTIriRef(),used);
		return this;
	}
	public ProvActivity addUsed(Vocabulary used){
		return addUsed(used.asTTIriRef());
	}
}
