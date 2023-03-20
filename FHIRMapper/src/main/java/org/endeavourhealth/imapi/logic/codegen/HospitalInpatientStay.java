package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents hospital inpatient stay.
* An entry describing the stay of a patient in hospital. As well as spcialised properties it has sub component encounters of admission and discharge.<p>common data model attributes for Hospital inpatient stay
*/
public class HospitalInpatientStay extends IMDMBase<HospitalInpatientStay> {


	/**
	* Hospital inpatient stay constructor with identifier
	*/
	public HospitalInpatientStay(UUID id) {
		super("HospitalInpatientStay", id);
	}

	/**
	* Gets the administrative category of this hospital inpatient stay
	* @return administrativeCategory
	*/
	public String getAdministrativeCategory() {
		return getProperty("administrativeCategory");
	}


	/**
	* Changes the administrative category of this HospitalInpatientStay
	* @param administrativeCategory The new administrative category to set
	* @return HospitalInpatientStay
	*/
	public HospitalInpatientStay setAdministrativeCategory(String administrativeCategory) {
		setProperty("administrativeCategory", administrativeCategory);
		return this;
	}


	/**
	* Gets the record owner of this hospital inpatient stay
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this HospitalInpatientStay
	* @param recordOwner The new record owner to set
	* @return HospitalInpatientStay
	*/
	public HospitalInpatientStay setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the treatment function type of this hospital inpatient stay
	* @return treatmentFunctionType
	*/
	public String getTreatmentFunctionType() {
		return getProperty("treatmentFunctionType");
	}


	/**
	* Changes the treatment function type of this HospitalInpatientStay
	* @param treatmentFunctionType The new treatment function type to set
	* @return HospitalInpatientStay
	*/
	public HospitalInpatientStay setTreatmentFunctionType(String treatmentFunctionType) {
		setProperty("treatmentFunctionType", treatmentFunctionType);
		return this;
	}


	/**
	* Gets the text of this hospital inpatient stay
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this HospitalInpatientStay
	* @param text The new text to set
	* @return HospitalInpatientStay
	*/
	public HospitalInpatientStay setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the concept of this hospital inpatient stay
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this HospitalInpatientStay
	* @param concept The new concept to set
	* @return HospitalInpatientStay
	*/
	public HospitalInpatientStay setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the patient of this hospital inpatient stay
	* @return patient
	*/
	public UUID getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this HospitalInpatientStay
	* @param patient The new patient to set
	* @return HospitalInpatientStay
	*/
	public HospitalInpatientStay setPatient(UUID patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the original concept of this hospital inpatient stay
	* @return originalConcept
	*/
	public UUID getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this HospitalInpatientStay
	* @param originalConcept The new original concept to set
	* @return HospitalInpatientStay
	*/
	public HospitalInpatientStay setOriginalConcept(UUID originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this hospital inpatient stay
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this HospitalInpatientStay
	* @param effectiveDate The new effective date to set
	* @return HospitalInpatientStay
	*/
	public HospitalInpatientStay setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date of this hospital inpatient stay
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this HospitalInpatientStay
	* @param endDate The new end date to set
	* @return HospitalInpatientStay
	*/
	public HospitalInpatientStay setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

