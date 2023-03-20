package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents gp registration episode.
* A period of registration with a general practice
*/
public class GpRegistrationEpisode extends IMDMBase<GpRegistrationEpisode> {


	/**
	* Gp registration episode constructor with identifier
	*/
	public GpRegistrationEpisode(UUID id) {
		super("GpRegistrationEpisode", id);
	}

	/**
	* Gets the patient type of this gp registration episode
	* @return patientType
	*/
	public String getPatientType() {
		return getProperty("patientType");
	}


	/**
	* Changes the patient type of this GpRegistrationEpisode
	* @param patientType The new patient type to set
	* @return GpRegistrationEpisode
	*/
	public GpRegistrationEpisode setPatientType(String patientType) {
		setProperty("patientType", patientType);
		return this;
	}


	/**
	* Gets the record owner of this gp registration episode
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this GpRegistrationEpisode
	* @param recordOwner The new record owner to set
	* @return GpRegistrationEpisode
	*/
	public GpRegistrationEpisode setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the start date of this gp registration episode
	* @return startDate
	*/
	public PartialDateTime getStartDate() {
		return getProperty("startDate");
	}


	/**
	* Changes the start date of this GpRegistrationEpisode
	* @param startDate The new start date to set
	* @return GpRegistrationEpisode
	*/
	public GpRegistrationEpisode setStartDate(PartialDateTime startDate) {
		setProperty("startDate", startDate);
		return this;
	}


	/**
	* Gets the patient of this gp registration episode
	* @return patient
	*/
	public UUID getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this GpRegistrationEpisode
	* @param patient The new patient to set
	* @return GpRegistrationEpisode
	*/
	public GpRegistrationEpisode setPatient(UUID patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the end date of this gp registration episode
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this GpRegistrationEpisode
	* @param endDate The new end date to set
	* @return GpRegistrationEpisode
	*/
	public GpRegistrationEpisode setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}


	/**
	* Gets the usual GP of this gp registration episode
	* @return usualGp
	*/
	public UUID getUsualGp() {
		return getProperty("usualGp");
	}


	/**
	* Changes the usual GP of this GpRegistrationEpisode
	* @param usualGp The new usual GP to set
	* @return GpRegistrationEpisode
	*/
	public GpRegistrationEpisode setUsualGp(UUID usualGp) {
		setProperty("usualGp", usualGp);
		return this;
	}
}

