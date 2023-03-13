package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents provenance activity
* In order to have generated some data, or changed some data, or deleted some data, some form of activity has taken place. This entity holds the nature of the activity that took place, points to the resulting entity and any used entities  and the date and time it took place. Provenance can be illustrated by providing a timeline of all linked activities, operating as a chain going back in time.<p>Activities would normally be implemented using activity subtypes
*/
public class ProvenanceActivity extends IMDMBase<ProvenanceActivity> {


	/**
	* Provenance activity constructor 
	*/
	public ProvenanceActivity() {
		super("ProvenanceActivity");
	}

	/**
	* Provenance activity constructor with identifier
	*/
	public ProvenanceActivity(String id) {
		super("ProvenanceActivity", id);
	}

	/**
	* Gets the used entity of this provenance activity
	* @return usedEntity
	*/
	public ProvenanceSourceEntity getUsedEntity() {
		return getProperty("usedEntity");
	}


	/**
	* Changes the used entity of this ProvenanceActivity
	* @param usedEntity The new used entity to set
	* @return ProvenanceActivity
	*/
	public ProvenanceActivity setUsedEntity(ProvenanceSourceEntity usedEntity) {
		setProperty("usedEntity", usedEntity);
		return this;
	}


	/**
	* Gets the provenance agent of this provenance activity
	* @return provenanceAgent
	*/
	public ProvenanceAgent getProvenanceAgent() {
		return getProperty("provenanceAgent");
	}


	/**
	* Changes the provenance agent of this ProvenanceActivity
	* @param provenanceAgent The new provenance agent to set
	* @return ProvenanceActivity
	*/
	public ProvenanceActivity setProvenanceAgent(ProvenanceAgent provenanceAgent) {
		setProperty("provenanceAgent", provenanceAgent);
		return this;
	}


	/**
	* Gets the effective date of this provenance activity
	* @return effectiveDate
	*/
	public String getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this ProvenanceActivity
	* @param effectiveDate The new effective date to set
	* @return ProvenanceActivity
	*/
	public ProvenanceActivity setEffectiveDate(String effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the start time of this provenance activity
	* @return startTime
	*/
	public String getStartTime() {
		return getProperty("startTime");
	}


	/**
	* Changes the start time of this ProvenanceActivity
	* @param startTime The new start time to set
	* @return ProvenanceActivity
	*/
	public ProvenanceActivity setStartTime(String startTime) {
		setProperty("startTime", startTime);
		return this;
	}


	/**
	* Gets the provenance target entity of this provenance activity
	* @return provenanceTargetEntity
	*/
	public String getProvenanceTargetEntity() {
		return getProperty("provenanceTargetEntity");
	}


	/**
	* Changes the provenance target entity of this ProvenanceActivity
	* @param provenanceTargetEntity The new provenance target entity to set
	* @return ProvenanceActivity
	*/
	public ProvenanceActivity setProvenanceTargetEntity(String provenanceTargetEntity) {
		setProperty("provenanceTargetEntity", provenanceTargetEntity);
		return this;
	}


	/**
	* Gets the activity type of this provenance activity
	* @return activityType
	*/
	public String getActivityType() {
		return getProperty("activityType");
	}


	/**
	* Changes the activity type of this ProvenanceActivity
	* @param activityType The new activity type to set
	* @return ProvenanceActivity
	*/
	public ProvenanceActivity setActivityType(String activityType) {
		setProperty("activityType", activityType);
		return this;
	}
}

