package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents observation
* A point in time event entry or note in a record of some characteristic
*/
public class Observation extends IMDMBase<Observation> {


	/**
	* Observation constructor 
	*/
	public Observation() {
		super("Observation");
	}

	/**
	* Observation constructor with identifier
	*/
	public Observation(String id) {
		super("Observation", id);
	}

	/**
	* Gets the concept of this observation
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this Observation
	* @param concept The new concept to set
	* @return Observation
	*/
	public Observation setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the record owner of this observation
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this Observation
	* @param recordOwner The new record owner to set
	* @return Observation
	*/
	public Observation setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this observation
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this Observation
	* @param text The new text to set
	* @return Observation
	*/
	public Observation setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the patient of this observation
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this Observation
	* @param patient The new patient to set
	* @return Observation
	*/
	public Observation setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the original concept of this observation
	* @return originalConcept
	*/
	public TerminologyConcept getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this Observation
	* @param originalConcept The new original concept to set
	* @return Observation
	*/
	public Observation setOriginalConcept(TerminologyConcept originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this observation
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this Observation
	* @param effectiveDate The new effective date to set
	* @return Observation
	*/
	public Observation setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date of this observation
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this Observation
	* @param endDate The new end date to set
	* @return Observation
	*/
	public Observation setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

