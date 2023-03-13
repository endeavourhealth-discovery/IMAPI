package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents gp registration administration status history
* The set of gp registration statuses associated with a registration period with general practices
*/
public class GpRegistrationAdministrationStatusHistory extends IMDMBase<GpRegistrationAdministrationStatusHistory> {


	/**
	* Gp registration administration status history constructor 
	*/
	public GpRegistrationAdministrationStatusHistory() {
		super("GpRegistrationAdministrationStatusHistory");
	}

	/**
	* Gp registration administration status history constructor with identifier
	*/
	public GpRegistrationAdministrationStatusHistory(String id) {
		super("GpRegistrationAdministrationStatusHistory", id);
	}

	/**
	* Gets the registration administrative status of this gp registration administration status history
	* @return registrationAdministrativeStatus
	*/
	public String getRegistrationAdministrativeStatus() {
		return getProperty("registrationAdministrativeStatus");
	}


	/**
	* Changes the registration administrative status of this GpRegistrationAdministrationStatusHistory
	* @param registrationAdministrativeStatus The new registration administrative status to set
	* @return GpRegistrationAdministrationStatusHistory
	*/
	public GpRegistrationAdministrationStatusHistory setRegistrationAdministrativeStatus(String registrationAdministrativeStatus) {
		setProperty("registrationAdministrativeStatus", registrationAdministrativeStatus);
		return this;
	}


	/**
	* Gets the record owner of this gp registration administration status history
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this GpRegistrationAdministrationStatusHistory
	* @param recordOwner The new record owner to set
	* @return GpRegistrationAdministrationStatusHistory
	*/
	public GpRegistrationAdministrationStatusHistory setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the gp registration of this gp registration administration status history
	* @return gpRegistration
	*/
	public GpRegistrationEpisode getGpRegistration() {
		return getProperty("gpRegistration");
	}


	/**
	* Changes the gp registration of this GpRegistrationAdministrationStatusHistory
	* @param gpRegistration The new gp registration to set
	* @return GpRegistrationAdministrationStatusHistory
	*/
	public GpRegistrationAdministrationStatusHistory setGpRegistration(GpRegistrationEpisode gpRegistration) {
		setProperty("gpRegistration", gpRegistration);
		return this;
	}


	/**
	* Gets the text of this gp registration administration status history
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this GpRegistrationAdministrationStatusHistory
	* @param text The new text to set
	* @return GpRegistrationAdministrationStatusHistory
	*/
	public GpRegistrationAdministrationStatusHistory setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the patient of this gp registration administration status history
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this GpRegistrationAdministrationStatusHistory
	* @param patient The new patient to set
	* @return GpRegistrationAdministrationStatusHistory
	*/
	public GpRegistrationAdministrationStatusHistory setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the concept of this gp registration administration status history
	* @return concept
	*/
	public TerminologyConcept getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this GpRegistrationAdministrationStatusHistory
	* @param concept The new concept to set
	* @return GpRegistrationAdministrationStatusHistory
	*/
	public GpRegistrationAdministrationStatusHistory setConcept(TerminologyConcept concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the original concept of this gp registration administration status history
	* @return originalConcept
	*/
	public TerminologyConcept getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this GpRegistrationAdministrationStatusHistory
	* @param originalConcept The new original concept to set
	* @return GpRegistrationAdministrationStatusHistory
	*/
	public GpRegistrationAdministrationStatusHistory setOriginalConcept(TerminologyConcept originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this gp registration administration status history
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this GpRegistrationAdministrationStatusHistory
	* @param effectiveDate The new effective date to set
	* @return GpRegistrationAdministrationStatusHistory
	*/
	public GpRegistrationAdministrationStatusHistory setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date of this gp registration administration status history
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this GpRegistrationAdministrationStatusHistory
	* @param endDate The new end date to set
	* @return GpRegistrationAdministrationStatusHistory
	*/
	public GpRegistrationAdministrationStatusHistory setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

