package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents flag.
* A flag is a warning or notification of some sort presented to the user - who may be a clinician or some other person involve in patient care. It usually represents something of sufficient significance to be warrant a special display of some sort - rather than just a note in an entry
*/
public class Flag extends IMDMBase<Flag> {


	/**
	* Flag constructor with identifier
	*/
	public Flag(UUID id) {
		super("Flag", id);
	}

	/**
	* Gets the flag category of this flag
	* @return flagCategory
	*/
	public String getFlagCategory() {
		return getProperty("flagCategory");
	}


	/**
	* Changes the flag category of this Flag
	* @param flagCategory The new flag category to set
	* @return Flag
	*/
	public Flag setFlagCategory(String flagCategory) {
		setProperty("flagCategory", flagCategory);
		return this;
	}


	/**
	* Gets the record owner of this flag
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this Flag
	* @param recordOwner The new record owner to set
	* @return Flag
	*/
	public Flag setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this flag
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this Flag
	* @param text The new text to set
	* @return Flag
	*/
	public Flag setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the flag type of this flag
	* @return flagType
	*/
	public String getFlagType() {
		return getProperty("flagType");
	}


	/**
	* Changes the flag type of this Flag
	* @param flagType The new flag type to set
	* @return Flag
	*/
	public Flag setFlagType(String flagType) {
		setProperty("flagType", flagType);
		return this;
	}


	/**
	* Gets the linked entry of this flag
	* @return linkedEntry
	*/
	public String getLinkedEntry() {
		return getProperty("linkedEntry");
	}


	/**
	* Changes the linked entry of this Flag
	* @param linkedEntry The new linked entry to set
	* @return Flag
	*/
	public Flag setLinkedEntry(String linkedEntry) {
		setProperty("linkedEntry", linkedEntry);
		return this;
	}


	/**
	* Gets the patient of this flag
	* @return patient
	*/
	public UUID getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this Flag
	* @param patient The new patient to set
	* @return Flag
	*/
	public Flag setPatient(UUID patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the concept of this flag
	* @return concept
	*/
	public UUID getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this Flag
	* @param concept The new concept to set
	* @return Flag
	*/
	public Flag setConcept(UUID concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the original concept of this flag
	* @return originalConcept
	*/
	public UUID getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this Flag
	* @param originalConcept The new original concept to set
	* @return Flag
	*/
	public Flag setOriginalConcept(UUID originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this flag
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this Flag
	* @param effectiveDate The new effective date to set
	* @return Flag
	*/
	public Flag setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date of this flag
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this Flag
	* @param endDate The new end date to set
	* @return Flag
	*/
	public Flag setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

