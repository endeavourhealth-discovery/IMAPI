package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents gp current registration
* The status of a patient in relation to a registration episode at a point in time
*/
public class GpCurrentRegistration extends IMDMBase<GpCurrentRegistration> {


	/**
	* Gp current registration constructor 
	*/
	public GpCurrentRegistration() {
		super("GpCurrentRegistration");
	}

	/**
	* Gp current registration constructor with identifier
	*/
	public GpCurrentRegistration(String id) {
		super("GpCurrentRegistration", id);
	}

	/**
	* Gets the record owner of this gp current registration
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this GpCurrentRegistration
	* @param recordOwner The new record owner to set
	* @return GpCurrentRegistration
	*/
	public GpCurrentRegistration setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the usual GP of this gp current registration
	* @return usualGp
	*/
	public PractitionerInRole getUsualGp() {
		return getProperty("usualGp");
	}


	/**
	* Changes the usual GP of this GpCurrentRegistration
	* @param usualGp The new usual GP to set
	* @return GpCurrentRegistration
	*/
	public GpCurrentRegistration setUsualGp(PractitionerInRole usualGp) {
		setProperty("usualGp", usualGp);
		return this;
	}


	/**
	* Gets the GP patient registered practice of this gp current registration
	* @return gpPatientRegisteredPractice
	*/
	public Organisation getGpPatientRegisteredPractice() {
		return getProperty("gpPatientRegisteredPractice");
	}


	/**
	* Changes the GP patient registered practice of this GpCurrentRegistration
	* @param gpPatientRegisteredPractice The new GP patient registered practice to set
	* @return GpCurrentRegistration
	*/
	public GpCurrentRegistration setGpPatientRegisteredPractice(Organisation gpPatientRegisteredPractice) {
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

