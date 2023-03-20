package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents gp current registration.
* The status of a patient in relation to a registration episode at a point in time
*/
public class GpCurrentRegistration extends IMDMBase<GpCurrentRegistration> {


	/**
	* Gp current registration constructor with identifier
	*/
	public GpCurrentRegistration(UUID id) {
		super("GpCurrentRegistration", id);
	}

	/**
	* Gets the record owner of this gp current registration
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this GpCurrentRegistration
	* @param recordOwner The new record owner to set
	* @return GpCurrentRegistration
	*/
	public GpCurrentRegistration setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the usual GP of this gp current registration
	* @return usualGp
	*/
	public UUID getUsualGp() {
		return getProperty("usualGp");
	}


	/**
	* Changes the usual GP of this GpCurrentRegistration
	* @param usualGp The new usual GP to set
	* @return GpCurrentRegistration
	*/
	public GpCurrentRegistration setUsualGp(UUID usualGp) {
		setProperty("usualGp", usualGp);
		return this;
	}


	/**
	* Gets the GP patient registered practice of this gp current registration
	* @return gpPatientRegisteredPractice
	*/
	public UUID getGpPatientRegisteredPractice() {
		return getProperty("gpPatientRegisteredPractice");
	}


	/**
	* Changes the GP patient registered practice of this GpCurrentRegistration
	* @param gpPatientRegisteredPractice The new GP patient registered practice to set
	* @return GpCurrentRegistration
	*/
	public GpCurrentRegistration setGpPatientRegisteredPractice(UUID gpPatientRegisteredPractice) {
		setProperty("gpPatientRegisteredPractice", gpPatientRegisteredPractice);
		return this;
	}


	/**
	* Gets the GP patient type of this gp current registration
	* @return gpPatientType
	*/
	public String getGpPatientType() {
		return getProperty("gpPatientType");
	}


	/**
	* Changes the GP patient type of this GpCurrentRegistration
	* @param gpPatientType The new GP patient type to set
	* @return GpCurrentRegistration
	*/
	public GpCurrentRegistration setGpPatientType(String gpPatientType) {
		setProperty("gpPatientType", gpPatientType);
		return this;
	}


	/**
	* Gets the GP registered status of this gp current registration
	* @return gpRegisteredStatus
	*/
	public String getGpRegisteredStatus() {
		return getProperty("gpRegisteredStatus");
	}


	/**
	* Changes the GP registered status of this GpCurrentRegistration
	* @param gpRegisteredStatus The new GP registered status to set
	* @return GpCurrentRegistration
	*/
	public GpCurrentRegistration setGpRegisteredStatus(String gpRegisteredStatus) {
		setProperty("gpRegisteredStatus", gpRegisteredStatus);
		return this;
	}


	/**
	* Gets the GP patient registration date of this gp current registration
	* @return gpPatientRegistrationDate
	*/
	public PartialDateTime getGpPatientRegistrationDate() {
		return getProperty("gpPatientRegistrationDate");
	}


	/**
	* Changes the GP patient registration date of this GpCurrentRegistration
	* @param gpPatientRegistrationDate The new GP patient registration date to set
	* @return GpCurrentRegistration
	*/
	public GpCurrentRegistration setGpPatientRegistrationDate(PartialDateTime gpPatientRegistrationDate) {
		setProperty("gpPatientRegistrationDate", gpPatientRegistrationDate);
		return this;
	}
}

