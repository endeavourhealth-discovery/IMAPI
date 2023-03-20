package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents family history.
* Observations relating to family history of conditions
*/
public class FamilyHistory extends IMDMBase<FamilyHistory> {


	/**
	* Family history constructor with identifier
	*/
	public FamilyHistory(UUID id) {
		super("FamilyHistory", id);
	}

	/**
	* Gets the concept of this family history
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this FamilyHistory
	* @param concept The new concept to set
	* @return FamilyHistory
	*/
	public FamilyHistory setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the record owner of this family history
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this FamilyHistory
	* @param recordOwner The new record owner to set
	* @return FamilyHistory
	*/
	public FamilyHistory setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this family history
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this FamilyHistory
	* @param text The new text to set
	* @return FamilyHistory
	*/
	public FamilyHistory setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the patient of this family history
	* @return patient
	*/
	public UUID getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this FamilyHistory
	* @param patient The new patient to set
	* @return FamilyHistory
	*/
	public FamilyHistory setPatient(UUID patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the original concept of this family history
	* @return originalConcept
	*/
	public UUID getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this FamilyHistory
	* @param originalConcept The new original concept to set
	* @return FamilyHistory
	*/
	public FamilyHistory setOriginalConcept(UUID originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this family history
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this FamilyHistory
	* @param effectiveDate The new effective date to set
	* @return FamilyHistory
	*/
	public FamilyHistory setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date of this family history
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this FamilyHistory
	* @param endDate The new end date to set
	* @return FamilyHistory
	*/
	public FamilyHistory setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

