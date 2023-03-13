package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents consultation
* An consultation whereby a health professional consults with the patient or person or persons representing the patient such as a third party or team
*/
public class Consultation extends IMDMBase<Consultation> {


	/**
	* Consultation constructor 
	*/
	public Consultation() {
		super("Consultation");
	}

	/**
	* Consultation constructor with identifier
	*/
	public Consultation(String id) {
		super("Consultation", id);
	}

	/**
	* Gets the record owner of this consultation
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this Consultation
	* @param recordOwner The new record owner to set
	* @return Consultation
	*/
	public Consultation setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the provider of this consultation
	* @return provider
	*/
	public Organisation getProvider() {
		return getProperty("provider");
	}


	/**
	* Changes the provider of this Consultation
	* @param provider The new provider to set
	* @return Consultation
	*/
	public Consultation setProvider(Organisation provider) {
		setProperty("provider", provider);
		return this;
	}


	/**
	* Gets the text of this consultation
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this Consultation
	* @param text The new text to set
	* @return Consultation
	*/
	public Consultation setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the location of this consultation
	* @return location
	*/
	public Location getLocation() {
		return getProperty("location");
	}


	/**
	* Changes the location of this Consultation
	* @param location The new location to set
	* @return Consultation
	*/
	public Consultation setLocation(Location location) {
		setProperty("location", location);
		return this;
	}


	/**
	* Gets the patient of this consultation
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this Consultation
	* @param patient The new patient to set
	* @return Consultation
	*/
	public Consultation setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the concept of this consultation
	* @return concept
	*/
	public TerminologyConcept getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this Consultation
	* @param concept The new concept to set
	* @return Consultation
	*/
	public Consultation setConcept(TerminologyConcept concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the original concept of this consultation
	* @return originalConcept
	*/
	public TerminologyConcept getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this Consultation
	* @param originalConcept The new original concept to set
	* @return Consultation
	*/
	public Consultation setOriginalConcept(TerminologyConcept originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this consultation
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this Consultation
	* @param effectiveDate The new effective date to set
	* @return Consultation
	*/
	public Consultation setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date of this consultation
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this Consultation
	* @param endDate The new end date to set
	* @return Consultation
	*/
	public Consultation setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

