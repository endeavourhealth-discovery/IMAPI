package org.endeavourhealth.imapi.cdm;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.List;

/**
 * Class which sets and gets Provenance activity entry
 */
public class ProvActivity extends Entry {
	public ProvActivity(){
		this.addType(TTIriRef.iri(IM.NAMESPACE+"ProvenanceActivity"));
	}

	public ProvActivity setIri(String iri){
		super.setIri(iri);
		return this;
	}
	public TTIriRef getTargetEntity() {
		return Utils.getIriValue(this,"provenanceTarget");
	}

	public ProvActivity setTargetEntity(TTIriRef targetEntity) {
		Utils.setTriple(this,"provenanceTarget",targetEntity);
		return this;
	}

	public TTIriRef getActivityType() {
		return Utils.getIriValue(this,"activityType");
	}

	public ProvActivity setActivityType(TTIriRef activityType) {
		Utils.setTriple(this,"activityType",activityType);
		return this;
	}

	public String getEffectiveDate() {
		return Utils.getStringValue(this,"effectiveDate");
	}

	public ProvActivity setEffectiveDate(String effectiveDate) {
		Utils.setTriple(this,"effectiveDate",TTLiteral.literal(effectiveDate));
		return this;
	}

	public String getStartTime() {
		return Utils.getStringValue(this,"startTime");
	}

	public ProvActivity setStartTime(String startTime) {
		Utils.setTriple(this,"startTime",TTLiteral.literal(startTime));
		return this;
	}

	public List<TTIriRef> getAgent() {
		return Utils.getIriList(this,"provenanceAgent");
	}

	public ProvActivity setAgent(List<TTValue> agent) {
		Utils.setTriple(this,"provenanceAgent",agent);
		return this;
	}

	public ProvActivity addAgent(TTValue agent){
		Utils.addValue(this,"provenanceAgent",agent);
		return this;
	}

	public List<TTIriRef> getUsed() {
		return Utils.getIriList(this,"usedEntity");
	}

	public ProvActivity setUsed(List<TTValue> used) {
		Utils.setTriple(this,"usedEntity",used);
		return this;
	}

	public ProvActivity addUsed(TTValue used){
		Utils.addValue(this,"usedEntity",used);
		return this;
	}
}
