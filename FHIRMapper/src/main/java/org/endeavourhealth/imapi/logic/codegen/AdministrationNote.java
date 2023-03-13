package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents administration note
* An record of a note or report or document involved in health care. Examples would be the filing of a lab report or discharge letter
*/
public class AdministrationNote extends IMDMBase<AdministrationNote> {


	/**
	* Administration note constructor 
	*/
	public AdministrationNote() {
		super("AdministrationNote");
	}

	/**
	* Administration note constructor with identifier
	*/
	public AdministrationNote(String id) {
		super("AdministrationNote", id);
	}

	/**
	* Gets the concept of this administration note
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this AdministrationNote
	* @param concept The new concept to set
	* @return AdministrationNote
	*/
	public AdministrationNote setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the record owner of this administration note
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this AdministrationNote
	* @param recordOwner The new record owner to set
	* @return AdministrationNote
	*/
	public AdministrationNote setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this administration note
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this AdministrationNote
	* @param text The new text to set
	* @return AdministrationNote
	*/
	public AdministrationNote setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the patient of this administration note
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this AdministrationNote
	* @param patient The new patient to set
	* @return AdministrationNote
	*/
	public AdministrationNote setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the original concept of this administration note
	* @return originalConcept
	*/
	public TerminologyConcept getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this AdministrationNote
	* @param originalConcept The new original concept to set
	* @return AdministrationNote
	*/
	public AdministrationNote setOriginalConcept(TerminologyConcept originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this administration note
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this AdministrationNote
	* @param effectiveDate The new effective date to set
	* @return AdministrationNote
	*/
	public AdministrationNote setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date of this administration note
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this AdministrationNote
	* @param endDate The new end date to set
	* @return AdministrationNote
	*/
	public AdministrationNote setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

