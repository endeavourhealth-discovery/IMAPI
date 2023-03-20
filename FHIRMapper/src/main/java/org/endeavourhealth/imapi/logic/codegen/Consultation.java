package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents consultation.
* An consultation whereby a health professional consults with the patient or person or persons representing the patient such as a third party or team
*/
public class Consultation extends IMDMBase<Consultation> {


	/**
	* Consultation constructor with identifier
	*/
	public Consultation(UUID id) {
		super("Consultation", id);
	}

	/**
	* Gets the record owner of this consultation
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this Consultation
	* @param recordOwner The new record owner to set
	* @return Consultation
	*/
	public Consultation setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the provider of this consultation
	* @return provider
	*/
	public UUID getProvider() {
		return getProperty("provider");
	}


	/**
	* Changes the provider of this Consultation
	* @param provider The new provider to set
	* @return Consultation
	*/
	public Consultation setProvider(UUID provider) {
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
	public UUID getLocation() {
		return getProperty("location");
	}


	/**
	* Changes the location of this Consultation
	* @param location The new location to set
	* @return Consultation
	*/
	public Consultation setLocation(UUID location) {
		setProperty("location", location);
		return this;
	}


	/**
	* Gets the patient of this consultation
	* @return patient
	*/
	public UUID getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this Consultation
	* @param patient The new patient to set
	* @return Consultation
	*/
	public Consultation setPatient(UUID patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the concept of this consultation
	* @return concept
	*/
	public UUID getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this Consultation
	* @param concept The new concept to set
	* @return Consultation
	*/
	public Consultation setConcept(UUID concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the original concept of this consultation
	* @return originalConcept
	*/
	public UUID getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this Consultation
	* @param originalConcept The new original concept to set
	* @return Consultation
	*/
	public Consultation setOriginalConcept(UUID originalConcept) {
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

