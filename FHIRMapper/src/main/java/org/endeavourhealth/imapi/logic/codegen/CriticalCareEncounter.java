package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents critical care encounter.
* An entry recording information about a criticial care encounter.<p>common data model attributes for Critical care encounter
*/
public class CriticalCareEncounter extends IMDMBase<CriticalCareEncounter> {


	/**
	* Critical care encounter constructor with identifier
	*/
	public CriticalCareEncounter(UUID id) {
		super("CriticalCareEncounter", id);
	}

	/**
	* Gets the has admission source of this critical care encounter
	* @return hasAdmissionSource
	*/
	public String getHasAdmissionSource() {
		return getProperty("hasAdmissionSource");
	}


	/**
	* Changes the has admission source of this CriticalCareEncounter
	* @param hasAdmissionSource The new has admission source to set
	* @return CriticalCareEncounter
	*/
	public CriticalCareEncounter setHasAdmissionSource(String hasAdmissionSource) {
		setProperty("hasAdmissionSource", hasAdmissionSource);
		return this;
	}


	/**
	* Gets the record owner of this critical care encounter
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this CriticalCareEncounter
	* @param recordOwner The new record owner to set
	* @return CriticalCareEncounter
	*/
	public CriticalCareEncounter setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the has critical care unit function of this critical care encounter
	* @return hasCriticalCareUnitFunction
	*/
	public String getHasCriticalCareUnitFunction() {
		return getProperty("hasCriticalCareUnitFunction");
	}


	/**
	* Changes the has critical care unit function of this CriticalCareEncounter
	* @param hasCriticalCareUnitFunction The new has critical care unit function to set
	* @return CriticalCareEncounter
	*/
	public CriticalCareEncounter setHasCriticalCareUnitFunction(String hasCriticalCareUnitFunction) {
		setProperty("hasCriticalCareUnitFunction", hasCriticalCareUnitFunction);
		return this;
	}


	/**
	* Gets the text of this critical care encounter
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this CriticalCareEncounter
	* @param text The new text to set
	* @return CriticalCareEncounter
	*/
	public CriticalCareEncounter setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the concept of this critical care encounter
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this CriticalCareEncounter
	* @param concept The new concept to set
	* @return CriticalCareEncounter
	*/
	public CriticalCareEncounter setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the patient of this critical care encounter
	* @return patient
	*/
	public UUID getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this CriticalCareEncounter
	* @param patient The new patient to set
	* @return CriticalCareEncounter
	*/
	public CriticalCareEncounter setPatient(UUID patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the original concept of this critical care encounter
	* @return originalConcept
	*/
	public UUID getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this CriticalCareEncounter
	* @param originalConcept The new original concept to set
	* @return CriticalCareEncounter
	*/
	public CriticalCareEncounter setOriginalConcept(UUID originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this critical care encounter
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this CriticalCareEncounter
	* @param effectiveDate The new effective date to set
	* @return CriticalCareEncounter
	*/
	public CriticalCareEncounter setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date of this critical care encounter
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this CriticalCareEncounter
	* @param endDate The new end date to set
	* @return CriticalCareEncounter
	*/
	public CriticalCareEncounter setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

