package org.endeavourhealth.imapi.cdm;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

public class ProvAgent extends Entry {

	public ProvAgent(){
		this.addType(TTIriRef.iri(IM.NAMESPACE+"ProvenanceAgent"));
	}


	public TTIriRef getParticipationType() {
		return Utils.getIriValue(this,"participationType");
	}

	public ProvAgent setParticipationType(TTIriRef participationType) {
		Utils.setTriple(this,"participationType",participationType);
		return this;
	}

	public TTIriRef getPersonInRole() {
		return Utils.getIriValue(this,"personInRole");
	}

	public ProvAgent setPersonInRole(TTIriRef personInRole) {
		Utils.setTriple(this,"personInRole",personInRole);
		return this;
	}
}
