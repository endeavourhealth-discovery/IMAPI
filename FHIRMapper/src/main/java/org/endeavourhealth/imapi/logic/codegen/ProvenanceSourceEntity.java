package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents provenance source entity
* Information about the source entity used in a provenance activity
*/
public class ProvenanceSourceEntity extends IMDMBase<ProvenanceSourceEntity> {


	/**
	* Provenance source entity constructor 
	*/
	public ProvenanceSourceEntity() {
		super("ProvenanceSourceEntity");
	}

	/**
	* Provenance source entity constructor with identifier
	*/
	public ProvenanceSourceEntity(String id) {
		super("ProvenanceSourceEntity", id);
	}

	/**
	* Gets the derivation type of this provenance source entity
	* @return derivationType
	*/
	public String getDerivationType() {
		return getProperty("derivationType");
	}


	/**
	* Changes the derivation type of this ProvenanceSourceEntity
	* @param derivationType The new derivation type to set
	* @return ProvenanceSourceEntity
	*/
	public ProvenanceSourceEntity setDerivationType(String derivationType) {
		setProperty("derivationType", derivationType);
		return this;
	}


	/**
	* Gets the entity identifier of this provenance source entity
	* @return entityIdentifier
	*/
	public String getEntityIdentifier() {
		return getProperty("entityIdentifier");
	}


	/**
	* Changes the entity identifier of this ProvenanceSourceEntity
	* @param entityIdentifier The new entity identifier to set
	* @return ProvenanceSourceEntity
	*/
	public ProvenanceSourceEntity setEntityIdentifier(String entityIdentifier) {
		setProperty("entityIdentifier", entityIdentifier);
		return this;
	}
}

