package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents specimen.
* A specimen definition that is part of a diagnostic report
*/
public class Specimen extends IMDMBase<Specimen> {


	/**
	* Specimen constructor with identifier
	*/
	public Specimen(UUID id) {
		super("Specimen", id);
	}

	/**
	* Gets the container of this specimen
	* @return container
	*/
	public String getContainer() {
		return getProperty("container");
	}


	/**
	* Changes the container of this Specimen
	* @param container The new container to set
	* @return Specimen
	*/
	public Specimen setContainer(String container) {
		setProperty("container", container);
		return this;
	}


	/**
	* Gets the is part of of this specimen
	* @return isPartOf
	*/
	public String getIsPartOf() {
		return getProperty("isPartOf");
	}


	/**
	* Changes the is part of of this Specimen
	* @param isPartOf The new is part of to set
	* @return Specimen
	*/
	public Specimen setIsPartOf(String isPartOf) {
		setProperty("isPartOf", isPartOf);
		return this;
	}


	/**
	* Gets the is part of encounter of this specimen
	* @return isPartOfEncounter
	*/
	public String getIsPartOfEncounter() {
		return getProperty("isPartOfEncounter");
	}


	/**
	* Changes the is part of encounter of this Specimen
	* @param isPartOfEncounter The new is part of encounter to set
	* @return Specimen
	*/
	public Specimen setIsPartOfEncounter(String isPartOfEncounter) {
		setProperty("isPartOfEncounter", isPartOfEncounter);
		return this;
	}


	/**
	* Gets the request of this specimen
	* @return request
	*/
	public UUID getRequest() {
		return getProperty("request");
	}


	/**
	* Changes the request of this Specimen
	* @param request The new request to set
	* @return Specimen
	*/
	public Specimen setRequest(UUID request) {
		setProperty("request", request);
		return this;
	}


	/**
	* Gets the fasting duration of this specimen
	* @return fastingDuration
	*/
	public String getFastingDuration() {
		return getProperty("fastingDuration");
	}


	/**
	* Changes the fasting duration of this Specimen
	* @param fastingDuration The new fasting duration to set
	* @return Specimen
	*/
	public Specimen setFastingDuration(String fastingDuration) {
		setProperty("fastingDuration", fastingDuration);
		return this;
	}


	/**
	* Gets the fasting status of this specimen
	* @return fastingStatus
	*/
	public String getFastingStatus() {
		return getProperty("fastingStatus");
	}


	/**
	* Changes the fasting status of this Specimen
	* @param fastingStatus The new fasting status to set
	* @return Specimen
	*/
	public Specimen setFastingStatus(String fastingStatus) {
		setProperty("fastingStatus", fastingStatus);
		return this;
	}


	/**
	* Gets the method of this specimen
	* @return method
	*/
	public String getMethod() {
		return getProperty("method");
	}


	/**
	* Changes the method of this Specimen
	* @param method The new method to set
	* @return Specimen
	*/
	public Specimen setMethod(String method) {
		setProperty("method", method);
		return this;
	}


	/**
	* Gets the specimen identifier of this specimen
	* @return specimenIdentifier
	*/
	public String getSpecimenIdentifier() {
		return getProperty("specimenIdentifier");
	}


	/**
	* Changes the specimen identifier of this Specimen
	* @param specimenIdentifier The new specimen identifier to set
	* @return Specimen
	*/
	public Specimen setSpecimenIdentifier(String specimenIdentifier) {
		setProperty("specimenIdentifier", specimenIdentifier);
		return this;
	}


	/**
	* Gets the specimen type of this specimen
	* @return specimenType
	*/
	public String getSpecimenType() {
		return getProperty("specimenType");
	}


	/**
	* Changes the specimen type of this Specimen
	* @param specimenType The new specimen type to set
	* @return Specimen
	*/
	public Specimen setSpecimenType(String specimenType) {
		setProperty("specimenType", specimenType);
		return this;
	}


	/**
	* Gets the specimen volume of this specimen
	* @return specimenVolume
	*/
	public String getSpecimenVolume() {
		return getProperty("specimenVolume");
	}


	/**
	* Changes the specimen volume of this Specimen
	* @param specimenVolume The new specimen volume to set
	* @return Specimen
	*/
	public Specimen setSpecimenVolume(String specimenVolume) {
		setProperty("specimenVolume", specimenVolume);
		return this;
	}


	/**
	* Gets the status of this specimen
	* @return status
	*/
	public String getStatus() {
		return getProperty("status");
	}


	/**
	* Changes the status of this Specimen
	* @param status The new status to set
	* @return Specimen
	*/
	public Specimen setStatus(String status) {
		setProperty("status", status);
		return this;
	}


	/**
	* Gets the collection time of this specimen
	* @return collectionTime
	*/
	public String getCollectionTime() {
		return getProperty("collectionTime");
	}


	/**
	* Changes the collection time of this Specimen
	* @param collectionTime The new collection time to set
	* @return Specimen
	*/
	public Specimen setCollectionTime(String collectionTime) {
		setProperty("collectionTime", collectionTime);
		return this;
	}


	/**
	* Gets the received time of this specimen
	* @return receivedTime
	*/
	public String getReceivedTime() {
		return getProperty("receivedTime");
	}


	/**
	* Changes the received time of this Specimen
	* @param receivedTime The new received time to set
	* @return Specimen
	*/
	public Specimen setReceivedTime(String receivedTime) {
		setProperty("receivedTime", receivedTime);
		return this;
	}
}

