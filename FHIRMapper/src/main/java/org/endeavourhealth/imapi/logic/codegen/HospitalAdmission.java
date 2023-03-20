package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents hospital admission.
* An entry recording the event of a hospital admission. Has specialised properties.<p>common data model attributes for Hospital admission
*/
public class HospitalAdmission extends IMDMBase<HospitalAdmission> {


	/**
	* Hospital admission constructor with identifier
	*/
	public HospitalAdmission(UUID id) {
		super("HospitalAdmission", id);
	}

	/**
	* Gets the administrative category of this hospital admission
	* @return administrativeCategory
	*/
	public String getAdministrativeCategory() {
		return getProperty("administrativeCategory");
	}


	/**
	* Changes the administrative category of this HospitalAdmission
	* @param administrativeCategory The new administrative category to set
	* @return HospitalAdmission
	*/
	public HospitalAdmission setAdministrativeCategory(String administrativeCategory) {
		setProperty("administrativeCategory", administrativeCategory);
		return this;
	}


	/**
	* Gets the record owner of this hospital admission
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this HospitalAdmission
	* @param recordOwner The new record owner to set
	* @return HospitalAdmission
	*/
	public HospitalAdmission setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the has admission classification of patient of this hospital admission
	* @return hasAdmissionClassificationOfPatient
	*/
	public String getHasAdmissionClassificationOfPatient() {
		return getProperty("hasAdmissionClassificationOfPatient");
	}


	/**
	* Changes the has admission classification of patient of this HospitalAdmission
	* @param hasAdmissionClassificationOfPatient The new has admission classification of patient to set
	* @return HospitalAdmission
	*/
	public HospitalAdmission setHasAdmissionClassificationOfPatient(String hasAdmissionClassificationOfPatient) {
		setProperty("hasAdmissionClassificationOfPatient", hasAdmissionClassificationOfPatient);
		return this;
	}


	/**
	* Gets the text of this hospital admission
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this HospitalAdmission
	* @param text The new text to set
	* @return HospitalAdmission
	*/
	public HospitalAdmission setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the has admission method of this hospital admission
	* @return hasAdmissionMethod
	*/
	public String getHasAdmissionMethod() {
		return getProperty("hasAdmissionMethod");
	}


	/**
	* Changes the has admission method of this HospitalAdmission
	* @param hasAdmissionMethod The new has admission method to set
	* @return HospitalAdmission
	*/
	public HospitalAdmission setHasAdmissionMethod(String hasAdmissionMethod) {
		setProperty("hasAdmissionMethod", hasAdmissionMethod);
		return this;
	}


	/**
	* Gets the patient of this hospital admission
	* @return patient
	*/
	public UUID getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this HospitalAdmission
	* @param patient The new patient to set
	* @return HospitalAdmission
	*/
	public HospitalAdmission setPatient(UUID patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the has admission source of this hospital admission
	* @return hasAdmissionSource
	*/
	public String getHasAdmissionSource() {
		return getProperty("hasAdmissionSource");
	}


	/**
	* Changes the has admission source of this HospitalAdmission
	* @param hasAdmissionSource The new has admission source to set
	* @return HospitalAdmission
	*/
	public HospitalAdmission setHasAdmissionSource(String hasAdmissionSource) {
		setProperty("hasAdmissionSource", hasAdmissionSource);
		return this;
	}


	/**
	* Gets the original concept of this hospital admission
	* @return originalConcept
	*/
	public UUID getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this HospitalAdmission
	* @param originalConcept The new original concept to set
	* @return HospitalAdmission
	*/
	public HospitalAdmission setOriginalConcept(UUID originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the is component of of this hospital admission
	* @return isComponentOf
	*/
	public UUID getIsComponentOf() {
		return getProperty("isComponentOf");
	}


	/**
	* Changes the is component of of this HospitalAdmission
	* @param isComponentOf The new is component of to set
	* @return HospitalAdmission
	*/
	public HospitalAdmission setIsComponentOf(UUID isComponentOf) {
		setProperty("isComponentOf", isComponentOf);
		return this;
	}


	/**
	* Gets the effective date of this hospital admission
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this HospitalAdmission
	* @param effectiveDate The new effective date to set
	* @return HospitalAdmission
	*/
	public HospitalAdmission setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the concept of this hospital admission
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this HospitalAdmission
	* @param concept The new concept to set
	* @return HospitalAdmission
	*/
	public HospitalAdmission setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the end date of this hospital admission
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this HospitalAdmission
	* @param endDate The new end date to set
	* @return HospitalAdmission
	*/
	public HospitalAdmission setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

