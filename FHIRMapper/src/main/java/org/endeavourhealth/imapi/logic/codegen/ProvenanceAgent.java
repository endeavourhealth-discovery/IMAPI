package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents provenance agent
* The agent associated with a provenance activity
*/
public class ProvenanceAgent extends IMDMBase<ProvenanceAgent> {


	/**
	* Provenance agent constructor 
	*/
	public ProvenanceAgent() {
		super("ProvenanceAgent");
	}

	/**
	* Provenance agent constructor with identifier
	*/
	public ProvenanceAgent(String id) {
		super("ProvenanceAgent", id);
	}

	/**
	* Gets the participation type of this provenance agent
	* @return participationType
	*/
	public String getParticipationType() {
		return getProperty("participationType");
	}


	/**
	* Changes the participation type of this ProvenanceAgent
	* @param participationType The new participation type to set
	* @return ProvenanceAgent
	*/
	public ProvenanceAgent setParticipationType(String participationType) {
		setProperty("participationType", participationType);
		return this;
	}


	/**
	* Gets the is person in role of this provenance agent
	* @return isPersonInRole
	*/
	public PractitionerInRole getIsPersonInRole() {
		return getProperty("isPersonInRole");
	}


	/**
	* Changes the is person in role of this ProvenanceAgent
	* @param isPersonInRole The new is person in role to set
	* @return ProvenanceAgent
	*/
	public ProvenanceAgent setIsPersonInRole(PractitionerInRole isPersonInRole) {
		setProperty("isPersonInRole", isPersonInRole);
		return this;
	}
}

