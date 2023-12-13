package org.endeavourhealth.imapi.model.cdm;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;

public class ProvAgent extends Entry {

	public ProvAgent(){
		this.addType(TTIriRef.iri(IM.NAMESPACE.iri+"ProvenanceAgent"));
	}


	public TTIriRef getParticipationType() {

		return (TTIriRef) TTUtil.get(this,TTIriRef.iri(IM.NAMESPACE.iri+"participationType"),TTIriRef.class);
	}

	public ProvAgent setParticipationType(TTIriRef participationType) {
		set(TTIriRef.iri(IM.NAMESPACE.iri+"participationType"),participationType);
		return this;
	}

	public TTIriRef getPersonInRole() {
		return
			(TTIriRef) TTUtil.get(this,TTIriRef.iri(IM.NAMESPACE.iri+"personInRole"),TTIriRef.class);
	}

	public ProvAgent setPersonInRole(TTIriRef personInRole) {
		set(TTIriRef.iri(IM.NAMESPACE.iri+"personInRole"),personInRole);
		return this;
	}
}
