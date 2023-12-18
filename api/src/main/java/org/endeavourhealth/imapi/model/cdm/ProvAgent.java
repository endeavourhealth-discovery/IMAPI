package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;

public class ProvAgent extends Entry {

	public ProvAgent(){
		this.addType(IM.PROVENANCE_AGENT);
	}


	public TTIriRef getParticipationType() {

		return (TTIriRef) TTUtil.get(this,IM.PARTICIPATION_TYPE,TTIriRef.class);
	}

	@JsonSetter
	public ProvAgent setParticipationType(TTIriRef participationType) {
		set(IM.PARTICIPATION_TYPE,participationType);
		return this;
	}
	public ProvAgent setParticipationType(Vocabulary participationType) {
		return setParticipationType(participationType.asTTIriRef());
	}

	public TTIriRef getPersonInRole() {
		return
			(TTIriRef) TTUtil.get(this,IM.PERSON_IN_ROLE,TTIriRef.class);
	}

	@JsonSetter
	public ProvAgent setPersonInRole(TTIriRef personInRole) {
		set(IM.PERSON_IN_ROLE,personInRole);
		return this;
	}

	public ProvAgent setPersonInRole(Vocabulary personInRole) {
		return setPersonInRole(personInRole.asTTIriRef());
	}
}
